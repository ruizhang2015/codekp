/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-14
 * @modifier: a
 * @time 2010-1-14
 * @reviewer: a
 * @time 2010-1-14
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.jir;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.TryCatchBlockNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.tree.analysis.BasicValue;
import org.objectweb.asm.tree.analysis.Frame;


import edu.pku.cn.classfile.LocalVariableNode;
import edu.pku.cn.util.OpcodeUtil;

/**
 * @author a
 */
public class JIRTransfer {	
	HashMap<Integer,List<LocalVariableNode>> localMap=new HashMap<Integer, List<LocalVariableNode>>();
	public void putLocals(List<LocalVariableNode> locals){
		if(locals!=null)
		for(int i=0;i<locals.size();i++){
			LocalVariableNode node=locals.get(i);
			List<LocalVariableNode> nodes=localMap.get(node.index);
			if(nodes==null){
				nodes=new ArrayList<LocalVariableNode>();
				nodes.add(node);
				localMap.put(node.index, nodes);				
			}
			else
				nodes.add(node);
		}
	}
	HashSet<LocalVariableNode> paramSet=new HashSet<LocalVariableNode>();
	public void putParams(List<LocalVariableNode> params){
		if(params!=null)
		paramSet.addAll(params);
	}
	String handleType=null;
	HashMap<LabelNode,String> handles=new HashMap<LabelNode, String>();
	public void putHandles(List<TryCatchBlockNode> handles){
		Iterator<TryCatchBlockNode> iter=handles.iterator();
		while(iter.hasNext()){
			TryCatchBlockNode node=iter.next();
			this.handles.put(node.handler, node.type);
		}
	}
//	public LocalVariableNode getLocal(int index,int var){
//		List<LocalVariableNode> nodes=localMap.get(var);
//		for(int i=0;i<nodes.size();i++){
//			LocalVariableNode node=nodes.get(i);
//			if(nodes.size()==1 ||node.equals(index, var))
//				return node;
//		}
//		throw new RuntimeException("find no local here in JIRTransfer");
//	}
	
	public Type getTypeOfAtype(int atype){
		switch(atype){
		case 4:
			return Type.BOOLEAN_TYPE;
		case 5:
			return Type.CHAR_TYPE;
		case 6:
			return Type.FLOAT_TYPE;
		case 7:
			return Type.DOUBLE_TYPE;
		case 8:
			return Type.BYTE_TYPE;
		case 9:
			return Type.SHORT_TYPE;
		case 10:
			return Type.INT_TYPE;
		case 11:
			return Type.LONG_TYPE;
			default:
				throw new RuntimeException();
		}
	}
	private Type getStackType(Frame frame,int index){
		return ((BasicValue)frame.getStack(frame.getStackSize()-1+index)).getType();
	}
	private StackRef newStackRef(Frame frame,int index){
		int size=frame.getStackSize()-1+index;
		Type type=((BasicValue)frame.getStack(size)).getType();
		return new StackRef(size,type);
	}
	private StackRef newStackRef(Frame frame,Type type){
		return new StackRef(frame.getStackSize()-1,type);
	}
	public void addStmt(List<Stmt> stmts,Stmt stmt,int index){
		stmt.setIndex(stmts.size());
		stmt.setRegion(index, index);		
		stmts.add(stmt);
	}
	HashMap<LabelNode,LabelStmt> labelMap=new HashMap<LabelNode, LabelStmt>();
	private LabelStmt getLabelStmt(LabelNode index){
		LabelStmt stmt=labelMap.get(index);
		if(stmt==null){
			stmt=new LabelStmt(index.index);
			stmt.label=index;
			labelMap.put(index, stmt);
		}
		return stmt;
	}
	HashMap<LocalVariableNode,Ref> localRef=new HashMap<LocalVariableNode, Ref>();
	public int tempIndex;
	private Ref getLocal(LocalVariableNode node){
		Ref local=localRef.get(node);
		if(local==null){
			if(paramSet.contains(node)){
				local=new ParamRef(node);
			}else
				local=new LocalRef(node);
			localRef.put(node, local);
		}
		return local;
	}
	HashMap<Integer,Ref> tempCache=new HashMap<Integer, Ref>();
	private Ref getTemp(int var,Type type,boolean isStore){
		Ref temp=tempCache.get(var);
		if(temp==null || !type.equals(temp.getType())|| isStore){
			temp=new TempRef(tempIndex++, type);
			tempCache.put(var, temp);
		}
		return temp;
	}

	private Ref getLocal(int index,int var,Frame frame,boolean isStore){
		List<LocalVariableNode> nodes=localMap.get(var);
		if(nodes!=null)
		for(int i=0;i<nodes.size();i++){
			LocalVariableNode node=nodes.get(i);
			if(node.equals(index, var))
				return getLocal(node);
		}
		Type type=((BasicValue)frame.getLocal(var)).getType();
		if(type==null)try{
			type=((BasicValue)frame.getStack(frame.getStackSize())).getType();
		}catch(Exception e){
			System.err.println(frame.toString());			
		}
		if(type==null)
			type=Type.NULL;
		return getTemp(var,type,isStore);
		//throw new RuntimeException("find no local here in JIRTransfer");
	}
	public void generateJIR(AbstractInsnNode insn,Frame frame,Frame postFrame,List<Stmt> stmts){
		AbstractStmt stmt;
		JIRValue rvalue;
		int opcode=insn.getOpcode();
		if(opcode<0){
			if(insn instanceof LabelNode ){
				stmt=getLabelStmt((LabelNode)insn);			
				handleType=this.handles.get(insn);
			}
			else if(insn instanceof LineNumberNode){
				stmt=new LineStmt(((LineNumberNode)insn).line);
			}
			else{
				if(handleType!=null)
					stmt=new AssignStmt(newStackRef(frame,0), new CaughtExceptionRef());
				else
					stmt=null;
			}
		}
		else{
	      switch(opcode)
	      {
	         case Opcodes.BIPUSH:
	         case Opcodes.SIPUSH:
	        	 rvalue=new IntConstant(((IntInsnNode)insn).operand);
	        	 stmt =new AssignStmt(newStackRef(postFrame,Type.INT_TYPE), rvalue);	            	
	            break;
	         case Opcodes.LDC:
	        	 rvalue=new StringConstant(((LdcInsnNode)insn).cst.toString());
	        	 stmt =new AssignStmt(newStackRef(postFrame,rvalue.getType()), rvalue);
	            break;
	         case Opcodes.ACONST_NULL:
	        	 stmt =new AssignStmt(newStackRef(postFrame,Type.OBJECT_TYPE), Null.NULL);
	            break;
	         case Opcodes.ICONST_M1:
	         case Opcodes.ICONST_0:
	         case Opcodes.ICONST_1:
	         case Opcodes.ICONST_2:
	         case Opcodes.ICONST_3:
	         case Opcodes.ICONST_4:
	         case Opcodes.ICONST_5:
	            rvalue = IntConstant.v(opcode-Opcodes.ICONST_0);
	            stmt =new AssignStmt(newStackRef(postFrame,0), rvalue);
	            break;
	         case Opcodes.LCONST_0:
	         case Opcodes.LCONST_1:
	            rvalue = LongConstant.v(opcode-Opcodes.LCONST_0);
	            stmt =new AssignStmt(newStackRef(postFrame,0), rvalue);
	            break;
	         case Opcodes.FCONST_0:
	         case Opcodes.FCONST_1:
	         case Opcodes.FCONST_2:
	            rvalue = FloatConstant.v(opcode - Opcodes.FCONST_0);
	            stmt =new AssignStmt(newStackRef(postFrame,0), rvalue);
	            break;

	         case Opcodes.DCONST_0:
	         case Opcodes.DCONST_1:
	            rvalue = DoubleConstant.v(opcode-Opcodes.DCONST_0);
	            stmt =new AssignStmt(newStackRef(postFrame,0), rvalue);
	            break;
	         case Opcodes.ILOAD:
	         {
	            stmt = new AssignStmt(newStackRef(postFrame,0), getLocal(insn.index,((VarInsnNode)insn).var,postFrame,false));
	            break;
	         }
	         case Opcodes.LLOAD:
	         case Opcodes.FLOAD:
	         case Opcodes.DLOAD:
	         case Opcodes.ALOAD:
	         {
	        	 Ref ref=getLocal(insn.index,((VarInsnNode)insn).var,postFrame,false);
	        	 stmt = new AssignStmt(newStackRef(postFrame,ref.getType()),ref );
	            break;
	         }
	         case Opcodes.ISTORE:
	         {
	        	 stmt = new AssignStmt(getLocal(insn.index+1,((VarInsnNode)insn).var,postFrame,true),newStackRef(frame,0),true);
	            break;
	         }
	         case Opcodes.LSTORE:
	         case Opcodes.FSTORE:
	         case Opcodes.DSTORE:
	         case Opcodes.ASTORE:
	         {
	        	 Ref ref=getLocal(insn.index+1,((VarInsnNode)insn).var,postFrame,true);
	        	 stmt = new AssignStmt(ref,newStackRef(frame,ref.getType()),true);	      
	            break;
	         }
	         case Opcodes.IINC:	        	 
	         {
	        	 Ref ref=getLocal(insn.index, ((IincInsnNode)insn).var,postFrame,false);
	        	 Expr add=new BinopExpr(ref, new IntConstant(((IincInsnNode)insn).incr), BinopExpr.ADD);
	        	 stmt=new AssignStmt(ref,add);
	            break;
	         }	         
	         case Opcodes.NEWARRAY:
	         {
	        	 IntInsnNode intinsn=(IntInsnNode)insn;
	        	 Type baseType=getTypeOfAtype(intinsn.operand);	   
	        	 JIRValue[] size=new JIRValue[1];
	        	 size[0]=new StackRef(frame.getStackSize()-1,Type.INT_TYPE);
	        	 Expr newa=new NewArrayExpr(baseType, size);
	        	 stmt=new AssignStmt(newStackRef(postFrame, 0),newa);
	            break;
	         }
	         case Opcodes.ANEWARRAY:
	         {
	        	 TypeInsnNode typeinsn=(TypeInsnNode)insn;
	        	 Type baseType=Type.getObjectType(typeinsn.desc);
	        	 JIRValue[] size=new JIRValue[1];	        	 
	        	 size[0]=new StackRef(frame.getStackSize()-1,Type.INT_TYPE);
	        	 Expr newa=new NewArrayExpr(baseType, size);
	        	 stmt=new AssignStmt(newStackRef(postFrame,0),newa);	          
	            break;
	         }
	         case Opcodes.MULTIANEWARRAY:
	         {
	        	 MultiANewArrayInsnNode typeinsn=(MultiANewArrayInsnNode)insn;
	        	 JIRValue[] size=new JIRValue[typeinsn.dims];
	        	 int stacksize=frame.getStackSize();
	        	 for(int i=0;i<size.length;i++)
	        		 size[i]=new StackRef(stacksize-size.length+i,Type.INT_TYPE);
	        	 Expr newa=new NewArrayExpr(Type.getObjectType(typeinsn.desc), size);
	        	 stmt=new AssignStmt(newStackRef(postFrame,0),newa);
	        	 break;
	         }
	         case Opcodes.ARRAYLENGTH:
	         {
	        	 Expr len=new LengthOfExpr(newStackRef(frame,0));
	        	 stmt = new AssignStmt(newStackRef(postFrame,Type.INT_TYPE), len);
	        	 break;
	         }
	         case Opcodes.IALOAD:
	         case Opcodes.BALOAD:
	         case Opcodes.CALOAD:
	         case Opcodes.SALOAD:
	         case Opcodes.FALOAD:
	         case Opcodes.LALOAD:
	         case Opcodes.DALOAD:
	         case Opcodes.AALOAD:
	         {
//	        	 Ref array=new ArrayRef(newStackRef(frame,-1),newStackRef(frame,0));	        	 
	        	 stmt=new AssignStmt(newStackRef(postFrame,0),ArrayRef.e);
	        	 break;
	         }
	         case Opcodes.IASTORE:
	         case Opcodes.FASTORE:
	         case Opcodes.AASTORE:
	         case Opcodes.BASTORE:
	         case Opcodes.CASTORE:
	         case Opcodes.SASTORE:
	         case Opcodes.LASTORE://special check?-3,-2
	         case Opcodes.DASTORE://special check?-3,-2
	         {
//	        	 Ref array=new ArrayRef(newStackRef(frame,-2), 
//	        			 newStackRef(frame,-1));
	        	 stmt=new AssignStmt(ArrayRef.e,newStackRef(frame,0));
	        	 break;
	         }
	         case Opcodes.NOP:
		            stmt = null;//nop
		            break;
	         case Opcodes.POP:	        	 
	         case Opcodes.POP2:
	            stmt = new PopStmt(opcode-Opcodes.POP2+2);//nop
	            break;
	         case Opcodes.DUP:
	         {
	        	 Type type=getStackType(frame,0);
	        	 if(type.getSize()==2)
	        		 throw new RuntimeException("dup size==2 in JIRTransfer");
	        	 stmt=new AssignStmt(true,newStackRef(postFrame,0),
	        			 newStackRef(frame,0));
		            break;
	         }
	         case Opcodes.DUP2:
	            //if(frame.getStack(frame.getStackSize()).getSize() == 2)
	            {
	            	Type type=getStackType(frame,0);
	            	if(type.getSize()==2)
	            		stmt=new AssignStmt(true,newStackRef(postFrame,0),
		            			newStackRef(frame,0));
	            	else{
	            		addStmt(stmts,new AssignStmt(true,newStackRef(postFrame,-1),
	        			 newStackRef(frame,-1)),insn.index);
	            		stmt=new AssignStmt(true,newStackRef(postFrame,0),newStackRef(frame,0));
	            	}
	            	break;
	            }	            
	         case Opcodes.DUP_X1:
	         {
	        	 StackRef word1=newStackRef(frame,0);
	        	 StackRef word2=newStackRef(frame,-1);
	        	 StackRef word3=newStackRef(postFrame,0);
	        	 addStmt(stmts,new AssignStmt(true,word3,word1),insn.index);
	        	 addStmt(stmts,new AssignStmt(true,word1,word2),insn.index);
	        	 stmt=new AssignStmt(true,word2,word3);
	         }
	            break;
	         case Opcodes.DUP_X2:
	         {
	        	 Type type=getStackType(frame,-1);
	        	 StackRef word1=newStackRef(frame,0);
	        	 if(type.getSize()==2){
	        		 StackRef word2=newStackRef(frame,type);
	        		 addStmt(stmts,new AssignStmt(true,newStackRef(postFrame,0),word1),insn.index);
	        		 addStmt(stmts,new AssignStmt(true,newStackRef(postFrame,-1),word2),insn.index);
	        		 stmt=new AssignStmt(true,newStackRef(postFrame,-2),word1);
	        	 }else{
	        		 StackRef word2=newStackRef(frame,-1);
	        		 StackRef word3=newStackRef(frame,-2);
	        		 StackRef word4=newStackRef(postFrame,0);
	        		 addStmt(stmts,new AssignStmt(true,word4,word1),insn.index);
	        		 addStmt(stmts,new AssignStmt(true,newStackRef(postFrame,-1),word2),insn.index);
	        		 addStmt(stmts,new AssignStmt(true,newStackRef(postFrame,-2),word3),insn.index);
	        		 stmt=new AssignStmt(true,newStackRef(postFrame,-3),word4);
	        	 }
	        	 break;
	         }
	        case Opcodes.DUP2_X1:
	        {
	        	Type type=getStackType(frame,0);	        	
	        	if(type.getSize()==2){
	        		StackRef word1=newStackRef(frame,0);
	        		StackRef word3=newStackRef(frame,-1);
	        		StackRef word0=newStackRef(postFrame,0);
	        		addStmt(stmts,new AssignStmt(true,word0,word1),insn.index);
	        		addStmt(stmts,new AssignStmt(true,newStackRef(postFrame,-1),word3),insn.index);
	        		stmt=new AssignStmt(true,newStackRef(postFrame,-2), word0);
	        	}else{
	        		StackRef word1=newStackRef(frame,0);
	        		StackRef word2=newStackRef(frame,-1);
	        		StackRef word3=newStackRef(frame,-2);
	        		StackRef word4=newStackRef(postFrame,-1);
	        		StackRef word5=newStackRef(postFrame,0);
	        		addStmt(stmts,new AssignStmt(true,word5,word1),insn.index);
	        		addStmt(stmts,new AssignStmt(true,word4,word2),insn.index);
	        		addStmt(stmts,new AssignStmt(true,newStackRef(postFrame,-2),word3),insn.index);
	        		addStmt(stmts,new AssignStmt(true,newStackRef(postFrame,-3),word5),insn.index);
	        		stmt=new AssignStmt(true,newStackRef(postFrame,-4),word4);
	        	}
	        	break;
	        }
	         case Opcodes.DUP2_X2:
	         {
	        	 Type type=getStackType(frame,0);
	        	 if(type.getSize()==2){
	        		 type=getStackType(frame,-1);
	        		 StackRef word1=newStackRef(frame,0);
	        		 StackRef word0=newStackRef(postFrame,0);
	        		 addStmt(stmts,new AssignStmt(true,word0,word1),insn.index);
	        		 addStmt(stmts,new AssignStmt(true,newStackRef(postFrame,-1),newStackRef(frame,-1)),insn.index);
	        		 if(type.getSize()==2){	        			 
	        			 stmt=new AssignStmt(true,newStackRef(postFrame,-2),word0);
	        		 }else{
	        			 addStmt(stmts,new AssignStmt(true,newStackRef(postFrame,-2),newStackRef(frame,-2)),insn.index);
	        			 stmt=new AssignStmt(true,newStackRef(postFrame,-3),word0);
	        		 }
	        	 }else{
	        		 type=getStackType(frame,-2);
	        		 StackRef word1=newStackRef(frame,0);
	        		 StackRef word2=newStackRef(frame,-1);
	        		 StackRef word3=newStackRef(frame,-2);
	        		 StackRef word10=newStackRef(postFrame,0);
	        		 StackRef word20=newStackRef(postFrame,-1);
	        		 addStmt(stmts,new AssignStmt(true,word10,word1),insn.index);
	        		 addStmt(stmts,new AssignStmt(true,word20,word2),insn.index);
	        		 addStmt(stmts,new AssignStmt(true,newStackRef(postFrame,-2),word3),insn.index);
	        		 if(type.getSize()==2){
	        			 addStmt(stmts,new AssignStmt(true,newStackRef(postFrame,-3),word10),insn.index);
	        			 stmt=new AssignStmt(true,newStackRef(postFrame,-4),word20);
	        		 }else{
	        			 addStmt(stmts,new AssignStmt(true,newStackRef(postFrame,-3),newStackRef(frame,-3)),insn.index);
	        			 addStmt(stmts,new AssignStmt(true,newStackRef(postFrame,-4),word10),insn.index);
	        			 stmt=new AssignStmt(true,newStackRef(postFrame,-5),word20);
	        		 }
	        	 }
	        	 break;
	         }
	         case Opcodes.SWAP:
	         {
	        	 StackRef word1=newStackRef(frame,0);
	        	 StackRef word2=newStackRef(frame,-1);
	        	 if(word1.getType().getSize()==2 || word2.getType().getSize()==2)
	        		 throw new RuntimeException("swap a 2size in JIRTransfer");
	        	 StackRef word3=new StackRef(frame.getStackSize(),word1.getType());
	        	 addStmt(stmts,new AssignStmt(true,word3,word2),insn.index);
	        	 addStmt(stmts,new AssignStmt(true,word2,word1),insn.index);
	        	 addStmt(stmts,new AssignStmt(true,word1,word3),insn.index);
	        	 stmt=new PopStmt(0);
	        	 break;	            
	         }
	         case Opcodes.FADD:
	         case Opcodes.IADD:
	         case Opcodes.DADD:
	         case Opcodes.LADD:
	         {
	        	 Expr add=new BinopExpr(newStackRef(frame,-1), newStackRef(frame,0), BinopExpr.ADD);
	        	 stmt=new AssignStmt(newStackRef(postFrame,0),add);
	        	 break;
	         }
	         case Opcodes.FSUB:
	         case Opcodes.ISUB:
	         case Opcodes.DSUB:
	         case Opcodes.LSUB:
	         {
	        	 Expr add=new BinopExpr(newStackRef(frame,-1), newStackRef(frame,0), BinopExpr.SUB);
	        	 stmt=new AssignStmt(newStackRef(postFrame,0),add);
	        	 break;
	         }
	         case Opcodes.FMUL:
	         case Opcodes.IMUL:
	         case Opcodes.DMUL:
	         case Opcodes.LMUL:
	         {
	        	 Expr add=new BinopExpr(newStackRef(frame,-1), newStackRef(frame,0), BinopExpr.MUL);
	        	 stmt=new AssignStmt(newStackRef(postFrame,0),add);
	        	 break;
	         }
	         case Opcodes.FDIV:
	         case Opcodes.IDIV:
	         case Opcodes.DDIV:
	         case Opcodes.LDIV:
	         {
	        	 Expr add=new BinopExpr(newStackRef(frame,-1), newStackRef(frame,0), BinopExpr.DIV);
	        	 stmt=new AssignStmt(newStackRef(postFrame,0),add);
	        	 break;
	         }
	         case Opcodes.FREM:
	         case Opcodes.IREM:
	         case Opcodes.DREM:
	         case Opcodes.LREM:
	         {
	        	 Expr add=new BinopExpr(newStackRef(frame,-1), newStackRef(frame,0), BinopExpr.REMAINDER);
	        	 stmt=new AssignStmt(newStackRef(postFrame,0),add);
	        	 break;
	         }
	         case Opcodes.INEG:
	         case Opcodes.LNEG:
	         case Opcodes.FNEG:
	         case Opcodes.DNEG:
	         {
	        	 Expr add=new NegExpr(newStackRef(frame,0));
	        	 stmt=new AssignStmt(newStackRef(postFrame,0),add);
	        	 break;
	         }

	         case Opcodes.ISHL:
	         case Opcodes.LSHL:
	         {
	        	 Expr add=new BinopExpr(newStackRef(frame,-1), newStackRef(frame,0), BinopExpr.SHIFTLEFT);
	        	 stmt=new AssignStmt(newStackRef(postFrame,0),add);
	        	 break;
	         }
	         case Opcodes.ISHR:
	         case Opcodes.LSHR:
	         {
	        	 Expr add=new BinopExpr(newStackRef(frame,-1), newStackRef(frame,0), BinopExpr.SHIFTRIGHT);
	        	 stmt=new AssignStmt(newStackRef(postFrame,0),add);
	        	 break;
	         }
	         case Opcodes.IUSHR:
	         case Opcodes.LUSHR:
	         {
	        	 Expr add=new BinopExpr(newStackRef(frame,-1), newStackRef(frame,0), BinopExpr.UNSIGNEDSHIFTRIGHT);
	        	 stmt=new AssignStmt(newStackRef(postFrame,0),add);
	        	 break;
	         }
	         case Opcodes.IAND:
	         case Opcodes.LAND:
	         {
	        	 Expr add=new BinopExpr(newStackRef(frame,-1), newStackRef(frame,0), BinopExpr.AND);
	        	 stmt=new AssignStmt(newStackRef(postFrame,0),add);
	        	 break;
	         }
	         case Opcodes.IOR:
	         case Opcodes.LOR:
	         {
	        	 Expr add=new BinopExpr(newStackRef(frame,-1), newStackRef(frame,0), BinopExpr.OR);
	        	 stmt=new AssignStmt(newStackRef(postFrame,0),add);
	        	 break;
	         }
	         case Opcodes.IXOR:
	         case Opcodes.LXOR:
	         {
	        	 Expr add=new BinopExpr(newStackRef(frame,-1), newStackRef(frame,0), BinopExpr.XOR);
	        	 stmt=new AssignStmt(newStackRef(postFrame,0),add);
	        	 break;
	         }
	         case Opcodes.D2L:
	         case Opcodes.F2L:
	         case Opcodes.I2L:
	         {
	        	 Expr cast=new CastExpr(newStackRef(frame,0), Type.LONG_TYPE);
	        	 stmt=new AssignStmt(newStackRef(postFrame,0),cast);
	        	 break;
	         }
	         case Opcodes.D2F:
	         case Opcodes.L2F:
	         case Opcodes.I2F:
	         {
	        	 Expr cast=new CastExpr(newStackRef(frame,0), Type.FLOAT_TYPE);
	        	 stmt=new AssignStmt(newStackRef(postFrame,0),cast);
	        	 break;
	         }
	         case Opcodes.I2D:
	         case Opcodes.L2D:
	         case Opcodes.F2D:
	         {
	        	 Expr cast=new CastExpr(newStackRef(frame,0), Type.DOUBLE_TYPE);
	        	 stmt=new AssignStmt(newStackRef(postFrame,0),cast);
	        	 break;
	         }
	         case Opcodes.L2I:
	         case Opcodes.F2I:
	         case Opcodes.D2I:
	         {
	        	 Expr cast=new CastExpr(newStackRef(frame,0), Type.INT_TYPE);
	        	 stmt=new AssignStmt(newStackRef(postFrame,0),cast);
	        	 break;
	         }
	         case Opcodes.I2B:
	         {
	        	 Expr cast=new CastExpr(newStackRef(frame,0), Type.BYTE_TYPE);
	        	 stmt=new AssignStmt(newStackRef(postFrame,0),cast);
	        	 break;
	         }
	         case Opcodes.I2C:
	         {
	        	 Expr cast=new CastExpr(newStackRef(frame,0), Type.CHAR_TYPE);
	        	 stmt=new AssignStmt(newStackRef(postFrame,0),cast);
	        	 break;
	         }
	         case Opcodes.I2S:
	         {
	        	 Expr cast=new CastExpr(newStackRef(frame,0), Type.SHORT_TYPE);
	        	 stmt=new AssignStmt(newStackRef(postFrame,0),cast);
	        	 break;
	         }
	         case Opcodes.IFEQ:
	         case Opcodes.IFLT:
	         case Opcodes.IFLE:
	         case Opcodes.IFNE:
	         case Opcodes.IFGT:
	         case Opcodes.IFGE:
	         {
	        	 Expr con=new BinopExpr(newStackRef(frame,0), IntConstant.INT0, (opcode-Opcodes.IFEQ)+BinopExpr.EQUAL);
	        	 stmt=new IfStmt(con,getLabelStmt(((JumpInsnNode)insn).label));//resolve it later;
	        	 break;
	         }
	         case Opcodes.IFNULL:
	         {
	        	 Expr con=new BinopExpr(newStackRef(frame,0), Null.NULL, BinopExpr.EQUAL);
	        	 stmt=new IfStmt(con,getLabelStmt(((JumpInsnNode)insn).label));//resolve it later;
	        	 break;
	         }	
	         case Opcodes.IFNONNULL:
	         {
	        	 Expr con=new BinopExpr(newStackRef(frame,0), Null.NULL, BinopExpr.NEQUAL);
	        	 stmt=new IfStmt(con,getLabelStmt(((JumpInsnNode)insn).label));//resolve it later;
	        	 break;
	         }
	         case Opcodes.IF_ICMPEQ:
	         case Opcodes.IF_ICMPLT:
	         case Opcodes.IF_ICMPLE:
	         case Opcodes.IF_ICMPNE:
	         case Opcodes.IF_ICMPGT:
	         case Opcodes.IF_ICMPGE:
	         {	        	 
	        	 Expr con=new BinopExpr(newStackRef(frame,-1), newStackRef(frame,0), 
	        			 opcode-Opcodes.IF_ICMPEQ+BinopExpr.EQUAL);
	        	 stmt=new IfStmt(con,getLabelStmt(((JumpInsnNode)insn).label));//resolve it later;
	        	 break;
	         }
	         case Opcodes.LCMP:
	         {	        	 
	        	 Expr con=new BinopExpr(newStackRef(frame,-1), newStackRef(frame,0), 
	        			 BinopExpr.EQUAL);
	        	 stmt=new AssignStmt(newStackRef(postFrame, 0), con);
	        	 break;
	         }
	         case Opcodes.FCMPL:
	         case Opcodes.FCMPG:
	         {	        	 
	        	 Expr con=new BinopExpr(newStackRef(frame,-1), newStackRef(frame,0), 
	        			 opcode-Opcodes.FCMPL+BinopExpr.CMPL);
	        	 stmt=new AssignStmt(newStackRef(postFrame, 0), con);
	        	 break;
	         }
	         case Opcodes.DCMPL:
	         case Opcodes.DCMPG:
	         {	        	 
	        	 Expr con=new BinopExpr(newStackRef(frame,-1), newStackRef(frame,0), 
	        			 opcode-Opcodes.DCMPL+BinopExpr.CMPL);
	        	 stmt=new AssignStmt(newStackRef(postFrame, 0), con);
	        	 break;
	         }
	         case Opcodes.IF_ACMPEQ:
	         case Opcodes.IF_ACMPNE:
	         {	        	 
	        	 Expr con=new BinopExpr(newStackRef(frame,-1), newStackRef(frame,0), 
	        			 opcode-Opcodes.IF_ACMPEQ+BinopExpr.EQUAL);
	        	 stmt=new IfStmt(con,getLabelStmt(((JumpInsnNode)insn).label));//resolve it later;
	        	 break;
	         }
	         case Opcodes.GOTO:
	         {
	        	 stmt=new GotoStmt(getLabelStmt(((JumpInsnNode)insn).label));
	        	 break;
	         }
	         case Opcodes.JSR:
	         {
	        	 System.err.println("Find JSR in JIRTransfer");
//	        	 stmt=new AssignStmt(newStackRef(postFrame, 0), new IntConstant(insn.getNext().index));
//	        	 stmts.add(stmt);
	        	 stmt=new GotoStmt(getLabelStmt(((JumpInsnNode)insn).label));
	        	 break;
	         }
	         case Opcodes.RET:
	         {
	            Ref local =getLocal(insn.index, ((VarInsnNode)insn).var,postFrame,false);
	            stmt = new RetStmt(local);
	            break;
	         }
	         case Opcodes.RETURN:
	            stmt = new ReturnStmt(null);
	            break;
	         case Opcodes.LRETURN:
	         case Opcodes.DRETURN:
	         case Opcodes.IRETURN:
	         case Opcodes.FRETURN:
	         case Opcodes.ARETURN:
	            stmt = new ReturnStmt(newStackRef(frame,0));
	            break;
	         case Opcodes.TABLESWITCH:
	         {
	        	 TableSwitchInsnNode table=(TableSwitchInsnNode)insn;
	        	 LabelStmt target[]=new LabelStmt[table.labels.size()];
	        	 for(int i=0;i<target.length;i++)
	        		 target[i]=getLabelStmt(((LabelNode)table.labels.get(i)));
	            stmt = new TableSwitchStmt(newStackRef(frame,0),
	                    table.min,
	                    table.max,
	                    target,getLabelStmt(table.dflt));
	            break;
	         }
	         case Opcodes.LOOKUPSWITCH:
	         {
	        	 LookupSwitchInsnNode look=(LookupSwitchInsnNode)insn;
	        	 List<IntConstant> keys=new ArrayList<IntConstant>(look.keys.size());
	        	 for(int i=0;i<keys.size();i++)
	        		 keys.add(IntConstant.v(((Integer)look.keys.get(i)).intValue()));
	        	 LabelStmt target[]=new LabelStmt[look.labels.size()];
	        	 for(int i=0;i<target.length;i++)
	        		 target[i]=getLabelStmt((LabelNode)look.labels.get(i));
	        	 stmt = new LookupSwitchStmt(newStackRef(frame,0),getLabelStmt(look.dflt),keys,target);
	        	 break;
	         }
	         case Opcodes.PUTFIELD:
	         {
	        	 FieldInsnNode field=(FieldInsnNode)insn;
	        	 FieldRef ref=new FieldRef(newStackRef(frame,-1), field);
	        	 //FieldRef ref=new FieldRef(newStackRef(frame,-1), getField(field));
	        	 stmt=new AssignStmt(ref,newStackRef(frame,0));
	            break;
	         }
	         case Opcodes.GETFIELD:
	         {
	        	 FieldInsnNode field=(FieldInsnNode)insn;
	        	 FieldRef ref=new FieldRef(newStackRef(frame,0), field);
//	        	 FieldRef ref=new FieldRef(newStackRef(frame,0),getField(field));
	        	 stmt=new AssignStmt(newStackRef(postFrame,0),ref);
	            break;
	         }
	         case Opcodes.PUTSTATIC:
	         {
	        	 FieldInsnNode field=(FieldInsnNode)insn;
	        	 FieldRef ref=new FieldRef(Null.NULL,field);
//	        	 FieldRef ref=new FieldRef(Null.NULL,getField(field));
	        	 stmt=new AssignStmt(ref,newStackRef(frame,0));
	            break;
	         }
	         case Opcodes.GETSTATIC:
	         {
	        	 FieldInsnNode field=(FieldInsnNode)insn;
	        	 FieldRef ref=new FieldRef(Null.NULL,field);
//	        	 FieldRef ref=new FieldRef(Null.NULL,getField(field));
	        	 stmt=new AssignStmt(newStackRef(postFrame,0),ref);
	            break;
	         }
	         case Opcodes.INVOKEVIRTUAL:
	         case Opcodes.INVOKEINTERFACE:
	         case Opcodes.INVOKESPECIAL:
	         case Opcodes.INVOKESTATIC:
	         {
	        	 MethodInsnNode mi=(MethodInsnNode)insn;
	        	 Type params[]=Type.getArgumentTypes(mi.desc);
	        	 Type rType=Type.getReturnType(mi.desc);
	        	 List<JIRValue> ps=new ArrayList<JIRValue>(params.length);
	        	 for(int i=params.length-1;i>=0;i--){
	        		 ps.add(newStackRef(frame,-i));
	        	 }
	        	 JIRValue ref;
	        	 if(opcode==Opcodes.INVOKESTATIC)
	        		 ref=Null.NULL;
	        	 else
	        		 ref=newStackRef(frame,Type.getObjectType(mi.owner));
	        		 //ref=newStackRef(frame,-params.length);
	        	 Expr invoke=new InvokeExpr(ref,opcode-Opcodes.INVOKEVIRTUAL,mi,ps);
	        	 if(rType.getSort()==Type.VOID)
	        		 stmt=new InvokeStmt(invoke);
	        	 else
	        		 stmt=new AssignStmt(newStackRef(postFrame,0),invoke);
	        	 break;
	         }
	         case Opcodes.ATHROW:
	        	 stmt=new ThrowStmt(newStackRef(frame,0));
	            break;
	         case Opcodes.NEW:
	         {
	        	 stmt=new AssignStmt(newStackRef(postFrame,0), new NewExpr(Type.getObjectType(((TypeInsnNode)insn).desc)));
	        	 break;
	         }
	         case Opcodes.CHECKCAST:
	         {
	        	 Type type=Type.getObjectType(((TypeInsnNode)insn).desc);
	        	 stmt=new AssignStmt(newStackRef(postFrame,0),new CastExpr(newStackRef(frame,0), type));
	        	 break;
	         }
	         case Opcodes.INSTANCEOF:
	         {
	        	 Type type=Type.getObjectType(((TypeInsnNode)insn).desc);
	        	 stmt=new AssignStmt(newStackRef(postFrame,0),new InstanceOfExpr(newStackRef(frame,0), type));
	        	 break;
	         }
	         case Opcodes.MONITORENTER:
	        	 stmt=new EnterMonitorStmt(newStackRef(frame,0));
	            break;
	         case Opcodes.MONITOREXIT:
	        	 stmt=new ExitMonitorStmt(newStackRef(frame,0));
	            break;
	         default:
	            throw new RuntimeException("Unrecognized Opcodes instruction: " + opcode);
	        }
		}
	      if(stmt!=null)
	    	  addStmt(stmts, stmt, insn.index);
	}
}

// end

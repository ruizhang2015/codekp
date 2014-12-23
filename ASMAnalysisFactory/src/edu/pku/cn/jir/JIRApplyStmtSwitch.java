/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-20
 * @modifier: a
 * @time 2010-1-20
 * @reviewer: a
 * @time 2010-1-20
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.jir;

import java.util.HashMap;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.FieldInsnNode;

/**
 * @author a
 */
public class JIRApplyStmtSwitch extends AbstractStmtSwitch {
	public int tempIndex=0;
	public JIRStack stack;
	public JIRApplyExprSwitch es;
	public HashMap<JIRValue,JIRValue> tempMap;
	HashMap<JIRValue,HashMap<String,FieldRef>> fieldCache=new HashMap<JIRValue, HashMap<String,FieldRef>>();
	private FieldRef getField(FieldRef ref){
		JIRValue base;
		String name;
		if(!ref.base.equals(Null.NULL)){
			base=stack.pop();
			name=ref.getName();
		}else{
			base=ref.base;
			name=ref.owner+"."+ref.getName();
		}
		HashMap<String,FieldRef> b=fieldCache.get(base);
		if(b==null){
			b=new HashMap<String, FieldRef>();
			fieldCache.put(base, b);
		}
		FieldRef f=b.get(name);
		if(f==null){
			f=new FieldRef(ref.name,ref.desc,ref.owner,base);
			b.put(name, f);
		}
		return f;
	}
	HashMap<JIRValue,HashMap<JIRValue,ArrayRef>> arrayCache=new HashMap<JIRValue, HashMap<JIRValue,ArrayRef>>();
	private ArrayRef getArrayRef(){
		JIRValue index=stack.pop();
		JIRValue base=stack.pop();
		HashMap<JIRValue,ArrayRef> as=arrayCache.get(base);
		if(as==null){
			as=new HashMap<JIRValue, ArrayRef>();
			arrayCache.put(base, as);
		}
		ArrayRef a=as.get(index);
		if(a==null){
			a=new ArrayRef(base,index);
			as.put(index, a);
		}
		return a;
	}
	public JIRApplyStmtSwitch(JIRStack stack){
		this.stack=stack;
		es=new JIRApplyExprSwitch(stack);
		tempMap=new HashMap<JIRValue, JIRValue>();
	}
	public void caseEnd(){
		if(!stack.empty())
			throw new RuntimeException("JIR Stack is not empty");
	}
	protected void casePop(PopStmt pop){
		if(stack.size()>0){
		JIRValue jir=stack.pop();
		if(pop.size>1 && jir.getType().getSize()==1)
			stack.pop();
		}
	}
	
	public void caseAssign(AssignStmt as){
		if(as.right instanceof StackRef){
			if(as.left instanceof StackRef){
				StackRef sr=(StackRef)as.right;	
				if(stack.size()==sr.index)
					as.right=stack.top();
				else
					as.right=stack.at(sr.index);				
			}
			else{
				StackRef sr=(StackRef)as.right;	
				if(stack.size()==sr.index)
					as.right=stack.pop();
				else
					as.right=stack.at(sr.index);
			}
		}else if(as.right instanceof Expr){					
			as.right.apply(es);					
		}else if(as.right instanceof ArrayRef){
			as.right=getArrayRef();
		}else if(as.right instanceof FieldRef){
			as.right=getField((FieldRef)as.right);
		}
		if(as.left instanceof StackRef){	
			StackRef sr=(StackRef)as.left;	
			if(as.isStore){
				stack.push(sr.index,stack.top());
				stack.pop();
			}
			else if(as.right instanceof Constant || as.right instanceof Ref)
				stack.push(sr.index,as.right);
			else
				stack.push(sr.index,new TempRef(tempIndex++,as.right.getType()));
			as.left=stack.top();
		}else if(as.left instanceof ArrayRef){
			as.left=getArrayRef();
		}else if(as.left instanceof FieldRef){
			as.left=getField((FieldRef)as.left);
		}else if(as.left instanceof TempRef){
			TempRef temp=(TempRef)as.left;
		}
		if(as.right instanceof TempRef){
			if(as.left instanceof Ref && !(as.left instanceof TempRef) &&as.isStackOp==false)
				tempMap.put(as.right, as.left);
		}
	}
	public void caseEnterMonitor(EnterMonitorStmt a){
		if(a.value instanceof StackRef)
			a.value=stack.pop();
	}
	public void caseExitMonitor(ExitMonitorStmt a){
		if(a.value instanceof StackRef)
			a.value=stack.pop();
	}
	public void caseGoto(GotoStmt a){}
	public void caseIf(IfStmt a){
		if(a.condition instanceof Expr)
			a.condition.apply(es);
		else{
			if(a.condition instanceof StackRef)
				a.condition=stack.pop();
		}
	}
	public void caseInvoke(InvokeStmt a){
		a.invoke.apply(es);
	}
	public void caseLookupSwitch(LookupSwitchStmt a){
		if(a.key instanceof StackRef)
			a.key=stack.pop();
	}
	public void caseLabel(LabelStmt a){}
	public void caseLine(LineStmt a){}
	public void caseRet(RetStmt a){}
	public void caseReturn(ReturnStmt a){
		if(a.value!=Null.NULL && a.value instanceof StackRef)
			a.value=stack.pop();
	}
	public void caseTableSwitch(TableSwitchStmt a){
		if(a.key instanceof StackRef)
			a.key=stack.pop();
	}
	public void caseThrow(ThrowStmt a){
		if(a.value instanceof StackRef)
			a.value=stack.pop();
	}
}

// end

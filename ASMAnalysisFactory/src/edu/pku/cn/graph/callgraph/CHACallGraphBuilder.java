/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author liuxi
 * @time 2010-3-23
 * @modifier: liuxi
 * @time 2010-3-23
 * @reviewer: liuxi
 * @time 2010-3-23
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.callgraph;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;

import edu.pku.cn.Project;
import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.classfile.ClassNodeLoader;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.hierarchy.Repository;
import edu.pku.cn.jir.AssignStmt;
import edu.pku.cn.jir.InvokeExpr;
import edu.pku.cn.jir.InvokeStmt;
import edu.pku.cn.jir.Stmt;
import edu.pku.cn.util.CodaProperties;

/**
 * @author liuxi
 */
public class CHACallGraphBuilder {
	public CHACallGraphBuilder() {
		Repository.getInstance().prepareCHA();
	}
	CallGraph call;
	class WorkList{
		List<MethodNode> work=new LinkedList<MethodNode>();
		HashSet<MethodNode> inWorkList=new HashSet<MethodNode>();
		public void push(MethodNode m){
			if(!inWorkList.contains(m)){
				work.add(m);
				inWorkList.add(m);
			}
		}
		public boolean contain(MethodNode node){
			return inWorkList.contains(node);
		}
		public MethodNode top(){
			return work.get(0);
		}
		public MethodNode pop(){
			return work.remove(0);
		}
		public boolean empty(){
			return work.size()<=0;
		}
	}
	public void visit(InvokeExpr invoke,int pc,MethodNode method,WorkList work){
		{
//			InvokeNode invoke=(InvokeNode)edge.from;
			// invoke.node.ower  调用者类型
			ClassNode cn=getClassNode(invoke.node.owner);
			if(cn==null){
				Type type=Type.getObjectType(invoke.node.owner);
				if(type!=null && type.getSort()==Type.ARRAY)
					cn=getClassNode("java.lang.Object");
				else
					System.err.println("can not find class " +invoke.node.owner);
				return;
			}
			MethodNode me=getMethod(invoke,cn);
			if(me==null)
				return;
			
			if(invoke.isStatic()){//direct call
				InvokeType it;
				if(me.name.equals("<clinit>"))
					it=InvokeType.CLINIT;
				else{
					it=InvokeType.STATIC;
					MethodNode cli=cn.getMethod("<clinit>", "()V");
					if(cli!=null && clinit.contains(cli)==false){
						clinit.add(cli);
						CallGraphEdge e=new CallGraphEdge(method,pc, cli,InvokeType.CLINIT);
						call.addEdge(e);		
						if(canBeAdd(cli))
							work.push(cli);
					}
				}
				CallGraphEdge e=new CallGraphEdge(method,pc, me,it);
				call.addEdge(e);
				if(canBeAdd(me))
					work.push(me);
			}else{
				InvokeType it=getInvokeType(invoke.invokeType);
				CallGraphEdge e=null;
				
				// 对接口的方法和虚函数的虚方法都是abstract的
				
				if(!me.isAbstract()){		
					e=new CallGraphEdge(method,pc, me,it);
					call.addEdge(e);
					if(canBeAdd(me))
						work.push(me);
				}
				
				// 子类不会继承父类的私有函数，如果是private方法的话，则不需要考虑继承
				if(!me.isPrivate()){//virtual call
					if(me.isConstructor()){
						if(call.addEdge(e))
						if(canBeAdd(me))
							work.push(me);
						return;
				
					}
					String meName=invoke.getMethodName();
					String meDesc=invoke.getMethodDesc();
					Set<ClassNode> subs=invoke.getClassNode().getSubType().getSubClass();
					Iterator<ClassNode> itc=subs.iterator();
					while(itc.hasNext()){
						cn=itc.next();
						if(cn.containMethod(meName, meDesc)){
							me=cn.getMethod(meName, meDesc);
							if(me.isAbstract())
								continue;
							if(canBeAdd(me))
								work.push(me);
							if(e==null){
								e=new CallGraphEdge(method,pc, me,it);
								call.addEdge(e);
							}else
								e.addCallee(me);
//							call.addEdge(new CallGraphEdge(method, pc, me,it));
							
						}
					}
				}
			}
		}
	}
	private ClassNode getClassNode(String className){
		Repository cha=Repository.getInstance();
		return cha.findClassNode(className);
	}
//	private Set<Type> getCHAType(Type type){
//		HashSet<Type> cha=new HashSet<Type>();
//		ClassNode cn=getClassNode(type);
//		if(cn!=null){
//			Set<Type> subType=cn.getSubType().getSubType();
//			cha.addAll(subType);
//		}else{
//			System.err.println("find class node null in cgbuilder");
//		}
//		return cha;
//	}
	private MethodNode getMethod(InvokeExpr in,ClassNode cn){
		String meName=in.getMethodName();
		String meDesc=in.getMethodDesc();
		do{
			if(cn.containMethod(meName, meDesc))
				return cn.getMethod(meName, meDesc);
			else
				cn=cn.getSuperClass();
		}while(cn!=null);
		return null;//依赖的库没有加载
		//throw new RuntimeException("some error in static invoke");
	}
	private InvokeType getInvokeType(int invokeType){
		InvokeType it;
		switch(invokeType){
		case InvokeExpr.INVOKE_INTERFACE:
			it=InvokeType.INTERFACE;
			break;
		case InvokeExpr.INVOKE_SPECIAL:
			it=InvokeType.SPECIAL;
			break;
		case InvokeExpr.INVOKE_VIRTUAL:
			it=InvokeType.VIRTUAL;
			break;
		default:
//			System.err.println("Some err in CallGraphBuilder");
			it=InvokeType.STATIC;
			break;
		}
		return it;
	}
	public CallGraph analyze(MethodNode main){
		call=new CallGraph();
		WorkList work=new WorkList();
		work.push(main);
		while(!work.empty()){
			MethodNode me=work.pop();
			analyze(me,work);
		}
		return call;
	}
	private boolean canBeAdd(MethodNode me){
		if(me.declaringClass.isLibClass() && !CodaProperties.isLibExpland)
			return false;
		if(me.declaringClass.isJREClass() && !CodaProperties.isJREExpland)
			return false;
		if(me.isBridge())
			return false;
		return true;
	}
	Set<MethodNode> clinit=new HashSet<MethodNode>();
	private void analyze(MethodNode method,WorkList work){
		Iterator<Stmt> iter=method.getStmts().iterator();
		while(iter.hasNext()){
			Stmt stmt=iter.next();
			switch(stmt.getSort()){
			case Stmt.ASSIGNSTMT:{
				AssignStmt as=(AssignStmt)stmt;
				if(as.right instanceof InvokeExpr){
					visit((InvokeExpr)as.right, stmt.getIndex(), method, work);
				}
				 break;
			}
			case Stmt.INVOKESTMT:{
				InvokeStmt is=(InvokeStmt)stmt;
				visit((InvokeExpr)is.invoke, stmt.getIndex(), method, work);
				break;
			}
				default:
					break;
			}
		}
	}	
	public static void main(String[] args){
		Project project = new Project("D:/test/bin/");
		//AnalysisFactoryManager.initial();
		ClassReader cr;
		
		
		try {
			ClassNodeLoader loader=new ClassNodeLoader("D:/test/bin/");
			ClassNode cn =loader.loadClassNode("edu.pku.cn.testcase.TestCloseDbConnection",0); 
				//loader.loadClassNode("edu.pku.cn.graph.cfg.test.LiveStoreTest");
			List methods = cn.methods;
			for (int i = 0; i < methods.size(); ++i) {
				MethodNode method = (MethodNode) methods.get(i);
				if (true&&method.name.equals("right2"))
					if (method.instructions.size() > 0) {
						CHACallGraphBuilder builder=new CHACallGraphBuilder();
						CallGraph call=builder.analyze(method);
						System.out.println("*****************************");
						FileOutputStream fs=new FileOutputStream(new File("cha.txt"));
						PrintWriter pw=new PrintWriter(fs);
						pw.write(call.toString());
						pw.close();
						System.out.println("Edges:"+call.edges.size());
						System.out.println("Nodes:"+call.methodToEdge.keySet().size());
						
					}
			}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

// end

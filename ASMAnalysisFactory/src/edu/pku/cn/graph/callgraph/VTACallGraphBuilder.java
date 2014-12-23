/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author liuxi
 * @time 2010-3-16
 * @modifier: liuxi
 * @time 2010-3-16
 * @reviewer: liuxi
 * @time 2010-3-16
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.callgraph;

import java.util.ArrayList;
import java.util.HashMap;
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
import edu.pku.cn.jir.InvokeExpr;
import edu.pku.cn.ptg.ArrayNode;
import edu.pku.cn.ptg.Edge;
import edu.pku.cn.ptg.InvokeNode;
import edu.pku.cn.ptg.Node;
import edu.pku.cn.ptg.PointToGraph;

/**
 * @author liuxi
 */
public class VTACallGraphBuilder {
	CallGraph call;
	Set<Type> newType=new HashSet<Type>();
	HashSet<MethodNode> haveNew=new HashSet<MethodNode>();
	public CallGraph analyze(MethodNode main){
		call=new CallGraph();
		HashMap<Node,Set<Type>> pt=new HashMap<Node, Set<Type>>();
		PointToGraph ptg=main.getPointToGraph();
		for(int i=0;i<ptg.params.size();i++){
			Node n=ptg.params.get(i);			
			if(n!=null){
				addToMap(pt, n, n.getClassNode().getSubType().getSubType());
			}
		}
		CallStack cs=new CallStack(main, 0, pt,new HashSet<Type>());
		WorkList work=new WorkList();
		work.push(cs);
		while(!work.empty()){
			cs=work.top();
			if(f(cs,work)){
				work.pop();
				setReturnType(cs,work);
			}
		}
		return call;
	}
	private void setReturnType(CallStack cs,WorkList work){
		
	}
	private Set<Type> getCHAType(Type type){
		HashSet<Type> cha=new HashSet<Type>();
		ClassNode cn=getClassNode(type);
		if(cn!=null){
			Set<Type> subType=cn.getSubType().getSubType();
			Iterator<Type> st=subType.iterator();
			while(st.hasNext()){
				Type t=st.next();
				if(newType.contains(t))
					cha.add(type);
			}
		}else{
			System.err.println("find class node null in cgbuilder");
		}
		return cha;
	}
	static final Type NULL=Type.getType("null;");
	private boolean addToMap(HashMap<Node,Set<Type>> pt,Node key,Type value){
		if(value.equals(NULL))
			return false;
		newType.add(value);
		Set<Type> pts=pt.get(key);
		if(pts==null){
			pts=new HashSet<Type>();
			pt.put(key, pts);
		}
		return pts.add(value);
	}
	private boolean addToMap(HashMap<Node,Set<Type>> pt,Node key,Set<Type> value){
		if(value==null){
			System.err.println("why null here in CallGraphBuilder");
			return false;
		}
		newType.addAll(value);
		Set<Type> pts=pt.get(key);
		if(pts==null){
			pts=new HashSet<Type>();
			pt.put(key, pts);
		}
		return pts.addAll(value);
	}

	private ClassNode getClassNode(Type type){
		Repository cha=Repository.getInstance();
		return cha.findClassNode(type.getClassName());
	}
	private MethodNode getMethod(InvokeNode in,HashMap<Node,Set<Type>> pt){
		try{
		Set<Type> possibleType=pt.get(in.getBase());
		String meName=in.getMethodName();
		String meDesc=in.getMethodDesc();
		ClassNode cn=in.getClassNode();
		if(in.invoke.isStatic()||possibleType.contains(cn.getType())){
			do{
				if(cn.containMethod(meName, meDesc))
					return cn.getMethod(meName, meDesc);
				else
					cn=cn.getSuperClass();
			}while(cn!=null);
			if(in.invoke.isStatic()){
				throw new RuntimeException("some error in static invoke");
			}
		}
		Iterator<Type> iter=possibleType.iterator();
		while(iter.hasNext()){
			cn=getClassNode(iter.next());
			do{
				if(cn.containMethod(meName, meDesc))
					return cn.getMethod(meName, meDesc);
				else
					cn=cn.getSuperClass();
			}while(cn!=null);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		throw new RuntimeException("some error here");
	}
	private boolean needToRecursive(MethodNode m,WorkList work){
		if(work.contain(m)){
			Iterator<CallStack> it=work.iterator(m);
			while(it.hasNext()){
				MethodNode mn=it.next().node;
				if(!haveNew.contains(mn)){
					PointToGraph ptg=mn.getPointToGraph();
					Iterator<Edge> eit=ptg.edges.iterator();
					while(eit.hasNext()){
						Edge edge=eit.next();
						switch(edge.getSort()){
						case Edge.ALLOCEDGE:
							
						}
					}
				}
			}
			switch(edge.getSort()){
			case Edge.ASSIGNEDGE:
			{
				switch(edge.from.getSort()){
				case Node.INVOKENODE:
				{
					InvokeNode invoke=(InvokeNode)edge.from;
					Node base=invoke.getBase();
					ClassNode cn=invoke.getClassNode();
					MethodNode me;
					Set<Type> ret;
					if(invoke.invoke.isStatic()){
						me=getMethod(invoke,pt);
						Type rt=Type.getReturnType(me.desc);
						ret=getClassNode(rt).getSubType().getSubType();
					}else{
						Set<Type> ct=pt.get(base);
						if(ct==null){
							ct=new HashSet<Type>();
							if(base.isStatic()){								
								ct.add(base.getType());
							}else{
								method.getCFG().printStmts();
								ptg.print();
								System.err.println("error in get type set");
							}
						}
						Iterator<Type> itert=ct.iterator();
						ret=new HashSet<Type>();
						while(itert.hasNext()){
							cn=getClassNode(itert.next());
							me=cn.getMethod(invoke.getMethodName(), invoke.getMethodDesc());
							if(me!=null){
								Type rt=Type.getReturnType(me.desc);
								ret.addAll(getClassNode(rt).getSubType().getSubType());
							}
						}
					}			
					addToMap(pt, edge.from,ret);
				}
				break;
				default:
					System.err.println("it must have error here");
					break;						
				}
				break;
			}
			default:
				System.err.println("it must have error here");
				break;
			}
			cs.index++;
			work.push(cs);
			return true;
		}
		return false;
	}
	private CallStack newCallStack(MethodNode m,List<Set<Type>> param){
		PointToGraph ptg=m.getPointToGraph();
		HashMap<Node,Set<Type>> pt=new HashMap<Node, Set<Type>>();
		HashSet<Type> reType=new HashSet<Type>();
		if(!m.isStatic()){
			addToMap(pt,ptg.This,ptg.This.getType());
		}
		for(int i=0;i<ptg.params.size();i++){
			Node n=ptg.params.get(i);
			if(n!=null){
				addToMap(pt, n, param.get(i));
			}
		}
		CallStack cs=new CallStack(m, 0, pt, reType);
		return cs;
	}
	private boolean f(CallStack cs,WorkList work){
		MethodNode method=cs.node;
		HashMap<Node,Set<Type>> pt=cs.pt;
		PointToGraph ptg=method.getPointToGraph();
		HashSet<Type> reType=cs.reType;		
		boolean shouldReturn=false;
		for(int i=cs.index;i<ptg.edges.size();i++){
			Edge edge=ptg.edges.get(i);
			switch(edge.getSort()){
			case Edge.ALLOCEDGE:
			{
				addToMap(pt,edge.to,edge.from.getType());
				break;
			}
			case Edge.ASSIGNEDGE:
			{
				switch(edge.from.getSort()){
				case Node.ALLOCATENODE:
					addToMap(pt, edge.to, edge.from.getType());
					System.err.println("never happen");
					break;
				case Node.ALLOCFIELDNODE:
					break;
				case Node.ARRAYNODE:
				{
					ArrayNode array=(ArrayNode)edge.from;
					
					break;
				}
				case Node.FIELDNODE:
					addToMap(pt, edge.to, pt.get(edge.from));
					break;
				case Node.LOCALNODE:
					addToMap(pt, edge.to, pt.get(edge.from));
					if(edge.to.equals(ptg.returnNodes))
						reType.addAll(pt.get(edge.to));
					break;
				case Node.PARAMETERNODE:
					break;
				case Node.INVOKENODE:					
				{					
					InvokeNode invoke=(InvokeNode)edge.from;
					Node base=invoke.getBase();
					ClassNode cn=invoke.getClassNode();
					MethodNode me;
					if(invoke.invoke.isStatic()){
						me=getMethod(invoke,pt);
						if(cn.isLibClass()){
							addToMap(pt, edge.to, getCHAType(invoke.getReturnType()));
						}else{			
							int size=0;
							if(invoke.params!=null)
								size=invoke.params.size();
							List<Set<Type>> param=new ArrayList<Set<Type>>(size);
							for(int j=0;j<size;j++){
								param.add(pt.get(invoke.params.get(j)));
							}
							if(work.contain(me))
								needToRecursive(me, work);
							else{								
								work.push(newCallStack(me, param));
								shouldReturn=true;
							}
						}
						InvokeType it;
						if(me.name.equals("<clinit>"))
							it=InvokeType.CLINIT;
						else
							it=InvokeType.STATIC;
						CallGraphEdge e=new CallGraphEdge(method, edge.getStmtPc(), me,it);
						call.addEdge(e);
					}else{
						if(base.getSort()==Node.ALLOCATENODE){
							addToMap(pt, base, base.getType());
						}
						Set<Type> ct=pt.get(base);
						if(ct==null){
							ct=new HashSet<Type>();
							if(base.isStatic()){								
								ct.add(base.getType());
							}else{
								method.getCFG().printStmts();
								ptg.print();
								System.err.println("error in get type set");
							}
						}
						Iterator<Type> itert=ct.iterator();
						while(itert.hasNext()){
							cn=getClassNode(itert.next());
							me=cn.getMethod(invoke.getMethodName(), invoke.getMethodDesc());
							if(me!=null){
								if(cn.isLibClass()){
									addToMap(pt, edge.to, getCHAType(invoke.getReturnType()));
								}else{				
									int size=0;
									if(invoke.params!=null)
										size=invoke.params.size();
									List<Set<Type>> param=new ArrayList<Set<Type>>(size);
									for(int j=0;j<size;j++){
										param.add(pt.get(invoke.params.get(j)));
									}
									if(work.contain(me))
										needToRecursive(me, work);
									else{								
										work.push(newCallStack(me, param));
										shouldReturn=true;
									}
								}
								InvokeType it;
								switch(invoke.invoke.invokeType){
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
									System.err.println("Some err in CallGraphBuilder");
									it=InvokeType.STATIC;
									break;
								}
								CallGraphEdge e=new CallGraphEdge(method, edge.getStmtPc(), me,it);
								call.addEdge(e);
							}
						}
					}
					break;
				}
				}
				break;
			}
			case Edge.LOADEDGE:
			case Edge.STOREEDGE:
				System.err.println("not implement yet");
				break;
			}
			if(shouldReturn)
				return false;
		}
		reType.addAll(pt.get(ptg.getReturnNodes()));
		return true;
	}

	public static void main(String[] args){
		Project project = new Project("D:/eclipse/workspace/ASMAnalysisFactory/bin/");
		//AnalysisFactoryManager.initial();
		ClassReader cr;
		try {
			ClassNodeLoader loader=new ClassNodeLoader("D:/eclipse/workspace/ASMAnalysisFactory/bin/");
			ClassNode cn =//loader.loadClassNode("edu.pku.cn.Project"); 
				loader.loadClassNode("edu.pku.cn.graph.cfg.test.LiveStoreTest");
			List methods = cn.methods;
			for (int i = 0; i < methods.size(); ++i) {
				MethodNode method = (MethodNode) methods.get(i);
				if (true&&method.name.equals("testSwitch"))
					if (method.instructions.size() > 0) {
						VTACallGraphBuilder builder=new VTACallGraphBuilder();
						CallGraph call=builder.analyze(method);
						System.out.println("*****************************");
						System.out.println(call.toString());
					}
			}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

// end

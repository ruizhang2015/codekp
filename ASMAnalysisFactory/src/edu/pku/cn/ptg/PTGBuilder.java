/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-3-11
 * @modifier: a
 * @time 2010-3-11
 * @reviewer: a
 * @time 2010-3-11
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.ptg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.Type;

import edu.pku.cn.classfile.LocalVariableNode;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.graph.cfg.BasicBlock;
import edu.pku.cn.graph.cfg.CFG;
import edu.pku.cn.jir.AnyNewExpr;
import edu.pku.cn.jir.ArrayRef;
import edu.pku.cn.jir.AssignStmt;
import edu.pku.cn.jir.CastExpr;
import edu.pku.cn.jir.CaughtExceptionRef;
import edu.pku.cn.jir.Constant;
import edu.pku.cn.jir.FieldRef;
import edu.pku.cn.jir.InvokeExpr;
import edu.pku.cn.jir.InvokeStmt;
import edu.pku.cn.jir.JIRValue;
import edu.pku.cn.jir.LocalRef;
import edu.pku.cn.jir.Null;
import edu.pku.cn.jir.Ref;
import edu.pku.cn.jir.ReturnStmt;
import edu.pku.cn.jir.Stmt;
import edu.pku.cn.jir.StringConstant;
import edu.pku.cn.jir.TempRef;

/**
 * @author a
 */
public class PTGBuilder {
	HashMap<JIRValue, Node> cache=new HashMap<JIRValue, Node>();
	HashMap<LocalVariableNode, Node> local2Node=new HashMap<LocalVariableNode, Node>();
	public Node getNode(JIRValue value) {
		Node node = cache.get(value);
		if (node == null) {
			if (value instanceof FieldRef) {
				FieldRef ref=(FieldRef)value;
				Node base =getNode(ref.base);
				node = new FieldNode((FieldRef) value,base);
			}
			else if(value instanceof LocalRef){
				LocalRef ref=(LocalRef)value;
				node=new LocalNode(ref);
				local2Node.put(ref.nodeRef, node);
			}
			else if(value instanceof TempRef){
				node=new LocalNode((TempRef)value);				
			}else if(value instanceof CaughtExceptionRef){
				node=new AllocateNode();
			}else if(value instanceof ArrayRef){
				node=new ArrayNode((ArrayRef)value);				
			}else if(value instanceof Null){
				node=Node.NULL;
			}else if(value instanceof StringConstant){
				node=new AllocateNode();
			}
			node.type=value.getType();
			cache.put(value, node);
		}
		return node;
	}
	public void print(final String owner, final MethodNode m){
		Iterator<Edge> iter=ptg.edges.iterator();
		while(iter.hasNext()){
			System.out.println(iter.next());
		}
	}
	PointToGraph ptg;
	public PointToGraph analyze(final MethodNode m) {
		ptg=new PointToGraph();
		Node returnNode=new LocalNode();
		returnNode.type=Type.getReturnType(m.desc);
		CFG cfg = m.getCFG();
		List<Stmt> stmts = cfg.getJIRStmt();
		int sTop[]=new int[stmts.size()];
		List<Edge> edges = new ArrayList<Edge>();
		Iterator<Stmt> iter = stmts.iterator();
		int si=0;
		while (iter.hasNext()) {
			Stmt stmt = iter.next();
			if (stmt instanceof AssignStmt) {
				AssignStmt as = (AssignStmt) stmt;
				Type type = as.left.getType();
				if (type.getSort() >= Type.ARRAY) {
					Edge edge;
					if(as.right instanceof Ref){
//						if(as.right instanceof FieldRef){
//							edge=new LoadEdge();
//							if(as.left instanceof FieldRef){
//								edge.from=getNode(as.left);
//								edge.to=getNode(as.right);
//								edges.add(edge);
//								edge=new StoreEdge();
//							}
//						}else
						Node n=getNode(as.right);
						if(n.getSort()==Node.ALLOCATENODE)
							edge=new AllocEdge();
						else
							edge=new AssignEdge();
						edge.from=n;						
					}else if(as.right instanceof AnyNewExpr){
						edge=new AllocEdge();
						edge.from=new AllocateNode();
						//edge.to.type=as.right
						edge.from.type=as.right.getType();
					}else if(as.right instanceof StringConstant){
						edge=new AllocEdge();						
						edge.from=new AllocateNode();
						edge.from.type=as.right.getType();
					}else if(as.right instanceof Null){
						edge=new AllocEdge();
						edge.from=Node.NULL;
					}
					else if(as.right instanceof InvokeExpr){
						InvokeExpr invoke=(InvokeExpr)as.right;
						InvokeNode node=new InvokeNode(invoke,getNode(invoke.invoker));
						Edge param;
						for(int i=0;i<invoke.params.size();i++){
							JIRValue p=invoke.params.get(i);
							if(p.getType().getSort()<Type.ARRAY)
								continue;
							param=new AssignEdge();
							param.from=getNode(p);							
							param.to=new ParameterNode(invoke,i);
							param.setStmtPc(stmt.getIndex());
							edges.add(param);
							node.addParam(param.from);
						}
//						node.method
						edge=new AssignEdge();
						edge.from=node;						
					}else if(as.right instanceof CastExpr){
						edge=new AssignEdge();
						edge.from=getNode(((CastExpr)as.right).value);
						
					}
					else{
						continue;
						//throw new RuntimeException();
					}
					edge.to=getNode(as.left);
//					if(as.left instanceof TempRef){
//						cache.put(as.left, edge.from);
//						if(as.right instanceof InvokeExpr ||as.right instanceof AnyNewExpr)
//							continue;
//					}
					edge.setStmtPc(stmt.getIndex());
					edges.add(edge);
				}
				}else if(stmt instanceof InvokeStmt){
					InvokeExpr invoke=(InvokeExpr)((InvokeStmt)stmt).invoke;
					InvokeNode node=new InvokeNode(invoke,getNode(invoke.invoker));
					Edge param;
					for(int i=0;i<invoke.params.size();i++){
						JIRValue pa=invoke.params.get(i);
						if(pa instanceof Constant && !(pa instanceof StringConstant))
							continue;
						param=new AssignEdge();
						param.from=getNode(pa);							
						param.to=new ParameterNode(invoke,i);
						param.setStmtPc(stmt.getIndex());
						edges.add(param);
						node.addParam(param.from);
					}	
					Edge edge=new AssignEdge();
					edge.setStmtPc(stmt.getIndex());
					edge.from=node;
					edge.to=Node.NULL;
					edges.add(edge);
				}else if(stmt instanceof ReturnStmt){
					ReturnStmt re=(ReturnStmt)stmt;
					if(re.value==null || re.value.getType().getSort()<Type.ARRAY)
						continue;
					Edge edge=new AssignEdge();
					edge.to=returnNode;
					edge.from=getNode(re.value);
					edge.setStmtPc(stmt.getIndex());
					edges.add(edge);
				}
			sTop[si++]=edges.size();
			}
		if(sTop.length>0){
		Iterator<BasicBlock> bbIter=cfg.blockIterator();
		while(bbIter.hasNext()){
			
			BasicBlock b=bbIter.next();
			
			b.startPPG=sTop[b.startStmt];
			b.endPPG=sTop[b.endStmt];

		}		
		}
		ptg.edges=edges;
		ptg.setReturnNodes(returnNode);
		ptg.local2Node=local2Node;
		if(!(m.isStatic() ||m.isSynthetic() ||m.isNative()) && m.localVariables!=null){
			try{
			ptg.This=(LocalNode) local2Node.get(m.localVariables.get(0));
		}catch(Exception e){
			e.printStackTrace();
		}
		}
		ptg.setParams(m.params);
//		print(m.owner, m);
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		local2Node.clear();
		cache.clear();
		return ptg;
	}
}

// end

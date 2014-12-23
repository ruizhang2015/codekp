/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-5 обнГ04:30:11
 * @modifier: a
 * @time 2010-1-5 обнГ04:30:11
 * @reviewer: a
 * @time 2010-1-5 обнГ04:30:11
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.point;

import java.util.HashMap;

import org.objectweb.asm.tree.AbstractInsnNode;

import edu.pku.cn.classfile.FieldNode;
import edu.pku.cn.classfile.LocalVariableNode;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.jir.ArrayRef;
import edu.pku.cn.jir.AssignStmt;
import edu.pku.cn.jir.BinopExpr;
import edu.pku.cn.jir.CastExpr;
import edu.pku.cn.jir.Constant;
import edu.pku.cn.jir.FieldRef;
import edu.pku.cn.jir.InvokeExpr;
import edu.pku.cn.jir.InvokeStmt;
import edu.pku.cn.jir.LengthOfExpr;
import edu.pku.cn.jir.LineStmt;
import edu.pku.cn.jir.LocalRef;
import edu.pku.cn.jir.NegExpr;
import edu.pku.cn.jir.NewArrayExpr;
import edu.pku.cn.jir.NewExpr;
import edu.pku.cn.jir.ParamRef;
import edu.pku.cn.jir.Stmt;
import edu.pku.cn.jir.TempRef;

/**
 * @author guangtai
 */
public class IntraPointsToAnalysisOld implements PointsToAnalysis {
	
	
	HashMap<LocalVariableNode, Node> localRef;
	HashMap<FieldNode,Node> fieldRef;
	
	public void analyze(MethodNode node){
		localRef=new HashMap<LocalVariableNode, Node>(node.maxLocals,0.7f);
		fieldRef=new HashMap<FieldNode, Node>();
		
		for (int i = 0; i <= node.getStmts().size(); i++) {
			Stmt stmt = node.getStmts().get(i);
			// fact = facts[stmt.getIndex()];
			System.out.println("i: " + i);
			
			if (i == 26) {
				System.out.println(i);
			}
			
			if (stmt instanceof AssignStmt) {
				AssignStmt as = (AssignStmt) stmt;
				
				if(as.left instanceof LocalRef) {
					
					if(as.right instanceof Node){
						// a = b 
						// a = this.attribute
						// a = b.f
						// a.f = b
						
						
					} else if (as.right instanceof BinopExpr) {
						// a = b + c;
					} else if (as.right instanceof CastExpr) {
						// A a = (A) b;
						
					} else if (as.right instanceof LengthOfExpr) {
						// ??;
						
					} else if (as.right instanceof NegExpr) {
						// ??;
						
					} else if (as.right instanceof NewArrayExpr) {
						// A[] a = new A[]();
						
					} else if (as.right instanceof NewExpr) {
						// A a = new A();
						
					} else if (as.right instanceof Constant) {
						// A a = 100;
						
					} else if (as.right instanceof InvokeExpr) {
						
						// a = r.m() (ext)
						
						// a = r.m() (app)
							
					}
						
				} else if(as.left instanceof FieldRef){
						
				} else if(as.left instanceof TempRef){
					
				} else if(as.left instanceof ArrayRef){
					
				} else if(as.left instanceof ParamRef){
					
				}
				
				
				
				
				
				
			}
		}
		
		
			
		
	}
	@Override
	public PointsToSet reachingObjects(LocalVariableNode l) {
		// TODO Auto-generated method stub
		Node ref=localRef.get(l);
		if(ref==null)
			return null;
		return ref.getPointsToSet();
	}

	@Override
	public PointsToSet reachingObjects(MethodNode c, LocalVariableNode l) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PointsToSet reachingObjects(FieldNode f) {
		Node ref=fieldRef.get(f);
		if(ref==null)
			return null;
		return ref.getPointsToSet();
	}

	@Override
	public PointsToSet reachingObjects(PointsToSet s, FieldNode f) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PointsToSet reachingObjects(LocalVariableNode l, FieldNode f) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PointsToSet reachingObjects(MethodNode c, LocalVariableNode l,
			FieldNode f) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PointsToSet reachingObjectsOfArrayElement(PointsToSet s) {
		// TODO Auto-generated method stub
		return null;
	}

}

// end

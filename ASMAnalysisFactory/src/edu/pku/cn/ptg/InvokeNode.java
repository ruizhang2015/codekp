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

import java.util.LinkedList;
import java.util.List;

import org.objectweb.asm.Type;

import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.hierarchy.Repository;
import edu.pku.cn.jir.InvokeExpr;

/**
 * @author a
 */
public class InvokeNode extends Node {
	private Node base;
public Node getBase() {
		return base;
	}
	//	public MethodNode method;
	public List<Node> params;
	public InvokeExpr invoke;
	public InvokeNode(InvokeExpr invoke,Node base){
		this.invoke=invoke;		
		this.base=base;
		this.type=invoke.getInvokerType();
	}
	public ClassNode getClassNode(){
//		if(invoke.isStatic()){
			Repository cha=Repository.getInstance();
			return cha.findClassNode(invoke.node.owner);
//		}
//		if(base.getType().equals(o))
//		return base.getClassNode();
	}
	public void addParam(Node node){
		if(params==null)
			params=new LinkedList<Node>();
		params.add(node);
	}
	public String toString(){
		return invoke.toString();
	}
	public Type getReturnType(){
		return Type.getReturnType(invoke.node.desc);
	}
	public String getMethodName(){
		return invoke.node.name;
	}
	public String getMethodDesc(){
		return invoke.node.desc;
	}
	@Override
	public int getSort() {
		// TODO Auto-generated method stub
		return INVOKENODE;
	}
	@Override
	public boolean isStatic() {
		// TODO Auto-generated method stub
		return false;
	}
}

// end

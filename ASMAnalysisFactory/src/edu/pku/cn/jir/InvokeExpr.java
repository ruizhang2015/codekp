/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-7 ����03:41:48
 * @modifier: a
 * @time 2010-1-7 ����03:41:48
 * @reviewer: a
 * @time 2010-1-7 ����03:41:48
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.jir;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodInsnNode;

import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.hierarchy.Repository;

/**
 * @author zhouzhiyi
 */
public class InvokeExpr extends Expr {
	public InvokeExpr(JIRValue invoker, int invokeType, MethodInsnNode node,
			List<JIRValue> params) {
		super();
		this.invoker = invoker;
		this.invokeType = invokeType;
		this.node = node;
		this.params = params;
	}
	public String getMethodName(){
		return node.name;
	}
	public String getMethodDesc(){
		return node.desc;
	}
	
	public String getClassName(){
		return node.owner;		
	}
	
	public ClassNode getClassNode(){
		Repository cha=Repository.getInstance();
		return cha.findClassNode(node.owner);
	}
	
	/**
	 * Get the methodNode of the invoked method
	 * @return
	 */
	public MethodNode getMethodNode(){
		Repository cha=Repository.getInstance();
		ClassNode cn=cha.findClassNode(node.owner);
		if(cn!=null)
			return cn.getMethod(node.name, node.desc);
		else{
			System.out.println("can't find class:"+node.owner);
			return null;
		}
		
	}
	
	/**the object which owns this invoke operation */
	public JIRValue invoker;
	public static final int INVOKE_VIRTUAL=0;
	public static final int INVOKE_INTERFACE=3;
	public static final int INVOKE_STATIC=2;
	public static final int INVOKE_SPECIAL=1;
	private static final String[] name={
		"InvokeVirtual","InvokeSpecial","InvokeStatic","InvokeInterface"
	};
	/**the invoke type of this operation*/
	public int invokeType;
	public boolean isStatic(){
		return invokeType==INVOKE_STATIC;
	}
	public MethodInsnNode node;
	//public MethodNode methodRef;
	public List<JIRValue> params;
	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		List<JIRValue> clonedParams=new ArrayList<JIRValue>(params.size());
		for(int i=0;i<params.size();i++){
			clonedParams.add(JIR.cloneIfNeed(params.get(i)));
		}		
		return new InvokeExpr(JIR.cloneIfNeed(invoker), invokeType, node, clonedParams);
	}
	@Override
	public void apply(Switch sw) {
		// TODO Auto-generated method stub
		((AbstractExprSwitch)sw).caseInvoke(this);
	}
	public Type getInvokerType(){
		return Type.getObjectType(node.owner);
	}
	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return Type.getReturnType(node.desc);
	}
	@Override
	public String toString() {
		StringBuilder builder=new StringBuilder();
//		builder.append(name[invokeType]).append(" ");
		builder.append("::");
		if(invokeType!=INVOKE_STATIC)
			builder.append(invoker).append(":" + invoker.getType()).append(":");
		else
			builder.append(node.owner).append(".");
		
		builder.append(node.owner).append(".").append(node.name).append(node.desc).append("(");
		for(int i=0;i<params.size();i++){
			if(i!=0)
				builder.append(",");
			builder.append(params.get(i));
		}
		builder.append(")");
		
		builder.append("[" + this.getMethodDesc() + "]");
		return builder.toString();
	}
	
	public String getMethodNodeName() {
		StringBuilder builder=new StringBuilder();
		builder.append(node.owner + "::");
		builder.append(node.name).append(node.desc);
		return builder.toString();
	}
	
}

// end

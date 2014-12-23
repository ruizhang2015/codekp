/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-12 ионГ10:34:30
 * @modifier: a
 * @time 2010-1-12 ионГ10:34:30
 * @reviewer: a
 * @time 2010-1-12 ионГ10:34:30
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.jir;

import org.objectweb.asm.Type;

/**
 * @author zhouzhiyi
 */
public class NewArrayExpr extends Expr implements AnyNewExpr {
	public Type type;
	public JIRValue[] sizes;
	protected NewArrayExpr(Type type,JIRValue[] sizes){
		this.type=type;
		this.sizes=sizes;//new JIRValue[sizes.length];
		//System.arraycopy(this.sizes, 0, sizes, 0, sizes.length);
	}
	public int getDimensions(){
		return sizes.length;
	}
	@Override
	public void apply(Switch sw) {
		// TODO Auto-generated method stub
		((AbstractExprSwitch)sw).caseNewArray(this);
	}
	public Object clone(){
		JIRValue[] clonedSizes=new JIRValue[sizes.length];
		for(int i=0;i<sizes.length;i++){
			clonedSizes[i]=JIR.cloneIfNeed(sizes[i]);
		}
		return new NewArrayExpr(type, clonedSizes);
	}
	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return type;
	}
	@Override
	public String toString() {
		StringBuilder builder=new StringBuilder();
		builder.append("newarray ").append(type);
		for(int i=0;i<sizes.length;i++){
			builder.append("[").append(sizes[i]).append("]");
		}
		return builder.toString();
	}
}

// end

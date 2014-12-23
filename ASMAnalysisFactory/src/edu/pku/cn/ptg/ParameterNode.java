/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-3-10
 * @modifier: a
 * @time 2010-3-10
 * @reviewer: a
 * @time 2010-3-10
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.ptg;


import edu.pku.cn.jir.InvokeExpr;

/**
 * Yellow Node
 * @author a
 */
public class ParameterNode extends LocalNode {
	public InvokeExpr invoke;
	public int index;
	public ParameterNode(InvokeExpr invoke,int index){
		this.invoke=invoke;
		this.index=index;
		type=invoke.params.get(index).getType();
	}
	public String toString(){
		return type.toString()+":"+index;
	}
	@Override
	public int getSort() {
		// TODO Auto-generated method stub
		return PARAMETERNODE;
	}
}

// end

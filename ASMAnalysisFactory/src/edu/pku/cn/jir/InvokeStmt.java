/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-15
 * @modifier: a
 * @time 2010-1-15
 * @reviewer: a
 * @time 2010-1-15
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.jir;
/**
 * @author a
 */
public class InvokeStmt extends AbstractStmt {
	public JIRValue invoke;
	public InvokeStmt(JIRValue invoke){
		this.invoke=invoke;
	}
	
	public JIRValue getInvoke(){
		return this.invoke;
	}
	@Override
	public String toString() {
//		return invoke.toString()+super.toString();
		return invoke.toString();
	}
	public void apply(Switch sw){
		((AbstractStmtSwitch)sw).caseInvoke(this);
	}
	@Override
	public void accept(JIRVisitor jv) {
		// TODO Auto-generated method stub
		jv.visit(this);
	}
	@Override
	public int getSort() {
		// TODO Auto-generated method stub
		return INVOKESTMT;
	}
}

// end

/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-12 ����01:20:58
 * @modifier: a
 * @time 2010-1-12 ����01:20:58
 * @reviewer: a
 * @time 2010-1-12 ����01:20:58
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.jir;
/**
 * @author zhouzhiyi
 */
public class AssignStmt extends AbstractStmt {
	public boolean isStore=false;
	public boolean isStackOp=false;
	public AssignStmt(JIRValue left, JIRValue right) {
		super();
		this.left = left;
		this.right = right;
	}
	public AssignStmt(JIRValue left, JIRValue right,boolean isStore) {
		super();
		this.left = left;
		this.right = right;
		this.isStore=isStore;
	}
	public AssignStmt(boolean isStackOp,JIRValue left, JIRValue right) {
		super();
		this.left = left;
		this.right = right;
		this.isStackOp=isStackOp;
	}
	public JIRValue left;
	public JIRValue right;
	
	public JIRValue getRight(){
		return this.right;
	}
	
	public JIRValue getLeft(){
		return this.left;
	}
	@Override
	public String toString() {
		return left.getType().toString() + left.toString() + "=" + right.toString()+super.toString();
	}
//	public String toString() {
//		return  right.toString();
//	}
	public void apply(Switch sw){
		((AbstractStmtSwitch)sw).caseAssign(this);
	}
	@Override
	public void accept(JIRVisitor jv) {
		// TODO Auto-generated method stub
		jv.visit(this);
	}
	@Override
	public int getSort() {
		// TODO Auto-generated method stub
		return ASSIGNSTMT;
	}
}

// end

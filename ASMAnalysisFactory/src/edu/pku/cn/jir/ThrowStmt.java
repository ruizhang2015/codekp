/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-12 обнГ01:57:18
 * @modifier: a
 * @time 2010-1-12 обнГ01:57:18
 * @reviewer: a
 * @time 2010-1-12 обнГ01:57:18
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
public class ThrowStmt extends AbstractStmt {
	public ThrowStmt(JIRValue value) {
		super();
		this.value = value;
	}

	public JIRValue value;
	public void apply(Switch sw){
		((AbstractStmtSwitch)sw).caseThrow(this);
	}
	@Override
	public String toString() {
		return "throw " + value+super.toString();
	}
	@Override
	public void accept(JIRVisitor jv) {
		// TODO Auto-generated method stub
		jv.visit(this);
	}
	@Override
	public int getSort() {
		// TODO Auto-generated method stub
		return THROWSTMT;
	}
}

// end

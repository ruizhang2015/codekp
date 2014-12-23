/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-12 ����01:56:32
 * @modifier: a
 * @time 2010-1-12 ����01:56:32
 * @reviewer: a
 * @time 2010-1-12 ����01:56:32
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
public class ReturnStmt extends AbstractStmt {
	public ReturnStmt(JIRValue value) {
		super();
		this.value = value;
	}

	public JIRValue value;

	@Override
	public String toString() {
		if(value==null)
			return "return";
		return "return " + value;
	}
	public void apply(Switch sw){
		((AbstractStmtSwitch)sw).caseReturn(this);
	}
	@Override
	public void accept(JIRVisitor jv) {
		// TODO Auto-generated method stub
		jv.visit(this);
	}
	@Override
	public int getSort() {
		// TODO Auto-generated method stub
		return RETURNSTMT;
	}
}

// end

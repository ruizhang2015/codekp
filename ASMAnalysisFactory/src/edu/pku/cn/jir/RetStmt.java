/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-12 обнГ01:56:02
 * @modifier: a
 * @time 2010-1-12 обнГ01:56:02
 * @reviewer: a
 * @time 2010-1-12 обнГ01:56:02
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
public class RetStmt extends AbstractStmt {
	public RetStmt(JIRValue value) {
		super();
		this.value = value;
	}

	public JIRValue value;

	@Override
	public String toString() {
		return "Ret " + value+super.toString();
	}
	public void apply(Switch sw){
		((AbstractStmtSwitch)sw).caseRet(this);
	}
	@Override
	public void accept(JIRVisitor jv) {
		// TODO Auto-generated method stub
		jv.visit(this);
	}
	@Override
	public int getSort() {
		// TODO Auto-generated method stub
		return RETSTMT;
	}
}

// end

/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-12 обнГ01:24:32
 * @modifier: a
 * @time 2010-1-12 обнГ01:24:32
 * @reviewer: a
 * @time 2010-1-12 обнГ01:24:32
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
public class IfStmt  extends AbstractStmt implements JumpStmt{
	public IfStmt(JIRValue condition, LabelStmt target) {
		super();
		this.condition = condition;
		this.target = target;
	}
	public JIRValue condition;
	public LabelStmt target;
	@Override
	public String toString() {
		return "If " + condition + " Goto " + target+super.toString();
	}
	public void apply(Switch sw){
		((AbstractStmtSwitch)sw).caseIf(this);
	}
	@Override
	public void accept(JIRVisitor jv) {
		// TODO Auto-generated method stub
		jv.visit(this);
	}
	@Override
	public int getSort() {
		// TODO Auto-generated method stub
		return IFSTMT;
	}
}

// end

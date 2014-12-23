/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-12 обнГ01:23:56
 * @modifier: a
 * @time 2010-1-12 обнГ01:23:56
 * @reviewer: a
 * @time 2010-1-12 обнГ01:23:56
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
public class GotoStmt  extends AbstractStmt implements JumpStmt{
	public GotoStmt(LabelStmt target) {
		super();
		this.target = target;
	}

	public LabelStmt target;

	@Override
	public String toString() {
		return "Goto " + target+super.toString();
	}
	public void apply(Switch sw){
		((AbstractStmtSwitch)sw).caseGoto(this);
	}
	@Override
	public void accept(JIRVisitor jv) {
		// TODO Auto-generated method stub
		jv.visit(this);
	}
	@Override
	public int getSort() {
		// TODO Auto-generated method stub
		return GOTOSTMT;
	}
}

// end

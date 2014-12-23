/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-12 обнГ01:53:23
 * @modifier: a
 * @time 2010-1-12 обнГ01:53:23
 * @reviewer: a
 * @time 2010-1-12 обнГ01:53:23
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.jir;

import java.util.Arrays;

/**
 * @author zhouzhiyi
 */
public class TableSwitchStmt extends AbstractStmt  implements JumpStmt{
	public TableSwitchStmt(JIRValue key, int lowIndex, int hightIndex,
			LabelStmt[] targets, LabelStmt defaultTarget) {
		super();
		this.key = key;
		this.lowIndex = lowIndex;
		this.hightIndex = hightIndex;
		this.targets = targets;
		this.defaultTarget = defaultTarget;
	}
	public JIRValue key;
	public int lowIndex;
	public int hightIndex;
	public LabelStmt[] targets;
	public LabelStmt defaultTarget;
	public void apply(Switch sw){
		((AbstractStmtSwitch)sw).caseTableSwitch(this);
	}
	@Override
	public String toString() {
		StringBuilder builder=new StringBuilder();
		builder.append("tableswitch(").append(key).append("){\n");
		for(int i=lowIndex;i<=hightIndex;i++){
			builder.append(i).append(":goto ").append(targets[i-lowIndex]).append("\n");
		}
		builder.append("default:goto ").append(defaultTarget).append("}");
		return builder.toString()+super.toString();
	}
	@Override
	public void accept(JIRVisitor jv) {
		// TODO Auto-generated method stub
		jv.visit(this);
	}
	@Override
	public int getSort() {
		// TODO Auto-generated method stub
		return TABLESWITCHSTMT;
	}
}

// end

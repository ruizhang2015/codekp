/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-12 обнГ01:39:12
 * @modifier: a
 * @time 2010-1-12 обнГ01:39:12
 * @reviewer: a
 * @time 2010-1-12 обнГ01:39:12
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.jir;

import java.util.List;

/**
 * @author zhouzhiyi
 */
public class LookupSwitchStmt extends AbstractStmt  implements JumpStmt{
	public LookupSwitchStmt(JIRValue key, LabelStmt defaultTarget, List<IntConstant> lookupValues, LabelStmt[] targets) {
		super();
		this.key = key;
		this.defaultTarget = defaultTarget;
		this.lookupValues = lookupValues;
		this.targets = targets;
	}
	public JIRValue key;
	public LabelStmt defaultTarget;
	public List<IntConstant> lookupValues;
	public LabelStmt[] targets;
	public void apply(Switch sw){
		((AbstractStmtSwitch)sw).caseLookupSwitch(this);
	}
	@Override
	public String toString() {
		StringBuilder builder=new StringBuilder();
		builder.append("LookupSwitch ").append(key).append("{\n");
		builder.append("default:").append(defaultTarget).append("\n");
		for(int i=0;i<lookupValues.size();i++){
			builder.append(lookupValues.get(i)).append(":").append(targets[i]).append("\n");
		}
		builder.append("}");
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
		return LOOKUPSWITCHSTMT;
	}
}

// end

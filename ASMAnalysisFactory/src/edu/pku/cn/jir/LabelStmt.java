/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-18
 * @modifier: a
 * @time 2010-1-18
 * @reviewer: a
 * @time 2010-1-18
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.jir;

import org.objectweb.asm.tree.LabelNode;

/**
 * @author a
 */
public class LabelStmt extends NopStmt {
	public int labelIndex;
	public LabelNode label;
	public LabelStmt(int index){
		this.labelIndex=index;
	}
	public String toString(){
		return "Label:"+labelIndex+" "+label.getLabel()+super.toString();
	}
	public void apply(Switch sw){
		((AbstractStmtSwitch)sw).caseLabel(this);
	}
	@Override
	public void accept(JIRVisitor jv) {
		// TODO Auto-generated method stub
		jv.visit(this);
	}
	@Override
	public int getSort() {
		// TODO Auto-generated method stub
		return LABLESTMT;
	}
}

// end

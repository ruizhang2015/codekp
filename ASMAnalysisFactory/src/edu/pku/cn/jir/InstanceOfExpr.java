/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-12 ионГ11:30:34
 * @modifier: a
 * @time 2010-1-12 ионГ11:30:34
 * @reviewer: a
 * @time 2010-1-12 ионГ11:30:34
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.jir;

import org.objectweb.asm.Type;

/**
 * @author zhouzhiyi
 */
public class InstanceOfExpr extends Expr {
	public InstanceOfExpr(JIRValue value, Type checkType) {
		super();
		this.value = value;
		this.checkType = checkType;
	}

	public JIRValue value;
	public Type checkType;
	
	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		return new InstanceOfExpr(JIR.cloneIfNeed(value), checkType);
	}
	@Override
	public void apply(Switch sw) {
		// TODO Auto-generated method stub
		((AbstractExprSwitch)sw).caseInstanceOf(this);
	}
	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return Type.BOOLEAN_TYPE;
	}

	@Override
	public String toString() {
		return value.toString()+" instanceof "+checkType.toString();
	}

}

// end

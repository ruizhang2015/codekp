/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-12 ионГ11:28:39
 * @modifier: a
 * @time 2010-1-12 ионГ11:28:39
 * @reviewer: a
 * @time 2010-1-12 ионГ11:28:39
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
public class CastExpr extends Expr {
	public CastExpr(JIRValue value, Type type) {
		super();
		this.value = value;
		this.type = type;
	}
	public JIRValue value;
	public Type type;
	
	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		return new CastExpr(JIR.cloneIfNeed(value),type);
	}
	@Override
	public void apply(Switch sw) {
		// TODO Auto-generated method stub
		((AbstractExprSwitch)sw).caseCast(this);
	}
	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return type;
	}

	@Override
	public String toString() {
		return "(" + type.toString() + ")" + value.toString() ;
	}

}

// end

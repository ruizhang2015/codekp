/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-12 ионГ11:34:11
 * @modifier: a
 * @time 2010-1-12 ионГ11:34:11
 * @reviewer: a
 * @time 2010-1-12 ионГ11:34:11
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
public class LengthOfExpr extends Expr implements UnopExpr {
	public JIRValue value;
	public LengthOfExpr(JIRValue value){
		this.value=value;
	}
	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		return new LengthOfExpr(JIR.cloneIfNeed(value));
	}
	@Override
	public void apply(Switch sw) {
		// TODO Auto-generated method stub
		((AbstractExprSwitch)sw).caseLengthOf(this);
	}
	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return Type.INT_TYPE;
	}
	@Override
	public String toString() {
		return value+".length";
	}

}

// end

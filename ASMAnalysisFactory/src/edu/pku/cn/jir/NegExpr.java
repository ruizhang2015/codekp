/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-12 ионГ11:36:49
 * @modifier: a
 * @time 2010-1-12 ионГ11:36:49
 * @reviewer: a
 * @time 2010-1-12 ионГ11:36:49
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
public class NegExpr extends Expr implements UnopExpr {
	public NegExpr(JIRValue value) {
		super();
		this.value = value;
	}

	public JIRValue value;
	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		return new NegExpr(JIR.cloneIfNeed(value));
	}
	@Override
	public void apply(Switch sw) {
		// TODO Auto-generated method stub
		((AbstractExprSwitch)sw).caseNeg(this);
	}
	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		int sort=value.getType().getSort();
		switch(sort){
		case Type.DOUBLE:
			return Type.DOUBLE_TYPE;
		case Type.FLOAT:
			return Type.FLOAT_TYPE;
		case Type.LONG:
			return Type.LONG_TYPE;
			default:
				return Type.INT_TYPE;
		}
	}

	@Override
	public String toString() {
		return "-" + value;
	}

}

// end

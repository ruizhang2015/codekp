/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-12 ионГ10:45:29
 * @modifier: a
 * @time 2010-1-12 ионГ10:45:29
 * @reviewer: a
 * @time 2010-1-12 ионГ10:45:29
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
public class NewExpr extends Expr implements AnyNewExpr {
	public Type type;
	public NewExpr(Type type){
		this.type=type;
	}
	public Object clone(){
		return new NewExpr(type);
	}
	@Override
	public void apply(Switch sw) {
		// TODO Auto-generated method stub
		((AbstractExprSwitch)sw).caseNew(this);
	}
	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return type;
	}
	@Override
	public String toString() {
		return "New " + type;
	}

}

// end

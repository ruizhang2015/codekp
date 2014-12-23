/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-12 ÉÏÎç10:55:19
 * @modifier: a
 * @time 2010-1-12 ÉÏÎç10:55:19
 * @reviewer: a
 * @time 2010-1-12 ÉÏÎç10:55:19
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
public class BinopExpr extends Expr {
	public JIRValue op1;
	public JIRValue op2;
	public int opType;
	public static final int ADD = 0;
	public static final int SUB = 1;
	public static final int MUL = 2;
	public static final int DIV = 3;
	public static final int REMAINDER = 4;
	public static final int SHIFTLEFT = 5;
	public static final int SHIFTRIGHT = 6;
	public static final int UNSIGNEDSHIFTRIGHT = 7;
	public static final int NEG = 8;
	public static final int AND = 9;
	public static final int OR = 10;
	public static final int XOR = 11;
	public static final int EQUAL = 12;
	public static final int NEQUAL = 13;
	public static final int LT = 14;
	public static final int GTE = 15;
	public static final int GT = 16;
	public static final int LTE = 17;
	public static final int CMP = 18;// LONG
	public static final int CMPL = 19;// REAL
	public static final int CMPG = 20;// REAL
	public static final String[] name = { "+", "-", "*", "/", "%", ">>", "<<", ">>>", "-", "&", "|", "^", "==", "!=",
			"<", ">=", ">", "<=", " cmp ", " cmpl ", " cmpg " };

	public BinopExpr(JIRValue op1, JIRValue op2, int opType) {
		this.op1 = op1;
		this.op2 = op2;
		this.opType = opType;
	}

	@Override
	public void apply(Switch sw) {
		// TODO Auto-generated method stub
		((AbstractExprSwitch) sw).caseBinop(this);
	}

	public Object clone() {
		return new BinopExpr(JIR.cloneIfNeed(op1), JIR.cloneIfNeed(op2), opType);
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		int sort = Math.max(op1.getType().getSort(), op2.getType().getSort());
		switch (sort) {
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
		return op1.toString() + name[opType] + op2.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof BinopExpr){
			BinopExpr expr = (BinopExpr)obj;
			if(expr.toString().equals(this.toString())){
				return true;
			} else
				return false;
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return this.toString().hashCode();
	}
	
	public static int oppositeOpType(BinopExpr expr) {
		int rightOpType;
		switch (expr.opType) {
		case BinopExpr.EQUAL:
			rightOpType = BinopExpr.NEQUAL;
			break;
		case BinopExpr.NEQUAL:
			rightOpType = BinopExpr.EQUAL;
			break;
		case BinopExpr.LT:
			rightOpType = BinopExpr.GTE;
			break;
		case BinopExpr.GTE:
			rightOpType = BinopExpr.LT;
			break;
		case BinopExpr.GT:
			rightOpType = BinopExpr.LTE;
			break;
		case BinopExpr.LTE:
			rightOpType = BinopExpr.GT;
			break;
		default:
			rightOpType = -1;
		}
		return rightOpType;
	}

	public static int mirrorOpType(BinopExpr expr) {
		int rightOpType;
		switch (expr.opType) {
		case BinopExpr.EQUAL:
			rightOpType = BinopExpr.EQUAL;
			break;
		case BinopExpr.NEQUAL:
			rightOpType = BinopExpr.NEQUAL;
			break;
		case BinopExpr.LT:
			rightOpType = BinopExpr.GT;
			break;
		case BinopExpr.GTE:
			rightOpType = BinopExpr.LTE;
			break;
		case BinopExpr.GT:
			rightOpType = BinopExpr.LT;
			break;
		case BinopExpr.LTE:
			rightOpType = BinopExpr.GTE;
			break;
		default:
			rightOpType = -1;
		}
		return rightOpType;
	}
}

// end

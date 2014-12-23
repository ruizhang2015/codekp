/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-11 обнГ01:41:50
 * @modifier: a
 * @time 2010-1-11 обнГ01:41:50
 * @reviewer: a
 * @time 2010-1-11 обнГ01:41:50
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
public class IntConstant extends Constant {
	public final int value;
	public static final IntConstant INTM=new IntConstant(-1);
	public static final IntConstant INT0=new IntConstant(0);
	public static final IntConstant INT1=new IntConstant(1);
	public static final IntConstant INT2=new IntConstant(2);
	public static final IntConstant INT3=new IntConstant(3);
	public static final IntConstant INT4=new IntConstant(4);
	public static final IntConstant INT5=new IntConstant(5);
	public static IntConstant v(int value){
		switch(value){
		case -1:
			return INTM;
		case 0:
			return INT0;
		case 1:
			return INT1;
		case 2:
			return INT2;
		case 3:
			return INT3;
		case 4:
			return INT4;
		case 5:
			return INT5;
			default:
				return new IntConstant(value);
		}
	}
	protected IntConstant(int value){
		this.value=value;
	}
	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return Type.INT_TYPE;
	}
	public int hashCode(){
		return value;
	}
	public boolean equals(Object c){
		if(c instanceof IntConstant){
			return value==((IntConstant)c).value;
		}
		return false;
	}
	@Override
	public void apply(Switch sw) {
		// TODO Auto-generated method stub
		((AbstractConstantSwitch)sw).caseInt(this);
	}
	@Override
	public String toString() {
		return Integer.toString(value);
	}
}

// end

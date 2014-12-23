/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-11 ÏÂÎç02:55:47
 * @modifier: a
 * @time 2010-1-11 ÏÂÎç02:55:47
 * @reviewer: a
 * @time 2010-1-11 ÏÂÎç02:55:47
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
public class DoubleConstant extends Constant {
	public final double value;
	public static final DoubleConstant DOUBLE0=new DoubleConstant(0);
	public static final DoubleConstant DOUBLE1=new DoubleConstant(1);
	public static DoubleConstant v(double value){
		if(value==0)
			return DOUBLE0;
		else if(value==1)
			return DOUBLE1;
		return new DoubleConstant(value);		
	}
	protected DoubleConstant(double value){
		this.value=value;
	}
	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return Type.DOUBLE_TYPE;
	}
	public int hashCode(){
		long v=Double.doubleToLongBits(value);
		return (int)(v^(v>>>32));
	}
	public boolean equals(Object c){
		if(c instanceof DoubleConstant){
			return value==((DoubleConstant)c).value;
		}
		return false;
	}
	@Override
	public String toString() {
		return Double.toString(value);
	}
	@Override
	public void apply(Switch sw) {
		// TODO Auto-generated method stub
		((AbstractConstantSwitch)sw).caseDouble(this);
	}
}

// end

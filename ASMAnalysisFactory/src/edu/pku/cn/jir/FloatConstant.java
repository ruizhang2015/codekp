/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-11 обнГ02:53:53
 * @modifier: a
 * @time 2010-1-11 обнГ02:53:53
 * @reviewer: a
 * @time 2010-1-11 обнГ02:53:53
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
public class FloatConstant extends Constant {
	public final float value;
	public static final FloatConstant FLOAT0=new FloatConstant(0);
	public static final FloatConstant FLOAT1=new FloatConstant(1);
	public static final FloatConstant FLOAT2=new FloatConstant(2);
	public static FloatConstant v(float value){
		if(value==0)
			return FLOAT0;
		else if(value==1)
			return FLOAT1;
		else if(value==2)
			return FLOAT2;
		return new FloatConstant(value);		
	}

	protected FloatConstant(float value){
		this.value=value;
	}
	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return Type.FLOAT_TYPE;
	}
	public int hashCode(){
		return Float.floatToIntBits(value);
	}
	public boolean equals(Object c){
		if(c instanceof FloatConstant){
			return value==((FloatConstant)c).value;
		}
		return false;
	}
	@Override
	public void apply(Switch sw) {
		// TODO Auto-generated method stub
		((AbstractConstantSwitch)sw).caseFloat(this);
	}
	@Override
	public String toString() {
		return Float.toString(value);
	}
}

// end

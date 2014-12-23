/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-11 ÏÂÎç02:51:29
 * @modifier: a
 * @time 2010-1-11 ÏÂÎç02:51:29
 * @reviewer: a
 * @time 2010-1-11 ÏÂÎç02:51:29
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
public class LongConstant extends Constant {
	public final long value;
	public static final LongConstant LONG0=new LongConstant(0);
	public static final LongConstant LONG1=new LongConstant(1);
	public static LongConstant v(long value){
		if(value==0)
			return LONG0;
		else if(value==1)
			return LONG1;
		return new LongConstant(value);		
	}

	protected LongConstant(long value){
		this.value=value;
	}
	@Override
	public void apply(Switch sw) {
		// TODO Auto-generated method stub
		((AbstractConstantSwitch)sw).caseLong(this);
	}
	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return Type.LONG_TYPE;
	}
	public int hashCode(){
		return (int)(value^(value>>>32));
	}
	public boolean equals(Object c){
		if(c instanceof LongConstant){
			return value==((LongConstant)c).value;
		}
		return false;
	}
	@Override
	public String toString() {
		return Long.toString(value);
	}
}

// end

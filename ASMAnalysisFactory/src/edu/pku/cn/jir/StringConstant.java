/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-11 обнГ01:28:05
 * @modifier: a
 * @time 2010-1-11 обнГ01:28:05
 * @reviewer: a
 * @time 2010-1-11 обнГ01:28:05
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
public class StringConstant extends Constant {
	public final String value;
	public StringConstant(String s){
		value=s;
	}
	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return Type.getObjectType("java.lang.String");
	}
	@Override
	public void apply(Switch sw) {
		// TODO Auto-generated method stub
		((AbstractConstantSwitch)sw).caseString(this);
	}
	public boolean equals(Object c){
		return (c instanceof StringConstant &&((StringConstant)c).value.equals(value));
	}
	public int hashCode(){
		return value.hashCode();
	}
//	public void apply(Swtich sw){
//		((ConstantSwitch)sw).caseStringConstant(this);
//	}
	@Override
	public String toString() {
		return value;
	}
}

// end

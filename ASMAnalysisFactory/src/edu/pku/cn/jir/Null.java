/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-11 обнГ01:34:29
 * @modifier: a
 * @time 2010-1-11 обнГ01:34:29
 * @reviewer: a
 * @time 2010-1-11 обнГ01:34:29
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
public class Null extends Constant {
	public static Null NULL=new Null();
	public Null(){}
	public static Null NULL(){
		if(NULL==null)
			NULL=new Null();
		return NULL;
	}
	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return Type.NULL;
	}
	@Override
	public void apply(Switch sw) {
		// TODO Auto-generated method stub
		((AbstractConstantSwitch)sw).caseNull(this);
	}
	public int hashCode(){
		return 982;
	}
	public boolean equals(Object o){
		return o instanceof Null;
	}
	@Override
	public String toString() {
		return "Null";
	}
}

// end

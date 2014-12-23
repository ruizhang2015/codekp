/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-11 обнГ03:24:11
 * @modifier: a
 * @time 2010-1-11 обнГ03:24:11
 * @reviewer: a
 * @time 2010-1-11 обнГ03:24:11
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
public class This implements Ref {
	public Type type;
	public int getSort(){
		return THIS;
	}
	public This(Type type){
		this.type=type;
	}
	public Object clone(){
		return new This(type);
	}
	@Override
	public void apply(Switch sw) {
		// TODO Auto-generated method stub
		((AbstractRefSwitch)sw).caseThis(this);
	}
	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return type;
	}
	@Override
	public String toString() {
		return "this";
	}
}

// end

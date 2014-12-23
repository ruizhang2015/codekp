/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-20
 * @modifier: a
 * @time 2010-1-20
 * @reviewer: a
 * @time 2010-1-20
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.jir;

import org.objectweb.asm.Type;

/**
 * @author a
 */
public class CaughtExceptionRef implements Ref {

	public Object clone(){
		return new CaughtExceptionRef();
	}
	public int getSort(){
		return CAUGHEXCEPTIONREF;
	}
	@Override
	public void apply(Switch sw) {
		// TODO Auto-generated method stub
		((AbstractRefSwitch)sw).caseCaughtException(this);
	}
	public String toString(){
		return "@CaughtException";
	}
	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return Type.getType("Ljava.lang.Throwable;");
	}

}

// end

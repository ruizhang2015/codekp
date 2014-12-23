/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-14
 * @modifier: a
 * @time 2010-1-14
 * @reviewer: a
 * @time 2010-1-14
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
public class StackRef implements Ref {
//	protected StackRef(int index){
//		this.index=index;
//	}
	protected StackRef(int index, Type type) {
		this.index = index;
		this.type = type;
	}
	public int getSort(){
		return STACKREF;
	}
	@Override
	public void apply(Switch sw) {
		// TODO Auto-generated method stub
		((AbstractRefSwitch)sw).caseStack(this);
	}
	public static final String TEMP="$stack";
	public int index;
	public Type type;
	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return type;
	}
	public Object clone(){
		return this;
	}
	public String getName(){
		return TEMP+index;
	}
	@Override
	public String toString() {
		return getName();
	}
	@Override
	public int hashCode() {
		return type.hashCode()+index;
	}
}

// end

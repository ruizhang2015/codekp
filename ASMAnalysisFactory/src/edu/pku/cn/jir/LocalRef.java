/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-12 обнГ03:42:31
 * @modifier: a
 * @time 2010-1-12 обнГ03:42:31
 * @reviewer: a
 * @time 2010-1-12 обнГ03:42:31
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.jir;

import org.objectweb.asm.Type;

import edu.pku.cn.classfile.LocalVariableNode;

/**
 * @author zhouzhiyi
 */
public class LocalRef implements Ref {
	public LocalVariableNode nodeRef;
	public LocalRef(LocalVariableNode ref){
		nodeRef=ref;
	}
	public String getName(){
		return nodeRef.name;
	}
	public int getSort(){
		return LOCALREF;
	}
	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return nodeRef.getType();
	}
	@Override
	public void apply(Switch sw) {
		// TODO Auto-generated method stub
		((AbstractRefSwitch)sw).caseLocal(this);
	}
	public Object clone(){
		return this;
	}
	public String toString(){
		return nodeRef.name;
	}
	public void setType(Type type){
		
	}
}

// end

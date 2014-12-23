/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-3-10
 * @modifier: a
 * @time 2010-3-10
 * @reviewer: a
 * @time 2010-3-10
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.ptg;

import edu.pku.cn.jir.FieldRef;


/**
 * Blue Node in ppg
 * @author a
 */
public class FieldNode extends Node {
	public Node base;
	public FieldRef field;
	public FieldNode(FieldRef field,Node base){
		this.field=field;
		this.base=base;
		this.type=field.getType();
	}
	public String toString(){
		return base+"."+field.getName()+":"+field.getType();
	}
	@Override
	public int getSort() {
		// TODO Auto-generated method stub
		return FIELDNODE;
	}
	@Override
	public boolean isStatic() {
		// TODO Auto-generated method stub
		return field.isStatic();
	}
}

// end

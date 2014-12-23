/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-3-15
 * @modifier: a
 * @time 2010-3-15
 * @reviewer: a
 * @time 2010-3-15
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.ptg;

import edu.pku.cn.jir.ArrayRef;

/**
 * @author a
 */
public class ArrayNode extends Node {
	public ArrayRef array;
	public ArrayNode(ArrayRef array){
		this.array=array;
		this.type=array.getType();
	}
	public String toString(){
		return array.toString();
	}
	@Override
	public int getSort() {
		// TODO Auto-generated method stub
		return ARRAYNODE;
	}
	@Override
	public boolean isStatic() {
		// TODO Auto-generated method stub
		return false;
	}
}

// end

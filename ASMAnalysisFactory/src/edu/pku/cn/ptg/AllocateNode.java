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


/**
 * Red Node in ppg
 * @author a
 */
public class AllocateNode extends Node {
//public HashMap<FieldNode, Node> fields;
	public String toString(){
		return "new "+type.toString();
	}

	@Override
	public int getSort() {
		// TODO Auto-generated method stub
		return ALLOCATENODE;
	}

	@Override
	public boolean isStatic() {
		// TODO Auto-generated method stub
		return false;
	}
}

// end

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

import edu.pku.cn.classfile.LocalVariableNode;
import edu.pku.cn.jir.Ref;

/**
 * Green Node in ppg
 * @author a
 */
public class LocalNode extends Node {
	public LocalVariableNode var;
	public Ref ref;
	public LocalNode(){
		
	}
	public LocalNode(Ref ref){
		this.ref=ref;
		this.type=ref.getType();
	}
	public String toString(){
		if(ref!=null)
		return ref.toString();
		else
			return type.toString();
	}
	@Override
	public int getSort() {
		// TODO Auto-generated method stub
		return LOCALNODE;
	}
	@Override
	public boolean isStatic() {
		// TODO Auto-generated method stub
		return false;
	}
}

// end

/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-5 обнГ02:04:08
 * @modifier: a
 * @time 2010-1-5 обнГ02:04:08
 * @reviewer: a
 * @time 2010-1-5 обнГ02:04:08
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.point;

import org.objectweb.asm.Type;

import edu.pku.cn.classfile.LocalVariableNode;

/**
 * @author zhouzhiyi
 */
public class LocalRef extends Ref{	
	public LocalVariableNode node;
	public boolean gloable=false;
	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return node.getType();
	}
}

// end

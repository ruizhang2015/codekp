/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-5 обнГ02:26:47
 * @modifier: a
 * @time 2010-1-5 обнГ02:26:47
 * @reviewer: a
 * @time 2010-1-5 обнГ02:26:47
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.point;

import org.objectweb.asm.Type;

import edu.pku.cn.classfile.FieldNode;

/**
 * @author zhouzhiyi
 */
public class FieldRef extends Ref {
	public FieldNode node;

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return node.getType();
	}
}

// end

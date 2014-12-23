/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-12 обнГ03:45:48
 * @modifier: a
 * @time 2010-1-12 обнГ03:45:48
 * @reviewer: a
 * @time 2010-1-12 обнГ03:45:48
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.jir;

import edu.pku.cn.classfile.LocalVariableNode;


/**
 * @author zhouzhiyi
 */
public class ParamRef extends LocalRef {

	public ParamRef(LocalVariableNode ref) {
		super(ref);
		// TODO Auto-generated constructor stub
	}
	public int getSort(){
		return PARAMREF;
	}
}

// end

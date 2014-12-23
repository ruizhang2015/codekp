/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-11 обнГ01:14:32
 * @modifier: a
 * @time 2010-1-11 обнГ01:14:32
 * @reviewer: a
 * @time 2010-1-11 обнГ01:14:32
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.jir;

import org.objectweb.asm.Type;

/**
 * @author zhouzhiyi
 */
public abstract class Constant implements JIRValue {

	public Object clone(){
		throw new RuntimeException();
	}
}

// end

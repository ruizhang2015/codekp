/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-11 обнГ01:10:59
 * @modifier: a
 * @time 2010-1-11 обнГ01:10:59
 * @reviewer: a
 * @time 2010-1-11 обнГ01:10:59
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
public interface JIRValue {
	public Type getType();
	public Object clone();
	public void apply(Switch sw);
//	public int getSort();
}

// end

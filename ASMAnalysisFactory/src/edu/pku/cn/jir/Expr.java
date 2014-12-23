/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-7 обнГ03:55:36
 * @modifier: a
 * @time 2010-1-7 обнГ03:55:36
 * @reviewer: a
 * @time 2010-1-7 обнГ03:55:36
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
public abstract class Expr extends Object implements JIRValue {
	public abstract Object clone();
}
// end

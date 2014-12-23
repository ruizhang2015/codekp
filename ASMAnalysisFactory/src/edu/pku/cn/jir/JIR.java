/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-11 обнГ03:04:29
 * @modifier: a
 * @time 2010-1-11 обнГ03:04:29
 * @reviewer: a
 * @time 2010-1-11 обнГ03:04:29
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.jir;
/**
 * @author zhouzhiyi
 */
public class JIR {
	public static JIRValue cloneIfNeed(JIRValue v){
		if(v instanceof Constant)
			return v;
		return (JIRValue) v.clone();
	}
}

// end

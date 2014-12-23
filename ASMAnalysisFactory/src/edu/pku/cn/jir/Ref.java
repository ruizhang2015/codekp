/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-11 обнГ02:59:42
 * @modifier: a
 * @time 2010-1-11 обнГ02:59:42
 * @reviewer: a
 * @time 2010-1-11 обнГ02:59:42
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
public interface Ref extends JIRValue {
	public static final int ARRAYREF=0;
	public static final int CAUGHEXCEPTIONREF=1;
	public static final int FIELDREF=2;
	public static final int LOCALREF=3;
	public static final int PARAMREF=4;
	public static final int STACKREF=5;
	public static final int TEMPREF=6;
	public static final int THIS=7;
	int getSort();
}

// end

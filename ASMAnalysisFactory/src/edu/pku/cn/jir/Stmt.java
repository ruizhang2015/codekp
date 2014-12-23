/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-12 обнГ01:20:09
 * @modifier: a
 * @time 2010-1-12 обнГ01:20:09
 * @reviewer: a
 * @time 2010-1-12 обнГ01:20:09
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
public interface Stmt {
	public static final int ASSIGNSTMT=0;
	public static final int INVOKESTMT=1;
	public static final int IFSTMT=2;
	public static final int GOTOSTMT=3;
	public static final int LOOKUPSWITCHSTMT=4;
	public static final int TABLESWITCHSTMT=5;
	public static final int LABLESTMT=6;
	public static final int LINESTMT=7;
	public static final int POPSTMT=8;
	public static final int RETSTMT=9;
	public static final int RETURNSTMT=10;
	public static final int THROWSTMT=11;
	public static final int ENTERMONITORSTMT=12;
	public static final int EXITMONITORSTMT=13;
	public void setRegion(int start,int end);
	public int getIndex();
	public void setIndex(int index);
	public void apply(Switch sw);
	public void accept(JIRVisitor jv);
	public int getSort();
}

// end

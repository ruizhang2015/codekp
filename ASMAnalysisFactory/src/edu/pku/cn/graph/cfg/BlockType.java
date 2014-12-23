/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-6-15 ����01:31:14
 * @modifier: Liuxizhiyi
 * @time 2008-6-15 ����01:31:14
 * @reviewer: Liuxizhiyi
 * @time 2008-6-15 ����01:31:14
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.cfg;

/**
 * 
 * @author Liuxizhiyi
 */
public interface BlockType {
	/*
	 * ----------------------------------------------------------------------
	 * Block types
	 * ----------------------------------------------------------------------
	 */

	public static final int NORMAL_BLOCK = 0;

	public static final int IF_BLOCK = 1;

	public static final int SWITCH_BLOCK = 2;

	public static final int WHILE_BLOCK = 3;

	public static final int DOWHILE_BLOCK = 4;

	public static final int EXCEPTION_BLOCK = 5;

	public static final int UNHANDLE_EXCEPTION_BLOCK = 6;

	public static final int RETURN_BLOCK = 7;

	public static final String[] Type = { "NORMAL_BLOCK", "IF_BLOCK", "SWITCH_BLOCK", "WHILE_BLOCK", "DOWHILE_BLOCK",
			"EXCEPTION_BLOCK", "UNHANDLE_EXCEPTION_BLOCK", "RETURN_BLOCK" };
}

// end

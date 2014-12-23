/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-6-22 ����12:37:33
 * @modifier: Liuxizhiyi
 * @time 2008-6-22 ����12:37:33
 * @reviewer: Liuxizhiyi
 * @time 2008-6-22 ����12:37:33
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.user;

/**
 * 
 * @author Liuxizhiyi
 */
public class Priorities {
	/**
	 * Experimental priority for bug instances.
	 */
	public static final int EXP_PRIORITY = 4;
	/**
	 * High priority for bug instances.
	 */
	public static final int HIGH_PRIORITY = 3;
	/**
	 * Low priority for bug instances.
	 */
	public static final int LOW_PRIORITY = 2;

	/**
	 * Normal priority for bug instances.
	 */
	public static final int NORMAL_PRIORITY = 1;

	/**
	 * priority for bug instances that should be ignored
	 */
	public static final int IGNORE_PRIORITY = 0;

	public final static String[] Types = { "IGNORE_PRIORITY", "NORMAL_PRIORITY", "LOW_PRIORITY", "HIGH_PRIORITY",
			"EXP_PRIORITY" };

	public static int boundedPriority(int priority) {
		return Math.max(Math.min(priority, Priorities.EXP_PRIORITY), Priorities.IGNORE_PRIORITY);
	}

	public static String getType(int priority) {
		return Types[boundedPriority(priority)];
	}
}

// end

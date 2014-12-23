/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-5-30 ����05:18:41
 * @modifier: Liuxizhiyi
 * @time 2008-5-30 ����05:18:41
 * @reviewer: Liuxizhiyi
 * @time 2008-5-30 ����05:18:41
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.asm.tree.analysis;

/**
 * 
 * @author Liuxizhiyi
 */
public class ValueException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5989457908995774088L;

	public ValueException(String message) {
		super(message);
	}

	public ValueException(String message, Throwable e) {
		super(message, e);
	}
}

// end

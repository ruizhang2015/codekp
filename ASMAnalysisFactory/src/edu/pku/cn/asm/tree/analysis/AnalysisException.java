/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-6-1 ����11:52:58
 * @modifier: Liuxizhiyi
 * @time 2008-6-1 ����11:52:58
 * @reviewer: Liuxizhiyi
 * @time 2008-6-1 ����11:52:58
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
public class AnalysisException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2520115911700107476L;

	/**
	 * 
	 */
	public AnalysisException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public AnalysisException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public AnalysisException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public AnalysisException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}

// end

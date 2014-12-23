/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time Oct 20, 2008 2:27:42 PM
 * @modifier: Administrator
 * @time Oct 20, 2008 2:27:42 PM
 * @reviewer: Administrator
 * @time Oct 20, 2008 2:27:42 PM
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.testcase;

public class TestExit {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s = new String("This is the first string");

		String s2 = new String("This is the second string");

		System.runFinalizersOnExit(true);

		Runtime.runFinalizersOnExit(true);

		System.gc();

		Runtime r = Runtime.getRuntime();
		r.gc();

		System.exit(0);
	}

}

// end

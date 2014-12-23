/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time Oct 20, 2008 2:39:49 PM
 * @modifier: Administrator
 * @time Oct 20, 2008 2:39:49 PM
 * @reviewer: Administrator
 * @time Oct 20, 2008 2:39:49 PM
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.testcase;

public class TestRuntimeExec_getExitValue_notWait {

	/**
	 * @param args
	 */
	void right() throws Exception {
		Process p = Runtime.getRuntime().exec("java -version");
		p.waitFor();
		System.out.println(p.exitValue());
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Process p = Runtime.getRuntime().exec("java -version");

		System.out.println(p.exitValue());

	}

}

// end

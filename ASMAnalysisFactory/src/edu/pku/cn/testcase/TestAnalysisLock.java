/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-2-13 ����11:18:57
 * @modifier: Administrator
 * @time 2009-2-13 ����11:18:57
 * @reviewer: Administrator
 * @time 2009-2-13 ����11:18:57
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.testcase;

public class TestAnalysisLock {

	private Object o1;

	private String s2;

	public void testLockOnO1() {
		synchronized (o1) {
			s2 = "This is an assignment from testLockOnO1";
		}
	}

	public synchronized void testLockOnThis() {
		s2 = "This is an assignment from testLockOnThis";
	}

}

// end

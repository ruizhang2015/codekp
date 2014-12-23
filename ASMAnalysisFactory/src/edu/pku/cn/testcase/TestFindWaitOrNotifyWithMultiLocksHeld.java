/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-2-19 ����03:15:25
 * @modifier: Administrator
 * @time 2009-2-19 ����03:15:25
 * @reviewer: Administrator
 * @time 2009-2-19 ����03:15:25
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.testcase;

public class TestFindWaitOrNotifyWithMultiLocksHeld {

	private String ss1 = "A private field";

	private Object field2 = new Object();

	public synchronized void testcaseWithTwoLocks() {
		System.out.println("This is a test case trying to form up two locks");
		synchronized (ss1) {
			ss1 = "Change the synchronized field";
			try {
				field2.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

// end

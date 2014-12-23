/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-2-18 ����02:50:32
 * @modifier: Administrator
 * @time 2009-2-18 ����02:50:32
 * @reviewer: Administrator
 * @time 2009-2-18 ����02:50:32
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.testcase;

public class TestFindWaitOrNotifyWithoutLock {

	private Object o1;

	private Object o2;

	public void testWaitOnO1() {
		synchronized (o1) {
			try {
				o1.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void testNotifyOnO2() {
		o2.notify();
	}

	public void test3() {
		String ss = "Hello!";
		try {
			ss = "This is another test";
			if (ss == null) {
				synchronized (ss) {
					ss.notify();
				}
			} else {
				ss = "";
			}
		} finally {
			synchronized (ss) {
				ss.notify();
			}
		}
	}

}

// end

/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-2-19 ����02:14:49
 * @modifier: Administrator
 * @time 2009-2-19 ����02:14:49
 * @reviewer: Administrator
 * @time 2009-2-19 ����02:14:49
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.testcase;

public class TestFindSleepWithLockHeld {

	public static Object o = new Object();

	Thread t = null;

	public synchronized void testcaseT1() {
		t = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("This is a new thread");
			}
		});
		try {
			t.sleep(20);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testcaseT2() {
		synchronized (t) {
			try {
				Thread.sleep(200l);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void testcaseT3() {
		try {
			Thread.sleep(2000l);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

// end

/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-1-11 ����11:38:08
 * @modifier: Administrator
 * @time 2009-1-11 ����11:38:08
 * @reviewer: Administrator
 * @time 2009-1-11 ����11:38:08
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.testcase;

public class TestCallList {

	class TestClass {

		private boolean flag = false;

		public void t1() {
			System.out.println("This is the first method in TestClass");
		}

		public int t2() {
			return 0;
		}

		public void open() {
			if (!flag)
				flag = true;
		}

		public void close() {
			if (flag)
				flag = false;
		}
	}

	public void example() {
		TestClass t = new TestClass();
		t.t1();
		t.t2();
		t.open();
		boolean flag = false;
		if (flag & true) {
			t.close();
		} else {
			t.open();
			t.close();
		}
		t.close();
		t.close();
		t.open();
	}
}

// end

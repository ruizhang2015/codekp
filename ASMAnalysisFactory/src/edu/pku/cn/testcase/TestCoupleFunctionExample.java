/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author lijinhui
 * @time Oct 30, 2008 3:27:06 PM
 * @modifier: lijinhui
 * @time Oct 30, 2008 3:27:06 PM
 * @reviewer: lijinhui
 * @time Oct 30, 2008 3:27:06 PM
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.testcase;

public class TestCoupleFunctionExample {

	void f1(int a, int b) {
	}

	void f2(int a) {
	}

	void wrong() {
		f1(2, 4);
	}

	void wrong2(TestCoupleFunctionExample t3) {
		int bb = -2;
		bb--;
		f1(2, Math.abs(100 * bb));
		// f2(Math.abs(100*bb));
		TestCoupleFunctionExample t1 = new TestCoupleFunctionExample();
		t1.f1(2, Math.abs(100 * bb));
		TestCoupleFunctionExample t2 = new TestCoupleFunctionExample();
		t2.f1(2, Math.abs(100 * bb));
		t3.f1(bb, bb * bb);
		// t1.f2(Math.abs(1001*bb));
		// t2.f2(Math.abs(1050*bb));
	}

	void right(TestCoupleFunctionExample t3) {
		int bb = -2;
		bb--;
		f1(2, Math.abs(100 * bb));

		TestCoupleFunctionExample t1 = new TestCoupleFunctionExample();
		t1.f1(2, Math.abs(100 * bb));
		TestCoupleFunctionExample t2 = new TestCoupleFunctionExample();
		t2.f1(2, Math.abs(100 * bb));
		t3.f1(bb, bb * bb);
		// t1.f2(Math.abs(1001*bb));
		// t2.f2(Math.abs(1050*bb));
		f2(Math.abs(100 * bb));
		t1.f2(1);
		t2.f2(2);
		t3.f2(3);
		new TestCoupleFunctionExample().f1(2, 3);
	}

}

// end

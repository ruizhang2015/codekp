/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-1-5 ����11:19:02
 * @modifier: Administrator
 * @time 2009-1-5 ����11:19:02
 * @reviewer: Administrator
 * @time 2009-1-5 ����11:19:02
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.testcase;

public class TestFindNoSuperCall implements Cloneable {

	public Object clone() {
		System.out.println("This is a defective example since super.clone() is not called");
		return new TestFindNoSuperCall();
	}

	private class Test2 implements Cloneable {
		public Object clone() {
			System.out.println("This example should not be identified as a defect since the class is private");
			return new Test2();
		}
	}
}

// end

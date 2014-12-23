/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-1-4 ����07:59:39
 * @modifier: Administrator
 * @time 2009-1-4 ����07:59:39
 * @reviewer: Administrator
 * @time 2009-1-4 ����07:59:39
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.testcase;

public class TestFindImplClassNoDefOrUseMethod implements Cloneable {

	public static void main(String[] args) {
		System.out.println("This is a defective program since clone() method is not defined");
	}
}

class Test2 implements Cloneable {
	public void test2() {
		Test2 instance = new Test2();
		try {
			Test2 instance2 = (Test2) instance.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class Test3 implements Cloneable {
		public void test3() {
		}

		public Object clone() {
			return null;
		}

		class Test4 implements Cloneable {
			public void test4() {

			}

			public Object clone() {
				return null;
			}
		}
	}
}

// end

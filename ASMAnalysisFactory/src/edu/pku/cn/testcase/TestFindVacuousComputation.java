/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-1-8 ����06:12:19
 * @modifier: Administrator
 * @time 2009-1-8 ����06:12:19
 * @reviewer: Administrator
 * @time 2009-1-8 ����06:12:19
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.testcase;

public class TestFindVacuousComputation {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int m = 500;
		if (m <= Integer.MAX_VALUE) {
			System.out.println("m <= Integer.MAX_VALUE");
		}
		if (m >= Integer.MIN_VALUE) {
			System.out.println("m > Integer.MIN_VALUE");
		}
		if (Integer.MAX_VALUE < m) {
			System.out.println("m > Integer.MAX_VALUE");
		}
		if (Integer.MIN_VALUE > m) {
			System.out.println("m < Integer.MIN_VALUE");
		}
		int i = 1;
		if (5 % i != 0) {
			System.out.println("This computation is valid");
		}
	}
}

// end

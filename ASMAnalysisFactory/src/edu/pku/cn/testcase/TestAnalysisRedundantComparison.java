/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-4-1 ����08:32:47
 * @modifier: Administrator
 * @time 2009-4-1 ����08:32:47
 * @reviewer: Administrator
 * @time 2009-4-1 ����08:32:47
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.testcase;

public class TestAnalysisRedundantComparison {

	public static void main(String[] args) {
		int m = 49;
		int n = 14;
		if (m > n) {
			System.out.println("Hello, it is a certain result");
		} else {
			System.out.println("No, this is impossible");
		}
	}
}

// end

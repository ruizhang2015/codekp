/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-3-4 ����08:03:06
 * @modifier: Administrator
 * @time 2009-3-4 ����08:03:06
 * @reviewer: Administrator
 * @time 2009-3-4 ����08:03:06
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.testcase;

public class TestFindWeakLoopController {

	public static void main(String[] args) {
		int n = 100;
		for (int i = 0; i < 100; i++) {
			for (int j = 0; i < n; i++) {
				j = j + i;
			}
		}

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; i++) {
				j = j + i;
			}
		}
	}
}

// end

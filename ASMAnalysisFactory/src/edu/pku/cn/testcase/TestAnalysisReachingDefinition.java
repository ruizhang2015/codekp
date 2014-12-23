/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-3-27 ����10:09:54
 * @modifier: Administrator
 * @time 2009-3-27 ����10:09:54
 * @reviewer: Administrator
 * @time 2009-3-27 ����10:09:54
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.testcase;

public class TestAnalysisReachingDefinition {

	public void test() {
		int m = 0;
		int n = 100;
		String ss = null;
		while (n > m) {
			switch (n) {
			case 0:
				ss = "Case 0";
				n += 2;
				break;
			case 1:
				ss = "Case 1";
				n += 3;
				break;
			case 2:
				ss = "Case 2";
				n += 4;
				break;
			case 3:
				ss = "Case 3";
				n -= 1;
				break;
			case 4:
				ss = "Case 4";
				n -= 2;
				break;
			}
			if (n > 90) {
				ss = "n > 90";
				n -= 10;
			} else if (n > 80) {
				ss = "n > 80";
				n -= 10;
			} else if (n > 70) {
				ss = "n > 70";
				n -= 10;
			}
		}
	}
}

// end

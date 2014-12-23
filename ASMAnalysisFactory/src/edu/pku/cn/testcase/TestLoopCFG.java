/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Meng Na
 * @time 2009-3-23 ����08:46:15
 * @modifier: Meng Na
 * @time 2009-3-23 ����08:46:15
 * @reviewer: Meng Na
 * @time 2009-3-23 ����08:46:15
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.testcase;

public class TestLoopCFG {

	public int test1() {
		int n = 100;
		int result = 0;
		try {
			for (int i = 0; i < n; i++) {
				result += i;
			}
			result = 1;
			int j = 3;
			do {
				result *= j;
				j++;
			} while (result < 100);
			int m = 1000;
			n = 10;
			while (m != n) {
				m = m / n;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return n;
	}
}

// end

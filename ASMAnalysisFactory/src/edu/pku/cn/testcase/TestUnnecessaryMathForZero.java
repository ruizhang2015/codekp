/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author MENG Na
 * @time 2008-11-3 ����10:11:50
 * @modifier: MENG Na
 * @time 2008-11-3 ����10:11:50
 * @reviewer: MENG Na
 * @time 2008-11-3 ����10:11:50
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.testcase;

public class TestUnnecessaryMathForZero {

	public static void main(String[] args) {
		Math.sin(0);
		Math.ceil((double) 2);
	}
}

// end

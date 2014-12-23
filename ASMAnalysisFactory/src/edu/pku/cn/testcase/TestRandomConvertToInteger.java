/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-1-9 ����09:21:48
 * @modifier: Administrator
 * @time 2009-1-9 ����09:21:48
 * @reviewer: Administrator
 * @time 2009-1-9 ����09:21:48
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.testcase;

import java.util.Random;

public class TestRandomConvertToInteger {

	public static void main(String[] args) {
		Random r = new Random();
		System.out.println((int) r.nextDouble());

		System.out.println((int) Math.random());

		System.out.println((int) (r.nextDouble() * 2));

		System.out.println((int) (Math.random() * 5));

	}
}

// end

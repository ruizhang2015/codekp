/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-4-2 ����07:12:46
 * @modifier: Administrator
 * @time 2009-4-2 ����07:12:46
 * @reviewer: Administrator
 * @time 2009-4-2 ����07:12:46
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.testcase;

import org.objectweb.asm.tree.analysis.AnalyzerException;

public class TestAnalysisIsNullValue {

	public void test1(Object o) {
		o=null;
		String ss = null;
		if (ss.toString() == null) {
			System.out.println("this is a null-string");
		} else {
			System.out.println("This is a nonnull-string");
		}
	}

	public void test2() {
		Object o1 = new Object();
		if (o1 == null) {
			System.out.println("This is a null object");
		} else {
			o1 = null;
			System.out.println(o1.hashCode());
		}
	}

	public void test3() {
		Object o1 = new Object();
		Object o2 = null;

		try {
			if (o2 == null)
				throw new Exception("This is an Exception instance");
			return;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			o2 = new Object();
		}
	}
}

// end

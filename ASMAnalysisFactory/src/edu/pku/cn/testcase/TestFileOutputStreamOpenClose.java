/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time Oct 20, 2008 3:22:10 PM
 * @modifier: Administrator
 * @time Oct 20, 2008 3:22:10 PM
 * @reviewer: Administrator
 * @time Oct 20, 2008 3:22:10 PM
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.testcase;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TestFileOutputStreamOpenClose {

	void testwrong() {
		OutputStream out = null;
		try {
			out = new BufferedOutputStream(new FileOutputStream(""));

		} catch (IOException e) {

		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
	}

	void test() {
		try {
			FileOutputStream fop = new FileOutputStream("abc");
			fop.write(2);
			fop.write(2);
			fop.write(2);
			fop.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void right() {
		try {
			FileOutputStream fop = new FileOutputStream("abc");
			fop.write(2);
			fop.write(2);
			fop.write(2);
			fop.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			FileOutputStream fop = new FileOutputStream("abc");
			fop.write(2);
			fop.write(2);
			fop.write(2);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

// end

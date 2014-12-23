/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-1-2 ����11:12:51
 * @modifier: Administrator
 * @time 2009-1-2 ����11:12:51
 * @reviewer: Administrator
 * @time 2009-1-2 ����11:12:51
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.testcase;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TestFindBadUseOfReturnValue {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int constant = 100;
		String temp1 = "This is a test for bad use of return value.";
		if (temp1.indexOf("This") > 0) {
			System.out.println("The string contains the substring");
		}
		try {
			BufferedReader in = new BufferedReader(new FileReader("customization_for_returnBadUse.txt"));
			if (in.readLine() != null) {
				System.out.println(in.readLine().indexOf("This"));
			}
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

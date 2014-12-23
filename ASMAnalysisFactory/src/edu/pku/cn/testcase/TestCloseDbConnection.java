/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time Oct 20, 2008 4:22:59 PM
 * @modifier: Administrator
 * @time Oct 20, 2008 4:22:59 PM
 * @reviewer: Administrator
 * @time Oct 20, 2008 4:22:59 PM
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.testcase;

import java.io.File;
import java.sql.*;
import java.util.Properties;

public class TestCloseDbConnection {
	int j = 2;
	String temp = "";
	int tempSize = 0;
	Connection con;

	TestCloseDbConnection(){
		
	}
	
	// 计算历次temp的size的和
	int sychronizedMethod(String str) {
		synchronized (temp) {
			if (str == null) {
				return tempSize;
			} else {
				temp += str;
				tempSize += temp.length();
			}
		}
		return j;
	}

	void wrong(Connection con3) {
		try {
			// con = DriverManager.getConnection("");

			// int a = 100;
			// int c = a + j;
			//
			// Connection con2 = DriverManager.getConnection("222");
			// con2.commit();
			// con = con2;

			con = DriverManager.getConnection("333");
			
			// String str = con.toString();
			//

		} catch (Exception e) {
			
		} finally {

			try {
				con.close();
			} catch (SQLException e) {
				
			}

			// if (con == null)
			// return;
			//			
			// try {
			// con.close();
			// } catch (SQLException e) {
			// e.printStackTrace();
			// }
		}
	}

	void wrong2(Connection con3) {
		try {
			// con = DriverManager.getConnection("");

			// int a = 100;
			// int c = a + j;
			//
			// Connection con2 = DriverManager.getConnection("222");
			// con2.commit();
			// con = con2;

			con3 = DriverManager.getConnection("333");

			// String str = con.toString();
			//
			// con.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.clearWarnings();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

}

// end

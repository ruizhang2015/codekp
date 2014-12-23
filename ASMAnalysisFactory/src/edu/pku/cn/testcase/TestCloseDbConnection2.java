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

public class TestCloseDbConnection2 {
	
	Connection con;


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
			con.commit();
			
			con.close();
			// String str = con.toString();
			//

		} catch (Exception e) {
			
		} finally {

//			try {
//				con.close();
//			} catch (SQLException e) {
//				
//			}

			
		}
	}


}

// end

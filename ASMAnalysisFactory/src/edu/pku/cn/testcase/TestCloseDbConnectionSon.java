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

public class TestCloseDbConnectionSon extends TestCloseDbConnection {
	
	public TestCloseDbConnectionSon(int j) {
		super(j);
		// TODO Auto-generated constructor stub
	}

	private void closeConnection(Connection c) throws SQLException{
		c.close();
	}
	
	private void closeConnection2(Connection c) throws SQLException{
		this.closeConnection3(c);
	}
	
	private void closeConnection3(Connection c) throws SQLException{
		this.closeConnection4(c);
	}
	
	private void closeConnection4(Connection c) throws SQLException{
		this.closeConnection5(c);
	}
	
	private void closeConnection5(Connection c) throws SQLException{
		this.closeConnection6(c);
	}
	
	private void closeConnection6(Connection c) throws SQLException{
		this.closeConnection7(c);
	}
	
	private void closeConnection7(Connection c) throws SQLException{
		c.close();
	}
	
	private void closeConnection1(Connection c) throws SQLException{
		this.closeConnection2(c);
	}
	
	void f1(Connection c){
		this.f2(c);
	}
	
	public void f2(Connection c){
		this.f3(c);
	}
	
	void f3(Connection c){
		this.f4(c);
	}
	
	void f4(Connection c){
		this.f5(c);
	}
	
	void f5(Connection c){
		try {
			this.f6(c);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	void f6(Connection c) throws SQLException{
		c.close();
	}

	void right2() {
		try {
			Connection con = DriverManager.getConnection("");
			// con.close();
			Connection con2 = DriverManager.getConnection("", new Properties());
			// con2.close();
			Connection con3 = DriverManager.getConnection("", "", "");
			// con3.close();
			Connection con4 = DriverManager.getConnection("", "", "");
			
			this.closeConnection(con);

			con = con2;
			this.f1(con);
			
			Test test = new Test();
			test.f1(con3);
			
			con3 = con4;
			con3.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

// end

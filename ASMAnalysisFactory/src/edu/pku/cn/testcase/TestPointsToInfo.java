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

import java.io.FileNotFoundException;
import java.sql.*;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.objectweb.asm.Type;

import edu.pku.cn.jir.JIRValue;
import edu.pku.cn.jir.Switch;

public class TestPointsToInfo implements testImpl {
 
	Set<String> set;
	String str;
	int i;
	boolean b = true;
	private Set<String> set2;
	Test test;
	
	TestPointsToInfo() {
	}
	
	TestPointsToInfo(int i) {
		this.i = i;
	}
	
	String right(String a)  throws NoSuchFieldException {
		try {
//			int i = 1;
//			int b = i;
//			int c = i + b;
//			c = i + 3 * 2;
//			
//			this.set = new HashSet<String>();
//			this.set.add("sina");
//			Set<String> localSet = new HashSet<String>();
//			
//			a = "hello";
//			TestPointsToInfo newTest = new TestPointsToInfo();
//			TestPointsToInfo newTest2 = new TestPointsToInfo();
//			newTest = this;
//			
//			str = a;
//			str = "newHello";
//			
//			newTest2.set = this.set;
//			
//			localSet = newTest.set;
//			
			Connection con = DriverManager.getConnection("");
//
//			Connection con2 = DriverManager.getConnection("", new Properties());
//			Connection con4; 
//			Connection con5 =con2;
//			con4 = con2;
//			con2 = con4;
//			con4.close();
//			
//			Connection con3 = DriverManager.getConnection("", "", "");
//			con4 = con3;
//			con4.close();
//			
//			con.close();
//
//			i = 100;
			
//			int i2 = this.i;
//			set2 = this.set;
//			i2 = this.i + this.i;
//			this.set.size();
//			int size = this.set.size();
			
//			if(this.set == null || i > 10){
//					throw new NoSuchFieldException("not null");
//			}
			boolean isRight = true;
			
			if(this.isSetNull() == null){
				throw new NoSuchFieldException("not null");
			}
			
			//if(b == false){
				//throw new NoSuchFieldException("not null");
			//}
			
//			else {
//				if(i < 10){
//					throw new NoSuchFieldException("not null");
//				}
//			}
			
//			String[] strs = new String[2];
//			strs[0] = "str0";
//			strs[1] = "str1";
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;
	}

	public boolean isBRight(){
		try {
			new PrintWriter("");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.b == false;
	}
	
	public Set<String> isSetNull(){
		Test tmpTest = this.test;
		try {
			new PrintWriter("");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// this.test.set = new HashSet<String>();
		
		return tmpTest.set;
	}
	
	public boolean isB(){
		try {
			new PrintWriter("");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.b;
	}
	
	void changeB() {
		this.b = false;
	}
	
	boolean isIRight(){
		return this.i > 10;
	}
	
	void changeI(){
		this.i = 17;
	}
	
	Set meetPrecondition(){
		this.set = null;
		return this.set;
	}
	
	
	boolean judge() {
		return this.set != null;
	}
	
	Set meetPrecondition2(){
		return this.set;
	}
	
	Set meetPrecondition3(){
		HashSet<String> localSet = null;
		this.set = localSet;
		return localSet;
	}
	
	Set notMeetPrecondition(){
		HashSet<String> localSet = new HashSet<String> ();
		this.set = localSet;
		return localSet;
	}
	
	Set doNothing(Set localSet2){
		try {
			this.right("");
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return localSet2;
	}
	
	String meetPrecondition4(String a, Set<String> localSet2) {
		Set localSet4 = doNothing(localSet2);
		this.set = localSet4;
		return a;
	}

	int meetPrecondition5(int a, Set<String> localSet2) {
		Set localSet4 = doNothing(localSet2);
		this.set = localSet4;
		this.set.size();
		int j = 100 + a;
		return j;
	}

	public void apply(Switch sw) {
		// TODO Auto-generated method stub
		
	}

	public Type getType() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isBWrong() {
		// TODO Auto-generated method stub
		return false;
	}
	
//	void wrong2(int a) {
//		try {
//			
//			Connection con = DriverManager.getConnection("");
//
//			Connection con2 = DriverManager.getConnection("", new Properties());
//			Connection con3 = DriverManager.getConnection("", "", "");
//			
//			
//			if( a > 10 ){
//				con2.close();
//			} else
//				con3.close();
//			
//			int i = 2;
//			while(i > 0){
//				con.close();
//			}
//			
//			con3.close();
//			// con.close();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	void wrong() {
//		try {
//			Connection con = DriverManager.getConnection("");
//			// con.close();
//			Connection con2 = DriverManager.getConnection("", new Properties());
//			// con2.close();
//			Connection con3 = DriverManager.getConnection("", "", "");
//			// con3.close();
//
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

}

// end

package edu.pku.cn.util;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.objectweb.asm.Type;

public class DBUtil {
	static HashMap<String, Integer> opcodeStringMap = new HashMap<String, Integer>();
	static final String DBUrl = "jdbc:mysql://127.0.0.1:3306/resourceUsageEtt";
	static final String DBUser = "root";
	static final String DBPwd = "wuqian";

	// generate automachine from a database
	public static boolean generateAllMethodsRelationshipFromDatabase() {

		Connection connection;
		Statement statement;
		ResultSet resultset;

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			connection = DriverManager.getConnection(DBUrl, DBUser, DBPwd);

			String amSql = "select * from methodsrelation";
			statement = connection.createStatement();
			resultset = statement.executeQuery(amSql);

			while (resultset.next()) {

				System.out.println(resultset.getInt("id"));
				System.out.println(resultset.getString("className"));
				System.out.println(resultset.getString("beforeMethod"));
				System.out.println(resultset.getString("afterMethod"));
				System.out.println(resultset.getBoolean("relationship"));
				System.out.println(resultset.getInt("methodsNumber"));
				System.out.println(resultset.getString("remark"));

			}// while am

		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}
	
	public static ResultSet executeQuery(String sql) {

		Connection connection;
		Statement statement;
		ResultSet resultset = null;

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(DBUrl, DBUser, DBPwd);
			statement = connection.createStatement();
			resultset = statement.executeQuery(sql);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultset;
	}

	public static int getResultSetSize(ResultSet resultSet) {
	    int size = -1;

	    try {
	        resultSet.last();
	        size = resultSet.getRow();
	        resultSet.beforeFirst();
	    } catch(SQLException e) {
	        return size;
	    }

	    return size;
	}

	// relationship: Sequential_Exp; Sequential_NonExp; InnerInvoke_Exp
	public static boolean saveMethodsRelationshipIntoDatabase(String className, String beforeMethod,
			String afterMethod, String relationship, int methodsNumber, String remark) {

		Connection connection;
		Statement statement;
		boolean succeed = true;

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(DBUrl, DBUser, DBPwd);
			String sql = "insert into methodsrelation(className, beforeMethod, afterMethod, relationship, methodsNumber, remark) values "
					+ "('"
					+ className
					+ "', "
					+ "'"
					+ beforeMethod
					+ "', "
					+ "'"
					+ afterMethod
					+ "', "
					+ "'"
					+ relationship + "', " + methodsNumber + ", '" + remark + "')";
			System.out.println(sql);
			statement = connection.createStatement();
			statement.execute(sql);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			succeed = false;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			succeed = false;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			succeed = false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			succeed = false;
		}
		return succeed;
	}
	
	public static boolean executeSQLStmt(String sql) {

		Connection connection;
		Statement statement;
		boolean succeed = true;

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(DBUrl, DBUser, DBPwd);
			statement = connection.createStatement();
			statement.execute(sql);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			succeed = false;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			succeed = false;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			succeed = false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			succeed = false;
		}
		return succeed;
	}
	

//	 relationship: Sequential_Exp; Sequential_NonExp; InnerInvoke_Exp
	public static boolean saveMethodsRelationship(String proName, String className, String bmAccessibility, String beforeMethod,
			String amAccessibility, String afterMethod, String relationship, int methodsNumber, String remark) {

		Connection connection;
		Statement statement;
		boolean succeed = true;

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(DBUrl, DBUser, DBPwd);
			String sql = "insert into MethodsRelation(projectName, className, beforeMethodAccessibility, beforeMethod, afterMethodAccessibility, afterMethod, relationship, publicMethodsNumber, remark) values "
					+ "('"
					+proName
					+"', '"
					+ className
					+ "', "
					+ "'"
					+ bmAccessibility
					+ "', "
					+ "'"
					+ beforeMethod
					+ "', "
					+ "'"
					+ amAccessibility
					+ "', "
					+ "'"
					+ afterMethod
					+ "', "
					+ "'"
					+ relationship + "', " + methodsNumber + ", '" + remark + "')";
			System.out.println(sql);
			statement = connection.createStatement();
			statement.execute(sql);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			succeed = false;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			succeed = false;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			succeed = false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			succeed = false;
		}
		return succeed;
	}
	
	public static boolean saveInitMethods(String providedClassName, String invokeMethodName, String subjectClassName,
			String subjectInitMethodName) {

		Connection connection;
		Statement statement;
		boolean succeed = true;
		String relationShip = "Invoke_Init";

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(DBUrl, DBUser, DBPwd);
			String sql = "insert into initcleanupmethods(providedClassName, invokeMethodName, subjectClassName, initMethodName, relationship) values "
					+ "('"
					+ providedClassName
					+ "', "
					+ "'"
					+ invokeMethodName
					+ "', "
					+ "'"
					+ subjectClassName
					+ "', " + "'" + subjectInitMethodName + "', '" + relationShip + "')";
			System.out.println(sql);
			statement = connection.createStatement();
			statement.execute(sql);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			succeed = false;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			succeed = false;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			succeed = false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			succeed = false;
		}
		return succeed;
	}

	public static boolean saveMethodRelationInterface(String className, String interfaceName) {

		Connection connection;
		Statement statement;
		boolean succeed = true;

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(DBUrl, DBUser, DBPwd);
			String sql = "insert into methodrelationinterface(ClassName, InterfaceName) values "
					+ "('"
					+ className
					+ "', "
					+ "'"
					+ interfaceName
					+ "')";
			System.out.println(sql);
			statement = connection.createStatement();
			statement.execute(sql);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			succeed = false;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			succeed = false;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			succeed = false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			succeed = false;
		}
		return succeed;
	}
	
	public static boolean saveMethodInterface(String className, String interfaceName) {

		Connection connection;
		Statement statement;
		boolean succeed = true;

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(DBUrl, DBUser, DBPwd);
			String sql = "insert into methodinterface(ClassName, InterfaceName) values "
					+ "('"
					+ className
					+ "', "
					+ "'"
					+ interfaceName
					+ "')";
			System.out.println(sql);
			statement = connection.createStatement();
			statement.execute(sql);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			succeed = false;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			succeed = false;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			succeed = false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			succeed = false;
		}
		return succeed;
	}

	public static boolean saveClassInterface(String className, String interfaceName) {

		Connection connection;
		Statement statement;
		boolean succeed = true;

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(DBUrl, DBUser, DBPwd);
			String sql = "insert into classinterface(ClassName, InterfaceName) values "
					+ "('"
					+ className
					+ "', "
					+ "'"
					+ interfaceName
					+ "')";
			System.out.println(sql);
			statement = connection.createStatement();
			statement.execute(sql);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			succeed = false;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			succeed = false;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			succeed = false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			succeed = false;
		}
		return succeed;
	}
	
	public static boolean saveClassParent(String className, String interfaceName) {

		Connection connection;
		Statement statement;
		boolean succeed = true;

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(DBUrl, DBUser, DBPwd);
			String sql = "insert into classparent(ClassName, InterfaceName) values "
					+ "('"
					+ className
					+ "', "
					+ "'"
					+ interfaceName
					+ "')";
			System.out.println(sql);
			statement = connection.createStatement();
			statement.execute(sql);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			succeed = false;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			succeed = false;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			succeed = false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			succeed = false;
		}
		return succeed;
	}
	
	public static void main(String[] args) {
		// generateAllMethodsRelationshipFromDatabase();
		// System.out.println(saveMethodsRelationshipIntoDatabase("class", "a",
		// "b", "Sequential_Exp", 44, "remark" ));
		System.out.println(DBUtil.saveInitMethods("p1", "m1", "s1", "sm1"));
		System.out.println(DBUtil.saveMethodInterface("m1", "i1"));
	}

}

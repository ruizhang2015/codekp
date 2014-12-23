/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author swensen
 * @time 2011-2-9
 * @modifier: swensen
 * @time 2011-2-9
 * @reviewer: swensen
 * @time 2011-2-9
 * (C) Copyright PKU Software Lab. 2011
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package experiment.icse;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import automachine.AutoMachine;
import automachine.AutomaUtil;
import automachine.Edge;
import automachine.State;

/**
 * @author swensen
 */
/**
 * 
 * @author swensen
 */
public class Utils {

	static final String DBUrl = "jdbc:mysql://127.0.0.1:3306/testcasegenerator";// "jdbc:mysql://192.168.4.131:3306/jcdlib";
	static final String DBUser = "root";
	static final String DBPwd = "111111";// "massjcdlib";

	static void readWarningsFromFindBugsReportIntoDatabase(String filePath2, String proName2, String toolName2) {

		Connection amConnection;
		Statement amStatement;
		ResultSet amResultset;
		String proName, className, methodName, fromLine = null, toLine = null, type, lineRole, message = null, precise, actionable, topprecision, toolName, remark;
		String varName;

		String filePath = filePath2;
		proName = proName2;
		toolName = toolName2;

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			amConnection = DriverManager.getConnection(DBUrl, DBUser, DBPwd);

			// read info from xml and then construct the sql statements
			SAXBuilder sb = new SAXBuilder();

			Document doc = sb.build(new FileInputStream(filePath));
			Element ele = doc.getRootElement();

			List<Element> bugs = ele.getChildren("BugInstance");
			for (Element bug : bugs) {
				type = bug.getAttributeValue("type");
				Element hitMethod = bug.getChild("Method");
				className = hitMethod.getAttributeValue("classname");
				methodName = hitMethod.getAttributeValue("name") + hitMethod.getAttributeValue("signature");
				Element hitVar = bug.getChild("LocalVariable");
				if (hitVar == null) {
					hitVar = bug.getChild("Field");
				}

				if (hitVar != null) {
					varName = hitVar.getAttributeValue("name");
				} else
					varName = "";

				List<Element> soucelines = bug.getChildren("SourceLine");
				int i = 0;
				for (Element sourceLine : soucelines) {

					String startBytecode = sourceLine.getAttributeValue("startBytecode");
					String endBytecode = sourceLine.getAttributeValue("endBytecode");
					fromLine = sourceLine.getAttributeValue("start");
					toLine = sourceLine.getAttributeValue("end");
					lineRole = sourceLine.getAttributeValue("role");

					String amSql = "";
					amSql = "insert into warnings_all (proName,begin,className,methodName,fromLine,toLine,type,lineRole,message,toolName) values ("
							+ "'"
							+ proName
							+ "',"
							+ "'"
							+ (i == 0 ? 1 : 0)
							+ "',"
							+ "'"
							+ className
							+ "',"
							+ "'"
							+ methodName
							+ "',"
							+ "'"
							+ ((fromLine == null || fromLine.equals("")) ? (startBytecode + "b") : fromLine)
							+ "',"
							+ "'"
							+ ((toLine == null || toLine.equals("")) ? (endBytecode + "b"): toLine)
							+ "',"
							+ "'"
							+ type
							+ "',"
							+ "'"
							+ lineRole
							+ "',"
							+ "'" + varName + "'," + "'" + toolName + "'" + ")";

					amStatement = amConnection.createStatement();
					amStatement.execute(amSql);
					i++;
				}
			}
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
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}

	static void readWarningsFromPMDReportIntoDatabase(String filePath2, String proName2, String toolName2) {

		Connection amConnection;
		Statement amStatement;
		String proName, className, methodName, fromLine, toLine, type, message, precise, actionable, topprecision, toolName, remark;

		String filePath = filePath2;
		proName = proName2;
		toolName = toolName2;

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			amConnection = DriverManager.getConnection(DBUrl, DBUser, DBPwd);

			// read info from xml and then construct the sql statements
			SAXBuilder sb = new SAXBuilder();

			Document doc = sb.build(new FileInputStream(filePath));
			Element ele = doc.getRootElement();

			List<Element> files = ele.getChildren("file");
			for (Element file : files) {
				// className = file.getAttributeValue("name");

				List<Element> violations = file.getChildren("violation");
				for (Element violation : violations) {
					className = violation.getAttributeValue("package") + "." + violation.getAttributeValue("class");
					methodName = violation.getAttributeValue("method");

					fromLine = violation.getAttributeValue("beginline");
					toLine = violation.getAttributeValue("endline");

					message = violation.getText() + ": variable " + violation.getAttributeValue("variable");
					type = violation.getAttributeValue("rule");

					String amSql = "select * from warnings";
					amSql = "insert into warnings (proName,className,methodName,fromLine,toLine,type,message,toolName) values ("
							+ "'"
							+ proName
							+ "',"
							+ "'"
							+ className
							+ "',"
							+ "'"
							+ methodName
							+ "',"
							+ "'"
							+ fromLine
							+ "',"
							+ "'"
							+ toLine
							+ "',"
							+ "'"
							+ type
							+ "',"
							+ "'"
							+ message
							+ "',"
							+ "'"
							+ toolName
							+ "'" + ")";

					amStatement = amConnection.createStatement();
					amStatement.execute(amSql);
				}
			}
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
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String dir = "D:\\Lab\\Tasks\\Test Case自动生成与运行 ICSE2012\\Report\\";
		String[] proName = { "ant", "axion", "azureus", "eclipse-1", "eclipse-pde", "eclipse-swt-team",
				"eclipse-tomcat", "eclipse-ui", "eclipse-update", "hibernate", "hsqldb", "jboss", "jfreechart" };
		String toolName = "findbugs-null-related-defects";

		for (int i = 0; i < proName.length; i++) {
			Utils.readWarningsFromFindBugsReportIntoDatabase(dir + proName[i] + ".xml", proName[i], toolName);
		}
	}

}

// end

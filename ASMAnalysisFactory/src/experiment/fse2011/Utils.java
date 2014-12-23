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
package experiment.fse2011;

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
		String proName, className, methodName, fromLine = null, toLine = null, type, message = null, precise, actionable, topprecision, toolName, remark;

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
				if (type.equals("OS_OPEN_STREAM") || type.equals("ODR_OPEN_DATABASE_RESOURCE")) {
					Element hitMethod = bug.getChild("Method");
					className = hitMethod.getAttributeValue("classname");
					methodName = hitMethod.getAttributeValue("name") + hitMethod.getAttributeValue("signature");
					Element hitType = bug.getChild("Type");
					message = hitType.getAttributeValue("descriptor") + ":" + hitType.getAttributeValue("role");

					List<Element> soucelines = bug.getChildren("SourceLine");
					for (Element sourceLine : soucelines) {
						fromLine = sourceLine.getAttributeValue("start");
						toLine = sourceLine.getAttributeValue("end");

						String amSql = "";
						amSql = "insert into warnings_all (proName,className,methodName,fromLine,toLine,type,message,toolName) values ("
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
								+ "'" + toolName + "'" + ")";

						amStatement = amConnection.createStatement();
						amStatement.execute(amSql);
					}
				} else if (type.equals("OBL_UNSATISFIED_OBLIGATION")) {
					Element hitMethod = bug.getChild("Method");
					className = hitMethod.getAttributeValue("classname");
					methodName = hitMethod.getAttributeValue("name") + hitMethod.getAttributeValue("signature");
					Element hitType = bug.getChild("Type");
					// message = hitType.getAttributeValue("descriptor") + ":" +
					// hitType.getAttributeValue("role");

					List<Element> classes = bug.getChildren("Class");
					for (Element _class : classes) {
						if (_class.getAttributeValue("role") != null) {
							message = _class.getAttributeValue("classname");
						}
					}

					List<Element> soucelines = bug.getChildren("SourceLine");
					
					if (soucelines != null && soucelines.size() > 0) {
						fromLine = soucelines.get(0).getAttributeValue("start");
						toLine = soucelines.get(0).getAttributeValue("end");
						message += soucelines.get(0).getAttributeValue("role");
					}
					
					String amSql = "select * from warnings";
					amSql = "insert into warnings_all (proName,className,methodName,fromLine,toLine,type,message,toolName) values ("
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
		// TODO Auto-generated method stub
		// D:\\FSE2011\\´æµµ\\PMDReports\\
		// String[] fileToParse = {
		// "D:\\FSE2011\\´æµµ\\PMDReports\\eclipse-3.0.1.xml",
		// "D:\\FSE2011\\´æµµ\\PMDReports\\jboss-3.0.6.xml"};
		String dir = "D:\\FSE2011\\´æµµ\\FindbugsUnFilteredReports\\";
		String[] proName = { "sablecc-2.18.2", "jfreechart-1.0.1", "iText-2.0.7", "jedit4.2", "hibernate-2.0beta4",
				"axion-1.0-M2", "azureus_2.5.0.4", "velocity-1.5", "org.apache.ant_1.5.2", "weka-3-6-4",
				"hsqldb_1_7_1", "eclipse-3.0.1", "jboss-3.0.6" };
		
		//String[] proName = { "eclipse-3.0.1" };
		
		String toolName = "findbugs-unfiltered";
		// Utils.readWarningsFromPMDReportIntoDatabase(dir + proName[0] +
		// ".xml",
		// proName[0],toolName);

		for (int i = 0; i < proName.length; i++) {
			Utils.readWarningsFromFindBugsReportIntoDatabase(dir + proName[i] + ".xml", proName[i], toolName);
		}

	}

}

// end

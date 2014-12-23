/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author root
 * @time 2009-11-11 下午03:07:21
 * @modifier: root
 * @time 2009-11-11 下午03:07:21
 * @reviewer: root
 * @time 2009-11-11 下午03:07:21
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import edu.pku.cn.classfile.ClassNodeLoader;

public class CodaProperties {
	static final String currentPath = System.getProperty("user.dir");
	Properties prop;
	static CodaProperties instance = null;
	public static String projectPath;
	public static List<String> libPath=new ArrayList<String>();
	public static final int PROJECT_EXPLAND=0;
	public static final int LIB_EXPLAND=1;
	public static final int JRE_EXPLAND=2;
	public static boolean isLibExpland(int access){
		return (access & LIB_EXPLAND)>0;
	}
	public static boolean isJreExpland(int access){
		return (access & JRE_EXPLAND)>0;
	}
	
	/**
	 * this property decides whether methods in libs should be visited
	 */
	public static boolean isLibExpland=false;
	public static boolean isJREExpland=false;
	public boolean add(String e) {
		return libPath.add(e);
	}

	public String get(int index) {
		return libPath.get(index);
	}

	public boolean isEmpty() {
		return libPath.isEmpty();
	}

	public Iterator<String> iterator() {
		return libPath.iterator();
	}

	public int size() {
		return libPath.size();
	}

	public static String getProjectPath() {
		return projectPath;
	}

	public static void setProjectPath(String projectPath) {
		CodaProperties.projectPath = projectPath;
	}

	private CodaProperties() throws FileNotFoundException, IOException {
		assert (currentPath != null);
		prop = new Properties();
		prop.load(new BufferedInputStream(new FileInputStream(currentPath + File.separator + "CODA.properties")));
	}

	public static synchronized CodaProperties e() {
		if (instance == null) {
			try {
				instance = new CodaProperties();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
		}
		return instance;
	}

	public String getProperties(String key) {
		return prop.getProperty(key);
	}

	public String getMessage() {
		return prop.getProperty("Message");
	}

	public String getDetectorConfiger() {
		return prop.getProperty("DetectorConfiger");
	}

	public String getAutoMachineSpecs() {
		return prop.getProperty("AutoMachineSpecs");
	}

	public String getRuleSet() {
		return prop.getProperty("RuleSet");
	}

	public String getJar() {
		return prop.getProperty("Jar");
	}

	public String getZip() {
		return prop.getProperty("Zip");
	}

	public String getClassAtt() {
		return prop.getProperty("Class");
	}
	public String getJRELibPath(){
		return prop.getProperty("JRELibPath");
	}
}

// end

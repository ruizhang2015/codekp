/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author ZR-Private
 * @time Jan 6, 2015
 * @modifier: ZR-Private
 * @time Jan 6, 2015
 * @reviewer: ZR-Private
 * @time Jan 6, 2015
 * (C) Copyright PKU Software Lab. 2015
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.sun.javadoc.*;



public class JavadocUtils {

	public JavadocUtils() {
	}
	
	public Map<String, String> processDoc(String classname) {
		Map<String, String> res = new HashMap<String, String>();
		try {
			//System.out.println("javadoc -doclet ListParams -sourcepath AccessLogValve.java");
			String cmd = "javadoc -docletpath bin -doclet edu.pku.cn.util.ListParams -private ";
			Process pro = Runtime.getRuntime().exec(cmd + classname);
			StreamGobbler errorGobbler = new StreamGobbler(pro.getErrorStream(), "ERROR");     
			errorGobbler.start();  

			BufferedReader br0 = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			String line=null;
			String name = null;
			String comment = "";
			String[] strs = null;
			while ( (line = br0.readLine()) != null) {
			    //System.out.println(line);   
			    if (line.startsWith("[mname]")){
			    	strs = line.split("\\.");
			    	name = strs[strs.length-1];
			    }
			    else if (line.startsWith("[mlineno]")){
			    	name += line.split(":")[1];
			    }
			    else if (line.startsWith("[mcomments]")){
			    	comment = line.substring(11);
			    	if (comment == null){
			    		System.out.println("!!!null!!!!!!!!!!!!!!!!!!!!");
			    	}
			    	if (res.containsKey(name)){
			    		System.out.println("!!!duplicated!!!" + name);
			    		comment += "!!!duplicated!!!" + res.get(name);
			    	}
			    	res.put(name, comment);
			    }
			}
	        br0.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		String classname = ".." + "/tomcat/trunk/java/org/apache/catalina/valves/AccessLogValve.java";
		JavadocUtils ju = new JavadocUtils();
		ju.processDoc(classname);
	}

}

// end

/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author ZR-Private
 * @time Jan 3, 2015
 * @modifier: ZR-Private
 * @time Jan 3, 2015
 * @reviewer: ZR-Private
 * @time Jan 3, 2015
 * (C) Copyright PKU Software Lab. 2015
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import edu.pku.cn.analysis.RefactoredCalleeDataflowAnalysis;
import edu.pku.cn.analysis.RefactoredDataflow;
import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.detector.MethodboundAnalysis;
import edu.pku.cn.detector.RefactoredDataflowTestDriver;
import edu.pku.cn.jir.Stmt;

/**
 * @author ZR-Private
 */
public class MethodAnalysis {
	private Map<String, Map<String, String[]>> mbounds = new HashMap<String, Map<String, String[]>>();
	
	public Map<String, Map<String, String[]>> getMbounds() {
		return this.mbounds;
	}

	public MethodAnalysis() {
		// TODO Auto-generated constructor stub
	}

	public Map<String, Integer[]> analysis(String path) throws Exception{
		MethodboundAnalysis<HashSet<String>, RefactoredCalleeDataflowAnalysis> driver = new MethodboundAnalysis<HashSet<String>, RefactoredCalleeDataflowAnalysis>() {
			public RefactoredDataflow<HashSet<String>, RefactoredCalleeDataflowAnalysis> createDataflow(
					ClassNode cc, MethodNode method) {
				return null;
			}

			@Override
			public void examineResults(RefactoredDataflow<HashSet<String>, RefactoredCalleeDataflowAnalysis> dataflow) {
				// TODO Auto-generated method stub				
			}	
		};
		return driver.execute(path);
		//File file = new File(path);
		/*if (path.endsWith(".java")){
			FileReader fr=new FileReader(path);
	        BufferedReader br=new BufferedReader(fr);
	        String line="";
	        String[] mres= new String[2];
	        String name = null;
	        Map<String, String[]> res = null;
	        int i = 0;
	        while ((line=br.readLine())!=null) {
	        	i++;
	        	if ((line.contains("public ") || line.contains("private ") || line.contains("protected ")) && !line.contains("class ")){
	        		if (line.contains(";")) continue; //fields
	        		//methods begin
	        		if (res == null){
	        			res = new HashMap<String, String[]>();
	        		}
	        		else{
		        		mres[1] = (i - 1) + "";
		        		res.put(name, mres);
	        		}
	        		mres[0] = i + "";
	        		name = line.trim().split(" ")[2].split("(")[0];
	        	}
	        }
	        mres[1] = (i - 1) + "";
    		res.put(name, mres);
    		
	        br.close();
	        fr.close();
		}*/
		
	}
	public static void main(String[] args) throws Exception {
		MethodAnalysis ma = new MethodAnalysis();
		ma.analysis("testcase/TestCloseDbConnection");
	}

}

// end

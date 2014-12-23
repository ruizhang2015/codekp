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

import java.io.IOException;
import java.sql.*;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.objectweb.asm.tree.analysis.AnalyzerException;

import edu.pku.cn.Project;
import edu.pku.cn.analysis.InterValueDataflowAnalysis;
import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.classfile.ClassNodeLoader;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.graph.cfg.CFG;
import edu.pku.cn.util.AnalysisFactoryManager;
import edu.pku.cn.util.CodaProperties;

public class TestInterPointsToInfo {

	Set<String> set;
	String str;
	static int i;
    static int j;
    
	String right(String a) throws NoSuchFieldException {

		int k2 = this.i + this.j;
		
		System.out.print("hello");
		java.io.File file;
		
		try {
			Runtime.getRuntime().exec("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.set = new HashSet<String>();
		boolean succeed = this.set.add("sina");
		String a2 = "a-string";
		Set<String> localSet = new HashSet<String>();

		int i = 100;
		int j = 10;
		int testInt = i;
		
//		
		double k = testInt + j;
		// to consider
		//boolean isContained = localSet.contains("sina");
		
		TestPointsToInfo callee = new TestPointsToInfo();
		TestInterPointsToInfo newInterInfo = this;
		
		a2 = callee.meetPrecondition4(a, localSet);
		String a3 = this.meetPrecondition3(a2, callee.set);	
		
		this.i = testInt;
		this.i = this.meetPrecondition4(i, localSet);
		return a2;
	}

	String meetPrecondition3(String a, Set<String> localSet2) {	
		this.set = localSet2;
		return a;
	}
	
	int meetPrecondition4(int a, Set<String> localSet2) {	
		this.set = localSet2;
		a++;
		TestPointsToInfo callee = new TestPointsToInfo();
		a = callee.meetPrecondition5(a, localSet2);
		return a;
	}
	
//	public static void main(String[] args) {
//		String a = "test";
//		String b = a;
//		
//		System.out.println(a);
//		System.out.println(b);
//		
//		a = a + 1;
//		
//		System.out.println(a);
//		System.out.println(b);
//		
//		
//	}
}

// end

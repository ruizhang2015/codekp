/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author ZR-Private
 * @time Dec 23, 2014
 * @modifier: ZR-Private
 * @time Dec 23, 2014
 * @reviewer: ZR-Private
 * @time Dec 23, 2014
 * (C) Copyright PKU Software Lab. 2014
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.analysis;

import java.io.File;

import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.classfile.ClassNodeLoader;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.util.AnalysisFactoryManager;

/**
 * @author ZR-Private
 */
public class CFGAnalysis {
	
	public CFGAnalysis(){
		AnalysisFactoryManager.initial();
	}
	
	public void execute(String packageName) {

		//ClassNodeLoader loader = new ClassNodeLoader("bin/edu/pku/cn/testcase/");
		ClassNodeLoader loader = new ClassNodeLoader(packageName);
		File rootDir = new File(packageName);
		if (rootDir.isDirectory()){
			for (File classFile : rootDir.listFiles()){
				String className = classFile.getName();
				if (className.endsWith(".class")){
					className = className.split("\\.")[0];
					ClassNode cc = loader.loadClassNode(className,0);
					for (MethodNode method : cc.methods) {
						method.getStmts();
						System.out.println("======================" + className + "::" + method.name + "=======================");
						System.out.println(method.getCFG());
					}
				}				
			}
		}		
	}

	public static void main(String[] args) {
		CFGAnalysis ca = new CFGAnalysis();
		ca.execute("bin/edu/pku/cn/testcase/");
	}

}

// end

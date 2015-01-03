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
package edu.pku.cn.detector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.pku.cn.analysis.DataflowAnalysis;
import edu.pku.cn.analysis.RefactoredDataflow;
import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.classfile.ClassNodeLoader;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.jir.Stmt;
import edu.pku.cn.util.AnalysisFactoryManager;

/**
 * @author ZR-Private
 */
public abstract class MethodboundAnalysis<Fact, AnalysisType extends DataflowAnalysis<Fact>> {

	public MethodboundAnalysis() {
		AnalysisFactoryManager.initial();
	}

	public Map<String, Integer[]> execute(String className) {

		ClassNodeLoader loader = new ClassNodeLoader("bin/edu/pku/cn/");
		ClassNode cc = loader.loadClassNode(className, 0);
		
		Map<String, Integer[]> resMap = new HashMap<String, Integer[]>();

		String tmp = "";
		
		for (MethodNode method : cc.methods) {
			List<Stmt> stmtsList = method.getStmts();
			System.out.println(method.getFullName());
			int min = Integer.MAX_VALUE;
			int max = Integer.MIN_VALUE;
			for (Stmt stmt : stmtsList){
				tmp = (stmt.toString());
				if (tmp.startsWith("Line:")){
					int no = Integer.parseInt(tmp.split(" ")[0].split(":")[1]);
					if (no > max) max = no;
					if (no < min) min = no;
				}
			}
			System.out.println(min + " " + max);
			System.out.println();
			resMap.put(method.getFullName(), new Integer[]{min, max});
		}
		return resMap;
	}

	public void execute(ClassNode cc, MethodNode method) {
		RefactoredDataflow<Fact, AnalysisType> dataflow = createDataflow(cc, method);
		examineResults(dataflow);
	}

	public abstract RefactoredDataflow<Fact, AnalysisType> createDataflow(ClassNode cc, MethodNode method);

	public abstract void examineResults(RefactoredDataflow<Fact, AnalysisType> dataflow);

}

// end

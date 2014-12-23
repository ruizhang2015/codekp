/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-1-11 ����10:09:04
 * @modifier: Administrator
 * @time 2009-1-11 ����10:09:04
 * @reviewer: Administrator
 * @time 2009-1-11 ����10:09:04
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.detector;

import edu.pku.cn.analysis.DataflowAnalysis;
import edu.pku.cn.analysis.RefactoredDataflow;
import edu.pku.cn.analysis.SimpleDataflow;
import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.classfile.ClassNodeLoader;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.util.AnalysisFactoryManager;

public abstract class RefactoredDataflowTestDriver<Fact, AnalysisType extends DataflowAnalysis<Fact>> {

	public RefactoredDataflowTestDriver() {
		AnalysisFactoryManager.initial();
	}

	public void execute(String className) {

		ClassNodeLoader loader = new ClassNodeLoader("bin/edu/pku/cn/testcase/");
//		ClassNodeLoader loader = new ClassNodeLoader(
//				"F:/CodeWorld/Workspaces/eclipseworkspace/xerces-131686/bin/org/apache/xerces/parsers/");

		ClassNode cc = loader.loadClassNode(className, 0);

		for (MethodNode method : cc.methods) {
			method.getStmts();
			//execute(cc, method);
			System.out.println(method.getCFG());
		}
	}

	public void execute(ClassNode cc, MethodNode method) {
		RefactoredDataflow<Fact, AnalysisType> dataflow = createDataflow(cc, method);
		examineResults(dataflow);
	}

	public abstract RefactoredDataflow<Fact, AnalysisType> createDataflow(ClassNode cc, MethodNode method);

	public abstract void examineResults(RefactoredDataflow<Fact, AnalysisType> dataflow);

}

// end

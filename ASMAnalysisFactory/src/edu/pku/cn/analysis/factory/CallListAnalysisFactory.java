/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-1-11 ����10:16:10
 * @modifier: Administrator
 * @time 2009-1-11 ����10:16:10
 * @reviewer: Administrator
 * @time 2009-1-11 ����10:16:10
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.analysis.factory;

import org.objectweb.asm.tree.analysis.AnalyzerException;
import edu.pku.cn.asm.tree.analysis.CallList;
import edu.pku.cn.analysis.AnalysisFactory;
import edu.pku.cn.analysis.CallListDataflowAnalysis;
import edu.pku.cn.analysis.SimpleDataflow;
import edu.pku.cn.graph.cfg.CFG;

public class CallListAnalysisFactory extends AnalysisFactory<CFG, CallListDataflowAnalysis> {

	private static CallListAnalysisFactory factoryTest = new CallListAnalysisFactory();

	public static CallListAnalysisFactory getInstance() {
		return factoryTest;
	}

	// @Override
	// public CallListDataflowAnalysis getAnalysis(CFG target)throws
	// AnalyzerException{
	// CallListDataflowAnalysis analysis = analysisMap.get(target);
	// if(analysis == null){
	// analysis = analyze(target);
	// analysisMap.put(target, analysis);
	// }
	// return analysis;
	// }
	//	
	@Override
	protected CallListDataflowAnalysis analyze(CFG target) throws AnalyzerException {
		CallListDataflowAnalysis analysis = new CallListDataflowAnalysis(target);
		SimpleDataflow<CallList, CallListDataflowAnalysis> simpleDataflow = new SimpleDataflow<CallList, CallListDataflowAnalysis>(
				target, analysis);
		simpleDataflow.execute();

		return analysis;
	}

	@Override
	public CallListAnalysisFactory clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return new CallListAnalysisFactory();
	}

	public static final String NAME = "CallListAnalysisFactory";

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return NAME;
	}
}

// end

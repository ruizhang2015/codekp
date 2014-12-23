/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-3-19 ����03:48:54
 * @modifier: Administrator
 * @time 2009-3-19 ����03:48:54
 * @reviewer: Administrator
 * @time 2009-3-19 ����03:48:54
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.analysis.factory;

import org.objectweb.asm.tree.analysis.AnalyzerException;

import edu.pku.cn.analysis.AnalysisFactory;
import edu.pku.cn.analysis.LiveVariableDataflowAnalysis;
import edu.pku.cn.analysis.SimpleDataflow;
import edu.pku.cn.asm.tree.analysis.LoadStoreFact;
import edu.pku.cn.graph.cfg.CFG;

public class LiveVariableDataflowFactory extends AnalysisFactory<CFG, LiveVariableDataflowAnalysis> {

	private static LiveVariableDataflowFactory factoryTest = new LiveVariableDataflowFactory();

	public static LiveVariableDataflowFactory getInstance() {
		return factoryTest;
	}

	// @Override
	// public LiveVariableDataflowAnalysis getAnalysis(CFG target)throws
	// AnalyzerException{
	// LiveVariableDataflowAnalysis analysis = analysisMap.get(target);
	// if(analysis == null){
	// analysis = analyze(target);
	// analysisMap.put(target, analysis);
	// }
	// return analysis;
	// }

	@Override
	protected LiveVariableDataflowAnalysis analyze(CFG target) throws AnalyzerException {
		LiveVariableDataflowAnalysis analysis = new LiveVariableDataflowAnalysis(target);
		SimpleDataflow<LoadStoreFact, LiveVariableDataflowAnalysis> simpleDataflow = new SimpleDataflow<LoadStoreFact, LiveVariableDataflowAnalysis>(
				target, analysis);
		simpleDataflow.execute();

		return analysis;
	}

	//	
	// public void setRealValueDataflowAnalysis(RealValueDataflowAnalysis
	// analysis){
	// this.rvAnalysis = analysis;
	// }

	@Override
	public LiveVariableDataflowFactory clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return new LiveVariableDataflowFactory();
	}

	public static final String NAME = "LiveVariableDataflowFactory";

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return NAME;
	}
}

// end

/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-2-18 ����03:17:15
 * @modifier: Administrator
 * @time 2009-2-18 ����03:17:15
 * @reviewer: Administrator
 * @time 2009-2-18 ����03:17:15
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.analysis.factory;

import org.objectweb.asm.tree.analysis.AnalyzerException;
import edu.pku.cn.analysis.AnalysisFactory;
import edu.pku.cn.analysis.LockDataflowAnalysis;
import edu.pku.cn.analysis.SimpleDataflow;
import edu.pku.cn.asm.tree.analysis.LockMap;
import edu.pku.cn.graph.cfg.CFG;

public class LockDataflowFactory extends AnalysisFactory<CFG, LockDataflowAnalysis> {

	private static LockDataflowFactory factoryTest = new LockDataflowFactory();

	// private RealValueDataflowAnalysis rvAnalysis = null;

	public static LockDataflowFactory getInstance() {
		return factoryTest;
	}

	// public LockDataflowAnalysis getAnalysis(CFG target)throws
	// AnalyzerException{
	// LockDataflowAnalysis analysis = analysisMap.get(target);
	// if(analysis == null){
	// analysis = analyze(target);
	// analysisMap.put(target, analysis);
	// }
	// return analysis;
	// }

	@Override
	protected LockDataflowAnalysis analyze(CFG target) throws AnalyzerException {
		LockDataflowAnalysis analysis = new LockDataflowAnalysis(target);
		SimpleDataflow<LockMap, LockDataflowAnalysis> simpleDataflow = new SimpleDataflow<LockMap, LockDataflowAnalysis>(
				target, analysis);
		simpleDataflow.execute();

		return analysis;
	}

	// public void setRealValueDataflowAnalysis(RealValueDataflowAnalysis
	// analysis){
	// this.rvAnalysis = analysis;
	// }

	@Override
	public LockDataflowFactory clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return new LockDataflowFactory();
	}

	public static final String NAME = "LockDataflowFactory";

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return NAME;
	}
}

// end

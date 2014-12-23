/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-4-3 ����01:32:55
 * @modifier: Administrator
 * @time 2009-4-3 ����01:32:55
 * @reviewer: Administrator
 * @time 2009-4-3 ����01:32:55
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.analysis.factory;

import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Frame;

import edu.pku.cn.analysis.AnalysisFactory;
import edu.pku.cn.analysis.IsNullValueDataflowAnalysis; //import edu.pku.cn.analysis.RealValueDataflowAnalysis;
import edu.pku.cn.analysis.SimpleDataflow;
import edu.pku.cn.graph.cfg.CFG;

public class IsNullValueDataflowFactory extends AnalysisFactory<CFG, IsNullValueDataflowAnalysis> {

	private static IsNullValueDataflowFactory factoryTest = new IsNullValueDataflowFactory();

	// private RealValueDataflowAnalysis rvAnalysis = null;

	public static IsNullValueDataflowFactory getInstance() {
		return factoryTest;
	}

	public IsNullValueDataflowFactory() {
	}

	// public void setInterpreter(Interpreter interpreter){
	// this.interpreter = interpreter;
	// }
	//	
	// @Override
	// public IsNullValueDataflowAnalysis getAnalysis(CFG target)throws
	// AnalyzerException{
	// IsNullValueDataflowAnalysis analysis = analysisMap.get(target);
	// if(analysis == null){
	// analysis = analyze(target);
	// analysisMap.put(target, analysis);
	// }
	// return analysis;
	// }

	@Override
	protected IsNullValueDataflowAnalysis analyze(CFG target) throws AnalyzerException {
		IsNullValueDataflowAnalysis analysis = new IsNullValueDataflowAnalysis(target);
		SimpleDataflow<Frame, IsNullValueDataflowAnalysis> simpleDataflow = new SimpleDataflow<Frame, IsNullValueDataflowAnalysis>(
				target, analysis);
		simpleDataflow.execute();

		return analysis;
	}

	@Override
	public IsNullValueDataflowFactory clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return new IsNullValueDataflowFactory();
	}

	public static final String NAME = "IsNullValueDataflowFactory";

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return NAME;
	}

	// public void setRealValueDataflowAnalysis(RealValueDataflowAnalysis
	// analysis){
	// this.rvAnalysis = analysis;
	// }
}

// end

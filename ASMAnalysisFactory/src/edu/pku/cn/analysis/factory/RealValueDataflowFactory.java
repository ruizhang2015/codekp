/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2008-12-24 ����04:56:36
 * @modifier: Administrator
 * @time 2008-12-24 ����04:56:36
 * @reviewer: Administrator
 * @time 2008-12-24 ����04:56:36
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.analysis.factory;

import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Frame;

import edu.pku.cn.analysis.AnalysisFactory;
import edu.pku.cn.analysis.RealValueDataflowAnalysis;
import edu.pku.cn.analysis.SimpleDataflow;
import edu.pku.cn.graph.cfg.CFG;

public class RealValueDataflowFactory extends AnalysisFactory<CFG, RealValueDataflowAnalysis> {

	public RealValueDataflowFactory() {
	}

	// @Override
	// public synchronized RealValueDataflowAnalysis getAnalysis(CFG
	// target)throws AnalyzerException{
	// return super.getAnalysis(target);
	// }

	@Override
	protected RealValueDataflowAnalysis analyze(CFG target) throws AnalyzerException {
		RealValueDataflowAnalysis analysis = new RealValueDataflowAnalysis(target);
		synchronized (analysis) {
			SimpleDataflow<Frame, RealValueDataflowAnalysis> simpleDataflow = new SimpleDataflow<Frame, RealValueDataflowAnalysis>(
					target, analysis);
			simpleDataflow.execute();
		}
		return analysis;
	}

	@Override
	public RealValueDataflowFactory clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return new RealValueDataflowFactory();
	}

	public static final String NAME = "RealValueDataflowFactory";

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return NAME;
	}

}

// end

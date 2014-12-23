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
package edu.pku.cn.analysis.factory.security;

import org.objectweb.asm.tree.analysis.AnalyzerException;

import edu.pku.cn.analysis.AnalysisFactory;
import edu.pku.cn.analysis.sercurity.IntraSQLInjectionDataflowAnalysis;
import edu.pku.cn.graph.cfg.CFG;

public class IntraSQLIjectionDataflowFactory extends AnalysisFactory<CFG, IntraSQLInjectionDataflowAnalysis> {

	public IntraSQLIjectionDataflowFactory() {
	}

	// @Override
	// public synchronized RealValueDataflowAnalysis getAnalysis(CFG
	// target)throws AnalyzerException{
	// return super.getAnalysis(target);
	// }

	@Override
	protected IntraSQLInjectionDataflowAnalysis analyze(CFG target) throws AnalyzerException {
		IntraSQLInjectionDataflowAnalysis analysis = new IntraSQLInjectionDataflowAnalysis(target);
		synchronized (analysis) {
			analysis.execute();
		}
		return analysis;
	}

	@Override
	public IntraSQLIjectionDataflowFactory clone() throws CloneNotSupportedException {
		return new IntraSQLIjectionDataflowFactory();
	}

	public static final String NAME = "IntraSQLIjectionDataflowFactory";

	@Override
	public String getName() {
		return NAME;
	}

}

// end

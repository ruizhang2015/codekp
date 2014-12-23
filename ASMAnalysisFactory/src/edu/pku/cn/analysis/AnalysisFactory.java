/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-5-22 ����02:57:36
 * @modifier: Liuxizhiyi
 * @time 2008-5-22 ����02:57:36
 * @reviewer: Liuxizhiyi
 * @time 2008-5-22 ����02:57:36
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.analysis;

import org.objectweb.asm.tree.analysis.AnalyzerException;

import edu.pku.cn.util.AnalysisMap;

/**
 * 
 * @author Liuxizhiyi
 */
public abstract class AnalysisFactory<Target extends Object, Analysis> implements Cloneable {
	// protected IdentityHashMap<Target,Analysis> analysisMap=new
	// IdentityHashMap<Target,Analysis>();
	// private IdentityHashMap<Target, Analysis> cfgMap = new
	// IdentityHashMap<Target, Analysis>();//ר�����ڻ��������㹹���CFG
	/**
	 * Get the Analysis for given method. If Analysis has already been
	 * performed, the cached result is returned.
	 * 
	 * @param target
	 *            the target to analyze
	 * @return the Analysis object representing the result of analyzing the
	 *         method
	 */
	public synchronized Analysis getAnalysis(Target target) throws AnalyzerException {
		Analysis analysis = (Analysis) AnalysisMap.value().getAnalysis(getName(), target);
		if (analysis == null) {
			analysis = analyze(target);
			AnalysisMap.value().put(getName(), target, analysis);
		}
		return analysis;
	}

	// modified by Meng Na
	// protected abstract Analysis analysis(Target target) throws
	// AnalyzerException;
	// added by Meng Na
	protected abstract Analysis analyze(Target target) throws AnalyzerException;

	public abstract AnalysisFactory clone() throws CloneNotSupportedException;

	public abstract String getName();
}

// end

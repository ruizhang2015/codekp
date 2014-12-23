/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author liuxizhiyi
 * @time 2008-12-19 ����04:36:53
 * @modifier: liuxizhiyi
 * @time 2008-12-19 ����04:36:53
 * @reviewer: liuxizhiyi
 * @time 2008-12-19 ����04:36:53
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.analysis;

/**
 * 
 * @author liuxizhiyi
 */
public interface FlowInterpreter<VertexType, AnalysisResult> {
	/**
	 * Returns the flow object corresponding to the initial values for each
	 * graph node.
	 */
	AnalysisResult newInitialFlow();

	/**
	 * Returns the initial flow value for entry/exit graph nodes.
	 */
	AnalysisResult entryInitialFlow();

	/**
	 * Compute the merge of the <code>in1</code> and <code>in2</code> sets,
	 * putting the result into <code>out</code>. The behavior of this function
	 * depends on the implementation ( it may be necessary to check whether
	 * <code>in1</code> and <code>in2</code> are equal or aliased ). Used by the
	 * doAnalysis method.
	 */
	void merge(AnalysisResult in1, AnalysisResult in2, AnalysisResult out);

	/**
	 * Creates a copy of the <code>source</code> flow object in
	 * <code>dest</code>.
	 */
	void copy(AnalysisResult source, AnalysisResult dest);

	/**
	 * Given the merge of the <code>out</code> sets, compute the <code>in</code>
	 * set for <code>s</code> (or in to out, depending on direction).
	 * 
	 * This function often causes confusion, because the same interface is used
	 * for both forward and backward flow analyses. The first parameter is
	 * always the argument to the flow function (i.e. it is the "in" set in a
	 * forward analysis and the "out" set in a backward analysis), and the third
	 * parameter is always the result of the flow function (i.e. it is the "out"
	 * set in a forward analysis and the "in" set in a backward analysis).
	 * */
	void flowThrough(VertexType node, AnalysisResult in, AnalysisResult out);
}

// end

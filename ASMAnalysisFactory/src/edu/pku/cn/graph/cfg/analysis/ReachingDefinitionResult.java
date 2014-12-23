/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-11-27 ����12:27:06
 * @modifier: Liuxizhiyi
 * @time 2008-11-27 ����12:27:06
 * @reviewer: Liuxizhiyi
 * @time 2008-11-27 ����12:27:06
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.cfg.analysis;

import java.util.BitSet;
import java.util.Map;

/**
 * 
 * @author Liuxizhiyi
 */
public class ReachingDefinitionResult implements AnalysisResult {
	Map<Integer, Integer> insnToVar;
	BitSet result;

	/**
	 * @param insnToVar
	 * @param result
	 */
	public ReachingDefinitionResult(Map<Integer, Integer> insnToVar, BitSet result) {
		super();
		this.insnToVar = insnToVar;
		this.result = result;
	}

	public ReachingDefinitionResult() {
	}

	/**
	 * @return Returns the insnToVar.
	 */
	public Map<Integer, Integer> getInsnToVar() {
		return insnToVar;
	}

	/**
	 * @return Returns the result.
	 */
	public BitSet getResult() {
		return result;
	}

	/**
	 * @param insnToVar
	 *            The insnToVar to set.
	 */
	public void setInsnToVar(Map<Integer, Integer> insnToVar) {
		this.insnToVar = insnToVar;
	}

	/**
	 * @param result
	 *            The result to set.
	 */
	public void setResult(BitSet result) {
		this.result = result;
	}

}

// end

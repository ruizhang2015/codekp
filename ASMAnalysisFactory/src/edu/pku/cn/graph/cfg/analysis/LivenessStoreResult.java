/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-11-27 ����02:58:46
 * @modifier: Liuxizhiyi
 * @time 2008-11-27 ����02:58:46
 * @reviewer: Liuxizhiyi
 * @time 2008-11-27 ����02:58:46
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.cfg.analysis;

import java.util.BitSet;
import java.util.Map;

import edu.pku.cn.classfile.LineNumberTable;
import edu.pku.cn.classfile.LocalVariableTable;

/**
 * 
 * @author Liuxizhiyi
 */
public class LivenessStoreResult implements AnalysisResult {
	Map<Integer, BitSet> in;
	Map<Integer, BitSet> out;
	Map<Integer, Map<Integer, BitSet>> defOutOfBlock;
	LocalVariableTable localVariableTable;
	LineNumberTable lineNumberTable;
	int entry;

	/**
	 * @param unliveStore
	 * @param localVariableTable
	 * @param lineNumberTable
	 */
	public LivenessStoreResult(Map<Integer, BitSet> in, Map<Integer, BitSet> out,
			Map<Integer, Map<Integer, BitSet>> defOutOfBlock, int entry, LocalVariableTable localVariableTable,
			LineNumberTable lineNumberTable) {
		super();
		this.in = in;
		this.out = out;
		this.entry = entry;
		this.defOutOfBlock = defOutOfBlock;
		this.localVariableTable = localVariableTable;
		this.lineNumberTable = lineNumberTable;
	}

	public LivenessStoreResult() {
	}

	/**
	 * @return Returns the localVariableTable.
	 */
	public LocalVariableTable getLocalVariableTable() {
		return localVariableTable;
	}

	/**
	 * @param localVariableTable
	 *            The localVariableTable to set.
	 */
	public void setLocalVariableTable(LocalVariableTable localVariableTable) {
		this.localVariableTable = localVariableTable;
	}

	/**
	 * @return Returns the lineNumberTable.
	 */
	public LineNumberTable getLineNumberTable() {
		return lineNumberTable;
	}

	/**
	 * @param lineNumberTable
	 *            The lineNumberTable to set.
	 */
	public void setLineNumberTable(LineNumberTable lineNumberTable) {
		this.lineNumberTable = lineNumberTable;
	}

	/**
	 * @return Returns the in.
	 */
	public Map<Integer, BitSet> getIn() {
		return in;
	}

	/**
	 * @param in
	 *            The in to set.
	 */
	public void setIn(Map<Integer, BitSet> in) {
		this.in = in;
	}

	/**
	 * @return Returns the out.
	 */
	public Map<Integer, BitSet> getOut() {
		return out;
	}

	/**
	 * @param out
	 *            The out to set.
	 */
	public void setOut(Map<Integer, BitSet> out) {
		this.out = out;
	}

	/**
	 * @return Returns the defOutOfBlock.
	 */
	public Map<Integer, Map<Integer, BitSet>> getDefOutOfBlock() {
		return defOutOfBlock;
	}

	/**
	 * @param defOutOfBlock
	 *            The defOutOfBlock to set.
	 */
	public void setDefOutOfBlock(Map<Integer, Map<Integer, BitSet>> defOutOfBlock) {
		this.defOutOfBlock = defOutOfBlock;
	}

	/**
	 * @return Returns the entry.
	 */
	public int getEntry() {
		return entry;
	}

	/**
	 * @param entry
	 *            The entry to set.
	 */
	public void setEntry(int entry) {
		this.entry = entry;
	}

}

// end

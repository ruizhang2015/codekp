/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-1-9 ����02:12:27
 * @modifier: Administrator
 * @time 2009-1-9 ����02:12:27
 * @reviewer: Administrator
 * @time 2009-1-9 ����02:12:27
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.analysis;

import edu.pku.cn.graph.cfg.BasicBlock;
import edu.pku.cn.graph.visit.BlockOrder;

public interface DataflowAnalysis<Fact> {

	/**
	 * Create empty (uninitialized) dataflow facts for one program point. A
	 * valid value will be copied into it before it is used.
	 */
	public Fact createFact();

	/**
	 * create start fact of the entry block
	 * 
	 * @return
	 */
	public Fact createEntryFact();

	/**
	 * Create new fact with given fact (copy the given fact)
	 * 
	 * @param fact
	 * @return
	 */
	public Fact createFact(Fact fact);

	/**
	 * Get the new Start fact for given basic block
	 * 
	 * @param block
	 * @return
	 */
	public Fact getNewStartFact(BasicBlock block);

	/**
	 * Get the fact corresponding to certain insn specified with index.
	 * 
	 * @param index
	 * @return
	 */
	public Fact getFact(int index);

	/**
	 * Get all facts.
	 * 
	 * @return
	 */
	public Fact[] getFacts();

	/**
	 * set the Start fact for given basic block
	 * 
	 * @param block
	 * @param fact
	 */
	public void setStartFact(BasicBlock block, Fact fact);

	/**
	 * Get the Start fact for given basic block.
	 * 
	 * @param block
	 * @return
	 */
	public Fact getStartFact(BasicBlock block);

	/**
	 * Get the result fact for given basic block.
	 * 
	 * @param block
	 * @return
	 */
	public Fact getResultFact(BasicBlock block);

	/**
	 * Set the result fact for given basic block to the given fact.
	 * 
	 * @param block
	 * @param result
	 */
	public void setResultFact(BasicBlock block, Fact result);

	// /**
	// * Copy dataflow facts.
	// * @param source
	// * @param dest
	// */
	// public void copy(Fact source, Fact dest);

	/**
	 * Apply the transfer function of a block to generate a result fact.
	 * 
	 * @param block
	 * @return
	 */
	public Fact transferVertex(BasicBlock block);

	/**
	 * Init the fact of entry block.
	 */
	public void initEntryFact();

	/**
	 * Returns true if the analysis is forwards, false if backwards.
	 * 
	 * @return
	 */
	public boolean isForwards();

	/**
	 * Return true if the block is End Block.
	 * 
	 * @return
	 */
	public boolean isEndBlock(BasicBlock block);

	/**
	 * Return the BlockOrder specifying the order in which BasicBlocks should be
	 * visited in the main dataflow loop.
	 * 
	 * @param cfg
	 * @return
	 */
	public BlockOrder getBlockOrder();

	/**
	 * Called before beginning an iteration of analysis. Each iteration visits
	 * every basic block in the CFG.
	 */
	public void startIteration();

	/**
	 * Called after finishing an iteration of analysis.
	 */
	public void finishIteration();

	public int getLastUpdateTimestamp(Fact fact);

	public void setLastUpdateTimestamp(Fact fact, int timestamp);

	public Fact merge(Fact start, Fact pred);

	public boolean same(Fact fact1, Fact fact2);

	public void setNewStartFact(BasicBlock block, Fact fact);
}

// end

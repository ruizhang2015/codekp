/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author root
 * @time 2009-12-25 上午11:40:16
 * @modifier: root
 * @time 2009-12-25 上午11:40:16
 * @reviewer: root
 * @time 2009-12-25 上午11:40:16
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.analysis.dataflow;

import java.util.Iterator;

import org.objectweb.asm.tree.AbstractInsnNode;

import edu.pku.cn.graph.GraphVertex;
import edu.pku.cn.graph.visit.BlockOrder;

public abstract class DataflowAnalysis<Fact, Vertex extends GraphVertex> {
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
	protected void flowThrough(Fact in, Vertex d, Fact out) {
		copy(in, out);
		Iterator<AbstractInsnNode> iter = d.nodeIterator(!isForward());
		while (iter.hasNext()) {
			flowThroughInsn(out, iter.next(), out);
		}
	}

	protected abstract void flowThroughInsn(Fact in, AbstractInsnNode insn, Fact out);

	/**
	 * Returns the flow object corresponding to the initial values for each
	 * graph node.
	 */
	protected abstract Fact newInitialFlow();

	/**
	 * Returns the initial flow value for entry/exit graph nodes.
	 */
	protected abstract Fact entryInitialFlow();

	/**
	 * Determines whether <code>entryInitialFlow()</code> is applied to trap
	 * handlers.
	 */
	protected boolean treatTrapHandlersAsEntries() {
		return false;
	}

	/** Returns true if this analysis is forwards. */
	protected abstract boolean isForward();

	/**
	 * Compute the merge of the <code>in1</code> and <code>in2</code> sets,
	 * putting the result into <code>out</code>. The behavior of this function
	 * depends on the implementation ( it may be necessary to check whether
	 * <code>in1</code> and <code>in2</code> are equal or aliased ). Used by the
	 * doAnalysis method.
	 */
	protected abstract void merge(Fact in1, Fact in2, Fact out);

	protected void merge(Fact inout, Fact in) {
		Fact tmp = newInitialFlow();
		merge(inout, in, tmp);
		copy(tmp, inout);
	}

	/**
	 * Creates a copy of the <code>source</code> flow object in
	 * <code>dest</code>.
	 */
	protected abstract void copy(Fact source, Fact dest);

	/**
	 * Return the BlockOrder specifying the order in which BasicBlocks should be
	 * visited in the main dataflow loop.
	 */
	protected abstract BlockOrder getBlockOrder();

	/**
	 * check that if the flow Fact before Vertex has been changed. so that it is
	 * not the same as flow Fact after
	 * 
	 * @param before
	 * @param after
	 * @return
	 */
	public boolean same(Fact before, Fact after) {
		return before.equals(after);
	}
}

// end

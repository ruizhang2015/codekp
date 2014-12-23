/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author zhouzhiyi
 * @time 2009-12-24 下午03:36:32
 * @modifier: root
 * @time 2009-12-24 下午03:36:32
 * @reviewer: root
 * @time 2009-12-24 下午03:36:32
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.analysis.dataflow;

import java.util.HashMap;
import java.util.Map;

import edu.pku.cn.analysis.DataflowAnalysis;
import edu.pku.cn.graph.Graph;

public abstract class AbstractFlow<Fact, AnalysisType extends DataflowAnalysis<Fact>> {
	/** Maps graph nodes to IN sets. */
	protected Map<Fact, AnalysisType> unitToBeforeFlow;
	protected Graph graph;

	/** Constructs a flow analysis on the given <code>DirectedGraph</code>. */
	public AbstractFlow(Graph graph) {
		unitToBeforeFlow = new HashMap<Fact, AnalysisType>(graph.getVerticeSize() * 2 + 1, 0.7f);
		this.graph = graph;
	}

	// /**
	// * Returns the flow object corresponding to the initial values for
	// * each graph node.
	// */
	// protected abstract A newInitialFlow();
	//
	// /**
	// * Returns the initial flow value for entry/exit graph nodes.
	// */
	// protected abstract A entryInitialFlow();
	//
	// /**
	// * Determines whether <code>entryInitialFlow()</code>
	// * is applied to trap handlers.
	// */
	// protected boolean treatTrapHandlersAsEntries() { return false; }
	//
	/** Returns true if this analysis is forwards. */
	protected abstract boolean isForward();

	//
	// /** Compute the merge of the <code>in1</code> and <code>in2</code> sets,
	// putting the result into <code>out</code>.
	// * The behavior of this function depends on the implementation ( it may be
	// necessary to check whether
	// * <code>in1</code> and <code>in2</code> are equal or aliased ).
	// * Used by the doAnalysis method. */
	// protected abstract void merge(A in1, A in2, A out);
	//
	// /** Creates a copy of the <code>source</code> flow object in
	// <code>dest</code>. */
	// protected abstract void copy(A source, A dest);

	/**
	 * Carries out the actual flow analysis. Typically called from a concrete
	 * FlowAnalysis's constructor.
	 */
	protected abstract void doAnalysis(Graph graph, AnalysisType analysis);
}

// end

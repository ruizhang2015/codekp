/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author liuxizhiyi
 * @time 2008-12-16 ����01:32:11
 * @modifier: liuxizhiyi
 * @time 2008-12-16 ����01:32:11
 * @reviewer: liuxizhiyi
 * @time 2008-12-16 ����01:32:11
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.cfg.analysis;

import java.util.BitSet;

import edu.pku.cn.analysis.FlowInterpreter;
import edu.pku.cn.analysis.ForwardFlowAnalysis;
import edu.pku.cn.graph.cfg.BasicBlock;
import edu.pku.cn.graph.cfg.CFG;
import edu.pku.cn.graph.cfg.Edge;

/**
 * 
 * @author liuxizhiyi
 */
public class ReachingAnalysis extends ForwardFlowAnalysis<CFG, Edge, BasicBlock, BitSet> {

	int size;

	public ReachingAnalysis(CFG graph) {
		super(graph, new FlowInterpreter<BasicBlock, BitSet>() {
			/**
			 * @see edu.pku.cn.analysis.FlowAnalysis#flowThrough(edu.pku.cn.graph.BasicVertex,
			 *      java.lang.Object, java.lang.Object)
			 */
			@Override
			public void flowThrough(BasicBlock node, BitSet in, BitSet out) {
				// TODO Auto-generated method stub

			}

			/**
			 * @see edu.pku.cn.analysis.AbstractFlowAnalysis#copy(java.lang.Object,
			 *      java.lang.Object)
			 */
			@Override
			public void copy(BitSet source, BitSet dest) {
				// TODO Auto-generated method stub

			}

			/**
			 * @see edu.pku.cn.analysis.AbstractFlowAnalysis#entryInitialFlow()
			 */
			@Override
			public BitSet entryInitialFlow() {
				// TODO Auto-generated method stub
				return new BitSet(0);
			}

			/**
			 * @see edu.pku.cn.analysis.AbstractFlowAnalysis#merge(java.lang.Object,
			 *      java.lang.Object, java.lang.Object)
			 */
			@Override
			public void merge(BitSet in1, BitSet in2, BitSet out) {
				// TODO Auto-generated method stub

			}

			/**
			 * @see edu.pku.cn.analysis.AbstractFlowAnalysis#newInitialFlow()
			 */
			@Override
			public BitSet newInitialFlow() {
				// TODO Auto-generated method stub
				return new BitSet(0);
			}
		});
	}
}

// end

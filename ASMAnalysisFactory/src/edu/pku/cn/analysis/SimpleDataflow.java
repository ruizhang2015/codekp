/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-1-9 ����11:25:55
 * @modifier: Administrator
 * @time 2009-1-9 ����11:25:55
 * @reviewer: Administrator
 * @time 2009-1-9 ����11:25:55
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.analysis;

import java.util.Iterator;
import java.util.Map;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import edu.pku.cn.graph.cfg.BasicBlock;
import edu.pku.cn.graph.cfg.CFG;
import edu.pku.cn.graph.cfg.Edge;
import edu.pku.cn.graph.cfg.EdgeType;
import edu.pku.cn.graph.visit.BlockOrder;
import edu.pku.cn.graph.visit.ReversePostOrder;

/**
 * The original Dataflow is separated into two parts: SimpleDataflow and
 * DataflowAnalysis.
 * 
 * @param <Fact>
 * @param <AnalysisType>
 * @author Administrator
 */
public class SimpleDataflow<Fact, AnalysisType extends DataflowAnalysis<Fact>> {
	private final boolean DEBUG = false;

	private InsnList insns;

	private CFG cfg;

	private AnalysisType analysis;

	private BlockOrder blockOrder;

	private boolean isForwards;

	private int numIterations;

	private static final int MAX_ITERS = 100;

	public SimpleDataflow(CFG cfg, AnalysisType analysis) {
		this.cfg = cfg;
		this.insns = cfg.getMethod().instructions;
		this.analysis = analysis;
		blockOrder = analysis.getBlockOrder();
		isForwards = analysis.isForwards();
		numIterations = 0;
	}

	public synchronized AnalysisType execute() throws AnalyzerException {
		boolean change;
		// OpcodeUtil.printInsnList(insns, cfg.getMethod().name +
		// cfg.getMethod().desc);
		initEntryBlock();

		int timestamp = 0;
		do {
			change = false;
			// if(numIterations >= 10)
			// break;
			if (numIterations >= MAX_ITERS) {
				assert false : "Too many iterations (" + numIterations + ") in dataflow when analyzing "
						+ getFullyQualifiedMethodName();
			}

			analysis.startIteration();

			Iterator<BasicBlock> i = blockOrder.blockIterator();
			while (i.hasNext()) {
				BasicBlock block = i.next();
				Fact start = analysis.getStartFact(block);
				Fact result = analysis.getResultFact(block);

				boolean isEndBlock = analysis.isEndBlock(block);
				boolean needToRecompute = false;
				
				if (!isEndBlock && start != null) {// if the original start is
					// not equal to new start,
					// need to recompute
					if (analysis.getNewStartFact(block) == null) {
						needToRecompute = true;
						analysis.setNewStartFact(block, analysis.createFact(start));
					} else if (!start.equals(analysis.getNewStartFact(block))) {
						needToRecompute = true;
					}
					Iterator<Edge> predEdgeIter = logicalPredecessorEdgeIterator(block);
					Fact startBack = analysis.getNewStartFact(block);
					// get all predecessors' result fact and merge them with the
					// new start fact
					while (predEdgeIter.hasNext()) {
						Edge edge = predEdgeIter.next();
						BasicBlock logicalPred = isForwards ? edge.getSource() : edge.getTarget();
						Fact pred = null;
						// ////////////perhaps we need to distinguish different
						// handlers
						if (edge.getType() != EdgeType.HANDLED_EXCEPTION_EDGE) {
							if (this.blockOrder instanceof ReversePostOrder) {
								int lastOpcode = -1;
								if (logicalPred.getEndInc() < insns.size()) {
									lastOpcode = insns.get(logicalPred.getEndInc()).getOpcode();
								}
								if (lastOpcode == Opcodes.IFNULL || lastOpcode == Opcodes.IFNONNULL) {
									// do nothing, does not do any merge work
								} else {
									pred = analysis.createFact(analysis.getResultFact(logicalPred));
									startBack = analysis.merge(startBack, pred);
								}
							} else {
								pred = analysis.createFact(analysis.getResultFact(logicalPred));
								startBack = analysis.merge(startBack, pred);
							}
						}
						// else{ //if this is an FINALLY_EDGE, we need to
						// distinguish insns throwing out exceptions
						// int startInc = logicalPred.getStartInc();
						// int endInc = logicalPred.getEndInc();
						// for(int inc = startInc; inc < endInc; inc ++){
						// if(OpcodeUtil.isExceptionThrow(insns.get(inc).getOpcode())){
						// pred = analysis.getFact(inc);
						// startBack = analysis.merge(startBack, pred);
						// }
						// }
						// }
					}
					// if the generated startBack is different from start, need
					// to recompute
					if (!analysis.same(startBack, start)) {
						needToRecompute = true;
						start = startBack;
					}
				}
				// if really need to recompute, recompute the current block's
				// frames
				if (needToRecompute && !isEndBlock) {
					analysis.setLastUpdateTimestamp(start, timestamp);
					analysis.setStartFact(block, analysis.createFact(start)); // set
					// start
					// facts
					// in
					// startFactMap
					analysis.setNewStartFact(block, analysis.createFact(start));// set
					// start
					// facts
					// in
					// blocks

					if (DEBUG) {
						if (Thread.activeCount() > 1) {
							Map map = Thread.getAllStackTraces();
							System.out.println("The thread number before calling transferVertex");
							for (Object key : map.keySet()) {
								System.out.println("key = " + key + "    value = " + map.get(key).toString());
							}
						}
					}

					Fact newResult = analysis.transferVertex(block);

					if (newResult != null && !analysis.same(newResult, result)) {
						analysis.setLastUpdateTimestamp(newResult, timestamp);
						analysis.setResultFact(block, analysis.createFact(newResult));
						change = true;
					}
					if (DEBUG) {
						if (Thread.activeCount() > 1) {
							Map map = Thread.getAllStackTraces();
							System.out.println("The thread number after calling transferVertex");
							for (Object key : map.keySet()) {
								System.out.println("key = " + key);
								StackTraceElement[] stack = (StackTraceElement[]) (map.get(key));
								for (int ii = 0; ii < stack.length; ii++) {
									System.out.println(stack[ii]);
								}
							}
						}
					}
				}
				if (DEBUG) {
					// System.out.println(block.toString());
				}
			}
			analysis.finishIteration();
			++numIterations;
			++timestamp;
		} while (change);

		return analysis;
	}

	public AnalysisType getAnalysis() {
		return analysis;
	}

	protected String getFullyQualifiedMethodName() {
		return cfg.getMethod().name;
	}

	private Iterator<Edge> logicalPredecessorEdgeIterator(BasicBlock block) {
		return isForwards ? block.inEdgeIterator() : block.outEdgeIterator();
	}

	private Iterator<Edge> logicalSuccessorEdgeIterator(BasicBlock block) {
		return isForwards ? block.outEdgeIterator() : block.inEdgeIterator();
	}

	private void initEntryBlock() {
		analysis.initEntryFact();
		Fact result = analysis.transferVertex(logicalEntryBlock());
		analysis.setResultFact(logicalEntryBlock(), result);
	}

	protected BasicBlock logicalEntryBlock() {
		if (blockOrder instanceof ReversePostOrder)
			return cfg.getRoot();
		else
			return cfg.getExit();
	}
}

// end

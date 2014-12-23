/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-3-25 ����04:31:40
 * @modifier: Administrator
 * @time 2009-3-25 ����04:31:40
 * @reviewer: Administrator
 * @time 2009-3-25 ����04:31:40
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.analysis;

import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.TryCatchBlockNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Frame;

import edu.pku.cn.analysis.common.Subroutine;
import edu.pku.cn.analysis.factory.RealValueDataflowFactory;
import edu.pku.cn.asm.tree.analysis.LockMap;
import edu.pku.cn.asm.tree.analysis.ReachingDefinitionFact;
import edu.pku.cn.asm.tree.analysis.RealInterpreter;
import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.detector.DataflowTestDriver;
import edu.pku.cn.graph.cfg.BasicBlock;
import edu.pku.cn.graph.cfg.CFG;
import edu.pku.cn.graph.visit.ReversePostOrder;
import edu.pku.cn.util.AnalysisFactoryManager;
import edu.pku.cn.util.BasicBlockUtil;
import edu.pku.cn.util.LocalVariableUtil;
import edu.pku.cn.util.OpcodeUtil;

public class ReachingDefinitionDataflowAnalysis extends BasicDataflowAnalysis<ReachingDefinitionFact> {

	private Map<Integer, List> localVariableMap;

	private Map<LocalVariableNode, Integer> lvIndexMap;

	private RealValueDataflowAnalysis rvAnalysis;

	private int size;
	// private MethodNode m;
	// private Subroutine[] subroutines;
	private InsnList insns;
	private List<TryCatchBlockNode>[] handlers;

	public ReachingDefinitionDataflowAnalysis(CFG cfg, RealValueDataflowAnalysis analysis) {

		this.blockOrder = new ReversePostOrder(cfg);
		this.isForwards = true; 

		RealValueDataflowFactory fac = (RealValueDataflowFactory) AnalysisFactoryManager
				.lookup(RealValueDataflowFactory.NAME);
		try {
			RealValueDataflowAnalysis rvAnalysis = fac.getAnalysis(cfg);
			localVariableMap = rvAnalysis.getLocalVariableMap();
		} catch (AnalyzerException e) {
			e.printStackTrace();
		}

		this.lvIndexMap = LocalVariableUtil.indexAllLocalVariables(localVariableMap);

		insns = cfg.getMethod().instructions;
		handlers = cfg.getHandlers();
		this.facts = new ReachingDefinitionFact[insns.size()];
		this.size = insns.size();
		// TODO Auto-generated constructor stub
	}

	@Override
	public ReachingDefinitionFact createFact() {
		ReachingDefinitionFact rdFact = new ReachingDefinitionFact();
		return rdFact;
	}

	@Override
	public ReachingDefinitionFact createFact(ReachingDefinitionFact fact) {
		ReachingDefinitionFact rdFact = new ReachingDefinitionFact(fact);
		return rdFact;
	}

	@Override
	public ReachingDefinitionFact getNewStartFact(BasicBlock block) {
		return facts[block.getStartInc()];
	}

	@Override
	public void initEntryFact() {
		ReachingDefinitionFact rdFact = createFact();
		Set<LocalVariableNode> arguments = rvAnalysis.getArguments();
		for (LocalVariableNode lvNode : arguments) {
			int index = lvIndexMap.get(lvNode);
			BitSet bitSet = new BitSet(size);
			bitSet.set(0);// 0��
			rdFact.put(index, bitSet);
		}
		rdFact.setLastTimestamp(0);
		startFactMap.put(blockOrder.getEntry(), rdFact);
		facts[0] = rdFact;
	}

	@Override
	public boolean isEndBlock(BasicBlock block) {
		return block.getStartInc() >= insns.size();
	}

	@Override
	public ReachingDefinitionFact merge(ReachingDefinitionFact start, ReachingDefinitionFact pred) {
		ReachingDefinitionFact result = createFact(start);
		if (!pred.equals(start)) {
			result.orOp(pred);
		}
		return result;
	}

	@Override
	public boolean same(ReachingDefinitionFact fact1, ReachingDefinitionFact fact2) {
		if (fact1 == null)
			return fact2 == null;
		return fact1.equals(fact2);
	}

	@Override
	public void setNewStartFact(BasicBlock block, ReachingDefinitionFact fact) {
		facts[block.getStartInc()] = fact;
	}

	@Override
	public ReachingDefinitionFact transferVertex(BasicBlock block) {
		boolean isChanged = true;

		Iterator<AbstractInsnNode> insnIter = block.nodeIterator(false);
		int insn = block.getStartInc() - 1;
		int insnOpcode = -1;
		AbstractInsnNode insnNode = null;

		ReachingDefinitionFact fact = createFact(startFactMap.get(block));
		while (insnIter.hasNext()) {
			isChanged = false;
			if (insn + 2 == facts.length)
				break;
			insn++;

			insnNode = insnIter.next();
			insnOpcode = insnNode.getOpcode();
			if (insnOpcode >= Opcodes.ISTORE && insnOpcode <= Opcodes.ASTORE) {
				defOp(fact, insnNode.index, ((VarInsnNode) insnNode).var);
			} else if (insnOpcode == Opcodes.IINC) {
				defOp(fact, insnNode.index, ((IincInsnNode) insnNode).var);
			}
			try {
				if (insnNode instanceof JumpInsnNode) {
					JumpInsnNode j = (JumpInsnNode) insnNode;
					if (insnOpcode != Opcodes.GOTO && insnOpcode != Opcodes.JSR) {
						isChanged |= merge(insn + 1, fact);
					}
					int jump = insns.indexOf(j.label);
					isChanged |= merge(jump, fact);
				} else if (insnNode instanceof LookupSwitchInsnNode) {
					LookupSwitchInsnNode lsi = (LookupSwitchInsnNode) insnNode;
					int jump = insns.indexOf(lsi.dflt);
					isChanged = merge(jump, fact);
					for (int j = 0; j < lsi.labels.size(); j++) {
						LabelNode label = (LabelNode) lsi.labels.get(j);
						jump = insns.indexOf(label);
						isChanged |= merge(jump, fact);
					}
				} else if (insnNode instanceof TableSwitchInsnNode) {
					TableSwitchInsnNode tsi = (TableSwitchInsnNode) insnNode;
					int jump = insns.indexOf(tsi.dflt);
					isChanged |= merge(jump, fact);
					for (int j = 0; j < tsi.labels.size(); j++) {
						LabelNode label = (LabelNode) tsi.labels.get(j);
						jump = insns.indexOf(label);
						isChanged |= merge(jump, fact);
					}
				} else if (insnOpcode != Opcodes.ATHROW
						&& (insnOpcode < Opcodes.IRETURN || insnOpcode > Opcodes.RETURN)) {
					isChanged |= merge(insn + 1, fact);
				}
				List<TryCatchBlockNode> insnHandlers = handlers[insn];
				if (insnHandlers != null) {
					for (int i = 0; i < insnHandlers.size(); i++) {
						TryCatchBlockNode tcb = (TryCatchBlockNode) insnHandlers.get(i);
						int jump = insns.indexOf(tcb.handler);
						ReachingDefinitionFact handler = createFact(facts[insn]);
						handler.setLastTimestamp(handler.getLastTimestamp() + 1);
						boolean handlerIsChanged = merge(jump, handler);
						isChanged |= handlerIsChanged;

					}
				}
			} catch (AnalyzerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return fact;
	}

	private void defOp(ReachingDefinitionFact fact, int index, int var) {
		int allIndex = LocalVariableUtil.getAllIndex(this.localVariableMap, this.lvIndexMap, index, var);
		BitSet bitSet = new BitSet(size);
		bitSet.set(index);
		fact.put(allIndex, bitSet);
	}

	private boolean merge(final int insn, final ReachingDefinitionFact fact) throws AnalyzerException {
		ReachingDefinitionFact oldFact = facts[insn];
		boolean change = false;
		if (oldFact == null) {
			facts[insn] = createFact(fact);
			change = true;
		} else {
			ReachingDefinitionFact temp = createFact(oldFact);
			oldFact.orOp(fact);
			change = !oldFact.equals(temp);
		}
		return change;
	}

	public static void main(String[] args) {
		DataflowTestDriver<ReachingDefinitionFact, ReachingDefinitionDataflowAnalysis> driver = new DataflowTestDriver<ReachingDefinitionFact, ReachingDefinitionDataflowAnalysis>() {
			public SimpleDataflow<ReachingDefinitionFact, ReachingDefinitionDataflowAnalysis> createDataflow(
					ClassNode cc, MethodNode method) {
				// TODO Auto-generated method stub
				try {
					CFG cfg = method.getCFG();
					RealValueDataflowFactory fac = (RealValueDataflowFactory) AnalysisFactoryManager
							.lookup(RealValueDataflowFactory.NAME);
					RealValueDataflowAnalysis rvAnalysis = fac.getAnalysis(cfg);

					ReachingDefinitionDataflowAnalysis analysis = new ReachingDefinitionDataflowAnalysis(cfg,
							rvAnalysis);
					SimpleDataflow<ReachingDefinitionFact, ReachingDefinitionDataflowAnalysis> dataflow = new SimpleDataflow<ReachingDefinitionFact, ReachingDefinitionDataflowAnalysis>(
							cfg, analysis);
					dataflow.execute();
					return dataflow;
				} catch (AnalyzerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public void examineResults(
					SimpleDataflow<ReachingDefinitionFact, ReachingDefinitionDataflowAnalysis> dataflow) {
				// TODO Auto-generated method stub
				ReachingDefinitionFact[] facts = dataflow.getAnalysis().facts;
				for (int i = 0; i < facts.length; i++) {
					System.out.println(i + "   " + facts[i]);
				}
			}
		};
		driver.execute("TestAnalysisLiveVariable");
	}
}

// end

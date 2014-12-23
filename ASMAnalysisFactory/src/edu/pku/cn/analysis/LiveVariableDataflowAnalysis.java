/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-3-7 ����08:11:18
 * @modifier: Administrator
 * @time 2009-3-7 ����08:11:18
 * @reviewer: Administrator
 * @time 2009-3-7 ����08:11:18
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
import java.util.Map.Entry;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.TryCatchBlockNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Frame;

import edu.pku.cn.analysis.common.Subroutine;
import edu.pku.cn.analysis.factory.RealValueDataflowFactory;
import edu.pku.cn.asm.tree.analysis.LoadStoreFact;
import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.detector.DataflowTestDriver;
import edu.pku.cn.graph.cfg.CFG;
import edu.pku.cn.graph.cfg.BasicBlock;
import edu.pku.cn.graph.cfg.CFGFactory;
import edu.pku.cn.graph.visit.PostOrder;
import edu.pku.cn.util.AnalysisFactoryManager;
import edu.pku.cn.util.LocalVariableUtil;
import edu.pku.cn.util.OpcodeUtil;

public class LiveVariableDataflowAnalysis extends BasicDataflowAnalysis<LoadStoreFact> {

	private static boolean DEBUG = false;

	private Map<Integer, List> localVariableMap;

	private Map<LocalVariableNode, Integer> lvIndexMap;// �洢�ֲ�������ȫ������֮���ӳ���ϵ

	private RealValueDataflowAnalysis realValueDataflow;

	private int locals;
	private MethodNode m;
	protected InsnList insns;
	protected Subroutine[] subroutines;
	protected List<TryCatchBlockNode>[] handlers;

	public LiveVariableDataflowAnalysis(CFG cfg) {

		this.blockOrder = new PostOrder(cfg);
		this.isForwards = false; // ������� backward analysis
		RealValueDataflowFactory fac = (RealValueDataflowFactory) AnalysisFactoryManager
				.lookup(RealValueDataflowFactory.NAME);
		try {
			RealValueDataflowAnalysis rvAnalysis = fac.getAnalysis(cfg);
			localVariableMap = rvAnalysis.getLocalVariableMap();
		} catch (AnalyzerException e) {
			e.printStackTrace();
		}
		// this.lvIndexMap = new HashMap<LocalVariableNode, Integer>();

		// int allIndex = 0;
		// Set<Entry<Integer, List>> entries = localVariableMap.entrySet();
		// for(Entry<Integer, List>entry : entries){
		// List lvNodes = entry.getValue();
		// for(int i = 0; i < lvNodes.size(); i ++){
		// lvIndexMap.put((LocalVariableNode)lvNodes.get(i), allIndex++);
		// }
		// }
		lvIndexMap = LocalVariableUtil.indexAllLocalVariables(localVariableMap);

		locals = lvIndexMap.size(); // locals represents the number of all local
		// variable nodes
		insns = cfg.getMethod().instructions;
		this.facts = new LoadStoreFact[insns.size()];
	}

	public int getLocals() {
		return locals;
	}

	public LocalVariableNode getLocalVariableNode(Integer allIndex) throws AnalyzerException {
		Set<Entry<LocalVariableNode, Integer>> entries = lvIndexMap.entrySet();
		for (Entry<LocalVariableNode, Integer> entry : entries) {
			if (entry.getValue().equals(allIndex))
				return entry.getKey();
		}
		throw new AnalyzerException(
				"No local variable node can be found with the given allIndex in LiveVariableDataflowAnalysis");
	}

	@Override
	public LoadStoreFact createFact() {
		LoadStoreFact lsFact = new LoadStoreFact(locals * 2);
		return lsFact;
	}

	@Override
	public LoadStoreFact createFact(LoadStoreFact fact) {
		LoadStoreFact lsFact = new LoadStoreFact();
		lsFact.bitSet = (BitSet) fact.bitSet.clone();
		lsFact.setLastUpdateTimestamp(fact.getLastUpdateTimestamp());
		return lsFact;
	}

	@Override
	public int getLastUpdateTimestamp(LoadStoreFact fact) {
		return fact.getLastUpdateTimestamp();
	}

	@Override
	public LoadStoreFact getNewStartFact(BasicBlock block) {
		return facts[block.getEndInc()];
	}

	@Override
	public void initEntryFact() {
		LoadStoreFact lsFact = createFact();
		lsFact.setLastUpdateTimestamp(0);
		setStartFact(blockOrder.getExit(), lsFact);
		facts[insns.size() - 1] = lsFact;
	}

	@Override
	public boolean isEndBlock(BasicBlock block) {// unchecked
		return block.getStartInc() >= insns.size();
	}

	@Override
	public LoadStoreFact merge(LoadStoreFact start, LoadStoreFact pred) {// the
		// pred
		// is
		// logical
		// predecessor,
		// in
		// the
		// backward
		// analysis,
		// this
		// predecessor
		// is
		// subsequence
		LoadStoreFact result = createFact(start);
		if (!pred.equals(start)) {
			for (int i = 0; i < locals; i++) {
				result.bitSet.set(i, result.bitSet.get(i) | pred.bitSet.get(i));
			}
			for (int i = locals; i < locals * 2; i++) {
				result.bitSet.set(i, result.bitSet.get(i) & pred.bitSet.get(i));
			}
			// result.bitSet.or(pred.bitSet);
		}
		return result;
	}

	@Override
	public boolean same(LoadStoreFact fact1, LoadStoreFact fact2) {
		if (fact1 == null)
			return fact2 == null;
		return fact1.equals(fact2);
	}

	@Override
	public void setLastUpdateTimestamp(LoadStoreFact fact, int timestamp) {
		fact.setLastUpdateTimestamp(timestamp);
	}

	@Override
	public void setNewStartFact(BasicBlock block, LoadStoreFact fact) {
		facts[block.getEndInc()] = fact;
	}

	@Override
	public LoadStoreFact transferVertex(BasicBlock block) {
		if (block.getStartInc() >= insns.size()) {// ���ǰblock�в����κ�insn������Exit
			// block���򷵻�startFact������ʱresultFact
			// == startFact
			return createFact(this.getStartFact(block));
		}
		boolean isChanged = true;

		int insn = block.getInsnList().size();
		AbstractInsnNode insnNode = null;

		LoadStoreFact fact = createFact(getStartFact(block));
		// List<AbstractInsnNode> insns = block.getInsnList();
		Iterator<AbstractInsnNode> iter = block.nodeIterator(true);
		while (iter.hasNext()) {
			isChanged = false;
			insn--;
			insnNode = iter.next();
			int insnOpcode = insnNode.getOpcode();
			try {
				// if(insn == insns.size() - 1){ //block�����һ��ָ��
				// Iterator<BasicBlock> succIter = block.successorIterator();
				// BasicBlock succBlock = null;
				// while(succIter.hasNext()){
				// succBlock = succIter.next();
				// isChanged |=
				// merge(block.getEndInc(),this.getResultFact(succBlock));
				// }
				// if(isChanged){
				// setStartFact(block, createFact(facts[block.getEndInc()]));
				// }
				// }

				if (insnOpcode >= Opcodes.ILOAD && insnOpcode <= Opcodes.ALOAD || insnOpcode == Opcodes.RET) {
					useOp(fact, insnNode.index, ((VarInsnNode) insnNode).var);
				} else if (insnOpcode == Opcodes.IINC) {
					defOp(fact, insnNode.index, ((IincInsnNode) insnNode).var);
					useOp(fact, insnNode.index, ((IincInsnNode) insnNode).var);
				} else if (insnOpcode >= Opcodes.ISTORE && insnOpcode <= Opcodes.ASTORE) {
					defOp(fact, insnNode.index, ((VarInsnNode) insnNode).var);
				}

				if (insn == 0) {
					setResultFact(block, createFact(fact));
				} else {
					isChanged |= merge(block.getStartInc() + insn - 1, fact);
				}
			} catch (AnalyzerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return fact;
	}

	private boolean merge(final int insn, final LoadStoreFact fact) throws AnalyzerException {
		// lockMap representing fact before instructions
		LoadStoreFact oldFact = facts[insn];
		boolean change = false;
		if (oldFact == null) {
			facts[insn] = createFact(fact);
			change = true;
		} else {
			LoadStoreFact temp = createFact(oldFact);
			// oldFact.bitSet.or(fact.bitSet);
			oldFact.bitSet = (BitSet) merge(oldFact, fact).bitSet.clone();
			change = !oldFact.equals(temp);
		}
		return change;
	}

	private void defOp(LoadStoreFact fact, int index, int var) {
		// LocalVariableNode tempNode = lookForLvNode(index, var);
		// int allIndex = lvIndexMap.get(tempNode);
		// int allIndex = LocalVariableUtil.getAllIndex(this.localVariableMap,
		// this.lvIndexMap, index, var);
		int allIndex = getAllIndex(index, var);
		fact.bitSet.clear(allIndex);
		fact.bitSet.set(locals + allIndex);
	}

	public int getAllIndex(int index, int var) {
		// LocalVariableNode tempNode =
		// LocalVariableUtil.lookForLvNode(this.localVariableMap, index, var);
		LocalVariableNode tempNode = lookForLvNode(index, var);
		return lvIndexMap.get(tempNode);
	}

	private void useOp(LoadStoreFact fact, int index, int var) {
		// LocalVariableNode tempNode = lookForLvNode(index, var);
		// int allIndex = lvIndexMap.get(tempNode);
		int allIndex = LocalVariableUtil.getAllIndex(this.localVariableMap, this.lvIndexMap, index, var);
		fact.bitSet.clear(locals + allIndex);
		fact.bitSet.set(allIndex);
	}

	private LocalVariableNode lookForLvNode(int index, int var) {
		LocalVariableNode tempNode = null;
		List lvList = localVariableMap.get(var);
		int maxStart = -1;
		int minEnd = Integer.MAX_VALUE;
		for (int i = 0; i < lvList.size(); i++) {
			LocalVariableNode lvNode = (LocalVariableNode) lvList.get(i);
			if (lvNode.start.index <= index + 1 && lvNode.end.index >= index && lvNode.start.index >= maxStart
					&& lvNode.end.index <= minEnd) {
				maxStart = lvNode.start.index;
				minEnd = lvNode.end.index;
				tempNode = lvNode;
			}
		}
		if (tempNode == null) {
			try {
				throw new AnalyzerException("The wanted localVariable in LiveVariableDatflowAnalysis is not found!");
			} catch (AnalyzerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return tempNode;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < facts.length; i++) {
			buf.append(i + "   " + facts[i] + "\n");
		}
		return buf.toString();
	}

	public static void main(String[] args) {
		DataflowTestDriver<LoadStoreFact, LiveVariableDataflowAnalysis> driver = new DataflowTestDriver<LoadStoreFact, LiveVariableDataflowAnalysis>() {

			@Override
			public SimpleDataflow<LoadStoreFact, LiveVariableDataflowAnalysis> createDataflow(ClassNode cc,
					MethodNode method) {
				// TODO Auto-generated method stub
				try {
					CFG cfg = ((CFGFactory) AnalysisFactoryManager.lookup(CFGFactory.NAME)).analyze(method);

					LiveVariableDataflowAnalysis analysis = new LiveVariableDataflowAnalysis(cfg);
					SimpleDataflow<LoadStoreFact, LiveVariableDataflowAnalysis> dataflow = new SimpleDataflow<LoadStoreFact, LiveVariableDataflowAnalysis>(
							cfg, analysis);
					if (DEBUG) {
						OpcodeUtil.printInsnList(method.instructions, method.name);
					}
					dataflow.execute();
					return dataflow;
				} catch (AnalyzerException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public void examineResults(SimpleDataflow<LoadStoreFact, LiveVariableDataflowAnalysis> dataflow) {
				// TODO Auto-generated method stub
				LoadStoreFact[] facts = dataflow.getAnalysis().facts;

				for (int i = 0; i < facts.length; i++) {
					System.out.println(i + "   " + facts[i]);
				}
			}
		};
		driver.execute("TestAnalysisLiveVariable");
	}
}

// end

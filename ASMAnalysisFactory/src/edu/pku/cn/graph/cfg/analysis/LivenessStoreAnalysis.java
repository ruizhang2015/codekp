/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-5-29 ����02:58:58
 * @modifier: Liuxizhiyi
 * @time 2008-5-29 ����02:58:58
 * @reviewer: Liuxizhiyi
 * @time 2008-5-29 ����02:58:58
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.cfg.analysis;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicInterpreter;

import edu.pku.cn.analysis.AnalysisFactory;
import edu.pku.cn.graph.cfg.BasicBlock;
import edu.pku.cn.graph.cfg.CFG;
import edu.pku.cn.graph.cfg.CFGFactory;
import edu.pku.cn.graph.cfg.Edge;
import edu.pku.cn.graph.visit.GraphVisitor;
import edu.pku.cn.graph.visit.ReverseTopologic;

/**
 * In(B)=Union(use(B),Out(B)-def(B)) Out(B)=Union(In(successor of B))
 * 
 * @author Liuxizhiyi
 */
public class LivenessStoreAnalysis extends AnalysisFactory<MethodNode, LivenessStoreResult> {
	public final static String NAME = "LivenessStoreAnalysis";

	Map<Integer, BitSet> in;

	class Visitor extends GraphVisitor<CFG, Edge, BasicBlock> {
		Map<Integer, BitSet> use;
		Map<Integer, BitSet> def;
		Map<Integer, BitSet> out;
		Map<Integer, Map<Integer, BitSet>> defOutOfBlock;
		CFG cfg;
		boolean isChanged = false;

		public Visitor(CFG cfg) {
			this.cfg = cfg;
			def = new HashMap<Integer, BitSet>(cfg.getVerticeSize());
			use = new HashMap<Integer, BitSet>(cfg.getVerticeSize());
			out = new HashMap<Integer, BitSet>(cfg.getVerticeSize());
			defOutOfBlock = new HashMap<Integer, Map<Integer, BitSet>>(cfg.getVerticeSize());
		}

		// BitSet visitedVertex;
		@Override
		public void visitVertex(BasicBlock vertex) {
			// System.out.print(vertex.getLabel()+ " ");
			isChanged = false;
			int index = vertex.getLabel();
			if (use.get(index) == null) {
				Map<Integer, BitSet> readLine = new HashMap<Integer, BitSet>();
				Map<Integer, BitSet> writeLine = new HashMap<Integer, BitSet>();
				BitSet tempUse = new BitSet();
				BitSet tempDef = new BitSet();
				List<AbstractInsnNode> insn = vertex.getInsnList();

				for (int i = insn.size() - 1; i >= 0; i--) {
					AbstractInsnNode node = insn.get(i);
					int allIndex = -1;
					int readOrWrite = 0; // read=1,write=-1,readAndWrite=0;
					if (node.getOpcode() >= Opcodes.ILOAD && node.getOpcode() <= Opcodes.ALOAD) {
						VarInsnNode var = (VarInsnNode) node;
						LocalVariableNode localNode = cfg.getLocalVariableNode(var.index, var.var);
						if (localNode == null) {
							// System.out.println("null point "+var.index+" "+cfg.getMethod().name);
						} else {
							allIndex = localNode.allIndex;
							tempUse.set(allIndex);
							readOrWrite = 1;
						}
					} else if (node.getOpcode() >= Opcodes.ISTORE && node.getOpcode() <= Opcodes.ASTORE) {
						VarInsnNode var = (VarInsnNode) node;
						LocalVariableNode localNode = cfg.getLocalVariableNode(var.index, var.var);
						if (localNode == null) {
							// System.out.println("null point "+var.index+" "+cfg.getMethod().name);
						} else {
							allIndex = localNode.allIndex;
							tempDef.set(allIndex);
							readOrWrite = -1;
						}
					} else if (node.getOpcode() == Opcodes.IINC) {
						IincInsnNode iinc = (IincInsnNode) node;
						allIndex = cfg.getLocalVariableNode(iinc.index, iinc.var).allIndex;
						tempDef.set(allIndex);
						tempUse.set(allIndex);
					}
					if (allIndex >= 0) {
						BitSet read = readLine.get(allIndex);
						BitSet write = writeLine.get(allIndex);
						if (read == null) {
							read = new BitSet();
							readLine.put(allIndex, read);
						}
						if (write == null) {
							write = new BitSet();
							writeLine.put(allIndex, write);
						}
						if (readOrWrite == 1)
							read.set(node.index);
						else if (readOrWrite == -1)
							write.set(node.index);
						else {
							read.set(node.index - 1);
							write.set(node.index);
						}
					}
				}
				use.put(index, tempUse);
				def.put(index, tempDef);
				if (readLine.size() > 0) {
					Iterator<Entry<Integer, BitSet>> iter = writeLine.entrySet().iterator();
					while (iter.hasNext()) {
						Entry<Integer, BitSet> entry = iter.next();
						BitSet read = readLine.get(entry.getKey());
						BitSet write = entry.getValue();
						int from = 0;
						for (int i = read.nextSetBit(0); i >= 0; i = read.nextSetBit(i + 1)) {
							int lastSet = -1;
							for (int j = write.nextSetBit(from); j >= from && j <= i; j = write.nextSetBit(j + 1)) {
								lastSet = j;
							}
							if (lastSet >= from && lastSet <= i)
								write.clear(lastSet);
							from = i;
						}
					}
					defOutOfBlock.put(index, writeLine);
				}
			}

			Iterator<Edge> iter = vertex.outEdgeIterator();
			BitSet tempOut = new BitSet();
			while (iter.hasNext()) {
				Edge edge = iter.next();
				tempOut.or(in.get(edge.getTarget().getLabel()));
			}
			out.put(index, (BitSet) tempOut.clone());
			tempOut.andNot(def.get(index));
			tempOut.or(use.get(index));
			if (tempOut.equals(in.get(index)) == false) {
				isChanged = true;
				in.put(index, tempOut);
			}

		}

		public boolean setVisitBit() {
			return !isChanged;
		}
	}

	public String owner;

	public LivenessStoreResult analysis(MethodNode method) throws AnalyzerException {
		// here may be replace by some factory share field
		CFGFactory cfgFactory = CFGFactory.newInstance();
		// end
		cfgFactory.setOwner(owner);
		cfgFactory.setInterpreter(new BasicInterpreter());

		CFG cfg = cfgFactory.getAnalysis(method);

		if (cfg.hasLocalVariable() == false)
			throw new AnalyzerException("without LocalVariableTable,some analysis can not be excuted");
		Visitor visitor = new Visitor(cfg);

		in = new HashMap<Integer, BitSet>(cfg.getVerticeSize());
		for (int i = 0; i < cfg.getVerticeSize(); i++) {
			in.put(i, new BitSet());
		}
		ReverseTopologic<CFG, Edge, BasicBlock> rTopogic = new ReverseTopologic<CFG, Edge, BasicBlock>(cfg);

		rTopogic.accept(visitor);
		// GraphPrinter.print(cfg);

		return new LivenessStoreResult(in, visitor.out, visitor.defOutOfBlock, cfg.getRoot().getLabel(), cfg
				.getLocalVariableTable(), cfg.getLineNumberTable());
	}

	public String getName() {
		return NAME;
	}
}

// end

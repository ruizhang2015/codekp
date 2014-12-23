/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-11-24 ����01:57:11
 * @modifier: Liuxizhiyi
 * @time 2008-11-24 ����01:57:11
 * @reviewer: Liuxizhiyi
 * @time 2008-11-24 ����01:57:11
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.cfg.analysis;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicInterpreter;

import edu.pku.cn.analysis.AnalysisFactory;
import edu.pku.cn.graph.cfg.BasicBlock;
import edu.pku.cn.graph.cfg.BlockType;
import edu.pku.cn.graph.cfg.CFG;
import edu.pku.cn.graph.cfg.CFGFactory;
import edu.pku.cn.graph.cfg.Edge;
import edu.pku.cn.graph.cfg.EdgeType;
import edu.pku.cn.graph.cfg.WhileBlock;
import edu.pku.cn.graph.visit.GraphVisitor;
import edu.pku.cn.graph.visit.Topologic;

/**
 * In(B)=Union(Out[Predecessor of B]) Out(B)=Union(gen(B),In(B)-kill(B))
 * 
 * @author Liuxizhiyi
 */
public class ReachingDefinitionAnalysis extends AnalysisFactory<MethodNode, ReachingDefinitionResult> {
	public final static String NAME = "ReachingDefinitionAnalysis";
	public String owner;

	private List<BitSet> gen;
	private List<BitSet> kill;
	private List<BitSet> out;

	class Visitor extends GraphVisitor<CFG, Edge, BasicBlock> {
		boolean isChanged = false;
		BitSet visitedVertex;

		@Override
		public void visitVertex(BasicBlock vertex) {
			isChanged = false;
			int index = vertex.getLabel();

			BitSet inFrom = new BitSet();
			if (!visitedVertex.get(vertex.getLabel()) || !(vertex instanceof WhileBlock)) {
				Iterator<Edge> iter = vertex.inEdgeIterator();
				while (iter.hasNext()) {
					BasicBlock from = iter.next().getSource();
					inFrom.or(out.get(from.getLabel()));
				}
			} else {
				Iterator<Edge> iter = vertex.inEdgeIterator();
				while (iter.hasNext()) {
					Edge edge = iter.next();
					if ((vertex.getType() == BlockType.WHILE_BLOCK && edge.getType() != EdgeType.FALL_THROUGH_EDGE)
							|| (vertex.getType() == BlockType.DOWHILE_BLOCK && edge.getType() != EdgeType.IFCMP_EDGE_JUMP))
						continue;
					BasicBlock from = edge.getSource();
					inFrom.or(out.get(from.getLabel()));
				}
			}
			inFrom.andNot(kill.get(index));
			inFrom.or(gen.get(index));
			if (inFrom.equals(out.get(index)) == false) {
				isChanged = true;
				out.set(index, inFrom);
			}
			visitedVertex.set(vertex.getLabel());
		}

		public boolean setVisitBit() {
			return !isChanged;
		}
	}

	/**
	 * @see edu.pku.cn.analysis.AnalysisFactory#analysis(java.lang.Object)
	 */
	@Override
	public ReachingDefinitionResult analysis(MethodNode target) throws AnalyzerException {
		// here may be replace by some factory share field
		CFGFactory cfgFactory = CFGFactory.newInstance();
		// end
		cfgFactory.setOwner(owner);
		cfgFactory.setInterpreter(new BasicInterpreter());

		CFG cfg = cfgFactory.getAnalysis(target);
		List<BitSet> varToInsn = new ArrayList<BitSet>(cfg.getLocalVariableTable().size());
		initializeBitSetList(varToInsn, cfg.getLocalVariableTable().size());
		Map<Integer, Integer> InsnToVar = new HashMap<Integer, Integer>();
		gen = new ArrayList<BitSet>(cfg.getVerticeSize());
		kill = new ArrayList<BitSet>(cfg.getVerticeSize());

		out = new ArrayList<BitSet>(cfg.getVerticeSize());
		if (cfg.hasLocalVariable() == false)
			throw new AnalyzerException("without LocalVariableTable,some analysis can not be excuted");
		for (int i = 0; i < cfg.getVerticeSize(); i++) {
			BasicBlock vertex = cfg.getVertex(i);
			List<AbstractInsnNode> insn = vertex.getInsnList();
			for (int j = 0; j < insn.size(); j++) {
				int index = 0;
				int insnIndex = 0;
				int opcode = insn.get(j).getOpcode();
				if (opcode >= Opcodes.ISTORE && opcode < Opcodes.IASTORE) {
					VarInsnNode node = (VarInsnNode) insn.get(j);
					index = cfg.getLocalVariableNode(node.index, node.var).allIndex;
					insnIndex = node.index;
				} else if (opcode == Opcodes.IINC) {
					IincInsnNode node = (IincInsnNode) insn.get(j);
					index = cfg.getLocalVariableNode(node.index, node.var).allIndex;
					insnIndex = node.index;
				} else
					continue;
				varToInsn.get(index).set(insnIndex, true);
				InsnToVar.put(insnIndex, index);
			}
		}
		for (int i = 0; i < cfg.getVerticeSize(); i++) {
			BasicBlock vertex = cfg.getVertex(i);
			List<AbstractInsnNode> insn = vertex.getInsnList();

			BitSet blockGen = new BitSet();
			gen.add(vertex.getLabel(), blockGen);
			BitSet blockKill = new BitSet();
			kill.add(vertex.getLabel(), blockKill);

			for (int j = 0; j < insn.size(); j++) {
				int index = 0;
				int insnIndex = 0;
				int opcode = insn.get(j).getOpcode();
				if (opcode >= Opcodes.ISTORE && opcode < Opcodes.IASTORE) {
					VarInsnNode node = (VarInsnNode) insn.get(j);
					index = cfg.getLocalVariableNode(node.index, node.var).allIndex;
					insnIndex = node.index;
				} else if (opcode == Opcodes.IINC) {
					IincInsnNode node = (IincInsnNode) insn.get(j);
					index = cfg.getLocalVariableNode(node.index, node.var).allIndex;
					insnIndex = node.index;
				} else
					continue;
				BitSet killj = (BitSet) varToInsn.get(index).clone();
				killj.set(insnIndex, false);
				blockKill.or(killj);
			}
			BitSet tempBlockKill = (BitSet) blockKill.clone();
			for (int j = 0; j < insn.size(); j++) {
				int index = 0;
				int insnIndex = 0;
				int opcode = insn.get(j).getOpcode();
				if (opcode >= Opcodes.ISTORE && opcode < Opcodes.IASTORE) {
					VarInsnNode node = (VarInsnNode) insn.get(j);
					index = cfg.getLocalVariableNode(node.index, node.var).allIndex;
					insnIndex = node.index;
				} else if (opcode == Opcodes.IINC) {
					IincInsnNode node = (IincInsnNode) insn.get(j);
					index = cfg.getLocalVariableNode(node.index, node.var).allIndex;
					insnIndex = node.index;
				} else
					continue;
				BitSet killj = (BitSet) varToInsn.get(index).clone();
				killj.set(insnIndex, false);
				tempBlockKill.andNot(killj);
				BitSet genj = new BitSet();
				genj.set(insnIndex, true);
				genj.andNot(tempBlockKill);
				blockGen.or(genj);
			}
			out.add(i, new BitSet());
		}
		int entry = cfg.getRoot().getLabel();
		BitSet entryOut = new BitSet();
		entryOut.andNot(kill.get(entry));
		entryOut.or(gen.get(entry));
		out.set(entry, entryOut);
		Topologic<CFG, Edge, BasicBlock> dfs = new Topologic<CFG, Edge, BasicBlock>(cfg);
		Visitor visitor = new Visitor();
		visitor.visitedVertex = new BitSet(cfg.getVerticeSize());
		dfs.accept(visitor);

		return new ReachingDefinitionResult(InsnToVar, out.get(cfg.getExit().getLabel()));
	}

	void initializeBitSetList(List<BitSet> sets, int size) {
		for (int i = 0; i < size; i++)
			sets.add(new BitSet());
	}

	public static void main(String[] args) {
		ClassReader cr;
		try {
			cr = new ClassReader("edu.pku.cn.graph.cfg.test.LiveStoreTest");
			ClassNode cn = new ClassNode();
			cr.accept(cn, 0);

			List<MethodNode> methods = cn.methods;
			for (int i = 0; i < methods.size(); ++i) {
				MethodNode method = methods.get(i);
				if (method.name.equals("testReaching"))
					if (method.instructions.size() > 0) {
						System.out.println("DFS result:" + method.name);
						ReachingDefinitionAnalysis analysis = new ReachingDefinitionAnalysis();
						analysis.owner = cn.name;
						ReachingDefinitionResult result = analysis.analysis(method);
						BitSet re = result.getResult();
						for (int j = re.nextSetBit(0); j >= 0; j = re.nextSetBit(j + 1)) {
							System.out.print(j + " ");
						}
						System.out.println();
					}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

// end

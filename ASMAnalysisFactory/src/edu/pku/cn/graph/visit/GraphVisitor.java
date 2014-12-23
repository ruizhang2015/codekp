/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-6-2 ����11:09:06
 * @modifier: Liuxizhiyi
 * @time 2008-6-2 ����11:09:06
 * @reviewer: Liuxizhiyi
 * @time 2008-6-2 ����11:09:06
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.visit;

import java.util.List;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.tree.analysis.Frame;

import edu.pku.cn.graph.BasicEdge;
import edu.pku.cn.graph.BasicGraph;
import edu.pku.cn.graph.BasicVertex;

/**
 * the visitor of BasicGraph
 * 
 * @author Liuxizhiyi
 */
public abstract class GraphVisitor<GraphType extends BasicGraph<EdgeType, VertexType>, EdgeType extends BasicEdge<EdgeType, VertexType>, VertexType extends BasicVertex<EdgeType, VertexType>> {
	/**
	 * visit the edge of graph
	 * 
	 * @param edge
	 */
	public void visitEdge(EdgeType edge) {
	}

	public void visitCrossEdge(EdgeType edge) {
	}

	/**
	 * visit the edge which construct a circle.
	 * 
	 * @param edge
	 */
	public void visitCircleEdge(EdgeType edge) {
	}

	/**
	 * visit the stack when find the circleEdge. note: now we just supply the
	 * method for dfs. it will not work for other algorithm.
	 * 
	 * @param stack
	 */
	public void visitStackInLoop(final List<EdgeType> stack) {
	};

	/**
	 * visit the vertex of graph
	 * 
	 * @param vertex
	 */
	public void visitVertex(VertexType vertex) {
	}

	public boolean setVisitBit() {
		return true;
	}

	public void visitInsn(AbstractInsnNode insn, Frame frame) {
		switch (insn.getType()) {
		case AbstractInsnNode.INSN:
			visitInsn((InsnNode) insn, frame);
			break;
		case AbstractInsnNode.INT_INSN:
			visitInsn((IntInsnNode) insn, frame);
			break;
		case AbstractInsnNode.VAR_INSN:
			visitInsn((VarInsnNode) insn, frame);
			break;
		case AbstractInsnNode.TYPE_INSN:
			visitInsn((TypeInsnNode) insn, frame);
			break;
		case AbstractInsnNode.FIELD_INSN:
			visitInsn((FieldInsnNode) insn, frame);
			break;
		case AbstractInsnNode.METHOD_INSN:
			visitInsn((MethodInsnNode) insn, frame);
			break;
		case AbstractInsnNode.JUMP_INSN:
			visitInsn((JumpInsnNode) insn, frame);
			break;
		case AbstractInsnNode.LABEL:
			visitInsn((LabelNode) insn, frame);
			break;
		case AbstractInsnNode.LDC_INSN:
			visitInsn((LdcInsnNode) insn, frame);
			break;
		case AbstractInsnNode.IINC_INSN:
			visitInsn((IincInsnNode) insn, frame);
			break;
		case AbstractInsnNode.TABLESWITCH_INSN:
			visitInsn((TableSwitchInsnNode) insn, frame);
			break;
		case AbstractInsnNode.LOOKUPSWITCH_INSN:
			visitInsn((LookupSwitchInsnNode) insn, frame);
			break;
		case AbstractInsnNode.MULTIANEWARRAY_INSN:
			visitInsn((MultiANewArrayInsnNode) insn, frame);
			break;
		case AbstractInsnNode.FRAME:
			visitInsn((FrameNode) insn, frame);
			break;
		case AbstractInsnNode.LINE:
			visitInsn((LineNumberNode) insn, frame);
			break;
		}
	}

	public void visitInsn(InsnNode insn, Frame frame) {
	}

	public void visitInsn(IntInsnNode insn, Frame frame) {
	}

	public void visitInsn(VarInsnNode insn, Frame frame) {
	}

	public void visitInsn(TypeInsnNode insn, Frame frame) {
	}

	public void visitInsn(FieldInsnNode insn, Frame frame) {
	}

	public void visitInsn(MethodInsnNode insn, Frame frame) {
	}

	public void visitInsn(JumpInsnNode insn, Frame frame) {
	}

	public void visitInsn(LabelNode insn, Frame frame) {
	}

	public void visitInsn(LdcInsnNode insn, Frame frame) {
	}

	public void visitInsn(IincInsnNode insn, Frame frame) {
	}

	public void visitInsn(TableSwitchInsnNode insn, Frame frame) {
	}

	public void visitInsn(LookupSwitchInsnNode insn, Frame frame) {
	}

	public void visitInsn(MultiANewArrayInsnNode insn, Frame frame) {
	}

	public void visitInsn(FrameNode insn, Frame frame) {
	}

	public void visitInsn(LineNumberNode insn, Frame frame) {
	}

	/**
	 * after visit the graph
	 */
	public void visitEnd() {
	}

	/**
	 * ���Ʒ������ȣ���Ϊtrue����opcode���з���
	 */
	protected boolean atom = false;

	public boolean getVisitAtom() {
		return atom;
	}

	public void setVisitAtom(boolean atom) {
		this.atom = atom;
	}
}

// end

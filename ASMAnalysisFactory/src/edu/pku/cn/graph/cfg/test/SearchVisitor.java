/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-7-21 ����04:23:51
 * @modifier: Liuxizhiyi
 * @time 2008-7-21 ����04:23:51
 * @reviewer: Liuxizhiyi
 * @time 2008-7-21 ����04:23:51
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.cfg.test;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.analysis.Frame;

import edu.pku.cn.graph.cfg.BasicBlock;
import edu.pku.cn.graph.cfg.CFG;
import edu.pku.cn.graph.cfg.Edge;
import edu.pku.cn.graph.visit.GraphVisitor;

/**
 * 
 * @author Liuxizhiyi
 */
public class SearchVisitor extends GraphVisitor<CFG, Edge, BasicBlock> {

	public void visitVertex(BasicBlock vertex) {
		System.out.print(vertex.getLabel() + " ");
	}

	public void visitInsn(AbstractInsnNode insn, Frame frame) {
		// System.out.println(insn.index);
	}
}

// end

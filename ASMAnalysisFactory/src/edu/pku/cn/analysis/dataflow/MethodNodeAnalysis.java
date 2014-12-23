/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author zhouzhiyi
 * @time 2009-12-25 下午02:48:50
 * @modifier: root
 * @time 2009-12-25 下午02:48:50
 * @reviewer: root
 * @time 2009-12-25 下午02:48:50
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.analysis.dataflow;

import org.objectweb.asm.tree.AbstractInsnNode;

import edu.pku.cn.classfile.MethodNodeSumary;
import edu.pku.cn.graph.cfg.BasicBlock;
import edu.pku.cn.graph.cfg.CFG;
import edu.pku.cn.graph.visit.BlockOrder;

public class MethodNodeAnalysis extends DataflowAnalysis<MethodNodeSumary, BasicBlock> {

	CFG cfg;

	public MethodNodeAnalysis(CFG cfg) {

	}

	@Override
	protected void copy(MethodNodeSumary source, MethodNodeSumary dest) {
		// TODO Auto-generated method stub
		if (dest == null)
			dest = newInitialFlow();
		dest.copy(source);
	}

	@Override
	protected MethodNodeSumary entryInitialFlow() {
		// TODO Auto-generated method stub
		return new MethodNodeSumary();
	}

	@Override
	protected void flowThroughInsn(MethodNodeSumary in, AbstractInsnNode insn, MethodNodeSumary out) {
		// TODO Auto-generated method stub

	}

	@Override
	protected BlockOrder getBlockOrder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean isForward() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void merge(MethodNodeSumary in1, MethodNodeSumary in2, MethodNodeSumary out) {
		// TODO Auto-generated method stub

	}

	@Override
	protected MethodNodeSumary newInitialFlow() {
		// TODO Auto-generated method stub
		return new MethodNodeSumary();
	}

}

// end

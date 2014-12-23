/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-3-19 ����03:35:19
 * @modifier: Administrator
 * @time 2009-3-19 ����03:35:19
 * @reviewer: Administrator
 * @time 2009-3-19 ����03:35:19
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.detector.analysis;

import java.util.ArrayList;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Frame;

import edu.pku.cn.analysis.LiveVariableDataflowAnalysis;
import edu.pku.cn.analysis.RealValueDataflowAnalysis;
import edu.pku.cn.analysis.factory.LiveVariableDataflowFactory;
import edu.pku.cn.analysis.factory.RealValueDataflowFactory;
import edu.pku.cn.asm.tree.analysis.LoadStoreFact;
import edu.pku.cn.asm.tree.analysis.RealValue;
import edu.pku.cn.detector.Detector;
import edu.pku.cn.detector.MethodDetector;
import edu.pku.cn.util.AnalysisFactoryManager;
import edu.pku.cn.util.LocalVariableUtil;
import edu.pku.cn.util.OpcodeUtil;

import edu.pku.cn.bugreport.BugInstance;

/**
 * Find unused def For example int a = 2; then a is not used at all
 * ����߼�����ִ��xstoreָ��ʱ����local
 * variable�ں���������Ƿ��Ѿ���ʹ�ã����û�б�ʹ�ã���˵���˴�defΪ���õ�(unused)
 * 
 * @author Administrator
 */
public class FindUnusedDef extends MethodDetector {

	private LoadStoreFact fact = null;

	private Frame rvFact = null;

	private LoadStoreFact[] facts = null;

	private Frame[] rvFacts = null;

	private boolean DEBUG = false;

	private InsnList insns = null;

	private RealValueDataflowAnalysis rvAnalysis = null;

	private LiveVariableDataflowAnalysis liveAnalysis = null;

	@Override
	public ArrayList<Detector> getInstances() {
		// TODO Auto-generated method stub
		ArrayList<Detector> detectors = new ArrayList<Detector>();
		detectors.add(new FindUnusedDef());
		return detectors;
	}

	@Override
	public void visitCode() {
		super.visitCode();
		insns = node.instructions;
		if (DEBUG) {
			OpcodeUtil.printInsnList(insns, name);
		}
		try {
			rvAnalysis = (RealValueDataflowAnalysis) AnalysisFactoryManager.lookup(RealValueDataflowFactory.NAME)
					.getAnalysis(node.getCFG());
			rvFacts = rvAnalysis.getFacts();

			liveAnalysis = (LiveVariableDataflowAnalysis) AnalysisFactoryManager.lookup(
					LiveVariableDataflowFactory.NAME).getAnalysis(node.getCFG());
			facts = liveAnalysis.getFacts();
		} catch (AnalyzerException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void visitVarInsn(int opcode, int var) {
		super.visitVarInsn(opcode, var);
		try {
			if (opcode >= Opcodes.ISTORE && opcode <= Opcodes.ASTORE) {
				fact = facts[this.currInsnNumber];
				int allIndex = liveAnalysis.getAllIndex(this.currInsnNumber, var);
				rvFact = rvFacts[this.currInsnNumber];
				RealValue rv = (RealValue) rvFact.getStack(rvFact.getStackSize() - 1);
				if (rv.exactValue == RealValue.EXACT_VALUE && rv.getValue() == null)
					return; // ���ڽ�������ֵΪnull�ĸ�ֵ���������ǽ����迼��

				// �ر�ģ���ƥ�䷽����final���͵ľֲ����������ã���Ϊ���������Զ��ѶԾֲ����������ý����ɶԳ��������á���ˣ�����������£�һ��������

				if (!fact.bitSet.get(allIndex)) {
					BugInstance instance = new BugInstance(BugInstance.format(getBugPattern("UNUSED_DEF")
							.getLongDescription(), new String[] { liveAnalysis.getLocalVariableNode(allIndex).name }),
							this.currentLine);
					reportor.report(node.owner, instance);
				}
			}
		} catch (AnalyzerException e) {
			e.printStackTrace();
		}
	}

	// private int lookForNearestLineNumberAbove(int insnIndex)throws
	// AnalyzerException{
	// int line = -1;
	// for(int i = insnIndex; i >= 0; i --){
	// if(insns.get(i) instanceof LineNumberNode){
	// LineNumberNode lnNode = (LineNumberNode)insns.get(i);
	// line = lnNode.line;
	// break;
	// }
	// }
	// return line;
	// }
}

// end

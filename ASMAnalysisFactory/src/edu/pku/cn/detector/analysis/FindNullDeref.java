/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-4-3 ����01:20:54
 * @modifier: Administrator
 * @time 2009-4-3 ����01:20:54
 * @reviewer: Administrator
 * @time 2009-4-3 ����01:20:54
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.detector.analysis;

import java.util.ArrayList;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Frame;

import edu.pku.cn.analysis.IsNullValueDataflowAnalysis;
import edu.pku.cn.analysis.RealValueDataflowAnalysis;
import edu.pku.cn.analysis.factory.IsNullValueDataflowFactory;
import edu.pku.cn.analysis.factory.RealValueDataflowFactory;
import edu.pku.cn.asm.tree.analysis.IsNullValue;
import edu.pku.cn.asm.tree.analysis.RealValue;
import edu.pku.cn.bugreport.BugInstance;
import edu.pku.cn.bugreport.BugReporter;
import edu.pku.cn.detector.Detector;
import edu.pku.cn.detector.DetectorTestDriver;
import edu.pku.cn.detector.MethodDetector;
import edu.pku.cn.util.AnalysisFactoryManager;
import edu.pku.cn.util.OpcodeUtil;

/**
 * Look for null pointer dereference. for example Object o1 = null;
 * System.out.println(o1.hashCode());
 * 
 * @author Administrator
 */
public class FindNullDeref extends MethodDetector {

	private boolean DEBUG = false;

	private IsNullValue invValue = null;

	private RealValue realValue = null;

	private Frame fact = null;

	private Frame[] facts = null;

	private Frame realFact = null;

	private Frame[] realFacts = null;

	private InsnList insns = null;

	private IsNullValueDataflowAnalysis invAnalysis = null;

	private RealValueDataflowAnalysis rvAnalysis = null;

	// private Map<Integer, List> localVariableMap = null;

	@Override
	public ArrayList<Detector> getInstances() {
		ArrayList<Detector> detectors = new ArrayList<Detector>();
		detectors.add(new FindNullDeref());
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
			RealValueDataflowFactory fac = (RealValueDataflowFactory) AnalysisFactoryManager
					.lookup(RealValueDataflowFactory.NAME);
			rvAnalysis = fac.getAnalysis(node.getCFG());
			IsNullValueDataflowFactory nullfac = (IsNullValueDataflowFactory) AnalysisFactoryManager
					.lookup(IsNullValueDataflowFactory.NAME);
			invAnalysis = nullfac.getAnalysis(node.getCFG());
			facts = invAnalysis.getFacts();
			realFacts = rvAnalysis.getFacts();
			// localVariableMap = rvAnalysis.getLocalVariableMap();
		} catch (AnalyzerException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void visitInsn(int opcode) {
		super.visitInsn(opcode);
		fact = facts[this.currInsnNumber];
		realFact = realFacts[this.currInsnNumber];
		switch (opcode) {
		case Opcodes.MONITORENTER:
			invValue = (IsNullValue) fact.getStack(fact.getStackSize() - 1);
			if (invValue.getValue() == IsNullValue.DEFINITE_NULL) {
				BugInstance instance = null;
				realValue = (RealValue) realFact.getStack(realFact.getStackSize() - 1);
				Object varObject = realValue.getVarObject();
				if (varObject instanceof LocalVariableNode) {
					instance = new BugInstance(BugInstance.format(getBugPattern("NULL_DEREF").getLongDescription(),
							new String[] { ((LocalVariableNode) varObject).name }), this.currentLine);
					reportor.report(node.owner, instance);
				} else {// varObject instanceof FieldNode
					// ����field���������Ƚϸ��ӣ��˴����Զ�field�Ĵ���
					// instance = new
					// BugInstance(BugInstance.format(getBugPattern("NULL_DEREF").getLongDescription(),
					// new String[]{((FieldInsnNode)varObject).name}),
					// this.currentLine);
				}

			}
		}
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc) {
		super.visitMethodInsn(opcode, owner, name, desc);
		fact = facts[this.currInsnNumber];
		realFact = realFacts[this.currInsnNumber];
		switch (opcode) {
		case Opcodes.INVOKEVIRTUAL:
		case Opcodes.INVOKESPECIAL:
		case Opcodes.INVOKEINTERFACE:
			int numOfArgs = Type.getArgumentTypes(desc).length;
			invValue = (IsNullValue) fact.getStack(fact.getStackSize() - 1 - numOfArgs);
			if (invValue.getValue() == IsNullValue.DEFINITE_NULL) {
				BugInstance instance = null;
				realValue = (RealValue) realFact.getStack(realFact.getStackSize() - 1 - numOfArgs);
				Object varObject = realValue.getVarObject();
				if (varObject instanceof LocalVariableNode) {
					instance = new BugInstance(BugInstance.format(getBugPattern("NULL_DEREF").getLongDescription(),
							new String[] { ((LocalVariableNode) varObject).name }), this.currentLine);

					reportor.report(node.owner, instance);
				} else {// varObject instanceof FieldNode
					// instance = new
					// BugInstance(BugInstance.format(getBugPattern("NULL_DEREF").getLongDescription(),
					// new String[]{((FieldInsnNode)varObject).name}),
					// this.currentLine);
				}

			}
		}
	}

	public static void main(String[] args) {
		DetectorTestDriver driver = new DetectorTestDriver() {

			@Override
			public Detector createDetector(BugReporter reporter) {
				// TODO Auto-generated method stub
				Detector detector = new FindNullDeref();
				detector.setBugReporter(reporter);
				detector.initBugPattern("Analysis", "edu.pku.cn.detector.analysis.FindNullDeref");
				return detector;
			}
		};
		driver.execute("TestAnalysisIsNullValue");
	}
}

// end

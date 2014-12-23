/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time Mar 7, 2009 9:23:13 AM
 * @modifier: Administrator
 * @time Mar 7, 2009 9:23:13 AM
 * @reviewer: Administrator
 * @time Mar 7, 2009 9:23:13 AM
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.detector;

import java.util.ArrayList;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Frame;

import edu.pku.cn.analysis.RealValueDataflowAnalysis;
import edu.pku.cn.analysis.factory.RealValueDataflowFactory;
import edu.pku.cn.asm.tree.analysis.RealValue;
import edu.pku.cn.bugreport.BugInstance;
import edu.pku.cn.util.AnalysisFactoryManager;

public class FindFloatMatch extends MethodDetector {
	private Frame frame = null;

	private Frame[] frames = null;

	@Override
	public void visitInsn(int opcode) {
		super.visitInsn(opcode);
		if (opcode == Opcodes.FADD) {
			frame = frames[this.currInsnNumber];
			RealValue value1 = null;
			RealValue value2 = null;
			value1 = (RealValue) frame.getStack(frame.getStackSize() - 1);
			value2 = (RealValue) frame.getStack(frame.getStackSize() - 2);
			double a, b;
			if (value1.getValue() instanceof Float && value2.getValue() instanceof Float) {
				a = Double.valueOf(((Float) value1.getValue()).toString());
				b = Double.valueOf(((Float) value2.getValue()).toString());
				if ((a + b) > 16777216.0d) {
					// System.out.println("--findFloatMatch---" + (a + b));
					BugInstance instance = new BugInstance(getBugPattern("FL_MATH_USING_FLOAT_PRECISION")
							.getShortDescription(), getLineNumber());
					reportor.report(node.owner, instance);
				}
			}
		}

	}

	@Override
	public ArrayList<Detector> getInstances() {
		ArrayList<Detector> detectors = new ArrayList<Detector>();
		detectors.add(new FindFloatMatch());
		return detectors;
	}

	@Override
	public void visitCode() {
		super.visitCode();
		try {
			RealValueDataflowFactory fac = (RealValueDataflowFactory) AnalysisFactoryManager
					.lookup(RealValueDataflowFactory.NAME);
			RealValueDataflowAnalysis analysis = fac.getAnalysis(node.getCFG());
			frames = analysis.getFacts();
		} catch (AnalyzerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

// end

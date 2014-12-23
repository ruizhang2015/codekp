/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-2-18 ����02:55:02
 * @modifier: Administrator
 * @time 2009-2-18 ����02:55:02
 * @reviewer: Administrator
 * @time 2009-2-18 ����02:55:02
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.detector.analysis;

import java.util.ArrayList;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Frame;

import edu.pku.cn.analysis.LockDataflowAnalysis;
import edu.pku.cn.analysis.RealValueDataflowAnalysis;
import edu.pku.cn.analysis.factory.LockDataflowFactory;
import edu.pku.cn.analysis.factory.RealValueDataflowFactory;
import edu.pku.cn.asm.tree.analysis.LockMap;
import edu.pku.cn.asm.tree.analysis.RealValue;
import edu.pku.cn.bugreport.BugInstance;
import edu.pku.cn.detector.Detector;
import edu.pku.cn.detector.MethodDetector;
import edu.pku.cn.util.AnalysisFactoryManager;

/**
 * To find wait() or notify() invocation when the object's lock is not held. for
 * example: class TestFindWaitOrNotifyWithoutLock{ public void testNotifyOnO2(){
 * o2.notify(); } }
 * 
 * @author Administrator M.N.
 */
public class FindWaitOrNotifyWithoutLock extends MethodDetector {

	private Frame frame = null;

	private Frame[] frames = null;

	private LockMap map = null;

	private LockMap[] lockMaps = null;

	@Override
	public ArrayList<Detector> getInstances() {
		// TODO Auto-generated method stub
		ArrayList<Detector> detectors = new ArrayList<Detector>();
		detectors.add(new FindWaitOrNotifyWithoutLock());
		return detectors;
	}

	@Override
	public void visitCode() {
		super.visitCode();
		try {
			RealValueDataflowAnalysis rvAnalysis = (RealValueDataflowAnalysis) AnalysisFactoryManager.lookup(
					RealValueDataflowFactory.NAME).getAnalysis(node.getCFG());
			frames = rvAnalysis.getFacts();
			LockDataflowAnalysis lockAnalysis = (LockDataflowAnalysis) AnalysisFactoryManager.lookup(
					LockDataflowFactory.NAME).getAnalysis(node.getCFG());
			lockMaps = lockAnalysis.getFacts();
		} catch (AnalyzerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc) {
		super.visitMethodInsn(opcode, owner, name, desc);
		boolean isWait = name.equals("wait") && (desc.equals("()V") || desc.equals("(J)V") || desc.equals("(JI)V"));
		boolean isNotify = name.equals("notify") && desc.equals("()V");
		boolean isNotifyAll = name.equals("notifyAll") && desc.equals("()V");
		if (isWait || isNotify || isNotifyAll) {
			int numParms = Type.getArgumentTypes(desc).length;
			frame = frames[currInsnNumber];
			if (frame.getStackSize() - numParms < 0)
				try {
					throw new AnalyzerException("Stack underflow");
				} catch (AnalyzerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			RealValue value = (RealValue) (frame.getStack(frame.getStackSize() - numParms - 1));
			LockMap map = lockMaps[currInsnNumber];
			int lockCount = map.getLockCount(value.getVarObject());
			if (lockCount == 0) {
				BugInstance instance = new BugInstance(BugInstance.format(getBugPattern("METHOD_CALLED_WITHOUT_LOCK")
						.getLongDescription(), new String[] { owner, name + desc }), getLineNumber());
				reportor.report(node.owner, instance);
			}

		}
	}
}

// end

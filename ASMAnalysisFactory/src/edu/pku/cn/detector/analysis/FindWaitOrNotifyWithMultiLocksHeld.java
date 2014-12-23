/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-2-19 ����03:00:43
 * @modifier: Administrator
 * @time 2009-2-19 ����03:00:43
 * @reviewer: Administrator
 * @time 2009-2-19 ����03:00:43
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.detector.analysis;

import java.util.ArrayList;

import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import edu.pku.cn.analysis.LockDataflowAnalysis;
import edu.pku.cn.analysis.RealValueDataflowAnalysis;
import edu.pku.cn.analysis.factory.LockDataflowFactory;
import edu.pku.cn.asm.tree.analysis.LockMap;
import edu.pku.cn.bugreport.BugInstance;
import edu.pku.cn.detector.Detector;
import edu.pku.cn.detector.MethodDetector;
import edu.pku.cn.util.AnalysisFactoryManager;

/**
 * To find wait() or notify() invoked with multiple locks held. For example,
 * public class TestFindWaitOrNotifyWithMultiLocksHeld {
 * 
 * private String ss1 = "A private field";
 * 
 * private Object field2 = new Object();
 * 
 * public synchronized void testcaseWithTwoLocks(){
 * System.out.println("This is a test case trying to form up two locks");
 * synchronized(ss1){ ss1 = "Change the synchronized field"; try {
 * field2.wait(); } catch (InterruptedException e) { // TODO Auto-generated
 * catch block e.printStackTrace(); } } } }
 * 
 * @author Administrator Meng Na
 */
public class FindWaitOrNotifyWithMultiLocksHeld extends MethodDetector {

	private LockMap map = null;

	private LockMap[] lockMaps = null;

	@Override
	public ArrayList<Detector> getInstances() {
		// TODO Auto-generated method stub
		ArrayList<Detector> detectors = new ArrayList<Detector>();
		detectors.add(new FindWaitOrNotifyWithMultiLocksHeld());
		return detectors;
	}

	@Override
	public void visitCode() {
		super.visitCode();
		try {
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
			LockMap map = lockMaps[this.currInsnNumber];
			int lockSum = map.getLockSum();
			if (lockSum > 1) {
				BugInstance instance = new BugInstance(BugInstance
						.format(getBugPattern("METHOD_CALLED_WITH_MULTILOCKS_HELD").getLongDescription(), new String[] {
								owner, name + desc }), getLineNumber());
				reportor.report(node.owner, instance);
			}
		}
	}
}

// end

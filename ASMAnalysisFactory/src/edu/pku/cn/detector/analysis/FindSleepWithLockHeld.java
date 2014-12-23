/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-2-19 ����02:11:59
 * @modifier: Administrator
 * @time 2009-2-19 ����02:11:59
 * @reviewer: Administrator
 * @time 2009-2-19 ����02:11:59
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.detector.analysis;

import java.util.ArrayList;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Frame;

import edu.pku.cn.analysis.LockDataflowAnalysis;
import edu.pku.cn.analysis.RealValueDataflowAnalysis;
import edu.pku.cn.analysis.factory.LockDataflowFactory;
import edu.pku.cn.analysis.factory.RealValueDataflowFactory;
import edu.pku.cn.asm.tree.analysis.LockMap;
import edu.pku.cn.bugreport.BugInstance;
import edu.pku.cn.detector.Detector;
import edu.pku.cn.detector.MethodDetector;
import edu.pku.cn.util.AnalysisFactoryManager;

/**
 * To find sleep() invocation when an object's lock is held. for example, public
 * class TestFindSleepWithLockHeld {
 * 
 * Thread t = null; public synchronized void testcaseT1(){ t = new Thread(new
 * Runnable(){
 * 
 * @Override public void run() { System.out.println("This is a new thread"); }
 *           }); try { t.sleep(20); } catch (InterruptedException e) {
 *           e.printStackTrace(); } } }
 * @author Administrator Meng Na
 */
public class FindSleepWithLockHeld extends MethodDetector {

	private LockMap map = null;

	private LockMap[] lockMaps = null;

	@Override
	public ArrayList<Detector> getInstances() {
		// TODO Auto-generated method stub
		ArrayList<Detector> detectors = new ArrayList<Detector>();
		detectors.add(new FindSleepWithLockHeld());
		return detectors;
	}

	@Override
	public void visitCode() {
		super.visitCode();
		try {
			LockDataflowFactory fac = (LockDataflowFactory) AnalysisFactoryManager.lookup(LockDataflowFactory.NAME);
			LockDataflowAnalysis lockAnalysis = fac.getAnalysis(node.getCFG());
			lockMaps = lockAnalysis.getFacts();
		} catch (AnalyzerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc) {
		super.visitMethodInsn(opcode, owner, name, desc);
		boolean isInvokeStatic = (opcode == Opcodes.INVOKESTATIC);
		boolean isSleep = owner.equals("java/lang/Thread") && name.equals("sleep")
				&& (desc.equals("(J)V") || desc.equals("(JI)V"));
		if (isInvokeStatic && isSleep) {
			LockMap map = lockMaps[this.currInsnNumber];
			int lockSum = map.getLockSum();
			if (lockSum > 0) {// the method sleep() is invoked with lock(s)
				// held.
				BugInstance instance = new BugInstance(BugInstance.format(getBugPattern("METHOD_CALLED_WITH_LOCK_HELD")
						.getLongDescription(), new String[] { owner, name + desc }), getLineNumber());
				reportor.report(node.owner, instance);
			}
		}
	}
}

// end

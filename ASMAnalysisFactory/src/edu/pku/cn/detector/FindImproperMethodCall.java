/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-2-20 ����01:37:38
 * @modifier: Administrator
 * @time 2009-2-20 ����01:37:38
 * @reviewer: Administrator
 * @time 2009-2-20 ����01:37:38
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.detector;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicValue;
import org.objectweb.asm.tree.analysis.Frame;
//import org.objectweb.asm.tree.analysis.Value;
//
//import edu.pku.cn.analysis.RealValueDataflowAnalysis;
//import edu.pku.cn.analysis.factory.RealValueDataflowFactory;
//import edu.pku.cn.asm.tree.analysis.RealValue;
import edu.pku.cn.bugreport.BugInstance;
//import edu.pku.cn.hierarchy.Repository;
//import edu.pku.cn.util.AnalysisFactoryManager;
import edu.pku.cn.util.HASelect;

/**
 * Find improper method invocation. For example, public class
 * TestFindImproperMethodCall { final Lock lock = new ReentrantLock(); final
 * Condition notFull = lock.newCondition(); final Condition notEmpty =
 * lock.newCondition();
 * 
 * final Object[] items = new Object[100]; int putptr, takeptr, count;
 * 
 * public void put(Object x) throws InterruptedException { lock.lock(); try {
 * while (count == items.length) notFull.wait(); //should be "notFull.await()"
 * items[putptr] = x; if (++putptr == items.length) putptr = 0; ++count;
 * notEmpty.notify(); //should be "notEmpty.signal()" } finally { lock.unlock();
 * } } }
 * 
 * @author Administrator Meng Na
 */
public class FindImproperMethodCall extends MethodDetector {

	private static Map<String, ArrayList<BugDesc>> method_bug_map = new HashMap<String, ArrayList<BugDesc>>();

	private Frame frame = null;

	private Frame[] frames = null;

//	private static Repository repository = null;

	private class BugDesc {
		String receiver; // the type of object on which the method is called
		String methodName;

		BugDesc(String receiver, String methodName) {
			this.receiver = receiver;
			this.methodName = methodName;
		}

		public boolean equals(Object other) {
			if (other == null)
				return false;
			if (!(other instanceof BugDesc))
				return false;
			BugDesc bugDesc = (BugDesc) other;
			if (!this.receiver.equals(bugDesc.receiver))
				return false;
			if (!this.methodName.equals(bugDesc.methodName))
				return false;
			return true;
		}

		public int hashCode() {
			return this.receiver.hashCode() * 1000 + this.methodName.hashCode();
		}
	}

	@Override
	public ArrayList<Detector> getInstances() {
		// TODO Auto-generated method stub
		initBugDescs();
//		repository = Repository.getInstance();
		ArrayList<Detector> detectors = new ArrayList<Detector>();
		detectors.add(new FindImproperMethodCall());
		return detectors;
	}

	private void initBugDescs() {
		StringTokenizer st;
		String receiver = "";
		String methodDesc = "";
		BufferedReader in = null;
		ArrayList<BugDesc> bugList = null;
		try {
			in = new BufferedReader(new FileReader(System.getProperty("user.dir")
					+ "/defect_set/defectsOfFindImproperMethodCall"));
			String line = in.readLine();
			while (line != null) {
				st = new StringTokenizer(line, " ");
				receiver = st.nextToken();
				methodDesc = st.nextToken() + st.nextToken();
				bugList = method_bug_map.get(methodDesc);
				if (bugList == null) {
					bugList = new ArrayList<BugDesc>();
				}
				bugList.add(new BugDesc(receiver, methodDesc));
				method_bug_map.put(methodDesc, bugList);
				line = in.readLine();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void visitCode() {
		super.visitCode();
//		try {
//			RealValueDataflowFactory fac = (RealValueDataflowFactory) AnalysisFactoryManager
//					.lookup(RealValueDataflowFactory.NAME);
//			RealValueDataflowAnalysis analysis = fac.getAnalysis(node.getCFG());
			frames = this.node.getCFG().getFrames();//analysis.getFacts();
//		} catch (AnalyzerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc) {
		super.visitMethodInsn(opcode, owner, name, desc);
		if (method_bug_map.containsKey(name + desc)) {
			List<BugDesc> bugList = method_bug_map.get(name + desc);
			int numParms = Type.getArgumentTypes(desc).length;
			frame = frames[this.currInsnNumber];
			if (frame.getStackSize() - numParms < 0) {
				try {
					throw new AnalyzerException("Stack underflow");
				} catch (AnalyzerException e) {
					e.printStackTrace();
				}
			}
			BasicValue value = (BasicValue) frame.getStack(frame.getStackSize() - numParms - 1);
			Type actualReceiver = value.getType();
			for (int j = 0; j < bugList.size(); j++) {
				BugDesc bugDesc = bugList.get(j);
				Type receiver=Type.getObjectType(bugDesc.receiver);
				if(HASelect.isInstanceOf(actualReceiver, receiver)
						|| HASelect.isAssignableFrom(receiver, actualReceiver)){
//				if (repository.instanceOf(actualReceiver, bugDesc.receiver)
//						|| repository.implementOf(actualReceiver, bugDesc.receiver)) { // the
																						// invocation
																						// is
					// matched
					BugInstance instance = new BugInstance(BugInstance.format(getBugPattern("IMPROPER_METHOD_CALL")
							.getLongDescription(), new String[] { bugDesc.methodName, bugDesc.receiver }),
							getLineNumber());
					reportor.report(node.owner, instance);
					break;
				}
			}
		}
	}
}

// end

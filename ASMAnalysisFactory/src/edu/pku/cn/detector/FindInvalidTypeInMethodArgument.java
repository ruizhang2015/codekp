/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-1-7 ����09:54:06
 * @modifier: Administrator
 * @time 2009-1-7 ����09:54:06
 * @reviewer: Administrator
 * @time 2009-1-7 ����09:54:06
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
//
//import edu.pku.cn.analysis.RealValueDataflowAnalysis;
//import edu.pku.cn.analysis.factory.RealValueDataflowFactory;
//import edu.pku.cn.asm.tree.analysis.RealValue;
import edu.pku.cn.bugreport.BugInstance;
//import edu.pku.cn.hierarchy.Repository;
//import edu.pku.cn.util.AnalysisFactoryManager;
import edu.pku.cn.util.HASelect;

/**
 * Find invalid type of method argument. For example, if a method requires that
 * its parameter type should be Runnable. However,the actual parameter type is
 * Thread, this should be a defect.
 * 
 * @author Administrator
 */
public class FindInvalidTypeInMethodArgument extends MethodDetector {

	private static Map<String, ArrayList<BugDesc>> owner_bug_map = new HashMap<String, ArrayList<BugDesc>>();

	private Frame frame = null;

	private Frame[] frames = null;

	private class BugDesc {
		String owner;
		String methodName;
		int orderOfArgu;// if the order may be any, use -1 to sign
		String wrongTypeSig;

		BugDesc(String owner, String methodName, int orderOfArgu, String wrongTypeSig) {
			this.owner = owner;
			this.methodName = methodName;
			this.orderOfArgu = orderOfArgu;
			this.wrongTypeSig = wrongTypeSig;
		}

		public boolean equals(Object other) {
			if (other == null)
				return false;
			if (!(other instanceof BugDesc))
				return false;
			BugDesc bugDesc = (BugDesc) other;
			if (!this.owner.equals(bugDesc.owner))
				return false;
			if (!this.methodName.equals(bugDesc.methodName))
				return false;
			if (this.orderOfArgu != orderOfArgu)
				return false;
			if (!this.wrongTypeSig.equals(bugDesc.wrongTypeSig))
				return false;
			return true;
		}

		public int hashCode() {
			return this.owner.hashCode() * 1000 + this.methodName.hashCode() * 100 + this.orderOfArgu * 10
					+ this.wrongTypeSig.hashCode();
		}
	}

	@Override
	public ArrayList<Detector> getInstances() {
		// TODO Auto-generated method stub
		initBugDescs();
		ArrayList<Detector> detectors = new ArrayList<Detector>();
		detectors.add(new FindInvalidTypeInMethodArgument());
		return detectors;
	}

	private void initBugDescs() {
		StringTokenizer st;
		String owner = "";
		String methodDesc;
		int orderOfArgu;
		String wrongTypeSig;
		BufferedReader in = null;
		ArrayList<BugDesc> bugList = null;
		try {
			in = new BufferedReader(new FileReader(System.getProperty("user.dir")
					+ "/defect_set/defectsOfFindInvalidTypeInMethodArgument"));
			String line = in.readLine();
			while (line != null) {
				st = new StringTokenizer(line, " ");
				owner = st.nextToken();
				methodDesc = st.nextToken() + st.nextToken();
				orderOfArgu = Integer.parseInt(st.nextToken());
				wrongTypeSig = st.nextToken();
				bugList = owner_bug_map.get(owner);
				if (bugList == null) {
					bugList = new ArrayList<BugDesc>();
				}
				bugList.add(new BugDesc(owner, methodDesc, orderOfArgu, wrongTypeSig));
				owner_bug_map.put(owner, bugList);
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
//			frames = analysis.getFacts();
//		} catch (AnalyzerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		frames = this.node.getCFG().getFrames();
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc) {
		super.visitMethodInsn(opcode, owner, name, desc);
		if (owner_bug_map.containsKey(owner)) {
			List<BugDesc> bugList = owner_bug_map.get(owner);
			for (int j = 0; j < bugList.size(); j++) {
				BugDesc bugDesc = bugList.get(j);
				if (bugDesc.methodName.equals(name + desc)) {
					int numParms = Type.getArgumentTypes(desc).length;
					frame = frames[this.currInsnNumber];
					if (frame != null && frame.getStackSize() > numParms) {
						int orderOfArgu = bugDesc.orderOfArgu;
//						RealValue value = null;
						BasicValue value = (BasicValue) frame.getStack(frame.getStackSize() - numParms + orderOfArgu);
//						Repository repository = Repository.getInstance();
						Type temp = value.getType();
//						if (temp.startsWith("L")) {
//							temp = temp.substring(1, temp.lastIndexOf(";"));
//						}
						Type temp2 =Type.getType(bugDesc.wrongTypeSig);
//						if (temp2.startsWith("L")) {
//							temp2 = temp2.substring(1, temp2.lastIndexOf(";"));
//						}
						if(HASelect.isInstanceOf(temp, temp2)||HASelect.isAssignableFrom(temp2, temp)){
//						if (repository.implementOf(temp, temp2) || repository.instanceOf(temp, temp2)) {
							Type[] args = Type.getArgumentTypes(desc);
							BugInstance instance = new BugInstance(BugInstance.format(getBugPattern(
									"INVALID_ARGUMENT_TYPE").getLongDescription(), new String[] { bugDesc.owner,
									bugDesc.methodName,
									// Integer.valueOf(bugDesc.orderOfArgu).toString(),
									bugDesc.wrongTypeSig, args[orderOfArgu].toString(),
									Integer.valueOf(getLineNumber()).toString() }), getLineNumber());
							reportor.report(node.owner, instance);
							break;
						}
					}
				}
			}
		}
	}
}

// end

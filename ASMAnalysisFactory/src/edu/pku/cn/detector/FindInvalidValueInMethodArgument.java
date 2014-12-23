/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2008-12-24 ����10:52:59
 * @modifier: Administrator
 * @time 2008-12-24 ����10:52:59
 * @reviewer: Administrator
 * @time 2008-12-24 ����10:52:59
 * (C) Copyright PKU Software Lab. 2008
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

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Frame;
import edu.pku.cn.analysis.RealValueDataflowAnalysis;
import edu.pku.cn.analysis.factory.RealValueDataflowFactory;
import edu.pku.cn.asm.tree.analysis.AnalysisException;
import edu.pku.cn.asm.tree.analysis.RealValue;
import edu.pku.cn.bugreport.BugInstance;
import edu.pku.cn.graph.cfg.CFG;
import edu.pku.cn.util.AnalysisFactoryManager;

/**
 * To find invocation of some methods with certain invalid parameter value(s).
 * for example: String ss = "This is an example for FindInvalidValueInMethod";
 * String ss2 = ss.substring(0); In the example, 0 is invalid since it is almost
 * impossible to get a substring which equals to itself.
 * 
 * @author Administrator M.N.
 */
public class FindInvalidValueInMethodArgument extends MethodDetector {

	private class BugDesc<T> {
		String owner;
		String methodName;
		int orderOfArgu;
		T invalidValue;

		BugDesc(String owner, String methodName, int orderOfArgu, T invalidValue) {
			this.owner = owner;
			this.methodName = methodName;
			this.orderOfArgu = orderOfArgu;
			this.invalidValue = invalidValue;
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
			if (this.orderOfArgu != bugDesc.orderOfArgu)
				return false;
			if (!this.invalidValue.equals(bugDesc.invalidValue))
				return false;
			return true;
		}

		public int hashCode() {
			return this.owner.hashCode() * 1000 + this.methodName.hashCode() * 100 + this.orderOfArgu * 10
					+ this.invalidValue.hashCode();
		}
	}

	private static Map<String, ArrayList<BugDesc>> owner_bug_map = new HashMap<String, ArrayList<BugDesc>>();

	private Frame frame = null;

	private Frame[] frames = null;

	private void initBugDescs() {
		StringTokenizer st;
		String owner = "";
		String methodDesc;
		int orderOfDesc, invalidValue;
		BufferedReader in = null;
		ArrayList<BugDesc> bugList = null;
		try {
			in = new BufferedReader(new FileReader(System.getProperty("user.dir")
					+ "/defect_set/defectsOfFindInvalidValueInMethodArgument"));
			bugList = new ArrayList<BugDesc>();
			String line = in.readLine();
			String originalOwner = line.substring(0, line.indexOf(" "));
			while (line != null) {
				st = new StringTokenizer(line, " ");
				owner = st.nextToken();
				methodDesc = st.nextToken() + st.nextToken();
				orderOfDesc = Integer.parseInt(st.nextToken());
				invalidValue = Integer.parseInt(st.nextToken());
				if (!owner.equals(originalOwner)) {
					owner_bug_map.put(originalOwner, bugList);
					bugList = new ArrayList<BugDesc>();
					originalOwner = owner;
				}
				bugList.add(new BugDesc<Integer>(owner, methodDesc, orderOfDesc, invalidValue));
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
				owner_bug_map.put(owner, bugList);
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public ArrayList<Detector> getInstances() {
		// TODO Auto-generated method stub
		initBugDescs();
		ArrayList<Detector> detectors = new ArrayList<Detector>();
		detectors.add(new FindInvalidValueInMethodArgument());
		return detectors;
	}

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
						// RealValue value = (RealValue) frame.getStack(numParms
						// - bugDesc.orderOfArgu);
						RealValue value = null;
						value = (RealValue) frame.getStack(frame.getStackSize() - numParms + bugDesc.orderOfArgu);
						try {
							if (value.exactValue == RealValue.EXACT_VALUE
									&& value.getValue().equals(bugDesc.invalidValue)) {
								BugInstance instance = new BugInstance(BugInstance.format(getBugPattern(
										"INVALID_ARGUMENT_VALUE").getLongDescription(), new String[] {
										bugDesc.methodName, bugDesc.invalidValue.toString(),
										Integer.valueOf(getLineNumber()).toString() }), getLineNumber());
								// System.out.println(getLineNumber());
								reportor.report(node.owner, instance);

								break;
							}
						} catch (Exception e) {
							System.out.println(e);
							break;
						}
					}
				}
			}
		}
	}
}

// end

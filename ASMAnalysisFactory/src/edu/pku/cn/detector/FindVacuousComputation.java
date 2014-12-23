/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-1-8 ����04:00:38
 * @modifier: Administrator
 * @time 2009-1-8 ����04:00:38
 * @reviewer: Administrator
 * @time 2009-1-8 ����04:00:38
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

import org.objectweb.asm.Label;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Frame;

import edu.pku.cn.analysis.RealValueDataflowAnalysis;
import edu.pku.cn.analysis.factory.RealValueDataflowFactory;
import edu.pku.cn.asm.tree.analysis.RealValue;
import edu.pku.cn.bugreport.BugInstance;
import edu.pku.cn.util.AnalysisFactoryManager;
import edu.pku.cn.util.OpcodeUtil;

import automachine.AutomaUtil;

public class FindVacuousComputation extends MethodDetector {

	private static Map<Integer, ArrayList<BugDesc>> opcode_bugs_map = new HashMap<Integer, ArrayList<BugDesc>>();

	private Frame frame = null;

	private Frame[] frames = null;

	private boolean DEBUG = false;

	private class BugDesc {
		Integer opcode;
		Integer orderOfComparator;
		Integer value;

		BugDesc(Integer opcode, Integer orderOfComparator, Integer value) {
			this.opcode = opcode;
			this.orderOfComparator = orderOfComparator;
			this.value = value;
		}

		public boolean equals(Object other) {
			if (other == null)
				return false;
			if (!(other instanceof BugDesc))
				return false;
			BugDesc bugDesc = (BugDesc) other;
			if (!this.opcode.equals(bugDesc.opcode))
				return false;
			if (!this.orderOfComparator.equals(bugDesc.orderOfComparator))
				return false;
			if (!this.value.equals(bugDesc.value))
				return false;
			return true;
		}

		public int hashCode() {
			return this.opcode.hashCode() * 100 + this.orderOfComparator * 10 + this.value.hashCode();
		}
	}

	private void initBugDescs() {
		StringTokenizer st;
		Integer opcode = null;
		Integer orderOfComparator = null; // order of the known comparator
		Integer value = null;
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(System.getProperty("user.dir")
					+ "/defect_set/defectsOfFindVacuousComputation"));
			String line = in.readLine();
			ArrayList<BugDesc> bugList = null;
			while (line != null) {
				st = new StringTokenizer(line, " ");
				opcode = Integer.valueOf(AutomaUtil.convertOpcodeToInt(st.nextToken()));
				orderOfComparator = Integer.valueOf(st.nextToken());
				value = Integer.valueOf(st.nextToken());
				bugList = opcode_bugs_map.get(opcode);
				if (bugList == null) {
					bugList = new ArrayList<BugDesc>();
				}
				bugList.add(new BugDesc(opcode, orderOfComparator, value));
				opcode_bugs_map.put(opcode, bugList);
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
	public ArrayList<Detector> getInstances() {
		// TODO Auto-generated method stub
		initBugDescs();
		ArrayList<Detector> detectors = new ArrayList<Detector>();
		detectors.add(new FindVacuousComputation());
		return detectors;
	}

	public void visitCode() {
		super.visitCode();
		try {
			if (DEBUG)
				OpcodeUtil.printInsnList(node.instructions, node.name);
			RealValueDataflowFactory fac = (RealValueDataflowFactory) AnalysisFactoryManager
					.lookup(RealValueDataflowFactory.NAME);
			RealValueDataflowAnalysis analysis = fac.getAnalysis(node.getCFG());
			frames = analysis.getFacts();
		} catch (AnalyzerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void visitInsn(int opcode) {
		super.visitInsn(opcode);
		if (opcode_bugs_map.containsKey(opcode)) {
			List<BugDesc> bugList = opcode_bugs_map.get(opcode);
			frame = frames[this.currInsnNumber];
			if (frame != null) {
				BugDesc bugDesc = null;
				RealValue value = null;
				for (int j = 0; j < bugList.size(); j++) {
					bugDesc = bugList.get(j);
					value = (RealValue) frame.getStack(frame.getStackSize() - 1 - bugDesc.orderOfComparator);
					if (value.exactValue == RealValue.EXACT_VALUE && value.getValue().equals(bugDesc.value)) {
						BugInstance instance = new BugInstance(BugInstance.format(getBugPattern("VACUOUS_COMPUTATION")
								.getLongDescription(),
								new String[] { AutomaUtil.convertOpcodeToString(bugDesc.opcode),
										Integer.valueOf(bugDesc.value).toString(),
										Integer.valueOf(getLineNumber()).toString() }), getLineNumber());
						reportor.report(node.owner, instance);
					}
				}
			}
		}

	}

	public void visitJumpInsn(int opcode, Label label) {
		super.visitJumpInsn(opcode, label);
		if (opcode_bugs_map.containsKey(opcode)) {
			List<BugDesc> bugList = opcode_bugs_map.get(opcode);
			frame = frames[this.currInsnNumber];
			if (frame != null) {
				BugDesc bugDesc = null;
				RealValue value = null;
				for (int j = 0; j < bugList.size(); j++) {
					bugDesc = bugList.get(j);
					value = (RealValue) frame.getStack(frame.getStackSize() - 1 - bugDesc.orderOfComparator);
					if (value.exactValue == RealValue.EXACT_VALUE && value.getValue().equals(bugDesc.value)) {
						BugInstance instance = new BugInstance(BugInstance.format(getBugPattern("VACUOUS_COMPUTATION")
								.getLongDescription(),
								new String[] { AutomaUtil.convertOpcodeToString(bugDesc.opcode),
										Integer.valueOf(bugDesc.value).toString(),
										Integer.valueOf(getLineNumber()).toString() }), getLineNumber());
						reportor.report(node.owner, instance);
					}
				}
			}
		}

	}

}

// end

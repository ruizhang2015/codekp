/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-5-6 ����09:35:40
 * @modifier: Administrator
 * @time 2009-5-6 ����09:35:40
 * @reviewer: Administrator
 * @time 2009-5-6 ����09:35:40
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package autoAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import automachine.AutoMachine;
import automachine.AutoMachineException;
import automachine.AutomaUtil;
import automachine.Edge;
import automachine.State;
import automachine.VisitMethodInsnEdge;

import edu.pku.cn.bugreport.BugInstance;
import edu.pku.cn.detector.ClassDetector;
import edu.pku.cn.detector.Detector;
import edu.pku.cn.detector.MethodDetector;

public class InterMethodsInvokeDetector extends MethodDetector {

	static boolean inited = false;
	boolean DEBUG = false;
	static ArrayList<AutoMachine> ams = AutomaUtil.generateAllAutoMachineFromDatabase();

	public static void init() {
		ArrayList<AutoMachine> ams = AutomaUtil.generateAllAutoMachineFromDatabase();

		for (AutoMachine am : ams) {
			for (State s : am.getStates()) {
				for (Edge e : s.getOutEdges()) {
					if (e instanceof VisitMethodInsnEdge) {
						VisitMethodInsnEdge ee = (VisitMethodInsnEdge) e;
						MethodSummaryUtil.concernClass.add(ee.getOwner());
						// System.out.println("concern:"+ ee.getOwner());
					}
				}
			}
		}
	}

	@Override
	public void visitCode() {
		if (!inited) {
			inited = true;
			init();
			for (String c : MethodSummaryUtil.concernClass) {
				System.out.println("concern:" + c);
			}
		}
		// //��visitcode�д��?���û��code��ô�϶����Ժ��Ըķ���
		// MethodNode mn = context.getMethod(this.name);
		// if (DEBUG) {
		// System.out.println(mn.name + " " + mn.desc);
		//					
		// }
		// try {
		// cfg = context.getCFGFactory().analyze(mn);

		String key = node.owner + "." + node.name + node.desc;
		// System.out.println("inKey="+key);

		MethodSummary summary = MethodSummaryUtil.getSummary(node.getCFG());

		if (summary == null)
			return;
		if (!summary.propagated) {
			MethodSummaryUtil.propagateSummary(summary);
			// ��鲢����
		}

		if (DEBUG) {
			System.out.println(summary.toString());
		}

		HashSet<HeapObject> usedByConstructor = new HashSet<HeapObject>();
		for (ExecuteMethod em : summary.allExecuteMethods) {
			if (em.methodName.indexOf("<init>") >= 0) {
				for (int i = 1; i < em.parameters.size(); i++) {
					usedByConstructor.add(em.parameters.get(i));
				}
			}

		}

		for (Map.Entry<HeapObject, ArrayList<ExecuteMethod>> en : summary.ho2invokes.entrySet()) {
			HeapObject ho = en.getKey();
			ArrayList<ExecuteMethod> ems = en.getValue();
			if (ho.desc == HeapObject.DescReturn || ho.desc.startsWith(HeapObject.DescParam))
				continue;

			if (summary.returnedObjects.contains(ho))
				continue;

			if (usedByConstructor.contains(ho))
				continue;

			for (AutoMachine am : ams) {
				// ��ʼ��
				am.initial();

				for (ExecuteMethod em : ems) {
					if (em.parameters.get(0).hashCode() != ho.hashCode())
						continue;
					try {
						am.goOneStep(new VisitMethodInsnEdge(null, null, -2, em.methodOwner, em.methodName,
								em.methodDesc), em.executePosition.sourceLine);
					} catch (AutoMachineException e) {
						// TODO Auto-generated catch block
						reportor.report(printError(ho, ems, am), new BugInstance(e.getMessage() + " by am:"
								+ am.automachineId, ho.createLocation.sourceLine));

					}
				}
				// �����
				if ((am.endMeanViolateDefect && am.currentState.isEndState())
						|| (!am.endMeanViolateDefect && !am.currentState.isEndState() && !am.currentState
								.isOriginState())) {
					// System.out.println(am.currentState);

					// reportor.report(context.getName(), new
					// BugInstance(am.getErrorMessage()+" by am:"+am.automachineId,ho.createLocation.sourceLine));
					reportor.report(printError(ho, ems, am), new BugInstance(am.getErrorMessage() + " by am:"
							+ am.automachineId, ho.createLocation.sourceLine));

					// reportAmBug(e.getMessage()+" in method "+this.getName()+"\n",);
					// System.out.println(e.getMessage()+" in method "+this.getName()+""+am.getCurrentState().getLastLine());
				}
			}

		}

		//			
		// } catch (AnalyzerException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// return ;
		// }

	}

	private String printError(HeapObject ho, ArrayList<ExecuteMethod> ems, AutoMachine am) {

		if (DEBUG) {
			System.out.print("\n\n**");
			System.out.println(ho);
		}
		String sourceFile = ho.createLocation.className;
		if (sourceFile.startsWith("L") && sourceFile.endsWith(";"))
			sourceFile = sourceFile.substring(1, sourceFile.length() - 2);
		if (sourceFile.indexOf('$') > 0)
			sourceFile = sourceFile.substring(0, sourceFile.indexOf('$'));

		if (DEBUG) {
			System.out.println(am.getErrorMessage() + " by am:" + am.automachineId + " source:(" + sourceFile
					+ ".java:" + ho.createLocation.sourceLine + ")");
			for (ExecuteMethod emm : ems)
				System.out.println(emm);
		}

		return sourceFile;
	}

	@Override
	public ArrayList<Detector> getInstances() {
		// TODO Auto-generated method stub
		System.out.println("asdfasdfasd");
		ArrayList<Detector> detectors = new ArrayList<Detector>();
		detectors.add(new InterMethodsInvokeDetector());
		return detectors;
	}

}

// end

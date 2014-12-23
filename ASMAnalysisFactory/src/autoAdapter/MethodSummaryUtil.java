/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-5-3 ����02:34:42
 * @modifier: Administrator
 * @time 2009-5-3 ����02:34:42
 * @reviewer: Administrator
 * @time 2009-5-3 ����02:34:42
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package autoAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Frame;

import automachine.AutoMachine;
import automachine.AutomaUtil;
import automachine.Edge;
import automachine.State;
import automachine.VisitMethodInsnEdge;

import edu.pku.cn.analysis.ObjectInvokeMethodAnalysis;
import edu.pku.cn.analysis.SimpleDataflow;
import edu.pku.cn.analysis.factory.ObjectMethodInvokeDataFlowFactory;
import edu.pku.cn.asm.tree.analysis.HeapObjectInterpreter;
import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.graph.cfg.CFG;
import edu.pku.cn.hierarchy.Repository;

public class MethodSummaryUtil {
	public static final HashMap<String, MethodSummary> methodSummarys = new HashMap<String, MethodSummary>();
	public static HashSet<String> concernClass = new HashSet<String>();
	public static boolean DEBUG = false;

	static boolean inited = false;

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

	static {
		concernClass.add("java.io.File");
		concernClass.add("java.io.FileInputStream");

		concernClass.add("java.io.InputStream");
		concernClass.add("java.io.ObjectOutputStream");
		concernClass.add("java.io.PipedOutputStream");
		concernClass.add("java.sql.Connection");
		concernClass.add("java.io.PipedInputStream");
		concernClass.add("java.io.File");
		concernClass.add("java.nio.channels.Selector");
		concernClass.add("java.io.FileInputStream");
		concernClass.add("java.io.FileOutputStream");
		concernClass.add("java.io.ObjectInputStream");
		concernClass.add("java.io.BufferedOutputstream");

	}

	public static void addMethodInvoke(MethodSummary summary, HeapObject ho, ExecuteMethod em) {

	}

	public static boolean isClassConcern(String classCompleteName) {
		if (classCompleteName == null)
			return false;
		classCompleteName = classCompleteName.replace('/', '.');
		if (classCompleteName.startsWith("L"))
			classCompleteName = classCompleteName.substring(1);
		if (classCompleteName.endsWith(";"))
			classCompleteName = classCompleteName.substring(0, classCompleteName.length() - 1);

		// System.out.println("class="+classcn);
		return concernClass.contains(classCompleteName);
	}

	public static boolean isMethodConcern(MethodNode mn) {
		return isMethodConcern(mn.desc);
	}

	public static boolean isMethodConcern(String desc) {
		// TODO Auto-generated method stub

		if (desc == null)
			return false;

		// case1:desc = className.methodName(ArgsType)ReturnType

		if (desc.indexOf('.') >= 0) {
			if (isClassConcern(desc.substring(0, desc.lastIndexOf('.'))))
				return true;
		}

		// case2: xxx(ArgsType)ReturnType
		// descӦ�ô�(��ʼ
		desc = desc.substring(desc.indexOf('('));
		// System.out.println("concern judge desc="+desc);
		Type[] argTypes = Type.getArgumentTypes(desc);
		for (Type t : argTypes) {
			if (t == null)
				continue;
			// System.out.println("arg:"+t.toString());
			if (isClassConcern(t.toString()))
				return true;
		}
		Type returnType = Type.getReturnType(desc);
		if (isClassConcern(returnType.toString()))
			return true;
		// System.out.println("ret:"+returnType.toString());
		return false;
	}

	public static void addSummary(ClassNode cc) {
		if (cc == null)
			return;
		// System.out.println("add cc="+cc.name);

		for (int i = 0; i < cc.methods.size(); i++) {
			MethodNode method = cc.methods.get(i);
			// if(!MethodSummaryUtil.isMethodConcern(method))
			// continue;
			CFG cfg;
			try {

				String key = method.owner + "." + method.name + method.desc;
				if (DEBUG) {
					System.out.println("key=" + key);
				}
				if (methodSummarys.containsKey(key))
					continue;

				ObjectMethodInvokeDataFlowFactory factory = cc.getObjectMethodInvokeDataFlowFactory();

				HeapObjectInterpreter interpreter = new HeapObjectInterpreter(cc.getFieldList());
				interpreter.setInsns(cfg.getMethod().instructions);

				ObjectInvokeMethodAnalysis analysis = null;
				SimpleDataflow<Frame, ObjectInvokeMethodAnalysis> dataflow;
				try {
					analysis = new ObjectInvokeMethodAnalysis(cfg, interpreter);
				} catch (Exception e) {
					// TODO Auto-generated catch block

				}

				if (analysis == null)
					return;

				dataflow = new SimpleDataflow<Frame, ObjectInvokeMethodAnalysis>(analysis.getCfg(), analysis);

				dataflow.execute();
				MethodSummary sum = extractSummary(analysis);

				methodSummarys.put(key, sum);

				// ��Ӧ�÷���������
				// if(! sum.propagated)
				// propagateSummary(sum);

				if (DEBUG)
					printSummary(sum);

			} catch (AnalyzerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public static MethodSummary extractSummary(ObjectInvokeMethodAnalysis analysis) {
		MethodNode m = analysis.getCfg().getMethod();
		if (m == null)
			return null;
		MethodSummary summary = new MethodSummary(m);
		summary.allExecuteMethods = analysis.allExecuteMethods;
		CFG cfg = analysis.getCfg();
		String key = cfg.getOwner() + "." + cfg.getMethod().name + cfg.getMethod().desc;
		summary.key = key;
		summary.parameters = analysis.parameters;
		summary.returnedObjects = analysis.returnObject;
		HeapObjectInterpreter interpreter = analysis.interpreter;

		if ((m.access & Opcodes.ACC_STATIC) == 0) {

			summary.thisObject = summary.parameters.get(0);
			summary.ho2invokes.put(summary.thisObject, new ArrayList<ExecuteMethod>());

		} else {

			summary.thisObject = null;
		}

		for (HeapObject o : analysis.heapObjects) {
			if (o.desc.startsWith(HeapObject.DescNew)) {
				summary.newObjects.add(o);
				summary.innerObjects.add(o);
			}

		}

		for (ExecuteMethod em : analysis.allExecuteMethods) {
			for (int i = 0; i < em.parameters.size(); i++) {
				HeapObject p = em.parameters.get(i);
				if (p != null && !p.desc.startsWith(HeapObject.DescBuiltin) && p != HeapObject.BasicDontcare) {
					if (!summary.ho2invokes.containsKey(p))
						summary.ho2invokes.put(p, new ArrayList<ExecuteMethod>());
					summary.ho2invokes.get(p).add(em);
					summary.escapeObjects.add(p);
					summary.innerObjects.remove(p);
				}

			}

		}

		return summary;
	}

	public static void printSummary(MethodSummary ms) {
		System.out.println("**************parameters**************");
		for (HeapObject h : ms.parameters) {
			if (!isClassConcern(h.classType))
				continue;
			System.out.println(h + "\n");
			printHOSummary(ms, h);
			System.out.println("-----------ob sep-------------");
		}

		System.out.println("*****************escape objects*******");
		for (HeapObject h : ms.escapeObjects) {
			if (!isClassConcern(h.classType))
				continue;
			System.out.println(h + "\n");
			printHOSummary(ms, h);
			System.out.println("-----------ob sep-------------");
		}
		System.out.println("*****************inner objects*******");
		for (HeapObject h : ms.innerObjects) {
			if (!isClassConcern(h.classType))
				continue;
			System.out.println(h + "\n");
			printHOSummary(ms, h);
			System.out.println("-----------ob sep-------------");
		}

	}

	static HashSet<HeapObject> printed = new HashSet<HeapObject>();

	public static void printHOSummary(MethodSummary ms, HeapObject ho) {
		if (ho == null)
			return;

		if (printed.contains(ho))
			return;
		printed.add(ho);

		ArrayList<ExecuteMethod> ems = ms.ho2invokes.get(ho);
		if (ems != null)
			for (ExecuteMethod em : ems) {
				System.out.println(" on " + ho + ":");
				System.out.println(em);
			}
		for (HeapObject hf : ho.referenceFields.values()) {

			printHOSummary(ms, hf);
		}
	}

	public static void findHOExecuteMethod(MethodSummary summary, ObjectInvokeMethodAnalysis analysis, HeapObject ho,
			ArrayList<ExecuteMethod> ms) {
		if (ho != null && !ho.desc.startsWith(HeapObject.DescBuiltin) && ho != HeapObject.BasicDontcare) {
			if (!summary.ho2invokes.containsKey(ho))
				summary.ho2invokes.put(ho, new ArrayList<ExecuteMethod>());

			int hc = ho.hashCode();

		}
	}

	public static MethodSummary getSummary(String key) {
		MethodSummary ms = methodSummarys.get(key);
		if (ms != null)
			return ms;
		ClassContext cc = Repository.getInstance().findClassContext(key.substring(0, key.lastIndexOf('.')));
		addSummary(cc);

		ms = methodSummarys.get(key);
		return ms;

	}

	public static MethodSummary getSummary(CFG cfg) {
		// TODO Auto-generated method stub
		String key = cfg.getOwner() + "." + cfg.getMethod().name + cfg.getMethod().desc;
		MethodSummary ms = methodSummarys.get(key);
		if (ms != null)
			return ms;
		try {
			ClassContext cc = Repository.getInstance().findClassContext(cfg.getOwner());
			addSummary(cc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		ms = methodSummarys.get(key);
		return ms;

	}

	public static void propagateSummary(MethodSummary ms) {
		propagateSummary(new ArrayList<Integer>(), ms);
	}

	public static void propagateSummary(ArrayList<Integer> q, MethodSummary ms) {
		if (ms == null)
			return;

		// ���>20
		if (q.size() >= 10)
			return;

		// ��״
		if (q.contains(ms.hashCode()))
			return;

		q.add(ms.hashCode());

		for (ExecuteMethod em : ms.allExecuteMethods) {
			MethodSummary ems = getSummary(em.methodOwner + "." + em.methodName + em.methodDesc);

			if (ems != null) {
				if (q.contains(ems.hashCode()))
					continue;

				if (!ems.propagated)
					propagateSummary(q, ems);
				for (int i = 0; i < em.parameters.size(); i++) {
					HeapObject p = em.parameters.get(i);
					if (isClassConcern(p.classType)) {
						if (ems.ho2invokes.get(ems.parameters.get(i)) != null)
							ms.ho2invokes.get(p).addAll(ems.ho2invokes.get(ems.parameters.get(i)));
					}
				}
			}
		}
		// printSummary(ms);
		q.remove(q.size() - 1);
		ms.propagated = true;
	}

}

// end

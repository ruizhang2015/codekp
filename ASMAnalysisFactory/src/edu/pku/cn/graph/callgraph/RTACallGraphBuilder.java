/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author liuxi
 * @time 2010-3-23
 * @modifier: liuxi
 * @time 2010-3-23
 * @reviewer: liuxi
 * @time 2010-3-23
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.callgraph;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;

import edu.pku.cn.Project;
import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.classfile.ClassNodeLoader;
import edu.pku.cn.classfile.LocalVariableNode;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.detector.Detector;
import edu.pku.cn.jir.AbstractJIRVisitor;
import edu.pku.cn.jir.AssignStmt;
import edu.pku.cn.jir.EnterMonitorStmt;
import edu.pku.cn.jir.ExitMonitorStmt;
import edu.pku.cn.jir.GotoStmt;
import edu.pku.cn.jir.IfStmt;
import edu.pku.cn.jir.InvokeStmt;
import edu.pku.cn.jir.JIRVisitor;
import edu.pku.cn.jir.LabelStmt;
import edu.pku.cn.jir.LineStmt;
import edu.pku.cn.jir.LookupSwitchStmt;
import edu.pku.cn.jir.RetStmt;
import edu.pku.cn.jir.ReturnStmt;
import edu.pku.cn.jir.TableSwitchStmt;
import edu.pku.cn.jir.ThrowStmt;

/**
 * @author liuxi
 */
public class RTACallGraphBuilder {
	CallGraph cha, call;
	Set<Type> newType = new HashSet<Type>();
	HashMap<MethodNode, CallGraphEdge> callee = new HashMap<MethodNode, CallGraphEdge>();

	class Pair {
		public MethodNode caller, callee;

		public Pair(MethodNode caller, MethodNode callee) {
			this.caller = caller;
			this.callee = callee;
		}

		public int hashCode() {
			return caller.hashCode() + callee.hashCode();
		}

		public String toString() {
			return caller.name + "->" + callee.name;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Pair other = (Pair) obj;
			if (callee == null) {
				if (other.callee != null)
					return false;
			} else if (!callee.equals(other.callee))
				return false;
			if (caller == null) {
				if (other.caller != null)
					return false;
			} else if (!caller.equals(other.caller))
				return false;
			return true;

		}
	}

	class EdgeDealed {
		Set<Pair> edgeDealed = new HashSet<Pair>();
		Pair temp = new Pair(null, null);

		public boolean add(MethodNode caller, MethodNode callee) {
			return edgeDealed.add(new Pair(caller, callee));
		}

		public boolean contains(MethodNode caller, MethodNode callee) {
			temp.caller = caller;
			temp.callee = callee;
			return edgeDealed.contains(temp);
		}

	}

	EdgeDealed edgeDealed = new EdgeDealed();

	public CallGraph analyze(CallGraph cha, MethodNode main) {
		this.cha = cha;
		call = new CallGraph();
		List<CallGraphEdge> workList = new LinkedList<CallGraphEdge>();
		workList.add(cha.getCallGraphEdge(main));
		while (workList.size() > 0) {
			CallGraphEdge cge = workList.remove(0);
			analyze(cge, workList);
		}
		return call;
	}

	private void addToWorkList(CallGraphEdge cge, List<CallGraphEdge> work) {
		CallGraphEdge ncge = (CallGraphEdge) cge.clone();
		MethodNode caller = ncge.getCaller();
		List<MethodNode> subs = ncge.getCallees();
		for (int i = 0; i < subs.size(); i++) {
			MethodNode callee = subs.get(i);
			if (edgeDealed.contains(caller, callee))
				ncge.removeCallee(callee);
		}
		if (ncge.isValid())
			work.add(0, ncge);
		else {
			if (cha.hasNext(cge))
				addToWorkList(cge.next, work);
			// System.out.println("can not add: "+cge.toString());
		}
	}

	private void analyze(CallGraphEdge cge, List<CallGraphEdge> work) {
		MethodNode callee = cge.getCallee();
		MethodNode caller = cge.getCaller();
		if (callee != null && callee.isConstructor()) {
			ClassNode cn = cge.getCallee().declaringClass;
			if (!caller.isConstructor())
				newType.add(cn.getType());
			else if (!cn.instanceOf(caller.declaringClass)) {
				newType.add(cn.getType());
			}
		}
		if (callee != null)
			edgeDealed.add(caller, callee);

		if (cha.hasNext(cge))
			addToWorkList(cge.next, work);
		// work.add(0,cge.next);
		if (cge.isClinit() || cge.isStatic()) {
			call.addEdge((CallGraphEdge) cge.clone());
			List<MethodNode> ce = cge.getCallees();
			if (ce != null)
				for (int i = 0; i < ce.size(); i++) {
					MethodNode mn = ce.get(i);
					CallGraphEdge ne = cha.getCallGraphEdge(mn);
					if (ne != null)
						addToWorkList(ne, work);
				}
		} else {
			List<MethodNode> ce = cge.getCallees();
			if (ce != null) {
				CallGraphEdge edge = null;
				for (int i = 0; i < ce.size(); i++) {
					MethodNode mn = ce.get(i);
					ClassNode cn = mn.declaringClass;
					if (newType.contains(cn.getType())) {
						if (edge == null) {
							edge = new CallGraphEdge(cge.caller, cge.invokePC, mn, cge.type);
							call.addEdge(edge);
						} else
							edge.addCallee(mn);
						
						CallGraphEdge ne = cha.getCallGraphEdge(mn);
						if (ne != null)
							addToWorkList(ne, work);
					}
					edgeDealed.add(caller, mn);
				}
				if (edge == null) {
					call.addEdge((CallGraphEdge) cge.clone());
					for (int i = 0; i < ce.size(); i++) {
						MethodNode mn = ce.get(i);
						CallGraphEdge ne = cha.getCallGraphEdge(mn);
						if (ne != null)
							addToWorkList(ne, work);
					}
				}
			}
		}
		// work.add(0,cge);
	}

	public static void main(String[] args) {
		Project project = new Project("D:/eclipse/workspace/ASMAnalysisFactory/bin/");
		// AnalysisFactoryManager.initial();
		ClassReader cr;
		try {
			ClassNodeLoader loader = new ClassNodeLoader("D:/eclipse/workspace/ASMAnalysisFactory/bin/");
			ClassNode cn = loader.loadClassNode("edu.pku.cn.Project", 0);
			// loader.loadClassNode("edu.pku.cn.graph.cfg.test.LiveStoreTest");
			List methods = cn.methods;
			for (int i = 0; i < methods.size(); ++i) {
				MethodNode method = (MethodNode) methods.get(i);
				if (true && method.name.equals("main"))
					if (method.instructions.size() > 0) {
						CHACallGraphBuilder builder = new CHACallGraphBuilder();
						CallGraph call = builder.analyze(method);
						RTACallGraphBuilder rta = new RTACallGraphBuilder();
						call = rta.analyze(call, method);
						System.out.println("*****************************");
						FileOutputStream fs = new FileOutputStream(new File("rta.txt"));
						PrintWriter pw = new PrintWriter(fs);
						pw.write(call.toString());
						pw.close();
						System.out.println("Edges:" + call.edges.size());
						System.out.println("Nodes:" + call.methodToEdge.keySet().size());

					}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

// end

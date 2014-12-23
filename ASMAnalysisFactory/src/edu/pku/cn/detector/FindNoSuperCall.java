/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-1-5 ����09:45:19
 * @modifier: Administrator
 * @time 2009-1-5 ����09:45:19
 * @reviewer: Administrator
 * @time 2009-1-5 ����09:45:19
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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;


import edu.pku.cn.bugreport.BugInstance;
import edu.pku.cn.detector.ClassDetector;
import edu.pku.cn.detector.Detector;
//import edu.pku.cn.hierarchy.Repository;
import edu.pku.cn.util.HASelect;

/**
 * Look for overridden methods which do not invoke super.callXXX().
 * 
 * @author Administrator
 */
public class FindNoSuperCall extends ClassDetector {

	private InvokeTracerVisitor mv;
	private boolean isPublicClass; // indicate a class is public
	private boolean isFinalMethod; // indicate a method is final

	private ArrayList<BugInstance> bugInstances = new ArrayList<BugInstance>();

	private class BugDesc {
		String owner;
		String methodName;

		BugDesc(String owner, String methodName) {
			this.owner = owner;
			this.methodName = methodName;
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
			return true;
		}

		public int hashCode() {
			return this.owner.hashCode() * 10 + this.methodName.hashCode();
		}
	}

	private static Map<String, ArrayList<BugDesc>> owner_bug_map = new HashMap<String, ArrayList<BugDesc>>();
	private HashSet<BugDesc> potentialConcernedMethods = new HashSet<BugDesc>();

	private void initBugDescs() {
		StringTokenizer st;
		String owner = "";
		String methodDesc;
		BufferedReader in = null;
		ArrayList<BugDesc> bugList = null;
		try {
			in = new BufferedReader(new FileReader(System.getProperty("user.dir")
					+ "/defect_set/defectsOfFindNoSuperCall"));
			String line = in.readLine();
			while (line != null) {
				st = new StringTokenizer(line, " ");
				owner = st.nextToken();
				methodDesc = st.nextToken() + st.nextToken();
				bugList = owner_bug_map.get(owner);
				if (bugList == null) {
					bugList = new ArrayList<BugDesc>();
				}
				bugList.add(new BugDesc(owner, methodDesc));
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
	public ArrayList<Detector> getInstances() {
		// TODO Auto-generated method stub
		initBugDescs();
		ArrayList<Detector> detectors = new ArrayList<Detector>();
		detectors.add(new FindNoSuperCall());
		return detectors;
	}

	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		// take care of interfaces and super classes which may be specified in
		// config file
		bugInstances.clear();
		ArrayList<String> potentialSupers = new ArrayList<String>();
		Set<String> supers = owner_bug_map.keySet();
		potentialConcernedMethods.clear();

//		Repository repository = Repository.getInstance();
		Type tn=Type.getType(name);
		for (String certainSuper : supers) {
			Type ts=Type.getType(certainSuper);
			if(HASelect.isAssignableFrom(ts,tn)){
//			if (repository.implementOf(name, certainSuper)) {
				potentialSupers.add(certainSuper);
			}else if(HASelect.isInstanceOf(tn, ts)){
//			} else if (repository.instanceOf(name, certainSuper)) {
				potentialSupers.add(certainSuper);
			}
		}
		for (String potentialSuper : potentialSupers) {
			potentialConcernedMethods.addAll(owner_bug_map.get(potentialSuper));
		}
		mv = new InvokeTracerVisitor();
		if ((access & Opcodes.ACC_PUBLIC) == 1) {
			isPublicClass = true;
		} else {
			isPublicClass = false;
		}
	}

	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		String methodDesc = name + desc;
		if ((access & Opcodes.ACC_FINAL) == Opcodes.ACC_FINAL) {
			isFinalMethod = true;
		} else {
			isFinalMethod = false;
		}
		if (isPublicClass && !isFinalMethod && !potentialConcernedMethods.isEmpty()) {
			// to guarantee that the method is concerned method
			for (BugDesc bugDesc : potentialConcernedMethods) {
				if (bugDesc.methodName.equals(methodDesc)) {
					mv.setBugDesc(bugDesc);
					return mv;
				}
			}
		}
		return null;// otherwise, there is no need to visit the method's
		// internal
	}

	public void visitEnd() {
		if (!bugInstances.isEmpty()) {
			for (BugInstance instance : bugInstances) {
				reportor.report(node.name, instance);
			}
		}
	}

	private class InvokeTracerVisitor implements MethodVisitor {

		private BugDesc bugDesc;

		public void setBugDesc(BugDesc bugDesc) {
			this.bugDesc = bugDesc;
		}

		public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
			// TODO Auto-generated method stub
			return null;
		}

		public AnnotationVisitor visitAnnotationDefault() {
			// TODO Auto-generated method stub
			return null;
		}

		public void visitAttribute(Attribute attr) {
			// TODO Auto-generated method stub

		}

		public void visitCode() {
			// TODO Auto-generated method stub

		}

		public void visitEnd() {
			// TODO Auto-generated method stub
			if (potentialConcernedMethods.contains(bugDesc)) {// the super
				// method is not
				// called
				BugInstance bugInstance = new BugInstance(BugInstance.format(getBugPattern("NO_SUPER_CALL")
						.getLongDescription(), new String[] { bugDesc.owner, bugDesc.methodName }));
				bugInstances.add(bugInstance);
			}
		}

		public void visitFieldInsn(int opcode, String owner, String name, String desc) {
			// TODO Auto-generated method stub

		}

		public void visitFrame(int type, int local, Object[] local2, int stack, Object[] stack2) {
			// TODO Auto-generated method stub

		}

		public void visitIincInsn(int var, int increment) {
			// TODO Auto-generated method stub

		}

		public void visitInsn(int opcode) {
			// TODO Auto-generated method stub

		}

		public void visitIntInsn(int opcode, int operand) {
			// TODO Auto-generated method stub

		}

		public void visitJumpInsn(int opcode, Label label) {
			// TODO Auto-generated method stub

		}

		public void visitLabel(Label label) {
			// TODO Auto-generated method stub

		}

		public void visitLdcInsn(Object cst) {
			// TODO Auto-generated method stub

		}

		public void visitLineNumber(int line, Label start) {
			// TODO Auto-generated method stub

		}

		public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
			// TODO Auto-generated method stub

		}

		public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
			// TODO Auto-generated method stub

		}

		public void visitMaxs(int maxStack, int maxLocals) {
			// TODO Auto-generated method stub

		}

		public void visitMethodInsn(int opcode, String owner, String name, String desc) {
			// TODO Auto-generated method stub
			if (opcode == Opcodes.INVOKESPECIAL && bugDesc.methodName.equals(name + desc)) {// invoke
																							// super
				// method
				potentialConcernedMethods.remove(bugDesc);
			}
		}

		public void visitMultiANewArrayInsn(String desc, int dims) {
			// TODO Auto-generated method stub

		}

		public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
			// TODO Auto-generated method stub
			return null;
		}

		public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels) {
			// TODO Auto-generated method stub

		}

		public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
			// TODO Auto-generated method stub

		}

		public void visitTypeInsn(int opcode, String type) {
			// TODO Auto-generated method stub

		}

		public void visitVarInsn(int opcode, int var) {
			// TODO Auto-generated method stub

		}

	}
}

// end

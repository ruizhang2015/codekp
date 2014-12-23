/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-1-4 ����02:41:11
 * @modifier: Administrator
 * @time 2009-1-4 ����02:41:11
 * @reviewer: Administrator
 * @time 2009-1-4 ����02:41:11
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
import java.util.StringTokenizer;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import edu.pku.cn.bugreport.BugInstance;
import edu.pku.cn.detector.ClassDetector;
import edu.pku.cn.detector.Detector;
import edu.pku.cn.hierarchy.Repository;
import edu.pku.cn.util.HASelect;

/**
 * To find the implementation of certain interface which does not define methods
 * required. for example: class ImplForCloneable implements Cloneable{ // does
 * not define clone() method or use clone() method }
 * 
 * @author Administrator
 */
public class FindImplClassNoDefOrUseMethod extends ClassDetector {

	private InvokeTracerVisitor mv;

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
	private HashSet<BugDesc> potentialConcernedMethods = null;

	private void initBugDescs() {
		StringTokenizer st;
		String owner = "";
		String methodDesc;
		BufferedReader in = null;
		ArrayList<BugDesc> bugList = null;
		try {
			in = new BufferedReader(new FileReader(System.getProperty("user.dir")
					+ "/defect_set/defectsOfFindImplClassNoDefOrUseMethod"));
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
		detectors.add(new FindImplClassNoDefOrUseMethod());
		return detectors;
	}

	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		// only take care of direct implemented interfaces and related
		// constraints
		ArrayList<String> potentialDirectInterfaces = new ArrayList<String>();
		for (int i = 0; i < interfaces.length; i++) {
			if (owner_bug_map.containsKey(interfaces[i])) {
				potentialDirectInterfaces.add(interfaces[i]);
			}
		}

		Repository repository = Repository.getInstance();
		ArrayList<String> temp = new ArrayList<String>();

		for (int i = 0; i < potentialDirectInterfaces.size(); i++) {
			if(!HASelect.isAssignableFrom(Type.getType(potentialDirectInterfaces.get(i)),
					Type.getType(superName))){
//			if (!repository.implementOf(superName, )) {
				temp.add(potentialDirectInterfaces.get(i));
			}
		}

		potentialDirectInterfaces.clear();
		potentialDirectInterfaces.addAll(temp);

		potentialConcernedMethods = new HashSet<BugDesc>();
		for (int i = 0; i < potentialDirectInterfaces.size(); i++) {
			ArrayList<BugDesc> bugList = owner_bug_map.get(potentialDirectInterfaces.get(i));
			potentialConcernedMethods.addAll(bugList);
		}
		mv = new InvokeTracerVisitor(potentialConcernedMethods);
	}

	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		// only take care of methods not defined as expected
		for (BugDesc bugDesc : potentialConcernedMethods) {
			if (bugDesc.methodName.equals(name + desc)) {
				potentialConcernedMethods.remove(bugDesc);
				break;
			}
		}

		mv.setBugSet(potentialConcernedMethods);
		return mv;
	}

	public void visitEnd() {
		potentialConcernedMethods = mv.getBugSet();
		if (!potentialConcernedMethods.isEmpty()) {
			for (BugDesc bugDesc : potentialConcernedMethods) {
				BugInstance instance = new BugInstance(BugInstance.format(getBugPattern("IMPL_NO_METHOD")
						.getLongDescription(), new String[] { bugDesc.owner, bugDesc.methodName }));
				reportor.report(node.name, instance);
			}
		}
	}

	private class InvokeTracerVisitor implements MethodVisitor {

		private HashSet<BugDesc> bugSet = null;

		public InvokeTracerVisitor(HashSet<BugDesc> potentialConcernedMethods) {
			this.bugSet = potentialConcernedMethods;
		}

		public void setBugSet(HashSet<BugDesc> bugSet) {
			this.bugSet = bugSet;
		}

		public HashSet<BugDesc> getBugSet() {
			return bugSet;
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
			// remove methods invoked as expected
			for (BugDesc bugDesc : bugSet) {
				if (bugDesc.methodName.equals(name + desc)) {
					bugSet.remove(bugDesc);
				}
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

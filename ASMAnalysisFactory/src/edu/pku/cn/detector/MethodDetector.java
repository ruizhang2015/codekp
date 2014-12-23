/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-7-21 ����01:28:44
 * @modifier: Liuxizhiyi
 * @time 2008-7-21 ����01:28:44
 * @reviewer: Liuxizhiyi
 * @time 2008-7-21 ����01:28:44
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.detector;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import edu.pku.cn.classfile.MethodNode;

/**
 * 
 * @author Liuxizhiyi
 */
public abstract class MethodDetector extends Detector<MethodNode> implements MethodVisitor {
	// added by MengNa
	protected int currInsnNumber;

	protected int currentLine;

	// added by lijinhui
	// protected int offset;
	public int getLineNumber() {
		return currentLine;

	}

	public String getClassName() {
		return node.owner;
	}

	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		return null;
	}

	public AnnotationVisitor visitAnnotationDefault() {
		return null;
	}

	public void visitAttribute(Attribute attr) {
	}

	public void visitCode() {
		currInsnNumber = -1;
	}

	public void visitEnd() {
	}

	public void visitFieldInsn(int opcode, String owner, String name, String desc) {
		currInsnNumber++;
	}

	public void visitFrame(int type, int local, Object[] local2, int stack, Object[] stack2) {
		currInsnNumber++;
	}

	public void visitIincInsn(int var, int increment) {
		currInsnNumber++;
	}

	public void visitInsn(int opcode) {
		currInsnNumber++;
	}

	public void visitIntInsn(int opcode, int operand) {
		currInsnNumber++;
	}

	public void visitJumpInsn(int opcode, Label label) {
		currInsnNumber++;
	}

	public void visitLabel(Label label) {
		currInsnNumber++;
	}

	public void visitLdcInsn(Object cst) {
		currInsnNumber++;
	}

	public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
	}

	public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
		currInsnNumber++;
	}

	public void visitMaxs(int maxStack, int maxLocals) {
	}

	public void visitMethodInsn(int opcode, String owner, String name, String desc) {
		currInsnNumber++;
	}

	public void visitMultiANewArrayInsn(String desc, int dims) {
		currInsnNumber++;
	}

	public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
		return null;
	}

	public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels) {
		currInsnNumber++;
	}

	public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
	}

	public void visitTypeInsn(int opcode, String type) {
		currInsnNumber++;
	}

	public void visitVarInsn(int opcode, int var) {
		currInsnNumber++;
	}

	public void visitLineNumber(int line, Label start) {
		currentLine = line;
		// offset=start.getOffset();
		currInsnNumber++;
	}
}

// end

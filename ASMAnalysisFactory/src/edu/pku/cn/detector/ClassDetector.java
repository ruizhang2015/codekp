/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-7-21 ����01:30:00
 * @modifier: Liuxizhiyi
 * @time 2008-7-21 ����01:30:00
 * @reviewer: Liuxizhiyi
 * @time 2008-7-21 ����01:30:00
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.detector;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import edu.pku.cn.classfile.ClassNode;

/**
 * 
 * @author Liuxizhiyi
 */
public abstract class ClassDetector extends Detector<ClassNode> implements ClassVisitor {

	public String getClassName() {
		return node.name;
	}

	/**
	 * @see org.objectweb.asm.ClassVisitor#visit(int, int, java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String[])
	 */

	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {

	}

	/**
	 * @see org.objectweb.asm.ClassVisitor#visitAnnotation(java.lang.String,
	 *      boolean)
	 */

	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		return null;
	}

	/**
	 * @see org.objectweb.asm.ClassVisitor#visitAttribute(org.objectweb.asm.Attribute)
	 */

	public void visitAttribute(Attribute attr) {
	}

	/**
	 * @see org.objectweb.asm.ClassVisitor#visitEnd()
	 */

	public void visitEnd() {
	}

	/**
	 * @see org.objectweb.asm.ClassVisitor#visitField(int, java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.Object)
	 */

	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
		return null;
	}

	/**
	 * @see org.objectweb.asm.ClassVisitor#visitInnerClass(java.lang.String,
	 *      java.lang.String, java.lang.String, int)
	 */

	public void visitInnerClass(String name, String outerName, String innerName, int access) {
	}

	/**
	 * @see org.objectweb.asm.ClassVisitor#visitMethod(int, java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String[])
	 */

	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		return null;
	}

	/**
	 * @see org.objectweb.asm.ClassVisitor#visitOuterClass(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */

	public void visitOuterClass(String owner, String name, String desc) {
	}

	/**
	 * @see org.objectweb.asm.ClassVisitor#visitSource(java.lang.String,
	 *      java.lang.String)
	 */

	public void visitSource(String source, String debug) {
	}

}

// end

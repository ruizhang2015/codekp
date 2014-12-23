/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-6-30 ����12:44:53
 * @modifier: Liuxizhiyi
 * @time 2008-6-30 ����12:44:53
 * @reviewer: Liuxizhiyi
 * @time 2008-6-30 ����12:44:53
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.visitor;

import org.objectweb.asm.AnnotationVisitor;

/**
 * 
 * @author Liuxizhiyi
 */
public abstract class AbstractAnnotationVisitor implements AnnotationVisitor {

	/**
	 * @see org.objectweb.asm.AnnotationVisitor#visit(java.lang.String,
	 *      java.lang.Object)
	 */
	@Override
	public void visit(String name, Object value) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.objectweb.asm.AnnotationVisitor#visitAnnotation(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public AnnotationVisitor visitAnnotation(String name, String desc) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.objectweb.asm.AnnotationVisitor#visitArray(java.lang.String)
	 */
	@Override
	public AnnotationVisitor visitArray(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.objectweb.asm.AnnotationVisitor#visitEnd()
	 */
	@Override
	public void visitEnd() {
		visitAnnotationEnd();
	}

	public abstract void visitAnnotationEnd();

	/**
	 * @see org.objectweb.asm.AnnotationVisitor#visitEnum(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public void visitEnum(String name, String desc, String value) {
		// TODO Auto-generated method stub

	}

}

// end

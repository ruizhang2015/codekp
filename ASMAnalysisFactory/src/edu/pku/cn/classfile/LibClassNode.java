/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author liuxi
 * @time 2010-3-27
 * @modifier: liuxi
 * @time 2010-3-27
 * @reviewer: liuxi
 * @time 2010-3-27
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.classfile;

import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import edu.pku.cn.util.CodaProperties;

/**
 * @author liuxi
 */
public class LibClassNode extends ClassNode {
	// ------------------------------------------------------------------------
	// Implementation of the ClassVisitor interface
	// ------------------------------------------------------------------------

	public FieldVisitor visitField(final int access, final String name, final String desc, final String signature,
			final Object value) {
		FieldNode fn = new FieldNode(access, name, desc, signature, value);
		fn.decalringClass=this;
		fields.add(fn);
		fieldMap.put(name, fields.size() - 1);
		return null;
	}

	public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature,
			final String[] exceptions) {
		MethodNode mn = new MethodNode(access, name, desc, signature, exceptions);
		mn.owner = this.name;
        mn.declaringClass=this;
		methods.add(mn);
		methodMap.put(name + desc, methods.size() - 1);
		if(CodaProperties.isLibExpland && this.isLibClass())
			return mn;
		if(CodaProperties.isJREExpland && this.isJREClass())
			return mn;
		return null;
	}
}

// end

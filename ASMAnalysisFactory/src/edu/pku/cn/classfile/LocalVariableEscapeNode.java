/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author root
 * @time 2009-12-8 下午03:54:55
 * @modifier: root
 * @time 2009-12-8 下午03:54:55
 * @reviewer: root
 * @time 2009-12-8 下午03:54:55
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.classfile;

import org.objectweb.asm.tree.LabelNode;

public class LocalVariableEscapeNode extends LocalVariableNode {
	/**
	 * Constructs a new {@link LocalVariableEscapeNode}.
	 * 
	 * @param name
	 *            the name of a local variable.
	 * @param desc
	 *            the type descriptor of this local variable.
	 * @param signature
	 *            the signature of this local variable. May be <tt>null</tt>.
	 * @param start
	 *            the first instruction corresponding to the scope of this local
	 *            variable (inclusive).
	 * @param end
	 *            the last instruction corresponding to the scope of this local
	 *            variable (exclusive).
	 * @param index
	 *            the local variable's index.
	 */
	public LocalVariableEscapeNode(final String name, final String desc, final String signature, final LabelNode start,
			final LabelNode end, final int index) {
		super(name, desc, signature, start, end, index);
	}
}

// end

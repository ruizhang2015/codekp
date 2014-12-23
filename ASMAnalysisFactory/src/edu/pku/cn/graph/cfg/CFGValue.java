/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author root
 * @time 2009-12-29 下午02:55:13
 * @modifier: root
 * @time 2009-12-29 下午02:55:13
 * @reviewer: root
 * @time 2009-12-29 下午02:55:13
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.cfg;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.analysis.BasicValue;
import org.objectweb.asm.tree.analysis.Value;

public class CFGValue implements Value {
	BasicValue value;
	public int var;
	public int insnIndex;

	public CFGValue(BasicValue value) {
		this.value = value;
		var = -1;
		insnIndex = -1;
	}

	public boolean equals(Object value) {
		return value.equals(value);
	}

	public int getSize() {
		return value.getSize();
	}

	public Type getType() {
		return value.getType();
	}

	public int hashCode() {
		return value.hashCode();
	}

	public boolean isReference() {
		return value.isReference();
	}

	public String toString() {
		return value.toString();
	}

}

// end

/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-11 обнГ03:11:44
 * @modifier: a
 * @time 2010-1-11 обнГ03:11:44
 * @reviewer: a
 * @time 2010-1-11 обнГ03:11:44
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.jir;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.FieldInsnNode;

import edu.pku.cn.classfile.FieldNode;

/**
 * @author zhouzhiyi
 */
public class FieldRef implements Ref {
	public String name;
	public String desc;
	public String owner;
	// public FieldInsnNode node;
	public JIRValue base;

	FieldRef(String name, String desc, String owner, JIRValue base) {
		this.name = name;
		this.desc = desc;
		this.owner = owner;
		this.base = base;
	}

	public FieldRef(JIRValue base, FieldInsnNode node) {
		this.base = base;
		this.name = node.name;
		this.desc = node.desc;
		this.owner = node.owner;
	}

	public int getSort() {
		return FIELDREF;
	}

	public String getName() {
		return name;
	}

	public Object clone() {
		return new FieldRef(name, desc, owner, JIR.cloneIfNeed(base));
	}

	@Override
	public void apply(Switch sw) {
		// TODO Auto-generated method stub
		((AbstractRefSwitch) sw).caseField(this);
	}

	public boolean isStatic() {
		return base instanceof Null;
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return Type.getType(desc);
	}

	@Override
	public String toString() {
		if (isStatic())
			return owner + "." + name + ":" + desc;
		return base + "." + name + ":" + desc;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FieldRef) {
			FieldRef ref = (FieldRef) obj;
			if (ref.toString().equals(this.toString())) {
				return true;
			} else
				return false;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}

}

// end

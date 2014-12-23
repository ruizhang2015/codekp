/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-5 ÏÂÎç03:42:13
 * @modifier: a
 * @time 2010-1-5 ÏÂÎç03:42:13
 * @reviewer: a
 * @time 2010-1-5 ÏÂÎç03:42:13
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.analysis;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.objectweb.asm.Type;

import edu.pku.cn.jir.ArrayRef;
import edu.pku.cn.jir.Expr;
import edu.pku.cn.jir.JIRValue;
import edu.pku.cn.jir.Ref;

/**
 * @author liangguangtai
 */
public class RefVarInfo extends Object {

	Set<JIRValue> alias = new HashSet<JIRValue>(16, 0.7f);
	Set<Type> types = new HashSet<Type>(16, 0.7f);
	Set<String> objects = new HashSet<String>(16, 0.7f);

	public boolean hasNonEmptyIntersection(RefVarInfo other) {
		RefVarInfo points = (RefVarInfo) other;
		Iterator<JIRValue> iter = points.alias.iterator();
		while (iter.hasNext()) {
			if (alias.contains(iter.next()))
				return true;
		}
		return false;
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return alias.isEmpty();
	}

	public RefVarInfo mergeWith(RefVarInfo guest) {
		this.alias.addAll(guest.alias);
		this.types.addAll(guest.types);
		this.objects.addAll(guest.objects);
		return this;
	}

	public RefVarInfo clone() {

		RefVarInfo newInfo = new RefVarInfo();

		for (JIRValue jirValue : this.alias) {
			newInfo.alias.add(jirValue);
		}
		for (Type type : this.types) {
			newInfo.types.add(type);
		}
		for (String str : this.objects) {
			newInfo.objects.add(str);
		}

		return newInfo;

	}

	public Set<Type> possibleTypes() {
		return this.types;
	}

	public boolean add(JIRValue e) {
		return alias.add(e);
	}

	public boolean contains(JIRValue o) {
		return alias.contains(o);
	}

	public static RefVarInfo clone(RefVarInfo rightNodePointsToInfo) {
		RefVarInfo newInfo = new RefVarInfo();
		Set<JIRValue> alias = rightNodePointsToInfo.alias;
		for (JIRValue ref : alias) {
			newInfo.alias.add(ref);
		}
		Set<Type> types = rightNodePointsToInfo.types;
		for (Type type : types) {
			newInfo.types.add(type);
		}

		Set<String> objects = rightNodePointsToInfo.objects;
		for (String object : objects) {
			newInfo.objects.add(object);
		}

		return newInfo;
	}

	public boolean equals(RefVarInfo info) {
		if (this.alias.size() != info.alias.size() || this.types.size() != info.types.size()
				|| this.objects.size() != info.objects.size()) {
			return false;
		}

		for (JIRValue value : this.alias) {
			if (!info.alias.contains(value)) {
				return false;
			}
		}

		for (String object : this.objects) {
			if (!info.objects.contains(object)){
				return false;
			}
		}

		for (Type type : this.types) {
			if (!info.types.contains(type)){
				return false;
			}
		}
		
		return true;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("--types:  ");
		for (Type type : this.types) {
			builder.append(type.getClassName() + "; ");
		}
		builder.append("\n--objects:  ");
		for (String obj : this.objects) {
			builder.append(obj + "; ");
		}
		builder.append("\n--alias:	");
		for (JIRValue alias : this.alias) {
			builder.append(alias.toString() + "; ");
		}
		return builder.toString();
	}
}

// end

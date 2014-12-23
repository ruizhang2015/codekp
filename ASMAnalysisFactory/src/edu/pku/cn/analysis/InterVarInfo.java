/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-5 ����03:42:13
 * @modifier: a
 * @time 2010-1-5 ����03:42:13
 * @reviewer: a
 * @time 2010-1-5 ����03:42:13
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
public class InterVarInfo extends Object {

	public Set<InterJIRValue> alias = new HashSet<InterJIRValue>(16, 0.7f);
	Set<Type> types = new HashSet<Type>(16, 0.7f);
	public Set<String> objects = new HashSet<String>(16, 0.7f);
	public Set<JIRValue> values = new HashSet<JIRValue>(16, 0.7f);
	boolean isRead = false;
	boolean isWritten = false;
	public Set<InterJIRValue> lastAssignJIRValues = new HashSet<InterJIRValue>(16, 0.7f);

	public boolean hasNonEmptyIntersection(InterVarInfo other) {
		InterVarInfo points = (InterVarInfo) other;
		Iterator<InterJIRValue> iter = points.alias.iterator();
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

	public InterVarInfo mergeWith(InterVarInfo guest) {
		this.alias.addAll(guest.alias);
		this.types.addAll(guest.types);
		this.objects.addAll(guest.objects);
		this.values.addAll(guest.values);
		this.lastAssignJIRValues.addAll(guest.lastAssignJIRValues);
		
		if(guest.isRead == true || this.isRead == true){
			this.isRead = true;
		} else
			this.isRead = false;
		
		if(guest.isWritten == true || this.isWritten == true) {
			this.isWritten = true;
		} else 
			this.isWritten = false;
		
		return this;
	}

	public InterVarInfo clone() {

		InterVarInfo newInfo = new InterVarInfo();

		for (InterJIRValue jirValue : this.alias) {
			newInfo.alias.add(jirValue);
		}
		for (Type type : this.types) {
			newInfo.types.add(type);
		}
		for (String str : this.objects) {
			newInfo.objects.add(str);
		}
		for (JIRValue jirValue : this.values) {
			newInfo.values.add(jirValue);
		}
		
		for (InterJIRValue jirValue : this.lastAssignJIRValues) {
			newInfo.lastAssignJIRValues.add(jirValue);
		}
		
		newInfo.isRead = this.isRead;
		newInfo.isWritten = this.isWritten;
		
		return newInfo;

	}

	public Set<Type> possibleTypes() {
		return this.types;
	}

	public boolean add(InterJIRValue e) {
		return alias.add(e);
	}

	public boolean contains(InterJIRValue o) {
		return alias.contains(o);
	}

	public static InterVarInfo clone(InterVarInfo rightNodePointsToInfo) {
		InterVarInfo newInfo = new InterVarInfo();
		Set<InterJIRValue> alias = rightNodePointsToInfo.alias;
		for (InterJIRValue ref : alias) {
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

		Set<JIRValue> values = rightNodePointsToInfo.values;
		for (JIRValue value : values) {
			newInfo.values.add(value);
		}
		
		for (InterJIRValue jirValue : rightNodePointsToInfo.lastAssignJIRValues) {
			newInfo.lastAssignJIRValues.add(jirValue);
		}
		
		newInfo.isRead = rightNodePointsToInfo.isRead;
		newInfo.isWritten = rightNodePointsToInfo.isWritten;
		
		return newInfo;
	}

	public boolean equals(InterVarInfo info) {
		if (this.alias.size() != info.alias.size() || this.types.size() != info.types.size()
				|| this.objects.size() != info.objects.size() || this.values.size() != info.values.size()
				|| this.isRead != info.isRead || this.isWritten != info.isWritten ) {
			return false;
		}
  
		for (InterJIRValue value : this.alias) {
			if (!info.alias.contains(value)) {
				return false;
			}
		}

		for (String object : this.objects) {
			if (!info.objects.contains(object)) {
				return false;
			}
		}

		for (Type type : this.types) {
			if (!info.types.contains(type)) {
				return false;
			}
		}

		for (JIRValue value : this.values) {
			if (!info.values.contains(value)) {
				return false;
			}
		}

		return true;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("--types:  ");
		for (Type type : this.types) {
			builder.append(type.getClassName());
		}

		builder.append("\n--objects:  ");
		for (String obj : this.objects) {
			builder.append(obj);
		}

		builder.append("\n--values:  ");
		for (JIRValue value : this.values) {
			builder.append(value.toString() + "; ");
		}
		
		builder.append("\n--isRead: " + this.isRead);
		builder.append("\n--isWritten: " + this.isWritten);
		
		builder.append("\n--alias:	");
		for (InterJIRValue alias : this.alias) {
			builder.append(alias.toString() + "; ");
		}
		
		builder.append("\n--lastAssignJIRValue:	");
		for (InterJIRValue jirValue : this.lastAssignJIRValues) {
			builder.append(jirValue.toString() + "; ");
		}
		
		return builder.toString();
	}

	public Set<InterJIRValue> getAlias() {
		return alias;
	}

	public void setAlias(Set<InterJIRValue> alias) {
		this.alias = alias;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	public boolean isWritten() {
		return isWritten;
	}

	public void setWritten(boolean isWritten) {
		this.isWritten = isWritten;
	}

	public Set<InterJIRValue> getLastAssignJIRValues() {
		return lastAssignJIRValues;
	}

	public void setLastAssignJIRValues(Set<InterJIRValue> lastAssignJIRValues) {
		this.lastAssignJIRValues = lastAssignJIRValues;
	}

	public Set<String> getObjects() {
		return objects;
	}

	public void setObjects(Set<String> objects) {
		this.objects = objects;
	}

	public Set<Type> getTypes() {
		return types;
	}

	public void setTypes(Set<Type> types) {
		this.types = types;
	}

	public Set<JIRValue> getValues() {
		return values;
	}

	public void setValues(Set<JIRValue> values) {
		this.values = values;
	}
}

// end

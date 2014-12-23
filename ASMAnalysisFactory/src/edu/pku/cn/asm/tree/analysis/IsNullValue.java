/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-4-1 ����08:53:46
 * @modifier: Administrator
 * @time 2009-4-1 ����08:53:46
 * @reviewer: Administrator
 * @time 2009-4-1 ����08:53:46
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.asm.tree.analysis;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.analysis.Value;

public class IsNullValue implements Value {

	public static final int DEFINITE_NULL = 0x00001;

	public static final int DEFINITE_NONNULL = 0x00011;

	public static final int MAY_NULL = 0x00111;

	public static final int UNKNOWN = 0x01111;

	public static final int INAPPLICABLE = 0x11111;

	protected int value;

	private Type type;

	public IsNullValue(int value, final Type type) {
		this.value = value;
		this.setType(type);
	}

	public boolean equals(final Object value) {
		if (value == null)
			return false;
		if (value == this) {
			return true;
		}
		if (value instanceof IsNullValue) {
			IsNullValue inv = (IsNullValue) value;
			if (type == null) {
				if (inv.type == null && this.value == inv.value)
					return true;
			} else {// type != null
				if (type.equals(inv.type) && this.value == inv.value)
					return true;
			}
		}
		return false;
	}

	public int getSize() {
		return type == Type.LONG_TYPE || type == Type.DOUBLE_TYPE ? 2 : 1;
	}

	public Type getType() {
		return type;
	}

	public int hashCode() {
		int result = (type == null ? 0 : type.hashCode());
		result = result * 1000 + value;
		return result;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(type.toString());
		if (value == IsNullValue.DEFINITE_NULL) {
			buf.append("DEFINITE_NULL,");
		} else if (value == IsNullValue.DEFINITE_NONNULL) {
			buf.append("DEFINITE_NONNULL,");
		} else if (value == IsNullValue.INAPPLICABLE) {
			buf.append("INAPPLICABLE,");
		} else if (value == IsNullValue.MAY_NULL) {
			buf.append("MAY_NULL,");
		} else if (value == IsNullValue.UNKNOWN) {
			buf.append("UNKNOWN,");
		} else {
			buf.append(" ,");
		}
		return buf.toString();
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	// public Object clone(){
	// IsNullValue inv = null;
	// try{
	// inv = (IsNullValue)super.clone();
	// inv.type = type;
	// inv.value = value;
	// }catch(CloneNotSupportedException e){
	// e.printStackTrace();
	// }
	// return inv;
	// }
}

// end

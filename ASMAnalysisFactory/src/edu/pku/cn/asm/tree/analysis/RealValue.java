/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-5-29 ����07:46:49
 * @modifier: Liuxizhiyi
 * @time 2008-5-29 ����07:46:49
 * @reviewer: Liuxizhiyi
 * @time 2008-5-29 ����07:46:49
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.asm.tree.analysis;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.analysis.Value;

/**
 * 
 * @author Liuxizhiyi
 */
public class RealValue<T> implements Value {
	public static final Value RETURNADDRESS_VALUE = new RealValue<Object>(null, null);

	// ��ֵΪ��ȷֵ
	public static final int EXACT_VALUE = 0x111;
	// ��ֵΪ����ֵ
	public static final int CONDITION_VALUE = 0x011;
	// ��ֵ�޷�ȷ��
	public static final int UNKNOW_VALUE = 0x001;

	protected String name;
	// protected int sourceLine;
	protected T value;

	private Type type;

	private Object varObject = null;// if this is a value from LocalVariable or
	// from Field, the variable or the field
	// will be stored here
	// �ֲ���������
	protected int var = -1;

	public int exactValue = EXACT_VALUE;

	public RealValue(final Type type, T value) {
		this.type = type == null ? Type.NULL: type;
		this.value = value;
	}

	public RealValue(final Type type, int exactValue, T value, String name, final Object varObject) {
		this.name = name;
		this.exactValue = exactValue;
		this.value = value;
		this.type = type;
		this.varObject = varObject;
	}

	public RealValue(final Type type, T value, int exact) {
		this(type, value);
		exactValue = exact;
	}

	public RealValue(final Type type, T value, int var, int exact) {
		this(type, value, exact);
		this.var = var;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public int getSize() {
		return type == Type.LONG_TYPE || type == Type.DOUBLE_TYPE ? 2 : 1;
	}

	public boolean isReference() {
		return type != null && (type.getSort() == Type.OBJECT || type.getSort() == Type.ARRAY);
	}

	// public boolean equals(final Object value) {
	// if(value == null) return false;
	// if (value == this) {
	// return true;
	// } else if (value instanceof RealValue) {
	// RealValue rv=(RealValue)value;
	// if (type == null) {
	// if(rv.type==null){
	// if(this.value==null && rv.value==null)
	// return true;
	// return this.value.equals(rv.value);
	// }
	// } else if(type.equals(rv.type)){
	// if(this.value==null && rv.value==null)
	// return true;
	// return this.value.equals(rv.value);
	// }
	// }
	// return false;
	// }

	public boolean equals(final Object value) {
		if (value == null)
			return false;
		if (value == this) {
			return true;
		} else if (value instanceof RealValue) {
			RealValue rv = (RealValue) value;
			if (type == null) {
				if (rv.type == null) {
					if (this.value == null && rv.value == null)
						return true;
					if (this.exactValue == rv.exactValue) { // if both are exact
						// values and the
						// values are the
						// same
						if (this.exactValue == EXACT_VALUE)
							return this.value.equals(rv.value);
						else if (this.exactValue == UNKNOW_VALUE) // if both are
							// unexact
							// values
							return true;
					}
				}
			} else if (type.equals(rv.type)) {
				if (this.value == null && rv.value == null)
					return true;
				if ((this.value != null && rv.value == null) || (this.value == null && rv.value != null))
					return false;
				if (this.exactValue == rv.exactValue) { // if both are exact
					// values and the values
					// are the same
					if (this.exactValue == EXACT_VALUE)
						return this.value.equals(rv.value);
					else if (this.exactValue == UNKNOW_VALUE) // if both are
						// unexact values
						return true;
				}
			}
		}
		return false;
	}

	public int hashCode() {
		return type == null ? 0 : type.hashCode();
	}

	public String toString() {
		if (this == RETURNADDRESS_VALUE) {
			return "Return,";
		} else if (type == null) {
			return "Null,";
		} else if (type.getSort() == Type.ARRAY) {
			// return "["+(value==null?"null":value.toString())+",";
			return (type == null ? "null type" : type.getDescriptor() + ",");
		} else if (type.getSort() == Type.OBJECT) {
			// return "L"+(value==null?"null":value.toString())+",";
			return (type == null ? "null type" : type.getDescriptor() + ",");
		} else {
			if (value == null) {
				return type.getDescriptor() + "null,";
			} else if (exactValue == EXACT_VALUE) {
				return type.getDescriptor() + value.toString() + ",";
			} else {
				return type.getDescriptor() + "-,";
			}
		}
	}

	/**
	 * ������
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getVar() {
		return var;
	}

	public void setVar(int var) {
		this.var = var;
	}

	public void setVarObject(Object obj) {
		this.varObject = obj;
	}

	public Object getVarObject() {
		return this.varObject;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public RealValue<T> getFromArray(Value index) {
		try {
			int i = ((RealValue<Integer>) index).getValue().intValue();
			T v = null;
			return new RealValue<T>(Type.getObjectType("null"), v);
			// if(i < ((Object[])value).length)
			// v=(T)((Object[])value)[i];
			// if(v==null)
			// return new RealValue<T>(Type.getObjectType("null"),v);
			// else
			// return new RealValue<T>(Type.getType(v.getClass()),v);
		} catch (Exception e) {
			RealValue<T> v = new RealValue<T>(Type.getType(e.getClass()), (T) e);
			v.setName(e.getMessage());
			return v;
		}
	}

	public <ValueType extends Number> ValueType getNumber() {
		if (value instanceof Number) {
			return (ValueType) value;
		} else if (value instanceof Boolean) {
			return ((Boolean) value == true) ? (ValueType) Integer.valueOf(1) : (ValueType) Integer.valueOf(0);
		} else if (value instanceof Character) {
			return (ValueType) Integer.valueOf(((Character) value).charValue());
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	protected static double execute(char type, Number v1, Number v2) throws ValueException {
		try {
			switch (type) {
			case '+':
				return v1.doubleValue() + v2.doubleValue();
			case '-':
				return v1.doubleValue() - v2.doubleValue();
			case '*':
				return v1.doubleValue() * v2.doubleValue();
			case '/':
				return v1.doubleValue() / v2.doubleValue();
			case '%':
				return v1.doubleValue() % v2.doubleValue();
			case '&':
				return v1.longValue() & v2.longValue();
			case '|':
				return v1.longValue() | v2.longValue();
			case '^':
				return v1.longValue() ^ v2.longValue();
			case '<':
				return v1.longValue() << v2.longValue();
			case '>':
				return v1.longValue() >> v2.longValue();
			case ']':
				return v1.longValue() >>> v2.longValue();
			default:
				return Double.NaN;
			}
		} catch (Exception e) {
			throw new ValueException(e.getMessage(), e);
		}
	}

	protected Number getValue(char opType, Number v1, Number v2) throws ValueException {
		double value = execute(opType, v1, v2);
		switch (type.getSort()) {
		case Type.BYTE:
			return new Byte((byte) value);
		case Type.INT:
			return new Integer((int) value);
		case Type.FLOAT:
			return new Float(value);
		case Type.LONG:
			return new Long((long) value);
		case Type.DOUBLE:
			return value;
		default:
			return value;
		}
	}
}

// end

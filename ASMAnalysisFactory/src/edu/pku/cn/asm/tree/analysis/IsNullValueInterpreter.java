/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-4-1 ����08:48:26
 * @modifier: Administrator
 * @time 2009-4-1 ����08:48:26
 * @reviewer: Administrator
 * @time 2009-4-1 ����08:48:26
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.asm.tree.analysis;

/**
 * The design of the lattice is 
 *             MAY_NULL
 *             /     \
 *            /       \
 *           /         \
 *       UNKNOWN      NULL
 *         /            /    
 *        /            /
 *      NONNULL       /
 *        \          /
 *         \        /
 *         INAPPLICABLE
 *      NONNULL union NULL = MAY_NULL
 *      NONNULL union UNKNOWN = UNKNOWN
 *      NULL union UNKNOWN = MAY_NULL
 *      NONNULL/NULL/UNKNOWN union MAY_NULL = MAY_NULL    
 *      INAPPLICABLE union any = any
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Interpreter;
import org.objectweb.asm.tree.analysis.Value;

import edu.pku.cn.classfile.FieldNode;

public class IsNullValueInterpreter implements Opcodes, Interpreter {

	private InsnList insns;

	private HashMap<String, IsNullValue> fieldMap;

	private Map<Integer, List> localVariableMap;

	public IsNullValueInterpreter(List<FieldNode> fields) {
		fieldMap = new HashMap<String, IsNullValue>();
		IsNullValue inv = null;
		for (int i = 0; i < fields.size(); i++) {
			FieldNode fNode = fields.get(i);
			String desc = fNode.desc;
			Type type = Type.getType(desc);
			// to decide the type and IsNullValue of the field
			try {
				if (type == null) {
					throw new AnalyzerException("This is an impossible type of a field");
				}
				switch (type.getSort()) {
				case Type.INT:
				case Type.BOOLEAN:
				case Type.CHAR:
				case Type.FLOAT:
				case Type.DOUBLE:
				case Type.BYTE:
				case Type.SHORT:
				case Type.LONG:
					inv = (IsNullValue) newValue(type);
					break;
				default:
					inv = (IsNullValue) newValue(IsNullValue.UNKNOWN, type);
					if (fNode.value == null)
						inv.value = IsNullValue.DEFINITE_NULL;
					else
						inv.value = IsNullValue.DEFINITE_NONNULL;
					break;
				}
				fieldMap.put(fNode.name, inv);
			} catch (AnalyzerException e) {
				e.printStackTrace();
			}
		}
	}

	public void setLocalVariableMap(Map<Integer, List> localVariableMap) {
		this.localVariableMap = localVariableMap;
	}

	public void setFieldInvValue(FieldNode fNode, int invValue) {
		IsNullValue isNullValue = fieldMap.get(fNode);
		try {
			if (isNullValue == null) {
				throw new AnalyzerException("The needed fieldNode does not exist");
			} else {
				isNullValue.setValue(invValue);
				fieldMap.put(fNode.name, isNullValue);
			}
		} catch (AnalyzerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setInsns(InsnList insns) {
		this.insns = insns;
	}

	public Value binaryOperation(AbstractInsnNode insn, Value value1, Value value2) throws AnalyzerException {
		switch (insn.getOpcode()) {
		case IALOAD:
		case BALOAD:
		case CALOAD:
		case SALOAD:
		case IADD:
		case ISUB:
		case IMUL:
		case IDIV:
		case IREM:
		case ISHL:
		case ISHR:
		case IUSHR:
		case IAND:
		case IOR:
		case IXOR:
			return newValue(Type.INT_TYPE);
		case FALOAD:
		case FADD:
		case FSUB:
		case FMUL:
		case FDIV:
		case FREM:
			return newValue(Type.FLOAT_TYPE);
		case LALOAD:
		case LADD:
		case LSUB:
		case LMUL:
		case LDIV:
		case LREM:
		case LSHL:
		case LSHR:
		case LUSHR:
		case LAND:
		case LOR:
		case LXOR:
			return newValue(Type.LONG_TYPE);
		case DALOAD:
		case DADD:
		case DSUB:
		case DMUL:
		case DDIV:
		case DREM:
			return newValue(Type.DOUBLE_TYPE);
		case AALOAD:
			return newValue(Type.OBJECT_TYPE);
		case LCMP:
		case FCMPL:
		case FCMPG:
		case DCMPL:
		case DCMPG:
			return newValue(Type.INT_TYPE);
		case IF_ICMPEQ:
		case IF_ICMPNE:
		case IF_ICMPLT:
		case IF_ICMPGE:
		case IF_ICMPGT:
		case IF_ICMPLE:
		case IF_ACMPEQ:
		case IF_ACMPNE:
			return null;
		case PUTFIELD:
			FieldInsnNode fieldInsn = (FieldInsnNode) insn;
			IsNullValue invValue = fieldMap.get(fieldInsn.name);
			if (invValue != null)
				invValue.value = ((IsNullValue) value2).value;
			return null;
		default:
			throw new Error("Internal error.");
		}
	}

	public Value copyOperation(AbstractInsnNode insn, Value value) throws AnalyzerException {
		int maxStart = -1;
		int minEnd = Integer.MAX_VALUE;
		Object tempNode = null;
		IsNullValue result = (IsNullValue) newValue(((IsNullValue) value).getValue(), ((IsNullValue) value).getType());
		switch (insn.getOpcode()) {
		case Opcodes.ILOAD:
		case Opcodes.LLOAD:
		case Opcodes.FLOAD:
		case Opcodes.DLOAD:
		case Opcodes.ALOAD:
			int lvIndex = ((VarInsnNode) insn).var;
			int insnIndex = insn.index;
			List lvList = localVariableMap.get(lvIndex);// lvListӦ�ô���Ǹ��ֲ�������
			maxStart = -1;
			minEnd = Integer.MAX_VALUE;
			tempNode = null; // ��Ϊ�洢����ƥ���LocalVariableNode
			for (int i = 0; i < lvList.size(); i++) {
				Object node = lvList.get(i);// ����ƥ��ֲ�����
				if (node instanceof LocalVariableNode) {
					LocalVariableNode lvNode = (LocalVariableNode) node;
					if (lvNode.index == lvIndex && lvNode.start.index <= insnIndex + 1 && lvNode.end.index >= insnIndex
							&& lvNode.start.index >= maxStart && lvNode.end.index <= minEnd) {
						maxStart = lvNode.start.index;
						minEnd = lvNode.end.index;
						tempNode = lvNode; // ��ƥ��ɹ���Ŀǰ���žֲ���������tempNode��
					}
				}
			}
			if (tempNode == null) {
				throw new AnalyzerException(
						"We cannot find any local variable to aload in the class IsNullValueInterpreter");
			} else {
				switch (insn.getOpcode()) {
				case Opcodes.ILOAD:
					((IsNullValue) result).setType(Type.INT_TYPE);
					break;
				case Opcodes.LLOAD:
					((IsNullValue) result).setType(Type.LONG_TYPE);
					break;
				case Opcodes.FLOAD:
					((IsNullValue) result).setType(Type.FLOAT_TYPE);
					break;
				case Opcodes.DLOAD:
					((IsNullValue) result).setType(Type.DOUBLE_TYPE);
					break;
				case Opcodes.ALOAD:
					((IsNullValue) result).setType(Type.getType(((LocalVariableNode) tempNode).desc));
					break;
				}
				return result;
			}
		case Opcodes.ISTORE:
		case Opcodes.LSTORE:
		case Opcodes.FSTORE:
		case Opcodes.DSTORE:
		case Opcodes.ASTORE:
			lvIndex = ((VarInsnNode) insn).var;
			insnIndex = insn.index;
			lvList = localVariableMap.get(lvIndex);
			if (lvList == null) {// ����ǿ�list���򴴽�һ���ֲ�����
				lvList = new ArrayList<LocalVariableNode>();
			}
			maxStart = -1;
			minEnd = Integer.MAX_VALUE;
			tempNode = null;
			for (int i = 0; i < lvList.size(); i++) {
				Object node = lvList.get(i);// ����ƥ��ֲ�����
				if (node instanceof LocalVariableNode) {
					LocalVariableNode lvNode = (LocalVariableNode) node;
					if (lvNode.index == lvIndex && lvNode.start.index <= insnIndex + 1 && lvNode.end.index >= insnIndex
							&& lvNode.start.index >= maxStart && lvNode.end.index <= minEnd) {
						maxStart = lvNode.start.index;
						minEnd = lvNode.end.index;
						tempNode = lvNode; // ��ƥ��ɹ���Ŀǰ���žֲ���������tempNode��
					}
				}
			}
			if (tempNode == null) {
				throw new AnalyzerException(
						"We cannot find any local variable to astore in the class IsNullValueInterpreter");
			} else {
				LocalVariableNode tempLvNode = (LocalVariableNode) tempNode;
				Type typeOfTempLvNode = Type.getType(tempLvNode.desc);
				Type typeOfValue = ((IsNullValue) value).getType();
				if (!typeOfTempLvNode.equals(typeOfValue)) {
					result.setType(typeOfTempLvNode);
				}
				return result;
			}
		}
		return result;
	}

	public Value merge(Value v, Value w) {
		IsNullValue result = (IsNullValue) newValue(Type.VOID_TYPE);

		// TODO Auto-generated method stub
		if (v == null)
			return w;

		if (w == null)
			return v;

		IsNullValue vNull = (IsNullValue) v;
		IsNullValue wNull = (IsNullValue) w;

		// deal with the types
		// if(vNull.getType() == null || vNull.getType().equals(Type.VOID_TYPE)
		// || vNull.getType().toString().endsWith("Throwable;") ||
		// vNull.getType().toString().endsWith("Exception;"))
		if (vNull.getType() == null || vNull.getType().equals(Type.VOID_TYPE)) {
			result = (IsNullValue) newValue(wNull.getValue(), wNull.getType());
			return result;
		} else if (wNull.getType() == null || wNull.getType().equals(Type.VOID_TYPE))
		// if(wNull.getType() == null || wNull.getType().equals(Type.VOID_TYPE)
		// || wNull.getType().toString().endsWith("Throwable;") ||
		// wNull.getType().toString().endsWith("Exception;"))
		{
			result = (IsNullValue) newValue(vNull.getValue(), vNull.getType());
			return result;
		} else if (wNull.getType().equals(Type.getObjectType("Lundefine"))) {
			result = (IsNullValue) newValue(wNull.value, wNull.getType());
		} else if (vNull.getType().equals(Type.getObjectType("Lundefine"))) {
			result = (IsNullValue) newValue(vNull.value, vNull.getType());
		} else if (!vNull.getType().equals(wNull.getType())) {// ����merge���������������ڲ�ͬ�����ͣ��п����������������������Ǿֲ������������������Σ�ֻҪ�ҳ���Ӧ���������
			if (vNull.getType().getSort() <= Type.DOUBLE && wNull.getType().getSort() <= Type.DOUBLE) {
				if (vNull.getType().getSort() < wNull.getType().getSort()) {
					result.setType(wNull.getType());
				} else {
					result.setType(vNull.getType());
				}
			} else {
				result = new IsNullValue(IsNullValue.MAY_NULL, Type.getObjectType("Lundefine"));
			}
		} else {
			result.setType(vNull.getType());
		}

		// deal with the values
		if (vNull.value == wNull.value) {
			result.value = vNull.value;
		} else if (vNull.value == IsNullValue.UNKNOWN) {
			if (wNull.value == IsNullValue.MAY_NULL || wNull.value == IsNullValue.DEFINITE_NULL) {
				result.value = IsNullValue.MAY_NULL;
			} else {// wNull.value == IsNullValue.DEFINITE_NONNULL
				result.value = IsNullValue.UNKNOWN;
			}
		} else if (vNull.value == IsNullValue.DEFINITE_NONNULL) {
			if (wNull.value == IsNullValue.DEFINITE_NULL || wNull.value == IsNullValue.MAY_NULL) {
				result.value = IsNullValue.MAY_NULL;
			} else {// wNull.value == IsNullValue.UNKNOWN
				result.value = IsNullValue.UNKNOWN;
			}
		} else {// vNull.value == IsNullValue.MAY_NULL || vNull.value ==
			// IsNullValue.DEFINITE_NULL
			result.value = IsNullValue.MAY_NULL;
		}
		return result;
	}

	public Value naryOperation(AbstractInsnNode insn, List values) throws AnalyzerException {
		switch (insn.getOpcode()) {
		case MULTIANEWARRAY:
			return newValue(IsNullValue.DEFINITE_NONNULL, Type.getType(((MultiANewArrayInsnNode) insn).desc));
		case INVOKEVIRTUAL:
		case INVOKESPECIAL:
		case INVOKESTATIC:
		case INVOKEINTERFACE:
			MethodInsnNode mInsn = (MethodInsnNode) insn;
			if (Type.getObjectType(mInsn.owner).getClassName().equals("java.lang.String")
					&& Type.getReturnType(mInsn.desc).getClassName().equals("java.lang.String")) {// by
																									// default,
																									// the
																									// "String"
				// return value of String
				// methods cannot be null
				return newValue(IsNullValue.DEFINITE_NONNULL, Type.getObjectType(mInsn.owner));
			}

			int typeSort = Type.getReturnType(mInsn.desc).getSort();
			switch (typeSort) {
			case Type.BOOLEAN:
			case Type.BYTE:
			case Type.CHAR:
			case Type.DOUBLE:
			case Type.FLOAT:
			case Type.INT:
			case Type.LONG:
			case Type.SHORT:
				return newValue(IsNullValue.INAPPLICABLE, Type.getReturnType(mInsn.desc));
			case Type.VOID:
				return null; // ����ֵ�ǿգ����ô�����Value���أ���ΪFrame���ò���
			}

			return newValue(IsNullValue.UNKNOWN, Type.getReturnType(mInsn.desc));
		}
		return null;
	}

	public Value newOperation(AbstractInsnNode insn) throws AnalyzerException {
		switch (insn.getOpcode()) {
		case ACONST_NULL:
			return newValue(IsNullValue.DEFINITE_NULL, Type.VOID_TYPE);
		case ICONST_M1:
		case ICONST_0:
		case ICONST_1:
		case ICONST_2:
		case ICONST_3:
		case ICONST_4:
		case ICONST_5:
			return newValue(IsNullValue.INAPPLICABLE, Type.INT_TYPE);
		case LCONST_0:
		case LCONST_1:
			return newValue(IsNullValue.INAPPLICABLE, Type.LONG_TYPE);
		case FCONST_0:
		case FCONST_1:
		case FCONST_2:
			return newValue(IsNullValue.INAPPLICABLE, Type.FLOAT_TYPE);
		case DCONST_0:
		case DCONST_1:
			return newValue(IsNullValue.INAPPLICABLE, Type.DOUBLE_TYPE);
		case BIPUSH:
		case SIPUSH:
			return newValue(IsNullValue.INAPPLICABLE, Type.INT_TYPE);
		case LDC:
			Object cst = ((LdcInsnNode) insn).cst;
			if (cst instanceof Integer) {
				return newValue(IsNullValue.INAPPLICABLE, Type.INT_TYPE);
			} else if (cst instanceof Float) {
				return newValue(IsNullValue.INAPPLICABLE, Type.FLOAT_TYPE);
			} else if (cst instanceof Long) {
				return newValue(IsNullValue.INAPPLICABLE, Type.LONG_TYPE);
			} else if (cst instanceof Double) {
				return newValue(IsNullValue.INAPPLICABLE, Type.DOUBLE_TYPE);
			} else {
				return newValue(IsNullValue.DEFINITE_NONNULL, Type.getType(cst.getClass()));
			}
		case JSR:
			return newValue(IsNullValue.INAPPLICABLE, Type.VOID_TYPE);
		case GETSTATIC:
			return newValue(Type.getType(((FieldInsnNode) insn).desc));
		case NEW:
			return newValue(IsNullValue.DEFINITE_NONNULL, Type.getObjectType(((TypeInsnNode) insn).desc));
		default:
			throw new Error("Internal error.");
		}
	}

	@Override
	public Value newValue(final Type type) {
		if (type == null) {
			return newValue(IsNullValue.INAPPLICABLE, Type.VOID_TYPE);
		}
		switch (type.getSort()) {
		case Type.VOID:
		case Type.BOOLEAN:
		case Type.CHAR:
		case Type.BYTE:
		case Type.SHORT:
		case Type.INT:
		case Type.FLOAT:
		case Type.LONG:
		case Type.DOUBLE:
			return newValue(IsNullValue.INAPPLICABLE, type);
		case Type.ARRAY:
		case Type.OBJECT:
			return newValue(IsNullValue.UNKNOWN, type);
		default:
			throw new Error("Internal error");
		}
	}

	public Value newValue(int value, Type type) {
		return new IsNullValue(value, type);
	}

	public Value newValue(final Type type, String valueString) {
		if (valueString.equals("$this") || valueString.equals("$outer")) {
			return newValue(IsNullValue.DEFINITE_NONNULL, type);
		}
		return newValue(IsNullValue.INAPPLICABLE, type);
	}

	public Value ternaryOperation(AbstractInsnNode insn, Value value1, Value value2, Value value3)
			throws AnalyzerException {
		// TODO Auto-generated method stub
		return null;
	}

	public Value unaryOperation(AbstractInsnNode insn, Value value) throws AnalyzerException {
		switch (insn.getOpcode()) {
		case INEG:
		case IINC:
		case L2I:
		case F2I:
		case D2I:
		case I2B:
		case I2C:
		case I2S:
			return newValue(Type.INT_TYPE);
		case FNEG:
		case I2F:
		case L2F:
		case D2F:
			return newValue(Type.FLOAT_TYPE);
		case LNEG:
		case I2L:
		case F2L:
		case D2L:
			return newValue(Type.LONG_TYPE);
		case DNEG:
		case I2D:
		case L2D:
		case F2D:
			return newValue(Type.DOUBLE_TYPE);
		case IFEQ:
		case IFNE:
		case IFLT:
		case IFGE:
		case IFGT:
		case IFLE:
		case TABLESWITCH:
		case LOOKUPSWITCH:
		case IRETURN:
		case LRETURN:
		case FRETURN:
		case DRETURN:
		case ARETURN:
		case PUTSTATIC:
			return null;
		case GETFIELD:
			FieldInsnNode fieldInsn = (FieldInsnNode) insn;
			IsNullValue invValue = (IsNullValue) newValue(IsNullValue.UNKNOWN, Type.getType(fieldInsn.desc));
			if (fieldMap.containsKey(fieldInsn.name))
				invValue.value = fieldMap.get(fieldInsn.name).value;
			return invValue;
		case NEWARRAY:
			switch (((IntInsnNode) insn).operand) {
			case T_BOOLEAN:
				return newValue(IsNullValue.DEFINITE_NONNULL, Type.getType("[Z"));
			case T_CHAR:
				return newValue(IsNullValue.DEFINITE_NONNULL, Type.getType("[C"));
			case T_BYTE:
				return newValue(IsNullValue.DEFINITE_NONNULL, Type.getType("[B"));
			case T_SHORT:
				return newValue(IsNullValue.DEFINITE_NONNULL, Type.getType("[S"));
			case T_INT:
				return newValue(IsNullValue.DEFINITE_NONNULL, Type.getType("[I"));
			case T_FLOAT:
				return newValue(IsNullValue.DEFINITE_NONNULL, Type.getType("[F"));
			case T_DOUBLE:
				return newValue(IsNullValue.DEFINITE_NONNULL, Type.getType("[T"));
			case T_LONG:
				return newValue(IsNullValue.DEFINITE_NONNULL, Type.getType("[J"));
			default:
				throw new AnalyzerException("Invalid array type when analyzing IsNullValue");

			}
		case ANEWARRAY:
			String desc = ((TypeInsnNode) insn).desc;
			return newValue(IsNullValue.DEFINITE_NONNULL, Type.getType("[" + Type.getObjectType(desc)));
		case ARRAYLENGTH:
			return newValue(Type.INT_TYPE);
		case ATHROW:
			return null;
		case CHECKCAST:
			return newValue(IsNullValue.UNKNOWN, Type.getObjectType(((TypeInsnNode) insn).desc));
		case INSTANCEOF:
			return newValue(Type.INT_TYPE);
		case MONITORENTER:
		case MONITOREXIT:
		case IFNULL:
		case IFNONNULL:
			return null;
		default:
			throw new Error("Internal error.");
		}
	}
}

// end

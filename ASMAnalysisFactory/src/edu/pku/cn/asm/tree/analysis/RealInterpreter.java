/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-5-29 ����07:51:12
 * @modifier: Liuxizhiyi
 * @time 2008-5-29 ����07:51:12
 * @reviewer: Liuxizhiyi
 * @time 2008-5-29 ����07:51:12
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.asm.tree.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Interpreter;
import org.objectweb.asm.tree.analysis.Value;

import edu.pku.cn.asm.tree.analysis.RealValue;
import edu.pku.cn.classfile.FieldNode;
import edu.pku.cn.hierarchy.Repository;

/**
 * ���಻��Array��������ģ�⣬��Ϊ����Ƚϸ��ￄ1�7
 * 
 * @author Liuxizhiyi
 */
public class RealInterpreter implements Opcodes, Interpreter {

	private HashMap<String, FieldNode> fieldMaps;

	private List<LocalVariableNode> localVariables;

	private Map<Integer, List> localVariableMap; // if a temp local variable is
	// generated to store a
	// fieldInsn, put the
	// FieldInsn object into a
	// list of the map

	private InsnList insns;// ���ô����Ŀ�ģ����㴴���µľ������

	private Repository repository;

	/**
	 * 
	 */
	public RealInterpreter(List<FieldNode> fields) {
		fieldMaps = new HashMap<String, FieldNode>();
		for (int i = 0; i < fields.size(); i++) {// ������Щû��ָ����ʼֵ����JVM���Զ�Ϊ�����ó�ʼֵ��ģ���ʱ��Ҳģ����ù�ￄ1�7
			FieldNode fNode = fields.get(i);
			String desc = fNode.desc;
			if (desc.equals("I") && fNode.value == null) // �����Ա����Ĭ��ֵ΄1�7
				fNode.value = Integer.valueOf(0);
			else if (desc.equals("Z") && fNode.value == null)
				fNode.value = Boolean.valueOf(false);
			else if (desc.equals("C") && fNode.value == null)
				fNode.value = Character.valueOf((char) 0);
			else if (desc.equals("F") && fNode.value == null)
				fNode.value = Float.valueOf(0.0f);
			else if (desc.equals("D") && fNode.value == null)
				fNode.value = Double.valueOf(0.0d);
			else if (desc.equals("B") && fNode.value == null)
				fNode.value = Byte.valueOf((byte) 0);
			else if (desc.equals("S") && fNode.value == null)
				fNode.value = Short.valueOf((short) 0);
			else if (desc.equals("J") && fNode.value == null)
				fNode.value = Long.valueOf((long) 0);
			fieldMaps.put(fields.get(i).name, fNode);
		}
		localVariables = new ArrayList<LocalVariableNode>();
		repository = Repository.getInstance();
	}

	// /**
	// * Pass in localVariables and build index-localVariableList map.
	// * @param localVariables
	// */
	// public void setLocalVariables(List<LocalVariableNode> localVariables){
	// this.localVariables = localVariables;
	// this.localVariableMap = buildMap(localVariables);
	// }

	public void addLocalVariable(LocalVariableNode lvNode) {
		this.localVariables.add(lvNode);
	}

	public void addLocalVariables(List<LocalVariableNode> lvNodes) {
		this.localVariables.addAll(lvNodes);
	}

	public void clearLocalVariables() {
		this.localVariables.clear();
	}

	public void setInsns(InsnList insns) {
		this.insns = insns;
	}

	public Map<Integer, List> buildMapFromLocalVariables() {
		localVariableMap = new HashMap<Integer, List>();
		for (int i = 0; i < localVariables.size(); i++) {
			LocalVariableNode lvNode = (LocalVariableNode) localVariables.get(i);
			List lvList = localVariableMap.get(lvNode.index);
			if (lvList == null) {
				lvList = new ArrayList();
			}
			// ����ͬһ�������Ķ�����������������ϲￄ1�7
			// for(int j = 0; j < lvList.size(); j ++){
			// if(lvNode.name.equals(((LocalVariableNode)lvList.get(j)).name)
			// &&
			// lvNode.desc.equals(((LocalVariableNode)lvList.get(j)).desc)){//��������ͬһslot����������ͬ�����������ͬ�����Ǿ���Ϊ�������ֲ�����ʵ����ͬһ���ֲ����ￄ1�7
			// LocalVariableNode temp = (LocalVariableNode)lvList.get(j);
			// boolean lvNodeMinStart = lvNode.start.index < temp.start.index ?
			// true : false;
			// boolean lvNodeMaxEnd = lvNode.end.index > temp.end.index ? true :
			// false;
			// LabelNode start = lvNodeMinStart? lvNode.start : temp.start;
			// LabelNode end = lvNodeMaxEnd ? lvNode.end : temp.end;
			// lvList.remove(j);
			// lvNode = new LocalVariableNode(temp.name, temp.desc,
			// temp.signature, start, end, temp.index);
			// break;
			// }//�ϲ���Ӧͬһslot��������ͬ��ͬ��ͬ���;ֲ�����
			// }
			lvList.add(lvNode);
			localVariableMap.put(lvNode.index, lvList);
		}
		return localVariableMap;
	}

	// public Value[] newDescValues(final String desc){
	// Type[] args = Type.getArgumentTypes(desc);
	//		 
	//		 
	// StringTokenizer st = new StringTokenizer(desc, ",");
	// int size = st.countTokens();
	// Value[] values = new RealValue[size];
	// for(int i = 0; i < size; i ++){
	// values[i] = RealValue.UNINITIALIZED_VALUE;
	// }
	// return values;
	// }

	private Value newRealValue(Value value) {
		return new RealValue(((RealValue) value).getType(), ((RealValue) value).exactValue, ((RealValue) value)
				.getValue(), ((RealValue) value).getName(), ((RealValue) value).getVarObject());
	}

	public Value newValue(final Type type) {
		if (type == null) {
			return new RealValue<Object>(Type.NULL, null, RealValue.EXACT_VALUE);
		}
		switch (type.getSort()) {
		case Type.VOID:
			return new RealValue<Object>(Type.VOID_TYPE, null, RealValue.UNKNOW_VALUE);
		case Type.BOOLEAN:
			return new RealValue<Boolean>(Type.BOOLEAN_TYPE, false, RealValue.UNKNOW_VALUE);
		case Type.CHAR:
			return new RealValue<Character>(Type.CHAR_TYPE, '0', RealValue.UNKNOW_VALUE);
		case Type.BYTE:
			return new RealValue<Byte>(Type.BYTE_TYPE, (byte) 0, RealValue.UNKNOW_VALUE);
		case Type.SHORT:
			return new RealValue<Short>(Type.SHORT_TYPE, (short) 0, RealValue.UNKNOW_VALUE);
		case Type.INT:
			return new RealValue<Integer>(Type.INT_TYPE, 0, RealValue.UNKNOW_VALUE);
		case Type.FLOAT:
			return new RealValue<Float>(Type.FLOAT_TYPE, 0f, RealValue.UNKNOW_VALUE);
		case Type.LONG:
			return new RealValue<Long>(Type.LONG_TYPE, 0l, RealValue.UNKNOW_VALUE);
		case Type.DOUBLE:
			return new RealValue<Double>(Type.DOUBLE_TYPE, 0d, RealValue.UNKNOW_VALUE);
		case Type.ARRAY:// modified by Meng, Na �Է��ص��������ģ�ￄ1�7
			if (type.toString().equals("[I"))
				return new RealValue<int[]>(Type.getType("[I"), new int[0], RealValue.UNKNOW_VALUE);
			else if (type.toString().equals("[Z"))
				return new RealValue<boolean[]>(Type.getType("[Z"), new boolean[0], RealValue.UNKNOW_VALUE);
			else if (type.toString().equals("[C"))
				return new RealValue<char[]>(Type.getType("[C"), new char[0], RealValue.UNKNOW_VALUE);
			else if (type.toString().equals("[B"))
				return new RealValue<byte[]>(Type.getType("[B"), new byte[0], RealValue.UNKNOW_VALUE);
			else if (type.toString().equals("[S"))
				return new RealValue<short[]>(Type.getType("[S"), new short[0], RealValue.UNKNOW_VALUE);
			else if (type.toString().equals("[F"))
				return new RealValue<float[]>(Type.getType("[F"), new float[0], RealValue.UNKNOW_VALUE);
			else if (type.toString().equals("[D"))
				return new RealValue<double[]>(Type.getType("[D"), new double[0], RealValue.UNKNOW_VALUE);
			else if (type.toString().equals("[J"))
				return new RealValue<long[]>(Type.getType("[J"), new long[0], RealValue.UNKNOW_VALUE);
			else
				return new RealValue<Object[]>(Type.getType(type.toString()), new Object[0], RealValue.UNKNOW_VALUE);
			// return new RealValue<Object>(type,new Object(),
			// RealValue.UNKNOW_VALUE);
			// return object
		case Type.OBJECT:
			try {
				// return new RealValue<Object>(type,
				// Class.forName(type.getClassName()).newInstance());
				return new RealValue<Object>(type, new Object());// ���ʹ��Class.forName()�������������Timer��ͻᴴ��Timer���󣬵��³������в���ֹ
			} catch (Exception e) {
				return new RealValue<Object>(type, new Object());
			}
		default:
			throw new Error("Internal error");
		}
	}

	// public Value newValue(final Type type) {
	// return newValue(type,false);
	// }

	@SuppressWarnings("unchecked")
	public Value newOperation(final AbstractInsnNode insn) {
		switch (insn.getOpcode()) {
		case ACONST_NULL:
			return new RealValue<Object>(Type.NULL, null, RealValue.EXACT_VALUE);
		case ICONST_M1:
			return new RealValue<Integer>(Type.INT_TYPE, -1, RealValue.EXACT_VALUE);
		case ICONST_0:
		case ICONST_1:
		case ICONST_2:
		case ICONST_3:
		case ICONST_4:
		case ICONST_5:
			return new RealValue<Integer>(Type.INT_TYPE, insn.getOpcode() - ICONST_0, RealValue.EXACT_VALUE);
		case LCONST_0:
		case LCONST_1:
			return new RealValue<Long>(Type.LONG_TYPE, (long) insn.getOpcode() - ICONST_0, RealValue.EXACT_VALUE);
		case FCONST_0:
		case FCONST_1:
		case FCONST_2:
			return new RealValue<Float>(Type.LONG_TYPE, (float) insn.getOpcode() - LCONST_0, RealValue.EXACT_VALUE);
		case DCONST_0:
		case DCONST_1:
			return new RealValue<Double>(Type.LONG_TYPE, (double) insn.getOpcode() - DCONST_0, RealValue.EXACT_VALUE);
		case BIPUSH:
		case SIPUSH:
			return new RealValue<Integer>(Type.INT_TYPE, ((IntInsnNode) insn).operand, RealValue.EXACT_VALUE);
		case LDC:
			Object cst = ((LdcInsnNode) insn).cst;
			if (cst instanceof Integer) {
				return new RealValue<Integer>(Type.INT_TYPE, (Integer) cst, RealValue.EXACT_VALUE);
			} else if (cst instanceof Float) {
				return new RealValue<Float>(Type.FLOAT_TYPE, (Float) cst, RealValue.EXACT_VALUE);
			} else if (cst instanceof Long) {
				return new RealValue<Long>(Type.LONG_TYPE, (Long) cst, RealValue.EXACT_VALUE);
			} else if (cst instanceof Double) {
				return new RealValue<Double>(Type.DOUBLE_TYPE, (Double) cst, RealValue.EXACT_VALUE);
			} else if (cst instanceof Type) {
				// return newValue(Type.getObjectType("java/lang/Class"));
				return new RealValue<Class<?>>(Type.getType(Class.class), cst.getClass(), RealValue.EXACT_VALUE);
			} else if (cst instanceof String) {
				return new RealValue<String>(Type.getType(String.class), (String) cst, RealValue.EXACT_VALUE);
			} else {
				return newValue(Type.getType(cst.getClass()));
			}
		case JSR:
			return RealValue.RETURNADDRESS_VALUE;
		case GETSTATIC:
			FieldInsnNode fieldInsn = (FieldInsnNode) insn;
			RealValue value = (RealValue) newValue(Type.getType(fieldInsn.desc));
			value.setName(fieldInsn.name);
			if (fieldMaps.containsKey(fieldInsn.name))
				// value.setValue(fieldMaps.get(fieldInsn.name).value);
				value.setVarObject(fieldInsn);// add this fieldInsn to the value
			return value;
		case NEW:
			return newValue(Type.getObjectType(((TypeInsnNode) insn).desc));
		default:
			throw new Error("Internal error.");
		}
	}

	@SuppressWarnings("unchecked")
	public Value unaryOperation(final AbstractInsnNode insn, final Value value) throws AnalyzerException {
		RealValue<Object> rv = (RealValue<Object>) value;
		int exact = rv.exactValue;
		RealValue<Object> rv2 = (RealValue) newValue(rv.getType());// do not
		// modify
		// rv, but
		// modify
		// rv2
		// if(exact != RealValue.EXACT_VALUE){
		// return rv2;
		// }
		switch (insn.getOpcode()) {
		case IINC:
			rv2.setValue((Integer) rv.getValue() + ((IincInsnNode) insn).incr);
			rv2.exactValue = exact;
			return rv2;
		case INEG:
			rv2.setValue(-rv.getNumber().intValue());
			rv2.exactValue = exact;
			return rv2;
		case L2I:
		case F2I:
		case D2I:
			return new RealValue<Integer>(Type.INT_TYPE, rv.getNumber().intValue(), exact);
		case I2B:
			return new RealValue<Byte>(Type.INT_TYPE, rv.getNumber().byteValue(), exact);
		case I2C:
			return new RealValue<Character>(Type.INT_TYPE, (char) rv.getNumber().intValue(), exact);
		case I2S:
			return new RealValue<Short>(Type.INT_TYPE, rv.getNumber().shortValue(), exact);
		case FNEG:
			return new RealValue<Float>(Type.INT_TYPE, -rv.getNumber().floatValue(), exact);
		case I2F:
		case L2F:
		case D2F:
			return new RealValue<Float>(Type.INT_TYPE, rv.getNumber().floatValue(), exact);
		case LNEG:
			return new RealValue<Long>(Type.LONG_TYPE, -rv.getNumber().longValue(), exact);
		case I2L:
		case F2L:
		case D2L:
			return new RealValue<Long>(Type.LONG_TYPE, rv.getNumber().longValue(), exact);
		case DNEG:
			return new RealValue<Double>(Type.LONG_TYPE, -rv.getNumber().doubleValue(), exact);
		case I2D:
		case L2D:
		case F2D:
			return new RealValue<Double>(Type.LONG_TYPE, rv.getNumber().doubleValue(), exact);
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
			RealValue v = (RealValue) newValue(Type.getType(fieldInsn.desc));
			v.setName(fieldInsn.name);
			v.setVarObject(fieldInsn);// add this fieldInsn to the value
			if (fieldMaps.containsKey(fieldInsn.name))
				v.setValue(fieldMaps.get(fieldInsn.name).value);
			return v;
		case NEWARRAY:
			int length = ((RealValue<Integer>) value).getValue().intValue();
			if (length < 0) {
				length = 0;
			} else {
				// do nothing
			}
			switch (((IntInsnNode) insn).operand) {
			case T_BOOLEAN:
				return new RealValue<boolean[]>(Type.getType("[Z"), new boolean[length]);
			case T_CHAR:
				return new RealValue<char[]>(Type.getType("[C"), new char[length]);
			case T_BYTE:
				return new RealValue<byte[]>(Type.getType("[B"), new byte[length]);
			case T_SHORT:
				return new RealValue<short[]>(Type.getType("[S"), new short[length]);
			case T_INT:
				return new RealValue<int[]>(Type.getType("[I"), new int[length]);
			case T_FLOAT:
				return new RealValue<float[]>(Type.getType("[F"), new float[length]);
			case T_DOUBLE:
				return new RealValue<double[]>(Type.getType("[D"), new double[length]);
			case T_LONG:
				return new RealValue<long[]>(Type.getType("[J"), new long[length]);
			default:
				throw new AnalyzerException("Invalid array type");
			}
		case ANEWARRAY:
			String desc = ((TypeInsnNode) insn).desc;
			length = ((RealValue<Integer>) value).getValue().intValue();
			if (length < 0)
				return new RealValue<Object[]>(Type.getType("[" + Type.getObjectType(desc)), new Object[0]);
			else
				return new RealValue<Object[]>(Type.getType("[" + Type.getObjectType(desc)), new Object[length]);
			// return newValue(Type.getType("[" + Type.getObjectType(desc)));
		case ARRAYLENGTH:
			return new RealValue<Integer>(Type.INT_TYPE, 0, RealValue.UNKNOW_VALUE);// ������ĳ��ȣ�ģ���ￄ1�7
		case ATHROW:
			return null;
		case CHECKCAST:
			desc = ((TypeInsnNode) insn).desc;
			return newValue(Type.getObjectType(desc));
		case INSTANCEOF:
			// return BasicValue.INT_VALUE;
			String className = Type.getObjectType(((TypeInsnNode) insn).desc).getClassName();
			boolean isInstance = repository.instanceOf(className, rv.getType().getClassName());
			if (isInstance) {
				return new RealValue<Integer>(Type.INT_TYPE, 1, RealValue.EXACT_VALUE);
			} else {
				return new RealValue<Integer>(Type.INT_TYPE, 0, RealValue.EXACT_VALUE);
			}
			// return new RealValue<Integer>(Type.INT_TYPE, null);
		case MONITORENTER:
		case MONITOREXIT:
			// return null;
		case IFNULL:
			// rv.setValue(null);
			// rv.exactValue=true;
			// return null;
		case IFNONNULL:
			// if(rv.getValue()==null && rv.getType()!=null){
			// try {
			// Class c=Class.forName(rv.getType().getClassName());
			// rv.setValue(c.newInstance());
			// rv.exactValue=true;
			// } catch (Exception e) {
			// throw new AnalyzerException(e.getMessage());
			// }
			// }
			return null;
		default:
			throw new Error("Internal error.");
		}
	}

	@SuppressWarnings("unchecked")
	public Value binaryOperation(final AbstractInsnNode insn, final Value value1, final Value value2)
			throws AnalyzerException {
		RealValue<Object> v1 = (RealValue<Object>) value1;
		RealValue<Object> v2 = (RealValue<Object>) value2;
		Type type = v1.getType();
		int exact = v1.exactValue & v2.exactValue;

		// RealValue r2=(RealValue)value2;
		try {
			// System.out.println("v2.var = " + v2.var);
			// modified by Meng Na, so as to take PUTFIELD into consideration
			int index = -1;
			if (v2.getNumber() != null) {
				index = v2.getNumber().intValue();
			}
			switch (insn.getOpcode()) {
			case AALOAD:
				return ((RealValue) value1).getFromArray(value2);
			case BALOAD:
				return new RealValue<Integer>(Type.INT_TYPE, 0, RealValue.UNKNOW_VALUE);// �Դ������ж�ȡԪ�صĲ���������ģ����
				// if(index >= ((RealValue<boolean[]>)value1).getValue().length)
				// return new RealValue<Boolean>(Type.BOOLEAN_TYPE, false,
				// RealValue.UNKNOW_VALUE);//����޷��õ���ȷֵ���������һ��UNKNOWֵ
				// else
				// return new
				// RealValue<Boolean>(Type.BOOLEAN_TYPE,((RealValue<boolean[]>)
				// value1).getValue()[index],RealValue.EXACT_VALUE);//�������ҵ��ľ�ȷֵ
				// return new
				// RealValue<Boolean>(Type.BOOLEAN_TYPE,((RealValue<boolean[]>)
				// value1).getValue()[index],exact);
			case CALOAD:
				return new RealValue<Integer>(Type.INT_TYPE, 0, RealValue.UNKNOW_VALUE);
				// if(index >= ((RealValue<char[]>)value1).getValue().length)
				// return new RealValue<Character>(Type.CHAR_TYPE, (char)0,
				// RealValue.UNKNOW_VALUE);
				// else
				// return new
				// RealValue<Character>(Type.CHAR_TYPE,((RealValue<char[]>)
				// value1).getValue()[index],RealValue.EXACT_VALUE);
				// return new RealValue<Character>(Type.CHAR_TYPE,
				// ((RealValue<char[]>) value1).getValue()[index],exact);
			case SALOAD:
				return new RealValue<Integer>(Type.INT_TYPE, 0, RealValue.UNKNOW_VALUE);
				// if(index >= ((RealValue<short[]>)value1).getValue().length)
				// return new RealValue<Short>(Type.SHORT_TYPE, (short)0,
				// RealValue.UNKNOW_VALUE);
				// else
				// return new RealValue<Short>(Type.SHORT_TYPE,
				// ((RealValue<short[]>) value1).getValue()[index],
				// RealValue.EXACT_VALUE);
				// return new
				// RealValue<Short>(Type.BOOLEAN_TYPE,((RealValue<short[]>)
				// value1).getValue()[index],exact);
			case IALOAD:
				return new RealValue<Integer>(Type.INT_TYPE, (int) 0, RealValue.UNKNOW_VALUE);
				// if(index >= ((RealValue<int[]>)value1).getValue().length)
				// return new RealValue<Integer>(Type.INT_TYPE, (int)0,
				// RealValue.UNKNOW_VALUE);
				// else
				// return new RealValue<Integer>(Type.INT_TYPE,
				// ((RealValue<int[]>) value1).getValue()[index],
				// RealValue.EXACT_VALUE);
				// return new RealValue<Integer>(Type.BOOLEAN_TYPE,
				// ((RealValue<int[]>) value1).getValue()[index],exact);
			case FALOAD:
				return new RealValue<Float>(Type.FLOAT_TYPE, (float) 0, RealValue.UNKNOW_VALUE);
				// if(index >=((RealValue<float[]>) value1).getValue().length)
				// return new RealValue<Float>(Type.FLOAT_TYPE, (float)0,
				// RealValue.UNKNOW_VALUE);
				// else
				// return new RealValue<Float>(Type.FLOAT_TYPE,
				// ((RealValue<float[]>) value1).getValue()[index],
				// RealValue.EXACT_VALUE);
				// return new
				// RealValue<Float>(Type.BOOLEAN_TYPE,((RealValue<float[]>)
				// value1).getValue()[index],exact);
			case LALOAD:
				return new RealValue<Long>(Type.LONG_TYPE, (long) 0, RealValue.UNKNOW_VALUE);
				// if(index >= ((RealValue<long[]>)value1).getValue().length)
				// return new RealValue<Long>(Type.LONG_TYPE, (long)0,
				// RealValue.UNKNOW_VALUE);
				// else
				// return new RealValue<Long>(Type.LONG_TYPE,
				// ((RealValue<long[]>) value1).getValue()[index],
				// RealValue.EXACT_VALUE);
				// return new
				// RealValue<Long>(Type.BOOLEAN_TYPE,((RealValue<long[]>)
				// value1).getValue()[index],exact);
			case DALOAD:
				return new RealValue<Double>(Type.DOUBLE_TYPE, (double) 0, RealValue.UNKNOW_VALUE);
				// if(index >= ((RealValue<double[]>)value1).getValue().length)
				// return new RealValue<Double>(Type.DOUBLE_TYPE, (double)0,
				// RealValue.UNKNOW_VALUE);
				// else
				// return new RealValue<Double>(Type.DOUBLE_TYPE,
				// ((RealValue<double[]>) value1).getValue()[index],
				// RealValue.EXACT_VALUE);
				// return new
				// RealValue<Double>(Type.BOOLEAN_TYPE,((RealValue<double[]>)
				// value1).getValue()[index],exact);
			case IADD:// ֮���Էֱ���IADD/FADD�����ͬһ���?ȡv1��type��Ϊ����type���������е�ʱ��v1��type��v2��type����һ�£�v1��type���������typeҲ��һ��
				return new RealValue(Type.INT_TYPE, v1.getValue('+', ((RealValue) value1).getNumber(),
						((RealValue) value2).getNumber()), exact);
			case FADD:
				return new RealValue(Type.FLOAT_TYPE, v1.getValue('+', ((RealValue) value1).getNumber(),
						((RealValue) value2).getNumber()), exact);
			case LADD:
				return new RealValue(Type.LONG_TYPE, v1.getValue('+', ((RealValue) value1).getNumber(),
						((RealValue) value2).getNumber()), exact);
			case DADD:
				return new RealValue(Type.DOUBLE_TYPE, v1.getValue('+', ((RealValue) value1).getNumber(),
						((RealValue) value2).getNumber()), exact);

			case ISUB:
				return new RealValue(Type.INT_TYPE, v1.getValue('-', ((RealValue) value1).getNumber(),
						((RealValue) value2).getNumber()), exact);
			case FSUB:
				return new RealValue(Type.FLOAT_TYPE, v1.getValue('-', ((RealValue) value1).getNumber(),
						((RealValue) value2).getNumber()), exact);
			case LSUB:
				return new RealValue(Type.LONG_TYPE, v1.getValue('-', ((RealValue) value1).getNumber(),
						((RealValue) value2).getNumber()), exact);
			case DSUB:
				return new RealValue(Type.DOUBLE_TYPE, v1.getValue('-', ((RealValue) value1).getNumber(),
						((RealValue) value2).getNumber()), exact);

			case IMUL:
				return new RealValue(Type.INT_TYPE, v1.getValue('*', ((RealValue) value1).getNumber(),
						((RealValue) value2).getNumber()), exact);
			case FMUL:
				return new RealValue(Type.FLOAT_TYPE, v1.getValue('*', ((RealValue) value1).getNumber(),
						((RealValue) value2).getNumber()), exact);
			case LMUL:
				return new RealValue(Type.LONG_TYPE, v1.getValue('*', ((RealValue) value1).getNumber(),
						((RealValue) value2).getNumber()), exact);
			case DMUL:
				return new RealValue(Type.DOUBLE_TYPE, v1.getValue('*', ((RealValue) value1).getNumber(),
						((RealValue) value2).getNumber()), exact);

			case IDIV:
				return new RealValue(Type.INT_TYPE, v1.getValue('/', ((RealValue) value1).getNumber(),
						((RealValue) value2).getNumber()), exact);
			case FDIV:
				return new RealValue(Type.FLOAT_TYPE, v1.getValue('/', ((RealValue) value1).getNumber(),
						((RealValue) value2).getNumber()), exact);
			case LDIV:
				return new RealValue(Type.LONG_TYPE, v1.getValue('/', ((RealValue) value1).getNumber(),
						((RealValue) value2).getNumber()), exact);
			case DDIV:
				return new RealValue(Type.DOUBLE_TYPE, v1.getValue('/', ((RealValue) value1).getNumber(),
						((RealValue) value2).getNumber()), exact);

			case IREM:
				return new RealValue(Type.INT_TYPE, v1.getValue('%', ((RealValue) value1).getNumber(),
						((RealValue) value2).getNumber()), exact);
			case FREM:
				return new RealValue(Type.FLOAT_TYPE, v1.getValue('%', ((RealValue) value1).getNumber(),
						((RealValue) value2).getNumber()), exact);
			case LREM:
				return new RealValue(Type.LONG_TYPE, v1.getValue('%', ((RealValue) value1).getNumber(),
						((RealValue) value2).getNumber()), exact);
			case DREM:
				return new RealValue(Type.DOUBLE_TYPE, v1.getValue('%', ((RealValue) value1).getNumber(),
						((RealValue) value2).getNumber()), exact);

			case ISHL:
			case LSHL:
				return new RealValue(type, v1.getValue('<', ((RealValue) value1).getNumber(), ((RealValue) value2)
						.getNumber()));
			case ISHR:
			case LSHR:
				return new RealValue(type, v1.getValue('>', ((RealValue) value1).getNumber(), ((RealValue) value2)
						.getNumber()), exact);
			case IUSHR:
				return new RealValue(Type.INT_TYPE, v1.getValue(']', ((RealValue) value1).getNumber(),
						((RealValue) value2).getNumber()), exact);
			case LUSHR:
				return new RealValue(Type.LONG_TYPE, v1.getValue(']', ((RealValue) value1).getNumber(),
						((RealValue) value2).getNumber()), exact);

			case IAND:
				return new RealValue(Type.INT_TYPE, v1.getValue('&', ((RealValue) value1).getNumber(),
						((RealValue) value2).getNumber()), exact);
			case LAND:
				return new RealValue(Type.LONG_TYPE, v1.getValue('&', ((RealValue) value1).getNumber(),
						((RealValue) value2).getNumber()), exact);

			case IOR:
				return new RealValue(Type.INT_TYPE, v1.getValue('|', ((RealValue) value1).getNumber(),
						((RealValue) value2).getNumber()), exact);
			case LOR:
				return new RealValue(Type.LONG_TYPE, v1.getValue('|', ((RealValue) value1).getNumber(),
						((RealValue) value2).getNumber()), exact);

			case IXOR:
				return new RealValue(Type.INT_TYPE, v1.getValue('^', ((RealValue) value1).getNumber(),
						((RealValue) value2).getNumber()), exact);
			case LXOR:
				return new RealValue(Type.LONG_TYPE, v1.getValue('^', ((RealValue) value1).getNumber(),
						((RealValue) value2).getNumber()), exact);

			case LCMP: {
				long l1 = ((RealValue<Long>) value1).getValue().longValue();
				long l2 = ((RealValue<Long>) value2).getValue().longValue();
				return new RealValue<Integer>(Type.INT_TYPE, l1 > l2 ? 1 : (l2 < l2 ? -1 : 0), exact);
			}
			case FCMPL:
			case FCMPG: {
				if (exact != RealValue.EXACT_VALUE)
					return new RealValue<Integer>(Type.INT_TYPE, 0, exact);
				float f1 = 0;
				float f2 = 0;
				if (((RealValue) value1).getType().equals(Type.INT_TYPE)) {
					f1 = ((RealValue<Integer>) value1).getValue().intValue();
				} else {
					f1 = ((RealValue<Float>) value1).getValue().floatValue();
				}

				if (((RealValue) value2).getType().equals(Type.INT_TYPE)) {
					f2 = ((RealValue<Integer>) value1).getValue().intValue();
				} else {
					f2 = ((RealValue<Float>) value2).getValue().floatValue();
				}
				if (Float.isNaN(f1) || Float.isNaN(f2)) {
					if (insn.getOpcode() == FCMPL)
						new RealValue<Integer>(Type.INT_TYPE, -1, exact);
					else
						new RealValue<Integer>(Type.INT_TYPE, 1, exact);
				}
				return new RealValue<Integer>(Type.INT_TYPE, f1 > f2 ? 1 : (f2 < f2 ? -1 : 0), exact);
			}
			case DCMPL:
			case DCMPG: {
				double d1 = ((RealValue<Double>) value1).getValue().doubleValue();
				double d2 = ((RealValue<Double>) value2).getValue().doubleValue();
				if (Double.isNaN(d1) || Double.isNaN(d2)) {
					if (insn.getOpcode() == DCMPL)
						new RealValue<Integer>(Type.INT_TYPE, -1, exact);
					else
						new RealValue<Integer>(Type.INT_TYPE, 1, exact);
				}
				return new RealValue<Integer>(Type.INT_TYPE, d1 > d2 ? 1 : (d2 < d2 ? -1 : 0), exact);
			}
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
				FieldNode node = fieldMaps.get(fieldInsn.name);
				if (node != null)// ��������field�ǴӸ���̳����ģ���û�취ģ��˲���������и����class�ļ�������û�취��ȡ����������Щ�򡣼������ԴӸ����м̳е�field����
					node.value = ((RealValue) value2).value;
				return null;
			default:
				throw new Error("Internal error.");
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			RealValue<Exception> v = new RealValue<Exception>(Type.getType(e.getClass()), e);
			v.setName(e.getMessage());
			e.printStackTrace();
			return v;
		} catch (ValueException e) {
			e.printStackTrace();
			// RealValue<Exception> v = new RealValue<Exception>(Type.getType(e
			// .getClass()), e);
			// v.setName(e.getMessage());
			// return v;
			return null;
		}
	}

	// AAStore
	@SuppressWarnings("unchecked")
	public Value ternaryOperation(final AbstractInsnNode insn, final Value value1, final Value value2,
			final Value value3) throws AnalyzerException {
		// int index = ((RealValue<Integer>) value2).getValue().intValue();
		// switch (insn.getOpcode()) {//modified by Meng,Na, because I do not
		// want to take Array into consideration
		// case IASTORE:
		// int[] ints = ((RealValue<int[]>) value1).getValue();
		// ints[index] = ((RealValue<Integer>) value3).getValue().intValue();
		// break;
		// case LASTORE:
		// long[] longs = ((RealValue<long[]>) value1).getValue();
		// longs[index] = ((RealValue<Long>) value3).getValue().longValue();
		// break;
		// case FASTORE:
		// float[] floats = ((RealValue<float[]>) value1).getValue();
		// floats[index] = ((RealValue<Float>) value3).getValue().floatValue();
		// break;
		// case DASTORE:
		// double[] ds = ((RealValue<double[]>) value1).getValue();
		// ds[index] = ((RealValue<Double>) value3).getValue().doubleValue();
		// break;
		// case BASTORE:
		// boolean[] bs = ((RealValue<boolean[]>) value1).getValue();
		// bs[index] = ((RealValue<Boolean>) value3).getValue().booleanValue();
		// break;
		// case CASTORE:
		// char[] cs = ((RealValue<char[]>) value1).getValue();
		// cs[index] = ((RealValue<Character>) value3).getValue().charValue();
		// break;
		// case SASTORE:
		// short[] ss = ((RealValue<short[]>) value1).getValue();
		// ss[index] = ((RealValue<Short>) value3).getValue().shortValue();
		// break;
		// case AASTORE:
		// Object[] os = ((RealValue<Object[]>) value1).getValue();
		// if(index < os.length)
		// os[index] = ((RealValue<Object>) value3).getValue();
		// break;
		// }
		return null;
	}

	/**
	 * The algorithm is similar to Kildall's algorithm. Optimistic assumption:
	 * all variables start at an unknown constant value (top <> bottom).
	 * 
	 * @see org.objectweb.asm.tree.analysis.Interpreter#merge(org.objectweb.asm.tree.analysis.Value,
	 *      org.objectweb.asm.tree.analysis.Value)
	 */
	@SuppressWarnings("unchecked")
	public Value merge(final Value v, final Value w) {

		// if v is null return w
		if (v == null)
			return w;

		// if v is not null and w is null, return v
		if (w == null)
			return v;

		// if v is not null and w is not null
		RealValue vReal = (RealValue) v;
		RealValue wReal = (RealValue) w;
		Type type = null;

		// if vReal's type is null, return wReal's type
		if (vReal.getType().equals(Type.NULL) || vReal.getType().equals(Type.NULL)) {
			return wReal;
		}
		// if vReal's type is not null but wReal's type is null, return vReal's
		// type
		else if (wReal.getType().equals(Type.NULL)
				|| wReal.getType().equals(Type.NULL)) {
			return vReal;
		}

		// if wReal's type is undefine, return undefine type
		else if (vReal.getType().equals(Type.getObjectType("Lundefine"))) {
			return vReal;
		}
		// if vReal's type is not undefine, but wReal's type is undefine, return
		// wReal's type
		else if (wReal.getType().equals(Type.getObjectType("undefine"))) {
			return wReal;
		}
		// both are nonnull but their types are not equal
		else if (!vReal.getType().equals(wReal.getType())) {
			RealValue result = null;
			if (vReal.getType().getSort() <= Type.DOUBLE && wReal.getType().getSort() <= Type.DOUBLE) {
				if (vReal.getType().getSort() < wReal.getType().getSort()) {
					result = (RealValue) newValue(wReal.getType());
					type = wReal.getType();
				} else {
					result = (RealValue) newValue(vReal.getType());
					type = vReal.getType();
				}
				result.exactValue = RealValue.UNKNOW_VALUE;
				int exact = vReal.exactValue & wReal.exactValue;
				if (exact == RealValue.EXACT_VALUE) {
					if (vReal.value == null && wReal.value == null) {
						result.value = null;
						result.exactValue = exact;
					} else if (vReal.value == null) {
						// do nothing, since the result is not exact and its
						// value should be a default value
					} else if (wReal.value == null) {
						// do nothing, since the result is not exact and its
						// value should be a default value
					} else if (vReal.value.equals(wReal.value)) {
						result.value = wReal.value;
						result.exactValue = exact;
					} else {// do not equal to each other
						// do nothing
					}
				} else {// either of the operands is not exact
					// do nothing
				}
				return result;
			} else {
				return new RealValue<Object>(Type.getObjectType("undefine"), null);
			}
		} else {// they have equal type
			type = vReal.getType();
			RealValue result = (RealValue) newValue(type);
			result.exactValue = RealValue.UNKNOW_VALUE;
			int exact = vReal.exactValue & wReal.exactValue;

			if (exact == RealValue.EXACT_VALUE) {
				if (vReal.value == null && wReal.value == null) {
					result.value = null;
					result.exactValue = exact;
				} else if (vReal.value == null) {
					// do nothing, since the result is not exact and its value
					// should be a default value
				} else if (wReal.value == null) {
					// do nothing, since the result is not exact and its value
					// should be a default value
				} else if (vReal.value.equals(wReal.value)) {
					result.value = wReal.value;
					result.exactValue = exact;
				} else {// do not equal to each other
					// do nothing
				}
			} else {
				// do nothing
			}
			return result;
			// if(exact == RealValue.EXACT_VALUE && (vReal.getValue() ==
			// wReal.getValue() || vReal.getValue().equals(wReal.getValue()))){
			// return vReal;
			// }else{
			// RealValue result = (RealValue) newValue(type);
			// result.exactValue = RealValue.UNKNOW_VALUE;
			// return result;
			// }
		}

		// RealValue vReal=(RealValue)v;
		// RealValue wReal=(RealValue)w;
		// if(vReal.getType()==null
		// ||vReal.getType().equals(Type.getType("Lnull"))){
		// return new
		// RealValue<Object>(vReal.getType(),vReal.getValue(),vReal.var,vReal.exactValue);
		// }
		// if(wReal.getType()==null||wReal.getType().equals(Type.getType("Lnull"))){
		// return new
		// RealValue<Object>(vReal.getType(),vReal.getValue(),vReal.exactValue);
		// }

		// try {
		// Class vClass=Class.forName(vReal.getType().getClassName());
		// Class wClass=Class.forName(wReal.getType().getClassName());
		// vClass.asSubclass(wClass)
		// } catch (ClassNotFoundException e) {
		// e.printStackTrace();
		// }
		//    	
		// if (!v.equals(w)) {
		// return new RealValue<Object>(Type.getObjectType("Lnull"),null);
		// }
		// return v;
	}

	/**
	 * change this method anyway. remove the code of compute localvariablemap
	 * 
	 * @see org.objectweb.asm.tree.analysis.Interpreter#copyOperation(org.objectweb.asm.tree.AbstractInsnNode,
	 *      org.objectweb.asm.tree.analysis.Value)
	 */
	public Value copyOperation(final AbstractInsnNode insn, final Value value) throws AnalyzerException {
		int maxStart = -1;
		int minEnd = Integer.MAX_VALUE;
		Object tempNode = null;

		if (insn.getType() == AbstractInsnNode.VAR_INSN) {
			((RealValue) value).setVar(((VarInsnNode) insn).var);
		}

		RealValue result = (RealValue) newRealValue(value);
		switch (insn.getOpcode()) {
		case Opcodes.ILOAD:
		case Opcodes.LLOAD:
		case Opcodes.FLOAD:
		case Opcodes.DLOAD:
		case Opcodes.ALOAD:
			int lvIndex = ((VarInsnNode) insn).var;
			int insnIndex = insn.index;
			List lvList = localVariableMap.get(lvIndex);// lvListӦ�ô���Ǹ��ֲ������ￄ1�7
			// ////why
			if (lvList == null || lvList.size() == 0)
				break;
			maxStart = -1;
			minEnd = Integer.MAX_VALUE;
			tempNode = null; // ��Ϊ�洢����ƥ���LocalVariableNode
			for (int i = 0; i < lvList.size(); i++) {
				Object node = lvList.get(i);// ����ƥ��ֲ����ￄ1�7
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
				throw new AnalyzerException("We cannot find any local variable to aload");
			} else {
				((RealValue) result).setVarObject((LocalVariableNode) tempNode);
				switch (insn.getOpcode()) {
				case Opcodes.ILOAD:
					((RealValue) result).setType(Type.INT_TYPE);
					break;
				case Opcodes.LLOAD:
					((RealValue) result).setType(Type.LONG_TYPE);
					break;
				case Opcodes.FLOAD:
					((RealValue) result).setType(Type.FLOAT_TYPE);
					break;
				case Opcodes.DLOAD:
					((RealValue) result).setType(Type.DOUBLE_TYPE);
					break;
				case Opcodes.ALOAD:
					break;// do nothing temporarily
				}
			}
			break;
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
				Object node = lvList.get(i);// ����ƥ��ֲ����ￄ1�7
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
				RealValue rv = (RealValue) value;
				if (rv.getVarObject() != null && rv.getVarObject() instanceof LocalVariableNode) {
					LocalVariableNode tempLvNode = (LocalVariableNode) rv.getVarObject();
					LocalVariableNode local = newLocalVariableNode(tempLvNode);
					local.index = lvIndex;
					local.name = "";

					LabelNode lNode = new LabelNode(new Label());// ������������ı���������Ϊstart����ǰindex��end��insns.size()
					lNode.index = insns.size();
					local.end = lNode;
					// local.name = tempLvNode.name + " ";
					lvList.add(local);
				} else {
					LabelNode lNode = new LabelNode(new Label());
					lNode.index = insns.size();// ����һ��label��Ϊ�˱�ʶLocalVariableNode���ڵķ�Χ
					LocalVariableNode local = new LocalVariableNode("", rv.getType().toString(), null,
							lookForNearestLabelAbove(insn.index), lNode, lvIndex);
					lvList.add(local);
				}
				localVariableMap.put(lvIndex, lvList);
			} else {// tempNode is not null, we should compare the type of
				// typeNode and that of value, sometimes these types are
				// different
				LocalVariableNode tempLvNode = (LocalVariableNode) tempNode;
				Type typeOfTempLvNode = Type.getType(tempLvNode.desc);
				Type typeOfValue = ((RealValue) value).getType();
				if (!typeOfTempLvNode.equals(typeOfValue)) {
					result.setType(typeOfTempLvNode);
				}
			}
			break;
		}
		return result;
	}

	public Map getLocalVariableMap() {
		return localVariableMap;
	}

	public Value naryOperation(final AbstractInsnNode insn, final List values) throws AnalyzerException {
		switch (insn.getOpcode()) {
		case MULTIANEWARRAY:
			return newValue(Type.getType(((MultiANewArrayInsnNode) insn).desc));
		case INVOKEVIRTUAL:
		case INVOKESPECIAL:
		case INVOKESTATIC:
		case INVOKEINTERFACE:
			return newValue(Type.getReturnType(((MethodInsnNode) insn).desc));
		}
		assert (insn.getOpcode() < 0);
		return null;
	}

	private LabelNode lookForNearestLabelAbove(int insnIndex) throws AnalyzerException {
		if (insnIndex >= insns.size())
			insnIndex = insns.size() - 1;
		for (int i = insnIndex; i >= 0; i--) {
			if (insns.get(i) instanceof LabelNode) {
				return (LabelNode) insns.get(i);
			}
		}
		throw new AnalyzerException("There is no label which can apply to the situation");
	}

	private LocalVariableNode newLocalVariableNode(LocalVariableNode tempLvNode) {
		LocalVariableNode local = new LocalVariableNode(tempLvNode.name, tempLvNode.desc, null, tempLvNode.start,
				tempLvNode.end, tempLvNode.index);
		return local;
	}
}

// end

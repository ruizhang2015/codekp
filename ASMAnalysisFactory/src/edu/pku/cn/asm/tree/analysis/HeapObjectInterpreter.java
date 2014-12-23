/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-4-23 ����10:00:37
 * @modifier: Administrator
 * @time 2009-4-23 ����10:00:37
 * @reviewer: Administrator
 * @time 2009-4-23 ����10:00:37
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.asm.tree.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Frame;
import org.objectweb.asm.tree.analysis.Interpreter;
import org.objectweb.asm.tree.analysis.Value;

import edu.pku.cn.analysis.ObjectInvokeMethodAnalysis;
import edu.pku.cn.util.OpcodeUtil;

import autoAdapter.ExecuteMethod;
import autoAdapter.HeapObject;
import autoAdapter.MethodSummaryUtil;
import autoAdapter.ProgramLocation;

public class HeapObjectInterpreter implements Opcodes, Interpreter {

	private static final boolean DEBUG = false;
	private HashMap<LocalVariableNode, HeapObject> localVar2ho = new HashMap<LocalVariableNode, HeapObject>();
	private InsnList insns;
	private ObjectInvokeMethodAnalysis oimAnalysis;
	public String className = "";
	public String methodName = "";
	public String methodDesc = "";

	public InsnList getInsns() {
		return insns;
	}

	public void setInsns(InsnList insns) {
		this.insns = insns;
		// OpcodeUtil.printInsnList(insns, this.className+"."+this.methodDesc);
	}

	public void setOimAnalysis(ObjectInvokeMethodAnalysis oimAnalysis) {
		this.oimAnalysis = oimAnalysis;
	}

	public HeapObjectInterpreter(List<FieldNode> fields) {
		HeapObject ho;
		// for(int i = 0; i < fields.size(); i ++){
		// FieldNode fNode = fields.get(i);
		// String desc = fNode.desc;
		// Type type = Type.getType(desc);
		// // to decide the type and IsNullValue of the field
		// try{
		// if(type == null){
		// throw new AnalyzerException("This is an impossible type of a field");
		// }
		// switch(type.getSort()){
		// case Type.INT:
		// case Type.BOOLEAN:
		// case Type.CHAR:
		// case Type.FLOAT:
		// case Type.DOUBLE:
		// case Type.BYTE:
		// case Type.SHORT:
		// case Type.LONG:
		// ho = HeapObject.Basic;
		// break;
		// default:
		// ho = HeapObject.BasicFieldDefault;
		// break;
		// }

		// }catch (AnalyzerException e) {
		// e.printStackTrace();
		// }
		// }
	}

	public HeapObjectInterpreter() {
	}

	@Override
	public Value binaryOperation(AbstractInsnNode insn, Value value1, Value value2) throws AnalyzerException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
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
			return HeapObject.BasicInt;
		case FALOAD:
		case FADD:
		case FSUB:
		case FMUL:
		case FDIV:
		case FREM:
			return HeapObject.BasicFloat;
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
			return HeapObject.BasicLong;
		case DALOAD:
		case DADD:
		case DSUB:
		case DMUL:
		case DDIV:
		case DREM:
			return HeapObject.BasicDouble;
		case AALOAD:
			// ��ʱ��δ����
			return newValue(null);
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
			HeapObject h1 = (HeapObject) value1;
			HeapObject h2 = (HeapObject) value2;
			h1.referenceFields.put(fieldInsn.name, h2);
			return null;
		default:
			throw new Error("Internal error.");
		}
	}

	@Override
	public Value copyOperation(AbstractInsnNode insn, Value value) throws AnalyzerException {
		return value;
	}

	@Override
	public Value merge(Value v, Value w) {
		// TODO Auto-generated method stub
		// ////******************************
		// ////��ʱ������
		return v;
	}

	@Override
	public Value naryOperation(AbstractInsnNode insn, List values) throws AnalyzerException {
		// TODO Auto-generated method stub
		switch (insn.getOpcode()) {
		case MULTIANEWARRAY:
			return HeapObject.BasicDontcare;
		case INVOKEVIRTUAL:
		case INVOKESPECIAL:
		case INVOKESTATIC:
		case INVOKEINTERFACE:
			MethodInsnNode mInsn = (MethodInsnNode) insn;
			if (DEBUG) {
				System.out.println("invoke:" + mInsn.owner + "." + mInsn.name + mInsn.desc);
			}
			if (!MethodSummaryUtil.isMethodConcern(mInsn.owner + "." + mInsn.name + mInsn.desc)) {
				if (DEBUG)
					System.out.println("dnot concern!!!");

				Type dt = Type.getReturnType(mInsn.desc);
				if (dt.getSize() == 1)
					return HeapObject.BasicDontcare;
				else
					return HeapObject.BasicDouble;
			}
			String returnType = Type.getReturnType(mInsn.desc).toString();

			ExecuteMethod e = new ExecuteMethod();
			e.methodDesc = mInsn.desc;
			e.methodName = mInsn.name;
			e.methodOwner = mInsn.owner.replace('/', '.');

			ProgramLocation pl = new ProgramLocation(this.className, this.methodName, this.methodDesc, mInsn.index,
					oimAnalysis.insnIndex2line[mInsn.index]);
			e.executePosition = pl;
			if (Opcodes.INVOKESTATIC == insn.getOpcode()) {
				e.isStatic = true;
				HeapObject sho;
				if (this.oimAnalysis.staticFields.containsKey(mInsn.owner)) {
					sho = this.oimAnalysis.staticFields.get(mInsn.owner);
				} else {
					sho = newValue(mInsn.index, mInsn.desc, HeapObject.DescStaticField + mInsn.owner);
					this.oimAnalysis.staticFields.put(mInsn.owner, sho);
				}
				values.add(0, sho);
			} else {
				e.isStatic = false;
			}
			for (Object p : values) {
				if (p != null && p instanceof HeapObject) {
					HeapObject hp = (HeapObject) p;
					e.parameters.add(hp);
				} else
					e.parameters.add(HeapObject.BasicNull);

			}

			this.oimAnalysis.eMethods[insn.index].add(e);
			this.oimAnalysis.allExecuteMethods.add(e);
			// System.out.println(e);
			HeapObject ho = newValue(pl, returnType, HeapObject.DescReturn);
			return ho;

			// if(Type.getObjectType(mInsn.owner).equals("java.lang.String")&&
			// Type.getReturnType(mInsn.desc).equals("java.lang.String")){
			// //by default, the "String" return value of String methods cannot
			// be null
			// return newValue(IsNullValue.DEFINITE_NONNULL, (mInsn.owner));
			// }
			// return newValue(IsNullValue.UNKNOWN, (mInsn.desc));
		}
		return null;
	}

	@Override
	public Value newOperation(AbstractInsnNode insn) throws AnalyzerException {
		// TODO Auto-generated method stub
		switch (insn.getOpcode()) {
		case ACONST_NULL:
			return HeapObject.BasicNull;
		case ICONST_M1:
		case ICONST_0:
		case ICONST_1:
		case ICONST_2:
		case ICONST_3:
		case ICONST_4:
		case ICONST_5:
			return HeapObject.BasicInt;
		case LCONST_0:
		case LCONST_1:
			return HeapObject.BasicLong;
		case FCONST_0:
		case FCONST_1:
		case FCONST_2:
			return HeapObject.BasicFloat;
		case DCONST_0:
		case DCONST_1:
			return HeapObject.BasicDouble;
		case BIPUSH:
		case SIPUSH:
			return HeapObject.BasicInt;
		case LDC:
			Object cst = ((LdcInsnNode) insn).cst;
			if (cst instanceof Integer) {
				return HeapObject.BasicInt;
			} else if (cst instanceof Float) {
				return HeapObject.BasicFloat;
			} else if (cst instanceof Long) {
				return HeapObject.BasicLong;
			} else if (cst instanceof Double) {
				return HeapObject.BasicDouble;
			} else {
				return HeapObject.BasicDontcare;
			}
		case JSR:
			return HeapObject.BasicDontcare;
		case GETSTATIC:
			if (insn instanceof FieldInsnNode) {
				HeapObject ho;
				FieldInsnNode fieldInsn = (FieldInsnNode) insn;
				if (this.oimAnalysis.staticFields.containsKey(fieldInsn.owner + "." + fieldInsn.name)) {
					return this.oimAnalysis.staticFields.get(fieldInsn.owner + "." + fieldInsn.name);
				} else {
					ho = newValue(fieldInsn.index, fieldInsn.desc, HeapObject.DescStaticField + fieldInsn.name);
					this.oimAnalysis.staticFields.put(fieldInsn.owner + "." + fieldInsn.name, ho);
					return ho;
				}
			} else
				return HeapObject.BasicDontcare;
		case NEW:
			return newValue(insn.index, ((TypeInsnNode) insn).desc, HeapObject.DescNew);
		default:
			throw new Error("Internal error.");
		}

	}

	public HeapObject newValue(ProgramLocation createLocation, String classType, String desc) {
		classType = classType.replace('/', '.');
		// if(! MethodSummaryUtil.isClassConcern(classType))
		// return HeapObject.BasicDontcare;

		Integer createNth = this.oimAnalysis.newObjectTimesMap.get(createLocation);
		if (createNth == null)
			createNth = 1;
		this.oimAnalysis.newObjectTimesMap.put(createLocation, createNth + 1);
		HeapObject ho = new HeapObject(createLocation, createNth, classType, desc);
		this.oimAnalysis.heapObjects.add(ho);
		return ho;
	}

	public HeapObject newValue(int insnIndex, String classType, String desc) {
		// if(classType!=null && classType.length()>0 &&
		// classType.startsWith("L")&&classType.endsWith(";"))
		return newValue(new ProgramLocation(this.className, this.methodName, this.methodDesc, insnIndex,
				this.oimAnalysis.insnIndex2line[insnIndex]), classType, desc);

	}

	@Override
	public HeapObject newValue(Type type) {
		// TODO Auto-generated method stub
		if (type == null) {
			return HeapObject.BasicNull;
		}
		switch (type.getSort()) {
		case Type.VOID:
			return HeapObject.BasicVoid;
		case Type.BOOLEAN:
			return HeapObject.BasicBoolean;
		case Type.CHAR:
			return HeapObject.BasicChar;
		case Type.BYTE:
			return HeapObject.BasicByte;
		case Type.SHORT:
			return HeapObject.BasicShort;
		case Type.INT:
			return HeapObject.BasicInt;
		case Type.FLOAT:
			return HeapObject.BasicFloat;
		case Type.LONG:
			return HeapObject.BasicLong;
		case Type.DOUBLE:
			return HeapObject.BasicDouble;
		case Type.ARRAY:
		case Type.OBJECT:
			return newValue(0, type.toString(), "newValue");
		default:
			throw new Error("Internal error");
		}
	}

	@Override
	public Value ternaryOperation(AbstractInsnNode insn, Value value1, Value value2, Value value3)
			throws AnalyzerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value unaryOperation(AbstractInsnNode insn, Value value) throws AnalyzerException {
		// TODO Auto-generated method stub
		switch (insn.getOpcode()) {
		case INEG:
		case IINC:
		case L2I:
		case F2I:
		case D2I:
		case I2B:
		case I2C:
		case I2S:
			return HeapObject.BasicInt;
		case FNEG:
		case I2F:
		case L2F:
		case D2F:
			return HeapObject.BasicFloat;
		case LNEG:
		case I2L:
		case F2L:
		case D2L:
			return HeapObject.BasicLong;
		case DNEG:
		case I2D:
		case L2D:
		case F2D:
			return HeapObject.BasicDouble;
		case IFEQ:
		case IFNE:
		case IFLT:
		case IFGE:
		case IFGT:
		case IFLE:
		case TABLESWITCH:
		case LOOKUPSWITCH:
			return null;
		case IRETURN:
		case LRETURN:
		case FRETURN:
		case DRETURN:
			return HeapObject.BasicDontcare;
		case ARETURN:
			this.oimAnalysis.returnObject.add((HeapObject) value);
			return value;
		case PUTSTATIC:
			if (insn instanceof FieldInsnNode) {
				FieldInsnNode fieldInsn0 = (FieldInsnNode) insn;
				if (value != null)
					this.oimAnalysis.staticFields.put(fieldInsn0.owner + "." + fieldInsn0.name, (HeapObject) value);
			}
			return null;
		case GETFIELD:
			FieldInsnNode fieldInsn = (FieldInsnNode) insn;
			HeapObject ho;
			Frame f = this.oimAnalysis.getFact(fieldInsn.index);
			HeapObject hh = (HeapObject) f.getStack(f.getStackSize() - 1);
			if (hh.referenceFields.containsKey(fieldInsn.name)) {
				return hh.referenceFields.get(fieldInsn.name);
			} else {
				ho = newValue(fieldInsn.index, fieldInsn.desc, HeapObject.DescInstanceField + fieldInsn.name);
				hh.referenceFields.put(fieldInsn.name, ho);
			}

			return ho;
		case NEWARRAY:
			switch (((IntInsnNode) insn).operand) {
			case T_BOOLEAN:
				// return newValue(IsNullValue.DEFINITE_NONNULL,
				// Type.getType("[Z"));
			case T_CHAR:
				// return newValue(IsNullValue.DEFINITE_NONNULL,
				// Type.getType("[C"));
			case T_BYTE:
				// return newValue(IsNullValue.DEFINITE_NONNULL,
				// Type.getType("[B"));
			case T_SHORT:
				// return newValue(IsNullValue.DEFINITE_NONNULL,
				// Type.getType("[S"));
			case T_INT:
				// return newValue(IsNullValue.DEFINITE_NONNULL,
				// Type.getType("[I"));
			case T_FLOAT:
				// return newValue(IsNullValue.DEFINITE_NONNULL,
				// Type.getType("[F"));
			case T_DOUBLE:
				// return newValue(IsNullValue.DEFINITE_NONNULL,
				// Type.getType("[T"));
			case T_LONG:
				// return newValue(IsNullValue.DEFINITE_NONNULL,
				// Type.getType("[J"));
				return HeapObject.BasicDontcare;
			default:
				throw new AnalyzerException("Invalid array type when analyzing IsNullValue");

			}
		case ANEWARRAY:
			String desc = ((TypeInsnNode) insn).desc;
			return HeapObject.BasicDontcare;
		case ARRAYLENGTH:
			return HeapObject.BasicInt;
		case ATHROW:
			return null;
		case CHECKCAST:
			return newValue(insn.index, ((TypeInsnNode) insn).desc, "checkcast");
		case INSTANCEOF:
			return HeapObject.BasicInt;
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

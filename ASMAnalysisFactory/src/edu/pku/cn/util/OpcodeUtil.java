/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author MENG Na
 * @time 2008-11-14 ����07:39:25
 * @modifier: MENG Na
 * @time 2008-11-14 ����07:39:25
 * @reviewer: MENG Na
 * @time 2008-11-14 ����07:39:25
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.util;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;

public class OpcodeUtil {
	static String OPName[] = { "NOP",// 0; visitInsn
			"ACONST_NULL",// 1; -
			"ICONST_M1",// 2; -
			"ICONST_0",// 3; -
			"ICONST_1",// 4; -
			"ICONST_2",// 5; -
			"ICONST_3",// 6; -
			"ICONST_4",// 7; -
			"ICONST_5",// 8; -
			"LCONST_0",// 9; -
			"LCONST_1",// 10; -
			"FCONST_0",// 11; -
			"FCONST_1",// 12; -
			"FCONST_2",// 13; -
			"DCONST_0",// 14; -
			"DCONST_1",// 15; -
			"BIPUSH",// 16; visitIntInsn
			"SIPUSH",// 17; -
			"LDC",// 18; visitLdcInsn
			"LDC_W",// 19; -
			"LDC2_W",// 20; -
			"ILOAD",// 21; visitVarInsn
			"LLOAD",// 22; -
			"FLOAD",// 23; -
			"DLOAD",// 24; -
			"ALOAD",// 25; -
			"ILOAD_0",// 26; -
			"ILOAD_1",// 27; -
			"ILOAD_2",// 28; -
			"ILOAD_3",// 29; -
			"LLOAD_0",// 30; -
			"LLOAD_1",// 31; -
			"LLOAD_2",// 32; -
			"LLOAD_3",// 33; -
			"FLOAD_0",// 34; -
			"FLOAD_1",// 35; -
			"FLOAD_2",// 36; -
			"FLOAD_3",// 37; -
			"DLOAD_0",// 38; -
			"DLOAD_1",// 39; -
			"DLOAD_2",// 40; -
			"DLOAD_3",// 41; -
			"ALOAD_0",// 42; -
			"ALOAD_1",// 43; -
			"ALOAD_2",// 44; -
			"ALOAD_3",// 45; -
			"IALOAD",// 46; visitInsn
			"LALOAD",// 47; -
			"FALOAD",// 48; -
			"DALOAD",// 49; -
			"AALOAD",// 50; -
			"BALOAD",// 51; -
			"CALOAD",// 52; -
			"SALOAD",// 53; -
			"ISTORE",// 54; visitVarInsn
			"LSTORE",// 55; -
			"FSTORE",// 56; -
			"DSTORE",// 57; -
			"ASTORE",// 58; -
			"ISTORE_0",// 59; -
			"ISTORE_1",// 60; -
			"ISTORE_2",// 61; -
			"ISTORE_3",// 62; -
			"LSTORE_0",// 63; -
			"LSTORE_1",// 64; -
			"LSTORE_2",// 65; -
			"LSTORE_3",// 66; -
			"FSTORE_0",// 67; -
			"FSTORE_1",// 68; -
			"FSTORE_2",// 69; -
			"FSTORE_3",// 70; -
			"DSTORE_0",// 71; -
			"DSTORE_1",// 72; -
			"DSTORE_2",// 73; -
			"DSTORE_3",// 74; -
			"ASTORE_0",// 75; -
			"ASTORE_1",// 76; -
			"ASTORE_2",// 77; -
			"ASTORE_3",// 78; -
			"IASTORE",// 79; visitInsn
			"LASTORE",// 80; -
			"FASTORE",// 81; -
			"DASTORE",// 82; -
			"AASTORE",// 83; -
			"BASTORE",// 84; -
			"CASTORE",// 85; -
			"SASTORE",// 86; -
			"POP",// 87; -
			"POP2",// 88; -
			"DUP",// 89; -
			"DUP_X1",// 90; -
			"DUP_X2",// 91; -
			"DUP2",// 92; -
			"DUP2_X1",// 93; -
			"DUP2_X2",// 94; -
			"SWAP",// 95; -
			"IADD",// 96; -
			"LADD",// 97; -
			"FADD",// 98; -
			"DADD",// 99; -
			"ISUB",// 100; -
			"LSUB",// 101; -
			"FSUB",// 102; -
			"DSUB",// 103; -
			"IMUL",// 104; -
			"LMUL",// 105; -
			"FMUL",// 106; -
			"DMUL",// 107; -
			"IDIV",// 108; -
			"LDIV",// 109; -
			"FDIV",// 110; -
			"DDIV",// 111; -
			"IREM",// 112; -
			"LREM",// 113; -
			"FREM",// 114; -
			"DREM",// 115; -
			"INEG",// 116; -
			"LNEG",// 117; -
			"FNEG",// 118; -
			"DNEG",// 119; -
			"ISHL",// 120; -
			"LSHL",// 121; -
			"ISHR",// 122; -
			"LSHR",// 123; -
			"IUSHR",// 124; -
			"LUSHR",// 125; -
			"IAND",// 126; -
			"LAND",// 127; -
			"IOR",// 128; -
			"LOR",// 129; -
			"IXOR",// 130; -
			"LXOR",// 131; -
			"IINC",// 132; visitIincInsn
			"I2L",// 133; visitInsn
			"I2F",// 134; -
			"I2D",// 135; -
			"L2I",// 136; -
			"L2F",// 137; -
			"L2D",// 138; -
			"F2I",// 139; -
			"F2L",// 140; -
			"F2D",// 141; -
			"D2I",// 142; -
			"D2L",// 143; -
			"D2F",// 144; -
			"I2B",// 145; -
			"I2C",// 146; -
			"I2S",// 147; -
			"LCMP",// 148; -
			"FCMPL",// 149; -
			"FCMPG",// 150; -
			"DCMPL",// 151; -
			"DCMPG",// 152; -
			"IFEQ",// 153; visitJumpInsn
			"IFNE",// 154; -
			"IFLT",// 155; -
			"IFGE",// 156; -
			"IFGT",// 157; -
			"IFLE",// 158; -
			"IF_ICMPEQ",// 159; -
			"IF_ICMPNE",// 160; -
			"IF_ICMPLT",// 161; -
			"IF_ICMPGE",// 162; -
			"IF_ICMPGT",// 163; -
			"IF_ICMPLE",// 164; -
			"IF_ACMPEQ",// 165; -
			"IF_ACMPNE",// 166; -
			"GOTO",// 167; -
			"JSR",// 168; -
			"RET",// 169; visitVarInsn
			"TABLESWITCH",// 170; visiTableSwitchInsn
			"LOOKUPSWITCH",// 171; visitLookupSwitch
			"IRETURN",// 172; visitInsn
			"LRETURN",// 173; -
			"FRETURN",// 174; -
			"DRETURN",// 175; -
			"ARETURN",// 176; -
			"RETURN",// 177; -
			"GETSTATIC",// 178; visitFieldInsn
			"PUTSTATIC",// 179; -
			"GETFIELD",// 180; -
			"PUTFIELD",// 181; -
			"INVOKEVIRTUAL",// 182; visitMethodInsn
			"INVOKESPECIAL",// 183; -
			"INVOKESTATIC",// 184; -
			"INVOKEINTERFACE",// 185; -
			"UNUSED",// 186; NOT VISITED
			"NEW",// 187; visitTypeInsn
			"NEWARRAY",// 188; visitIntInsn
			"ANEWARRAY",// 189; visitTypeInsn
			"ARRAYLENGTH",// 190; visitInsn
			"ATHROW",// 191; -
			"CHECKCAST",// 192; visitTypeInsn
			"INSTANCEOF",// 193; -
			"MONITORENTER",// 194; visitInsn
			"MONITOREXIT",// 195; -
			"WIDE",// 196; NOT VISITED
			"MULTIANEWARRAY",// 197; visitMultiANewArrayInsn
			"IFNULL",// 198; visitJumpInsn
			"IFNONNULL",// 199; -
			"GOTO_W",// 200; -
			"JSR_W"// 201; -
	};

	public static String printOpcode(int opcode) {
		return OPName[opcode];
	}

	public static String printVersion(int version) {
		int i = 0;
		switch (version) {
		case Opcodes.V1_6:
			i++;
		case Opcodes.V1_5:
			i++;
		case Opcodes.V1_4:
			i++;
		case Opcodes.V1_3:
			i++;
		case Opcodes.V1_2:
			i++;
		case Opcodes.V1_1:
			i++;
		}
		return "//class version Java:1." + i;
	}

	private static String printAllUsed(int access) {
		String s = "";
		if ((access & Opcodes.ACC_PUBLIC) > 0)
			s += "public ";
		else if ((access & Opcodes.ACC_PRIVATE) > 0)
			s += "private ";
		else if ((access & Opcodes.ACC_PROTECTED) > 0)
			s += "protected ";
		else if (((access & Opcodes.ACC_FINAL) > 0))
			s += "final ";
		else if (((access & Opcodes.ACC_SYNTHETIC) > 0))
			s += "synthetic ";
		return s;
	}

	public static String printMethodAccess(int access) {
		String s = printAllUsed(access);
		if (((access & Opcodes.ACC_SYNCHRONIZED) > 0))
			s += "synchronized ";
		if (((access & Opcodes.ACC_BRIDGE) > 0))
			s += "bridge ";
		if (((access & Opcodes.ACC_VARARGS) > 0))
			s += "varargs ";
		if (((access & Opcodes.ACC_NATIVE) > 0))
			s += "native ";
		if (((access & Opcodes.ACC_ABSTRACT) > 0))
			s += "abstract ";
		if (((access & Opcodes.ACC_STRICT) > 0))
			s += "strict ";
		return s;
	}
	public static boolean isSynthetic(int access){
		return (access&Opcodes.ACC_SYNTHETIC)>0;
	}
	public static boolean isStatic(int access){
		return (access&Opcodes.ACC_STATIC)>0;
	}
	public static boolean isNative(int access){
		return (access&Opcodes.ACC_NATIVE)>0;
	}
	public static boolean isPrivate(int access){
		return (access&Opcodes.ACC_PRIVATE)>0;
	}
	public static boolean isPublic(int access){
		return (access&Opcodes.ACC_PUBLIC)>0;
	}
	public static boolean isProtected(int access){
		return (access&Opcodes.ACC_PROTECTED)>0;
	}
	
	public static boolean isAbstract(int access){
		return (access&Opcodes.ACC_ABSTRACT)>0;
	}
	public static boolean isBridge(int access){
		return (access&Opcodes.ACC_BRIDGE)>0;
	}
	public static boolean isStore(int opcode){
		return opcode>=Opcodes.ISTORE && opcode<=Opcodes.SASTORE;
	}
	public static String printFieldAccess(int access) {
		String s = printAllUsed(access);
		if (((access & Opcodes.ACC_STATIC) > 0))
			s += "static ";
		if (((access & Opcodes.ACC_VOLATILE) > 0))
			s += "volatile ";
		if (((access & Opcodes.ACC_TRANSIENT) > 0))
			s += "transient ";
		return s;
	}

	public static String printClassAccess(int access) {
		String s = printAllUsed(access);
		if (((access & Opcodes.ACC_SUPER) > 0))
			s += "super ";
		if (((access & Opcodes.ACC_INTERFACE) > 0))
			s += "interface ";
		else if (((access & Opcodes.ACC_ANNOTATION) > 0))
			s += "annotation ";
		else if ((access & Opcodes.ACC_ENUM) > 0)
			s += "enum ";
		else
			s += "class ";
		if (((access & Opcodes.ACC_ABSTRACT) > 0))
			s += "abstract ";
		return s;
	}

	public static void printInsnList(InsnList insns, String name) {
		StringBuffer buf = new StringBuffer("The followings are insns in the list of method " + name + " \n");
		for (int i = 0; i < insns.size(); i++) {
			int opcode = insns.get(i).getOpcode();
			if (opcode == -1) {
				buf.append(Integer.toString(i));
				if (insns.get(i) instanceof LabelNode) {
					buf.append("   LABEL");
				} else if (insns.get(i) instanceof LineNumberNode) {
					buf.append("   LineNumber   " + ((LineNumberNode) insns.get(i)).line);
				} else if (insns.get(i) instanceof FrameNode) {
					buf.append("   Frame");
				}
			} else {
				buf.append(Integer.toString(i) + printOpcode(opcode));
				if (insns.get(i) instanceof JumpInsnNode) {
					buf.append("   " + Integer.toString(((JumpInsnNode) insns.get(i)).label.index));
				} else if (insns.get(i) instanceof VarInsnNode) {
					buf.append("   " + ((VarInsnNode) (insns.get(i))).var);
				}
			}

			buf.append("\n");
		}
		System.out.println(buf.toString());
	}

	public static boolean isExceptionThrow(int opcode) {
		// LDC
		if (opcode == 18)
			return true;
		// IALOAD, FALOAD, DALOAD, AALOAD, BALOAD, CALOAD
		if (opcode == 46 || (opcode >= 48 && opcode <= 52))
			return true;
		// IASTORE, FASTORE, DASTORE, AASTORE, BASTORE, CASTORE
		if (opcode == 79 || (opcode >= 81 && opcode <= 85))
			return true;
		// IDIV, LDIV
		if (opcode == 108 || opcode == 109)
			return true;
		// IREM, LREM
		if (opcode == 112 || opcode == 113)
			return true;
		// xRETURN
		if (opcode >= 172 && opcode <= 177)
			return true;
		// (GET)PUTSTATIC, (GET)PUTFIELD
		if (opcode >= 178 && opcode <= 181)
			return true;
		// INVOKExxx
		if (opcode >= 182 && opcode <= 185)
			return true;
		// NEW, NEWARRAY, ANEWARRAY, ARRAYLENGTH, ATHROW, CHECKCAST, INSTANCEOF,
		// MONITORENTER, MONITOREXIT, MULTIANEWARRAY
		if (opcode >= 187 && opcode <= 195 || opcode == 197)
			return true;
		return false;
	}

	public static int lookForNearestLineNumberAbove(InsnList insns, int insnIndex) throws AnalyzerException {
		int line = -1;
		for (int i = insnIndex; i >= 0; i--) {
			if (insns.get(i) instanceof LineNumberNode) {
				LineNumberNode lnNode = (LineNumberNode) insns.get(i);
				line = lnNode.line;
				break;
			}
		}
		return line;
	}
}

// end

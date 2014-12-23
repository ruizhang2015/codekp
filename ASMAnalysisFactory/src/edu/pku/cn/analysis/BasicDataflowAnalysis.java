/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-1-9 ����03:58:41
 * @modifier: Administrator
 * @time 2009-1-9 ����03:58:41
 * @reviewer: Administrator
 * @time 2009-1-9 ����03:58:41
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.analysis;

import java.util.IdentityHashMap;
import java.util.Iterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import edu.pku.cn.graph.cfg.BasicBlock;
import edu.pku.cn.graph.cfg.Edge;
import edu.pku.cn.graph.visit.BlockOrder;

public class BasicDataflowAnalysis<Fact> implements DataflowAnalysis<Fact> {

	// protected String owner;
	// protected CFG cfg;
	// // protected Interpreter interpreter;
	protected BlockOrder<Edge, BasicBlock> blockOrder;
	protected boolean isForwards;
	//	
	// protected InsnList insns;
	// protected MethodNode m;
	protected Fact[] facts;
	// protected Subroutine[] subroutines;
	protected IdentityHashMap<BasicBlock, Fact> startFactMap;
	protected IdentityHashMap<BasicBlock, Fact> resultFactMap;

	// protected List<TryCatchBlockNode>[] handlers;
	public BasicDataflowAnalysis() {
		this.startFactMap = new IdentityHashMap<BasicBlock, Fact>();
		this.resultFactMap = new IdentityHashMap<BasicBlock, Fact>();
	}

	// public BasicDataflowAnalysis(CFG cfg){
	// this.owner = cfg.getOwner();
	// this.cfg = cfg;
	// this.m = cfg.getMethod();
	// this.insns = m.instructions;
	// this.subroutines = cfg.getSubroutines();
	// this.handlers = cfg.getHandlers();
	// this.startFactMap = new IdentityHashMap<BasicBlock, Fact>();
	// this.resultFactMap = new IdentityHashMap<BasicBlock, Fact>();
	//
	// }

	// @Override
	// public void copy(Fact source, Fact dest) {
	// // TODO Auto-generated method stub
	//		
	// }

	// @Override
	public Fact createEntryFact() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Fact createFact() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Fact createFact(Fact fact) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void finishIteration() {
		// TODO Auto-generated method stub

	}

	@Override
	public BlockOrder getBlockOrder() {
		// TODO Auto-generated method stub
		return blockOrder;
	}

	@Override
	public Fact getFact(int index) {
		// TODO Auto-generated method stub
		return facts[index];
	}

	@Override
	public Fact[] getFacts() {
		// TODO Auto-generated method stub
		return facts;
	}

	@Override
	public int getLastUpdateTimestamp(Fact fact) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Fact getNewStartFact(BasicBlock block) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Fact getResultFact(BasicBlock block) {
		// TODO Auto-generated method stub
		Fact fact = resultFactMap.get(block);
		if (fact == null) {
			fact = createFact();
			resultFactMap.put(block, fact);
		}
		return fact;
	}

	@Override
	public Fact getStartFact(BasicBlock block) {
		// TODO Auto-generated method stub
		Fact fact = startFactMap.get(block);
		if (fact == null) {
			fact = createFact();
			startFactMap.put(block, fact);
		}
		return fact;
	}

	@Override
	public void initEntryFact() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isEndBlock(BasicBlock block) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isForwards() {
		// TODO Auto-generated method stub
		return this.isForwards;
	}

	@Override
	public Fact merge(Fact start, Fact pred) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean same(Fact fact1, Fact fact2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setLastUpdateTimestamp(Fact fact, int timestamp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setNewStartFact(BasicBlock block, Fact fact) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setResultFact(BasicBlock block, Fact result) {
		// TODO Auto-generated method stub
		resultFactMap.put(block, result);
	}

	@Override
	public void setStartFact(BasicBlock block, Fact fact) {
		// TODO Auto-generated method stub
		startFactMap.put(block, fact);
	}

	@Override
	public void startIteration() {
		// TODO Auto-generated method stub

	}

	@Override
	public Fact transferVertex(BasicBlock block) {
		// TODO Auto-generated method stub
		// boolean isChanged = true;

		Iterator<AbstractInsnNode> insnIter = block.nodeIterator(false);
		// int insn = block.getStartInc() - 1;
		// int insnOpcode = -1;
		// AbstractInsnNode insnNode = null;

		Fact in = createFact(startFactMap.get(block));
		Fact out = null;
		while (insnIter.hasNext()) {
			// isChanged = false;
			out = transferInsn(insnIter.next(), in);
			// isChanged=same(in, out);
		}
		return out;
	}

	public Fact transferInsn(AbstractInsnNode insn, Fact in) {
		return in;
	}

	/**
	 * Visits a zero operand instruction.
	 * 
	 * @param insn
	 *            opcode the opcode of the instruction to be visited. This
	 *            opcode is either NOP, ACONST_NULL, ICONST_M1, ICONST_0,
	 *            ICONST_1, ICONST_2, ICONST_3, ICONST_4, ICONST_5, LCONST_0,
	 *            LCONST_1, FCONST_0, FCONST_1, FCONST_2, DCONST_0, DCONST_1,
	 *            IALOAD, LALOAD, FALOAD, DALOAD, AALOAD, BALOAD, CALOAD,
	 *            SALOAD, IASTORE, LASTORE, FASTORE, DASTORE, AASTORE, BASTORE,
	 *            CASTORE, SASTORE, POP, POP2, DUP, DUP_X1, DUP_X2, DUP2,
	 *            DUP2_X1, DUP2_X2, SWAP, IADD, LADD, FADD, DADD, ISUB, LSUB,
	 *            FSUB, DSUB, IMUL, LMUL, FMUL, DMUL, IDIV, LDIV, FDIV, DDIV,
	 *            IREM, LREM, FREM, DREM, INEG, LNEG, FNEG, DNEG, ISHL, LSHL,
	 *            ISHR, LSHR, IUSHR, LUSHR, IAND, LAND, IOR, LOR, IXOR, LXOR,
	 *            I2L, I2F, I2D, L2I, L2F, L2D, F2I, F2L, F2D, D2I, D2L, D2F,
	 *            I2B, I2C, I2S, LCMP, FCMPL, FCMPG, DCMPL, DCMPG, IRETURN,
	 *            LRETURN, FRETURN, DRETURN, ARETURN, RETURN, ARRAYLENGTH,
	 *            ATHROW, MONITORENTER, or MONITOREXIT.
	 */
	public Fact transferInsn(InsnNode insn, Fact in) {
		return in;
	}

	/**
	 * Visits an instruction with a single int operand.
	 * 
	 * @param insn
	 *            opcode the opcode of the instruction to be visited. This
	 *            opcode is either BIPUSH, SIPUSH or NEWARRAY. operand the
	 *            operand of the instruction to be visited.<br>
	 *            When opcode is BIPUSH, operand value should be between
	 *            Byte.MIN_VALUE and Byte.MAX_VALUE.<br>
	 *            When opcode is SIPUSH, operand value should be between
	 *            Short.MIN_VALUE and Short.MAX_VALUE.<br>
	 *            When opcode is NEWARRAY, operand value should be one of
	 *            {@link Opcodes#T_BOOLEAN}, {@link Opcodes#T_CHAR},
	 *            {@link Opcodes#T_FLOAT}, {@link Opcodes#T_DOUBLE},
	 *            {@link Opcodes#T_BYTE}, {@link Opcodes#T_SHORT},
	 *            {@link Opcodes#T_INT} or {@link Opcodes#T_LONG}.
	 */
	public Fact transferInsn(IntInsnNode insn, Fact in) {
		return in;
	}

	/**
	 * Visits a local variable instruction. A local variable instruction is an
	 * instruction that loads or stores the value of a local variable.
	 * 
	 * @param var
	 *            opcode the opcode of the local variable instruction to be
	 *            visited. This opcode is either ILOAD, LLOAD, FLOAD, DLOAD,
	 *            ALOAD, ISTORE, LSTORE, FSTORE, DSTORE, ASTORE or RET.
	 */
	public Fact transferInsn(VarInsnNode var, Fact in) {
		return in;
	}

	/**
	 * Visits a type instruction. A type instruction is an instruction that
	 * takes the internal name of a class as parameter.
	 * 
	 * @param opcode
	 *            the opcode of the type instruction to be visited. This opcode
	 *            is either NEW, ANEWARRAY, CHECKCAST or INSTANCEOF.
	 */
	public Fact transferInsn(TypeInsnNode type, Fact in) {
		return in;
	}

	/**
	 * Visits a field instruction. A field instruction is an instruction that
	 * loads or stores the value of a field of an object.
	 * 
	 * @param opcode
	 *            the opcode of the type instruction to be visited. This opcode
	 *            is either GETSTATIC, PUTSTATIC, GETFIELD or PUTFIELD.
	 */
	public Fact transferInsn(FieldInsnNode field, Fact in) {
		return in;
	}

	/**
	 * Visits a method instruction. A method instruction is an instruction that
	 * invokes a method.
	 * 
	 * @param opcode
	 *            the opcode of the type instruction to be visited. This opcode
	 *            is either INVOKEVIRTUAL, INVOKESPECIAL, INVOKESTATIC or
	 *            INVOKEINTERFACE.
	 */
	public Fact transferInsn(MethodInsnNode insn, Fact in) {
		return in;
	}

	/**
	 * Visits a jump instruction. A jump instruction is an instruction that may
	 * jump to another instruction.
	 * 
	 * @param opcode
	 *            the opcode of the type instruction to be visited. This opcode
	 *            is either IFEQ, IFNE, IFLT, IFGE, IFGT, IFLE, IF_ICMPEQ,
	 *            IF_ICMPNE, IF_ICMPLT, IF_ICMPGE, IF_ICMPGT, IF_ICMPLE,
	 *            IF_ACMPEQ, IF_ACMPNE, GOTO, JSR, IFNULL or IFNONNULL.
	 */
	public Fact transferInsn(JumpInsnNode jump, Fact in) {
		return in;
	}

	// -------------------------------------------------------------------------
	// Special instructions
	// -------------------------------------------------------------------------

	/**
	 * Visits a LDC instruction.
	 * 
	 * @param cst
	 *            the constant to be loaded on the stack. This parameter must be
	 *            a non null {@link Integer}, a {@link Float}, a {@link Long}, a
	 *            {@link Double} a {@link String} (or a {@link Type} for
	 *            <tt>.class</tt> constants, for classes whose version is 49.0
	 *            or more).
	 */
	public Fact transferInsn(LdcInsnNode ldc, Fact in) {
		return in;
	}

	/**
	 * Visits an IINC instruction.
	 * 
	 * @param var
	 *            index of the local variable to be incremented.
	 */
	public Fact transferInsn(IincInsnNode inc, Fact in) {
		return in;
	}

	/**
	 * Visits a TABLESWITCH instruction.
	 * 
	 */
	public Fact transferInsn(TableSwitchInsnNode table, Fact in) {
		return in;
	}

	/**
	 * Visits a LOOKUPSWITCH instruction.
	 * 
	 */
	public Fact transferInsn(LookupSwitchInsnNode insn, Fact in) {
		return in;
	}

	/**
	 * Visits a MULTIANEWARRAY instruction.
	 * 
	 */
	public Fact transferInsn(MultiANewArrayInsnNode mnew, Fact in) {
		return in;
	}

}

// end
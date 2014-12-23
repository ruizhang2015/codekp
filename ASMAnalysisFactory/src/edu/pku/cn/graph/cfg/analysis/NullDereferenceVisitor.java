/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-6-20 ����02:35:22
 * @modifier: Liuxizhiyi
 * @time 2008-6-20 ����02:35:22
 * @reviewer: Liuxizhiyi
 * @time 2008-6-20 ����02:35:22
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.cfg.analysis;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.tree.analysis.Frame;

import edu.pku.cn.asm.tree.analysis.RealValue;
import edu.pku.cn.classfile.LineNumberTable;
import edu.pku.cn.classfile.LocalVariableTable;
import edu.pku.cn.classfile.LocalVariableTable.LocalVariableNode;
import edu.pku.cn.graph.cfg.BasicBlock;
import edu.pku.cn.graph.cfg.BlockType;
import edu.pku.cn.graph.cfg.CFG;
import edu.pku.cn.graph.cfg.Edge;
import edu.pku.cn.graph.cfg.EdgeType;
import edu.pku.cn.graph.cfg.SwitchBlock;
import edu.pku.cn.graph.visit.GraphVisitor;

/**
 * 
 * @author Liuxizhiyi
 */
public class NullDereferenceVisitor extends GraphVisitor<CFG, Edge, BasicBlock> {
	class JumpState {
		public static final int JUMP = 1;
		public static final int FALLTHROUGH = -1;
		public static final int UNKNOW = 0;
		public int type = UNKNOW;
		int value;
		public int conditionLabel;
		public BitSet forbid = new BitSet();
		public BitSet lable = new BitSet();

		public JumpState(int type) {
			this.type = type;
		}

		public void setValue(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public String toString() {
			switch (type) {
			case JUMP:
				return "JUMP";
			case UNKNOW:
				return "UNKNOW";
			case FALLTHROUGH:
				return "FALLTHROUGH";
			default:
				return "ERROR STATE";
			}
		}
	}

	protected int[] localVariablesState;
	// private BitSet variableExat;
	// private HashMap<String,Boolean> fieldState=new HashMap<String,
	// Boolean>();
	public final static int NULL = 1;
	public final static int MAYNULL = 0;
	public final static int NOTNULL = -1;
	private int state;

	protected LineNumberTable lineNumbers;
	protected LocalVariableTable localVariables;
	protected InsnList insnList;
	// private List<BasicBlock> stack=new ArrayList<BasicBlock>();
	boolean visitStatus = true;
	BitSet blockVertex = new BitSet();
	boolean blockLock = false;
	// HashMap<Integer,JumpState> jumpSate=new HashMap<Integer, JumpState>();
	List<JumpState> jumpStateStack = new ArrayList<JumpState>();
	protected MethodNode method;
	BasicBlock currentBlock;

	public NullDereferenceVisitor(CFG cfg, MethodNode method) {
		insnList = method.instructions;
		// localVariables=method.localVariables;
		if (cfg.hasLineNumber())
			lineNumbers = cfg.getLineNumberTable();
		// ���û��local variableￄ1�7һЩ�����޷�����
		if (cfg.hasLocalVariable())
			localVariables = cfg.getLocalVariableTable();
		localVariablesState = new int[localVariables.size()];
		// variableExat=new BitSet(localVariables.size());
		this.method = method;
		if ((method.access & Opcodes.ACC_STATIC) == 0) {
			localVariablesState[0] = NullDereferenceVisitor.NOTNULL;
		}
	}

	/**
	 * @see edu.pku.cn.graph.visit.GraphVisitor#visitEdge(edu.pku.cn.graph.BasicEdge)
	 */
	@Override
	public void visitEdge(Edge edge) {
		// �Ƿ��ڵ㱻�������û�У��жϿ��ܵ�if�ڵ�
		JumpState jump = null;
		if (jumpStateStack.size() > 0 == false)
			return;
		do {
			jump = jumpStateStack.get(0);
			if (jump.lable.get(edge.getSource().getLabel())) {
				break;
			} else {
				jumpStateStack.remove(0);
			}
		} while (jumpStateStack.size() >= 0);
		jump.lable.set(edge.getTarget().getLabel());
		if (jump.forbid.get(edge.getSource().getLabel()) == false) {
			if (edge.getType() == EdgeType.IFCMP_EDGE_FALLTHROUGH && jump.type == JumpState.JUMP
					|| edge.getType() == EdgeType.IFCMP_EDGE_JUMP && jump.type == JumpState.FALLTHROUGH) {
				visitStatus = false;
				jump.forbid.set(edge.getTarget().getLabel());
			} else if (jump.type == JumpState.JUMP
					&& (edge.getType() == EdgeType.SWITCH_DEFAULT_EDGE || edge.getType() == EdgeType.SWITCH_EDGE)) {
				int value = jump.getValue();
				SwitchBlock block = (SwitchBlock) edge.getSource();
				Iterator<BasicBlock> iter = block.switchCase.values().iterator();
				while (iter.hasNext()) {
					jump.forbid.set(iter.next().getLabel());
				}
				if (block.switchCase.containsKey(value)) {
					jump.forbid.set(block.switchCase.get(value).getLabel(), false);
					if (block.defaultCase != null)
						jump.forbid.set(block.defaultCase.getLabel());
				}
				if (jump.forbid.get(edge.getTarget().getLabel()))
					visitStatus = false;
				else
					visitStatus = true;
				jump.forbid.set(block.getLabel());
			} else {
				visitStatus = true;
			}
		}
		// ��ڵ㱻�����ж��Ƿ��Ǹ�ڵ㣬����ǣ��ͷ�����ĸ�ڵￄ1�7
		else {
			BasicBlock target = edge.getTarget();
			visitStatus = false;
			if (target.getType() != BlockType.WHILE_BLOCK || target.getType() != BlockType.DOWHILE_BLOCK) {
				if (target.inComingEdgeSize() > 1 && currentBlock.pointTo(target))
					visitStatus = true;
			}
			if (edge.getSource().getType() == BlockType.SWITCH_BLOCK) {
				SwitchBlock block = (SwitchBlock) edge.getSource();
				if (block.switchCase.containsKey(jump.getValue())) {
					if (edge.getTarget().equals(block.switchCase.get(jump.getValue()))) {
						visitStatus = true;
					}
				} else if (edge.getTarget().equals(block.defaultCase)) {
					visitStatus = true;
				}
			}
			if (visitStatus == false) {
				jump.forbid.set(target.getLabel());
			}
		}
		// System.out.println("Edge:"+edge.getLabel());

		// return visitStatus;
		return;
	}

	public void visitVertex(BasicBlock vertex) {
		currentBlock = vertex;
		// System.out.println(vertex.getLabel());
	}

	public void visitInsn(VarInsnNode insn, Frame frame) {
		if (insn.getOpcode() == Opcodes.ASTORE) {
			int index = insnList.indexOf(insn);
			LocalVariableNode node = localVariables.getNode(index, insn.var);
			if (node != null) {
				int allIndex = localVariables.getNode(index, insn.var).allIndex;
				RealValue stackValue = (RealValue) frame.getStack(0);
				localVariablesState[allIndex] = state;
				if (stackValue.getValue() != null)
					if (stackValue.getValue() instanceof Exception)
						System.out.println("Exception in " + insn);
					else
						localVariablesState[allIndex] = NOTNULL;
				else
					localVariablesState[allIndex] = NULL;

				if (localVariablesState[allIndex] == NULL)
					System.out.println("Line" + lineNumbers.getLine(index) + ":Null Value in " + node.name);
				else if (localVariablesState[allIndex] == MAYNULL && node.type.getSort() >= Type.ARRAY)
					System.out.println("Line" + lineNumbers.getLine(index) + ":May Null Value in " + node.name);
			}
		}
	}

	public void visitInsn(JumpInsnNode insn, Frame frame) {
		// System.out.println(insn);
		JumpState jump = null;
		switch (insn.getOpcode()) {
		// equals 0
		case Opcodes.IFEQ:
		case Opcodes.IFGE:
		case Opcodes.IFGT:
		case Opcodes.IFLE:
		case Opcodes.IFLT:
		case Opcodes.IFNE:
		case Opcodes.IF_ICMPEQ:
		case Opcodes.IF_ICMPGE:
		case Opcodes.IF_ICMPGT:
		case Opcodes.IF_ICMPLE:
		case Opcodes.IF_ICMPLT:
		case Opcodes.IF_ICMPNE:
			jump = compareInt(insn.getOpcode(), frame);
			break;
		case Opcodes.IFNONNULL:
		case Opcodes.IFNULL:
			jump = compareNull(insn.getOpcode(), frame);
			break;
		case Opcodes.IF_ACMPEQ:
		case Opcodes.IF_ACMPNE:
			jump = compareReference(insn.getOpcode(), frame);
			break;
		}
		if (insn.getOpcode() != Opcodes.GOTO && insn.getOpcode() != Opcodes.JSR) {
			jump.conditionLabel = currentBlock.getLabel();
			jump.lable.set(currentBlock.getLabel());
			jumpStateStack.add(0, jump);
		}
	}

	private JumpState intValue(Frame frame, int index) {
		JumpState state = new JumpState(JumpState.JUMP);
		RealValue value = (RealValue) frame.getStack(index);
		if (value.exactValue == RealValue.UNKNOW_VALUE)
			return new JumpState(JumpState.UNKNOW);
		int v = 0;
		if (value.getType() == Type.BOOLEAN_TYPE)
			v = ((Boolean) value.getValue()).booleanValue() ? 1 : 0;
		else if (value.getType() == Type.CHAR_TYPE)
			v = ((Character) value.getValue()).charValue();
		else
			v = value.getNumber().intValue();
		if (value.getType().getSort() > Type.INT) {
			System.out.println("you have compare a wrong type object");
			return new JumpState(JumpState.UNKNOW);
		}
		state.setValue(v);
		return state;
	}

	private JumpState compareInt(int opcode, Frame frame) {
		JumpState state = intValue(frame, 0);
		if (state.type == JumpState.UNKNOW)
			return state;
		int v = state.getValue();
		switch (opcode) {
		case Opcodes.IFEQ:
			if (v == 0)
				return state;
			break;
		case Opcodes.IFGE:
			if (v >= 0)
				return state;
			break;
		case Opcodes.IFGT:
			if (v > 0)
				return state;
			break;
		case Opcodes.IFLE:
			if (v <= 0)
				return state;
			break;
		case Opcodes.IFLT:
			if (v < 0)
				return state;
			break;
		case Opcodes.IFNE:
			if (v != 0)
				return state;
			break;
		case Opcodes.IF_ICMPEQ:
			state = intValue(frame, 1);
			if (v == state.getValue())
				return state;
			break;
		case Opcodes.IF_ICMPNE:
			state = intValue(frame, 1);
			if (v != state.getValue())
				return state;
			break;
		case Opcodes.IF_ICMPLT:
			state = intValue(frame, 1);
			if (v < state.getValue())
				return state;
			break;
		case Opcodes.IF_ICMPLE:
			state = intValue(frame, 1);
			if (v <= state.getValue())
				return state;
			break;
		case Opcodes.IF_ICMPGT:
			state = intValue(frame, 1);
			if (v > state.getValue())
				return state;
			break;
		case Opcodes.IF_ICMPGE:
			state = intValue(frame, 1);
			if (v >= state.getValue())
				return state;
			break;
		}
		state.type = JumpState.FALLTHROUGH;
		return state;
	}

	private JumpState compareNull(int opcode, Frame frame) {
		RealValue<Object> value = (RealValue<Object>) frame.getStack(0);
		if (value.exactValue == RealValue.UNKNOW_VALUE)
			return new JumpState(JumpState.UNKNOW);
		switch (opcode) {
		case Opcodes.IFNONNULL:
			if (value.getValue() != null)
				return new JumpState(JumpState.JUMP);
			break;
		case Opcodes.IFNULL:
			if (value.getValue() == null)
				return new JumpState(JumpState.JUMP);
			break;
		}
		return new JumpState(JumpState.FALLTHROUGH);
	}

	@SuppressWarnings("unchecked")
	private JumpState compareReference(int opcode, Frame frame) {
		RealValue<Object> v1 = (RealValue<Object>) frame.getStack(0);
		RealValue<Object> v2 = (RealValue<Object>) frame.getStack(1);
		if ((v1.exactValue & v1.exactValue) == RealValue.UNKNOW_VALUE)
			return new JumpState(JumpState.UNKNOW);
		boolean bothNull = false;
		boolean oneNull = false;
		if (v1 == null && v2 == null) {
			bothNull = true;
		}
		if (v1 == null || v2 == null) {
			oneNull = true;
		}
		JumpState jump = new JumpState(JumpState.JUMP);
		JumpState fall = new JumpState(JumpState.FALLTHROUGH);
		switch (opcode) {
		case Opcodes.IF_ACMPEQ:
			if (bothNull)
				return jump;
			else {
				if (oneNull)
					return fall;
				return v1.equals(v2) ? jump : fall;
			}
		case Opcodes.IF_ACMPNE:
			if (bothNull)
				return fall;
			else {
				if (oneNull)
					return jump;
				return !v1.equals(v2) ? jump : fall;
			}
		}
		return fall;
	}

	public void visitInsn(TableSwitchInsnNode insn, Frame frame) {
		JumpState jump = intValue(frame, 0);
		jump.lable.set(currentBlock.getLabel());
		jumpStateStack.add(0, jump);
	}

	public void visitInsn(LookupSwitchInsnNode insn, Frame frame) {
		JumpState jump = intValue(frame, 0);
		jump.lable.set(currentBlock.getLabel());
		jumpStateStack.add(0, jump);
	}

	public void visitInsn(FieldInsnNode insn, Frame frame) {
	}

	public void visitInsn(MethodInsnNode insn, Frame frame) {
		int stackIndex = -1;
		int index = insnList.indexOf(insn);
		int var;
		switch (insn.getOpcode()) {
		case Opcodes.INVOKEVIRTUAL:
		case Opcodes.INVOKESPECIAL:
		case Opcodes.INVOKEINTERFACE:
			RealValue<Object> value = (RealValue) frame.getStack(0);
			var = value.getVar();
			if (value.getValue() == null) {
				if (value.exactValue == RealValue.EXACT_VALUE)
					System.out.println("[Line " + lineNumbers.getLine(index) + "] Null pointer dereference of "
							+ localVariables.getNode(index, var).name + " when you " + insn);
				else
					System.out.println("[Line " + lineNumbers.getLine(index) + "] "
							+ localVariables.getNode(index, var).name + " could be null when you " + insn);
			}
			stackIndex = 0;
		case Opcodes.INVOKESTATIC:
			int size = insn.desc.split(";").length;
			for (int i = 1; i < size; i++) {
				value = (RealValue) frame.getStack(i + stackIndex);
				var = value.getVar();
				if (value.getType() == null || value.getType().getSort() >= Type.OBJECT) {
					if (value.getValue() == null) {
						if (value.exactValue == RealValue.EXACT_VALUE)
							System.out.print("[Line " + lineNumbers.getLine(index) + "] Null pointer dereference of "
									+ (var >= 0 ? localVariables.getNode(index, var).name : value.getValue()) + " in "
									+ method.name);
						else
							System.out.print("[Line " + lineNumbers.getLine(index) + "] "
									+ (var >= 0 ? localVariables.getNode(index, var).name : value.getValue())
									+ " could be null in " + method.name);
						System.out.println(" when you pass param to " + insn);
					}
				}
			}
			break;
		}
	}

	public void visitInsn(TypeInsnNode insn, Frame frame) {
		// if(insn.getOpcode()==Opcodes.NEW)
		// seeNew=true;
	}

	public void visitInsn(InsnNode insn, Frame frame) {
		switch (insn.getOpcode()) {
		case Opcodes.MONITORENTER:
		case Opcodes.MONITOREXIT:
			break;
		case Opcodes.LCMP:
		case Opcodes.FCMPL:
		case Opcodes.FCMPG:
		case Opcodes.DCMPL:
		case Opcodes.DCMPG:
			break;
		}

	}

	public void visitEnd() {
	}
}

// end

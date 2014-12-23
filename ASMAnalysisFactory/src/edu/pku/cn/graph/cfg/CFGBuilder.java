/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-5-22 ����09:41:44
 * @modifier: Liuxizhiyi
 * @time 2008-5-22 ����09:41:44
 * @reviewer: Liuxizhiyi
 * @time 2008-5-22 ����09:41:44
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.cfg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.TryCatchBlockNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Frame;
import org.objectweb.asm.tree.analysis.Interpreter;

import edu.pku.cn.analysis.common.Subroutine;
import edu.pku.cn.classfile.MethodNode;

/**
 * 
 * @author Liuxizhiyi
 */
public class CFGBuilder implements Opcodes {
	private final Interpreter interpreter;
	private int n;
	private InsnList insns;
	private List<TryCatchBlockNode>[] handlers;
	private Frame[] frames;
	private Subroutine[] subroutines;
	private boolean[] queued;
	private int[] queue;
	private int top;

	private CFG cfg;
	private HashMap<Integer, BasicBlock> blockMap;

	/**
	 * Constructs a new {@link Analyzer}.
	 * 
	 * @param interpreter
	 *            the interpreter to be used to symbolically interpret the
	 *            bytecode instructions.
	 */
	public CFGBuilder(final Interpreter interpreter) {
		this.interpreter = interpreter;
		blockMap = new HashMap<Integer, BasicBlock>();
	}

	public CFG create() {
		cfg.inline();
		blockMap.clear();
		return cfg;
	}

	/**
	 * Analyzes the given method.
	 * 
	 * @param owner
	 *            the internal name of the class to which the method belongs.
	 * @param m
	 *            the method to be analyzed.
	 * @return the symbolic state of the execution stack frame at each bytecode
	 *         instruction of the method. The size of the returned array is
	 *         equal to the number of instructions (and labels) of the method. A
	 *         given frame is <tt>null</tt> if and only if the corresponding
	 *         instruction cannot be reached (dead code).
	 * @throws AnalyzerException
	 *             if a problem occurs during the analysis.
	 */
	public Frame[] analyze(final String owner, final MethodNode m) throws AnalyzerException {
//		System.err.println(owner+"  "+m.name);
		cfg = new CFG(m);
		BasicBlock currentBlock = cfg.createBasicBlock(0, 0);
		blockMap.put(0, currentBlock);
		//cfg.addVertex(currentBlock);
		cfg.setRoot(currentBlock);		
		{// build BasicBlock
			int k = 0;
			if (m.instructions.size() > 0 && m.instructions.get(k) instanceof LabelNode)
				k++;
			while (k < m.instructions.size()) {
				if (m.instructions.get(k) instanceof LabelNode) {
					if (k > 0) {
						currentBlock.setEndInc(k - 1);
						// we put the index of end inc as the key of
						// currentBlock also
						blockMap.put(k - 1, currentBlock);
					}
					currentBlock = cfg.createBasicBlock(k, k);
					//cfg.addVertex(currentBlock);
					blockMap.put(k, currentBlock);
				}
				k++;
			}
		}
		if ((m.access & (ACC_ABSTRACT | ACC_NATIVE)) != 0) {
			frames = new Frame[0];
			return frames;
		}
		n = m.instructions.size();
		insns = m.instructions;
		handlers = new List[n];
		frames = cfg.getFrames();// new Frame[n];
		subroutines = new Subroutine[n];
		queued = new boolean[n];
		queue = new int[n];
		top = 0;

		// computes exception handlers for each instruction
		for (int i = 0; i < m.tryCatchBlocks.size(); ++i) {
			TryCatchBlockNode tcb = (TryCatchBlockNode) m.tryCatchBlocks.get(i);
			int begin = insns.indexOf(tcb.start);
			int end = insns.indexOf(tcb.end);
			for (int j = begin; j < end; ++j) {
				List insnHandlers = handlers[j];
				if (insnHandlers == null) {
					insnHandlers = new ArrayList();
					handlers[j] = insnHandlers;
				}
				insnHandlers.add(tcb);
			}
		}

		// computes the subroutine for each instruction:
		Subroutine main = new Subroutine(null, m.maxLocals, null);
		List subroutineCalls = new ArrayList();
		Map subroutineHeads = new HashMap();
		findSubroutine(0, main, subroutineCalls);
		while (!subroutineCalls.isEmpty()) {
			JumpInsnNode jsr = (JumpInsnNode) subroutineCalls.remove(0);
			Subroutine sub = (Subroutine) subroutineHeads.get(jsr.label);
			if (sub == null) {
				sub = new Subroutine(jsr.label, m.maxLocals, jsr);
				subroutineHeads.put(jsr.label, sub);
				findSubroutine(insns.indexOf(jsr.label), sub, subroutineCalls);
			} else {
				sub.callers.add(jsr);
			}
		}
		for (int i = 0; i < n; ++i) {
			if (subroutines[i] != null && subroutines[i].start == null) {
				subroutines[i] = null;
			}
		}

		// initializes the data structures for the control flow analysis
		Frame current = newFrame(m.maxLocals, m.maxStack);
		Frame handler = newFrame(m.maxLocals, m.maxStack);
		Type[] args = Type.getArgumentTypes(m.desc);
		int local = 0;
		if ((m.access & ACC_STATIC) == 0) {
			Type ctype = Type.getObjectType(owner);
			current.setLocal(local++, interpreter.newValue(ctype));
		}
		for (int i = 0; i < args.length; ++i) {
			current.setLocal(local++, interpreter.newValue(args[i]));
			if (args[i].getSize() == 2) {
				current.setLocal(local++, interpreter.newValue(null));
			}
		}
		while (local < m.maxLocals) {
			current.setLocal(local++, interpreter.newValue(null));
		}
		merge(0, current, null);

		// control flow analysis
		while (top > 0) {
			int insn = queue[--top];
			Frame f = frames[insn];
			Subroutine subroutine = subroutines[insn];
			queued[insn] = false;

			try {
				AbstractInsnNode insnNode = m.instructions.get(insn);
				int insnOpcode = insnNode.getOpcode();
				int insnType = insnNode.getType();
				if (insnType == AbstractInsnNode.LABEL || insnType == AbstractInsnNode.LINE
						|| insnType == AbstractInsnNode.FRAME) {
					merge(insn + 1, f, subroutine);
					// newControlFlowEdge(m.instructions,insn, insn + 1);
				} else {
					current.init(f).execute(insnNode, interpreter);
					subroutine = subroutine == null ? null : subroutine.copy();
					switch (insnType) {
					case AbstractInsnNode.JUMP_INSN: {
						JumpInsnNode j = (JumpInsnNode) insnNode;
						int jump = insns.indexOf(j.label);
						if (insnOpcode != GOTO && insnOpcode != JSR) {
							merge(insn + 1, current, subroutine);
							newControlFlowEdge(insns, insn, insn + 1, EdgeType.IFCMP_EDGE_FALLTHROUGH);
							merge(jump, current, subroutine);
							newControlFlowEdge(insns, insn, jump, EdgeType.IFCMP_EDGE_JUMP);
						} else if (insnOpcode == JSR) {
							merge(jump, current, new Subroutine(j.label, m.maxLocals, j));
							newControlFlowEdge(insns, insn, jump, EdgeType.JSR_EDGE);
						} else {
							merge(jump, current, subroutine);
							newControlFlowEdge(insns, insn, jump, EdgeType.GOTO_EDGE);
						}
					}
						break;
					case AbstractInsnNode.LOOKUPSWITCH_INSN: {
						LookupSwitchInsnNode lsi = (LookupSwitchInsnNode) insnNode;
						int jump = insns.indexOf(lsi.dflt);
						merge(jump, current, subroutine);
						newControlFlowEdge(m.instructions, insn, jump, EdgeType.SWITCH_DEFAULT_EDGE);
						for (int j = 0; j < lsi.labels.size(); ++j) {
							LabelNode label = (LabelNode) lsi.labels.get(j);
							jump = insns.indexOf(label);
							merge(jump, current, subroutine);
							newControlFlowEdge(insns, insn, jump, EdgeType.SWITCH_EDGE);
						}
					}
						break;
					case AbstractInsnNode.TABLESWITCH_INSN: {
						TableSwitchInsnNode tsi = (TableSwitchInsnNode) insnNode;
						int jump = insns.indexOf(tsi.dflt);
						merge(jump, current, subroutine);
						newControlFlowEdge(insns, insn, jump, EdgeType.SWITCH_DEFAULT_EDGE);
						for (int j = 0; j < tsi.labels.size(); ++j) {
							LabelNode label = (LabelNode) tsi.labels.get(j);
							jump = insns.indexOf(label);
							merge(jump, current, subroutine);
							newControlFlowEdge(insns, insn, jump, EdgeType.SWITCH_EDGE);
						}
					}
						break;
					case AbstractInsnNode.VAR_INSN:
					default:
						if (insnOpcode == RET) {
							if (subroutine == null) {
								throw new AnalyzerException("RET instruction outside of a sub routine");
							}
							for (int i = 0; i < subroutine.callers.size(); ++i) {
								Object caller = subroutine.callers.get(i);
								int call = insns.indexOf((AbstractInsnNode) caller);
								if (frames[call] != null) {
									merge(call + 1, frames[call], current, subroutines[call], subroutine.access);
									newControlFlowEdge(insns, insn, call + 1, EdgeType.RET_EDGE);
								}
							}
						} else if (insnOpcode != ATHROW && (insnOpcode < IRETURN || insnOpcode > RETURN)) {
							if (subroutine != null) {
								if (insnNode instanceof VarInsnNode) {
									int var = ((VarInsnNode) insnNode).var;
									subroutine.access[var] = true;
									if (insnOpcode == LLOAD || insnOpcode == DLOAD || insnOpcode == LSTORE
											|| insnOpcode == DSTORE) {
										subroutine.access[var + 1] = true;
									}
								} else if (insnNode instanceof IincInsnNode) {
									int var = ((IincInsnNode) insnNode).var;
									subroutine.access[var] = true;
								}
							}
							merge(insn + 1, current, subroutine);
							newControlFlowEdge(m.instructions, insn, insn + 1, EdgeType.FALL_THROUGH_EDGE);
						} else if (insnOpcode == ATHROW || insnOpcode >= IRETURN && insnOpcode <= RETURN) {
							if (insn != insns.size()) {
								int edgeType = insnOpcode == ATHROW ? EdgeType.ATHROW_EDGE : EdgeType.RETURN_EDGE;
								newControlFlowEdge(insns, insn, insns.size() - 1, edgeType);
							}
						}
						break;
					}

				}

				List insnHandlers = handlers[insn];
				if (insnHandlers != null) {
					for (int i = 0; i < insnHandlers.size(); ++i) {
						TryCatchBlockNode tcb = (TryCatchBlockNode) insnHandlers.get(i);
						Type type;
						if (tcb.type == null) {
							type = Type.getObjectType("java/lang/Throwable");
						} else {
							type = Type.getObjectType(tcb.type);
						}
						int jump = insns.indexOf(tcb.handler);
						if (newControlFlowExceptionEdge(insns, insn, jump)) {
							handler.init(f);
							handler.clearStack();
							handler.push(interpreter.newValue(type));
							merge(jump, handler, subroutine);
						}
					}
				}
			} catch (AnalyzerException e) {
				System.out.println(insns.toString());
				System.out.println(e.getMessage());
				e.printStackTrace();
				return null;
//				throw new AnalyzerException("Error at instruction " + insn + ": " + e.getMessage(), e);
			} catch (Exception e) {
				e.printStackTrace();
				throw new AnalyzerException("Error at instruction " + insn + ": " + e.getMessage(), e);
			}
		}

		cfg.setExit(blockMap.get(insns.size() - 1));
		cfg.setHandlers(handlers);
		cfg.setSubroutines(subroutines);
		// for(int i=1;i<frames.length-1;i++){
		// if(frames[i]==null){
		// frames[i]=new Frame(frames[i-1]);
		// //System.out.println(i);
		// }
		// }
		return frames;
	}

	private void findSubroutine(int insn, final Subroutine sub, final List<AbstractInsnNode> calls)
			throws AnalyzerException {
		while (true) {
			if (insn < 0 || insn >= n) {
				throw new AnalyzerException("Execution can fall off end of the code");
			}
			if (subroutines[insn] != null) {
				return;
			}
			subroutines[insn] = sub.copy();
			AbstractInsnNode node = insns.get(insn);

			// calls findSubroutine recursively on normal successors
			if (node instanceof JumpInsnNode) {
				if (node.getOpcode() == JSR) {
					// do not follow a JSR, it leads to another subroutine!
					calls.add(node);
				} else {
					JumpInsnNode jnode = (JumpInsnNode) node;
					findSubroutine(insns.indexOf(jnode.label), sub, calls);
				}
			} else if (node instanceof TableSwitchInsnNode) {
				TableSwitchInsnNode tsnode = (TableSwitchInsnNode) node;
				findSubroutine(insns.indexOf(tsnode.dflt), sub, calls);
				for (int i = tsnode.labels.size() - 1; i >= 0; --i) {
					LabelNode l = (LabelNode) tsnode.labels.get(i);
					findSubroutine(insns.indexOf(l), sub, calls);
				}
			} else if (node instanceof LookupSwitchInsnNode) {
				LookupSwitchInsnNode lsnode = (LookupSwitchInsnNode) node;
				findSubroutine(insns.indexOf(lsnode.dflt), sub, calls);
				for (int i = lsnode.labels.size() - 1; i >= 0; --i) {
					LabelNode l = (LabelNode) lsnode.labels.get(i);
					findSubroutine(insns.indexOf(l), sub, calls);
				}
			}

			// calls findSubroutine recursively on exception handler successors
			List<TryCatchBlockNode> insnHandlers = handlers[insn];
			if (insnHandlers != null) {
				for (int i = 0; i < insnHandlers.size(); ++i) {
					TryCatchBlockNode tcb = (TryCatchBlockNode) insnHandlers.get(i);
					findSubroutine(insns.indexOf(tcb.handler), sub, calls);
				}
			}

			// if insn does not falls through to the next instruction, return.
			switch (node.getOpcode()) {
			case GOTO:
			case RET:
			case TABLESWITCH:
			case LOOKUPSWITCH:
			case IRETURN:
			case LRETURN:
			case FRETURN:
			case DRETURN:
			case ARETURN:
			case RETURN:
			case ATHROW:
				return;
			}
			insn++;
		}
	}

	/**
	 * Returns the symbolic stack frame for each instruction of the last
	 * recently analyzed method.
	 * 
	 * @return the symbolic state of the execution stack frame at each bytecode
	 *         instruction of the method. The size of the returned array is
	 *         equal to the number of instructions (and labels) of the method. A
	 *         given frame is <tt>null</tt> if the corresponding instruction
	 *         cannot be reached, or if an error occured during the analysis of
	 *         the method.
	 */
	public Frame[] getFrames() {
		return frames;
	}

	/**
	 * Returns the exception handlers for the given instruction.
	 * 
	 * @param insn
	 *            the index of an instruction of the last recently analyzed
	 *            method.
	 * @return a list of {@link TryCatchBlockNode} objects.
	 */
	public List<TryCatchBlockNode> getHandlers(final int insn) {
		return handlers[insn];
	}

	/**
	 * Constructs a new frame with the given size.
	 * 
	 * @param nLocals
	 *            the maximum number of local variables of the frame.
	 * @param nStack
	 *            the maximum stack size of the frame.
	 * @return the created frame.
	 */
	protected Frame newFrame(final int nLocals, final int nStack) {
		return new Frame(nLocals, nStack);
	}

	/**
	 * Constructs a new frame that is identical to the given frame.
	 * 
	 * @param src
	 *            a frame.
	 * @return the created frame.
	 */
	protected Frame newFrame(final Frame src) {
		return new Frame(src);
	}

	protected BasicBlock findBlockInRegion(final int insn) {
		Iterator<BasicBlock> iter = blockMap.values().iterator();
		while (iter.hasNext()) {
			BasicBlock block = iter.next();
			if (block.getStartInc() <= insn && block.getEndInc() >= insn)
				return block;
		}
		return null;
	}

	protected BasicBlock getBlock(final int insn) {
		BasicBlock block = blockMap.get(insn);
		if (block == null) {
			block = cfg.createBasicBlock(insn, insn);
			blockMap.put(insn, block);
		}
		return block;
	}

	/**
	 * Creates a control flow graph edge. The default implementation of this
	 * method does nothing. It can be overriden in order to construct the
	 * control flow graph of a method (this method is called by the
	 * {@link #analyze analyze} method during its visit of the method's code).
	 * 
	 * @param insn
	 *            an instruction index.
	 * @param successor
	 *            index of a successor instruction.
	 */
	protected void newControlFlowEdge(InsnList list, final int insn, final int successor, int edgeType) {
		if (list.get(successor) instanceof LabelNode) {
			BasicBlock from = blockMap.get(insn);
			if (from == null) {
				int i = insn;
				for (; i > 0; i--) {
					if (insns.get(i) instanceof LabelNode)
						break;
				}
				from = blockMap.get(i);
				BasicBlock newBlock = cfg.breakBlock(from, insn);
				blockMap.put(from.getEndInc(), from);
				blockMap.put(newBlock.getStartInc(), newBlock);
				blockMap.put(newBlock.getEndInc(), newBlock);
//				System.err.println("break block"+insn);
			}
			BasicBlock to = blockMap.get(successor);
			cfg.createEdge(from, to, edgeType);
		}
		// if(list.get(successor) instanceof LabelNode)
		// System.out.println("Edge:"+insn+" to "+successor);
	}

	/**
	 * Creates a control flow graph edge corresponding to an exception handler.
	 * The default implementation of this method does nothing. It can be
	 * overriden in order to construct the control flow graph of a method (this
	 * method is called by the {@link #analyze analyze} method during its visit
	 * of the method's code).
	 * 
	 * @param insn
	 *            an instruction index.
	 * @param successor
	 *            index of a successor instruction.
	 * @return true if this edge must be considered in the data flow analysis
	 *         performed by this analyzer, or false otherwise. The default
	 *         implementation of this method always returns true.
	 */
	protected boolean isExceptionThrow(int opcode) {
		// LDC;LDC_w
		if (opcode == 18 || opcode == 19)
			// IALOAD= 46;LALOAD= 47;FALOAD= 48;DALOAD= 49;AALOAD= 50;BALOAD=
			// 51;CALOAD= 52;SALOAD= 53;
			if (opcode >= 46 && opcode <= 53)
				return true;
		// IASTORE--SASTORE
		if (opcode >= 79 && opcode <= 86)
			return true;
		// IDIV-DDIV;IREM-DREM
		if (opcode >= 108 && opcode <= 115)
			return true;
		// GetStatic=128(no),GetField,PutStatic=179(no),putField
		// InvokeVirtual182-InvokeStance185
		if (opcode >= 180 && opcode <= 185)
			return true;
		// No new and newarray187,188
		// ANewArray 189,ArrayLength190,Athrow 191
		if (opcode >= 189 && opcode <= 185)
			return true;
		// No CheckCast191,InstanceOf193 MoniterEnter194,MoniterExit195
		// miltiANew 197
		if (opcode == 197)
			return true;
		return false;

	}

	protected boolean newControlFlowExceptionEdge(InsnList list, final int insn, final int successor) {
		// 改写这个函数，如故确实是异常边，返回true

		if (list.get(insn) instanceof LabelNode) {
			BasicBlock from = blockMap.get(insn);
			BasicBlock to = blockMap.get(successor);
			cfg.createEdge(from, to, EdgeType.HANDLED_EXCEPTION_EDGE);
		}
		return true;
	}

	// -------------------------------------------------------------------------

	private void merge(final int insn, final Frame frame, final Subroutine subroutine) throws AnalyzerException {
		Frame oldFrame = frames[insn];
		Subroutine oldSubroutine = subroutines[insn];
		boolean changes;

		if (oldFrame == null) {
			frames[insn] = newFrame(frame);
			changes = true;
		} else {
			changes = oldFrame.merge(frame, interpreter);
		}

		if (oldSubroutine == null) {
			if (subroutine != null) {
				subroutines[insn] = subroutine.copy();
				changes = true;
			}
		} else {
			if (subroutine != null) {
				changes |= oldSubroutine.merge(subroutine);
			}
		}
		if (changes && !queued[insn]) {
			queued[insn] = true;
			queue[top++] = insn;
		}
	}

	private void merge(final int insn, final Frame beforeJSR, final Frame afterRET,
			final Subroutine subroutineBeforeJSR, final boolean[] access) throws AnalyzerException {
		Frame oldFrame = frames[insn];
		Subroutine oldSubroutine = subroutines[insn];
		boolean changes;

		afterRET.merge(beforeJSR, access);

		if (oldFrame == null) {
			frames[insn] = newFrame(afterRET);
			changes = true;
		} else {
			changes = oldFrame.merge(afterRET, access);
		}

		if (oldSubroutine != null && subroutineBeforeJSR != null) {
			changes |= oldSubroutine.merge(subroutineBeforeJSR);
		}
		if (changes && !queued[insn]) {
			queued[insn] = true;
			queue[top++] = insn;
		}
	}
}

// end

/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author MENG Na
 * @time 2008-11-6 ����08:02:23
 * @modifier: MENG Na
 * @time 2008-11-6 ����08:02:23
 * @reviewer: MENG Na
 * @time 2008-11-6 ����08:02:23
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.cfg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.objectweb.asm.tree.analysis.BasicInterpreter;
import org.objectweb.asm.tree.analysis.Frame;
import org.objectweb.asm.tree.analysis.Interpreter;

import edu.pku.cn.analysis.common.Subroutine;
import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.classfile.ClassNodeLoader;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.graph.cfg.BasicBlock;
import edu.pku.cn.graph.cfg.CFG;
import edu.pku.cn.graph.print.BasicGraphPrinter;
import edu.pku.cn.graph.visit.Topologic;
import edu.pku.cn.util.BasicBlockUtil;
import edu.pku.cn.util.OpcodeUtil;
import edu.pku.cn.util.Timer;

/**
 * There are three steps to construct the CFG: 1. find out all leaders & analyze
 * subroutine(to discuss later) 2. find out all blocks and draw edges among them
 * 
 * @author Meng Na
 */
public class SimpleCFGBuilder extends Analyzer {

	private boolean DEBUG = false;

	public SimpleCFGBuilder(Interpreter interpreter) {
		super(interpreter);
		// TODO Auto-generated constructor stub
	}

	// private static SimpleCFGBuilder cfgBuilder = null;

	private int n;

	private InsnList insns;

	private List<TryCatchBlockNode>[] handlers;

	private Subroutine[] subroutines;

	private CFG cfg;

	private HashMap<Integer, BasicBlock> blockMap = new HashMap<Integer, BasicBlock>();

	private HashMap<MethodNode, Subroutine[]> subroutinesMap = new HashMap<MethodNode, Subroutine[]>();

	private HashMap<String, CFG> cfgMap = new HashMap<String, CFG>();

	private MethodNode m;

	private Integer[] leaderArray = null;

	public final int START_PC = 0;

	public Frame[] analyze(final String owner, final MethodNode m) throws AnalyzerException {

		this.m = m;
		n = m.instructions.size();
		insns = m.instructions;
		handlers = new List[n];
		subroutines = new Subroutine[n];

		// initialize the CFG with an entry node
		cfg = new CFG(owner, m);

		blockMap.clear();
		// compute the subroutine for each instruction
		computeSubroutines(); // to compute subroutines[] initialized above

		// build up blockMap & blocks in CFG===========corresponding to
		// construct leader set use function "getBlock()" to add new block
		buildBlockMap();

		// sort the leaders
		sortLeaders();

		if (DEBUG) {
			// OpcodeUtil.printInsnList(insns, m.name);
		}

		// build blocks and draw edges among them
		buildCFG();

		cfgMap.put(m.name + m.desc, cfg);
		subroutinesMap.put(m, subroutines);

		// Iterator<BasicBlock> blockIter = cfg.blockIterator();
		// if(DEBUG){
		// System.out.println(this.cfg.getOwner() + "  " +
		// this.cfg.getMethod().name + this.cfg.getMethod().desc);
		// // BasicBlockUtil.printBasicBlocks(blockIter);
		// }
		return new Frame[0];
	}

	public List<TryCatchBlockNode>[] getHandlers() {
		return this.handlers;
	}

	public synchronized CFG getCFG(final String owner, final MethodNode m) throws AnalyzerException {
		if (!cfgMap.containsKey(m.name + m.desc))
			analyze(owner, m);
		return cfgMap.get(m.name + m.desc);
	}

	public Subroutine[] getSubroutines(final String owner, final MethodNode m) throws AnalyzerException {
		if (!subroutinesMap.containsKey(m))
			analyze(owner, m);
		return subroutinesMap.get(m);
	}

	protected boolean isExceptionThrow(int opcode) {

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

	protected BasicBlock getBlock(final int insn, final int type) {
		BasicBlock block = getBlock(insn);
		if (type != BlockType.NORMAL_BLOCK && block.getType() != type)
			block = changeBlock(block, type);
		return block;
	}

	/**
	 * (1) add the block to blockMap; (2) add the insn to the block
	 * 
	 * @param insn
	 * @param block
	 */
	private void addBlock(final int insn, final BasicBlock block) {
		if (insn < insns.size()) {
			block.addNode(insns.get(insn), null);
		}
		blockMap.put(insn, block);
	}

	protected BasicBlock getBlock(final int insn) {
		BasicBlock block = blockMap.get(insn);
		if (block == null) {
			block = cfg.createBasicBlock(insn, insn);
			if (insn < insns.size()) {
				block.addNode(insns.get(insn), null);
			}
			blockMap.put(insn, block);
		}
		return block;
	}

	protected BasicBlock changeBlock(BasicBlock block, int type) {
		BasicBlock newBlock = cfg.changeBlock(type, block);
		if (block.getType() != newBlock.getType()) {
			blockMap.put(block.getStartInc(), newBlock);
			if (cfg.containsVertex(block)) {
				cfg.replaceVertex(block, newBlock);
			}
		}
		return newBlock;
	}

	private void findSubroutine(int insn, final Subroutine sub, final List<AbstractInsnNode> calls)
			throws AnalyzerException {
		while (true) {
			if (n == 0)
				return;
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

	private void computeSubroutines() throws AnalyzerException {
		Subroutine main = new Subroutine(null, m.maxLocals, null);
		List<AbstractInsnNode> subroutineCalls = new ArrayList<AbstractInsnNode>();
		Map<LabelNode, Subroutine> subroutineHeads = new HashMap<LabelNode, Subroutine>();
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
		AbstractInsnNode insnNode = null;
		int insnOpcode = -1;
		for (int i = 0; i < n; i++) {
			if (subroutines[i] != null && subroutines[i].start == null) {
				subroutines[i] = null;
			}
			insnNode = insns.get(i);
			insnOpcode = insnNode.getOpcode();
			Subroutine subroutine = subroutines[i];
			if (subroutine != null) {
				if (insnNode instanceof VarInsnNode) {
					int var = ((VarInsnNode) insnNode).var;
					subroutine.access[var] = true;
					if (insnOpcode == LLOAD || insnOpcode == DLOAD || insnOpcode == LSTORE || insnOpcode == DSTORE) {
						subroutine.access[var + 1] = true;
					}
				} else if (insnNode instanceof IincInsnNode) {
					int var = ((IincInsnNode) insnNode).var;
					subroutine.access[var] = true;
				}
			}
		}
	}

	private void buildBlockMap() {
		getBlock(START_PC);// add start block
		getBlock(insns.size());// add exit block
		for (int i = 0; i < m.tryCatchBlocks.size(); i++) {
			TryCatchBlockNode tcb = (TryCatchBlockNode) m.tryCatchBlocks.get(i);
			int begin = insns.indexOf(tcb.start);
			int end = insns.indexOf(tcb.end);
			getBlock(begin);

			int indexOfHandler = insns.indexOf(tcb.handler);
			getBlock(indexOfHandler);

			for (int j = begin; j < end; j++) {
				List<TryCatchBlockNode> insnHandlers = handlers[j];
				if (insnHandlers == null) {
					insnHandlers = new ArrayList<TryCatchBlockNode>();
					handlers[j] = insnHandlers;
				}
				insnHandlers.add(tcb);
			}
		}
		AbstractInsnNode insnNode = null;
		int insnOpcode = -1;
		Subroutine subroutine = null;
		for (int insn = 1; insn < insns.size(); insn++) {// START_PC has already
			// been added to the
			// blockMap

			insnNode = insns.get(insn);
			insnOpcode = insnNode.getOpcode();
			// compute the subroutines for use of RET
			subroutine = subroutines[insn];
			subroutine = subroutine == null ? null : subroutine.copy();
			if (subroutine != null) {
				if (insnNode instanceof VarInsnNode) {
					int var = ((VarInsnNode) insnNode).var;
					subroutine.access[var] = true;
					if (insnOpcode == LLOAD || insnOpcode == DLOAD || insnOpcode == LSTORE || insnOpcode == DSTORE) {
						subroutine.access[var + 1] = true;
					}
				} else if (insnNode instanceof IincInsnNode) {
					int var = ((IincInsnNode) insnNode).var;
					subroutine.access[var] = true;
				}
			}

			if (insnNode instanceof JumpInsnNode) {
				JumpInsnNode j = (JumpInsnNode) insnNode;
				getBlock(insn + 1);

				int jump = insns.indexOf(j.label);
				getBlock(jump);
			} else if (insnNode instanceof LookupSwitchInsnNode) {
				LookupSwitchInsnNode lsi = (LookupSwitchInsnNode) insnNode;
				int jump = insns.indexOf(lsi.dflt);
				getBlock(jump);

				for (int i = 0; i < lsi.labels.size(); i++) {
					LabelNode label = (LabelNode) lsi.labels.get(i);
					jump = insns.indexOf(label);
					getBlock(jump);
				}
			} else if (insnNode instanceof TableSwitchInsnNode) {
				TableSwitchInsnNode tsi = (TableSwitchInsnNode) insnNode;
				int jump = insns.indexOf(tsi.dflt);
				getBlock(jump);

				for (int i = 0; i < tsi.labels.size(); i++) {
					LabelNode label = (LabelNode) tsi.labels.get(i);
					jump = insns.indexOf(label);
					getBlock(jump);
				}
			} else if (insnOpcode == RET) {
				for (int i = 0; i < subroutine.callers.size(); i++) {
					Object caller = subroutine.callers.get(i);
					int call = insns.indexOf((AbstractInsnNode) caller);
					getBlock(call + 1);
				}
			} else if (insnOpcode >= IRETURN && insnOpcode <= RETURN) {
				getBlock(n);
			}

			if (isExceptionThrow(insnOpcode)) {
				getBlock(insn + 1);
				List<TryCatchBlockNode> insnHandlers = handlers[insn];
				if (insnHandlers == null) {
					getBlock(n + 1, BlockType.UNHANDLE_EXCEPTION_BLOCK);
				}
			}
		}
	}

	private void sortLeaders() {
		Set<Integer> leaderSet = blockMap.keySet();
		leaderArray = new Integer[leaderSet.size()];
		int index = 0;

		for (Integer leader : leaderSet) {
			leaderArray[index++] = leader;
		}
		Arrays.sort(leaderArray);
	}

	private void buildCFG() {
		AbstractInsnNode insnNode = null;
		int insnOpcode = -1;
		int numOfBlocks = leaderArray.length;
		BitSet inCFG = new BitSet(numOfBlocks);
		BasicBlock currentBlock = null;
		Subroutine subroutine = null;
		for (int i = 0; i < leaderArray.length - 1; i++) {
			currentBlock = getBlock(leaderArray[i]);
			if (!inCFG.get(leaderArray[i])) {// the block is not added to CFG
				inCFG.set(leaderArray[i]);

				int endInc = leaderArray[i + 1] - 1;
				currentBlock.setEndInc(endInc);

				int indexOfInsn = leaderArray[i] + 1;
				while (indexOfInsn <= insns.size() - 1 && indexOfInsn <= endInc) {// ���indexOfInsnС�ڵ���insns��ָ���������к�
					// ��
					// indexOfInsnС�ڵ��ڼ�����ĵ�ǰblock��endInc��ֵ
					currentBlock.addNode(insns.get(indexOfInsn), null);
					indexOfInsn++;
				}
				if (endInc < n) {
					insnNode = insns.get(endInc);
					insnOpcode = insnNode.getOpcode();
					if (insnNode instanceof JumpInsnNode) {
						JumpInsnNode j = (JumpInsnNode) insnNode;
						int jump = insns.indexOf(j.label);
						BasicBlock block = getBlock(jump);
						Edge edge = cfg.createEdge(currentBlock, block);

						if (insnOpcode == JSR) {
							edge.setType(EdgeType.JSR_EDGE);
						} else if (insnOpcode == GOTO) {
							edge.setType(EdgeType.GOTO_EDGE);
						} else {
							edge.setType(EdgeType.IFCMP_EDGE_JUMP);
							currentBlock = changeBlock(currentBlock, BlockType.IF_BLOCK);
							IfBlock ifBlock = (IfBlock) currentBlock;
							ifBlock.jump = block;

							block = getBlock(endInc + 1);
							cfg.createEdge(currentBlock, block, EdgeType.IFCMP_EDGE_FALLTHROUGH);
							ifBlock.fallThrough = getBlock(endInc + 1);
						}
					} else if (insnNode instanceof LookupSwitchInsnNode) {
						currentBlock = changeBlock(currentBlock, BlockType.SWITCH_BLOCK);
						SwitchBlock switchBlock = (SwitchBlock) currentBlock;

						LookupSwitchInsnNode lsi = (LookupSwitchInsnNode) insnNode;
						int jump = insns.indexOf(lsi.dflt);
						BasicBlock block = getBlock(jump);
						cfg.createEdge(currentBlock, block, EdgeType.SWITCH_DEFAULT_EDGE);
						switchBlock.defaultCase = block;

						for (int j = 0; j < lsi.labels.size(); j++) {
							LabelNode label = (LabelNode) lsi.labels.get(j);
							jump = insns.indexOf(label);
							block = getBlock(jump);
							cfg.createEdge(currentBlock, block, EdgeType.SWITCH_EDGE);
							switchBlock.switchCase.put(lsi.keys.get(j), block);
						}
					} else if (insnNode instanceof TableSwitchInsnNode) {
						currentBlock = changeBlock(currentBlock, BlockType.SWITCH_BLOCK);
						SwitchBlock switchBlock = (SwitchBlock) currentBlock;

						TableSwitchInsnNode tsi = (TableSwitchInsnNode) insnNode;
						int jump = insns.indexOf(tsi.dflt);
						BasicBlock block = getBlock(jump);
						cfg.createEdge(currentBlock, block, EdgeType.SWITCH_DEFAULT_EDGE);
						switchBlock.defaultCase = block;

						for (int j = 0; j < tsi.labels.size(); j++) {
							LabelNode label = (LabelNode) tsi.labels.get(j);
							jump = insns.indexOf(label);
							block = getBlock(jump);
							cfg.createEdge(currentBlock, block, EdgeType.SWITCH_EDGE);
							switchBlock.switchCase.put(tsi.min + j, block);
						}
					} else if (insnOpcode == RET) {
						subroutine = subroutines[endInc] == null ? null : subroutines[endInc].copy();
						if (subroutine == null) {
							try {
								throw new AnalyzerException("RET instruction outside of a subroutine");
							} catch (AnalyzerException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						for (int j = 0; j < subroutine.callers.size(); j++) {
							Object caller = subroutine.callers.get(j);
							int call = insns.indexOf((AbstractInsnNode) caller);
							BasicBlock block = getBlock(call + 1);
							cfg.createEdge(currentBlock, block, EdgeType.RET_EDGE);
						}
					} else if (insnOpcode >= IRETURN && insnOpcode <= RETURN) {
						currentBlock = changeBlock(currentBlock, BlockType.RETURN_BLOCK);
						BasicBlock block = getBlock(n);
						if (cfg.getExit() == null)
							cfg.setExit(block);
						cfg.createEdge(currentBlock, block, EdgeType.RETURN_EDGE);
						cfg.createEdge(currentBlock, getBlock(endInc + 1), EdgeType.FALL_THROUGH_EDGE);
					} else if (insnOpcode == ATHROW) {
						BasicBlock block = getBlock(n);
						if (cfg.getExit() == null)
							cfg.setExit(block);
						cfg.createEdge(currentBlock, block, EdgeType.ATHROW_EDGE);
					} else {
						cfg.createEdge(currentBlock, getBlock(endInc + 1), EdgeType.FALL_THROUGH_EDGE);
					}
					if (isExceptionThrow(insnOpcode)) {
						List<TryCatchBlockNode> insnHandlers = handlers[endInc];
						if (insnHandlers != null) {
							for (int j = 0; j < insnHandlers.size(); j++) {
								TryCatchBlockNode tcb = (TryCatchBlockNode) insnHandlers.get(j);
								int jump = insns.indexOf(tcb.handler);
								BasicBlock block = getBlock(jump, BlockType.EXCEPTION_BLOCK);
								if (tcb.type == null) {
									if (currentBlock.getLabel() != block.getLabel()) {
										cfg.createEdge(currentBlock, block, EdgeType.FINALLY_EDGE);
									}
								} else {
									cfg.createEdge(currentBlock, block, EdgeType.HANDLED_EXCEPTION_EDGE);
								}
							}
						} else if (!currentBlock.pointTo(getBlock(n + 1))) {
							cfg.createEdge(currentBlock, getBlock(n + 1), EdgeType.UNHANDLED_EXCEPTION_EDGE);
						}
					}
				}
			}
		}
		if (blockMap.get(n + 1) != null) {
			cfg.createEdge(getBlock(n + 1), getBlock(n), EdgeType.FALL_THROUGH_EDGE);
		}

		// control flow analysis
		cfg.setRoot(getBlock(0));
		cfg.setExit(blockMap.get(n));
		// cfg.setSubroutines(subroutines);
		cfg.setHandlers(handlers);
	}

	public static void main(String[] args) {

		ClassNodeLoader loader = new ClassNodeLoader("bin/");
		ClassNode cc = loader.loadClassNode("Analysis");

		for (MethodNode method : cc.methods) {
			if (method.name.equals("analyze")) {
				CFGBuilder builder = new CFGBuilder(new BasicInterpreter());
				// SimpleCFGBuilder simpleB=new SimpleCFGBuilder(new
				// BasicInterpreter());
				try {
					Timer.start(builder);
					builder.analyze(cc.name, method);
					System.out.println("times:" + Timer.stop(builder) + "ms");
					// Timer.start(simpleB);
					// simpleB.analyze(cc.name, method);
					// System.out.println("times:"+Timer.stop(simpleB)+"ms");
					System.out.println(builder.create());
					System.out.println("Simple");
					// System.out.println(simpleB.getCFG(cc.name, method));
				} catch (AnalyzerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}

// end

/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-1-9 ����03:40:55
 * @modifier: Administrator
 * @time 2009-1-9 ����03:40:55
 * @reviewer: Administrator
 * @time 2009-1-9 ����03:40:55
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.analysis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.TryCatchBlockNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Frame;

import edu.pku.cn.analysis.common.Subroutine;
import edu.pku.cn.asm.tree.analysis.RealInterpreter;
import edu.pku.cn.asm.tree.analysis.RealValue;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.graph.cfg.BasicBlock;
import edu.pku.cn.graph.cfg.CFG;
import edu.pku.cn.graph.visit.ReversePostOrder;
import edu.pku.cn.hierarchy.Repository;

public class RealValueDataflowAnalysis extends BasicDataflowAnalysis<Frame> {

	private Map<Integer, List> localVariableMap = new HashMap<Integer, List>();

	private Set<LocalVariableNode> arguments = new HashSet<LocalVariableNode>();

	private RealInterpreter interpreter;

	private Repository repository;

	private MethodNode m;
	protected InsnList insns;
	protected Subroutine[] subroutines;
	protected List<TryCatchBlockNode>[] handlers;

	// private CFG cfg;
	public RealValueDataflowAnalysis(CFG cfg) {
		repository = Repository.getInstance();
		// this.cfg=cfg;
		blockOrder = new ReversePostOrder(cfg);
		isForwards = true;

		m = cfg.getMethod();
		insns = m.instructions;
		subroutines = cfg.getSubroutines();
		handlers = cfg.getHandlers();

		interpreter = new RealInterpreter(m.declaringClass.fields);
		interpreter.setInsns(insns);
		facts = new Frame[m.instructions.size()];
	}

	// public RealValueDataflowAnalysis(CFG cfg, RealInterpreter interpreter){
	// //super(cfg);
	// this.interpreter = interpreter;
	// this.blockOrder = new ReversePostOrder(cfg);
	// this.isForwards = true;
	//				
	// this.interpreter.setInsns(m.instructions);
	// maxLocals = m.maxLocals;
	// maxStack = m.maxStack;
	// facts = new Frame[m.instructions.size()];
	//		
	// this.repository = Repository.getInstance();
	// }
	// @Override
	// public void copy(Frame source, Frame dest) {
	// dest = new Frame(source);
	// }

	public Frame createEntryFact() {
		// TODO Auto-generated method stub
		InsnList insns = m.instructions;
		Frame frame = new Frame(m.maxLocals, m.maxStack);
		// �ѳ�ʼ������������ֲ������ￄ1�7
		Type[] args = Type.getArgumentTypes(m.desc);
		arguments.clear();
		boolean containsThis = false; // ���ڱ�ʶ�ֲ��������Ƿ���$this

		int local = 0;
		if ((m.access & Opcodes.ACC_STATIC) == 0) {
			Type type = Type.getObjectType(m.owner);
			frame.setLocal(local++, interpreter.newValue(type));
			containsThis = true;
		}
		for (int i = 0; i < args.length; i++) {
			frame.setLocal(local++, interpreter.newValue(args[i]));

			if (args[i].getSize() == 2) {
				frame.setLocal(local++, interpreter.newValue(null));
			}
		}
		while (local < m.maxLocals) {
			frame.setLocal(local++, interpreter.newValue(null));
		}

		interpreter.clearLocalVariables();
		// ����һ�����ɣ����ڱ������Զ��ϳɵķ�����args�ǲ��Զ�����localVariables�еġ���ô������֮�����������ǣ�args���ܲ�Ϊ�գ�����localVariablesΪ��
		if (args.length > 0) {
			if (m.localVariables.size() > 0) {
				if (((LocalVariableNode) m.localVariables.get(0)).desc.contains("$")) {// ���п�����һ���ڲ����ʼ������Ĵ�����ￄ1�7
					String pSubClassName = ((LocalVariableNode) m.localVariables.get(0)).desc;
					String pSuperClassName = args[0].toString();

					if (pSuperClassName.contains(";"))
						pSuperClassName = pSuperClassName.substring(0, pSuperClassName.indexOf(';'));// to
																										// remove
																										// ";"

					if (pSuperClassName.equals(pSubClassName.substring(0, pSubClassName.lastIndexOf('$')))) {// args[0]��Ϊ���ⲿ������ô��뷽���ￄ1�7
						interpreter.addLocalVariable(new LocalVariableNode(
						// create a local variable representing "$this"
								"$outer", pSuperClassName + ";", null, (LabelNode) (insns.get(blockOrder.getEntry()
										.getStartInc())), (LabelNode) (insns.get(insns.size() - 1)), 1));
					}
				}
			} else {
				// do nothing
			}
		}

		// if(args.length > 0 &&
		// ((LocalVariableNode)cfg.getMethod().localVariables.get(0)).desc.contains("$")){//���п�����һ���ڲ����ʼ������Ĵ�����ￄ1�7
		// String pSubClassName =
		// ((LocalVariableNode)cfg.getMethod().localVariables.get(0)).desc;
		// String pSuperClassName = args[0].toString();
		//
		// pSuperClassName = pSuperClassName.substring(0,
		// pSuperClassName.indexOf(';'));//to remove ";"
		//			
		// if(pSuperClassName.equals(pSubClassName.substring(0,
		// pSubClassName.lastIndexOf('$')))){//args[0]��Ϊ���ⲿ������ô��뷽���ￄ1�7
		// interpreter.addLocalVariable(new LocalVariableNode(//create a local
		// variable representing "$this"
		// "$outer", pSuperClassName, null,
		// (LabelNode)(insns.get(cfg.getRoot().getStartInc())),
		// (LabelNode)(insns.get(insns.size() - 1)),
		// 1));
		// }
		// }

		List<LocalVariableNode> lvNodes = m.localVariables;
		if (containsThis && 1 + args.length > lvNodes.size() || !containsThis && args.length > lvNodes.size()) {
			// �������û�а���ASM����ľֲ��������У���ĳЩ����£�ASM�����Ѵ������Ҳ��Ϊ�ֲ���������������������ĳ���ڲ����<init>��
			int baseNum = lvNodes.size();
			int currentSize = 0;
			if (insns.get(insns.size() - 1) instanceof LabelNode) {
				for (int i = 0; i < args.length; i++) {
					lvNodes.add(new LocalVariableNode(" ", args[i].toString(), null, (LabelNode) (insns.get(blockOrder
							.getEntry().getStartInc())), (LabelNode) (insns.get(insns.size() - 1)), baseNum
							+ currentSize));
					currentSize += args[i].getSize();// �Դ������ı��Ҫ�ɲ������š�����ǰ���������ռ�ݿռ����Ϣ��ͬ���ￄ1�7
				}
			} else {
				LabelNode lNode = new LabelNode(new Label());
				lNode.index = insns.size();// ����һ��label��Ϊ�˱�ʶLocalVariableNode���ڵķ�Χ
				for (int i = 0; i < args.length; i++) {
					lvNodes.add(new LocalVariableNode(" ", args[i].toString(), null, (LabelNode) (insns.get(blockOrder
							.getEntry().getStartInc())), lNode, baseNum + currentSize));
					currentSize += args[i].getSize();
				}
			}
			if (containsThis) {
				LabelNode lNode = new LabelNode(new Label());
				lNode.index = insns.size();
				interpreter.addLocalVariable(new LocalVariableNode(
				// create a local variable representing "this"
						"this", Type.getObjectType(m.owner).getDescriptor(), null, (LabelNode) (insns.get(blockOrder
								.getEntry().getStartInc())), lNode, 0));
			}
		}

		interpreter.addLocalVariables(lvNodes);

		// arguments: all local variables which are passed in as arguments
		if (args.length > 0) {
			if (containsThis) {
				for (int i = 1; i < args.length; i++) {
					arguments.add(lvNodes.get(i));
				}
			} else {
				for (int i = 0; i < args.length; i++) {
					arguments.add(lvNodes.get(i));
				}
			}
		}

		localVariableMap = interpreter.buildMapFromLocalVariables();// build
		// localVariableMap
		// from
		// localVariables
		return frame;
	}

	@Override
	public synchronized Frame createFact() {
		// TODO Auto-generated method stub
		Frame frame = new Frame(m.maxLocals, m.maxStack);
		for (int i = 0; i < m.maxLocals; i++) {
			frame.setLocal(i, interpreter.newValue(null));
		}
		return frame;
	}

	@Override
	public synchronized Frame createFact(Frame fact) {
		// TODO Auto-generated method stub
		return new Frame(fact);
	}

	@Override
	public synchronized void finishIteration() {
		// TODO Auto-generated method stub
		this.localVariableMap = interpreter.getLocalVariableMap();
	}

	// @Override
	// public BlockOrder getBlockOrder(CFG cfg) {
	// // TODO Auto-generated method stub
	// return blockOrder;
	// }

	// @Override
	// public Frame getFact(int index) {
	// // TODO Auto-generated method stub
	// return facts[index];
	// }
	/**
	 * provided to customers using this analysis, but not to SimpleDataflow.
	 * 
	 * @return
	 */
	// public Frame[] getFrames(){
	// return facts;
	// }

	public synchronized Set<LocalVariableNode> getArguments() {
		return this.arguments;
	}

	@Override
	public synchronized int getLastUpdateTimestamp(Frame fact) {
		// TODO Auto-generated method stub
		return fact.getLastUpdateTimestamp();
	}

	public synchronized Map<Integer, List> getLocalVariableMap() {
		return this.localVariableMap;
	}

	@Override
	public synchronized Frame getNewStartFact(BasicBlock block) {
		// TODO Auto-generated method stub
		return facts[block.getStartInc()];
	}

	// @Override
	// public Frame getResultFact(BasicBlock block) {
	// // TODO Auto-generated method stub
	// Frame frame = resultFactMap.get(block);
	// if(frame == null){
	// frame = createFact();
	// resultFactMap.put(block, frame);
	// }
	// return frame;
	// }

	// @Override
	// public Frame getStartFact(BasicBlock block) {
	// // TODO Auto-generated method stub
	// Frame frame = startFactMap.get(block);
	// if(frame == null){
	// frame = createFact();
	// startFactMap.put(block, frame);
	// }
	// return frame;
	// }

	@Override
	public synchronized void initEntryFact() {
		Frame frame = createEntryFact();
		frame.setLastUpdateTimestamp(0);
		startFactMap.put(blockOrder.getEntry(), frame);
		facts[0] = frame;
	}

	@Override
	public synchronized boolean isEndBlock(BasicBlock block) {
		// TODO Auto-generated method stub
		if (block.getStartInc() >= facts.length)
			return true;
		return false;
	}

	// @Override
	// public boolean isForwards() {
	// // TODO Auto-generated method stub
	// return this.isForwards;
	// }

	@Override
	public synchronized Frame merge(Frame start, Frame pred) {
		// TODO Auto-generated method stub
		Frame result = new Frame(start);
		if (!pred.equals(start) && start.getLastUpdateTimestamp() <= pred.getLastUpdateTimestamp()) {
			try {
				if (start.getStackSize() > 0) {
					String typeName = ((RealValue) (start.getStack(start.getStackSize() - 1))).getType().getClassName();
					if (typeName.endsWith("Throwable") || typeName.endsWith("Exception") || typeName.endsWith("Error")
							|| repository.instanceOf(typeName, "java.lang.Throwable")
							|| repository.instanceOf(typeName, "java.lang.Exception")
							|| repository.instanceOf(typeName, "java.lang.Error")) {
						pred.clearStack();
						for (int i = 0; i < start.getStackSize(); i++) {
							pred.push(start.getStack(i));// �������Ӧ�ĵ��ￄ1�7
						}
					} else {
						if (pred.getStackSize() != start.getStackSize()) {
							throw new AnalyzerException(
									"The stack hights of merged frames are different in RealValueDataflowAnalysis");
						}
					}
				}
				result.merge(pred, interpreter);
			} catch (AnalyzerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

	@Override
	public synchronized boolean same(Frame fact1, Frame fact2) {
		// TODO Auto-generated method stub
		if (fact1.getStackSize() != fact2.getStackSize() || fact1.getLocals() != fact2.getLocals())
			return false;
		for (int i = 0; i < fact1.getLocals(); i++) {
			if (!fact1.getLocal(i).equals(fact2.getLocal(i)))
				return false;
		}
		for (int i = 0; i < fact1.getStackSize(); i++) {
			if (!fact1.getStack(i).equals(fact2.getStack(i)))
				return false;
		}
		return true;
	}

	@Override
	public synchronized void setLastUpdateTimestamp(Frame fact, int timestamp) {
		// TODO Auto-generated method stub
		fact.setLastUpdateTimestamp(timestamp);
	}

	@Override
	public synchronized void setNewStartFact(BasicBlock block, Frame fact) {
		// TODO Auto-generated method stub
		facts[block.getStartInc()] = fact;
	}

	// @Override
	// public void setResultFact(BasicBlock block, Frame result) {
	// // TODO Auto-generated method stub
	// resultFactMap.put(block, result);
	// }

	// @Override
	// public void setStartFact(BasicBlock block, Frame fact) {
	// // TODO Auto-generated method stub
	// startFactMap.put(block, fact);
	// }

	@Override
	public synchronized void startIteration() {
		// TODO Auto-generated method stub

	}

	@Override
	public synchronized Frame transferVertex(BasicBlock block) {
		// TODO Auto-generated method stub
		boolean isChanged = true;

		Iterator<AbstractInsnNode> insnIter = block.nodeIterator(false);
		int insn = block.getStartInc() - 1;
		Subroutine subroutine = null;
		Frame frame = new Frame(startFactMap.get(block));
		AbstractInsnNode insnNode = null;
		while (insnIter.hasNext()) {
			isChanged = false;
			if (insn + 2 == facts.length)
				break;// if this is the last label after RETURN, ignore it
			insn++; // indicate the index of current insnNode

			try {
				insnNode = insnIter.next();
				subroutine = subroutines[insn];
				int insnOpcode = insnNode.getOpcode();
				int insnType = insnNode.getType();
				// if the insn does not change the frame at all, use the frame
				// to combine with the old frames directly
				if (insnType == AbstractInsnNode.LABEL || insnType == AbstractInsnNode.LINE
						|| insnType == AbstractInsnNode.FRAME) {
					isChanged |= merge(insn + 1, frame, subroutine);
				} else {
					frame.execute(insnNode, interpreter);
					if (insnNode instanceof JumpInsnNode) {
						JumpInsnNode j = (JumpInsnNode) insnNode;
						if (insnOpcode != Opcodes.GOTO && insnOpcode != Opcodes.JSR) {
							isChanged |= merge(insn + 1, frame, subroutine);
						}
						int jump = insns.indexOf(j.label);
						if (insnOpcode == Opcodes.JSR) {
							isChanged |= merge(jump, frame, subroutines[jump]);
						} else {
							isChanged |= merge(jump, frame, subroutine);
						}
					} else if (insnNode instanceof LookupSwitchInsnNode) {
						LookupSwitchInsnNode lsi = (LookupSwitchInsnNode) insnNode;
						int jump = insns.indexOf(lsi.dflt);
						isChanged = merge(jump, frame, subroutine);
						for (int j = 0; j < lsi.labels.size(); ++j) {
							LabelNode label = (LabelNode) lsi.labels.get(j);
							jump = insns.indexOf(label);
							isChanged |= merge(jump, frame, subroutine);
						}
					} else if (insnNode instanceof TableSwitchInsnNode) {
						TableSwitchInsnNode tsi = (TableSwitchInsnNode) insnNode;
						int jump = insns.indexOf(tsi.dflt);
						isChanged |= merge(jump, frame, subroutine);
						for (int j = 0; j < tsi.labels.size(); ++j) {
							LabelNode label = (LabelNode) tsi.labels.get(j);
							jump = insns.indexOf(label);
							isChanged |= merge(jump, frame, subroutine);
						}
					} else if (insnOpcode == Opcodes.RET) {
						for (int i = 0; i < subroutine.callers.size(); ++i) {
							Object caller = subroutine.callers.get(i);
							int call = insns.indexOf((AbstractInsnNode) caller);
							if (facts[call] != null) {
								isChanged |= merge(call + 1, facts[call], frame, subroutines[call], subroutine.access);
							}
						}
					} else if (insnOpcode != Opcodes.ATHROW
							&& (insnOpcode < Opcodes.IRETURN || insnOpcode > Opcodes.RETURN)) {
						isChanged |= merge(insn + 1, frame, subroutine);
					}
				}
				List<TryCatchBlockNode> insnHandlers = handlers[insn];
				if (insnHandlers != null) {
					for (int i = 0; i < insnHandlers.size(); i++) {
						TryCatchBlockNode tcb = (TryCatchBlockNode) insnHandlers.get(i);
						Type type;
						if (tcb.type == null) {
							type = Type.getObjectType("java/lang/Throwable");
						} else {
							type = Type.getObjectType(tcb.type);
						}
						int jump = insns.indexOf(tcb.handler);

						Frame handler = createFact(facts[insn]);// copy the
						// frame of an
						// insn in try
						// block
						handler.clearStack(); // set the value of top to 0
						handler.push(interpreter.newValue(type)); // push the
						// Throwable
						// Exception
						// to stack
						// handler.setLastUpdateTimestamp(handler.getLastUpdateTimestamp()
						// + 1);
						boolean handlerIsChanged = merge(jump, handler, subroutine);
						if (handlerIsChanged) {
							isChanged |= handlerIsChanged;
						}
					}
				}
			} catch (AnalyzerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// if(!isChanged){//if the frame remains the same, do not need to
			// compute any more
			// break;
			// }
		}
		// return frames[block.getEndInc()];
		return frame; // the frame after the EndInc
	}

	private boolean merge(final int insn, final Frame frame, final Subroutine subroutine) throws AnalyzerException {
		// frames represent those before instructions
		Frame oldFrame = facts[insn];
		Subroutine oldSubroutine = subroutines[insn];
		boolean change = false;
		// deal with oldFrame
		if (oldFrame == null) {
			facts[insn] = new Frame(frame);
			change = true;
		} else {
			Frame oldFrameCopy = new Frame(oldFrame);
			Frame result = merge(oldFrameCopy, frame);
			if (oldFrameCopy.equals(result)) {// do nothing, since nothing is
				// changed

			} else {
				facts[insn] = result;
				change = true;
			}
		}
		// deal with oldSubroutine
		if (oldSubroutine == null) {
			if (subroutine != null) {
				subroutines[insn] = subroutine.copy();
				change = true;
			}
		} else if (subroutine != null) {
			change |= oldSubroutine.merge(subroutine);
		}
		return change;
	}

	private boolean merge(final int insn, final Frame beforeJSR, final Frame afterRET,
			final Subroutine subroutineBeforeJSR, final boolean[] access) throws AnalyzerException {

		Frame oldFrame = facts[insn];
		Subroutine oldSubroutine = subroutines[insn];
		boolean changes;

		afterRET.merge(beforeJSR, access);

		if (oldFrame == null) {
			facts[insn] = new Frame(afterRET);
			changes = true;
		} else {
			changes = oldFrame.merge(afterRET, access);
		}

		if (oldSubroutine != null && subroutineBeforeJSR != null) {
			changes |= oldSubroutine.merge(subroutineBeforeJSR);
		}
		return changes;
	}
}
// end

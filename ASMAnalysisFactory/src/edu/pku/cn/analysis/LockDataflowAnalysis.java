/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-2-13 ����11:27:53
 * @modifier: Administrator
 * @time 2009-2-13 ����11:27:53
 * @reviewer: Administrator
 * @time 2009-2-13 ����11:27:53
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.analysis;

import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.TryCatchBlockNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Frame;

import edu.pku.cn.analysis.common.Subroutine;
import edu.pku.cn.analysis.factory.RealValueDataflowFactory;
import edu.pku.cn.asm.tree.analysis.LockMap;
import edu.pku.cn.asm.tree.analysis.RealValue;
import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.detector.DataflowTestDriver;
import edu.pku.cn.graph.cfg.CFG;
import edu.pku.cn.graph.cfg.BasicBlock;
import edu.pku.cn.graph.cfg.CFGFactory;
import edu.pku.cn.graph.visit.ReversePostOrder;
import edu.pku.cn.util.AnalysisFactoryManager;

public class LockDataflowAnalysis extends BasicDataflowAnalysis<LockMap> {

	private Frame[] frames;

	private boolean isSynchronized;

	private boolean isStatic;
	private MethodNode m;
	private Subroutine[] subroutines;
	private InsnList insns;
	private List<TryCatchBlockNode>[] handlers;

	public LockDataflowAnalysis(CFG cfg) {

		this.blockOrder = new ReversePostOrder(cfg);
		this.isForwards = true;
		RealValueDataflowFactory fac = (RealValueDataflowFactory) AnalysisFactoryManager
				.lookup(RealValueDataflowFactory.NAME);
		try {
			RealValueDataflowAnalysis rvAnalysis = fac.getAnalysis(cfg);
			frames = rvAnalysis.getFacts();
		} catch (AnalyzerException e) {
			e.printStackTrace();
		}
		subroutines = cfg.getSubroutines();
		m = cfg.getMethod();
		insns = m.instructions;
		handlers = cfg.getHandlers();
		this.facts = new LockMap[insns.size()];

		this.isStatic = ((m.access & Opcodes.ACC_STATIC) == Opcodes.ACC_STATIC);
		this.isSynchronized = ((m.access & Opcodes.ACC_SYNCHRONIZED) == Opcodes.ACC_SYNCHRONIZED);
		// TODO Auto-generated constructor stub
	}

	@Override
	public LockMap createFact() {
		LockMap map = new LockMap();
		return map;
	}

	@Override
	public LockMap createFact(LockMap fact) {
		LockMap map = new LockMap(fact);
		return map;
	}

	// @Override
	// public LockMap getFact(int index){
	// return facts[index];
	// }

	@Override
	public int getLastUpdateTimestamp(LockMap fact) {
		return fact.getLastUpdateTimestamp();
	}

	@Override
	public LockMap getNewStartFact(BasicBlock block) {
		return facts[block.getStartInc()];
	}

	// @Override
	// public LockMap getResultFact(BasicBlock block){
	// LockMap map = resultFactMap.get(block);
	// if(map == null){
	// map = new LockMap();
	// resultFactMap.put(block, map);
	// }
	// return map;
	// }

	// @Override
	// public LockMap getStartFact(BasicBlock block){
	// LockMap map = startFactMap.get(block);
	// if(map == null){
	// map = new LockMap();
	// startFactMap.put(block, map);
	// }
	// return map;
	// }

	@Override
	public void initEntryFact() {
		LockMap map = new LockMap();
		map.setLastUpdateTimestamp(0);
		if (this.isSynchronized && !this.isStatic) {
			map.put(m.localVariables.get(0), 1);
		} else if (this.isSynchronized && this.isStatic) {
			map.put(m.owner, 1);
		}

		startFactMap.put(blockOrder.getEntry(), map);
		facts[0] = map;
	}

	@Override
	public boolean isEndBlock(BasicBlock block) {
		if (block.getStartInc() >= insns.size())
			return true;
		return false;
	}

	@Override
	public LockMap merge(LockMap start, LockMap pred) {
		LockMap result = new LockMap(start);
		if (start.size() == 0) {
			result = new LockMap(start);
		} else if (!pred.equals(start) && start.getLastUpdateTimestamp() <= pred.getLastUpdateTimestamp()) {
			result.merge(pred);
		}
		return result;
	}

	@Override
	public boolean same(LockMap fact1, LockMap fact2) {
		if (fact1 == null)
			return fact2 == null;
		return fact1.equals(fact2);
	}

	@Override
	public void setLastUpdateTimestamp(LockMap fact, int timestamp) {
		fact.setLastUpdateTimestamp(timestamp);
	}

	@Override
	public void setNewStartFact(BasicBlock block, LockMap fact) {
		facts[block.getStartInc()] = fact;
	}

	// @Override
	// public void setResultFact(BasicBlock block, LockMap result){
	// resultFactMap.put(block, result);
	// }
	//	
	// @Override
	// public void setStartFact(BasicBlock block, LockMap fact){
	// startFactMap.put(block, fact);
	// }

	@Override
	public LockMap transferVertex(BasicBlock block) {
		boolean isChanged = true;

		Iterator<AbstractInsnNode> insnIter = block.nodeIterator(false);
		int insn = block.getStartInc() - 1;
		Subroutine subroutine = null;
		LockMap map = new LockMap(startFactMap.get(block));
		AbstractInsnNode insnNode = null;
		while (insnIter.hasNext()) {
			isChanged = false;
			if (insn + 2 == facts.length)
				break;
			insn++;

			try {
				insnNode = insnIter.next();
				subroutine = subroutines[insn];
				int insnOpcode = insnNode.getOpcode();

				if (insnOpcode == Opcodes.MONITORENTER || insnNode.getOpcode() == Opcodes.MONITOREXIT) {
					Frame frame = frames[insnNode.index];
					RealValue value = (RealValue) frame.getStack(frame.getStackSize() - 1);
					if (value.getVarObject() != null && insns.get(insn - 1) instanceof VarInsnNode
							&& insnOpcode == Opcodes.MONITORENTER) { // if this
						// is a
						// LocalVariableNode
						// or
						// FieldInsnNode
						lockOp(map, value.getVarObject(), ((VarInsnNode) insns.get(insn - 1)).var, 1);
					} else if (value.getVarObject() != null && insns.get(insn - 1) instanceof VarInsnNode
							&& insnOpcode == Opcodes.MONITOREXIT) {
						lockOp(map, value.getVarObject(), ((VarInsnNode) insns.get(insn - 1)).var, -1);
					}
				} else if (insnNode.getOpcode() >= Opcodes.IRETURN && insnNode.getOpcode() <= Opcodes.RETURN) {
					if (this.isSynchronized && !this.isStatic) {
						map.remove(m.localVariables.get(0));
					} else {
						map.remove(m.owner);
					}
				}

				if (insnNode instanceof JumpInsnNode) {
					JumpInsnNode j = (JumpInsnNode) insnNode;
					if (insnOpcode != Opcodes.GOTO && insnOpcode != Opcodes.JSR) {
						isChanged |= merge(insn + 1, map, subroutine);
					}
					int jump = insns.indexOf(j.label);
					if (insnOpcode == Opcodes.JSR) {
						isChanged |= merge(jump, map, subroutines[jump]);
					} else {
						isChanged |= merge(jump, map, subroutine);
					}
				} else if (insnNode instanceof LookupSwitchInsnNode) {
					LookupSwitchInsnNode lsi = (LookupSwitchInsnNode) insnNode;
					int jump = insns.indexOf(lsi.dflt);
					isChanged = merge(jump, map, subroutine);
					for (int j = 0; j < lsi.labels.size(); ++j) {
						LabelNode label = (LabelNode) lsi.labels.get(j);
						jump = insns.indexOf(label);
						isChanged |= merge(jump, map, subroutine);
					}
				} else if (insnNode instanceof TableSwitchInsnNode) {
					TableSwitchInsnNode tsi = (TableSwitchInsnNode) insnNode;
					int jump = insns.indexOf(tsi.dflt);
					isChanged |= merge(jump, map, subroutine);
					for (int j = 0; j < tsi.labels.size(); ++j) {
						LabelNode label = (LabelNode) tsi.labels.get(j);
						jump = insns.indexOf(label);
						isChanged |= merge(jump, map, subroutine);
					}
				} else if (insnOpcode == Opcodes.RET) {
					for (int i = 0; i < subroutine.callers.size(); ++i) {
						Object caller = subroutine.callers.get(i);
						int call = insns.indexOf((AbstractInsnNode) caller);
						if (frames[call] != null) {
							isChanged |= merge(call + 1, facts[call], map, subroutines[call], subroutine.access);
						}
					}
				} else if (insnOpcode != Opcodes.ATHROW
						&& (insnOpcode < Opcodes.IRETURN || insnOpcode > Opcodes.RETURN)) {
					isChanged |= merge(insn + 1, map, subroutine);
				}
				List<TryCatchBlockNode> insnHandlers = handlers[insn];
				if (insnHandlers != null) {
					for (int i = 0; i < insnHandlers.size(); i++) {
						TryCatchBlockNode tcb = (TryCatchBlockNode) insnHandlers.get(i);
						int jump = insns.indexOf(tcb.handler);

						LockMap handler = createFact(facts[insn]);// copy the
						// frame of an
						// insn in try
						// block
						// handler.setLastUpdateTimestamp(handler.getLastUpdateTimestamp()
						// + 1);
						boolean handlerIsChanged = merge(jump, handler, subroutine);
						isChanged |= handlerIsChanged;
					}
				}
			} catch (AnalyzerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return map;
	}

	private boolean merge(final int insn, final LockMap map, final Subroutine subroutine) throws AnalyzerException {
		// lockMap representing fact before instructions
		LockMap oldMap = facts[insn];
		Subroutine oldSubroutine = subroutines[insn];
		boolean change = false;
		// deal with oldMap
		if (oldMap == null) {
			facts[insn] = new LockMap(map);
			change = true;
		} else {
			LockMap oldMapCopy = new LockMap(oldMap);
			LockMap result = merge(oldMapCopy, map);
			if (oldMapCopy.equals(result)) {

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

	private boolean merge(final int insn, final LockMap beforeJSR, final LockMap afterRET,
			final Subroutine subroutineBeforeJSR, final boolean[] access) throws AnalyzerException {

		LockMap oldMap = facts[insn];
		Subroutine oldSubroutine = subroutines[insn];
		boolean changes;

		afterRET.merge(beforeJSR, access);

		if (oldMap == null) {
			facts[insn] = new LockMap(afterRET);
			changes = true;
		} else {
			changes = oldMap.merge(afterRET, access);
		}

		if (oldSubroutine != null && subroutineBeforeJSR != null) {
			changes |= oldSubroutine.merge(subroutineBeforeJSR);
		}
		return changes;
	}

	private void lockOp(LockMap fact, Object varObject, Object target, int delta) {

		// // the method getLockCount() cannot get the result I really need,
		// since the default equals() is not adequate
		// if(target == null){ //��Ӧ��MONITOREXIT
		// int count = fact.getLockCount(target);
		// count = count + delta;
		// fact.put(target, count);
		// if(count == 0){
		// fact.intToObject.remove(target);//intToObject����LockMap��¼��ǰ������ĸ�����
		// }
		// }else{//��Ӧ��MONITORENTER && MONITOREXIT
		int count = fact.getLockCount(target);
		count = count + delta;
		fact.put(target, count);
		if (count == 0)
			fact.intToObject.remove(target);
		else
			fact.intToObject.put((Integer) target, varObject);// �Ѿֲ�������ı��ӳ�䵽���Ӧ��varObject

	}

	// public LockMap[] getLockMaps(){
	// return this.facts;
	// }

	public static void main(String[] args) {
		DataflowTestDriver<LockMap, LockDataflowAnalysis> driver = new DataflowTestDriver<LockMap, LockDataflowAnalysis>() {

			@Override
			public SimpleDataflow<LockMap, LockDataflowAnalysis> createDataflow(ClassNode cc, MethodNode method) {
				// TODO Auto-generated method stub
				try {
					CFG cfg = ((CFGFactory) AnalysisFactoryManager.lookup(CFGFactory.NAME)).analyze(method);

					LockDataflowAnalysis analysis = new LockDataflowAnalysis(cfg);
					SimpleDataflow<LockMap, LockDataflowAnalysis> dataflow = new SimpleDataflow<LockMap, LockDataflowAnalysis>(
							cfg, analysis);
					dataflow.execute();
					return dataflow;
				} catch (AnalyzerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public void examineResults(SimpleDataflow<LockMap, LockDataflowAnalysis> dataflow) {
				// TODO Auto-generated method stub
				LockMap[] lockMaps = dataflow.getAnalysis().facts;
				for (int i = 0; i < lockMaps.length; i++) {
					System.out.println(lockMaps[i]);
				}
			}
		};
		driver.execute("TestAnalysisLock");
	}

}

// end

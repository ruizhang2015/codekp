/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-4-22 ����05:41:42
 * @modifier: Administrator
 * @time 2009-4-22 ����05:41:42
 * @reviewer: Administrator
 * @time 2009-4-22 ����05:41:42
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.TryCatchBlockNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Frame;

import edu.pku.cn.analysis.common.Subroutine;
import edu.pku.cn.analysis.factory.ObjectMethodInvokeDataFlowFactory;
import edu.pku.cn.asm.tree.analysis.HeapObjectInterpreter;
import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.detector.DataflowTestDriver;
import edu.pku.cn.graph.cfg.BasicBlock;
import edu.pku.cn.graph.cfg.CFG;
import edu.pku.cn.graph.visit.BlockOrder;
import edu.pku.cn.graph.visit.ReversePostOrder;
import edu.pku.cn.util.AnalysisFactoryManager;
import autoAdapter.ExecuteMethod;
import autoAdapter.HeapObject;
import autoAdapter.ProgramLocation;

public class ObjectInvokeMethodAnalysis extends BasicDataflowAnalysis<Frame> {

	// private RealValueDataflowAnalysis rvAnalysis;
	// private Map<Integer, List> localVariableMap;
	// private Map<LocalVariableNode, Integer> lvIndexMap;
	// private int size;
	private int maxLocals;
	private int maxStack;
	public ArrayList<ExecuteMethod>[] eMethods;
	public ArrayList<ExecuteMethod> allExecuteMethods = new ArrayList<ExecuteMethod>();
	public HeapObjectInterpreter interpreter;
	public ArrayList<HeapObject> parameters = new ArrayList<HeapObject>();

	public HashSet<HeapObject> heapObjects = new HashSet<HeapObject>();
	public HashSet<HeapObject> returnObject = new HashSet<HeapObject>();
	public HashMap<String, HeapObject> staticFields = new HashMap<String, HeapObject>();
	public HashMap<ProgramLocation, Integer> newObjectTimesMap = new HashMap<ProgramLocation, Integer>();
	public int insnIndex2line[];

	public HashSet<String> concernMethod = new HashSet<String>();

	boolean DEBUG = false;

	private MethodNode m;
	private Subroutine[] subroutines;
	private InsnList insns;
	private List<TryCatchBlockNode>[] handlers;

	public ObjectInvokeMethodAnalysis(CFG cfg) {
		m = cfg.getMethod();
		this.interpreter = new HeapObjectInterpreter();
		this.interpreter.setOimAnalysis(this);
		this.interpreter.className = m.owner;
		this.interpreter.methodName = m.name;
		this.interpreter.methodDesc = m.desc;

		this.blockOrder = new ReversePostOrder(cfg);
		this.isForwards = true;

		this.interpreter.setInsns(m.instructions);
		maxLocals = m.maxLocals;
		maxStack = m.maxStack;
		int insnSize = m.instructions.size();
		facts = new Frame[insnSize];
		eMethods = new ArrayList[insnSize];

		for (int i = 0; i < insnSize; i++) {
			eMethods[i] = new ArrayList<ExecuteMethod>();
		}

		insnIndex2line = new int[insnSize];
		int maxLine = -1;
		for (int i = 0; i < insnSize; i++) {

			if (insns.get(i) instanceof LineNumberNode) {
				maxLine = ((LineNumberNode) insns.get(i)).line;
			}
			insnIndex2line[i] = maxLine;
		}

	}

	public CFG getCfg() {
		return m.getCFG();
	}

	@Override
	public Frame createFact() {

		Frame frame = new Frame(maxLocals, maxStack);
		for (int i = 0; i < maxLocals; i++) {
			frame.setLocal(i, interpreter.newValue(null));
		}
		return frame;
	}

	@Override
	public Frame createFact(Frame fact) {
		// TODO Auto-generated method stub
		return new Frame(fact);
	}

	@Override
	public void finishIteration() {
		// TODO Auto-generated method stub
		super.finishIteration();
	}

	@Override
	public BlockOrder getBlockOrder() {
		// TODO Auto-generated method stub
		return super.getBlockOrder();
	}

	@Override
	public Frame getFact(int index) {
		// TODO Auto-generated method stub
		return super.getFact(index);
	}

	@Override
	public Frame[] getFacts() {
		// TODO Auto-generated method stub
		return super.getFacts();
	}

	@Override
	public int getLastUpdateTimestamp(Frame fact) {
		// TODO Auto-generated method stub
		return fact.getLastUpdateTimestamp();
	}

	@Override
	public Frame getNewStartFact(BasicBlock block) {
		// TODO Auto-generated method stub
		return facts[block.getStartInc()];
	}

	@Override
	public Frame getResultFact(BasicBlock block) {
		// TODO Auto-generated method stub
		return super.getResultFact(block);
	}

	@Override
	public Frame getStartFact(BasicBlock block) {
		// TODO Auto-generated method stub
		return super.getStartFact(block);
	}

	@Override
	public void initEntryFact() {
		// TODO Auto-generated method stub
		Frame fact = createEntryFact();
		fact.setLastUpdateTimestamp(0);
		startFactMap.put(blockOrder.getEntry(), fact);
		facts[0] = fact;
	}

	// ������������this
	public Frame createEntryFact() {
		Frame frame = new Frame(m.maxLocals, m.maxStack);

		Type[] args = Type.getArgumentTypes(m.desc);

		int local = 0;
		String thisTypeName = null;
		if ((m.access & Opcodes.ACC_STATIC) == 0) {
			Type type = Type.getObjectType(m.owner.replace('.', '/'));
			thisTypeName = type.getDescriptor().replace('.', '/');
			HeapObject hoThis = interpreter.newValue(0, type.toString(), HeapObject.DescThis);
			frame.setLocal(local++, hoThis);
			this.parameters.add(hoThis);
		} else {
			HeapObject sho;
			if (this.staticFields.containsKey(m.owner)) {
				sho = this.staticFields.get(m.owner);
			} else {
				sho = this.interpreter.newValue(0, m.owner, HeapObject.DescClass + m.owner);
				this.staticFields.put(m.owner, sho);
			}
			this.parameters.add(sho);
		}

		for (int i = 0; i < args.length; i++) {
			HeapObject p;
			if (thisTypeName != null && thisTypeName.indexOf('$') != -1 && i == 0) {
				String pSuperClassName = args[0].toString();
				if (pSuperClassName.indexOf(';') >= 0)
					pSuperClassName = pSuperClassName.substring(0, pSuperClassName.indexOf(';'));// ת��external
																									// name
				if (thisTypeName.startsWith(pSuperClassName)) {
					p = interpreter.newValue(0, args[0].toString(), "$outer");
					frame.setLocal(local++, p);
				} else {
					p = interpreter.newValue(0, args[i].toString(), HeapObject.DescParam + (i + 1));
					frame.setLocal(local++, p);
				}
			} else {
				p = interpreter.newValue(0, args[i].toString(), HeapObject.DescParam + (i + 1));
				frame.setLocal(local++, p);
			}
			if (p == null)
				this.parameters.add(HeapObject.BasicDontcare);
			else
				this.parameters.add(p);

			if (args[i].getSize() == 2) {
				frame.setLocal(local++, interpreter.newValue(null));
			}
		}

		while (local < m.maxLocals) {
			frame.setLocal(local++, interpreter.newValue(null));
		}
		return frame;
	}

	@Override
	public boolean isEndBlock(BasicBlock block) {
		// TODO Auto-generated method stub
		return block.getStartInc() >= insns.size();
	}

	@Override
	public boolean isForwards() {
		// TODO Auto-generated method stub
		return super.isForwards();
	}

	@Override
	public Frame merge(Frame start, Frame pred) {
		// TODO Auto-generated method stub
		Frame result = new Frame(start);
		// if(!pred.equals(start)
		// && start.getLastUpdateTimestamp() <= pred.getLastUpdateTimestamp()){
		// try{
		// if(pred.getStackSize() != start.getStackSize()){
		// // TODO �쳣�����merge
		// // String typeName =
		// ((IsNullValue)(start.getStack(start.getStackSize() -
		// 1))).getType().toString();
		// // if(typeName.endsWith("Throwable;") ||
		// typeName.endsWith("Exception;")){
		// // pred.clearStack();
		// // for(int i = 0; i < start.getStackSize(); i ++){
		// // pred.push(start.getStack(i));
		// // }
		// // }else{
		// // throw new
		// AnalyzerException("The stack hights of merged frames are different in IsNullValueDataflowAnalysis");
		// // }
		// }
		// result.merge(pred, interpreter);
		// }catch(AnalyzerException e){
		// e.printStackTrace();
		// }
		// }
		return result;
	}

	@Override
	public boolean same(Frame fact1, Frame fact2) {
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
	public void setLastUpdateTimestamp(Frame fact, int timestamp) {
		// TODO Auto-generated method stub
		fact.setLastUpdateTimestamp(timestamp);
	}

	@Override
	public void setNewStartFact(BasicBlock block, Frame fact) {
		// TODO Auto-generated method stub
		facts[block.getStartInc()] = fact;
	}

	@Override
	public void setResultFact(BasicBlock block, Frame result) {
		// TODO Auto-generated method stub
		super.setResultFact(block, result);
	}

	@Override
	public void setStartFact(BasicBlock block, Frame fact) {
		// TODO Auto-generated method stub
		super.setStartFact(block, fact);
	}

	@Override
	public void startIteration() {
		// TODO Auto-generated method stub
		super.startIteration();
	}

	@Override
	public Frame transferVertex(BasicBlock block) {
		// TODO analysis��״̬�ı�

		boolean isChanged = true;

		Iterator<AbstractInsnNode> insnIter = block.nodeIterator(false);
		int insn = block.getStartInc() - 1;
		Subroutine subroutine = null;
		Frame frame = new Frame(startFactMap.get(block));
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
				// if(DEBUG)
				// System.out.println(""+insn+" "+AutomaUtil.convertOpcodeToString(insnOpcode));
				int insnType = insnNode.getType();
				if (insnType == AbstractInsnNode.LABEL || insnType == AbstractInsnNode.LINE
						|| insnType == AbstractInsnNode.FRAME) {
					isChanged = merge(insn + 1, frame, subroutine);
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
						mergeExecuteMethod(insn, insn + 1);
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
						// ��Ҫ�Լ����merge
						boolean handlerIsChanged = merge(jump, handler, subroutine);
						if (handlerIsChanged) {
							isChanged |= handlerIsChanged;
						}
					}
				}
			} catch (AnalyzerException e) {
				e.printStackTrace();
			}
		}
		return frame;
	}

	private void mergeExecuteMethod(int source, int dest) {
		this.eMethods[dest].addAll(this.eMethods[source]);
	}

	private boolean merge(final int insn, final Frame frame, final Subroutine subroutine) throws AnalyzerException {
		Frame oldFrame = facts[insn];
		Subroutine oldSubroutine = subroutines[insn];
		boolean change = false;
		if (oldFrame == null) {
			facts[insn] = new Frame(frame);
			change = true;
		} else {
			change = oldFrame.merge(frame, interpreter);
		}

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

	public static void main(String[] args) {
		DataflowTestDriver<Frame, ObjectInvokeMethodAnalysis> driver = new DataflowTestDriver<Frame, ObjectInvokeMethodAnalysis>() {

			public SimpleDataflow<Frame, ObjectInvokeMethodAnalysis> createDataflow(ClassNode cc, MethodNode method) {
				// TODO Auto-generated method stub
				try {
					ObjectMethodInvokeDataFlowFactory factory = (ObjectMethodInvokeDataFlowFactory) AnalysisFactoryManager
							.lookup(ObjectMethodInvokeDataFlowFactory.NAME);
					ObjectInvokeMethodAnalysis omiAnalysis = factory.getAnalysis(method.getCFG());

					SimpleDataflow<Frame, ObjectInvokeMethodAnalysis> dataflow = new SimpleDataflow<Frame, ObjectInvokeMethodAnalysis>(
							method.getCFG(), omiAnalysis);
					dataflow.execute();
					return dataflow;
				} catch (AnalyzerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public void examineResults(SimpleDataflow<Frame, ObjectInvokeMethodAnalysis> dataflow) {
				// TODO Auto-generated method stub
				Frame[] facts = dataflow.getAnalysis().facts;
				for (int i = 0; i < facts.length; i++) {
					System.out.println(i + "   " + facts[i]);
				}
			}
		};
		driver.execute("TestFileInputStreamOpenClose");
	}

}

// end

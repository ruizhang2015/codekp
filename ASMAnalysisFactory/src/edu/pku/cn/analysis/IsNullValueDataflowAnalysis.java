/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-4-1 ����08:45:36
 * @modifier: Administrator
 * @time 2009-4-1 ����08:45:36
 * @reviewer: Administrator
 * @time 2009-4-1 ����08:45:36
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.analysis;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import edu.pku.cn.analysis.factory.RealValueDataflowFactory;
import edu.pku.cn.asm.tree.analysis.IsNullValue;
import edu.pku.cn.asm.tree.analysis.IsNullValueInterpreter;
import edu.pku.cn.asm.tree.analysis.RealValue;
import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.detector.DataflowTestDriver;
import edu.pku.cn.graph.cfg.BasicBlock;
import edu.pku.cn.graph.cfg.CFG;
import edu.pku.cn.graph.visit.ReversePostOrder;
import edu.pku.cn.hierarchy.Repository;
import edu.pku.cn.util.AnalysisFactoryManager;
import edu.pku.cn.util.OpcodeUtil;

public class IsNullValueDataflowAnalysis extends BasicDataflowAnalysis<Frame> {

	private static boolean DEBUG = true;

	private IsNullValueInterpreter interpreter;

	private Frame[] rvFacts;

	// private RealValueDataflowAnalysis rvAnalysis;

	private Map<Integer, List> localVariableMap;

	private Frame rvFact;

	private Repository repository;
	private MethodNode m;
	protected Subroutine[] subroutines;
	private InsnList insns;
	protected List<TryCatchBlockNode>[] handlers;

	public IsNullValueDataflowAnalysis(CFG cfg) {
		this.blockOrder = new ReversePostOrder(cfg);
		this.isForwards = true;
		RealValueDataflowFactory fac = (RealValueDataflowFactory) AnalysisFactoryManager
				.lookup(RealValueDataflowFactory.NAME);
		try {
			RealValueDataflowAnalysis rvAnalysis = fac.getAnalysis(cfg);
			rvFacts = rvAnalysis.getFacts();
			localVariableMap = rvAnalysis.getLocalVariableMap();
		} catch (AnalyzerException e) {
			e.printStackTrace();
		}
		subroutines = cfg.getSubroutines();
		m = cfg.getMethod();
		insns = m.instructions;
		handlers = cfg.getHandlers();
		facts = new Frame[m.instructions.size()];
		interpreter = new IsNullValueInterpreter(m.declaringClass.fields);
		interpreter.setLocalVariableMap(localVariableMap);
	}

	// public IsNullValueDataflowAnalysis(CFG cfg, RealValueDataflowAnalysis
	// rvAnalysis, IsNullValueInterpreter interpreter) {
	// this.interpreter = interpreter;
	// this.blockOrder = new ReversePostOrder(cfg);
	// this.isForwards = true;
	//		
	// this.interpreter.setInsns(m.instructions);
	// this.rvAnalysis = rvAnalysis;
	// maxLocals = m.maxLocals;
	// maxStack = m.maxStack;
	// facts = new Frame[m.instructions.size()];
	// rvFacts = rvAnalysis.getFacts();
	// localVariableMap = rvAnalysis.getLocalVariableMap();
	// interpreter.setLocalVariableMap(localVariableMap);
	//		
	// this.repository = Repository.getInstance();
	// }

	public Frame createEntryFact() {
		Frame frame = new Frame(m.maxLocals, m.maxStack);
		Type[] args = Type.getArgumentTypes(m.desc);
		int local = 0;
		// String thisTypeName = null;
		// if((m.access & Opcodes.ACC_STATIC) == 0){
		// Type type = Type.getObjectType(owner);
		// thisTypeName = type.getDescriptor().replace('.', '/');
		// frame.setLocal(local++, interpreter.newValue(type, "$this"));
		// }
		// for(int i = 0; i < args.length; i ++){
		// if(thisTypeName != null
		// && thisTypeName.indexOf('$') != -1
		// && i == 0){
		// String pSuperClassName = args[0].toString();
		//			    
		// if(pSuperClassName.contains(";"))
		// pSuperClassName = pSuperClassName.substring(0,
		// pSuperClassName.indexOf(';'));//ת��external name
		// if(thisTypeName.startsWith(pSuperClassName))
		// frame.setLocal(local++, interpreter.newValue(args[0], "$outer"));
		// else
		// frame.setLocal(local++, interpreter.newValue(args[i]));
		// }else{
		// frame.setLocal(local++, interpreter.newValue(args[i]));
		// }
		//			
		// if(args[i].getSize() == 2){
		// frame.setLocal(local++, interpreter.newValue(null));
		// }
		// }
		boolean containsThis = false;
		if ((m.access & Opcodes.ACC_STATIC) == 0) {
			Type type = Type.getObjectType(m.owner);
			frame.setLocal(local++, interpreter.newValue(type, "$this"));
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
		return frame;
	}

	@Override
	public Frame createFact() {
		Frame frame = new Frame(m.maxLocals, m.maxStack);
		for (int i = 0; i < m.maxLocals; i++) {
			frame.setLocal(i, interpreter.newValue(null));
		}
		return frame;
	}

	@Override
	public Frame createFact(Frame fact) {
		return new Frame(fact);
	}

	@Override
	public int getLastUpdateTimestamp(Frame fact) {
		return fact.getLastUpdateTimestamp();
	}

	@Override
	public Frame getNewStartFact(BasicBlock block) {
		return facts[block.getStartInc()];
	}

	@Override
	public void initEntryFact() {
		Frame fact = createEntryFact();
		fact.setLastUpdateTimestamp(0);
		startFactMap.put(blockOrder.getEntry(), fact);
		facts[0] = fact;
	}

	@Override
	public boolean isEndBlock(BasicBlock block) {
		if (block.getStartInc() >= facts.length)
			return true;
		return false;
	}

	@Override
	public Frame merge(Frame start, Frame pred) {
		Frame result = new Frame(start);

		if (!pred.equals(start) && start.getLastUpdateTimestamp() <= pred.getLastUpdateTimestamp()) {
			try {
				if (start.getStackSize() > 0) {
					String typeName = ((IsNullValue) (start.getStack(start.getStackSize() - 1))).getType()
							.getClassName();
					if (typeName.endsWith("Throwable") || typeName.endsWith("Exception") || typeName.endsWith("Error")
							|| repository.instanceOf(typeName, "java.lang.Throwable")
							|| repository.instanceOf(typeName, "java.lang.Exception")
							|| repository.instanceOf(typeName, "java.lang.Error")) {
						pred.clearStack();
						for (int i = 0; i < start.getStackSize(); i++) {
							pred.push(start.getStack(i));// �������Ӧ�ĵ��ￄ1�7
						}
					} else if (pred.getStackSize() != start.getStackSize()) {
						throw new AnalyzerException(
								"The stack hights of merged frames are different in IsNullValueDataflowAnalysis");
					}
				}
				// //�Ծֲ��������ж���Ĵ��?���pred��ĳЩ�ֲ������Ѿ���ʱ(ͨ����������start�����Ͳ�һ�����ж�)�����˺�start���кϲ�����ȥ����Щ��ʱ�ľֲ�����
				// Frame newPred = createFact(pred);
				// int locals = start.getLocals();
				// for(int i = 0; i < locals; i ++){
				// IsNullValue startInv = (IsNullValue)start.getLocal(i);
				// IsNullValue predInv =
				// (IsNullValue)newPred.getLocal(i);//�˴���Ϊ��Type.VOID_TYPE����ʾ��IsNullValue������������Ϣ
				// if(startInv != null && predInv != null &&
				// !startInv.getType().equals(Type.VOID_TYPE) &&
				// !startInv.getType().equals(predInv.getType())){
				// predInv.setType(Type.VOID_TYPE);//����������ΪVOID_TYPE���ϲ���ʱ��Ͳ��ῼ�����Ӧ��ֵ��ʲô
				// }
				// }
				//    			
				// result.merge(newPred, interpreter);
				result.merge(pred, interpreter);
			} catch (AnalyzerException e) {
				e.printStackTrace();
			}
		}
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
		fact.setLastUpdateTimestamp(timestamp);
	}

	@Override
	public void setNewStartFact(BasicBlock block, Frame fact) {
		facts[block.getStartInc()] = fact;
	}

	@Override
	public Frame transferVertex(BasicBlock block) {
		boolean isChanged = true;

		Iterator<AbstractInsnNode> insnIter = block.nodeIterator(false);
		int insn = block.getStartInc() - 1;
		Subroutine subroutine = null;
		Frame frame = new Frame(startFactMap.get(block));
		AbstractInsnNode insnNode = null;
		try {
			while (insnIter.hasNext()) {
				isChanged = false;
				if (insn + 2 == facts.length)
					break;
				insn++;

				insnNode = insnIter.next();
				subroutine = subroutines[insn];
				int insnOpcode = insnNode.getOpcode();
				int insnType = insnNode.getType();
				if (insnType == AbstractInsnNode.LABEL || insnType == AbstractInsnNode.LINE
						|| insnType == AbstractInsnNode.FRAME) {
					isChanged = merge(insn + 1, frame, subroutine);
				} else {
					LocalVariableNode lvNode = null;
					int newInvValueFollow = 0;
					int newInvValueJump = 0;

					if (insnOpcode == Opcodes.IFNULL || insnOpcode == Opcodes.IFNONNULL) {// ���Ӷ���IFNONNULL��IFNULL�Ķ��⴦��
						rvFact = rvFacts[insnNode.index];
						RealValue rv = (RealValue) rvFact.getStack(rvFact.getStackSize() - 1);
						newInvValueFollow = (insnOpcode == Opcodes.IFNULL ? IsNullValue.DEFINITE_NONNULL
								: IsNullValue.DEFINITE_NULL);
						newInvValueJump = (insnOpcode == Opcodes.IFNULL ? IsNullValue.DEFINITE_NULL
								: IsNullValue.DEFINITE_NONNULL);
						if (rv.getVarObject() != null) {
							Object obj = rv.getVarObject();
							if (obj instanceof LocalVariableNode) { // ���ǰ�ж��Ƿ�Ϊnull�ı�����Դ�ھֲ�����
								lvNode = (LocalVariableNode) obj;
							} else {// obj instanceof FieldNode do nothing when
								// it is fieldNode because fieldNode has
								// only one status
								// fNode = (FieldNode)obj;
							}
						}
					}
					frame.execute(insnNode, interpreter);

					if (insnNode instanceof JumpInsnNode) {
						JumpInsnNode j = (JumpInsnNode) insnNode;
						if (insnOpcode != Opcodes.GOTO && insnOpcode != Opcodes.JSR) {
							if (lvNode != null) {// frame��Ҫ�޸�
								Frame newFrame = createFact(frame);
								IsNullValue inv = (IsNullValue) newFrame.getLocal(lvNode.index);
								IsNullValue newInv = (IsNullValue) interpreter.newValue(newInvValueFollow, inv
										.getType());
								newFrame.setLocal(lvNode.index, newInv);
								isChanged |= merge(insn + 1, newFrame, subroutine);
							} else {
								isChanged |= merge(insn + 1, frame, subroutine);
							}
						}
						int jump = insns.indexOf(j.label);
						if (insnOpcode == Opcodes.JSR) {
							isChanged |= merge(jump, frame, subroutines[jump]);
						} else {// ���Ӷ�IFNULL��IFNONNULL�Ķ��⴦��
							if (lvNode != null) {
								Frame newFrame = createFact(frame);
								IsNullValue inv = (IsNullValue) newFrame.getLocal(lvNode.index);
								IsNullValue newInv = (IsNullValue) interpreter.newValue(newInvValueJump, inv.getType());
								newFrame.setLocal(lvNode.index, newInv);
								isChanged |= merge(jump, newFrame, subroutine);
							} else {
								isChanged |= merge(jump, frame, subroutine);
							}
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
					// else if (insnOpcode == Opcodes.ATHROW){
					// frame = facts[insn + 1];
					// //�����athrow������������resultӦ����ԭresult���Ϊ��֪����һblock��startFact��athrow���ı�ԭ����״̬
					// }
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

					Frame handler = createFact(facts[insn]);// copy the frame of
					// an insn in try
					// block
					handler.clearStack(); // set the value of top to 0
					handler.push(interpreter.newValue(IsNullValue.DEFINITE_NONNULL, type)); // push
																							// the
					// Throwable
					// Exception
					// to stack
					boolean handlerIsChanged = merge(jump, handler, subroutine);
					if (handlerIsChanged) {
						isChanged |= handlerIsChanged;
					}
				}
			}
		} catch (AnalyzerException e) {
			e.printStackTrace();
		}
		return frame;
	}

	private boolean merge(final int insn, final Frame frame, final Subroutine subroutine) throws AnalyzerException {
		Frame oldFrame = facts[insn];
		Subroutine oldSubroutine = subroutines[insn];
		boolean change = false;

		Frame newFrame = createFact(frame);
		// prepareFrameForMerge(insn,
		// newFrame);//�ںϲ�֮ǰ���Ѵ�������frame�в����ʵľֲ������滻�����Ӷ�֤�ϲ�������Ч��

		if (oldFrame == null) {
			facts[insn] = new Frame(newFrame);
			change = true;
		} else {
			Frame oldFrameCopy = new Frame(oldFrame);
			Frame result = merge(oldFrameCopy, frame);
			if (oldFrameCopy.equals(result)) {
				// do nothing
			} else {
				oldFrame = result;
				change = true;
			}
			// change = oldFrame.merge(newFrame, interpreter);
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

	/**
	 * perform changes on the given frame so that it is prepared to merge with
	 * other frame(s)
	 * 
	 * @param insn
	 * @param frame
	 */
	private void prepareFrameForMerge(int insn, Frame frame) {
		int locals = frame.getLocals();// �����в���ǰinsnλ�õľֲ������滻��
		for (int i = 0; i < locals; i++) {
			Type type = ((IsNullValue) frame.getLocal(i)).getType();
			List lvList = localVariableMap.get(i);
			if (lvList != null) {
				int maxStart = -1;
				int minEnd = Integer.MAX_VALUE;
				LocalVariableNode tempNode = null;
				for (int j = 0; j < lvList.size(); j++) {
					LocalVariableNode lvNode = (LocalVariableNode) lvList.get(j);
					if (lvNode.start.index <= insn + 1 && lvNode.end.index >= insn && lvNode.start.index >= maxStart
							&& lvNode.end.index <= minEnd) {
						maxStart = lvNode.start.index;
						minEnd = lvNode.end.index;
						tempNode = lvNode;
					}
				}
				if (tempNode == null) {
					if (insn < insns.size())
						frame.setLocal(i, interpreter.newValue(Type.VOID_TYPE));
					else {// do nothing, since this is not an actual insn with a
						// valid frame

					}
					// throw new
					// AnalyzerException("There is no matched localVariableNode with the given index");
				} else {
					if (!Type.getType(tempNode.desc).getClassName().equals(type.getClassName())) {// ����ҵ�����Ч�ֲ��������ľֲ��������Ͳ�ͬ��˵����ľֲ����������ڱ������򣬾͸ðѾֲ�������Ϊnull
						frame.setLocal(i, interpreter.newValue(Type.getType(tempNode.desc)));
					}
				}
			}
		}
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
		DataflowTestDriver<Frame, IsNullValueDataflowAnalysis> driver = new DataflowTestDriver<Frame, IsNullValueDataflowAnalysis>() {

			@Override
			public SimpleDataflow<Frame, IsNullValueDataflowAnalysis> createDataflow(ClassNode cc, MethodNode method) {
				// TODO Auto-generated method stub
				try {
					CFG cfg = method.getCFG();
					RealValueDataflowFactory factory = (RealValueDataflowFactory) AnalysisFactoryManager
							.lookup(RealValueDataflowFactory.NAME);
					RealValueDataflowAnalysis rvAnalysis = factory.getAnalysis(cfg);

					IsNullValueInterpreter interpreter = new IsNullValueInterpreter(cc.fields);
					interpreter.setInsns(cfg.getMethod().instructions);
					IsNullValueDataflowAnalysis analysis = new IsNullValueDataflowAnalysis(cfg);

					SimpleDataflow<Frame, IsNullValueDataflowAnalysis> dataflow = new SimpleDataflow<Frame, IsNullValueDataflowAnalysis>(
							cfg, analysis);

					if (DEBUG) {
						OpcodeUtil.printInsnList(method.instructions, method.name);

					}

					dataflow.execute();
					return dataflow;
				} catch (AnalyzerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public void examineResults(SimpleDataflow<Frame, IsNullValueDataflowAnalysis> dataflow) {
				// TODO Auto-generated method stub
				Frame[] facts = dataflow.getAnalysis().facts;
				for (int i = 0; i < facts.length; i++) {
					System.out.println(i + "   " + facts[i]);
				}
			}
		};
		driver.execute("TestAnalysisIsNullValue");
	}
}

// end

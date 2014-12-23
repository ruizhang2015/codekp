/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-3-25 锟斤拷锟斤拷04:31:40
 * @modifier: Administrator
 * @time 2009-3-25 锟斤拷锟斤拷04:31:40
 * @reviewer: Administrator
 * @time 2009-3-25 锟斤拷锟斤拷04:31:40
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.sysfix;

import java.io.File;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import automachine.AutoMachine;
import automachine.AutoMachineException;
import automachine.AutomaUtil;
import automachine.State;
import automachine.VisitMethodEndEdge;
import automachine.VisitMethodInsnEdge;
import automachine.VisitNewInsnEdge;
import automachine.VisitNullCheckIfStmtEdge;
import automachine.VisitVarDerefStmtEdge;
import edu.pku.cn.Project;
import edu.pku.cn.analysis.CallingContext;
import edu.pku.cn.analysis.InterJIRValue;
import edu.pku.cn.analysis.InterPointsToDataflowAnalysis;
import edu.pku.cn.analysis.InterVarInfo;
import edu.pku.cn.analysis.InterValueDataflowAnalysis;
import edu.pku.cn.analysis.PreconditionsDataflowAnalysis;
import edu.pku.cn.analysis.RefactoredBasicDataflowAnalysis;
import edu.pku.cn.analysis.RefactoredDataflow;
import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.classfile.LocalVariableNode;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.detector.Detector;
import edu.pku.cn.detector.RefactoredDataflowTestDriver;
import edu.pku.cn.graph.cfg.BasicBlock;
import edu.pku.cn.graph.cfg.CFG;
import edu.pku.cn.graph.cfg.Edge;
import edu.pku.cn.graph.cfg.EdgeType;
import edu.pku.cn.graph.visit.ReversePostOrder;
import edu.pku.cn.hierarchy.Repository;
import edu.pku.cn.jir.ArrayRef;
import edu.pku.cn.jir.AssignStmt;
import edu.pku.cn.jir.BinopExpr;
import edu.pku.cn.jir.Expr;
import edu.pku.cn.jir.FieldRef;
import edu.pku.cn.jir.IfStmt;
import edu.pku.cn.jir.InvokeExpr;
import edu.pku.cn.jir.InvokeStmt;
import edu.pku.cn.jir.JIRValue;
import edu.pku.cn.jir.LabelStmt;
import edu.pku.cn.jir.LengthOfExpr;
import edu.pku.cn.jir.LineStmt;
import edu.pku.cn.jir.LocalRef;
import edu.pku.cn.jir.NewExpr;
import edu.pku.cn.jir.Null;
import edu.pku.cn.jir.ParamRef;
import edu.pku.cn.jir.ReturnStmt;
import edu.pku.cn.jir.Stmt;
import edu.pku.cn.jir.TempRef;
import edu.pku.cn.jir.This;
import edu.pku.cn.jir.ThrowStmt;
import edu.pku.cn.util.HASelect;

// Reason to modify: using the points to analysis to implement an
// inter-procedural instance-based defect analysis
// + recording the process context which will be used to compute the precision
// of each warning
// Date: 20101114

public class SysFixInterDataflowAnalysis extends RefactoredBasicDataflowAnalysis<HashMap<String, HashSet<AutoMachine>>> {

	public boolean Debug = false;
	public boolean escapeSensitive = true;

	public AutoMachine sampleAutomachine;
	public HashSet<String> focusedObjects = new HashSet<String>();
	// automachine file path
	public String specificFile;
	// automachine database id
	int automachineId;

	boolean applyForChild = false;
	Type[] applyType;

	HashMap<String, HashSet<AutoMachine>> nodeAm = new HashMap<String, HashSet<AutoMachine>>();

	int currentLine;
	String desc;
	boolean isInConstructor = false;

	public InterValueDataflowAnalysis ptAnalysis;
	public PreconditionsDataflowAnalysis preConditionsAnalysis;

	private InsnList insns;
	private List<Stmt> stmts;

	private int maxInterLevel = 4;

	// used for inter-procedure analysis
	private CallingContext callingContext;
	private int level = 0;
	private HashMap<InterJIRValue, InterVarInfo> initPointsToFact;
	private HashMap<String, HashSet<AutoMachine>> initAMFact;

	private long[] lineNumbers; // line numbers of interested
	private Map<Long, HashSet<BasicBlock>> interestedBlocks = new HashMap<Long, HashSet<BasicBlock>>(); // line
	// number
	// ->
	// blocks
	private Map<Long, List<JIRValue>> interestedVariables = new HashMap<Long, List<JIRValue>>(); // line

	private Map<Long, List<BasicBlock>> backwardPaths = new HashMap<Long, List<BasicBlock>>();
	private Map<Long, List<BasicBlock>> forwardPaths = new HashMap<Long, List<BasicBlock>>();

	private Map<Long, BasicBlock> sliceBasedBlock = new HashMap<Long, BasicBlock>();
	private Map<Long, List<Stmt>> slicedBackwardPaths = new HashMap<Long, List<Stmt>>();
	private Map<Long, List<Stmt>> slicedForwardPaths = new HashMap<Long, List<Stmt>>();
	private Map<Long, List<Integer>> slicedBackwardStmtIndexes = new HashMap<Long, List<Integer>>();
	private Map<Long, List<Integer>> slicedForwardStmtIndexes = new HashMap<Long, List<Integer>>();
	private Map<Long, List<BasicBlock>> slicedBackwardStmtBlocks = new HashMap<Long, List<BasicBlock>>();
	private Map<Long, List<BasicBlock>> slicedForwardStmtBlocks = new HashMap<Long, List<BasicBlock>>();
	private boolean isFixOmissionDetection = false;

	// used for recording the precision discounts
	public SysFixInterDataflowAnalysis(AutoMachine am) {
		this.isForwards = true;
		this.automachineId = am.automachineId;
		this.sampleAutomachine = am;
		am.initial();
	}

	public void setAnalyzedMethod(CFG cfg) {
		this.blockOrder = new ReversePostOrder(cfg);
		this.cfg = cfg;
		this.stmts = cfg.getJIRStmt();
		this.insns = cfg.getMethod().instructions;
	}

	public void setPTAnalysis(CFG cfg) {
		this.ptAnalysis = new InterValueDataflowAnalysis(cfg);
		try {
			ptAnalysis.execute();
			// anlysis.printCallees();
			// anlysis.examineResults();
		} catch (AnalyzerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setPreconditionsAnalysis(CFG cfg) {
		this.preConditionsAnalysis = new PreconditionsDataflowAnalysis(cfg);
		try {
			preConditionsAnalysis.execute();
		} catch (AnalyzerException e) {
			e.printStackTrace();
		}
	}

	private void setPTAnalysis(CFG calleeCFG, HashMap<InterJIRValue, InterVarInfo> ptFact, int calleeLevel,
			CallingContext callingContext2) {
		this.ptAnalysis = new InterValueDataflowAnalysis(calleeCFG, ptFact, calleeLevel, callingContext2);
		try {
			ptAnalysis.execute();
			// anlysis.printCallees();
			// anlysis.examineResults();
		} catch (AnalyzerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public SysFixInterDataflowAnalysis(CFG cfg, String specificFile) {
		this.blockOrder = new ReversePostOrder(cfg);
		this.isForwards = true;
		this.cfg = cfg;
		this.stmts = cfg.getJIRStmt();
		this.insns = cfg.getMethod().instructions;

		this.specificFile = specificFile;
		this.sampleAutomachine = AutomaUtil.generateAutoMachineFromXML(specificFile);
		if (sampleAutomachine.getOnlyConcernClass() != null && !"".equals(sampleAutomachine.getOnlyConcernClass())) {
			applyForChild = true;
			String[] name = sampleAutomachine.getOnlyConcernClass().split(";");
			applyType = new Type[name.length];
			for (int i = 0; i < name.length; i++) {
				applyType[i] = Type.getObjectType(name[i]);
			}
		}

		// this.setAnalyzedMethod(cfg);
		this.setPTAnalysis(cfg);
		this.setPreconditionsAnalysis(cfg);
	}

	public SysFixInterDataflowAnalysis(CFG cfg, String specificFile, long[] lineNumbers) {
		this(cfg, specificFile);
		this.lineNumbers = lineNumbers;
	}

	private boolean isAssignableFrom(Type type) {
		if (applyType == null || applyType.length == 0) {
			return true;
		}

		for (int i = 0; i < applyType.length; i++) {
			if (HASelect.isAssignableFrom(applyType[i], type))
				return true;
		}
		return false;
	}

	private HashSet<AutoMachine> getAM(JIRValue key, HashMap<String, HashSet<AutoMachine>> fact,
			HashMap<InterJIRValue, InterVarInfo> ptFact) {
		Type keyType = key.getType();
		HashSet<AutoMachine> ams = new HashSet<AutoMachine>();

		if (keyType == null || keyType.getSort() < Type.ARRAY)
			return null;// / 当前我们只处理对象时序关系
		if (key instanceof LocalRef) {
			LocalRef l = (LocalRef) key;
			if (l.getName().equals("this"))
				return null;
		}
		if (applyForChild) {
			if (!isAssignableFrom(keyType))
				return null;
		}

		InterVarInfo ptInfo = ptFact.get(new InterJIRValue(key, this.level));
		if (ptInfo != null && ptInfo.getObjects() != null) {
			Set<String> objects = ptInfo.getObjects();
			HashSet<AutoMachine> am;
			for (String obj : objects) {
				am = fact.get(obj);
				if (am == null) {
					am = new HashSet<AutoMachine>();
					AutoMachine newMachine = sampleAutomachine.getCopy();
					newMachine.automachineName = obj;
					am.add(newMachine);
					fact.put(obj, am);
				}
				ams.addAll(am);
			}
		}

		return ams;
	}

	private HashSet<AutoMachine> getAM(JIRValue key, HashMap<JIRValue, HashSet<AutoMachine>> fact) {
		if (key instanceof Null) {
			return null;
		}

		Type keyType = key.getType();

		if (keyType == null || keyType.getSort() < Type.ARRAY)
			return null;// / 当前我们只处理对象时序关系
		if (key instanceof LocalRef) {
			LocalRef l = (LocalRef) key;
			if (l.getName().equals("this"))
				return null;
		}
		if (applyForChild) {
			if (!isAssignableFrom(keyType))
				return null;
		}

		HashSet<AutoMachine> am = fact.get(key);
		if (am == null) {
			am = new HashSet<AutoMachine>();
			AutoMachine newMachine = sampleAutomachine.getCopy();
			newMachine.automachineName = key.toString();
			am.add(newMachine);
			fact.put(key, am);
		}

		return am;
	}

	private HashSet<AutoMachine> getAMForMatching(JIRValue key, HashMap<String, HashSet<AutoMachine>> fact,
			HashMap<InterJIRValue, InterVarInfo> ptFact) {
		if (key instanceof Null) {
			return null;
		}

		Type keyType = key.getType();

		if (keyType == null || keyType.getSort() < Type.ARRAY)
			return null;// / 当前我们只处理对象时序关系
		if (key instanceof LocalRef) {
			LocalRef l = (LocalRef) key;
			if (l.getName().equals("this"))
				return null;
		}
		if (applyForChild) {
			if (!isAssignableFrom(keyType))
				return null;
		}

		InterJIRValue interJIRValue = new InterJIRValue(key, this.level);
		InterVarInfo ptInfo = ptFact.get(interJIRValue);

		if (ptInfo == null) {
			ptInfo = new InterVarInfo();
			String objStr = "";

			if (key instanceof ParamRef) {
				objStr = "O[" + key.getType() + "][param:" + key.toString() + "]" + "[" + this.level + "][]";
			} else if (key.toString().startsWith("this")) {
				objStr = "O[" + key.getType() + "][classField:" + key.toString() + "]" + "[" + this.level + "][]";
			} else {
				objStr = "O[" + key.getType() + "][unexpected:" + key.toString() + "]" + "[" + this.level + "][]";
			}

			ptInfo.objects.add(objStr);
			ptInfo.getTypes().add(key.getType());
			ptFact.put(interJIRValue, ptInfo);
		}

		HashSet<AutoMachine> ams = new HashSet<AutoMachine>();
		if (ptInfo != null && ptInfo.getObjects() != null) {
			Set<String> objects = ptInfo.getObjects();
			HashSet<AutoMachine> am = null;
			for (String obj : objects) {

				// if the fix omission detection is undergoing, need to only
				// focus on the selected objects
				if (this.isFixOmissionDetection == true) {
					
					boolean isContained = false; 
					for (String objStr : ptInfo.objects) {
						if (this.focusedObjects.contains(objStr)) {
							isContained = true; 
							am = fact.get(obj);
							if (am == null) {
								am = new HashSet<AutoMachine>();
								AutoMachine newMachine = sampleAutomachine.getCopy();
								newMachine.automachineName = obj;
								am.add(newMachine);
								fact.put(obj, am);
							}
							ams.addAll(am);
						}
					}
					
					if (isContained == false) {
						// 异常条件产生时，可能导致之前生成的自动机对应的对象是并不存在的，此时应该将不应该存在的object对应的自动机进行删除
						for (String objStr : ptInfo.objects) {
							if (objStr.contains("fail-creation")) {
								String nonExistObj = objStr.substring(0, objStr.length()- 15);
								am = fact.get(nonExistObj);
								if (am != null) {
									fact.remove(nonExistObj);
								}
							}
						}
						
					}
					
					
				} else {
					am = fact.get(obj);
					if (am == null) {
						am = new HashSet<AutoMachine>();
						AutoMachine newMachine = sampleAutomachine.getCopy();
						newMachine.automachineName = obj;
						am.add(newMachine);
						fact.put(obj, am);
					}
					ams.addAll(am);
				}
			}
		}
		return ams;
	}

	private void visit(InvokeExpr ie, JIRValue leftValue, boolean nparam, int currentLine,
			HashMap<String, HashSet<AutoMachine>> fact, HashMap<InterJIRValue, InterVarInfo> ptFact,
			HashSet<BinopExpr> conditions) {
		if (Debug)
			System.out.println(ie.invoker.getType() + "  " + ie.node.name + ie.node.desc);

		// ie.node.owner 调用者所属类型
		// ie.node.name 调用方法的名字
		VisitMethodInsnEdge edge = new VisitMethodInsnEdge(null, null, ie.node.getOpcode(), ie.node.owner,
				ie.node.name, ie.node.desc);
		edge.setApplyForChild(applyForChild);

		// ie.invoker 调用者实例的名字
		HashSet<AutoMachine> ams = getAM(ie.invoker, fact, ptFact);
		// 根据conditions为edge生成conditions的字符串
		edge.conditionsRegExp = generateConditionsStr(ie.invoker, conditions);

		if (ams != null) {
			// nparam 调用者是否是传入参数？
			if (nparam && !(ie.invoker instanceof FieldRef)) {
				for (AutoMachine machine : ams) {

					// 一旦状态发生迁移，需要将当前分析的上下文进行记录
					// 需要为每个自动机的状态对象增加相应属性进行记录
					try {
						machine.goOneStep(edge, currentLine);
					} catch (AutoMachineException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		if (leftValue != null) {
			ams = getAM(leftValue, fact, ptFact);
			edge.conditionsRegExp = generateConditionsStr(leftValue, conditions);
			if (ams != null)
				if (!(leftValue instanceof FieldRef)) {
					for (AutoMachine machine : ams) {
						try {
							machine.goOneStep(edge, currentLine);
						} catch (AutoMachineException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
		}

		// for (JIRValue jir : ie.params) {
		// // if(jir instanceof LocalRef){
		// ams = getAM(jir, fact, ptFact);
		// if (ams != null) {
		// for (AutoMachine machine : ams) {
		// machine.initial();
		// }
		// }
		// }
	}

	private String generateConditionsStr(JIRValue value, HashSet<BinopExpr> conditions) {
		StringBuffer conStr = new StringBuffer();
		for (BinopExpr expr : conditions) {
			if (expr.op1.equals(value)) {
				conStr.append("this");
			} else
				conStr.append(expr.op1.toString());

			conStr.append(BinopExpr.name[expr.opType]);

			if (expr.op2.equals(value)) {
				conStr.append("this");
			} else
				conStr.append(expr.op2.toString());

			conStr.append("||");
		}

		return conStr.toString();
	}

	@Override
	public HashMap<String, HashSet<AutoMachine>> createFact() {
		HashMap<String, HashSet<AutoMachine>> fact = new HashMap<String, HashSet<AutoMachine>>();
		return fact;
	}

	@Override
	public HashMap<String, HashSet<AutoMachine>> createFact(HashMap<String, HashSet<AutoMachine>> fact) {
		HashMap<String, HashSet<AutoMachine>> rdFact = new HashMap<String, HashSet<AutoMachine>>();
		Set<String> keys = fact.keySet();
		for (String key : keys) {
			HashSet<AutoMachine> machines = fact.get(key);
			HashSet<AutoMachine> newMachines = new HashSet<AutoMachine>();

			for (AutoMachine machine : machines) {
				AutoMachine newMachine = sampleAutomachine.getCopy();
				newMachine.automachineName = machine.automachineName;
				newMachine.currentState = newMachine.getStateByNumber(machine.currentState.getStateNumber());
				newMachine.lastStateChangingLine = machine.lastStateChangingLine;
				for (State state : machine.getStates()) {
					State newState = newMachine.getStateByNumber(state.getStateNumber());
					newState.discountsUnit = state.discountsUnit.clone();
				}
				newMachines.add(newMachine);
			}
			rdFact.put(key, newMachines);
		}
		return rdFact;
	}

	@Override
	public void initEntryFact() {
		if (this.initAMFact == null) {
			this.initAMFact = createFact();
		}
		startFactMap.put(blockOrder.getEntry(), this.initAMFact);
	}

	@Override
	public boolean isEndBlock(BasicBlock block) {
		return block.getEndInc() >= insns.size() - 1;
	}

	@Override
	public HashMap merge(HashMap<String, HashSet<AutoMachine>> start, HashMap<String, HashSet<AutoMachine>> pred) {
		HashMap result = createFact(start);
		if (!pred.equals(result)) {
			Set<String> keys = pred.keySet();
			for (String key : keys) {
				HashSet<AutoMachine> predMachines = (HashSet<AutoMachine>) pred.get(key);
				HashSet<AutoMachine> resultMachines = (HashSet<AutoMachine>) result.get(key);

				if (resultMachines == null) {
					result.put(key, predMachines);
				} else {

					for (AutoMachine machine : predMachines) {
						boolean contains = false;
						for (AutoMachine machine2 : resultMachines) {
							if (machine2.automachineName.equals(machine.automachineName)
									&& machine2.currentState.getStateNumber() == machine.currentState.getStateNumber()) {
								contains = true;
							}
						}

						if (contains == false) {
							resultMachines.add(machine);
						}
					}
				}

			}
		}
		return result;
	}

	@Override
	public boolean same(HashMap fact1, HashMap fact2) {
		if (fact1 == null)
			return fact2 == null;

		if (fact1.keySet().size() != fact2.keySet().size())
			return false;

		Set<String> keys = fact1.keySet();
		for (String key : keys) {
			HashSet<AutoMachine> fact1Machines = (HashSet<AutoMachine>) fact1.get(key);
			HashSet<AutoMachine> fact2Machines = (HashSet<AutoMachine>) fact2.get(key);

			if (fact1Machines.size() != fact2Machines.size()) {
				return false;
			}

			for (AutoMachine machine : fact1Machines) {
				boolean contains = false;
				for (AutoMachine machine2 : fact2Machines) {
					if (machine2.automachineName == machine.automachineName
							&& machine2.currentState.getStateNumber() == machine.currentState.getStateNumber()) {
						contains = true;
					}
				}

				if (contains == false)
					return false;
			}
		}
		return true;
	}

	// @Override
	// public void setNewStartFact(BasicBlock block, HashSet<String> fact) {
	// facts[block.startStmt] = fact;
	// }

	@Override
	public HashMap<String, HashSet<AutoMachine>> transferVertex(BasicBlock block) {
		this.currentBlock = block;

		HashMap<String, HashSet<AutoMachine>> fact = this.createFact(getStartFact(block));
		HashMap<InterJIRValue, InterVarInfo> optFact = this.ptAnalysis.getStartFact(block);
		HashMap<InterJIRValue, InterVarInfo> ptFact = this.ptAnalysis.createFact(optFact);
		HashSet<BinopExpr> conditions = this.preConditionsAnalysis.createFact(this.preConditionsAnalysis
				.getStartFact(block));

		if (fact == null)
			fact = new HashMap<String, HashSet<AutoMachine>>();

		for (int i = block.startStmt; i <= block.endStmt; i++) {
			if (this.stmts != null && this.stmts.size() < i + 1) {
				continue;
			}

			if (i == 36) {
				System.out.println("Begin to debug:\n");
			}
				
			Stmt stmt = this.stmts.get(i);
			// fact = facts[stmt.getIndex()];
			// System.out.println("i: " + i);

			this.transferStmtWhenDetecting(i, block, stmt, fact, ptFact, false);

		}
		return fact;
	}

	private void visit(boolean nparam, int currentLine, HashMap<String, HashSet<AutoMachine>> fact,
			HashMap<InterJIRValue, InterVarInfo> ptFact, HashSet<BinopExpr> conditions) {

		if (Debug)
			System.out.println("");

		VisitMethodEndEdge edge = new VisitMethodEndEdge(null, null);

		// 为所有的自动机发送EndOfMethod event
		for (String key : fact.keySet()) {
			for (AutoMachine machine : fact.get(key)) {
				edge.conditionsRegExp = generateConditionsStr(key, ptFact, conditions, this.level);
				// nparam 调用者是否是传入参数？
				if (nparam) {
					try {
						machine.goOneStep(edge, currentLine);
					} catch (AutoMachineException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	private String generateConditionsStr(String key, HashMap<InterJIRValue, InterVarInfo> ptFact,
			HashSet<BinopExpr> conditions, int level) {
		StringBuffer conStr = new StringBuffer();
		for (BinopExpr expr : conditions) {
			conStr.append(transformBinop(expr, key, ptFact, level));
			conStr.append("||");
		}
		return conStr.toString();
	}

	private String transformBinop(BinopExpr expr, String key, HashMap<InterJIRValue, InterVarInfo> ptFact, int level2) {
		InterVarInfo interPT = ptFact.get(new InterJIRValue(expr.op1, level2));
		String str = "";
		if (interPT != null) {
			Set<String> objs = interPT.getObjects();
			if (objs != null && objs.size() > 0) {
				if (objs.contains(key)) {
					str += "this";
				} else
					str += expr.op1.toString();
			}
		} else {
			str += expr.op1.toString();
		}

		str += BinopExpr.name[expr.opType];

		interPT = ptFact.get(new InterJIRValue(expr.op2, this.level));
		if (interPT != null) {
			Set<String> objs = interPT.getObjects();
			if (objs != null && objs.size() > 0) {
				if (objs.contains(key)) {
					str += "this";
				} else
					str += expr.op2.toString();
			}
		} else {
			str += expr.op2.toString();
		}

		return str;
	}

	private void visit(NewExpr newExpr, JIRValue leftValue, boolean nparam, int currentLine2,
			HashMap<String, HashSet<AutoMachine>> fact, HashMap<InterJIRValue, InterVarInfo> ptFact,
			HashSet<BinopExpr> conditions) {
		if (Debug)
			System.out.println("new " + newExpr.getType());

		// ie.node.owner 调用者所属类型
		// ie.node.name 调用方法的名字
		VisitNewInsnEdge edge = new VisitNewInsnEdge(null, null, newExpr.getType().toString());

		// ie.invoker 调用者实例的名字
		HashSet<AutoMachine> ams;
		if (leftValue != null) {
			ams = getAM(leftValue, fact, ptFact);
			edge.conditionsRegExp = generateConditionsStr(leftValue, conditions);
			if (ams != null)
				if (!(leftValue instanceof FieldRef)) {
					for (AutoMachine machine : ams) {
						try {
							machine.goOneStep(edge, currentLine);
						} catch (AutoMachineException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
		}
	}

	// added due to the inter-procedural analysi
	private ClassNode getClassNode(String className) {
		Repository cha = Repository.getInstance();
		return cha.findClassNode(className);
	}

	private MethodNode getMethod(InvokeExpr in, ClassNode cn) {
		String meName = in.getMethodName();
		String meDesc = in.getMethodDesc();
		do {
			if (cn.containMethod(meName, meDesc))
				return cn.getMethod(meName, meDesc);
			else
				cn = cn.getSuperClass();
		} while (cn != null);
		return null;// 依赖的库没有加载
		// throw new RuntimeException("some error in static invoke");
	}

	private HashMap<String, HashSet<AutoMachine>> interProceduralAnalysis(HashMap<String, HashSet<AutoMachine>> fact,
			HashMap<InterJIRValue, InterVarInfo> ptFact, JIRValue leftValue, int leftValueLevel, JIRValue rightValue,
			int rightValueLevel, boolean handleResturn) {
		// e.g.: a = b.f(c);
		// A f(d){ return e}

		InvokeExpr invoke = (InvokeExpr) rightValue;
		List<String> affectedObjects = extractPossibleAffectedObjects(fact, ptFact, invoke.params, rightValueLevel);
		String calleeSigniture = invoke.node.owner + "::" + invoke.getMethodName() + invoke.getMethodDesc();
		String callerSigniture = this.cfg.getMethod().getOriginalFullName();

		if (this.level + 1 < maxInterLevel) {

			// invoke.node.ower 调用者类型
			// 通过指向分析结果中的type信息，进行加载
			String classNameToAnalyze = invoke.node.owner;
			double interObjPrecision;
			InterVarInfo interPointsToInfo = ptFact.get(new InterJIRValue(invoke.invoker, rightValueLevel));
			Set<ClassNode> candidateClassNodes = new HashSet<ClassNode>();

			ClassNode cn = getClassNode(classNameToAnalyze);
			if (cn == null) { // 说明在当前情况下，无法加载相应代码
				Type invokeType = Type.getObjectType(invoke.node.owner);
				if (invokeType != null && invokeType.getSort() == Type.ARRAY)
					cn = getClassNode("java.lang.Object");
			}

			if (invoke.isStatic()) {
				// 如果是static的话，不需要考虑继承，精确定位即可
				if (cn != null) {
					MethodNode me = getMethod(invoke, cn);
					if (me != null) {
						candidateClassNodes.add(cn);
					}
				}

			} else {
				if (cn != null) { // 可以正常加载到源码
					// 加载该方法，获知该方法的access类型，根据不同的类型，进行后续分析
					Set<ClassNode> subClasses = cn.getSubType().getSubClass();
					MethodNode me = getMethod(invoke, cn);
					Set<ClassNode> tmpCandidateClassNodes = new HashSet<ClassNode>();
					// 此时me不应该为null，所以无需进行null判断（如果为null的话，说明该类及其父类无此方法，编译过程应该存在问题）
					if (me != null && me.isAbstract()) { // 如果是abstract的话，需要获取其实现类（针对接口而言）或子类（抽象类），结合指向分析的信息，
						// 筛选出符合条件的候选类（需要剔除抽象类），根据候选类的数量进行统计

						// 如果该类的子类实现了该抽象方法时，并且该类不是abstract时，才能够将其选进候选者队列
						for (ClassNode node : subClasses) {
							me = getMethod(invoke, cn);
							if (me != null && !me.isAbstract() && !node.isInterface()) {
								tmpCandidateClassNodes.add(node);
							}
						}

						// 根据指向信息进行再次过滤
						if (interPointsToInfo != null) {
							Set<Type> types = interPointsToInfo.getTypes();
							for (Type type : types) {
								ClassNode ptNode = getClassNode(type.getClassName());
								if (ptNode != null && tmpCandidateClassNodes.contains(ptNode)) {
									candidateClassNodes.add(ptNode);
								}
							}
						} else {
							candidateClassNodes = tmpCandidateClassNodes;
						}

					} else if (me != null) { // 非抽象方法
						if (me.isPrivate()) {// 如果是private的话，不需要考虑继承，精确定位即可
							// 如果是static的话，不需要考虑继承，精确定位即可
							me = getMethod(invoke, cn);
							if (me != null) {
								candidateClassNodes.add(cn);
							}
						} else if (me.isPublic() || me.isProtected()) { // 如果是public，protected的话，需要获取其子类,并结合指向分析信息，
							// 筛选出符合条件的候选类，根据候选类的数量进行统计
							tmpCandidateClassNodes.add(cn);
							for (ClassNode node : subClasses) {
								me = getMethod(invoke, node);
								if (me != null) {
									tmpCandidateClassNodes.add(node);
								}
							}
							// 根据指向信息进行再次过滤
							if (interPointsToInfo != null) {
								Set<Type> types = interPointsToInfo.getTypes();

								if (types == null || types.size() == 0) {
									candidateClassNodes = tmpCandidateClassNodes;
								} else {
									for (Type type : types) {
										ClassNode ptNode = getClassNode(type.getClassName());
										if (ptNode != null && tmpCandidateClassNodes.contains(ptNode)) {
											candidateClassNodes.add(ptNode);
										}
									}
								}

							} else {
								candidateClassNodes = tmpCandidateClassNodes;
							}

						} else {// 默认情况下，只允许本包内的子类继承 ->
							// 如果包不同，及时存在相同名字的方法，也不是对该方法的override
							tmpCandidateClassNodes.add(cn);
							for (ClassNode node : subClasses) {
								me = getMethod(invoke, node);
								// System.out.println(cn.name + " " +
								// node.name);
								if (me != null
										&& cn.name.substring(0, cn.name.lastIndexOf(".")).equals(
												node.name.substring(0, node.name.lastIndexOf(".")))) {
									tmpCandidateClassNodes.add(node);
								}
							}
							// 根据指向信息进行再次过滤
							if (interPointsToInfo != null) {
								Set<Type> types = interPointsToInfo.getTypes();
								if (types == null || types.size() == 0) {
									candidateClassNodes = tmpCandidateClassNodes;
								} else {
									for (Type type : types) {
										ClassNode ptNode = getClassNode(type.getClassName());
										if (ptNode != null && tmpCandidateClassNodes.contains(ptNode)) {
											candidateClassNodes.add(ptNode);
										}
									}
								}
							} else {
								candidateClassNodes = tmpCandidateClassNodes;
							}

						}
					}
				} else { // 无法根据当前的type加载到合适源码
				}
			}

			// 根据candidateClassNodes进行分情况处理
			// 当candidateClassNodes的size为0的话，说明无法加载到源码,因此无法跟进，不需要计算模糊度，需要更新UnableLoadedMethodsNumber
			// 当candidateClassNodes的size大于0的时，模糊度为1/size();

			boolean coarseAnalysis = false;

			// 以上过程需要考虑模糊度的计算，根据各种情况降低模糊度的值

			if (candidateClassNodes.size() == 0) {
				// 更新initUnableLoadedMethodsNumber
				// 判断该函数会影响到的当前fact中的哪些objects，为这些objects更新其对应的initUnableLoadedMethodsNumber
				// System.err.println("can not find class " +
				// invoke.node.owner);
				coarseAnalysis = true;
			} else {

				// 根据candidateClassNodes的数量和采取的策略计算模糊度（或准确度）
				// 策略1：仅选取candidateClassNodes中的第一个classNode进行处理，所以对应的模糊度为：1/候选者数目
				MethodNode me = getMethod(invoke, (ClassNode) candidateClassNodes.toArray()[0]);

				if (candidateClassNodes.size() == 1) {
					interObjPrecision = 1;
				} else {
					interObjPrecision = 1 / (candidateClassNodes.size() * 1.0);
				}

				// 成功加载callee方法，进行跨过程分析流程
				if (invoke.isStatic() || !me.isAbstract()) {// 递归分析
					CFG calleeCFG = me.getCFG();
					int calleeLevel = leftValueLevel + 1;
					CallingContext callingContext = new CallingContext(cfg.getMethod(), this.currentLine);

					// 处理 this = b
					JIRValue actualParam = invoke.invoker;
					InterJIRValue interActualParam = new InterJIRValue(actualParam, leftValueLevel);
					LocalRef formalParamJIRValue;
					InterJIRValue interFormalParam;

					if (calleeCFG == null) {
						return fact;
					}
					if (calleeCFG.getMethod().localVariables.size() > 0) {
						formalParamJIRValue = new LocalRef((LocalVariableNode) calleeCFG.getMethod().localVariables
								.get(0));
						interFormalParam = new InterJIRValue(formalParamJIRValue, calleeLevel);
						this.ptAnalysis.transferAssignmentStmt(ptFact, interFormalParam.intraJIRValue,
								interFormalParam.scopeLevel, interActualParam.intraJIRValue,
								interActualParam.scopeLevel);
					}

					// 依次处理各个输入参数 d = c
					List<LocalVariableNode> formalParams = calleeCFG.getMethod().params;
					List<JIRValue> actualParams = invoke.params;
					if (formalParams != null && actualParams != null && actualParams.size() == formalParams.size()) {
						for (int j = 0; j < actualParams.size(); j++) {
							actualParam = actualParams.get(j);
							interActualParam = new InterJIRValue(actualParam, leftValueLevel);

							formalParamJIRValue = new LocalRef(formalParams.get(j));
							interFormalParam = new InterJIRValue(formalParamJIRValue, calleeLevel);

							this.ptAnalysis.transferAssignmentStmt(ptFact, interFormalParam.intraJIRValue,
									interFormalParam.scopeLevel, interActualParam.intraJIRValue,
									interActualParam.scopeLevel);

						}
					}

					// 对callee执行递归分析
					// 传递discounts进入callee中
					// todo 根据指向信息的准确度进行调整

					// AMJIRInterDataflowAnalysis calleeAnalysis = new
					// AMJIRInterDataflowAnalysis(calleeCFG,
					// this.sampleAutomachine, fact, ptFact, calleeLevel,
					// callingContext);
					// calleeAnalysis.setPTAnalysis(calleeCFG, ptFact,
					// calleeLevel, callingContext);
					// calleeAnalysis.setPreconditionsAnalysis(calleeCFG);
					//
					// try {
					//
					// calleeAnalysis.execute();
					// fact =
					// calleeAnalysis.getResultFact(calleeAnalysis.endBlock);
					//
					// } catch (AnalyzerException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }
				}

			}
		} else { // 超过最大跨过程分析层数，不进行后续分析
			// update
			// System.out.println("Excced Inter " + this.level + " " +
			// this.numIterations);
			// this.precisionDiscountsStack.updateDiscountsWhenExceedMaxInterDepth(level,
			// affectedObjects,
			// callerSigniture, this.currentLine, calleeSigniture);
		}

		return fact;
	}

	private List<String> extractPossibleAffectedObjects(HashMap<String, HashSet<AutoMachine>> fact,
			HashMap<InterJIRValue, InterVarInfo> ptFact, List<JIRValue> params, int rightValueLevel) {
		List<String> objects = new ArrayList<String>();
		InterVarInfo ptInfo;
		for (JIRValue param : params) {
			InterJIRValue interJIRValue = new InterJIRValue(param, rightValueLevel);
			if (ptFact.get(interJIRValue) != null) { // 从指向信息中获取其对应的内存object
				ptInfo = ptFact.get(interJIRValue);
				if (ptInfo.getObjects() != null && ptInfo.getObjects().size() > 0) {
					for (String obj : ptInfo.getObjects()) {
						if (fact.keySet().contains(obj)) { // 判断该object是否是该缺陷模式关注的对象，如果是，则将其判定为“possible
							// affected objects”
							objects.add(obj);
						}
					}
				}
			}
		}
		return objects;
	}

	public synchronized void execute() throws AnalyzerException {
		boolean change;
		initEntryBlock();

		int timestamp = 0;
		do {
			change = false;

			if (numIterations >= MAX_ITERS) {
				System.out.println("Too many iterations (" + numIterations + ") in dataflow when analyzing "
						+ getFullyQualifiedMethodName());

				// update
				HashMap<String, HashSet<AutoMachine>> ams = this.resultFactMap.get(this.endBlock);
				// this.precisionDiscountsStack.updateDiscountsWhenExceedMaxIterTimes(level,
				// ams.keySet(), this.cfg
				// .getMethod().getOriginalFullName());
				break;
			}

			// System.out.println("\n****进入：" + this.level + " " +
			// this.numIterations + " ");
			// System.out.println(this.precisionDiscountsStack.toString());

			startIteration();

			Iterator<BasicBlock> i = blockOrder.blockIterator();
			while (i.hasNext()) {
				BasicBlock block = i.next();

				HashMap<String, HashSet<AutoMachine>> start = getStartFact(block);
				HashMap<String, HashSet<AutoMachine>> result = getResultFact(block);

				boolean isEndBlock = isEndBlock(block);
				if (isEndBlock) {
					this.endBlock = block;
				}

				boolean needToRecompute = false;

				if (start != null) {// if the original start is
					// not equal to new start,
					// need to recompute
					if (getNewStartFact(block) == null) {
						needToRecompute = true;
						setNewStartFact(block, createFact(start));
					}

					Iterator<Edge> predEdgeIter = logicalPredecessorEdgeIterator(block);
					HashMap<String, HashSet<AutoMachine>> startBack = getNewStartFact(block);
					// get all predecessors' result fact and merge them with the
					// new start fact
					while (predEdgeIter.hasNext()) {
						Edge edge = predEdgeIter.next();
						BasicBlock logicalPred = isForwards ? edge.getSource() : edge.getTarget();
						HashMap<String, HashSet<AutoMachine>> pred = null;
						// ////////////perhaps we need to distinguish different
						// handlers
						// if (edge.getType() !=
						// EdgeType.HANDLED_EXCEPTION_EDGE) {
						if (this.blockOrder instanceof ReversePostOrder) {

							if (edge.getType() == EdgeType.IFCMP_EDGE_JUMP) {
								pred = createFact(transferIfTrueBlock(logicalPred));
							} else {
								pred = createFact(getResultFact(logicalPred));
							}
							startBack = merge(startBack, pred);

						} else {
							pred = createFact(getResultFact(logicalPred));
							startBack = merge(startBack, pred);
						}
						// }
					}

					// if the generated startBack is different from start, need
					// to recompute
					if (!same(startBack, start)) {
						needToRecompute = true;
						start = startBack;
					}
				}
				// if really need to recompute, recompute the current block's
				// frames
				if (needToRecompute) {
					setLastUpdateTimestamp(start, timestamp);
					setStartFact(block, createFact(start)); // set
					// start
					// facts
					// in
					// startFactMap
					setNewStartFact(block, createFact(start));// set
					// start
					// facts
					// in
					// blocks

					if (DEBUG) {
						if (Thread.activeCount() > 1) {
							Map map = Thread.getAllStackTraces();
							System.out.println("The thread number before calling transferVertex");
							for (Object key : map.keySet()) {
								System.out.println("key = " + key + "    value = " + map.get(key).toString());
							}
						}
					}

					HashMap<String, HashSet<AutoMachine>> newResult = transferVertex(block);

					if (newResult != null && !same(newResult, result)) {
						setLastUpdateTimestamp(newResult, timestamp);
						setResultFact(block, createFact(newResult));
						change = true;
					}
					if (DEBUG) {
						if (Thread.activeCount() > 1) {
							Map map = Thread.getAllStackTraces();
							System.out.println("The thread number after calling transferVertex");
							for (Object key : map.keySet()) {
								System.out.println("key = " + key);
								StackTraceElement[] stack = (StackTraceElement[]) (map.get(key));
								for (int ii = 0; ii < stack.length; ii++) {
									System.out.println(stack[ii]);
								}
							}
						}
					}
				}
				if (DEBUG) {
					// System.out.println(block.toString());
				}
			}
			finishIteration();

			// System.out.println("\n****退出：" + this.level + " " +
			// this.numIterations + " ");
			// System.out.println(this.precisionDiscountsStack.toString());

			++numIterations;
			++timestamp;
		} while (change);
	}

	private HashMap<String, HashSet<AutoMachine>> transferIfTrueBlock(BasicBlock block) {
		this.currentBlock = block;

		HashMap<String, HashSet<AutoMachine>> fact = this.createFact(getStartFact(block));
		HashMap<InterJIRValue, InterVarInfo> optFact = this.ptAnalysis.getStartFact(block);
		HashMap<InterJIRValue, InterVarInfo> ptFact = this.ptAnalysis.createFact(optFact);
		HashSet<BinopExpr> conditions = this.preConditionsAnalysis.createFact(this.preConditionsAnalysis
				.getStartFact(block));

		if (fact == null)
			fact = new HashMap<String, HashSet<AutoMachine>>();

		for (int i = block.startStmt; i <= block.endStmt; i++) {
			if (this.stmts != null && this.stmts.size() < i + 1) {
				continue;
			}
			Stmt stmt = this.stmts.get(i);
			this.transferStmtWhenDetecting(i, block, stmt, fact, ptFact, true);
		}
		return fact;
	}

	protected void setInterestedBlocks() {
		ArrayList<BasicBlock> blocks = this.cfg.getBlocks();

		for (long line : this.lineNumbers) {
			for (BasicBlock block : blocks) {
				if (block.getStartSrcLine() <= line && line <= block.getEndSrcLine()) {
					if (this.interestedBlocks.get(line) == null) {
						HashSet<BasicBlock> tmpBlocks = new HashSet<BasicBlock>();
						tmpBlocks.add(block);
						this.interestedBlocks.put(line, tmpBlocks);
					} else {
						this.interestedBlocks.get(line).add(block);
					}
				}
			}
		}

		// fitler the uninterested blocks (false positives)

	}

	protected void pickInterestedPaths() {
		// private Map<Long, List<BasicBlock>> extendedBarkwardBlocks = new
		// HashMap<Long, List<BasicBlock>>();
		// private Map<Long, List<BasicBlock>> extendedForwardBlocks = new
		// HashMap<Long, List<BasicBlock>>();

		for (long intrestedLineNo : this.interestedBlocks.keySet()) {
			if (this.interestedBlocks.get(intrestedLineNo) != null
					&& this.interestedBlocks.get(intrestedLineNo).size() >= 1) {
				List<BasicBlock> path = null;
				List<BasicBlock> longestExceptionalPath = null;
				BasicBlock cutpointBlock = null;
				boolean isFullPath = false;

				int maxLength = 0;

				for (BasicBlock block : this.interestedBlocks.get(intrestedLineNo)) {
					// path = this.cfg.getABackwardPathWithNoException(block);
					path = this.cfg.getABackwardLongestExceptionalPath(block);
					if (path != null && path.size() > maxLength) {
						int entryBlockID = path.get(path.size() - 1).getLabel();
						if (entryBlockID == 0) {
							longestExceptionalPath = path;
							maxLength = longestExceptionalPath.size();
							cutpointBlock = block;
						}
					}
				}

				if (longestExceptionalPath != null) {
					this.sliceBasedBlock.put(intrestedLineNo, cutpointBlock);
					isFullPath = true;
				}

				if (isFullPath == false) {
					System.out.println(this.cfg.getMethod().getFullName());
					System.out.println(longestExceptionalPath);
					System.out.println("Can not find a full path starting with an entry block!!!");
				}

				// get a half path backwardly from this current cutpoint block
				this.backwardPaths.put(intrestedLineNo, longestExceptionalPath);

				// get another half path forwardly from this current cutpoint
				// block
				List<BasicBlock> forwardPath = null;
				boolean isFullForwardPath = false;

				forwardPath = this.cfg.getAForwardPathWithNoException(this.sliceBasedBlock.get(intrestedLineNo));

				BasicBlock exitBlock = forwardPath.get(forwardPath.size() - 1);
				if (exitBlock.getLabel() == this.cfg.getExit().getLabel()) {
					isFullForwardPath = true;
				} else {
					System.out.println("Can not find a full path ending with an exit block!!!");
				}
				this.forwardPaths.put(intrestedLineNo, forwardPath);
			}
		}
	}

	// null-pointer exceptions
	 public static String fixPatternFile =
	 "AutomachineSpecs\\BugPatternTemplate_NPE_CorrectUsageScenarios.xml";
	 public static String defectPatternFile =
	 "AutomachineSpecs\\BugPatternTemplate_NPE_BuggyUsageScenarios.xml";
		
	 // xerces - 131686
	 public static long[] interestingFixedLines = {1689};
	 public static String testFileName = "AbstractDOMParser";

	// public static long[] interestingFixedLines = { 1712 };
	// public static String testFileName = "XSSimpleTypeDecl";

	// public static long[] interestingFixedLines = { 268 };
	// public static String testFileName = "XMLDTDScannerImpl";
	//	
	// public static long[] interestingFixedLines = { 588 };
	// public static String testFileName = "XSWildcardDecl";

	// resource leaks
//	public static String fixPatternFile = "AutomachineSpecs\\BugPatternTemplate_RL_CorrectUsageScenarios.xml";
//	public static String defectPatternFile = "AutomachineSpecs\\BugPatternTemplate_RL_BuggyUsageScenarios.xml";
////
////	// my test case
//	public static long[] interestingFixedLines = { 44 };
//	public static String testFileName = "TestCloseDbConnection2";

	public static void main(String[] args) {
		RefactoredDataflowTestDriver<HashMap<String, HashSet<AutoMachine>>, SysFixInterDataflowAnalysis> driver = new RefactoredDataflowTestDriver<HashMap<String, HashSet<AutoMachine>>, SysFixInterDataflowAnalysis>() {
			public RefactoredDataflow<HashMap<String, HashSet<AutoMachine>>, SysFixInterDataflowAnalysis> createDataflow(
					ClassNode cc, MethodNode method) {
				try {

					CFG cfg = method.getCFG();

					if (method
							.getFullName()
							.contains(
									"applyFacets(Lorg/apache/xerces/impl/dv/XSFacets;SSSLorg/apache/xerces/impl/dv/ValidationContext;)V")) {
						return null;
					}

					if (method.getFullName().contains(
							"getLexicalEnumerations()Lorg/apache/xerces/impl/xs/psvi/StringList")) {
						System.out.println("debug:");
					}
					
//					if (method.getFullName().contains("startDTD")) {
//						System.out.println("Debug:");
//					} else {
//						return null; 
//					}

					// System.out.println(method.getFullName());
					// method.getCFG().printEachBlockEdgesAndStmts();

					long[] lines = interestingFixedLines;
					SysFixInterDataflowAnalysis analysis = new SysFixInterDataflowAnalysis(cfg, fixPatternFile, lines);

					RefactoredDataflow<HashMap<String, HashSet<AutoMachine>>, SysFixInterDataflowAnalysis> dataflow = new RefactoredDataflow<HashMap<String, HashSet<AutoMachine>>, SysFixInterDataflowAnalysis>(
							cfg, analysis);

					// identify the block cotaining the interested code change
					// (e.g., == null) based on the source code line number
					analysis.setInterestedBlocks();

					// extract a proper path based on the interested block
					// backward and forward
					analysis.pickInterestedPaths();

					// identify the variables/objects contained in the
					// interested code change
					// 根据不同类型的stmt进行identify
					analysis.setInterestedVariables();

					// slice the path based on the interested variables/objects
					// backwardly and forwardly
					analysis.slicePathByInterestedVars();

					// match the extracted path with with the pattern template
					// -> one time analysis
					analysis.matchSlicedPathWithPattern();

					// extract the concrete defect pattern out based on the
					// matching result
					// needs to consider each defect pattern's atrribute
					// "tendsToBeExceptionalObj"
					analysis.setFocusedObjects();

					analysis.relocateAMSpecFile(defectPatternFile);
					// detect violations based on the concrete defect pattern
					analysis.detectingFixOmissions();

					// if the focused object needs to condcut a class level
					// checking
					// get all other methods of cc, and then create a new
					// analysis and save the result to dataflow
					if (analysis.focusedObjects != null && analysis.focusedObjects.size() != 0) {
						for (String focusedObj : analysis.focusedObjects) {
							if (focusedObj.contains("classField")) {
								// meaning this obj is pointed by a class field,
								// at least need to conduct a
								// conduct a class level check
								for (MethodNode otherMethod : cc.methods) {
									if (!otherMethod.getFullName().equals(method.getFullName())) {
										// detect defects in this method
										if (otherMethod
												.getFullName()
												.contains(
														"applyFacets(Lorg/apache/xerces/impl/dv/XSFacets;SSSLorg/apache/xerces/impl/dv/ValidationContext;)V")) {
											continue;
										}

										otherMethod.getStmts();
										SysFixInterDataflowAnalysis otherAnalysis = new SysFixInterDataflowAnalysis(
												otherMethod.getCFG(), defectPatternFile);
										otherAnalysis.focusedObjects = analysis.focusedObjects;
										otherAnalysis.detectingFixOmissions();
										dataflow.analysisOnOtherMethods.add(otherAnalysis);
									}
								}
							}
						}
					}

					// tips: we need to save that which class filed points to
					// this object, then we can know its specifier

					return dataflow;

				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public void examineResults(
					RefactoredDataflow<HashMap<String, HashSet<AutoMachine>>, SysFixInterDataflowAnalysis> dataflow) {

				// dataflow.getAnalysis().cfg.printEachBlockEdges();
				// dataflow.getAnalysis().cfg.printEachBlockEdgesAndStmts();

				if (dataflow == null) {
					return;
				}

				Map<Long, HashSet<BasicBlock>> blocks = dataflow.getAnalysis().interestedBlocks;
				for (Long lineNumber : blocks.keySet()) {

					// dataflow.getAnalysis().cfg.printEachBlockStmts();

					System.out.println("Interesting Blocks for Line:" + lineNumber);

					for (BasicBlock block : blocks.get(lineNumber)) {
						block.printBlockFullInfo();
					}

					System.out.println("\nPath Extraction:");
					dataflow.getAnalysis().sliceBasedBlock.get(lineNumber).printBlockBasicInfo();

					System.out.println("\nFarward Path:");
					for (BasicBlock block : dataflow.getAnalysis().forwardPaths.get(lineNumber)) {
						block.printBlockStmts();
					}

					System.out.println("\nBackward Path:");
					for (BasicBlock block : dataflow.getAnalysis().backwardPaths.get(lineNumber)) {
						block.printBlockStmts();
					}

					System.out.println("\nSliced Path based on the bariable :"
							+ dataflow.getAnalysis().interestedVariables);

					System.out.println("Sliced Backward Stmts:");
					for (Stmt stmt : dataflow.getAnalysis().slicedBackwardPaths.get(lineNumber)) {
						System.out.println("	" + stmt);
					}

					System.out.println("\nSliced forward Stmts:");
					for (Stmt stmt : dataflow.getAnalysis().slicedForwardPaths.get(lineNumber)) {
						System.out.println("	" + stmt);
					}

					Map<Long, HashMap<String, HashSet<AutoMachine>>> result = dataflow.getAnalysis().matchedAMs;
					
					
					System.out.println("\nUsage-Scenario Matching Result —— Automachines: ");
					Set<Long> keys = result.keySet();
					for (Long key : keys) {
						System.out.println("Line Number: " + key.toString());
						HashMap<String, HashSet<AutoMachine>> ams = result.get(key);
						for (String am : ams.keySet()) {
							HashSet<AutoMachine> machines = (HashSet<AutoMachine>) ams.get(am);
							for (AutoMachine machine : machines) {
								System.out.println("	"
										+ machine.automachineName
										+ ":\n"
										+ "		CurrentState is "
										+ machine.currentState.getStateNumber()
										+ (machine.currentState.isFixedState() == true ? "[FixedState]"
												: "[NotFixedState]") + ";\n" + "		Last Changing Line Number is "
										+ machine.lastStateChangingLine);
							}
						}
					}

					System.out.println("\nFixed Objects [Their AMs are in FIXED states]: ");
					for (String obj : dataflow.getAnalysis().focusedObjects) {
						System.out.println("	" + obj);
					}

					System.out.println("\nSibling Bug Detection Result - Automachines:  ");

					// print all result for each block
					BasicBlock block = dataflow.getAnalysis().cfg.getExit();
					
//					for (BasicBlock bb : dataflow.getAnalysis().cfg.getBlocks()) {
//						
//						System.out.println("---" + bb.toString());
//						HashMap<String, HashSet<AutoMachine>> ams = dataflow.getAnalysis().getResultFact(bb);
//						Set<String> strkeys = ams.keySet();
//						for (String key : strkeys) {
//							System.out.println("	" + key.toString());
//							HashSet<AutoMachine> machines = (HashSet<AutoMachine>) ams.get(key);
//							for (AutoMachine machine : machines) {
//								System.out.println("		CurrentState is " + machine.currentState.getStateNumber()
//										+ (machine.currentState.isEndState() == true ? "[NotDefective]" : "[Defective]")
//										+ ";\n" + "		Last Changing Line Number is " + machine.lastStateChangingLine);
//							}
//						}
//						System.out.println("");
//					}
					
					
					HashMap<String, HashSet<AutoMachine>> fixOmissions = dataflow.getAnalysis().getResultFact(block);

					Set<String> strkeys = fixOmissions.keySet();
					for (String key : strkeys) {
						System.out.println("	" + key.toString());
						HashSet<AutoMachine> machines = (HashSet<AutoMachine>) fixOmissions.get(key);
						for (AutoMachine machine : machines) {
							System.out.println("		CurrentState is " + machine.currentState.getStateNumber()
									+ (machine.currentState.isEndState() == true ? "[NotDefective]" : "[Defective]")
									+ ";\n" + "		Last Changing Line Number is " + machine.lastStateChangingLine);
						}
					}
					System.out.println("");

//					System.out.println("\nFix Omission Detection Result on other methods :  ");
//
//					for (SysFixInterDataflowAnalysis analysisOnOtherMethod : dataflow.analysisOnOtherMethods) {
//
//						System.out.println("\n -- method:" + analysisOnOtherMethod.cfg.getMethod().getFullName());
//
//						block = analysisOnOtherMethod.cfg.getExit();
//						fixOmissions = analysisOnOtherMethod.getResultFact(block);
//
//						strkeys = fixOmissions.keySet();
//						for (String key : strkeys) {
//							System.out.println("	" + key.toString());
//							HashSet<AutoMachine> machines = (HashSet<AutoMachine>) fixOmissions.get(key);
//							for (AutoMachine machine : machines) {
//								System.out
//										.println("		CurrentState is "
//												+ machine.currentState.getStateNumber()
//												+ (machine.currentState.isEndState() == true ? "[NotDefective]"
//														: "[Defective]") + ";\n" + "		Last Changing Line Number is "
//												+ machine.lastStateChangingLine);
//							}
//						}
//						System.out.println("");
//					}
				}
			}

			// print the dataflow information at the top and end of each block
			private void printDetailInfo(
					RefactoredDataflow<HashMap<String, HashSet<AutoMachine>>, SysFixInterDataflowAnalysis> dataflow) {
				Iterator<BasicBlock> i = dataflow.getAnalysis().cfg.blockIterator();
				while (i.hasNext()) {
					BasicBlock block = i.next();
					HashMap<String, HashSet<AutoMachine>> start = dataflow.getAnalysis().getStartFact(block);

					// result 有问题，但下一个block的start是没有问题的
					HashMap<String, HashSet<AutoMachine>> result = dataflow.getAnalysis().getResultFact(block);

					System.out.println("________________________________");
					Set<String> keys = start.keySet();

					for (String key : keys) {
						System.out.println(key.toString());
						HashSet<AutoMachine> machines = (HashSet<AutoMachine>) start.get(key);

						for (AutoMachine machine : machines) {
							System.out.println("	" + machine.automachineName + ";"
									+ machine.currentState.getStateNumber() + " " + machine.lastStateChangingLine);
						}
					}

					System.out.println("");

					// if (block.getLabel() == 6) {
					// System.out.println("debug");
					// }

					block.printBlockFullInfo();

					// for (int m = block.startStmt; m <= block.endStmt; m++) {
					// System.out.println(m + ": " +
					// block.stmts.get(m).toString());
					// }
					System.out.println("");

					keys = result.keySet();
					for (String key : keys) {
						System.out.println(key.toString());
						HashSet<AutoMachine> machines = (HashSet<AutoMachine>) result.get(key);

						for (AutoMachine machine : machines) {
							System.out.println("	" + machine.automachineName + ";"
									+ machine.currentState.getStateNumber() + " " + machine.lastStateChangingLine);
						}
					}

					System.out.println("________________________________");
				}
			}
		};

		driver.execute(testFileName);
		// driver.execute("TestCloseDbConnection");

	}

	protected void relocateAMSpecFile(String specificFile) {
		this.specificFile = specificFile;
		this.sampleAutomachine = AutomaUtil.generateAutoMachineFromXML(specificFile);
		if (sampleAutomachine.getOnlyConcernClass() != null && !"".equals(sampleAutomachine.getOnlyConcernClass())) {
			applyForChild = true;
			String[] name = sampleAutomachine.getOnlyConcernClass().split(";");
			applyType = new Type[name.length];
			for (int i = 0; i < name.length; i++) {
				applyType[i] = Type.getObjectType(name[i]);
			}
		}

	}

	protected void detectingFixOmissions() {

		this.isFixOmissionDetection = true;

		boolean change;
		initEntryBlock();

		int timestamp = 0;
		do {
			change = false;

			if (numIterations >= MAX_ITERS) {
				System.out.println("Too many iterations (" + numIterations + ") in dataflow when analyzing "
						+ getFullyQualifiedMethodName());

				// update
				// HashMap<String, HashSet<AutoMachine>> ams =
				// this.resultFactMap.get(this.endBlock);
				// this.precisionDiscountsStack.updateDiscountsWhenExceedMaxIterTimes(level,
				// ams.keySet(), this.cfg
				// .getMethod().getOriginalFullName());
				break;
			}

			// System.out.println("\n****进入：" + this.level + " " +
			// this.numIterations + " ");
			// System.out.println(this.precisionDiscountsStack.toString());

			startIteration();

			Iterator<BasicBlock> i = blockOrder.blockIterator();
			while (i.hasNext()) {
				BasicBlock block = i.next();
				
				if (block.getLabel() == 4) {
					System.out.println("debug point:");
				}
				
				HashMap<String, HashSet<AutoMachine>> start = getStartFact(block);
				HashMap<String, HashSet<AutoMachine>> result = getResultFact(block);

				boolean isEndBlock = isEndBlock(block);
				if (isEndBlock) {
					this.endBlock = block;
				}

				boolean needToRecompute = false;

				if (start != null) {// if the original start is
					// not equal to new start,
					// need to recompute
					if (getNewStartFact(block) == null) {
						needToRecompute = true;
						setNewStartFact(block, createFact(start));
					}

					Iterator<Edge> predEdgeIter = logicalPredecessorEdgeIterator(block);
					HashMap<String, HashSet<AutoMachine>> startBack = getNewStartFact(block);
					// get all predecessors' result fact and merge them with the
					// new start fact
					while (predEdgeIter.hasNext()) {
						Edge edge = predEdgeIter.next();
						BasicBlock logicalPred = isForwards ? edge.getSource() : edge.getTarget();
						HashMap<String, HashSet<AutoMachine>> pred = null;
						// ////////////perhaps we need to distinguish different
						// handlers
						// if (edge.getType() !=
						// EdgeType.HANDLED_EXCEPTION_EDGE) {
						if (this.blockOrder instanceof ReversePostOrder) {

							if (edge.getType() == EdgeType.IFCMP_EDGE_JUMP) {
								pred = createFact(transferIfTrueBlock(logicalPred));
							} else {
								pred = createFact(getResultFact(logicalPred));
							}
							startBack = merge(startBack, pred);

						} else {
							pred = createFact(getResultFact(logicalPred));
							startBack = merge(startBack, pred);
						}
						// }
					}

					// if the generated startBack is different from start, need
					// to recompute
					if (!same(startBack, start)) {
						needToRecompute = true;
						start = startBack;
					}
				}
				// if really need to recompute, recompute the current block's
				// frames
				if (needToRecompute) {
					setLastUpdateTimestamp(start, timestamp);
					setStartFact(block, createFact(start)); // set
					// start
					// facts
					// in
					// startFactMap
					setNewStartFact(block, createFact(start));// set
					// start
					// facts
					// in
					// blocks

					if (DEBUG) {
						if (Thread.activeCount() > 1) {
							Map map = Thread.getAllStackTraces();
							System.out.println("The thread number before calling transferVertex");
							for (Object key : map.keySet()) {
								System.out.println("key = " + key + "    value = " + map.get(key).toString());
							}
						}
					}

					HashMap<String, HashSet<AutoMachine>> newResult = transferVertex(block);

					if (newResult != null && !same(newResult, result)) {
						setLastUpdateTimestamp(newResult, timestamp);
						setResultFact(block, createFact(newResult));
						change = true;
					}
					if (DEBUG) {
						if (Thread.activeCount() > 1) {
							Map map = Thread.getAllStackTraces();
							System.out.println("The thread number after calling transferVertex");
							for (Object key : map.keySet()) {
								System.out.println("key = " + key);
								StackTraceElement[] stack = (StackTraceElement[]) (map.get(key));
								for (int ii = 0; ii < stack.length; ii++) {
									System.out.println(stack[ii]);
								}
							}
						}
					}
				}
				if (DEBUG) {
					// System.out.println(block.toString());
				}
			}
			finishIteration();

			// System.out.println("\n****退出：" + this.level + " " +
			// this.numIterations + " ");
			// System.out.println(this.precisionDiscountsStack.toString());

			++numIterations;
			++timestamp;
		} while (change);
	}

	protected void setFocusedObjects() {
		for (long interestedLineNo : this.matchedAMs.keySet()) {
			HashMap<String, HashSet<AutoMachine>> ams = this.matchedAMs.get(interestedLineNo);
			if (ams != null) {
				for (String objName : ams.keySet()) {
					for (AutoMachine am : ams.get(objName)) {
						if (am.getCurrentState().isFixedState() == true) {
							this.focusedObjects.add(objName);
						}
					}
				}
			}
		}
	}

	public Map<Long, HashMap<String, HashSet<AutoMachine>>> matchedAMs = new HashMap<Long, HashMap<String, HashSet<AutoMachine>>>();
	public HashMap<InterJIRValue, InterVarInfo> currentVarInfoWhenMatching = new HashMap<InterJIRValue, InterVarInfo>();

	protected void matchSlicedPathWithPattern() {

		for (long interestedLineNo : this.slicedBackwardPaths.keySet()) {

			HashMap<String, HashSet<AutoMachine>> ams = this.matchedAMs.get(interestedLineNo);
			if (ams == null) {
				ams = new HashMap<String, HashSet<AutoMachine>>();
				this.matchedAMs.put(interestedLineNo, ams);
			}

			List<Stmt> backwardStmts = this.slicedBackwardPaths.get(interestedLineNo);
			List<Integer> backwardStmtIds = this.slicedBackwardStmtIndexes.get(interestedLineNo);
			List<BasicBlock> backwardBlocks = this.slicedBackwardStmtBlocks.get(interestedLineNo);

			for (int i = backwardStmts.size() - 1; i > 0; i--) {
				Stmt stmt = backwardStmts.get(i);
				int index = backwardStmtIds.get(i);
				BasicBlock block = backwardBlocks.get(i);
				// AM info analysis based on points-to info, and analyze the
				// stmt, and update the AM info
				transferStmt(index, block, stmt, ams, this.backwardPaths.get(interestedLineNo));
			}

			List<Stmt> forwardStmts = this.slicedForwardPaths.get(interestedLineNo);
			List<Integer> forwardStmtIds = this.slicedForwardStmtIndexes.get(interestedLineNo);
			List<BasicBlock> forwardBlocks = this.slicedForwardStmtBlocks.get(interestedLineNo);

			for (int i = 0; i < forwardStmts.size(); i++) {
				Stmt stmt = forwardStmts.get(i);
				int index = forwardStmtIds.get(i);
				BasicBlock block = forwardBlocks.get(i);

				transferStmt(index, block, stmt, ams, this.forwardPaths.get(interestedLineNo));
			}
		}
	}

	private void transferStmtWhenDetecting(int index, BasicBlock block, Stmt stmt,
			HashMap<String, HashSet<AutoMachine>> fact, HashMap<InterJIRValue, InterVarInfo> ptFact,
			boolean focusIfTrueBranch) {

		// if the stmt is the first stmt of a block and the hosting block owns
		// some exceptional edge, and also the exception is trigered by some
		// object-creation method call
		// we need to cancel the object creation status, label the object status
		// as failed-creation

		if (block.startStmt == index) {
			List<BasicBlock> exceptionalblocks = block.getIncomingExceptionBlocks();
			if (exceptionalblocks != null && exceptionalblocks.size() > 0) {

				// to identify the effects of what blocks needs to be rolled
				// back
				for (BasicBlock exceptionalblock : exceptionalblocks) {
					Integer i = exceptionalblock.getExceptionalObjectCreationStmt();

					if (i != null) {
						// roll back the object creation effect on the points-to
						// info
						Stmt objCreationStmt = exceptionalblock.stmts.get(i);
						if (objCreationStmt instanceof AssignStmt) {
							AssignStmt assignStmt = (AssignStmt) objCreationStmt;

							// to judege whether the points-to analysis result
							// contains the assigned variable, then cancel the
							// effect.
							this.ptAnalysis.deTransferStmt(assignStmt, ptFact);
						}
					}
				}
			}
		}

		if (stmt instanceof LineStmt) {
			this.currentLine = ((LineStmt) stmt).line;
			this.ptAnalysis.transferStmt(stmt, ptFact);
		} else if (stmt instanceof IfStmt) {
			IfStmt ifStmt = (IfStmt) stmt;
			if (ifStmt.condition instanceof BinopExpr) {
				BinopExpr expr = (BinopExpr) ifStmt.condition;
				if (expr.opType == BinopExpr.EQUAL || expr.opType == BinopExpr.NEQUAL) {
					String opType = "";

					if (focusIfTrueBranch == true) {
						if (expr.opType == BinopExpr.EQUAL) {
							opType = "==";
						} else {
							opType = "!=";
						}
					} else {
						if (expr.opType == BinopExpr.EQUAL) {
							opType = "!=";
						} else {
							opType = "==";
						}
					}

					if (expr.op1 instanceof Null) {
						transferIfNullCheckStmt(expr.op2, opType, fact, ptFact);
					} else if (expr.op2 instanceof Null) {
						transferIfNullCheckStmt(expr.op1, opType, fact, ptFact);
					}
				}
			}
		} else if (stmt instanceof AssignStmt) {

			AssignStmt assignStmt = (AssignStmt) stmt;
			// not sound now. sound analysis needs to be based on the points-to
			// information
			// 赋值语句的左侧是 感兴趣的变量

			if (assignStmt.right instanceof FieldRef) {
				// update the variable info
				this.ptAnalysis.transferStmt(stmt, ptFact);
				FieldRef fieldRef = (FieldRef) assignStmt.right;
				transferVarDerefEvent(fieldRef.base, this.currentLine, fact, ptFact);
			} else if (assignStmt.right instanceof LocalRef) {
				this.ptAnalysis.transferStmt(stmt, ptFact);
			} else if (assignStmt.right instanceof ArrayRef) {
				ArrayRef ar = (ArrayRef) assignStmt.right;
				this.ptAnalysis.transferStmt(stmt, ptFact);
				transferVarDerefEvent(ar.base, this.currentLine, fact, ptFact);
			} else if (assignStmt.right instanceof LengthOfExpr) {
				LengthOfExpr loe = (LengthOfExpr) assignStmt.right;
				this.ptAnalysis.transferStmt(stmt, ptFact);
				transferVarDerefEvent(loe.value, this.currentLine, fact, ptFact);
			} else if (assignStmt.right instanceof InvokeExpr) {
				// 查看invoker是否是感兴趣的变量，如果是的话，保留该stmt
				InvokeExpr invokeExpr = (InvokeExpr) assignStmt.right;

				// inter-procedural analysis
				fact = interProceduralAnalysis(fact, ptFact, assignStmt.left, this.level, assignStmt.right, this.level,
						true);

				// update the variable info
				this.ptAnalysis.transferStmt(stmt, ptFact);

				// impact the stmt on the ams
				transferVarDerefEvent(invokeExpr.invoker, this.currentLine, fact, ptFact);
				transferInvokeExpr(invokeExpr, assignStmt.left, this.currentLine, fact, ptFact);

			} else if (assignStmt.right instanceof NewExpr) {
				// update the variable info
				this.ptAnalysis.transferStmt(stmt, ptFact);
			}

			// update the alias of the current interested variable
			// left part
			if (assignStmt.left instanceof FieldRef) {
				FieldRef fieldRef = (FieldRef) assignStmt.left;
				transferVarDerefEvent(fieldRef.base, this.currentLine, fact, ptFact);
			} else if (assignStmt.left instanceof ArrayRef) {
				ArrayRef ar = (ArrayRef) assignStmt.left;
				transferVarDerefEvent(ar.base, this.currentLine, fact, ptFact);
			}

		} else if (stmt instanceof InvokeStmt) {
			InvokeStmt invokeStmt = (InvokeStmt) stmt;
			InvokeExpr invokeExpr = (InvokeExpr) invokeStmt.invoke;

			// inter-procedural analysis
			fact = interProceduralAnalysis(fact, ptFact, null, this.level, invokeExpr, this.level, false);

			// update the variable info
			this.ptAnalysis.transferStmt(stmt, ptFact);

			// impact the stmt on the ams
			transferVarDerefEvent(invokeExpr.invoker, this.currentLine, fact, ptFact);
			transferInvokeExpr(invokeExpr, null, this.currentLine, fact, ptFact);

			if (invokeExpr.invoker instanceof ArrayRef) {
				ArrayRef ar = (ArrayRef) invokeExpr.invoker;
				transferVarDerefEvent(ar.base, this.currentLine, fact, ptFact);
			}
		}
	}

	private void transferStmt(int index, BasicBlock block, Stmt stmt, HashMap<String, HashSet<AutoMachine>> ams,
			List<BasicBlock> pathBlocks) {

		// if the stmt is the first stmt of a block and the hosting block owns
		// some exceptional edge, and also the exception is trigered by some
		// object-creation method call
		// we need to cancel the object creation status, label the object status
		// as failed-creation

		if (block.startStmt == index) {
			List<BasicBlock> exceptionalblocks = block.getIncomingExceptionBlocks();
			if (exceptionalblocks != null && exceptionalblocks.size() > 0) {

				// to identify the effects of what blocks needs to be rolled
				// back
				for (BasicBlock exceptionalblock : exceptionalblocks) {
					Integer i = exceptionalblock.getExceptionalObjectCreationStmt();

					if (i != null) {
						// roll back the object creation effect on the points-to
						// info
						Stmt objCreationStmt = exceptionalblock.stmts.get(i);
						if (objCreationStmt instanceof AssignStmt) {
							AssignStmt assignStmt = (AssignStmt) objCreationStmt;

							// to judege whether the points-to analysis result
							// contains the assigned variable, then cancel the
							// effect.
							this.ptAnalysis.deTransferStmtWhenFixPatternMatching(assignStmt,
									this.currentVarInfoWhenMatching);
						}
					}
				}
			}
		}

		if (stmt instanceof LineStmt) {
			this.currentLine = ((LineStmt) stmt).line;
			this.ptAnalysis.transferStmt(stmt, this.currentVarInfoWhenMatching);
		} else if (stmt instanceof IfStmt) {
			IfStmt ifStmt = (IfStmt) stmt;
			if (ifStmt.condition instanceof BinopExpr) {
				BinopExpr expr = (BinopExpr) ifStmt.condition;
				if (expr.opType == BinopExpr.EQUAL || expr.opType == BinopExpr.NEQUAL) {
					String opType = "";

					// judege whether we should consider a if-true branch or a
					// if-false branch
					BasicBlock trueBlock = block.getItsTrueBlock();
					boolean nextIsTrueBranch = false;
					// judege whether the next block is the true block?

					if (trueBlock != null) {
						for (int i = 0; i < pathBlocks.size(); i++) {
							BasicBlock temp = pathBlocks.get(i);
							if (temp.getLabel() == block.getLabel()) {
								if (pathBlocks.get(++i).getLabel() == trueBlock.getLabel()) {
									// next block is the true block
									nextIsTrueBranch = true;
								}
								break;
							}
						}
					}

					if (nextIsTrueBranch) {
						// impact as a if-true branch
						if (expr.opType == BinopExpr.EQUAL) {
							opType = "==";
						} else {
							opType = "!=";
						}
					} else {
						// impact as a if-false branch
						if (expr.opType == BinopExpr.EQUAL) {
							opType = "!=";
						} else {
							opType = "==";
						}
					}

					if (expr.op1 instanceof Null) {
						transferIfNullCheckStmt(expr.op2, opType, ams, this.currentVarInfoWhenMatching);
					} else if (expr.op2 instanceof Null) {
						transferIfNullCheckStmt(expr.op1, opType, ams, this.currentVarInfoWhenMatching);
					}
				}
			}
		} else if (stmt instanceof AssignStmt) {

			AssignStmt assignStmt = (AssignStmt) stmt;
			// not sound now. sound analysis needs to be based on the points-to
			// information
			// 赋值语句的左侧是 感兴趣的变量
			if (assignStmt.right instanceof FieldRef) {
				// update the variable info
				this.ptAnalysis.transferStmt(stmt, this.currentVarInfoWhenMatching);
				FieldRef fieldRef = (FieldRef) assignStmt.right;
				transferVarDerefEvent(fieldRef.base, this.currentLine, ams, this.currentVarInfoWhenMatching);
			} else if (assignStmt.right instanceof LocalRef) {
				this.ptAnalysis.transferStmt(stmt, this.currentVarInfoWhenMatching);
			} else if (assignStmt.right instanceof ArrayRef) {
				ArrayRef ar = (ArrayRef) assignStmt.right;
				this.ptAnalysis.transferStmt(stmt, this.currentVarInfoWhenMatching);
				transferVarDerefEvent(ar.base, this.currentLine, ams, this.currentVarInfoWhenMatching);
			} else if (assignStmt.right instanceof LengthOfExpr) {
				LengthOfExpr loe = (LengthOfExpr) assignStmt.right;
				this.ptAnalysis.transferStmt(stmt, this.currentVarInfoWhenMatching);
				transferVarDerefEvent(loe.value, this.currentLine, ams, this.currentVarInfoWhenMatching);
			} else if (assignStmt.right instanceof InvokeExpr) {
				// 查看invoker是否是感兴趣的变量，如果是的话，保留该stmt
				InvokeExpr invokeExpr = (InvokeExpr) assignStmt.right;

				// update the variable info
				this.ptAnalysis.transferStmt(stmt, this.currentVarInfoWhenMatching);

				// impact the stmt on the ams
				transferVarDerefEvent(invokeExpr.invoker, this.currentLine, ams, this.currentVarInfoWhenMatching);
				transferInvokeExpr(invokeExpr, assignStmt.left, this.currentLine, ams, this.currentVarInfoWhenMatching);

			} else if (assignStmt.right instanceof NewExpr) {
				// update the variable info
				this.ptAnalysis.transferStmt(stmt, this.currentVarInfoWhenMatching);
			}

			// left part of the assign stmt
			if (assignStmt.left instanceof FieldRef) {
				FieldRef fieldRef = (FieldRef) assignStmt.left;
				transferVarDerefEvent(fieldRef.base, this.currentLine, ams, this.currentVarInfoWhenMatching);
			} else if (assignStmt.left instanceof ArrayRef) {
				ArrayRef ar = (ArrayRef) assignStmt.left;
				transferVarDerefEvent(ar.base, this.currentLine, ams, this.currentVarInfoWhenMatching);
			}

		} else if (stmt instanceof InvokeStmt) {
			InvokeStmt invokeStmt = (InvokeStmt) stmt;
			InvokeExpr invokeExpr = (InvokeExpr) invokeStmt.invoke;

			// update the variable info
			this.ptAnalysis.transferStmt(stmt, this.currentVarInfoWhenMatching);

			// impact the stmt on the ams
			transferVarDerefEvent(invokeExpr.invoker, this.currentLine, ams, this.currentVarInfoWhenMatching);
			transferInvokeExpr(invokeExpr, null, this.currentLine, ams, this.currentVarInfoWhenMatching);

			if (invokeExpr.invoker instanceof ArrayRef) {
				ArrayRef ar = (ArrayRef) invokeExpr.invoker;
				transferVarDerefEvent(ar.base, this.currentLine, ams, this.currentVarInfoWhenMatching);
			}

		}
	}

	private void transferVarDerefEvent(JIRValue invoker, int currentLine, HashMap<String, HashSet<AutoMachine>> fact,
			HashMap<InterJIRValue, InterVarInfo> ptFact) {
		VisitVarDerefStmtEdge edge = new VisitVarDerefStmtEdge(null, null);
		// ie.invoker 调用者实例的名字
		HashSet<AutoMachine> ams = getAMForMatching(invoker, fact, ptFact);

		if (ams != null) {
			for (AutoMachine machine : ams) {
				try {
					machine.goOneStep(edge, currentLine);
				} catch (AutoMachineException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void transferIfNullCheckStmt(JIRValue op2, String opType, HashMap<String, HashSet<AutoMachine>> fact,
			HashMap<InterJIRValue, InterVarInfo> ptFact) {
		VisitNullCheckIfStmtEdge edge = new VisitNullCheckIfStmtEdge(null, null, opType);
		HashSet<AutoMachine> ams = getAMForMatching(op2, fact, ptFact);
		if (ams != null) {
			for (AutoMachine machine : ams) {
				try {
					machine.goOneStep(edge, currentLine);
				} catch (AutoMachineException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void transferInvokeExpr(InvokeExpr ie, JIRValue leftValue, int currentLine,
			HashMap<String, HashSet<AutoMachine>> fact, HashMap<InterJIRValue, InterVarInfo> ptFact) {
		// ie.node.owner 调用者所属类型
		// ie.node.name 调用方法的名字
		VisitMethodInsnEdge edge = new VisitMethodInsnEdge(null, null, ie.node.getOpcode(), ie.node.owner,
				ie.node.name, ie.node.desc);
		edge.setApplyForChild(applyForChild);

		// ie.invoker 调用者实例的名字
		HashSet<AutoMachine> ams = getAMForMatching(ie.invoker, fact, ptFact);

		if (ams != null) {
			for (AutoMachine machine : ams) {
				try {
					machine.goOneStep(edge, currentLine);
				} catch (AutoMachineException e) {
					e.printStackTrace();
				}
			};
		}

		if (leftValue != null) {
			ams = getAMForMatching(leftValue, fact, ptFact);
			if (ams != null) {
				for (AutoMachine machine : ams) {
					try {
						machine.goOneStep(edge, currentLine);
					} catch (AutoMachineException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	protected void slicePathByInterestedVars() {
		// used variables
		// this.interestedVariables
		// this.backwardBlockChains
		// this.forwardBlockChains

		// affected variables
		// this.slicedBackwardBlockChains
		// this.slicedForwardBlockChains

		// slice the path based on the interesed variables backwardly
		for (long interestedLineNo : this.backwardPaths.keySet()) {
			List<BasicBlock> backBlocks = this.backwardPaths.get(interestedLineNo);
			List<JIRValue> interestedVars = new ArrayList<JIRValue>();
			interestedVars.addAll(this.interestedVariables.get(interestedLineNo));

			for (BasicBlock block : backBlocks) {

				// if (block.getLabel() == 6) {
				// System.out.println("test");
				// }

				for (int i = block.endStmt; i >= block.startStmt; i--) {
					Stmt stmt = block.stmts.get(i);
					sliceStmtBackward(i, block, stmt, interestedVars, interestedLineNo);
				}
			}
		}

		// slice the path based on the interesed variables forwardly
		for (long interestedLineNo : this.forwardPaths.keySet()) {
			List<BasicBlock> forwardBlocks = this.forwardPaths.get(interestedLineNo);
			List<JIRValue> interestedVars = new ArrayList<JIRValue>();
			interestedVars.addAll(this.interestedVariables.get(interestedLineNo));

			for (BasicBlock block : forwardBlocks) {
				for (int i = block.startStmt; i <= block.endStmt; i++) {
					Stmt stmt = block.stmts.get(i);
					sliceStmtForward(i, block, stmt, interestedVars, interestedLineNo);
				}
			}
		}

	}

	private void sliceStmtBackward(int index, BasicBlock block, Stmt stmt, List<JIRValue> interestedVars,
			long interestedLineNo) {
		List<Stmt> sliceStmts = this.slicedBackwardPaths.get(interestedLineNo);
		if (sliceStmts == null) {
			sliceStmts = new ArrayList<Stmt>();
			this.slicedBackwardPaths.put(interestedLineNo, sliceStmts);
		}

		List<Integer> sliceStmtIds = this.slicedBackwardStmtIndexes.get(interestedLineNo);
		if (sliceStmtIds == null) {
			sliceStmtIds = new ArrayList<Integer>();
			this.slicedBackwardStmtIndexes.put(interestedLineNo, sliceStmtIds);
		}

		List<BasicBlock> sliceStmtBlocks = this.slicedBackwardStmtBlocks.get(interestedLineNo);
		if (sliceStmtBlocks == null) {
			sliceStmtBlocks = new ArrayList<BasicBlock>();
			this.slicedBackwardStmtBlocks.put(interestedLineNo, sliceStmtBlocks);
		}

		if (stmt instanceof LineStmt || stmt instanceof LabelStmt) {
			sliceStmts.add(stmt);
			sliceStmtIds.add(index);
			sliceStmtBlocks.add(block);
		} else if (stmt instanceof IfStmt) {
			IfStmt ifStmt = (IfStmt) stmt;
			if (ifStmt.condition instanceof BinopExpr) {
				BinopExpr expr = (BinopExpr) ifStmt.condition;
				if (expr.opType == BinopExpr.EQUAL || expr.opType == BinopExpr.NEQUAL) {

					JIRValue extractedVar = null;
					if (expr.op1 instanceof Null) {
						extractedVar = extractFromJIRValue(expr.op2);
					} else if (expr.op2 instanceof Null) {
						extractedVar = extractFromJIRValue(expr.op1);
					}

					if (extractedVar != null && interestedVars.contains(extractedVar)) {
						sliceStmts.add(ifStmt);
						sliceStmtIds.add(index);
						sliceStmtBlocks.add(block);
					}
				}
			}
		} else if (stmt instanceof AssignStmt) {

			AssignStmt assignStmt = (AssignStmt) stmt;

			// not sound now. sound analysis needs to be based on the points-to
			// information
			// 赋值语句的左侧是 感兴趣的变量
			JIRValue extractedVar = extractFromAssignStmt(assignStmt);
			if (extractedVar != null && interestedVars.contains(extractedVar)) {
				sliceStmts.add(assignStmt);
				sliceStmtIds.add(index);
				sliceStmtBlocks.add(block);
				// ** interestedVars.remove(extractedVar);
				// 此时如果右侧为普通field ref 或者 local ref，需要更新alias信息
				if (assignStmt.right instanceof FieldRef || assignStmt.right instanceof LocalRef) {
					interestedVars.add(assignStmt.right);
				}

				// x = b.f()?? x is the interested variable
			} else {
				// 赋值语句的右侧为 感兴趣的变量
				// a. 右侧为method invocation
				// b. 右侧为普通field ref 或者 local ref
				if (assignStmt.right instanceof FieldRef || assignStmt.right instanceof LocalRef) {
					if (interestedVars.contains(assignStmt.right)) {
						sliceStmts.add(assignStmt);
						sliceStmtIds.add(index);
						sliceStmtBlocks.add(block);

						interestedVars.add(assignStmt.left);
					}
				} else if (assignStmt.right instanceof InvokeExpr) {
					// 查看invoker是否是感兴趣的变量，如果是的话，保留该stmt
					InvokeExpr invokeExpr = (InvokeExpr) assignStmt.right;
					if (interestedVars.contains(invokeExpr.invoker)) {
						sliceStmts.add(assignStmt);
						sliceStmtIds.add(index);
						sliceStmtBlocks.add(block);
					}
				}
			}

			// update the alias of the current interested variable

		} else if (stmt instanceof InvokeStmt) {
			InvokeStmt invokeStmt = (InvokeStmt) stmt;
			InvokeExpr invokeExpr = (InvokeExpr) invokeStmt.invoke;

			if (invokeExpr.invoker != null && interestedVars.contains(invokeExpr.invoker)) {
				sliceStmts.add(invokeStmt);
				sliceStmtIds.add(index);
				sliceStmtBlocks.add(block);
			}
		}
	}

	private void sliceStmtForward(int index, BasicBlock block, Stmt stmt, List<JIRValue> interestedVars,
			long interestedLineNo) {

		List<Stmt> sliceStmts = this.slicedForwardPaths.get(interestedLineNo);
		if (sliceStmts == null) {
			sliceStmts = new ArrayList<Stmt>();
			this.slicedForwardPaths.put(interestedLineNo, sliceStmts);
		}

		List<Integer> sliceStmtIds = this.slicedForwardStmtIndexes.get(interestedLineNo);
		if (sliceStmtIds == null) {
			sliceStmtIds = new ArrayList<Integer>();
			this.slicedForwardStmtIndexes.put(interestedLineNo, sliceStmtIds);
		}

		List<BasicBlock> sliceStmtBlocks = this.slicedForwardStmtBlocks.get(interestedLineNo);
		if (sliceStmtBlocks == null) {
			sliceStmtBlocks = new ArrayList<BasicBlock>();
			this.slicedForwardStmtBlocks.put(interestedLineNo, sliceStmtBlocks);
		}

		if (stmt instanceof LineStmt) {
			sliceStmts.add(stmt);
			sliceStmtIds.add(index);
			sliceStmtBlocks.add(block);
		} else if (stmt instanceof IfStmt) {
			IfStmt ifStmt = (IfStmt) stmt;
			if (ifStmt.condition instanceof BinopExpr) {
				BinopExpr expr = (BinopExpr) ifStmt.condition;
				if (expr.opType == BinopExpr.EQUAL || expr.opType == BinopExpr.NEQUAL) {

					JIRValue extractedVar = null;
					if (expr.op1 instanceof Null) {
						extractedVar = extractFromJIRValue(expr.op2);
					} else if (expr.op2 instanceof Null) {
						extractedVar = extractFromJIRValue(expr.op1);
					}

					if (extractedVar != null && interestedVars.contains(extractedVar)) {
						sliceStmts.add(ifStmt);
						sliceStmtIds.add(index);
						sliceStmtBlocks.add(block);
					}
				}
			}
		} else if (stmt instanceof AssignStmt) {

			AssignStmt assignStmt = (AssignStmt) stmt;

			// not sound now. sound analysis needs to be based on the points-to
			// information
			// 赋值语句的左侧是 感兴趣的变量
			JIRValue extractedVar = extractFromAssignStmt(assignStmt);
			if (extractedVar != null && interestedVars.contains(extractedVar)) {
				sliceStmts.add(assignStmt);
				sliceStmtIds.add(index);
				sliceStmtBlocks.add(block);
				// ** interestedVars.remove(extractedVar);
				// 此时如果右侧为普通field ref 或者 local ref，需要更新alias信息
				if (assignStmt.right instanceof FieldRef || assignStmt.right instanceof LocalRef) {
					interestedVars.add(assignStmt.right);
				}

				// x = b.f()?? x is the interested variable
			} else {
				// 赋值语句的右侧为 感兴趣的变量
				// a. 右侧为method invocation
				// b. 右侧为普通field ref 或者 local ref
				if (assignStmt.right instanceof FieldRef) {
					FieldRef fr = (FieldRef) assignStmt.right;
					if (interestedVars.contains(assignStmt.right)) {
						sliceStmts.add(assignStmt);
						sliceStmtIds.add(index);
						sliceStmtBlocks.add(block);
						interestedVars.add(assignStmt.left);
					} else if (interestedVars.contains(fr.base)) {
						sliceStmts.add(assignStmt);
						sliceStmtIds.add(index);
						sliceStmtBlocks.add(block);
					}
				}
				if (assignStmt.right instanceof LocalRef) {
					if (interestedVars.contains(assignStmt.right)) {
						sliceStmts.add(assignStmt);
						sliceStmtIds.add(index);
						sliceStmtBlocks.add(block);
						interestedVars.add(assignStmt.left);
					}
				}
				if (assignStmt.right instanceof LengthOfExpr) {
					LengthOfExpr loe = (LengthOfExpr) assignStmt.right;
					if (interestedVars.contains(loe.value)) {
						sliceStmts.add(assignStmt);
						sliceStmtIds.add(index);
						sliceStmtBlocks.add(block);
					}
				} else if (assignStmt.right instanceof InvokeExpr) {
					// 查看invoker是否是感兴趣的变量，如果是的话，保留该stmt
					InvokeExpr invokeExpr = (InvokeExpr) assignStmt.right;
					if (interestedVars.contains(invokeExpr.invoker)) {
						sliceStmts.add(assignStmt);
						sliceStmtIds.add(index);
						sliceStmtBlocks.add(block);
					}
				}
			}

			// update the alias of the current interested variable

		} else if (stmt instanceof InvokeStmt) {
			InvokeStmt invokeStmt = (InvokeStmt) stmt;
			InvokeExpr invokeExpr = (InvokeExpr) invokeStmt.invoke;

			if (invokeExpr.invoker != null && interestedVars.contains(invokeExpr.invoker)) {
				sliceStmts.add(invokeStmt);
				sliceStmtIds.add(index);
				sliceStmtBlocks.add(block);
			}
		}
	}

	protected void setInterestedVariables() {
		// extract the interested variables from this.interestedBlocks;

		int currentLine = 0;
		// Map<Long, HashSet<BasicBlock>>
		for (long interestedLineNo : this.interestedBlocks.keySet()) {
			BasicBlock interestedBlock = (BasicBlock) this.interestedBlocks.get(interestedLineNo).toArray()[0];

			List<JIRValue> interestedVars = this.interestedVariables.get(interestedLineNo);
			if (interestedVars == null) {
				interestedVars = new ArrayList<JIRValue>();
				this.interestedVariables.put(interestedLineNo, interestedVars);
			}

			for (int i = interestedBlock.startStmt; i <= interestedBlock.endStmt; i++) {
				Stmt currentStmt = interestedBlock.stmts.get(i);

				if (currentStmt instanceof LineStmt) {
					LineStmt ls = (LineStmt) currentStmt;
					currentLine = ls.line;
				}

				if (interestedLineNo == currentLine) {
					if (currentStmt instanceof AssignStmt) {
						AssignStmt assignStmt = (AssignStmt) currentStmt;
						JIRValue var = extractFromAssignStmt(assignStmt);
						if (var != null) {
							interestedVars.add(var);
						}
					} else if (currentStmt instanceof InvokeStmt) {
						InvokeStmt invokeStmt = (InvokeStmt) currentStmt;
						InvokeExpr invokeExpr = (InvokeExpr) invokeStmt.invoke;
						interestedVars.add(invokeExpr.invoker);
					} else if (currentStmt instanceof IfStmt) {
						IfStmt ifStmt = (IfStmt) currentStmt;
						if (ifStmt.condition instanceof BinopExpr) {
							BinopExpr expr = (BinopExpr) ifStmt.condition;
							if (expr.opType == BinopExpr.EQUAL || expr.opType == BinopExpr.NEQUAL) {
								if (expr.op1 instanceof Null) {
									interestedVars.add(extractFromJIRValue(expr.op2));
								} else if (expr.op2 instanceof Null) {
									interestedVars.add(extractFromJIRValue(expr.op1));
								}
							}
						}
					}
				}
			}
		}
	}

	private JIRValue extractFromJIRValue(JIRValue value) {
		if (value instanceof FieldRef || value instanceof LocalRef) {
			return value;
		} else
			return null;
	}

	private JIRValue extractFromAssignStmt(AssignStmt assignStmt) {
		if (assignStmt.left instanceof FieldRef || assignStmt.left instanceof LocalRef) {
			return assignStmt.left;
		} else
			return null;
	}
}

// end

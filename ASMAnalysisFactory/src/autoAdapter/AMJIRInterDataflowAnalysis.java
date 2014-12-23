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
package autoAdapter;

import java.io.File;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import automachine.AutoMachine;
import automachine.AutoMachineException;
import automachine.AutomaUtil;
import automachine.VisitMethodInsnEdge;
import edu.pku.cn.analysis.CallingContext;
import edu.pku.cn.analysis.InterJIRValue;
import edu.pku.cn.analysis.InterPointsToDataflowAnalysis;
import edu.pku.cn.analysis.InterVarInfo;
import edu.pku.cn.analysis.InterValueDataflowAnalysis;
import edu.pku.cn.analysis.RefactoredBasicDataflowAnalysis;
import edu.pku.cn.analysis.RefactoredDataflow;
import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.classfile.LocalVariableNode;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.detector.Detector;
import edu.pku.cn.detector.RefactoredDataflowTestDriver;
import edu.pku.cn.graph.cfg.BasicBlock;
import edu.pku.cn.graph.cfg.CFG;
import edu.pku.cn.graph.visit.ReversePostOrder;
import edu.pku.cn.hierarchy.Repository;
import edu.pku.cn.jir.AssignStmt;
import edu.pku.cn.jir.FieldRef;
import edu.pku.cn.jir.InvokeExpr;
import edu.pku.cn.jir.InvokeStmt;
import edu.pku.cn.jir.JIRValue;
import edu.pku.cn.jir.LineStmt;
import edu.pku.cn.jir.LocalRef;
import edu.pku.cn.jir.Stmt;
import edu.pku.cn.jir.TempRef;
import edu.pku.cn.util.HASelect;

// Reason to modify: using the points to analysis to implement an
// inter-procedural instance-based
// defect analysis 20111107

public class AMJIRInterDataflowAnalysis extends RefactoredBasicDataflowAnalysis<HashMap<String, HashSet<AutoMachine>>> {

	public boolean Debug = false;

	public AutoMachine sampleAutomachine;
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
	private InsnList insns;
	private List<Stmt> stmts;
	//public CFG cfg;

	// used for inter-procedure analysis
	private int level = 0;
	private int maxInterLevel = 5;
	private CallingContext callingContext;
	private HashMap<InterJIRValue, InterVarInfo> initPointsToFact;
	private HashMap<String, HashSet<AutoMachine>> initAMFact;

	public AMJIRInterDataflowAnalysis(AutoMachine am) {
		this.isForwards = true;
		this.automachineId = am.automachineId;
		this.sampleAutomachine = am;
		am.initial();
	}

	public AMJIRInterDataflowAnalysis(CFG cfg, AutoMachine am, HashMap<String, HashSet<AutoMachine>> amFact,
			HashMap<InterJIRValue, InterVarInfo> ptFact, int level, CallingContext callingContext) {
		this.isForwards = true;
		this.automachineId = am.automachineId;
		this.sampleAutomachine = am;
		am.initial();

		this.blockOrder = new ReversePostOrder(cfg);
		this.cfg = cfg;
		this.stmts = cfg.getJIRStmt();
		this.insns = cfg.getMethod().instructions;

		this.initPointsToFact = ptFact;
		this.initAMFact = amFact;

		this.level = level;
		this.callingContext = callingContext;
	}

	public AMJIRInterDataflowAnalysis(String specificFile) {
		this.isForwards = true;
		this.specificFile = specificFile;
		this.sampleAutomachine = AutomaUtil.generateAutoMachineFromXML(specificFile);
		this.sampleAutomachine.initial();

		// initiate the pointToAnalysis

		if (sampleAutomachine.getOnlyConcernClass() != null && !"".equals(sampleAutomachine.getOnlyConcernClass())) {
			applyForChild = true;
			String[] name = sampleAutomachine.getOnlyConcernClass().split(";");
			applyType = new Type[name.length];
			for (int i = 0; i < name.length; i++) {
				applyType[i] = Type.getObjectType(name[i]);
			}
		}
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

	private void setPTAnalysis(CFG calleeCFG, HashMap<InterJIRValue, InterVarInfo> ptFact, 
			int calleeLevel, CallingContext callingContext2) {
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
	
	public AMJIRInterDataflowAnalysis(CFG cfg, AutoMachine am) {
		this.blockOrder = new ReversePostOrder(cfg);
		this.isForwards = true;
		this.cfg = cfg;
		this.stmts = cfg.getJIRStmt();
		this.insns = cfg.getMethod().instructions;
		this.automachineId = am.automachineId;
		this.sampleAutomachine = am;
		am.initial();
	}

	public AMJIRInterDataflowAnalysis(CFG cfg, String specificFile) {
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
	}

	private boolean isAssignableFrom(Type type) {
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

	private void visit(InvokeExpr ie, JIRValue leftValue, boolean nparam, int currentLine,
			HashMap<String, HashSet<AutoMachine>> fact, HashMap<InterJIRValue, InterVarInfo> ptFact) {
		if (Debug)
			System.out.println(ie.invoker.getType() + "  " + ie.node.name + ie.node.desc);

		// ie.node.owner 调用者所属类型
		// ie.node.name 调用方法的名字
		VisitMethodInsnEdge e = new VisitMethodInsnEdge(null, null, ie.node.getOpcode(), ie.node.owner, ie.node.name,
				ie.node.desc);
		e.setApplyForChild(applyForChild);

		// ie.invoker 调用者实例的名字
		HashSet<AutoMachine> ams = getAM(ie.invoker, fact, ptFact);
		if (ams != null) {
			try {
				// nparam 调用者是否是传入参数？
				if (nparam && !(ie.invoker instanceof TempRef || ie.invoker instanceof FieldRef)) {
					for (AutoMachine machine : ams) {
						machine.goOneStep(e, currentLine);
					}
				}

			} catch (AutoMachineException e1) {
				// TODO Auto-generated catch block
				// reportAmBug(e1, am.automachineName, currentLine);
				e1.printStackTrace();
			}
		}

		if (leftValue != null) {
			ams = getAM(leftValue, fact, ptFact);
			if (ams != null)
				try {
					if (!(leftValue instanceof FieldRef || leftValue instanceof TempRef)) {
						for (AutoMachine machine : ams) {
							machine.goOneStep(e, currentLine);
						}
					}
				} catch (AutoMachineException e1) {
					// TODO Auto-generated catch block
					// reportAmBug(e1, am.automachineName,
					// am.getCurrentState().getLastLine());
					e1.printStackTrace();
				}
		}

//		for (JIRValue jir : ie.params) {
//			// if(jir instanceof LocalRef){
//			ams = getAM(jir, fact, ptFact);
//			if (ams != null) {
//				for (AutoMachine machine : ams) {
//					machine.initial();
//				}
//			}
//		}
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
				newMachines.add(newMachine);
			}

			rdFact.put(key, newMachines);
		}
		return rdFact;
	}

	// @Override
	// public HashSet<String> getNewStartFact(BasicBlock block) {
	// return facts[block.startStmt];
	// }

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
	public HashMap merge(HashMap start, HashMap pred) {
		HashMap result = createFact(start);
		if (!pred.equals(result)) {
			Set<String> keys = pred.keySet();
			for (String key : keys) {
				HashSet<AutoMachine> predMachines = (HashSet<AutoMachine>) pred.get(key);
				HashSet<AutoMachine> resultMachines = (HashSet<AutoMachine>) result.get(key);

				if (resultMachines == null) {
					result.put(key, predMachines.clone());
				} else {

					for (AutoMachine machine : predMachines) {
						boolean contains = false;
						for (AutoMachine machine2 : resultMachines) {
							if (machine2.automachineName == machine.automachineName
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

		HashMap<String, HashSet<AutoMachine>> fact = this.createFact(getStartFact(block));

		HashMap<InterJIRValue, InterVarInfo> ptFact = this.ptAnalysis.createFact(this.ptAnalysis
				.getStartFact(block));

		// for (InterJIRValue interJIRValue : ptFact.keySet()) {
		// System.out.println(interJIRValue.toString() + "->");
		// System.out.println(fact.get(interJIRValue).toString());
		// }

		if (fact == null)
			fact = new HashMap<String, HashSet<AutoMachine>>();

		for (int i = block.startStmt; i <= block.endStmt; i++) {
			Stmt stmt = this.stmts.get(i);
			// fact = facts[stmt.getIndex()];
			// System.out.println("i: " + i);

			boolean nparam = true;
			if (stmt instanceof LineStmt) {
				LineStmt ls = (LineStmt) stmt;
				currentLine = ls.line;
				this.ptAnalysis.transferStmt(stmt, ptFact);
			} else if (stmt instanceof InvokeStmt) {
				
				//System.out.println(stmt.toString());
				
				InvokeStmt is = (InvokeStmt) stmt;
				InvokeExpr ix = (InvokeExpr) is.invoke;

//				if (ix.invoker instanceof LocalRef) {
//					LocalRef ref = (LocalRef) ix.invoker;
//					// PointsToAnalysis p = null;
//					// PointsToSet ps=p.reachingObjects(ref.nodeRef);
//					// param 传入参数
//					if (this.cfg.getMethod().params.contains(ref.nodeRef))
//						nparam = false;
//				}

//				 inter-procedural analysis
				if (this.level < maxInterLevel) {
					fact = interProceduralAnalysis(fact, ptFact, null, this.level, is.invoke, this.level, false);
				}
				
//				 update the pointTo information
				this.ptAnalysis.transferStmt(stmt, ptFact);
				this.visit(ix, null, nparam, currentLine, fact, ptFact);

				

			} else if (stmt instanceof AssignStmt) {
				AssignStmt as = (AssignStmt) stmt;
				if (as.right instanceof InvokeExpr) {
					InvokeExpr is = (InvokeExpr) as.right;

//					if (is.invoker instanceof LocalRef) {
//						LocalRef ref = (LocalRef) is.invoker;
//						// param 传入参数
//						if (this.cfg.getMethod().params.contains(ref.nodeRef))
//							nparam = false;
//					}

//					 inter-procedural analysis
					if (this.level < maxInterLevel) {
						fact = interProceduralAnalysis(fact, ptFact, as.left, this.level, as.right, this.level, true);
					}

					this.ptAnalysis.transferStmt(stmt, ptFact);
					this.visit(is, as.left, nparam, currentLine, fact, ptFact);

				}
			}
			
		}
		return fact;
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
			HashMap<InterJIRValue, InterVarInfo> ptFact, JIRValue leftValue, int leftValueLevel,
			JIRValue rightValue, int rightValueLevel, boolean handleResturn) {
		// e.g.: a = b.f(c);
		// A f(d){ return e}

		InvokeExpr invoke = (InvokeExpr) rightValue;

		// invoke.node.ower 调用者类型
		// todo 通过指向分析结果中的type信息，进行加载
		String classNameToAnalyze = "";
		InterVarInfo interPointsToInfo = ptFact.get(new InterJIRValue(invoke.invoker, rightValueLevel));
		if (interPointsToInfo != null) {
			Set<Type> types = interPointsToInfo.getTypes();
			if (types.size() > 1) {
				// 模糊处理
				classNameToAnalyze = ((Type) types.toArray()[0]).getClassName();

			} else if (types.size() == 1) {
				// 精确处理
				classNameToAnalyze = ((Type) types.toArray()[0]).getClassName();

			} else {
				// 无分析结果，模糊处理

			}
		}

		if ("".equals(classNameToAnalyze)) {
			classNameToAnalyze = invoke.node.owner;
		}

		ClassNode cn = getClassNode(classNameToAnalyze);

		boolean coarseAnalysis = false;

		if (cn == null) {
			Type invokeType = Type.getObjectType(invoke.node.owner);
			if (invokeType != null && invokeType.getSort() == Type.ARRAY)
				cn = getClassNode("java.lang.Object");
			else {
				// System.err.println("can not find class " +
				// invoke.node.owner);
				coarseAnalysis = true;
			}
		}

		if (cn != null) {
			MethodNode me = getMethod(invoke, cn);
			if (me != null) {
				// 成功加载callee方法，进行跨过程分析流程
				if (invoke.isStatic() || !me.isAbstract()) {// 递归分析

					CFG calleeCFG = me.getCFG();
					// System.out.println("-> step into callee:" +
					// calleeCFG.getMethod().name);

					int calleeLevel = leftValueLevel + 1;
					CallingContext callingContext = new CallingContext(cfg.getMethod(), this.currentLine);
					HashMap<InterJIRValue, InterVarInfo> calleeInitPTFact = ptFact;

					// 处理 this = b
					JIRValue actualParam = invoke.invoker;
					InterJIRValue interActualParam = new InterJIRValue(actualParam, leftValueLevel);
					LocalRef formalParamJIRValue;
					InterJIRValue interFormalParam;

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
					AMJIRInterDataflowAnalysis calleeAnalysis = new AMJIRInterDataflowAnalysis(calleeCFG,
							this.sampleAutomachine, fact, ptFact, calleeLevel, callingContext);
					calleeAnalysis.setPTAnalysis(calleeCFG, ptFact, calleeLevel, callingContext);
					
					RefactoredDataflow<HashMap<String, HashSet<AutoMachine>>, AMJIRInterDataflowAnalysis> calleeDataflow = 
						new RefactoredDataflow<HashMap<String, HashSet<AutoMachine>>, AMJIRInterDataflowAnalysis>(
								calleeCFG, calleeAnalysis);
					try {
						calleeDataflow.execute();
						fact = calleeAnalysis.getResultFact(calleeDataflow.endBlock);
					} catch (AnalyzerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					// 是抽象方法（接口方法或者虚函数），此时进行模糊处理
					coarseAnalysis = true;
				}
			} else {
				// 无法加载callee源码，进行模糊处理
				// 指向信息不发生任何变化，仅对返回信息进行处理
				// 获取a的别名信息，将a从与其别名的ref的alias删除
				coarseAnalysis = true;

			}

		} else {
			coarseAnalysis = true;
		}
		
		return fact;
	}

	

	public static void main(String[] args) {
		RefactoredDataflowTestDriver<HashMap<String, HashSet<AutoMachine>>, AMJIRInterDataflowAnalysis> driver = new RefactoredDataflowTestDriver<HashMap<String, HashSet<AutoMachine>>, AMJIRInterDataflowAnalysis>() {
			public RefactoredDataflow<HashMap<String, HashSet<AutoMachine>>, AMJIRInterDataflowAnalysis> createDataflow(
					ClassNode cc, MethodNode method) {
				// TODO Auto-generated method stub
				try {
					CFG cfg = method.getCFG();

					String file = "AutomachineSpecs\\detector_Connection_createAndClose.xml";
					AMJIRInterDataflowAnalysis analysis = new AMJIRInterDataflowAnalysis(cfg, file);

					RefactoredDataflow<HashMap<String, HashSet<AutoMachine>>, AMJIRInterDataflowAnalysis> dataflow = new RefactoredDataflow<HashMap<String, HashSet<AutoMachine>>, AMJIRInterDataflowAnalysis>(
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
			public void examineResults(
					RefactoredDataflow<HashMap<String, HashSet<AutoMachine>>, AMJIRInterDataflowAnalysis> dataflow) {
				// TODO Auto-generated method stub
				// HashSet<String>[] facts = dataflow.getAnalysis().facts;
				// List<Stmt> stmts =
				// dataflow.getAnalysis().cfg.getMethod().getStmts();
				// for (int i = 0; i < facts.length; i++) {
				// System.out.println(i + ": " + stmts.get(i).toString() + " ->
				// " + facts[i]);
				// }
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
					for (int m = block.startStmt; m <= block.endStmt; m++) {
						System.out.println(m + ": " + block.stmts.get(m).toString());
					}
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
		driver.execute("TestCloseDbConnection");
	}
}

// end

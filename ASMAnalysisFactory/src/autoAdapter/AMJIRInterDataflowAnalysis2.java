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
import edu.pku.cn.jir.AssignStmt;
import edu.pku.cn.jir.BinopExpr;
import edu.pku.cn.jir.FieldRef;
import edu.pku.cn.jir.InvokeExpr;
import edu.pku.cn.jir.InvokeStmt;
import edu.pku.cn.jir.JIRValue;
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

public class AMJIRInterDataflowAnalysis2 extends RefactoredBasicDataflowAnalysis<HashMap<String, HashSet<AutoMachine>>> {

	public boolean Debug = false;
	public boolean escapeSensitive = true;

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
	public PreconditionsDataflowAnalysis preConditionsAnalysis;

	private InsnList insns;
	private List<Stmt> stmts;

	private int maxInterLevel = 4;

	// used for inter-procedure analysis
	private CallingContext callingContext;
	private int level = 0;
	private HashMap<InterJIRValue, InterVarInfo> initPointsToFact;
	private HashMap<String, HashSet<AutoMachine>> initAMFact;

	// used for recording the precision discounts
	private PrecisionDiscountsStack precisionDiscountsStack = new PrecisionDiscountsStack();

	public AMJIRInterDataflowAnalysis2(AutoMachine am) {
		this.isForwards = true;
		this.automachineId = am.automachineId;
		this.sampleAutomachine = am;
		am.initial();
		precisionDiscountsStack.stack.put(new Integer(this.level), new PrecisionDiscountsUnit(1));
	}

	public AMJIRInterDataflowAnalysis2(CFG cfg, AutoMachine am, HashMap<String, HashSet<AutoMachine>> amFact,
			HashMap<InterJIRValue, InterVarInfo> ptFact, int level, CallingContext callingContext,
			HashMap<String, Integer> initUnableLoadMethodsNumber,
			HashMap<String, Integer> initInterObjLocationObfuscationNumber) {

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

	public AMJIRInterDataflowAnalysis2(CFG cfg, AutoMachine am, HashMap<String, HashSet<AutoMachine>> amFact,
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

	public AMJIRInterDataflowAnalysis2(String specificFile) {
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

	public AMJIRInterDataflowAnalysis2(CFG cfg, AutoMachine am) {
		this.blockOrder = new ReversePostOrder(cfg);
		this.isForwards = true;
		this.cfg = cfg;
		this.stmts = cfg.getJIRStmt();
		this.insns = cfg.getMethod().instructions;
		this.automachineId = am.automachineId;
		this.sampleAutomachine = am;
		am.initial();
	}

	public AMJIRInterDataflowAnalysis2(CFG cfg, String specificFile) {
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

	public AMJIRInterDataflowAnalysis2(CFG calleeCFG, AutoMachine am, HashMap<String, HashSet<AutoMachine>> amFact,
			HashMap<InterJIRValue, InterVarInfo> ptFact, int calleeLevel, CallingContext callingContext2,
			PrecisionDiscountsStack stack) {
		this.isForwards = true;
		this.automachineId = am.automachineId;
		this.sampleAutomachine = am;
		am.initial();

		this.blockOrder = new ReversePostOrder(calleeCFG);
		this.cfg = calleeCFG;
		this.stmts = cfg.getJIRStmt();
		this.insns = cfg.getMethod().instructions;

		this.initPointsToFact = ptFact;
		this.initAMFact = amFact;

		this.level = calleeLevel;
		this.callingContext = callingContext2;
		this.precisionDiscountsStack = stack;
	}

	private boolean isAssignableFrom(Type type) {
		for (int i = 0; i < applyType.length; i++) {
			if (HASelect.isAssignableFrom(applyType[i], type))
				return true;
		}
		return false;
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

			// 异常条件产生时，可能导致之前生成的自动机对应的对象是并不存在的，此时应该将不应该存在的object对应的自动机进行删除
			// fail-creation存在，正常的不存在，但正常的在fact中存在，需要进行删除
			for (String objStr : ptInfo.objects) {
				if (objStr.contains("fail-creation")) {
					String normalObj = objStr.substring(0, objStr.length() - 15);
					if (!ptInfo.objects.contains(normalObj)) {
						am = fact.get(normalObj);
						if (am != null) {
							fact.remove(normalObj);
						}
					}
				}
			}

		}
		return ams;
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
		HashSet<AutoMachine> ams = getAMForMatching(ie.invoker, fact, ptFact);
		// 根据conditions为edge生成conditions的字符串
		edge.conditionsRegExp = generateConditionsStr(ie.invoker, conditions);

		if (ams != null) {
			try {
				// nparam 调用者是否是传入参数？
				if (nparam && !(ie.invoker instanceof FieldRef)) {
					for (AutoMachine machine : ams) {

						// 一旦状态发生迁移，需要将当前分析的上下文进行记录
						// 需要为每个自动机的状态对象增加相应属性进行记录
						machine.goOneStep(edge, currentLine, this.precisionDiscountsStack.stack, this.level);
					}
				} else {
					for (AutoMachine machine : ams) {

						// 一旦状态发生迁移，需要将当前分析的上下文进行记录
						// 需要为每个自动机的状态对象增加相应属性进行记录
						machine.goOneStep(edge, currentLine, this.precisionDiscountsStack.stack, this.level);
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
			edge.conditionsRegExp = generateConditionsStr(leftValue, conditions);
			if (ams != null)
				try {
					if (!(leftValue instanceof FieldRef)) {
						for (AutoMachine machine : ams) {
							machine.goOneStep(edge, currentLine, this.precisionDiscountsStack.stack, this.level);
						}
					} else {
						for (AutoMachine machine : ams) {
							machine.goOneStep(edge, currentLine, this.precisionDiscountsStack.stack, this.level);
						}
					}
				} catch (AutoMachineException e1) {
					// TODO Auto-generated catch block
					// reportAmBug(e1, am.automachineName,
					// am.getCurrentState().getLastLine());
					e1.printStackTrace();
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

		this.currentBlock = block;

		HashMap<String, HashSet<AutoMachine>> fact = this.createFact(getStartFact(block));
		HashMap<InterJIRValue, InterVarInfo> optFact = this.ptAnalysis.getStartFact(block);
		HashMap<InterJIRValue, InterVarInfo> ptFact = this.ptAnalysis.createFact(optFact);
		HashSet<BinopExpr> conditions = this.preConditionsAnalysis.createFact(this.preConditionsAnalysis
				.getStartFact(block));

		if (fact == null)
			fact = new HashMap<String, HashSet<AutoMachine>>();

		for (int i = block.startStmt; i <= block.endStmt; i++) {

			if (i == 36) {
				System.out.println("Debug Point");
			}

			if (this.stmts != null && this.stmts.size() < i + 1) {
				continue;
			}

			Stmt stmt = this.stmts.get(i);
			// fact = facts[stmt.getIndex()];
			// System.out.println("i: " + i);

			boolean nparam = true;
			if (stmt instanceof LineStmt) {
				LineStmt ls = (LineStmt) stmt;
				currentLine = ls.line;
				this.ptAnalysis.transferStmt(stmt, ptFact);
			} else if (stmt instanceof InvokeStmt) {

				// System.out.println(stmt.toString());

				InvokeStmt is = (InvokeStmt) stmt;
				InvokeExpr ix = (InvokeExpr) is.invoke;

				if (Project.debugStatus == true && ix.getMethodName().contains(Project.transferEdgeName)) {
					System.out.println(ix.getMethodName() + ix.getMethodDesc());
				}

				// if (ix.invoker instanceof LocalRef) {
				// LocalRef ref = (LocalRef) ix.invoker;
				// // PointsToAnalysis p = null;
				// // PointsToSet ps=p.reachingObjects(ref.nodeRef);
				// // param 传入参数
				// if (this.cfg.getMethod().params.contains(ref.nodeRef))
				// nparam = false;
				// }
				// System.out.println(this.level + " " + this.numIterations + "
				// " + stmt.toString());
				// System.out.println(this.level + " " +
				// this.precisionDiscountSources.toString());

				// inter-procedural analysis
				fact = interProceduralAnalysis(fact, ptFact, null, this.level, is.invoke, this.level, false);

				// update the pointTo information
				this.ptAnalysis.transferStmt(stmt, ptFact);
				this.visit(ix, null, nparam, currentLine, fact, ptFact, conditions);

			} else if (stmt instanceof AssignStmt) {
				AssignStmt as = (AssignStmt) stmt;
				if (as.right instanceof InvokeExpr) {
					InvokeExpr is = (InvokeExpr) as.right;

					if (Project.debugStatus == true && is.getMethodName().contains(Project.transferEdgeName)) {
						System.out.println(is.getMethodName() + is.getMethodDesc());
					}

					// if (is.invoker instanceof LocalRef) {
					// LocalRef ref = (LocalRef) is.invoker;
					// // param 传入参数
					// if (this.cfg.getMethod().params.contains(ref.nodeRef))
					// nparam = false;
					// }

					// inter-procedural analysis
					fact = interProceduralAnalysis(fact, ptFact, as.left, this.level, as.right, this.level, true);

					this.ptAnalysis.transferStmt(stmt, ptFact);
					this.visit(is, as.left, nparam, currentLine, fact, ptFact, conditions);

				} else if (as.right instanceof NewExpr) {
					NewExpr newExpr = (NewExpr) as.right;
					this.ptAnalysis.transferStmt(stmt, ptFact);
					this.visit(newExpr, as.left, nparam, currentLine, fact, ptFact, conditions);
				} else if (as.right instanceof FieldRef || as.right instanceof LocalRef || as.right instanceof TempRef
						|| as.right instanceof ParamRef) {
					this.ptAnalysis.transferStmt(stmt, ptFact);
				}

			} else if (stmt instanceof ReturnStmt || stmt instanceof ThrowStmt) {
				this.visit(nparam, currentLine, fact, ptFact, conditions);
			}
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
				// 一旦状态发生迁移，需要将当前分析的上下文进行记录
				// 需要为每个自动机的状态对象增加相应属性进行记录
				try {
					// nparam 调用者是否是传入参数？
					if (nparam) {
						machine.goOneStep(edge, currentLine, this.precisionDiscountsStack.stack, this.level);
					}
				} catch (AutoMachineException e1) {
					// TODO Auto-generated catch block
					// reportAmBug(e1, am.automachineName, currentLine);
					e1.printStackTrace();
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
			ams = getAMForMatching(leftValue, fact, ptFact);
			edge.conditionsRegExp = generateConditionsStr(leftValue, conditions);
			if (ams != null)
				try {
					if (!(leftValue instanceof FieldRef)) {
						for (AutoMachine machine : ams) {
							machine.goOneStep(edge, currentLine, this.precisionDiscountsStack.stack, this.level);
						}
					}
				} catch (AutoMachineException e1) {
					e1.printStackTrace();
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
				this.precisionDiscountsStack.updateDiscountsWhenUnableLoadMethods(level, affectedObjects,
						callerSigniture, this.currentLine, calleeSigniture);
				System.err.println("can not find class " + invoke.node.owner);
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

				// 记录discount
				this.precisionDiscountsStack.updateDiscountsWhenObfuscateInterObjects(level, affectedObjects,
						callerSigniture, this.currentLine, calleeSigniture, interObjPrecision);

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
					PrecisionDiscountsUnit calleeUnit = this.precisionDiscountsStack.stack.get(this.level).clone();
					// todo 根据指向信息的准确度进行调整
					calleeUnit.setInvokePrecision(1.0);
					// String: callerName-callerSite(lineNumberOfCallerSite)
					calleeUnit.callStack.put(level, callerSigniture + "-" + this.currentLine);
					calleeUnit.computeCallStackString();

					this.precisionDiscountsStack.stack.put(calleeLevel, calleeUnit);

					AMJIRInterDataflowAnalysis2 calleeAnalysis = new AMJIRInterDataflowAnalysis2(calleeCFG,
							this.sampleAutomachine, fact, ptFact, calleeLevel, callingContext,
							this.precisionDiscountsStack);
					calleeAnalysis.setPTAnalysis(calleeCFG, ptFact, calleeLevel, callingContext);
					calleeAnalysis.setPreconditionsAnalysis(calleeCFG);

					try {

						calleeAnalysis.execute();
						fact = calleeAnalysis.getResultFact(calleeAnalysis.endBlock);

						// 根据callee的discounts更新caller的discounts
						calleeUnit = this.precisionDiscountsStack.stack.get(calleeLevel);
						PrecisionDiscountsUnit callerUnit = this.precisionDiscountsStack.stack.get(this.level);

						for (String obj : calleeUnit.getHistoricalExceedMaxInterDeptMethods().keySet()) {
							callerUnit.getHistoricalExceedMaxInterDeptMethods().put(obj,
									calleeUnit.getHistoricalExceedMaxInterDeptMethods().get(obj));
						}

						for (String obj : calleeUnit.getHistoricalExceedMaxIterMethods().keySet()) {
							callerUnit.getHistoricalExceedMaxIterMethods().put(obj,
									calleeUnit.getHistoricalExceedMaxIterMethods().get(obj));
						}

						for (String obj : calleeUnit.getUnableLoadedMethods().keySet()) {
							callerUnit.getUnableLoadedMethods().put(obj, calleeUnit.getUnableLoadedMethods().get(obj));
						}

						for (String obj : calleeUnit.getHistoricalInterAmbiguities().keySet()) {
							callerUnit.getHistoricalInterAmbiguities().put(obj,
									calleeUnit.getHistoricalInterAmbiguities().get(obj));
						}

						// 删除无用的callee的discouts
						this.precisionDiscountsStack.stack.remove(calleeLevel);

					} catch (AnalyzerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		} else { // 超过最大跨过程分析层数，不进行后续分析
			// update
			// System.out.println("Excced Inter " + this.level + " " +
			// this.numIterations);
			this.precisionDiscountsStack.updateDiscountsWhenExceedMaxInterDepth(level, affectedObjects,
					callerSigniture, this.currentLine, calleeSigniture);
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
				this.precisionDiscountsStack.updateDiscountsWhenExceedMaxIterTimes(level, ams.keySet(), this.cfg
						.getMethod().getOriginalFullName());
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

							pred = createFact(getResultFact(logicalPred));
							startBack = merge(startBack, pred);

						} else {
							pred = createFact(getStartFact(logicalPred));
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

	public static void main(String[] args) {
		RefactoredDataflowTestDriver<HashMap<String, HashSet<AutoMachine>>, AMJIRInterDataflowAnalysis2> driver = new RefactoredDataflowTestDriver<HashMap<String, HashSet<AutoMachine>>, AMJIRInterDataflowAnalysis2>() {
			public RefactoredDataflow<HashMap<String, HashSet<AutoMachine>>, AMJIRInterDataflowAnalysis2> createDataflow(
					ClassNode cc, MethodNode method) {
				// TODO Auto-generated method stub
				try {
					CFG cfg = method.getCFG();

					String file = "AutomachineSpecs\\detector_Connection_createAndConditionalClose.xml";

					AMJIRInterDataflowAnalysis2 analysis = new AMJIRInterDataflowAnalysis2(cfg, file);

					analysis.setAnalyzedMethod(cfg);
					analysis.setPTAnalysis(cfg);
					analysis.setPreconditionsAnalysis(cfg);

					RefactoredDataflow<HashMap<String, HashSet<AutoMachine>>, AMJIRInterDataflowAnalysis2> dataflow = new RefactoredDataflow<HashMap<String, HashSet<AutoMachine>>, AMJIRInterDataflowAnalysis2>(
							cfg, analysis);

					analysis.execute();
					// dataflow.execute();

					return dataflow;
				} catch (AnalyzerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public void examineResults(
					RefactoredDataflow<HashMap<String, HashSet<AutoMachine>>, AMJIRInterDataflowAnalysis2> dataflow) {
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
		driver.execute("TestCloseDbConnection2");
	}
}

// end

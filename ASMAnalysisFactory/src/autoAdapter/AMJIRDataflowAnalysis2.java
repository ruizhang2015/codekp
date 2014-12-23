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
import edu.pku.cn.analysis.InterJIRValue;
import edu.pku.cn.analysis.InterPointsToDataflowAnalysis;
import edu.pku.cn.analysis.InterVarInfo;
import edu.pku.cn.analysis.InterValueDataflowAnalysis;
import edu.pku.cn.analysis.RefactoredBasicDataflowAnalysis;
import edu.pku.cn.analysis.RefactoredDataflow;
import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.detector.Detector;
import edu.pku.cn.detector.RefactoredDataflowTestDriver;
import edu.pku.cn.graph.cfg.BasicBlock;
import edu.pku.cn.graph.cfg.CFG;
import edu.pku.cn.graph.visit.ReversePostOrder;
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

// Reason to modify: using the points to analysis to implement an instance-based
// defect analysis 20111106

public class AMJIRDataflowAnalysis2 extends RefactoredBasicDataflowAnalysis<HashMap<String, HashSet<AutoMachine>>> {

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
	public CFG cfg;

	public AMJIRDataflowAnalysis2(AutoMachine am) {
		this.isForwards = true;
		this.automachineId = am.automachineId;
		this.sampleAutomachine = am;
		am.initial();
	}

	public AMJIRDataflowAnalysis2(String specificFile) {
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

	public AMJIRDataflowAnalysis2(CFG cfg, AutoMachine am) {
		this.blockOrder = new ReversePostOrder(cfg);
		this.isForwards = true;
		this.cfg = cfg;
		this.stmts = cfg.getJIRStmt();
		this.insns = cfg.getMethod().instructions;
		this.automachineId = am.automachineId;
		this.sampleAutomachine = am;
		am.initial();
	}

	public AMJIRDataflowAnalysis2(CFG cfg, String specificFile) {
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

		InterVarInfo ptInfo = ptFact.get(new InterJIRValue(key, 0));
		if (ptInfo.getObjects() != null) {
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

		for (JIRValue jir : ie.params) {
			// if(jir instanceof LocalRef){
			ams = getAM(jir, fact, ptFact);
			if (ams != null) {
				for (AutoMachine machine : ams) {
					machine.initial();
				}
			}
		}
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
		HashMap rdFact = createFact();
		startFactMap.put(blockOrder.getEntry(), rdFact);
	}

	@Override
	public boolean isEndBlock(BasicBlock block) {
		return block.getStartInc() >= insns.size();
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
			} else if (stmt instanceof InvokeStmt) {
				InvokeStmt is = (InvokeStmt) stmt;
				InvokeExpr ix = (InvokeExpr) is.invoke;

				if (ix.invoker instanceof LocalRef) {
					LocalRef ref = (LocalRef) ix.invoker;
					// PointsToAnalysis p = null;
					// PointsToSet ps=p.reachingObjects(ref.nodeRef);
					// param 传入参数
					if (this.cfg.getMethod().params.contains(ref.nodeRef))
						nparam = false;
				}

				this.visit(ix, null, nparam, currentLine, fact, ptFact);

				// fact.add("" + ix.toString());
				// facts[i] = fact;

			} else if (stmt instanceof AssignStmt) {
				AssignStmt as = (AssignStmt) stmt;
				if (as.right instanceof InvokeExpr) {
					InvokeExpr is = (InvokeExpr) as.right;

					if (is.invoker instanceof LocalRef) {
						LocalRef ref = (LocalRef) is.invoker;
						// param 传入参数
						if (this.cfg.getMethod().params.contains(ref.nodeRef))
							nparam = false;
					}

					// fact.add("" + is.node.getOpcode() + ":" + is.node.owner +
					// "." + is.node.name);
					this.visit(is, as.left, nparam, currentLine, fact, ptFact);

					// fact.add("" + is.toString());
					// facts[i] = fact;
				}
			}
			// update the points-to information
			this.ptAnalysis.transferStmt(stmt, ptFact);
		}
		return fact;
	}

	public static void main(String[] args) {
		RefactoredDataflowTestDriver<HashMap<String, HashSet<AutoMachine>>, AMJIRDataflowAnalysis2> driver = new RefactoredDataflowTestDriver<HashMap<String, HashSet<AutoMachine>>, AMJIRDataflowAnalysis2>() {
			public RefactoredDataflow<HashMap<String, HashSet<AutoMachine>>, AMJIRDataflowAnalysis2> createDataflow(
					ClassNode cc, MethodNode method) {
				// TODO Auto-generated method stub
				try {
					CFG cfg = method.getCFG();

					String file = "AutomachineSpecs\\detector_Connection_createAndConditionalClose.xml";
					AMJIRDataflowAnalysis2 analysis = new AMJIRDataflowAnalysis2(cfg, file);

					RefactoredDataflow<HashMap<String, HashSet<AutoMachine>>, AMJIRDataflowAnalysis2> dataflow = new RefactoredDataflow<HashMap<String, HashSet<AutoMachine>>, AMJIRDataflowAnalysis2>(
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
					RefactoredDataflow<HashMap<String, HashSet<AutoMachine>>, AMJIRDataflowAnalysis2> dataflow) {
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

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
import edu.pku.cn.point.PointsToAnalysis;
import edu.pku.cn.point.PointsToSet;
import edu.pku.cn.util.HASelect;

public class AMJIRDataflowAnalysis extends RefactoredBasicDataflowAnalysis<HashMap<JIRValue, HashSet<AutoMachine>>> {

	public boolean Debug = false;

	public AutoMachine sampleAutomachine;
	// automachine file path
	public String specificFile;
	// automachine database id
	int automachineId;

	boolean applyForChild = false;
	Type[] applyType;

	HashMap<JIRValue, HashSet<AutoMachine>> nodeAm = new HashMap<JIRValue, HashSet<AutoMachine>>();

	int currentLine;
	String desc;
	boolean isInConstructor = false;

	private InsnList insns;
	private List<Stmt> stmts;
	public CFG cfg;

	public AMJIRDataflowAnalysis(AutoMachine am) {
		this.isForwards = true;
		this.automachineId = am.automachineId;
		this.sampleAutomachine = am;
		am.initial();
	}
	
	public AMJIRDataflowAnalysis(String specificFile) {
		this.isForwards = true;
		this.specificFile = specificFile;
		this.sampleAutomachine = AutomaUtil.generateAutoMachineFromXML(specificFile);
		this.sampleAutomachine.initial();
		
		if (sampleAutomachine.getOnlyConcernClass() != null && !"".equals(sampleAutomachine.getOnlyConcernClass())) {
			applyForChild = true;
			String[] name = sampleAutomachine.getOnlyConcernClass().split(";");
			applyType = new Type[name.length];
			for (int i = 0; i < name.length; i++) {
				applyType[i] = Type.getObjectType(name[i]);
			}
		}
	}
	
	public void setAnalyzedMethod(CFG cfg){
		this.blockOrder = new ReversePostOrder(cfg);
		this.cfg = cfg;
		this.stmts = cfg.getJIRStmt();
		this.insns = cfg.getMethod().instructions;
	}
	
	public AMJIRDataflowAnalysis(CFG cfg, AutoMachine am) {
		this.blockOrder = new ReversePostOrder(cfg);
		this.isForwards = true;
		this.cfg = cfg;
		this.stmts = cfg.getJIRStmt();
		this.insns = cfg.getMethod().instructions;
		this.automachineId = am.automachineId;
		this.sampleAutomachine = am;
		am.initial();
	}

	public AMJIRDataflowAnalysis(CFG cfg, String specificFile) {
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

	private HashSet<AutoMachine> getAM(JIRValue key, HashMap<JIRValue, HashSet<AutoMachine>> fact) {
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
		try {
			if (am == null) {
				AutoMachine newMachine = sampleAutomachine.getCopy();
				newMachine.automachineName = keyType.getClassName();
				am = new HashSet<AutoMachine>();
				am.add(newMachine);
				fact.put(key, am);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return am;
	}

	private void visit(InvokeExpr ie, JIRValue leftValue, boolean nparam, int currentLine,
			HashMap<JIRValue, HashSet<AutoMachine>> fact) {
		if (Debug)
			System.out.println(ie.invoker.getType() + "  " + ie.node.name + ie.node.desc);

		// ie.node.owner 调用者所属类型
		// ie.node.name 调用方法的名字
		VisitMethodInsnEdge e = new VisitMethodInsnEdge(null, null, ie.node.getOpcode(), ie.node.owner, ie.node.name,
				ie.node.desc);
		e.setApplyForChild(applyForChild);

		// ie.invoker 调用者实例的名字
		HashSet<AutoMachine> am = getAM(ie.invoker, fact);
		if (am != null)
			try {
				// nparam 调用者是否是传入参数？
				if (nparam && !(ie.invoker instanceof TempRef || ie.invoker instanceof FieldRef)) {
					for (AutoMachine machine : am) {
						machine.goOneStep(e, currentLine);
					}
				}

			} catch (AutoMachineException e1) {
				// TODO Auto-generated catch block
				// reportAmBug(e1, am.automachineName, currentLine);
				e1.printStackTrace();
			}
		if (leftValue != null) {
			am = getAM(leftValue, fact);
			if (am != null)
				try {

					if (!(leftValue instanceof FieldRef || leftValue instanceof TempRef)) {
						for (AutoMachine machine : am) {
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
			am = getAM(jir, fact);
			if (am != null) {
				for (AutoMachine machine : am) {
					machine.initial();
				}
			}
		}
	}

	@Override
	public HashMap<JIRValue, HashSet<AutoMachine>> createFact() {
		HashMap<JIRValue, HashSet<AutoMachine>> fact = new HashMap<JIRValue, HashSet<AutoMachine>>();
		return fact;
	}

	@Override
	public HashMap<JIRValue, HashSet<AutoMachine>> createFact(HashMap<JIRValue, HashSet<AutoMachine>> fact) {
		HashMap<JIRValue, HashSet<AutoMachine>> rdFact = new HashMap<JIRValue, HashSet<AutoMachine>>();
		Set<JIRValue> keys = fact.keySet();
		for (JIRValue key : keys) {
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
			Set<JIRValue> keys = pred.keySet();
			for (JIRValue key : keys) {
				HashSet<AutoMachine> predMachines = (HashSet<AutoMachine>) pred.get(key);
				HashSet<AutoMachine> resultMachines = (HashSet<AutoMachine>) result.get(key);

				if (resultMachines == null) {
					result.put(key, predMachines.clone());
				} else {
					
					for (AutoMachine machine : predMachines) {
						boolean contains = false;
						for (AutoMachine machine2 : resultMachines) {
							if(machine2.automachineName == machine.automachineName 
									&& machine2.currentState.getStateNumber() == machine.currentState.getStateNumber()){
								contains = true;
							}
						}
						
						if(contains == false){
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
		
		if(fact1.keySet().size() != fact2.keySet().size())
			return false;
		
		Set<JIRValue> keys = fact1.keySet();
		for (JIRValue key : keys) {
			HashSet<AutoMachine> fact1Machines = (HashSet<AutoMachine>) fact1.get(key);
			HashSet<AutoMachine> fact2Machines = (HashSet<AutoMachine>) fact2.get(key);
			
			if(fact1Machines.size() != fact2Machines.size()){
				return false;
			}
			
			for (AutoMachine machine : fact1Machines) {
				boolean contains = false;
				for (AutoMachine machine2 : fact2Machines) {
					if(machine2.automachineName == machine.automachineName 
							&& machine2.currentState.getStateNumber() == machine.currentState.getStateNumber()){
						contains = true;
					}
				}
				
				if(contains == false)
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
	public HashMap<JIRValue, HashSet<AutoMachine>> transferVertex(BasicBlock block) {

		HashMap<JIRValue, HashSet<AutoMachine>> fact = this.createFact(getStartFact(block));

		if (fact == null)
			fact = new HashMap<JIRValue, HashSet<AutoMachine>>();

		for (int i = block.startStmt; i <= block.endStmt; i++) {
			Stmt stmt = this.stmts.get(i);
			// fact = facts[stmt.getIndex()];
			System.out.println("i: " + i);

			if (i == 26) {
				System.out.println(i);
			}

			boolean nparam = true;
			if (stmt instanceof LineStmt) {
				LineStmt ls = (LineStmt) stmt;
				currentLine = ls.line;

			} else if (stmt instanceof InvokeStmt) {
				InvokeStmt is = (InvokeStmt) stmt;
				InvokeExpr ix = (InvokeExpr) is.invoke;

				if (ix.invoker instanceof LocalRef) {
					LocalRef ref = (LocalRef) ix.invoker;
//					PointsToAnalysis p = null;
//					PointsToSet ps=p.reachingObjects(ref.nodeRef);
					// param 传入参数
					if (this.cfg.getMethod().params.contains(ref.nodeRef))
						nparam = false;
				}

				this.visit(ix, null, nparam, currentLine, fact);

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
					this.visit(is, as.left, nparam, currentLine, fact);

					// fact.add("" + is.toString());
					// facts[i] = fact;
				}
			}
		}
		return fact;
	}
	
	public static void main(String[] args) {
		RefactoredDataflowTestDriver<HashMap<JIRValue, HashSet<AutoMachine>>, AMJIRDataflowAnalysis> driver = new RefactoredDataflowTestDriver<HashMap<JIRValue, HashSet<AutoMachine>>, AMJIRDataflowAnalysis>() {
			public RefactoredDataflow<HashMap<JIRValue, HashSet<AutoMachine>>, AMJIRDataflowAnalysis> createDataflow(
					ClassNode cc, MethodNode method) {
				// TODO Auto-generated method stub
				try {
					CFG cfg = method.getCFG();

					String file = "AutomachineSpecs\\detector_Connection_createAndClose.xml";
					AMJIRDataflowAnalysis analysis = new AMJIRDataflowAnalysis(cfg, file);

					RefactoredDataflow<HashMap<JIRValue, HashSet<AutoMachine>>, AMJIRDataflowAnalysis> dataflow = new RefactoredDataflow<HashMap<JIRValue, HashSet<AutoMachine>>, AMJIRDataflowAnalysis>(
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
					RefactoredDataflow<HashMap<JIRValue, HashSet<AutoMachine>>, AMJIRDataflowAnalysis> dataflow) {
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
					HashMap<JIRValue, HashSet<AutoMachine>> start = dataflow.getAnalysis().getStartFact(block);
					
					// result 有问题，但下一个block的start是没有问题的
					HashMap<JIRValue, HashSet<AutoMachine>> result = dataflow.getAnalysis().getResultFact(block);

					System.out.println("________________________________");
					Set<JIRValue> keys = start.keySet();

					for (JIRValue key : keys) {
						System.out.println(key.toString());
						HashSet<AutoMachine> machines = (HashSet<AutoMachine>) start.get(key);

						for (AutoMachine machine: machines) {
							System.out.println("	" + machine.automachineName + ";" + machine.currentState.getStateNumber() + " " + machine.lastStateChangingLine);
						}
					}
					System.out.println("");
					for (int m = block.startStmt; m <= block.endStmt; m++) {
						System.out.println(m + ": " + block.stmts.get(m).toString());
					}
					System.out.println("");
					
					keys = result.keySet();
					for (JIRValue key : keys) {
						System.out.println(key.toString());
						HashSet<AutoMachine> machines = (HashSet<AutoMachine>) result.get(key);

						for (AutoMachine machine: machines) {
							System.out.println("	" + machine.automachineName + ";" + machine.currentState.getStateNumber() + " " + machine.lastStateChangingLine);
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

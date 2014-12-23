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
package edu.pku.cn.analysis;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import edu.pku.cn.util.DBUtil;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import edu.pku.cn.Project;
import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.classfile.ClassNodeLoader;
import edu.pku.cn.classfile.LocalVariableNode;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.classfile.PackageResource;
import edu.pku.cn.graph.cfg.BasicBlock;
import edu.pku.cn.graph.cfg.CFG;
import edu.pku.cn.graph.cfg.Edge;
import edu.pku.cn.graph.cfg.EdgeType;
import edu.pku.cn.graph.visit.ReversePostOrder;
import edu.pku.cn.hierarchy.Repository;
import edu.pku.cn.jir.AnyNewExpr;
import edu.pku.cn.jir.ArrayRef;
import edu.pku.cn.jir.AssignStmt;
import edu.pku.cn.jir.BinopExpr;
import edu.pku.cn.jir.CastExpr;
import edu.pku.cn.jir.CaughtExceptionRef;
import edu.pku.cn.jir.Constant;
import edu.pku.cn.jir.Expr;
import edu.pku.cn.jir.FieldRef;
import edu.pku.cn.jir.IfStmt;
import edu.pku.cn.jir.IntConstant;
import edu.pku.cn.jir.InvokeExpr;
import edu.pku.cn.jir.InvokeStmt;
import edu.pku.cn.jir.JIRValue;
import edu.pku.cn.jir.LineStmt;
import edu.pku.cn.jir.LocalRef;
import edu.pku.cn.jir.NewArrayExpr;
import edu.pku.cn.jir.NewExpr;
import edu.pku.cn.jir.Null;
import edu.pku.cn.jir.Ref;
import edu.pku.cn.jir.ReturnStmt;
import edu.pku.cn.jir.Stmt;
import edu.pku.cn.jir.StringConstant;
import edu.pku.cn.jir.TempRef;
import edu.pku.cn.jir.ThrowStmt;
import edu.pku.cn.util.AnalysisFactoryManager;
import edu.pku.cn.util.CodaProperties;

// 此版本异常条件不去分 or 和 and 的区别，会将所有条件进行收集 20100701
// 此版修改于ExceptionPrecondictionDataflowAnalysis，修改目的是为了进行 conditional defect pattern的检测流程中在进行状态机状态迁移过程中所需的 每个block的 preconditions的收集 20110128

public class PreconditionsDataflowAnalysis extends RefactoredBasicDataflowAnalysis<HashSet<BinopExpr>> {

	private static final int MAX_ITERS = 10;
	private int numIterations = 0;
	private final boolean DEBUG = false;

	int currentLine;
	String desc;
	boolean isInConstructor = false;

	private InsnList insns;
	private List<Stmt> stmts;
	private CFG cfg;

	HashMap<JIRValue, RefVarInfo> pointsToInfos = new HashMap<JIRValue, RefVarInfo>();

	public PreconditionsDataflowAnalysis(CFG cfg) {
		this.blockOrder = new ReversePostOrder(cfg);
		this.isForwards = true;
		this.cfg = cfg;
		this.stmts = cfg.getJIRStmt();
		this.insns = cfg.getMethod().instructions;
	}

	@Override
	public HashSet<BinopExpr> createFact() {
		HashSet<BinopExpr> fact = new HashSet<BinopExpr>();
		return fact;
	}

	@Override
	public HashSet<BinopExpr> createFact(HashSet<BinopExpr> fact) {
		HashSet<BinopExpr> newFact = new HashSet<BinopExpr>();
		for (BinopExpr expr : fact) {
			newFact.add(expr);
		}
		return newFact;
	}

	// @Override
	// public HashSet<String> getNewStartFact(BasicBlock block) {
	// return facts[block.startStmt];
	// }

	@Override
	public void initEntryFact() {
		HashSet rdFact = createFact();
		startFactMap.put(blockOrder.getEntry(), rdFact);
	}

	@Override
	public boolean isEndBlock(BasicBlock block) {
		return block.getStartInc() >= insns.size();
	}

	@Override
	public HashSet<BinopExpr> merge(HashSet<BinopExpr> start, HashSet<BinopExpr> pred) {
		HashSet<BinopExpr> result = this.createFact(start);
		for (BinopExpr expr : pred) {
			if (!result.contains(expr)){
				result.add(expr);
			}
		}
		return result;
	}

	@Override
	public boolean same(HashSet<BinopExpr> fact1, HashSet<BinopExpr> fact2) {
		if (fact1.size() != fact2.size())
			return false;

		for (BinopExpr expr : fact2) {
			if (!fact1.contains(expr))
				return false;
		}

		return true;
	}

	@Override
	public HashSet<BinopExpr> transferVertex(BasicBlock block) {

		HashSet<BinopExpr> fact = this.createFact(getStartFact(block));

		if (fact == null)
			fact = new HashSet<BinopExpr>();

		for (int i = block.startStmt; i <= block.endStmt &&  block.endStmt != 0; i++) {
			Stmt stmt = this.stmts.get(i);
			// fact = facts[stmt.getIndex()];
			// System.out.println("i: " + i);

			if (stmt instanceof LineStmt) {
				LineStmt ls = (LineStmt) stmt;
				currentLine = ls.line;
			} else if (stmt instanceof IfStmt) {
				IfStmt ifStmt = (IfStmt) stmt;
				if (ifStmt.condition instanceof BinopExpr) {
					BinopExpr expr = (BinopExpr) ifStmt.condition;

					switch (expr.opType) {
					case BinopExpr.EQUAL:
					case BinopExpr.NEQUAL:
					case BinopExpr.LT:
					case BinopExpr.GTE:
					case BinopExpr.GT:
					case BinopExpr.LTE:
						fact.add(expr);
					default:
					}
				}
			} 
		}

		// printTransferResult();
		return fact;

	}

	private void printTransferResult() {

		for (JIRValue jirValue : this.pointsToInfos.keySet()) {
			System.out.println(jirValue.toString() + "->");
			System.out.println(this.pointsToInfos.get(jirValue).toString());
		}

	}

	public void initEntryBlock() {
		this.initEntryFact();
		HashSet<BinopExpr> result = this.transferVertex(logicalEntryBlock());
		this.setResultFact(logicalEntryBlock(), result);
	}

	protected BasicBlock logicalEntryBlock() {
		if (blockOrder instanceof ReversePostOrder)
			return cfg.getRoot();
		else
			return cfg.getExit();
	}



	protected String getFullyQualifiedMethodName() {
		return cfg.getMethod().name;
	}

	protected Iterator<Edge> logicalPredecessorEdgeIterator(BasicBlock block) {
		return isForwards ? block.inEdgeIterator() : block.outEdgeIterator();
	}

	protected Iterator<Edge> logicalSuccessorEdgeIterator(BasicBlock block) {
		return isForwards ? block.outEdgeIterator() : block.inEdgeIterator();
	}

	public synchronized void execute() throws AnalyzerException {
		boolean change;
		// OpcodeUtil.printInsnList(insns, cfg.getMethod().name +
		// cfg.getMethod().desc);
		initEntryBlock();

		int timestamp = 0;
		do {
			change = false;
			// if(numIterations >= 10)
			// break;
			if (numIterations >= MAX_ITERS) {
				System.out.println("Too many iterations (" + numIterations + ") in dataflow when analyzing "
						+ getFullyQualifiedMethodName());
				break;
			}

			startIteration();

			Iterator<BasicBlock> i = blockOrder.blockIterator();
			while (i.hasNext()) {
				BasicBlock block = i.next();
				HashSet<BinopExpr> start = getStartFact(block);
				HashSet<BinopExpr> result = getResultFact(block);

				boolean isEndBlock = isEndBlock(block);
				boolean needToRecompute = false;

				if (!isEndBlock && start != null) {// if the original start is
					// not equal to new start,
					// need to recompute
					if (getNewStartFact(block) == null) {
						needToRecompute = true;
						setNewStartFact(block, createFact(start));
					}

					Iterator<Edge> predEdgeIter = logicalPredecessorEdgeIterator(block);
					HashSet<BinopExpr> newStart = new HashSet<BinopExpr>();
					// get all predecessors' result fact and merge them with the
					// new start fact
					int preEdgeNumber = 0;
					while (predEdgeIter.hasNext()) {
						preEdgeNumber++;
						Edge edge = predEdgeIter.next();
						BasicBlock logicalPred = isForwards ? edge.getSource() : edge.getTarget();
						HashSet<BinopExpr> pred = null;
						// ////////////perhaps we need to distinguish different
						// handlers
						if (edge.getType() != EdgeType.HANDLED_EXCEPTION_EDGE) {
							if (this.blockOrder instanceof ReversePostOrder) {

								if (edge.getType() == EdgeType.IFCMP_EDGE_FALLTHROUGH
										|| edge.getType() == EdgeType.FALL_THROUGH_EDGE) {
									if (this.stmts.get(logicalPred.endStmt) instanceof IfStmt) {
										pred = createFact(this.getStartFact(logicalPred));
										IfStmt ifStmt = (IfStmt) this.stmts.get(logicalPred.endStmt);
										if (ifStmt.condition instanceof BinopExpr) {
											BinopExpr expr = (BinopExpr) ifStmt.condition;
											int rightOpType = BinopExpr.oppositeOpType(expr);
											if (rightOpType != -1) {
												BinopExpr rightExpr = new BinopExpr(expr.op1, expr.op2, rightOpType);
												pred.add(rightExpr);
											}
										}
									} else {
										pred = createFact(this.getResultFact(logicalPred));
									}

								} else if (edge.getType() == EdgeType.IFCMP_EDGE_JUMP) {
									pred = createFact(this.getResultFact(logicalPred));
								} else {
									pred = createFact(getResultFact(logicalPred));
								}

								if (preEdgeNumber == 1) {
									newStart = this.createFact(pred);
								} else {
									newStart = merge(newStart, pred);
								}
							}
						}
					}

					// if the generated startBack is different from start, need
					// to recompute
					if (!same(newStart, start)) {
						needToRecompute = true;
						start = newStart;
					}
				}
				// if really need to recompute, recompute the current block's
				// frames
				if (needToRecompute && !isEndBlock) {
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

					HashSet<BinopExpr> newResult = transferVertex(block);

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

			// examineResults();

			++numIterations;
			++timestamp;
		} while (change);

		return;
	}

	public static void main(String[] args) {

		AnalysisFactoryManager.initial();

		String projectPath = "D:/eclipseworkspace/CODA20110114/bin/";
		String subjectClassName = "edu.pku.cn.testcase.TestFileInputStreamOpenClose";
		
		Project project = new Project(projectPath);
		ClassNodeLoader loader = new ClassNodeLoader(projectPath);
		
		CodaProperties.isLibExpland = true;
		project.addLibPath("lib/");

		try {
			System.setOut(new PrintStream(new FileOutputStream("output.txt")));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		ClassNode cc = loader.loadClassNode(subjectClassName, 0);

		for (MethodNode method : cc.methods) {

			// if (method.name.contains("right")) {
			// method.getStmts();
			
			if (method.getOriginalFullName().equals("org.jfree.chart.servlet.ServletUtilities::sendTempFile(Ljava/io/File;Ljavax/servlet/http/HttpServletResponse;Ljava/lang/String;)")) {
				continue;
			}
			
			System.out.println(method.name + method.desc);
			CFG cfg = method.getCFG();

			PreconditionsDataflowAnalysis anlysis = new PreconditionsDataflowAnalysis(cfg);
			try {
				anlysis.execute();
				anlysis.examineResults();
			} catch (AnalyzerException e) {
				e.printStackTrace();
			}
		}
	}

	

	public void examineResults() {
		System.out.println("*****************************************");
		System.out.println(this.numIterations);
		Iterator<BasicBlock> i = this.cfg.blockIterator();
		while (i.hasNext()) {
			BasicBlock block = i.next();
			HashSet<BinopExpr> start = this.getStartFact(block);

			// result 有问题，但下一个block的start是没有问题的
			HashSet<BinopExpr> result = this.getResultFact(block);

			System.out.println("________________________________");

			for (BinopExpr expr : start)
				System.out.println(expr.toString());

			System.out.println("");

			for (int m = block.startStmt; m <= block.endStmt; m++) {
				System.out.println(m + ": " + block.stmts.get(m).toString());
			}

			System.out.println("");

			for (BinopExpr expr : result)
				System.out.println(expr.toString());

			System.out.println("");

			System.out.println("________________________________");
		}
	}
}

// end

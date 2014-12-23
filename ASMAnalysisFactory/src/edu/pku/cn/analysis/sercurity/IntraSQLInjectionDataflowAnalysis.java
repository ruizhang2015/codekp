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
package edu.pku.cn.analysis.sercurity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import edu.pku.cn.Project;
import edu.pku.cn.analysis.RefactoredBasicDataflowAnalysis;
import edu.pku.cn.analysis.vo.security.SQLInjectionInfo;
import edu.pku.cn.analysis.vo.security.SQLInjectionPath;
import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.classfile.ClassNodeLoader;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.graph.cfg.BasicBlock;
import edu.pku.cn.graph.cfg.CFG;
import edu.pku.cn.graph.cfg.Edge;
import edu.pku.cn.graph.visit.ReversePostOrder;
import edu.pku.cn.jir.AssignStmt;
import edu.pku.cn.jir.FieldRef;
import edu.pku.cn.jir.InvokeExpr;
import edu.pku.cn.jir.InvokeStmt;
import edu.pku.cn.jir.JIRValue;
import edu.pku.cn.jir.LineStmt;
import edu.pku.cn.jir.LocalRef;
import edu.pku.cn.jir.ParamRef;
import edu.pku.cn.jir.Ref;
import edu.pku.cn.jir.Stmt;
import edu.pku.cn.jir.TempRef;
import edu.pku.cn.util.AnalysisFactoryManager;
import edu.pku.cn.util.CodaProperties;

public class IntraSQLInjectionDataflowAnalysis extends
		RefactoredBasicDataflowAnalysis<HashMap<JIRValue, SQLInjectionInfo>> {

	public boolean Debug = false;
	public boolean PARAMANDTHIS = true;

	int currentLine;
	String desc;
	boolean isInConstructor = false;

	private InsnList insns;
	private List<Stmt> stmts;
	private CFG cfg;

	// HashMap<JIRValue, IntraPointsToInfo> pointsToInfos = new
	// HashMap<JIRValue, IntraPointsToInfo>();

	public IntraSQLInjectionDataflowAnalysis(CFG cfg) {
		this.blockOrder = new ReversePostOrder(cfg);
		this.isForwards = true;
		this.cfg = cfg;
		this.stmts = cfg.getJIRStmt();
		this.insns = cfg.getMethod().instructions;
	}

	@Override
	public HashMap<JIRValue, SQLInjectionInfo> createFact() {
		HashMap<JIRValue, SQLInjectionInfo> fact = new HashMap<JIRValue, SQLInjectionInfo>();
		return fact;
	}

	@Override
	public HashMap<JIRValue, SQLInjectionInfo> createFact(HashMap<JIRValue, SQLInjectionInfo> fact) {
		HashMap<JIRValue, SQLInjectionInfo> newFact = new HashMap<JIRValue, SQLInjectionInfo>();
		for (JIRValue jirValue : fact.keySet()) {
			newFact.put(jirValue, fact.get(jirValue).clone());
		}
		return newFact;
	}

	// @Override
	// public HashSet<String> getNewStartFact(BasicBlock block) {
	// return facts[block.startStmt];
	// }

	@Override
	public void initEntryFact() {
		HashMap<JIRValue, SQLInjectionInfo> rdFact = createFact();
		startFactMap.put(blockOrder.getEntry(), rdFact);
	}

	@Override
	public boolean isEndBlock(BasicBlock block) {
		return block.getStartInc() >= insns.size();
	}

	@Override
	public HashMap<JIRValue, SQLInjectionInfo> merge(HashMap<JIRValue, SQLInjectionInfo> start,
			HashMap<JIRValue, SQLInjectionInfo> pred) {
		HashMap<JIRValue, SQLInjectionInfo> result = this.createFact(start);
		for (JIRValue jirValue : pred.keySet()) {
			SQLInjectionInfo info = pred.get(jirValue);
			if (result.get(jirValue) != null) {
				result.put(jirValue, result.get(jirValue).mergeWith(info));
			} else
				result.put(jirValue, info.clone());
		}
		return result;
	}

	@Override
	public boolean same(HashMap<JIRValue, SQLInjectionInfo> fact1, HashMap<JIRValue, SQLInjectionInfo> fact2) {
		if (fact1.keySet().size() != fact2.keySet().size())
			return false;
		for (JIRValue jirValue : fact1.keySet()) {
			if (fact2.get(jirValue) == null) {
				return false;
			}
			if (!fact1.get(jirValue).equals(fact2.get(jirValue))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public HashMap<JIRValue, SQLInjectionInfo> transferVertex(BasicBlock block) {
		HashMap<JIRValue, SQLInjectionInfo> fact = this.createFact(getStartFact(block));
		if (fact == null)
			fact = new HashMap<JIRValue, SQLInjectionInfo>();
		// 有些可能一条语句都没有
		if (this.stmts.size() != 0) {
			for (int i = block.startStmt; i <= block.endStmt; i++) {
				Stmt stmt = this.stmts.get(i);
				if (stmt instanceof LineStmt) {
					transferLineStmt(stmt);
				} else if (stmt instanceof InvokeStmt) {
					transferInvokeStmt(fact, stmt);
				} else if (stmt instanceof AssignStmt) {
					transferAssignStmt(fact, stmt);
				}
			}
		}
		// printTransferResult();
		return fact;

	}

	private void transferLineStmt(Stmt stmt) {
		LineStmt ls = (LineStmt) stmt;
		currentLine = ls.line;
	}

	private void transferAssignStmt(HashMap<JIRValue, SQLInjectionInfo> fact, Stmt stmt) {
		// 处理赋值语句
		AssignStmt as = (AssignStmt) stmt;
		// && !(as.left instanceof ParamRef)&&
		// !("this".equalsIgnoreCase(((LocalRef)
		// as.left).nodeRef.name))
		// 处理左边是本地变量的情况
		if (as.left instanceof LocalRef) {
			trasferAssignStmtLocalRef(fact, as);
			// 如果左边是临时变量
		} else if (as.left instanceof TempRef) {
			transferAssignStmtTempRef(fact, as);
		}else{
			addFact(fact, as, false);
		}
	}

	private void transferAssignStmtTempRef(HashMap<JIRValue, SQLInjectionInfo> fact, AssignStmt as) {
		TempRef tempRef = (TempRef) as.left;
		// 处理各种右边情况
		// 如果右边是表达式调用
		if (as.right instanceof Ref) {
			if (as.right instanceof LocalRef && !(as.right instanceof ParamRef)
					&& !("this".equalsIgnoreCase(((LocalRef) as.right).nodeRef.name)) && fact.get(as.right) != null
					&& fact.get(as.right).isTaint()) {
				// e.g.: a = b;
				addFactAll(fact, as, true);
			} else if (this.PARAMANDTHIS == true
					&& ((as.right instanceof LocalRef && (as.right instanceof ParamRef || "this"
							.equalsIgnoreCase(((LocalRef) as.right).nodeRef.name))) || as.right instanceof FieldRef)
					&& isTaint(fact, as.right)) {
				addFactAll(fact, as, true);
			} else {
				addFact(fact, as, false);
			}
		} else if (as.right instanceof InvokeExpr) {
			InvokeExpr invoke = (InvokeExpr) as.right;
			if (invoke.isStatic()) {
				if ("valueOf".equalsIgnoreCase(invoke.node.name)
						&& "(Ljava/lang/Object;)Ljava/lang/String;".equalsIgnoreCase(invoke.node.desc)
						&& "java/lang/String".equalsIgnoreCase(invoke.node.owner)) {
					handleParams(fact, as, invoke);
				} else if ("()Ljava/lang/String;".equalsIgnoreCase(invoke.node.desc)) {
					handleParams(fact, as, invoke);
				} else {
					addFact(fact, as, false);
				}
			} else {
				// e.g.: temp$0.
				JIRValue invoker = invoke.invoker;
				// 真正引入SQL注入问题的字符串拼接,isTaint()用来判断拼接的参数是否污染了，可以在这里写污染逻辑
				// 判断temp$2=InvokeVirtual
				// temp$1.append(Ljava/lang/String;)Ljava/lang/StringBuilder;(username)
				// [18,19]情况
				if (invoker != null && invoker instanceof TempRef && "append".equalsIgnoreCase(invoke.node.name)
						&& "java/lang/StringBuilder".equalsIgnoreCase(invoke.node.owner)
						&& "(Ljava/lang/String;)Ljava/lang/StringBuilder;".equalsIgnoreCase(invoke.node.desc)) {
					if (fact.get(invoker) != null && fact.get(invoker).isTaint()) {
						addFactAll(fact, as, invoker, true);
					} else
						handleParams(fact, as, invoke);
				} else if (invoker != null && invoker instanceof LocalRef
						&& "toString".equalsIgnoreCase(invoke.node.name)
						&& "java/lang/StringBuffer".equalsIgnoreCase(invoke.node.owner)
						&& "()Ljava/lang/String;".equalsIgnoreCase(invoke.node.desc)) {
					if (fact.get(invoker) != null && fact.get(invoker).isTaint()) {
						addFactAll(fact, as, invoker, true);
					} else
						handleParams(fact, as, invoke);
				} else {
					addFact(fact, as, false);
				}
			}
		} else {
			addFact(fact, as, false);
		}
	}

	private void trasferAssignStmtLocalRef(HashMap<JIRValue, SQLInjectionInfo> fact, AssignStmt as) {
		// e.g. a = x; a = x.f;
		// 左边是本地变量，右边各种情况
		// 右边是引用的情况
		if (as.right instanceof Ref) {
			if (as.right instanceof LocalRef && !(as.right instanceof ParamRef)
					&& !("this".equalsIgnoreCase(((LocalRef) as.right).nodeRef.name))
					&& fact.get(as.right).isTaint()) {
				// e.g.: a = b;
				addFactAll(fact, as, true);
			} else if (this.PARAMANDTHIS == true
					&& ((as.right instanceof LocalRef && (as.right instanceof ParamRef || "this"
							.equalsIgnoreCase(((LocalRef) as.right).nodeRef.name))) || as.right instanceof FieldRef)
					&& isTaint(fact, as.right)) {
				addFactAll(fact, as, true);
			} else {
				addFact(fact, as, false);
			}
			// 处理右边是调用的情况
		} else if (as.right instanceof InvokeExpr) {
			// e.g.: a = b.f();
			InvokeExpr invoke = (InvokeExpr) as.right;
			// 如果是静态方法调用则直接认为是污染的（这里可能产生误报）
			if (invoke.isStatic()) {
				// e.g.:username=InvokeStatic
				// getP()Ljava/lang/String;() [6,6]
				// 静态方法，只需考虑参数情况
				if ("()Ljava/lang/String;".equalsIgnoreCase(invoke.node.desc)) {
					handleParams(fact, as, invoke);
				} else {
					addFact(fact, as, false);
				}
			} else {
				// 只关注所有返回值为String类型的调用，才有可能产生SQL注入危险
				if ("()Ljava/lang/String;".equalsIgnoreCase(invoke.node.desc)) {
					// e.g.: temp$0.
					JIRValue invoker = invoke.invoker;
					// 如果临时变量污染了，则设置为true
					if (fact.get(invoker) != null && fact.get(invoker).isTaint()) {
						addFactAll(fact, as, invoker, true);
						// 如果没有被污染，设置为false
					} else {
						addFact(fact, as, false);
					}
					// 如果函数调用返回值不是String则不会产生SQL注入危险
				} else {
					handleParams(fact, as, invoke);
				}
			}
			// 右边如果是其他情况，全部设置false（可能误报）
		} else {
			addFact(fact, as, false);
		}
	}

	private void transferInvokeStmt(HashMap<JIRValue, SQLInjectionInfo> fact, Stmt stmt) {
		// e.g.:InvokeInterface
		// temp$5.<init>(Ljava/lang/String;)V(temp$6) [31,31]
		InvokeStmt invokeStmt = (InvokeStmt) stmt;
		InvokeExpr invoke = (InvokeExpr) invokeStmt.invoke;
		// e.g.: temp$5.
		JIRValue invoker = invoke.invoker;
		if (invoker != null
				&& "<init>".equalsIgnoreCase(invoke.node.name)
				&& ("java/lang/StringBuilder".equalsIgnoreCase(invoke.node.owner) || "java/lang/StringBuffer"
						.equalsIgnoreCase(invoke.node.owner))) {
			if (fact.get(invoker) != null && invoke.params.size() > 0) {
				JIRValue jirValue = invoke.params.get(0);
				if ((jirValue instanceof LocalRef || jirValue instanceof TempRef)
						&& (fact.get(jirValue) != null && isTaint(fact, jirValue))) {
					addFactInvokeStmt(fact, invoker, jirValue, true);
				}
			}
		}
	}

	// 处理调用参数
	private void handleParams(HashMap<JIRValue, SQLInjectionInfo> fact, AssignStmt as, InvokeExpr invoke) {
		if (invoke.params.size() > 0) {
			JIRValue jirValue = invoke.params.get(0);
			if (jirValue instanceof LocalRef && !(jirValue instanceof ParamRef)
					&& !("this".equalsIgnoreCase(((LocalRef) jirValue).nodeRef.name))) {
				if (isTaint(fact, jirValue)) {
					addFactAll(fact, as, jirValue, true);
				} else {
					addFact(fact, as, false);
				}
			} else if (this.PARAMANDTHIS == true
					&& ((jirValue instanceof LocalRef && (jirValue instanceof ParamRef || "this"
							.equalsIgnoreCase(((LocalRef) jirValue).nodeRef.name))) || jirValue instanceof FieldRef)
					&& isTaint(fact, jirValue)) {
				addFactAll(fact, as, jirValue, true);
			} else if (jirValue instanceof TempRef) {
				if (fact.get(jirValue).isTaint()) {
					addFactAll(fact, as, jirValue, true);
				} else {
					addFact(fact, as, false);
				}
			} else {
				addFact(fact, as, false);
			}
		} else {
			addFact(fact, as, false);
		}
	}

	private boolean isTaint(HashMap<JIRValue, SQLInjectionInfo> fact, JIRValue jirValue) {
		return true;
	}

	// 以下是处理InvokeStmt---------------------------

	private void addFactInvokeStmt(HashMap<JIRValue, SQLInjectionInfo> fact, JIRValue invoker, JIRValue jirValue,
			boolean isTaint) {
		SQLInjectionPath sqlInjectionPath = new SQLInjectionPath();
		sqlInjectionPath.setCurrentLine(currentLine);
		sqlInjectionPath.setValue(jirValue);
		List<SQLInjectionPath> path = new ArrayList<SQLInjectionPath>();
		path.add(sqlInjectionPath);
		path.addAll(fact.get(jirValue).getPath());
		fact.get(invoker).setPath(path);
		fact.get(invoker).setTaint(isTaint);
	}

	// 以上是处理InvokeStmt------------------------------

	// 以下为处理赋值语句--------------------------------

	private void addFactAll(HashMap<JIRValue, SQLInjectionInfo> fact, AssignStmt as, boolean isTaint) {
		SQLInjectionInfo sqlInjectionInfo = new SQLInjectionInfo();
		SQLInjectionPath sqlInjectionPath = new SQLInjectionPath();
		sqlInjectionPath.setCurrentLine(currentLine);
		sqlInjectionPath.setValue(as.right);
		List<SQLInjectionPath> path = new ArrayList<SQLInjectionPath>();
		path.add(sqlInjectionPath);
		if (fact.get(as.right) != null)
			path.addAll(fact.get(as.right).getPath());
		sqlInjectionInfo.setPath(path);
		sqlInjectionInfo.setTaint(isTaint);
		fact.put(as.left, sqlInjectionInfo);
	}

	// 增加一条fact，不仅要加入本条语句，还得加入相关联的变量的路径
	private void addFactAll(HashMap<JIRValue, SQLInjectionInfo> fact, AssignStmt as, JIRValue jirValue, boolean isTaint) {
		SQLInjectionInfo sqlInjectionInfo = new SQLInjectionInfo();
		SQLInjectionPath sqlInjectionPath = new SQLInjectionPath();
		sqlInjectionPath.setCurrentLine(currentLine);
		sqlInjectionPath.setValue(as.right);
		List<SQLInjectionPath> path = new ArrayList<SQLInjectionPath>();
		path.add(sqlInjectionPath);
		if (fact.get(jirValue) != null)
			path.addAll(fact.get(jirValue).getPath());
		sqlInjectionInfo.setPath(path);
		sqlInjectionInfo.setTaint(isTaint);
		fact.put(as.left, sqlInjectionInfo);
	}

	// 加入一条fact，该fact只和本条语句相关
	private void addFact(HashMap<JIRValue, SQLInjectionInfo> fact, AssignStmt as, boolean isTaint) {
		SQLInjectionInfo sqlInjectionInfo = new SQLInjectionInfo();
		SQLInjectionPath sqlInjectionPath = new SQLInjectionPath();
		sqlInjectionPath.setCurrentLine(currentLine);
		sqlInjectionPath.setValue(as.right);
		List<SQLInjectionPath> path = new ArrayList<SQLInjectionPath>();
		path.add(sqlInjectionPath);
		sqlInjectionInfo.setPath(path);
		sqlInjectionInfo.setTaint(isTaint);
		fact.put(as.left, sqlInjectionInfo);
	}

	// 以上为处理赋值语句----------------------------

	private void printTransferResult(HashMap<JIRValue, SQLInjectionInfo> fact) {
		for (JIRValue jirValue : fact.keySet()) {
			System.out.println(jirValue.toString() + "-->" + fact.get(jirValue).toString());
		}
	}

	private void initEntryBlock() {
		this.initEntryFact();
		HashMap<JIRValue, SQLInjectionInfo> result = this.transferVertex(logicalEntryBlock());
		this.setResultFact(logicalEntryBlock(), result);
	}

	protected BasicBlock logicalEntryBlock() {
		if (blockOrder instanceof ReversePostOrder)
			return cfg.getRoot();
		else
			return cfg.getExit();
	}

	private static final int MAX_ITERS = 2;
	private int numIterations = 0;
	private final boolean DEBUG = false;

	protected String getFullyQualifiedMethodName() {
		return cfg.getMethod().name;
	}

	private Iterator<Edge> logicalPredecessorEdgeIterator(BasicBlock block) {
		return isForwards ? block.inEdgeIterator() : block.outEdgeIterator();
	}

	private Iterator<Edge> logicalSuccessorEdgeIterator(BasicBlock block) {
		return isForwards ? block.outEdgeIterator() : block.inEdgeIterator();
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
				break;
			}
			startIteration();
			Iterator<BasicBlock> i = blockOrder.blockIterator();
			while (i.hasNext()) {
				BasicBlock block = i.next();
				HashMap<JIRValue, SQLInjectionInfo> start = getStartFact(block);
				HashMap<JIRValue, SQLInjectionInfo> result = getResultFact(block);
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
					HashMap<JIRValue, SQLInjectionInfo> newStart = new HashMap<JIRValue, SQLInjectionInfo>();
					// get all predecessors' result fact and merge them with the
					// new start fact
					int preEdgeNumber = 0;
					while (predEdgeIter.hasNext()) {
						preEdgeNumber++;
						Edge edge = predEdgeIter.next();
						BasicBlock logicalPred = isForwards ? edge.getSource() : edge.getTarget();
						HashMap<JIRValue, SQLInjectionInfo> pred = null;
						// ////////////perhaps we need to distinguish different
						// handlers

						if (this.blockOrder instanceof ReversePostOrder) {
							pred = createFact(getResultFact(logicalPred));
							if (preEdgeNumber == 1) {
								newStart = this.createFact(pred);
							} else {
								newStart = merge(newStart, pred);
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
					setNewStartFact(block, createFact(start));// set
					if (DEBUG) {
						if (Thread.activeCount() > 1) {
							Map map = Thread.getAllStackTraces();
							System.out.println("The thread number before calling transferVertex");
							for (Object key : map.keySet()) {
								System.out.println("key = " + key + "    value = " + map.get(key).toString());
							}
						}
					}
					HashMap<JIRValue, SQLInjectionInfo> newResult = transferVertex(block);
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

	public void examineResults() {
		System.out.println("*****************************************");
		System.out.println(this.cfg.getMethod().name);
		System.out.println(this.numIterations);
		Iterator<BasicBlock> i = this.cfg.blockIterator();
		while (i.hasNext()) {
			BasicBlock block = i.next();
			HashMap<JIRValue, SQLInjectionInfo> start = this.getStartFact(block);
			// result 有问题，但下一个block的start是没有问题的
			HashMap<JIRValue, SQLInjectionInfo> result = this.getResultFact(block);
			System.out.println("________________________________");
			this.printTransferResult(start);
			System.out.println("");
			for (int m = block.startStmt; m <= block.endStmt; m++) {
				System.out.println(m + ": " + block.stmts.get(m).toString());
			}
			System.out.println("");
			this.printTransferResult(result);
			System.out.println("");
			System.out.println("________________________________");
		}
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		AnalysisFactoryManager.initial();

		Project project = new Project("D:/workspace/java/myeclipse8/webgoat/target/classes/");
		CodaProperties.isLibExpland = true;
		project.addLibPath("D:/workspace/java/myeclipse8/webgoat/lib/");

		/*
		 * ClassNodeLoader loader = new ClassNodeLoader("D:/Test/bin/");
		 * ClassNode cc =
		 * loader.loadClassNode("edu.pku.cn.testcase.TestSQLInjection", 0);
		 */

		ClassNodeLoader loader = new ClassNodeLoader("D:/workspace/java/myeclipse8/webgoat/target/classes/");
		ClassNode cc = loader.loadClassNode("org.owasp.webgoat.lessons.SqlAddData", 0);

		/*
		 * ClassNodeLoader loader = new ClassNodeLoader("bin/"); ClassNode cc =
		 * loader.loadClassNode("edu.pku.cn.testcase.TestTryCatch", 0);
		 */
		for (MethodNode method : cc.methods) {
			method.getStmts();
			CFG cfg = method.getCFG();
			IntraSQLInjectionDataflowAnalysis anlysis = new IntraSQLInjectionDataflowAnalysis(cfg);
			try {
				anlysis.execute();
				anlysis.examineResults();
			} catch (AnalyzerException e) {
				e.printStackTrace();
			}
		}
	}
}

// end

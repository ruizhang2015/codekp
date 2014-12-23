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

public class ExceptionPrecondictionDataflowAnalysis extends RefactoredBasicDataflowAnalysis<HashSet<BinopExpr>> {

	public boolean Debug = false;

	int currentLine;
	String desc;
	boolean isInConstructor = false;

	private InsnList insns;
	private List<Stmt> stmts;
	private CFG cfg;

	HashMap<JIRValue, RefVarInfo> pointsToInfos = new HashMap<JIRValue, RefVarInfo>();
	HashSet<BasicBlock> exceptionBlocks = new HashSet<BasicBlock>();
	HashMap<BasicBlock, HashSet<String>> callees = new HashMap<BasicBlock, HashSet<String>>();
	HashSet<BasicBlock> returnBlocks = new HashSet<BasicBlock>();

	public ExceptionPrecondictionDataflowAnalysis(CFG cfg) {
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
		boolean existSameObject = false;
		for (BinopExpr expr : pred) {
			if (!start.contains(expr)) {
				for (BinopExpr startExpr : start) {
					if (startExpr.op1.equals(expr.op1)) {
						existSameObject = true;
						break;
					}
				}
				if (existSameObject == false) {
					result.add(expr);
				} else {
					existSameObject = false;
				}
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
				} else if (ifStmt.condition instanceof InvokeExpr) {
					InvokeExpr invokeExpr = (InvokeExpr) ifStmt.condition;
					HashSet<String> blockCallees = this.callees.get(block);
					if (blockCallees == null) {
						blockCallees = new HashSet<String>();
					}
					blockCallees.add(invokeExpr.getMethodName() + invokeExpr.getMethodDesc());
					this.callees.put(block, blockCallees);
				}
			} else if (stmt instanceof AssignStmt) {
				AssignStmt assignStmt = (AssignStmt) stmt;
				JIRValue leftValue = assignStmt.left;
				JIRValue rightValue = assignStmt.right;
				analyzeJIRValue(leftValue, fact, block);
				analyzeJIRValue(rightValue, fact, block);
			} else if (stmt instanceof ReturnStmt) {
				ReturnStmt returnStmt = (ReturnStmt) stmt;
				analyzeJIRValue(returnStmt.value, fact, block);
				this.returnBlocks.add(block);
			} else if (stmt instanceof InvokeStmt) {
				InvokeStmt invokeStmt = (InvokeStmt) stmt;
				analyzeJIRValue(invokeStmt.invoke, fact, block);
			} else if (stmt instanceof ThrowStmt) {
				this.exceptionBlocks.add(block);
			}
		}

		// printTransferResult();
		return fact;

	}

	private void analyzeJIRValue(JIRValue value, HashSet<BinopExpr> fact, BasicBlock block) {

		if (value instanceof Ref) {
			// e.g.: a = b; a = b.c; a = @CaughException; a
			// ="string";
			// a = b[i];

			// e.g.: e=@CaughException
			if (value instanceof FieldRef) {
				if (!((FieldRef) value).base.toString().equals("this") ){
					BinopExpr rightExpr = new BinopExpr(((FieldRef) value).base, new Null(), BinopExpr.EQUAL);
					fact.add(rightExpr);
					this.exceptionBlocks.add(block);
				}
			}
			
		} else if (value instanceof Expr) {
			if (value instanceof InvokeExpr) {
				// a = b.f();
				InvokeExpr invokeExpr = (InvokeExpr) value;

				BinopExpr rightExpr = new BinopExpr(invokeExpr.invoker, new Null(), BinopExpr.EQUAL);
				fact.add(rightExpr);
				this.exceptionBlocks.add(block);

				if (invokeExpr.invoker instanceof FieldRef) {
					rightExpr = new BinopExpr(((FieldRef) invokeExpr.invoker).base, new Null(), BinopExpr.EQUAL);
					fact.add(rightExpr);
					this.exceptionBlocks.add(block);
				}

			} else if (value instanceof CastExpr) {
				// e.g.: a = (A)b;
				// a = (A)b.f(); a = (A)c + b;
				CastExpr castExpr = (CastExpr) value;
				analyzeJIRValue(castExpr.value, fact, block);

			} else if (value instanceof BinopExpr) {
				BinopExpr binopExpr = (BinopExpr) value;
				analyzeJIRValue(binopExpr.op1, fact, block);
				analyzeJIRValue(binopExpr.op2, fact, block);
			}
		}
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

	private static final int MAX_ITERS = 10;
	private int numIterations = 0;
	private final boolean DEBUG = false;

	protected String getFullyQualifiedMethodName() {
		return cfg.getMethod().name;
	}

	protected Iterator<Edge> logicalPredecessorEdgeIterator(BasicBlock block) {
		return isForwards ? block.inEdgeIterator() : block.outEdgeIterator();
	}

	protected Iterator<Edge> logicalSuccessorEdgeIterator(BasicBlock block) {
		return isForwards ? block.outEdgeIterator() : block.inEdgeIterator();
	}

	/**
	 * Comments added by WuQian.
	 * The procedure records:
	 * 1.blocks that will throw exceptions.(in exceptionBlocks)
	 * 2.blocks that will return bino-exprs.(in returnBlocks, not sure)
	 * 3.path-condition for each blocks.(in the resultFact of each block)
	 * @see edu.pku.cn.analysis.RefactoredBasicDataflowAnalysis#execute()
	 */
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
									this.callees.put(block, this.callees.get(logicalPred));
								} else {
									newStart = merge(newStart, pred);
									if (this.callees.get(block) != null) {
										this.callees.get(block).addAll(this.callees.get(logicalPred));
									} else {
										this.callees.put(block, this.callees.get(logicalPred));
									}
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
		
		String[] projects = new String[5];
		projects[0] = "D:\\Study\\@PKU\\Research\\SelfPapers\\20100517 约束验证\\SubjectProjects\\all\\";
		//projects[0] = "bin/"; 
		projects[1] = "D:\\Study\\@PKU\\Research\\SelfPapers\\20100517 约束验证\\SubjectProjects\\group2\\";
		projects[2] = "D:\\Study\\@PKU\\Research\\SelfPapers\\20100517 约束验证\\SubjectProjects\\group3\\";
		projects[3] = "D:\\Study\\@PKU\\Research\\SelfPapers\\20100517 约束验证\\SubjectProjects\\group4\\";
		projects[4] = "D:\\Study\\@PKU\\Research\\SelfPapers\\20100517 约束验证\\SubjectProjects\\group5\\";
		
		String proName="";
		Project project = new Project(projects[0]);
		ClassNodeLoader loader = new ClassNodeLoader(projects[0]);
		
		//Project project = new Project("bin/");
		//ClassNodeLoader loader = new ClassNodeLoader("D:\\Study\\@PKU\\Research\\SelfPapers\\20100517 约束验证\\SubjectProjects\\all\\");
		
		CodaProperties.isLibExpland = true;
		project.addLibPath("lib/");

//		try {
//			System.setOut(new PrintStream(new FileOutputStream("output.txt")));
//		} catch (FileNotFoundException e1) {
//			e1.printStackTrace();
//		}
		
		//String className = "edu.pku.cn.testcase.TestPointsToInfo";
//		String className = "edu.pku.cn.testcase.TestPointsToInfo";
		
//		ClassNode cc = null;
		//cc = loader.loadClassNode(className, 0);
		//analyzeMethodRelations(className, loader, cc);
		
//		 try {
//		 System.setOut(new PrintStream(new FileOutputStream("output.txt")));
//		 } catch (FileNotFoundException e1) {
//		 e1.printStackTrace();
//		 }
		SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日   HH:mm:ss     ");      
		Date beginDate =  new   Date(System.currentTimeMillis());
		analyzeMethodRelationsForProject(proName, project, loader);
		Date endDate =  new   Date(System.currentTimeMillis());
		System.out.println("beginDate:" + formatter.format(beginDate));
		System.out.println("endDate:" + formatter.format(endDate));
		System.out.println(endDate.getTime() - beginDate.getTime());
		// className = "edu.pku.cn.testcase.PrintWriter";
	}

	public static void analyzeMethodRelationsForProject(String proName, Project project, ClassNodeLoader loader) {
		
		String className="";
		ClassNode cc=null;
		
		
		ResultSet resultSet = DBUtil.executeQuery("select distinct classNm from ClassInfo where projectName like %"+proName+"%");
		try {
			while (resultSet.next()) {
				className = resultSet.getString("classNm");
				cc = loader.loadClassNode(className, 0);

				if (cc != null) {
					System.out.println("___________________________________");
					System.out.println("analyze " + cc.name + " (isInterface==" + cc.isInterface() + ")");
					if (cc.isInterface() == false) {
						analyzeMethodRelations(proName, className, loader, cc);
					} else {
		//TODO:needed to change:for interfaces do what?

						// 遍历项目中的所有代码，寻找哪些代码是实现该接口的类，如果实现了该接口，才对其进行分析
//						int size = project.getPackageResource().getClassNodeList().size();
//						System.out.println(size);
//						int i = 0;
						//analysePackageResource(project.getPackageResource(), className, loader, cc, i);
					}
					System.out.println("___________________________________");
				} else
					System.out.println("无法load " + className);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	private static void analysePackageResource(PackageResource packageResource, String className,
//			ClassNodeLoader loader, ClassNode cc, int i) {
//		for (ClassNode cn : packageResource.getClassNodeList()) {
//			i++;
//			System.out.println("analyze: " + cn.name + " (" + i + ")");
//			if (cn.getInterfaces(loader) != null) {
//				if (cn.isInterface() == false && cn.getInterfaces(loader).contains(cc)) {
//					System.out.println("analyze " + cn.name + " (implements " + cc.name + ")");
//					analyzeMethodRelations(proName, className, loader, cc);
//				}
//			}
//		}
//
//		for (PackageResource pr : packageResource.getPackageList()) {
//			analysePackageResource(pr, className, loader, cc, i);
//		}
//	}

	private static void analyzeMethodRelations(String proName, String className, ClassNodeLoader loader, ClassNode cc) {
		String bmAcc = "public";
		String amAcc = "public";

		InterValueDataflowAnalysis.loader = loader;

		int methodsNumber = 0;
		for (MethodNode method : cc.methods) {
			if (method.isPublic() == true && method.isConstructor() == false) {
				methodsNumber++;
			}
		}

		// 保存接口信息
//		for (ClassNode interfaceNode : cc.getInterfaces(loader)) {
//			DBUtil.saveMethodRelationInterface(className, interfaceNode.name);
//		}

		HashMap<String, InterValueDataflowAnalysis> ptCache = new HashMap<String, InterValueDataflowAnalysis>();
		HashMap<String, ExceptionPrecondictionDataflowAnalysis> expCache = new HashMap<String, ExceptionPrecondictionDataflowAnalysis>();
		HashMap<String, HashSet<String>> calleesCache = new HashMap<String, HashSet<String>>();
		
		for (MethodNode method : cc.methods) {
			try {

				CFG cfg = method.getCFG();

				if (method.isPrivate())
					amAcc = "private";
				else if (method.isPublic())
					amAcc = "public";
				else
					amAcc = "protected";

				// IntraPointsToDataflowAnalysis2 pointsToAnalysis = new
				// IntraPointsToDataflowAnalysis2(cfg);
				
				System.out.println("——》开始分析方法" + method.name + method.desc);
				InterValueDataflowAnalysis pointsToAnalysis;
				ExceptionPrecondictionDataflowAnalysis exceptionPreAnalysis ;
				if (method.name.contains("clinit")){
					continue;
				}
				
				try {
					if(ptCache.get(method.getFullName()) != null) {
						pointsToAnalysis = ptCache.get(method.getFullName());
					} else {
						InterValueDataflowAnalysis.callees.clear();
						pointsToAnalysis = new InterValueDataflowAnalysis(cfg);
						pointsToAnalysis.execute();
						ptCache.put(method.getFullName(), pointsToAnalysis);
						calleesCache.put(method.getFullName(), (HashSet<String>)InterValueDataflowAnalysis.callees.clone());
					}
					
					// anlysis.examineResults();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}
 
				try {
					
					if(expCache.get(method.getFullName()) != null) {
						exceptionPreAnalysis = expCache.get(method.getFullName());
					} else {
						exceptionPreAnalysis = new ExceptionPrecondictionDataflowAnalysis(cfg);
						exceptionPreAnalysis.execute();
						expCache.put(method.getFullName(), exceptionPreAnalysis);
					}
					
					// extract critical information: the precondition in which
					// the
					// exception is thrown
					HashSet<BinopExpr> allPreConditions = new HashSet<BinopExpr>();
					HashSet<String> allCalleeNames = new HashSet<String>();

					System.out.println("******************************");
					System.out.println("——》开始分析方法" + method.name + method.desc);

					// for(BasicBlock block : analysis.returnBlocks){
					// System.out.println("**开始分析返回模块的条件为：block size:" +
					// (block.endStmt - block.startStmt + 1));
					// //System.out.println(block.toString());
					// System.out.println("");
					// HashSet<BinopExpr> preCondictions =
					// analysis.getStartFact(block);
					// for (BinopExpr expr : preCondictions)
					// System.out.println(expr.toString());
					// System.out.println("");
					// }

					for (BasicBlock block : exceptionPreAnalysis.exceptionBlocks) {

						HashSet<BinopExpr> preCondictions = exceptionPreAnalysis.getResultFact(block);
						System.out.println("**开始分析异常模块：" + block.startStmt + " to " + block.endStmt);

						// for (int m = block.startStmt; m <= block.endStmt;
						// m++) {
						// System.out.println(m + ": " +
						// block.stmts.get(m).toString());
						// }
						//

						// System.out.println("**该异常模块的callee为：");
						// if (analysis.callees.get(block) != null) {
						// for (String calleeName : analysis.callees.get(block))
						// {
						// System.out.println(calleeName);
						// allCalleeNames.add(calleeName);
						// }
						// }

						System.out.println("");

						System.out.println("**异常条件为：");
						for (BinopExpr expr : preCondictions)
							System.out.println(expr.toString());
						System.out.println("");

						System.out.println("**经过指向分析结果过滤后:");
						HashMap<InterJIRValue, InterVarInfo> pointsToInfos = pointsToAnalysis.getStartFact(block);
						// System.out.println(pointsToInfos.toString());

						filterConditions(allPreConditions, preCondictions, pointsToInfos);

						System.out.println("");

					}

					System.out.println("*所有异常条件为：");
					for (BinopExpr expr : allPreConditions)
						System.out.println(expr.toString());
					System.out.println("");

					if (allPreConditions.size() > 0 || allCalleeNames.size() > 0) {

						// to check which other methods meet the preconditions
						for (MethodNode otherMethod : cc.methods) {
							if (!method.equals(otherMethod)) {
								try {

									// to execute pointsToAnalysis
									// IntraPointsToDataflowAnalysis2
									// pointsToAnalysisForOtherMethod = new
									// IntraPointsToDataflowAnalysis2(
									// otherMethod.getCFG());
									if (otherMethod.isPrivate())
										bmAcc = "private";
									else if (otherMethod.isPublic())
										bmAcc = "public";
									else
										bmAcc = "protected";

									System.out.println("——————————————————————————————————————————");
									System.out.println("开始分析其他函数（" + otherMethod.name + otherMethod.desc + ") ：");
									
									InterValueDataflowAnalysis pointsToAnalysisForOtherMethod = null;
									HashSet<String> callees = new HashSet<String> ();
									
									if(ptCache.get(otherMethod.getFullName()) != null) {
										pointsToAnalysisForOtherMethod = ptCache.get(otherMethod.getFullName());
										callees = calleesCache.get(otherMethod.getFullName());
									} else {
										InterValueDataflowAnalysis.callees.clear();
										pointsToAnalysisForOtherMethod = new InterValueDataflowAnalysis(otherMethod.getCFG());
										pointsToAnalysisForOtherMethod.execute();
										ptCache.put(otherMethod.getFullName(), pointsToAnalysisForOtherMethod);
										calleesCache.put(otherMethod.getFullName(), (HashSet<String>)InterValueDataflowAnalysis.callees.clone());
									}
									
									ExceptionPrecondictionDataflowAnalysis exceptionAnalysisForOtherMethod ;
									
									if(expCache.get(otherMethod.getFullName()) != null) {
										exceptionAnalysisForOtherMethod = expCache.get(otherMethod.getFullName());
									} else {
										exceptionAnalysisForOtherMethod = new ExceptionPrecondictionDataflowAnalysis(otherMethod.getCFG());
										exceptionAnalysisForOtherMethod.execute();
										expCache.put(otherMethod.getFullName(), exceptionAnalysisForOtherMethod);
									}
									
									System.out.println("该函数的返回判断条件：");

									HashSet<BinopExpr> allReturnConditions = new HashSet<BinopExpr>();
									for (BasicBlock block : exceptionAnalysisForOtherMethod.returnBlocks) {
										System.out.println("block size:" + (block.endStmt - block.startStmt + 1));
										if ((block.endStmt - block.startStmt + 1) <= 2) {
											HashSet<BinopExpr> returnConditions = exceptionAnalysisForOtherMethod
													.getStartFact(block);
											filterConditions(allReturnConditions, returnConditions,
													pointsToAnalysisForOtherMethod.getStartFact(block));
										}
									}

									try {

										// to check whether it meets the
										// preconditions
										// using its points-to-information
										HashMap<InterJIRValue, InterVarInfo> pointsToInfos = pointsToAnalysisForOtherMethod
												.getResultFact(otherMethod.getCFG().getExit());
										// System.out.println("该函数的指向分析结果为：");
										// System.out.println(pointsToInfos.toString());

										boolean irrelevant = true;
										boolean existNull = false;

										StringBuilder builder = new StringBuilder();
										builder.append(method.owner.replace('.', '/') + "::");
										builder.append(method.name).append(method.desc);

										if (callees.contains(builder.toString())) {
											System.out.println("函数（" + otherMethod.name + ") 会调用该异常抛出方法");
											DBUtil.saveMethodsRelationship(proName, className, bmAcc, otherMethod.name
													+"&"+ otherMethod.desc, amAcc, method.name +"&"+ method.desc,
													"InnerInvoke_Exp", methodsNumber, "");
											irrelevant = false;
										}

										if (irrelevant == true) {
											for (BinopExpr expr : allPreConditions) {

												// this.a == null
												if (expr.opType == BinopExpr.EQUAL && expr.op2 instanceof Null) {

													// 如果pointsToInfos中不存在该变量的指向信息，说明该函数对此变量不会进行任何操作，可以忽略该函数的影响
													// 如果包含该变量的指向信息，则查看指向的object是否为空
													if (pointsToInfos.get(new InterJIRValue(expr.op1, 0)) != null) {

														// 如果为空，则说明无法判断；如果指向的object中包含null，则说明指向为null；如果包含内容不为空且不为null则不会导致异常抛出
														if (pointsToInfos.get(new InterJIRValue(expr.op1, 0)).objects
																.size() != 0) {
															irrelevant = false;
															for (String object : pointsToInfos.get(new InterJIRValue(
																	expr.op1, 0)).objects) {
																if (object.equals("null")) {
																	System.out.println("函数（" + otherMethod.name
																			+ ") 会导致异常抛出条件成立：" + expr.toString());
																	existNull = true;
																	DBUtil.saveMethodsRelationship(proName, className, bmAcc,
																			otherMethod.name +"&"+ otherMethod.desc, amAcc,
																			method.name +"&"+ method.desc,
																			"Sequential_Exp", methodsNumber, "");
																	break;
																}
															}

															if (existNull == false) {

																System.out.println("函数（" + otherMethod.name
																		+ ") 不会导致异常抛出条件成立：" + expr.toString());
																DBUtil.saveMethodsRelationship(proName, className, bmAcc,
																		otherMethod.name +"&"+ otherMethod.desc, amAcc,
																		method.name +"&"+ method.desc,
																		"Sequential_NonExp_Assign", methodsNumber, "");
															}

														}
													} else {
														// 如果函数中的pointsToInfo中不包括expr.op1相关的指向信息，说明该函数未对该变量进行赋值操作，
														// 但有可能因为该函数进行了判断操作，这样程序员在调用该函数后，可以通过该函数的返回结果来响应的调用该异常所在的方法，从而避免异常的发生

														for (BinopExpr returnJudgeExpr : allReturnConditions) {
															if ((returnJudgeExpr.op1.equals(expr.op1) && returnJudgeExpr.op2
																	.equals(expr.op2))
																	|| (returnJudgeExpr.op1.equals(expr.op2) && returnJudgeExpr.op2
																			.equals(expr.op1))) {
																System.out.println("函数（" + otherMethod.name
																		+ ") 调用结果可以帮助程序员有选择的调用异常所在函数，从而避免异常发生："
																		+ expr.toString());
																DBUtil.saveMethodsRelationship(proName, className, bmAcc,
																		otherMethod.name +"&"+ otherMethod.desc, amAcc,
																		method.name +"&"+ method.desc,
																		"Sequential_NonExp_Judge", methodsNumber, "");
																irrelevant = false;
																break;
															}
														}

													}

												} else if (expr.opType == BinopExpr.NEQUAL && expr.op2 instanceof Null) {
													// this.a != null

													// 空说明不能判定；有且其中没有null，则说明会导致异常发生；有且其中包含null，则说明不会导致异常发生
													if (pointsToInfos.get(new InterJIRValue(expr.op1, 0)) != null) {

														if (pointsToInfos.get(new InterJIRValue(expr.op1, 0)).objects
																.size() != 0) {
															irrelevant = false;
															for (String object : pointsToInfos.get(new InterJIRValue(
																	expr.op1, 0)).objects) {
																if (object.equals("null")) {
																	System.out.println("函数（" + otherMethod.name
																			+ ") 不会导致异常抛出条件成立：" + expr.toString());
																	existNull = true;
																	DBUtil.saveMethodsRelationship(proName, className, bmAcc,
																			otherMethod.name +"&"+ otherMethod.desc, amAcc,
																			method.name +"&"+ method.desc,
																			"Sequential_NonExp_Assign", methodsNumber,
																			"");
																	break;
																}
															}

															if (existNull == false) {
																System.out.println("函数（" + otherMethod.name
																		+ ") 会导致异常抛出条件成立：" + expr.toString());
																DBUtil.saveMethodsRelationship(proName, className, bmAcc,
																		otherMethod.name +"&"+ otherMethod.desc, amAcc,
																		method.name +"&"+ method.desc, "Sequential_Exp",
																		methodsNumber, "");
															}

														}
													} else {
														for (BinopExpr returnJudgeExpr : allReturnConditions) {
															if ((returnJudgeExpr.op1.equals(expr.op1) && returnJudgeExpr.op2
																	.equals(expr.op2))
																	|| (returnJudgeExpr.op1.equals(expr.op2) && returnJudgeExpr.op2
																			.equals(expr.op1))) {
																System.out.println("函数（" + otherMethod.name
																		+ ") 调用结果可以帮助程序员有选择的调用异常所在函数，从而避免异常发生："
																		+ expr.toString());
																DBUtil.saveMethodsRelationship(proName, className, bmAcc,
																		otherMethod.name +"&"+ otherMethod.desc, amAcc,
																		method.name +"&"+ method.desc,
																		"Sequential_NonExp_Judge", methodsNumber, "");
																irrelevant = false;
																break;
															}
														}
													}

												} else if (expr.op1 instanceof IntConstant
														|| expr.op2 instanceof IntConstant) {
													// this.a == 1; this.a != 1;
													// a > 1;
													// a <
													// 1;
													JIRValue jirValue;
													IntConstant constant;
													int opType;
													if (expr.op1 instanceof IntConstant) {
														jirValue = expr.op2;
														opType = BinopExpr.mirrorOpType(expr);
														constant = (IntConstant) expr.op1;
													} else {
														jirValue = expr.op1;
														opType = expr.opType;
														constant = (IntConstant) expr.op2;
													}

													if (pointsToInfos.get(new InterJIRValue(jirValue, 0)) != null) {
														if (pointsToInfos.get(new InterJIRValue(jirValue, 0)).values
																.size() != 0) {
															for (JIRValue object : pointsToInfos.get(new InterJIRValue(
																	jirValue, 0)).values) {
																if (object instanceof IntConstant) {
																	irrelevant = false;
																	IntConstant intConstant = (IntConstant) object;
																	if (compareTwoIntConstant(intConstant, opType,
																			constant)) {
																		System.out.println("函数（" + otherMethod.name
																				+ ") 会导致异常抛出条件成立：" + expr.toString());
																		DBUtil.saveMethodsRelationship(proName, className,
																				bmAcc, otherMethod.name
																				+"&"+ otherMethod.desc, amAcc,
																				method.name +"&"+ method.desc,
																				"Sequential_Exp", methodsNumber, "");
																		break;
																	} else {
																		System.out.println("函数（" + otherMethod.name
																				+ ") 不会导致异常抛出条件成立：" + expr.toString());
																		existNull = true;
																		DBUtil.saveMethodsRelationship(proName, className,
																				bmAcc, otherMethod.name
																				+"&"+ otherMethod.desc, amAcc,
																				method.name +"&"+ method.desc,
																				"Sequential_NonExp_Assign",
																				methodsNumber, "");
																		break;
																	}

																}
															}
														}

													} else {
														for (BinopExpr returnJudgeExpr : allReturnConditions) {
															if ((returnJudgeExpr.op1.equals(expr.op1) && returnJudgeExpr.op2
																	.equals(expr.op2))
																	|| (returnJudgeExpr.op1.equals(expr.op2) && returnJudgeExpr.op2
																			.equals(expr.op1))) {
																System.out.println("函数（" + otherMethod.name
																		+ ") 调用结果可以帮助程序员有选择的调用异常所在函数，从而避免异常发生："
																		+ expr.toString());
																DBUtil.saveMethodsRelationship(proName, className, bmAcc,
																		otherMethod.name +"&"+ otherMethod.desc, amAcc,
																		method.name +"&"+ method.desc,
																		"Sequential_NonExp_Judge", methodsNumber, "");
																irrelevant = false;
																break;
															}
														}
													}

												} else if (expr.op1 instanceof FieldRef && expr.op2 instanceof FieldRef
														&& expr.op1.getType().getSort() <= Type.DOUBLE
														&& expr.op2.getType().getSort() <= Type.DOUBLE) {
													// this.a == this.b;

												}

												// 如果分析出了结论，停止依次分析异常条件的过程
												if (irrelevant == false) {
													break;
												}
											}
										}

										if (irrelevant == true) {
											System.out.println("函数（" + otherMethod.name + ") 与该方法的所有异常抛出并无确定性关联：");

											// if(allCalleeNames.contains(otherMethod.name
											// + otherMethod.desc)){
											// System.out.println("函数（" +
											// otherMethod.name + ")
											// 的条件判断可以避免异常抛出：");
								 			// DBUtil.saveMethodsRelationshipIntoDatabase(className,
											// otherMethod.name +
											// otherMethod.desc,
											// method.name
											// + method.desc,
											// "Sequential_NonExp_judge",
											// methodsNumber, "");
											// }

										}

										System.out.println("结束分析函数（" + otherMethod.name + ")！");
										System.out.println("");
										System.out.println("——————————————————————————————————————————");

									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								} catch (Exception e) {
									
									e.printStackTrace();
								}
							}

						}
					} else {
						// 判断是否是简单的调用关系 InnerInvoke_NonExpr
						for (MethodNode otherMethod : cc.methods) {
							if (!method.equals(otherMethod)) {
								try {

									InterValueDataflowAnalysis pointsToAnalysisForOtherMethod = null;
									HashSet<String> callees = new HashSet<String> ();
									
									if(ptCache.get(otherMethod.getFullName()) != null) {
										pointsToAnalysisForOtherMethod = ptCache.get(otherMethod.getFullName());
										callees = calleesCache.get(otherMethod.getFullName());
									} else {
										InterValueDataflowAnalysis.callees.clear();
										pointsToAnalysisForOtherMethod = new InterValueDataflowAnalysis(otherMethod.getCFG());
										pointsToAnalysisForOtherMethod.execute();
										ptCache.put(otherMethod.getFullName(), pointsToAnalysisForOtherMethod);
										calleesCache.put(otherMethod.getFullName(), (HashSet<String>)InterValueDataflowAnalysis.callees.clone());
									}
									
									System.out.println(" otherMethod:" + className +  otherMethod.name + otherMethod.desc);

									StringBuilder builder = new StringBuilder();
									builder.append(method.owner.replace('.', '/') + "::");
									builder.append(method.name).append(method.desc);

									if (callees.contains(builder.toString())) {
										System.out.println("函数（" + otherMethod.name + ") 会调用该方法,该方法不会抛出异常");
										DBUtil.saveMethodsRelationship(proName, className, bmAcc, otherMethod.name
												+"&"+ otherMethod.desc, amAcc, method.name +"&"+ method.desc,
												"InnerInvoke_NonExp", methodsNumber, "");
									}

								} catch (Exception e) {
									e.printStackTrace();
								}

							}
						}

					}
					System.out.println("******************************");
					// anlysis.examineResults();
				} catch (AnalyzerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	private static boolean compareTwoIntConstant(IntConstant leftConstant, int opType, IntConstant rightConstant) {
		switch (opType) {
		case BinopExpr.EQUAL:
			return leftConstant.value == rightConstant.value;
		case BinopExpr.NEQUAL:
			return leftConstant.value != rightConstant.value;
		case BinopExpr.LT:
			return leftConstant.value < rightConstant.value;
		case BinopExpr.GTE:
			return leftConstant.value >= rightConstant.value;
		case BinopExpr.GT:
			return leftConstant.value > rightConstant.value;
		case BinopExpr.LTE:
			return leftConstant.value <= rightConstant.value;
		default:
			return false;
		}

	}

	private static void filterConditions(HashSet<BinopExpr> allPreConditions, HashSet<BinopExpr> preCondictions,
			HashMap<InterJIRValue, InterVarInfo> pointsToInfos) {
		for (BinopExpr expr : preCondictions) {
			boolean toFilter = true;

			if (expr.op1 instanceof LocalRef || expr.op1 instanceof TempRef) {

				// 以下过程应该是递归过程
				
				if (pointsToInfos.get(new InterJIRValue(expr.op1, 0)) != null) {
					
//					 对引用类型进行判断
					for (InterJIRValue alias : pointsToInfos.get(new InterJIRValue(expr.op1, 0)).alias) {
						if (alias.intraJIRValue instanceof FieldRef) {
							FieldRef aliaFieldRef = (FieldRef) alias.intraJIRValue;
							if (aliaFieldRef.base instanceof LocalRef) {
								LocalRef baseLocalRef = (LocalRef) aliaFieldRef.base;
								if ("this".equals(baseLocalRef.nodeRef.name)) {
									toFilter = false;
									expr.op1 = aliaFieldRef;
									break;
								}
							} else if (aliaFieldRef.base instanceof FieldRef) {
								FieldRef baseRef = (FieldRef) aliaFieldRef.base;
								if (baseRef.base instanceof LocalRef) {
									LocalRef baseLocalRef = (LocalRef) baseRef.base;
									if ("this".equals(baseLocalRef.nodeRef.name)) {
										toFilter = false;
										expr.op1 = aliaFieldRef;
										break;
									}
								}
								
							}
						}
					}
					
					// 基本类型数据
					// 记录 lastAssignJIRValues 最好使用 this.* 来代替
					for (InterJIRValue alias : pointsToInfos.get(new InterJIRValue(expr.op1, 0)).lastAssignJIRValues) {
						if (alias.intraJIRValue instanceof FieldRef) {
							FieldRef aliaFieldRef = (FieldRef) alias.intraJIRValue;
							if (aliaFieldRef.base instanceof LocalRef) {
								LocalRef baseLocalRef = (LocalRef) aliaFieldRef.base;
								if ("this".equals(baseLocalRef.nodeRef.name)) {
									toFilter = false;
									expr.op1 = aliaFieldRef;
									break;
								} 
								
							}  else if (aliaFieldRef.base instanceof FieldRef) {
								FieldRef baseRef = (FieldRef) aliaFieldRef.base;
								if (baseRef.base instanceof LocalRef) {
									LocalRef baseLocalRef = (LocalRef) baseRef.base;
									if ("this".equals(baseLocalRef.nodeRef.name)) {
										toFilter = false;
										expr.op1 = aliaFieldRef;
										break;
									}
								}
								
							}
						}
					}	
				}
				
			} else if (expr.op1 instanceof FieldRef) {

				FieldRef op1FieldRef = (FieldRef) expr.op1;
				if (op1FieldRef.base.toString().equals("this")) {
					toFilter = false;
					expr.op1 = op1FieldRef;
				} else {

					if (pointsToInfos.get(new InterJIRValue(expr.op1, 0)) != null) {
						for (InterJIRValue alias : pointsToInfos.get(new InterJIRValue(expr.op1, 0)).alias) {
							if (alias.intraJIRValue instanceof FieldRef) {
								FieldRef aliaFieldRef = (FieldRef) alias.intraJIRValue;
								if (aliaFieldRef.base instanceof LocalRef) {
									LocalRef baseLocalRef = (LocalRef) aliaFieldRef.base;
									if ("this".equals(baseLocalRef.nodeRef.name)) {
										toFilter = false;
										expr.op1 = aliaFieldRef;
										break;
									}
								} else if (aliaFieldRef.base instanceof FieldRef) {
									FieldRef baseRef = (FieldRef) aliaFieldRef.base;
									if (baseRef.base instanceof LocalRef) {
										LocalRef baseLocalRef = (LocalRef) baseRef.base;
										if ("this".equals(baseLocalRef.nodeRef.name)) {
											toFilter = false;
											expr.op1 = aliaFieldRef;
											break;
										}
									}
									
								}
							}
						}
					}
				}
			}

			if (expr.op2 instanceof LocalRef) {

				if (pointsToInfos.get(new InterJIRValue(expr.op2, 0)) != null) {
					for (InterJIRValue alias : pointsToInfos.get(new InterJIRValue(expr.op2, 0)).alias) {
						if (alias.intraJIRValue instanceof FieldRef) {
							FieldRef aliaFieldRef = (FieldRef) alias.intraJIRValue;
							if (aliaFieldRef.base instanceof LocalRef) {
								LocalRef baseLocalRef = (LocalRef) aliaFieldRef.base;
								if ("this".equals(baseLocalRef.nodeRef.name)) {
									toFilter = false;
									expr.op2 = aliaFieldRef;
								}
							}
						}
					}
				}

			} else if (expr.op2 instanceof FieldRef) {

				FieldRef op2FieldRef = (FieldRef) expr.op2;
				if (op2FieldRef.base.toString().equals("this")) {
					toFilter = false;
					expr.op2 = op2FieldRef;
				} else {

					if (pointsToInfos.get(new InterJIRValue(expr.op2, 0)) != null) {
						for (InterJIRValue alias : pointsToInfos.get(new InterJIRValue(expr.op2, 0)).alias) {
							if (alias.intraJIRValue instanceof FieldRef) {
								FieldRef aliaFieldRef = (FieldRef) alias.intraJIRValue;
								if (aliaFieldRef.base instanceof LocalRef) {
									LocalRef baseLocalRef = (LocalRef) aliaFieldRef.base;
									if ("this".equals(baseLocalRef.nodeRef.name)) {
										toFilter = false;
										expr.op2 = aliaFieldRef;
									}
								}
							}
						}
					}
				}
			}
			
			if (toFilter == false) {
				System.out.println(expr.toString());
				allPreConditions.add(expr);
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

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
 * 这个方法是一个跨过程的变量相关信息分析的流程：包含基本类型的变量的值分析，类型分析 + 引用类型的变量的指向信息，类型信息
 */
package edu.pku.cn.analysis;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import edu.pku.cn.Project;
import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.classfile.ClassNodeLoader;
import edu.pku.cn.classfile.LocalVariableNode;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.graph.callgraph.CallGraphEdge;
import edu.pku.cn.graph.callgraph.InvokeType;
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
import edu.pku.cn.jir.DoubleConstant;
import edu.pku.cn.jir.FieldRef;
import edu.pku.cn.jir.FloatConstant;
import edu.pku.cn.jir.IfStmt;
import edu.pku.cn.jir.IntConstant;
import edu.pku.cn.jir.InvokeExpr;
import edu.pku.cn.jir.InvokeStmt;
import edu.pku.cn.jir.JIR;
import edu.pku.cn.jir.JIRValue;
import edu.pku.cn.jir.LineStmt;
import edu.pku.cn.jir.LocalRef;
import edu.pku.cn.jir.LongConstant;
import edu.pku.cn.jir.NewArrayExpr;
import edu.pku.cn.jir.NewExpr;
import edu.pku.cn.jir.Null;
import edu.pku.cn.jir.Ref;
import edu.pku.cn.jir.ReturnStmt;
import edu.pku.cn.jir.Stmt;
import edu.pku.cn.jir.StringConstant;
import edu.pku.cn.jir.TempRef;
import edu.pku.cn.util.AnalysisFactoryManager;
import edu.pku.cn.util.CodaProperties;

public class EscapedObjsDataflowAnalysis extends
		RefactoredBasicDataflowAnalysis<HashMap<JIRValue, HashSet<JIRValue>>> {

	int currentLine;
	String desc;
	boolean isInConstructor = false;

	private int level = 0;
	private int maxInterLevel = 1;
	private int numIterations = 0;
	private static final int MAX_ITERS = 10;
	private final boolean DEBUG = false;
	
	private InsnList insns;
	private List<Stmt> stmts;
	private CFG cfg;
	private HashMap<JIRValue, HashSet<JIRValue>> initFact;
	
	private InterJIRValue returnInterJIRValue;
	static public ClassNodeLoader loader;

	public EscapedObjsDataflowAnalysis(CFG cfg) {
		this.blockOrder = new ReversePostOrder(cfg);
		this.isForwards = true;
		this.cfg = cfg;
		this.stmts = cfg.getJIRStmt();
		this.insns = cfg.getMethod().instructions;
	}

	public EscapedObjsDataflowAnalysis(CFG cfg, HashMap<JIRValue, HashSet<JIRValue>> initFact, int level) {
		this.blockOrder = new ReversePostOrder(cfg);
		this.isForwards = true;
		this.cfg = cfg;
		this.stmts = cfg.getJIRStmt();
		this.insns = cfg.getMethod().instructions;
		this.initFact = initFact;
		this.level = level;
	}
	
	@Override
	public HashMap<JIRValue, HashSet<JIRValue>> createFact() {
		HashMap<JIRValue, HashSet<JIRValue>> fact = new HashMap<JIRValue, HashSet<JIRValue>>();
		return fact;
	}

	@Override
	public HashMap<JIRValue, HashSet<JIRValue>> createFact(HashMap<JIRValue, HashSet<JIRValue>> fact) {
		HashMap<JIRValue, HashSet<JIRValue>> newFact = new HashMap<JIRValue, HashSet<JIRValue>>();
		for (JIRValue wrapperObject : fact.keySet()) {
			HashSet<JIRValue> innerObjects = fact.get(wrapperObject);
			newFact.put(wrapperObject, (HashSet<JIRValue>)innerObjects.clone());
		}
		return newFact;
	}

	// @Override
	// public HashSet<String> getNewStartFact(BasicBlock block) {
	// return facts[block.startStmt];
	// }

	@Override
	public void initEntryFact() {
		if (this.initFact == null) {
			this.initFact = createFact();
		}
		startFactMap.put(blockOrder.getEntry(), this.initFact);
	}

	@Override
	public boolean isEndBlock(BasicBlock block) {
		return block.getStartInc() >= insns.size();
	}

	@Override
	public HashMap<JIRValue, HashSet<JIRValue>> merge(HashMap<JIRValue, HashSet<JIRValue>> start,
			HashMap<JIRValue, HashSet<JIRValue>> pred) {
		HashMap<JIRValue, HashSet<JIRValue>> result = this.createFact(start);

		for (JIRValue wrapperObject : pred.keySet()) {

			// must analysis - 取交集
			HashSet<JIRValue> predInnerObjects = pred.get(wrapperObject);
			HashSet<JIRValue> resultInnerObjects = result.get(wrapperObject);
			
			if (resultInnerObjects != null) {
				HashSet<JIRValue> mustInnerObjects = new HashSet<JIRValue>();
				for (JIRValue innerObj : predInnerObjects) {
					if (resultInnerObjects.contains(innerObj)) {
						mustInnerObjects.add(innerObj);
					}
				}
				
				if (mustInnerObjects.size() > 0) {
					result.put(wrapperObject, mustInnerObjects);
				} else {
					result.remove(wrapperObject);
				}
				
			} else {
				if (predInnerObjects.size() > 0) {
					result.put(wrapperObject, predInnerObjects);
				}
			}
				
		}
		return result;
	}

	@Override
	public boolean same(HashMap<JIRValue, HashSet<JIRValue>> fact1, HashMap<JIRValue, HashSet<JIRValue>> fact2) {
		if (fact1.keySet().size() != fact2.keySet().size())
			return false;

		for (JIRValue wraperJIRValue : fact1.keySet()) {
			if (fact2.get(wraperJIRValue) == null) {
				return false;
			}

			HashSet<JIRValue> fact1Info = fact1.get(wraperJIRValue);
			HashSet<JIRValue> fact2Info = fact2.get(wraperJIRValue);

			if (!fact1Info.equals(fact2Info)) {
				return false;
			}
		}
		
		return true;
	}

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
	}

	@Override
	public HashMap<JIRValue, HashSet<JIRValue>> transferVertex(BasicBlock block) {

		HashMap<JIRValue, HashSet<JIRValue>> fact = this.createFact(getStartFact(block));

		if (fact == null)
			fact = new HashMap<JIRValue, HashSet<JIRValue>>();

		for (int i = block.startStmt; i <= block.endStmt && block.endStmt != 0; i++) {
			try {
				Stmt stmt = this.stmts.get(i);
				
				if (stmt instanceof LineStmt) {
					LineStmt ls = (LineStmt) stmt;
					currentLine = ls.line;
				} else if (stmt instanceof IfStmt) {

				} else if (stmt instanceof AssignStmt) {

					AssignStmt assignStmt = (AssignStmt) stmt;
					
					JIRValue leftValue = assignStmt.left;
					Type type = leftValue.getType();
					JIRValue rightValue = assignStmt.right;
										
//					 确保赋值语句是引用类型或基本类型
					if (type.getSort() >= Type.ARRAY) {

						if (leftValue instanceof LocalRef || leftValue instanceof TempRef || leftValue instanceof FieldRef || leftValue instanceof ArrayRef) {
							// e.g. a = x; a = x.f;
							// array ref: // a[1] = new A(); a[i] = "String";
							
							if (rightValue instanceof AnyNewExpr) {
								// e.g.: a = new B();
								// e.g.: a = new B[]();
								if (rightValue instanceof NewExpr) {
									
								} else if (rightValue instanceof NewArrayExpr) {
									// a = new B[]();	
								}

							} else if (rightValue instanceof Constant) {
								// e.g.: a = "string";

								if (rightValue instanceof StringConstant) {
									
								} else if (rightValue instanceof Null) {
									// e.g.: a = null;

								}

							} else if (rightValue instanceof Ref) {
								// e.g.: a = b; a = b.c; a = @CaughException; a
								// ="string";

								// new created object type
								Type objectType;
								String objectName = "";

								// e.g.: e=@CaughException
								if (rightValue instanceof CaughtExceptionRef) {
									
								} else if (rightValue instanceof LocalRef || rightValue instanceof FieldRef
										|| rightValue instanceof ArrayRef) {
									// e.g.: a = b;
								}

							} else if (rightValue instanceof InvokeExpr) {

								if (rightValue instanceof InvokeExpr) {
									InvokeExpr invokeExpr = (InvokeExpr) rightValue;
									HashSet<JIRValue> innerObjects;
									if (fact.get(leftValue)!= null) {
										innerObjects = fact.get(leftValue);
									} else {
										innerObjects = new HashSet<JIRValue>();
									}
									
									for (JIRValue innerObj : invokeExpr.params) {
										if (innerObj.getType().getSort() >= Type.ARRAY) {
											innerObjects.add(innerObj);
										}
									}
									
									if (innerObjects.size() > 0) {
										fact.put(leftValue, innerObjects);
									}
									
								}
								
								
								
							} else if (rightValue instanceof CastExpr) {
								// e.g.: a = (A)b;
								CastExpr castExpr = (CastExpr) rightValue;
							}
						} 
					} else if (type.getSort() > Type.VOID && type.getSort() < Type.ARRAY) {
						
					}

				} else if (stmt instanceof InvokeStmt) {
					
					InvokeStmt invokeStmt = (InvokeStmt)stmt;
					InvokeExpr invokeExpr = (InvokeExpr) invokeStmt.getInvoke();
					JIRValue leftValue = invokeExpr.invoker;
					
					HashSet<JIRValue> innerObjects;
					if (fact.get(leftValue)!= null) {
						innerObjects = fact.get(leftValue);
					} else {
						innerObjects = new HashSet<JIRValue>();
					}
					
					for (JIRValue innerObj : invokeExpr.params) {
						if (innerObj.getType().getSort() >= Type.ARRAY) {
							innerObjects.add(innerObj);
						}
					}
					
					if (innerObjects.size() > 0) {
						fact.put(leftValue, innerObjects);
					}
					
				} else if (stmt instanceof ReturnStmt) {
					ReturnStmt returnStmt = (ReturnStmt) stmt;
					this.returnInterJIRValue = new InterJIRValue(returnStmt.value, this.level);
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("this block start stmt:" + block.startStmt + " end stmt:" + block.endStmt);
			}

		}

		// printTransferResult();
		return fact;

	}

	public void initEntryBlock() {
		this.initEntryFact();
		HashMap<JIRValue, HashSet<JIRValue>> result = this.transferVertex(logicalEntryBlock());
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

	public Iterator<Edge> logicalPredecessorEdgeIterator(BasicBlock block) {
		return isForwards ? block.inEdgeIterator() : block.outEdgeIterator();
	}

	protected Iterator<Edge> logicalSuccessorEdgeIterator(BasicBlock block) {
		return isForwards ? block.outEdgeIterator() : block.inEdgeIterator();
	}

	public synchronized void execute() throws AnalyzerException {
		boolean change;
		
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
				HashMap<JIRValue, HashSet<JIRValue>> start = getStartFact(block);
				HashMap<JIRValue, HashSet<JIRValue>> result = getResultFact(block);

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
					HashMap<JIRValue, HashSet<JIRValue>> newStart = new HashMap<JIRValue, HashSet<JIRValue>>();
					if (block.equals(logicalEntryBlock())) {
						newStart = this.initFact;
					}

					// get all predecessors' result fact and merge them with the
					// new start fact
					int preEdgeNumber = 0;
					while (predEdgeIter.hasNext()) {
						preEdgeNumber++;
						Edge edge = predEdgeIter.next();
						BasicBlock logicalPred = isForwards ? edge.getSource() : edge.getTarget();
						HashMap<JIRValue, HashSet<JIRValue>> pred = null;
						// ////////////perhaps we need to distinguish different
						// handlers
//						if (edge.getType() != EdgeType.HANDLED_EXCEPTION_EDGE) {
							if (this.blockOrder instanceof ReversePostOrder) {
								pred = createFact(getResultFact(logicalPred));
								if (preEdgeNumber == 1) {
									newStart = this.createFact(pred);
								} else {
									newStart = merge(newStart, pred);
								}
							}
//						}
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

					HashMap<JIRValue, HashSet<JIRValue>> newResult = transferVertex(block);

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
			HashMap<JIRValue, HashSet<JIRValue>> start = this.getStartFact(block);

			// result 有问题，但下一个block的start是没有问题的
			HashMap<JIRValue, HashSet<JIRValue>> result = this.getResultFact(block);

			System.out.println("________________________________");

			for (JIRValue key : start.keySet()) {
				System.out.println("Wrapper:" + key.toString());
				for (JIRValue innerObj : start.get(key)) {
					System.out.println("	inners:" + innerObj.toString());
				}
			}

			System.out.println("");

			for (int m = block.startStmt; m <= block.endStmt; m++) {
				System.out.println(m + ": " + block.stmts.get(m).toString());
			}

			System.out.println("");

			for (JIRValue key : result.keySet()) {
				System.out.println("Wrapper:" + key.toString());
				for (JIRValue innerObj : result.get(key)) {
					System.out.println("	inners:" + innerObj.toString());
				}
			}

			System.out.println("");

			System.out.println("________________________________");
		}
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

		EscapedObjsDataflowAnalysis.loader = loader;

		for (MethodNode method : cc.methods) {

			// if (method.name.contains("right")) {
			// method.getStmts();
			
			if (method.getOriginalFullName().equals("org.jfree.chart.servlet.ServletUtilities::sendTempFile(Ljava/io/File;Ljavax/servlet/http/HttpServletResponse;Ljava/lang/String;)")) {
				continue;
			}
			
			System.out.println(method.name + method.desc);
			CFG cfg = method.getCFG();

			EscapedObjsDataflowAnalysis anlysis = new EscapedObjsDataflowAnalysis(cfg);
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

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
package edu.pku.cn.analysis.sercurity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import edu.pku.cn.Project;
import edu.pku.cn.analysis.CallingContext;
import edu.pku.cn.analysis.InterJIRValue;
import edu.pku.cn.analysis.RefactoredBasicDataflowAnalysis;
import edu.pku.cn.analysis.vo.security.InterSQLInjectionInfo;
import edu.pku.cn.analysis.vo.security.InterSQLInjectionPath;
import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.classfile.ClassNodeLoader;
import edu.pku.cn.classfile.LocalVariableNode;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.graph.cfg.BasicBlock;
import edu.pku.cn.graph.cfg.CFG;
import edu.pku.cn.graph.cfg.Edge;
import edu.pku.cn.graph.cfg.EdgeType;
import edu.pku.cn.graph.visit.ReversePostOrder;
import edu.pku.cn.hierarchy.Repository;
import edu.pku.cn.jir.ArrayRef;
import edu.pku.cn.jir.AssignStmt;
import edu.pku.cn.jir.FieldRef;
import edu.pku.cn.jir.InvokeExpr;
import edu.pku.cn.jir.InvokeStmt;
import edu.pku.cn.jir.JIRValue;
import edu.pku.cn.jir.LineStmt;
import edu.pku.cn.jir.LocalRef;
import edu.pku.cn.jir.ParamRef;
import edu.pku.cn.jir.Ref;
import edu.pku.cn.jir.ReturnStmt;
import edu.pku.cn.jir.Stmt;
import edu.pku.cn.jir.TempRef;
import edu.pku.cn.util.AnalysisFactoryManager;
import edu.pku.cn.util.CodaProperties;

public class InterSQLInjectionDataflowAnalysis extends
		RefactoredBasicDataflowAnalysis<HashMap<InterJIRValue, InterSQLInjectionInfo>> {

	public boolean Debug = false;

	int currentLine;
	String desc;
	boolean isInConstructor = false;

	private InsnList insns;
	private List<Stmt> stmts;
	private CFG cfg;
	private HashMap<InterJIRValue, InterSQLInjectionInfo> initFact;
	private int level = 0;
	private int maxInterLevel = 2;
	private CallingContext callingContext;

	private InterJIRValue returnInterJIRValue;

	static public HashSet<String> callees = new HashSet<String>();
	static public ClassNodeLoader loader;

	// HashMap<JIRValue, IntraPointsToInfo> pointsToInfos = new
	// HashMap<JIRValue, IntraPointsToInfo>();

	public InterSQLInjectionDataflowAnalysis(CFG cfg) {
		this.blockOrder = new ReversePostOrder(cfg);
		this.isForwards = true;
		this.cfg = cfg;
		this.stmts = cfg.getJIRStmt();
		this.insns = cfg.getMethod().instructions;
	}

	public InterSQLInjectionDataflowAnalysis(CFG cfg, HashMap<InterJIRValue, InterSQLInjectionInfo> initFact,
			int level, CallingContext callingContext) {
		this.blockOrder = new ReversePostOrder(cfg);
		this.isForwards = true;
		this.cfg = cfg;
		this.stmts = cfg.getJIRStmt();
		this.insns = cfg.getMethod().instructions;
		this.initFact = initFact;
		this.level = level;
		this.callingContext = callingContext;
	}

	@Override
	public HashMap<InterJIRValue, InterSQLInjectionInfo> createFact() {
		HashMap<InterJIRValue, InterSQLInjectionInfo> fact = new HashMap<InterJIRValue, InterSQLInjectionInfo>();
		return fact;
	}

	@Override
	public HashMap<InterJIRValue, InterSQLInjectionInfo> createFact(HashMap<InterJIRValue, InterSQLInjectionInfo> fact) {
		HashMap<InterJIRValue, InterSQLInjectionInfo> newFact = new HashMap<InterJIRValue, InterSQLInjectionInfo>();
		for (InterJIRValue interJIRValue : fact.keySet()) {
			newFact.put(interJIRValue, fact.get(interJIRValue).clone());
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
	public HashMap<InterJIRValue, InterSQLInjectionInfo> merge(HashMap<InterJIRValue, InterSQLInjectionInfo> start,
			HashMap<InterJIRValue, InterSQLInjectionInfo> pred) {
		HashMap<InterJIRValue, InterSQLInjectionInfo> result = this.createFact(start);
		for (InterJIRValue interJIRValue : pred.keySet()) {
			InterSQLInjectionInfo info = pred.get(interJIRValue);
			if (result.get(interJIRValue) != null) {
				result.put(interJIRValue, result.get(interJIRValue).mergeWith(info));
			} else
				result.put(interJIRValue, info.clone());
		}
		return result;
	}

	@Override
	public boolean same(HashMap<InterJIRValue, InterSQLInjectionInfo> fact1,
			HashMap<InterJIRValue, InterSQLInjectionInfo> fact2) {
		if (fact1.keySet().size() != fact2.keySet().size())
			return false;
		for (InterJIRValue jirValue : fact1.keySet()) {
			if (fact2.get(jirValue) == null) {
				return false;
			}
			if (!fact1.get(jirValue).equals(fact2.get(jirValue))) {
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
		// throw new RuntimeException("some error in static invoke");
	}

	@Override
	public HashMap<InterJIRValue, InterSQLInjectionInfo> transferVertex(BasicBlock block) {
		HashMap<InterJIRValue, InterSQLInjectionInfo> fact = this.createFact(getStartFact(block));
		if (fact == null)
			fact = new HashMap<InterJIRValue, InterSQLInjectionInfo>();
		if (this.stmts.size() != 0) {
			for (int i = block.startStmt; i <= block.endStmt; i++) {
				try {
					Stmt stmt = this.stmts.get(i);
					if (stmt instanceof LineStmt) {
						transferLineStmt(stmt);
					} else if (stmt instanceof AssignStmt) {
						AssignStmt assignStmt = (AssignStmt) stmt;
						fact = transferAssignmentStmt(fact, assignStmt.left, this.level, assignStmt.right, this.level);
					} else if (stmt instanceof InvokeStmt) {
						fact = transferInvokeStmt(fact, stmt);
					} else if (stmt instanceof ReturnStmt) {
						transferReturnStmt(stmt);
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("this block start stmt:" + block.startStmt + " end stmt:" + block.endStmt);
				}
			}
		}
		// printTransferResult();
		return fact;

	}

	private void transferReturnStmt(Stmt stmt) {
		ReturnStmt returnStmt = (ReturnStmt) stmt;
		this.returnInterJIRValue = new InterJIRValue(returnStmt.value, this.level);
	}

	private HashMap<InterJIRValue, InterSQLInjectionInfo> transferInvokeStmt(
			HashMap<InterJIRValue, InterSQLInjectionInfo> fact, Stmt stmt) {
		InvokeStmt invokeStmt = (InvokeStmt) stmt;
		InterSQLInjectionDataflowAnalysis.callees.add(((InvokeExpr) invokeStmt.invoke).getMethodNodeName());
		if (this.level < maxInterLevel) {
			fact = interInvokeProceduralAnalysis(fact, stmt, this.level, false);
		}
		return fact;
	}

	private void transferLineStmt(Stmt stmt) {
		LineStmt ls = (LineStmt) stmt;
		currentLine = ls.line;
	}

	private HashMap<InterJIRValue, InterSQLInjectionInfo> transferAssignmentStmt(
			HashMap<InterJIRValue, InterSQLInjectionInfo> fact, JIRValue leftValue, int leftValueLevel,
			JIRValue rightValue, int rightValueLevel) {
		// a = x
		// a.f = x
		// a = x.f
		Type type = leftValue.getType();
		if (rightValue instanceof InvokeExpr) {
			InterSQLInjectionDataflowAnalysis.callees.add(((InvokeExpr) rightValue).getMethodNodeName());
		}
		// 确保赋值语句是引用类型或基本类型
		if (leftValue instanceof LocalRef) {
			fact = trasferAssignStmtLocalRef(fact, leftValue, leftValueLevel, rightValue, rightValueLevel);
		} else if (leftValue instanceof TempRef) {
			trasferAssignStmtTempRef(fact, leftValue, leftValueLevel, rightValue, rightValueLevel);
		} else {
			addFact(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, false);
		}
		return fact;
	}

	private HashMap<InterJIRValue, InterSQLInjectionInfo> trasferAssignStmtTempRef(
			HashMap<InterJIRValue, InterSQLInjectionInfo> fact, JIRValue leftValue, int leftValueLevel,
			JIRValue rightValue, int rightValueLevel) {
		if (rightValue instanceof Ref) {
			if ((rightValue instanceof LocalRef && !"this".equalsIgnoreCase(((LocalRef) rightValue).nodeRef.name))){
				if(rightValue instanceof ParamRef){
					if (fact.get(new InterJIRValue(rightValue, rightValueLevel))!=null&&(fact.get(new InterJIRValue(rightValue, rightValueLevel)).isTaint())) {
						addFactAll(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, true);
					} else {
						addFactAll(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, false);
					}
				}else{
					addFactAll(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, false);
				}
			}else if(rightValue instanceof FieldRef || rightValue instanceof TempRef) {
				if (fact.get(new InterJIRValue(rightValue, rightValueLevel))!=null&&fact.get(new InterJIRValue(rightValue, rightValueLevel)).isTaint()) {
					addFactAll(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, true);
				} else {
					addFactAll(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, false);
				}
			} else {
				addFactAll(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, false);
			}
		} else if (rightValue instanceof InvokeExpr) {
			InvokeExpr invoke = (InvokeExpr) rightValue;
			if (invoke.isStatic()) {
				if ("valueOf".equalsIgnoreCase(invoke.node.name)
						&& "(Ljava/lang/Object;)Ljava/lang/String;".equalsIgnoreCase(invoke.node.desc)
						&& "java/lang/String".equalsIgnoreCase(invoke.node.owner)) {
					handleParams(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, invoke);
				} else if ("()Ljava/lang/String;".equalsIgnoreCase(invoke.node.desc)) {
					if (leftValueLevel < maxInterLevel) {
						fact = interProceduralAnalysis(fact, leftValue, leftValueLevel, rightValue, rightValueLevel,
								true);
					} else {
						addFactAll(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, false);
					}
				} else {
					addFactAll(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, false);
				}
			} else {
				JIRValue invoker = invoke.invoker;
				// 真正引入SQL注入问题的字符串拼接,isTaint()用来判断拼接的参数是否污染了，可以在这里写污染逻辑
				// 判断temp$2=InvokeVirtual
				// temp$1.append(Ljava/lang/String;)Ljava/lang/StringBuilder;(username)
				// [18,19]情况
				if (invoker != null && invoker instanceof TempRef && "append".equalsIgnoreCase(invoke.node.name)
						&& "java/lang/StringBuilder".equalsIgnoreCase(invoke.node.owner)
						&& "(Ljava/lang/String;)Ljava/lang/StringBuilder;".equalsIgnoreCase(invoke.node.desc)) {
					if (fact.get(new InterJIRValue(((InvokeExpr) rightValue).invoker, rightValueLevel)) != null
							&& fact.get(new InterJIRValue(((InvokeExpr) rightValue).invoker, rightValueLevel))
									.isTaint()) {
						addFactAll(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, true);
					} else
						handleParams(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, invoke);
				} else if (invoker != null && invoker instanceof LocalRef
						&& "toString".equalsIgnoreCase(invoke.node.name)
						&& "java/lang/StringBuffer".equalsIgnoreCase(invoke.node.owner)
						&& "()Ljava/lang/String;".equalsIgnoreCase(invoke.node.desc)) {
					if (fact.get(new InterJIRValue(((InvokeExpr) rightValue).invoker, rightValueLevel)) != null
							&& fact.get(new InterJIRValue(((InvokeExpr) rightValue).invoker, rightValueLevel))
									.isTaint()) {
						addFactAll(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, true);
					} else
						handleParams(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, invoke);
				} else {
					if (leftValueLevel < maxInterLevel) {
						fact = interProceduralAnalysis(fact, leftValue, leftValueLevel, rightValue, rightValueLevel,
								true);
					} else {
						addFactAll(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, false);
					}
				} 
			}
		} else {
			addFactAll(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, false);
		}
		return fact;
	}

	private HashMap<InterJIRValue, InterSQLInjectionInfo> trasferAssignStmtLocalRef(
			HashMap<InterJIRValue, InterSQLInjectionInfo> fact, JIRValue leftValue, int leftValueLevel,
			JIRValue rightValue, int rightValueLevel) {
		if (rightValue instanceof Ref) {
			if ((rightValue instanceof LocalRef && !"this".equalsIgnoreCase(((LocalRef) rightValue).nodeRef.name))){
				if(rightValue instanceof ParamRef){
					if (fact.get(new InterJIRValue(rightValue, rightValueLevel))!=null&&(fact.get(new InterJIRValue(rightValue, rightValueLevel)).isTaint())) {
						addFactAll(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, true);
					} else {
						addFactAll(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, false);
					}
				}else{
					addFactAll(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, false);
				}
			}else if(rightValue instanceof FieldRef || rightValue instanceof TempRef) {
				if (fact.get(new InterJIRValue(rightValue, rightValueLevel))!=null&&fact.get(new InterJIRValue(rightValue, rightValueLevel)).isTaint()) {
					addFactAll(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, true);
				} else {
					addFactAll(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, false);
				}
			} else {
				addFactAll(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, false);
			}
		} else if (rightValue instanceof InvokeExpr) {
			InvokeExpr invoke = (InvokeExpr) rightValue;
			if (invoke.isStatic()) {
				if ("valueOf".equalsIgnoreCase(invoke.node.name)
						&& "(Ljava/lang/Object;)Ljava/lang/String;".equalsIgnoreCase(invoke.node.desc)
						&& "java/lang/String".equalsIgnoreCase(invoke.node.owner)) {
					handleParams(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, invoke);
				} else if ("()Ljava/lang/String;".equalsIgnoreCase(invoke.node.desc)) {
					if (leftValueLevel < maxInterLevel) {
						fact = interProceduralAnalysis(fact, leftValue, leftValueLevel, rightValue, rightValueLevel,
								true);
					} else {
						addFactAll(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, false);
					}
				} else {
					addFactAll(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, false);
				}
			} else {
				JIRValue invoker = invoke.invoker;
				// 真正引入SQL注入问题的字符串拼接,isTaint()用来判断拼接的参数是否污染了，可以在这里写污染逻辑
				// 判断temp$2=InvokeVirtual
				// temp$1.append(Ljava/lang/String;)Ljava/lang/StringBuilder;(username)
				// [18,19]情况
				if (invoker != null && invoker instanceof TempRef && "append".equalsIgnoreCase(invoke.node.name)
						&& "java/lang/StringBuilder".equalsIgnoreCase(invoke.node.owner)
						&& "(Ljava/lang/String;)Ljava/lang/StringBuilder;".equalsIgnoreCase(invoke.node.desc)) {
					if (fact.get(new InterJIRValue(((InvokeExpr) rightValue).invoker, rightValueLevel)) != null
							&& fact.get(new InterJIRValue(((InvokeExpr) rightValue).invoker, rightValueLevel))
									.isTaint()) {
						addFactAll(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, true);
					} else
						handleParams(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, invoke);
				} else if (invoker != null && invoker instanceof LocalRef
						&& "toString".equalsIgnoreCase(invoke.node.name)
						&& "java/lang/StringBuffer".equalsIgnoreCase(invoke.node.owner)
						&& "()Ljava/lang/String;".equalsIgnoreCase(invoke.node.desc)) {
					if (fact.get(new InterJIRValue(((InvokeExpr) rightValue).invoker, rightValueLevel)) != null
							&& fact.get(new InterJIRValue(((InvokeExpr) rightValue).invoker, rightValueLevel))
									.isTaint()) {
						addFactAll(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, true);
					} else
						handleParams(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, invoke);
				} else  {
					if (leftValueLevel < maxInterLevel) {
						fact = interProceduralAnalysis(fact, leftValue, leftValueLevel, rightValue, rightValueLevel,
								true);
					} else {
						addFactAll(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, false);
					}
				}
			}
		} else {
			addFactAll(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, false);
		}
		return fact;
	}

	// 处理调用参数
	private void handleParams(HashMap<InterJIRValue, InterSQLInjectionInfo> fact, JIRValue leftValue,
			int leftValueLevel, JIRValue rightValue, int rightValueLevel, InvokeExpr invoke) {
		if (invoke.params.size() > 0) {
			JIRValue jirValue = invoke.params.get(0);
			if (jirValue instanceof LocalRef && !"this".equalsIgnoreCase(((LocalRef) jirValue).nodeRef.name)) {
				if (isTaint(fact, jirValue)) {
					addFactAll(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, jirValue, true);
				} else {
					addFactAll(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, false);
				}
			} else if (jirValue instanceof TempRef) {
				if (isTaint(fact, jirValue)) {
					addFactAll(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, jirValue, true);
				} else {
					addFactAll(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, false);
				}
			} else {
				addFactAll(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, false);
			}
		} else {
			addFactAll(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, false);
		}
	}

	private boolean isTaint(HashMap<InterJIRValue, InterSQLInjectionInfo> fact, JIRValue jirValue) {
		return true;
	}

	private HashMap<InterJIRValue, InterSQLInjectionInfo> interProceduralAnalysis(
			HashMap<InterJIRValue, InterSQLInjectionInfo> fact, JIRValue leftValue, int leftValueLevel,
			JIRValue rightValue, int rightValueLevel, boolean handleResturn) {
		// e.g.: a = b.f(c);
		// A f(d){ return e}
		InvokeExpr invoke = (InvokeExpr) rightValue;
		String classNameToAnalyze = "";
		InterSQLInjectionInfo InterSQLInjectionInfo = fact.get(new InterJIRValue(invoke.invoker, rightValueLevel));

		classNameToAnalyze = invoke.node.owner;
		ClassNode cn = getClassNode(classNameToAnalyze);

		if (cn == null) {
			Type invokeType = Type.getObjectType(invoke.node.owner);
			if (invokeType != null && invokeType.getSort() == Type.OBJECT)
				cn = getClassNode("java.lang.Object");
		}
		if (cn != null) {
			MethodNode me = getMethod(invoke, cn);
			if (me != null) {
				// 成功加载callee方法，进行跨过程分析流程
				// if (invoke.isStatic() || !me.isAbstract()) {// 递归分析
				CFG calleeCFG = me.getCFG();
				// System.out.println("-> step into callee:" +
				// calleeCFG.getMethod().name);
				int calleeLevel = leftValueLevel + 1;
				CallingContext callingContext = new CallingContext(cfg.getMethod(), this.currentLine);
				HashMap<InterJIRValue, InterSQLInjectionInfo> calleeInitFact = fact;
				// 处理 this = b,就是b.f()进入的这个方法，需要把b作为第一个参数传入
				JIRValue actualParam = invoke.invoker;
				InterJIRValue interActualParam = new InterJIRValue(actualParam, leftValueLevel);
				LocalRef formalParamJIRValue;
				InterJIRValue interFormalParam;
				if (calleeCFG.getMethod().localVariables.size() > 0) {
					formalParamJIRValue = new LocalRef((LocalVariableNode) calleeCFG.getMethod().localVariables.get(0));
					interFormalParam = new InterJIRValue(formalParamJIRValue, calleeLevel);
					transferAssignmentStmt(calleeInitFact, interFormalParam.intraJIRValue, interFormalParam.scopeLevel,
							interActualParam.intraJIRValue, interActualParam.scopeLevel);
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
						transferAssignmentStmt(calleeInitFact, interFormalParam.intraJIRValue,
								interFormalParam.scopeLevel, interActualParam.intraJIRValue,
								interActualParam.scopeLevel);
					}
				}

				// 对callee执行递归分析
				InterSQLInjectionDataflowAnalysis calleeAnlysis = new InterSQLInjectionDataflowAnalysis(calleeCFG,
						calleeInitFact, calleeLevel, callingContext);
				try {
					calleeAnlysis.execute();
					// 处理返回参数
					// a = e; a.b = e;
					HashMap<InterJIRValue, InterSQLInjectionInfo> calleeResultFact = calleeAnlysis
							.getResultFact(calleeCFG.getExit());
					if (handleResturn == true && leftValue != null) {
						if (calleeAnlysis.returnInterJIRValue != null) {
							transferAssignmentStmt(calleeResultFact, leftValue, leftValueLevel,
									calleeAnlysis.returnInterJIRValue.intraJIRValue,
									calleeAnlysis.returnInterJIRValue.scopeLevel);
						}
					}
					// 删除callee变量
					filterUnreachableVariables(calleeResultFact);
					fact = calleeResultFact;
					// anlysis.examineResults();
				} catch (AnalyzerException e) {
					e.printStackTrace();
				}
				// System.out.println("<- step out callee:" +
				// calleeCFG.getMethod().name);

			} else {
				// 无法加载callee源码，进行模糊处理
				if (fact.get(new InterJIRValue(((InvokeExpr) rightValue).invoker, rightValueLevel)) != null
						&& fact.get(new InterJIRValue(((InvokeExpr) rightValue).invoker, rightValueLevel)).isTaint()) {
					addFactAll(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, true);
				} else {
					handleParams(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, invoke);
				}
			}
		} else {
			if (fact.get(new InterJIRValue(((InvokeExpr) rightValue).invoker, rightValueLevel)) != null
					&& fact.get(new InterJIRValue(((InvokeExpr) rightValue).invoker, rightValueLevel)).isTaint()) {
				addFactAll(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, true);
			} else {
				handleParams(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, invoke);
			}
		}
		return fact;
	}

	private HashMap<InterJIRValue, InterSQLInjectionInfo> interInvokeProceduralAnalysis(
			HashMap<InterJIRValue, InterSQLInjectionInfo> fact, Stmt stmt, int level, boolean handleResturn) {
		// e.g.: a = b.f(c);
		// A f(d){ return e}
		InvokeExpr invoke = (InvokeExpr) ((InvokeStmt) stmt).invoke;
		String classNameToAnalyze = "";
		InterSQLInjectionInfo InterSQLInjectionInfo = fact.get(new InterJIRValue(invoke.invoker, level));

		classNameToAnalyze = invoke.node.owner;
		ClassNode cn = getClassNode(classNameToAnalyze);

		if (cn == null) {
			Type invokeType = Type.getObjectType(invoke.node.owner);
			if (invokeType != null && invokeType.getSort() == Type.OBJECT)
				cn = getClassNode("java.lang.Object");
		}
		if (cn != null) {
			MethodNode me = getMethod(invoke, cn);
			if (me != null) {
				// 成功加载callee方法，进行跨过程分析流程
				// if (invoke.isStatic() || !me.isAbstract()) {// 递归分析
				CFG calleeCFG = me.getCFG();
				// System.out.println("-> step into callee:" +
				// calleeCFG.getMethod().name);
				int calleeLevel = level + 1;
				CallingContext callingContext = new CallingContext(cfg.getMethod(), this.currentLine);
				HashMap<InterJIRValue, InterSQLInjectionInfo> calleeInitFact = fact;
				// 处理 this = b,就是b.f()进入的这个方法，需要把b作为第一个参数传入
				JIRValue actualParam = invoke.invoker;
				InterJIRValue interActualParam = new InterJIRValue(actualParam, level);
				LocalRef formalParamJIRValue;
				InterJIRValue interFormalParam;
				if (calleeCFG.getMethod().localVariables.size() > 0) {
					formalParamJIRValue = new LocalRef((LocalVariableNode) calleeCFG.getMethod().localVariables.get(0));
					interFormalParam = new InterJIRValue(formalParamJIRValue, calleeLevel);
					transferAssignmentStmt(calleeInitFact, interFormalParam.intraJIRValue, interFormalParam.scopeLevel,
							interActualParam.intraJIRValue, interActualParam.scopeLevel);
				}
				// 依次处理各个输入参数 d = c
				List<LocalVariableNode> formalParams = calleeCFG.getMethod().params;
				List<JIRValue> actualParams = invoke.params;
				if (formalParams != null && actualParams != null && actualParams.size() == formalParams.size()) {
					for (int j = 0; j < actualParams.size(); j++) {
						actualParam = actualParams.get(j);
						interActualParam = new InterJIRValue(actualParam, level);
						formalParamJIRValue = new LocalRef(formalParams.get(j));
						interFormalParam = new InterJIRValue(formalParamJIRValue, calleeLevel);
						transferAssignmentStmt(calleeInitFact, interFormalParam.intraJIRValue,
								interFormalParam.scopeLevel, interActualParam.intraJIRValue,
								interActualParam.scopeLevel);
					}
				}

				// 对callee执行递归分析
				InterSQLInjectionDataflowAnalysis calleeAnlysis = new InterSQLInjectionDataflowAnalysis(calleeCFG,
						calleeInitFact, calleeLevel, callingContext);
				try {
					calleeAnlysis.execute();
					// 处理返回参数
					// a = e; a.b = e;
					HashMap<InterJIRValue, InterSQLInjectionInfo> calleeResultFact = calleeAnlysis
							.getResultFact(calleeCFG.getExit());
					filterUnreachableVariables(calleeResultFact);
					fact = calleeResultFact;
				} catch (AnalyzerException e) {
					e.printStackTrace();
				}
			} else {
				JIRValue invoker = invoke.invoker;
				if (invoker != null
						&& "<init>".equalsIgnoreCase(invoke.node.name)
						&& ("java/lang/StringBuilder".equalsIgnoreCase(invoke.node.owner) || "java/lang/StringBuffer"
								.equalsIgnoreCase(invoke.node.owner))) {
					if (fact.get(invoker) != null && invoke.params.size() > 0) {
						JIRValue jirValue = invoke.params.get(0);
						if (((jirValue instanceof LocalRef && !"this"
								.equalsIgnoreCase(((LocalRef) jirValue).nodeRef.name))
								|| jirValue instanceof TempRef || jirValue instanceof FieldRef)
								&& (fact.get(new InterJIRValue(jirValue, level)) != null && isTaint(fact, jirValue))) {
							addFactInvokeStmt(fact, invoker, level, jirValue, level, true);
						}
					}
				}
			}
		} else {
			// e.g.: temp$5.
			JIRValue invoker = invoke.invoker;
			if (invoker != null
					&& "<init>".equalsIgnoreCase(invoke.node.name)
					&& ("java/lang/StringBuilder".equalsIgnoreCase(invoke.node.owner) || "java/lang/StringBuffer"
							.equalsIgnoreCase(invoke.node.owner))) {
				if (fact.get(new InterJIRValue(invoker, level)) != null && invoke.params.size() > 0) {
					JIRValue jirValue = invoke.params.get(0);
					if (((jirValue instanceof LocalRef && !"this".equalsIgnoreCase(((LocalRef) jirValue).nodeRef.name))
							|| jirValue instanceof TempRef || jirValue instanceof FieldRef)
							&& (fact.get(new InterJIRValue(jirValue, level)) != null && isTaint(fact, jirValue))) {
						addFactInvokeStmt(fact, invoker, level, jirValue, level, true);
					}
				}
			}
		}
		return fact;
	}

	private void filterUnreachableVariables(HashMap<InterJIRValue, InterSQLInjectionInfo> calleeResultFact) {
		/*
		 * HashMap<InterJIRValue, InterSQLInjectionInfo> newFact =
		 * this.createFact(calleeResultFact); for (InterJIRValue key :
		 * newFact.keySet()) { if (key.scopeLevel > this.level) {
		 * calleeResultFact.remove(key); } else { Set<InterJIRValue> alias =
		 * newFact.get(key).alias; for (InterJIRValue alia : alias) { if
		 * (alia.scopeLevel > this.level) {
		 * calleeResultFact.get(key).alias.remove(alia); } } } }
		 */
	}

	// 以下是处理InvokeStmt---------------------------

	private void addFactInvokeStmt(HashMap<InterJIRValue, InterSQLInjectionInfo> fact, JIRValue invoker,
			int invokerlevel, JIRValue jirValue, int jirValuelevel, boolean isTaint) {
		InterSQLInjectionPath interSqlInjectionPath = new InterSQLInjectionPath();
		interSqlInjectionPath.setCurrentLine(currentLine);
		interSqlInjectionPath.setValue(new InterJIRValue(jirValue, jirValuelevel));
		List<InterSQLInjectionPath> path = new ArrayList<InterSQLInjectionPath>();
		path.add(interSqlInjectionPath);
		path.addAll((fact.get(new InterJIRValue(jirValue, jirValuelevel))).getPath());
		fact.get(new InterJIRValue(invoker, level)).setPath(path);
		fact.get(new InterJIRValue(invoker, level)).setTaint(isTaint);
	}

	// 以上是处理InvokeStmt------------------------------

	// 以下为处理赋值语句--------------------------------

	private void addFactAll(HashMap<InterJIRValue, InterSQLInjectionInfo> fact, JIRValue leftValue, int leftValueLevel,
			JIRValue rightValue, int rightValueLevel, boolean isTaint) {
		InterSQLInjectionInfo interSQLInjectionInfo = new InterSQLInjectionInfo();
		InterSQLInjectionPath interSQLInjectionPath = new InterSQLInjectionPath();
		interSQLInjectionPath.setCurrentLine(currentLine);
		interSQLInjectionPath.setValue(new InterJIRValue(rightValue, rightValueLevel));
		List<InterSQLInjectionPath> path = new ArrayList<InterSQLInjectionPath>();
		path.add(interSQLInjectionPath);
		if (fact.get(rightValue) != null)
			path.addAll(fact.get(new InterJIRValue(rightValue, rightValueLevel)).getPath());
		interSQLInjectionInfo.setPath(path);
		interSQLInjectionInfo.setTaint(isTaint);
		fact.put(new InterJIRValue(leftValue, leftValueLevel), interSQLInjectionInfo);
	}

	// 以下为处理赋值语句--------------------------------

	private void addFactAll(HashMap<InterJIRValue, InterSQLInjectionInfo> fact, JIRValue leftValue, int leftValueLevel,
			JIRValue rightValue, int rightValueLevel, JIRValue jirValue, boolean isTaint) {
		InterSQLInjectionInfo interSQLInjectionInfo = new InterSQLInjectionInfo();
		InterSQLInjectionPath interSQLInjectionPath = new InterSQLInjectionPath();
		interSQLInjectionPath.setCurrentLine(currentLine);
		interSQLInjectionPath.setValue(new InterJIRValue(rightValue, rightValueLevel));
		List<InterSQLInjectionPath> path = new ArrayList<InterSQLInjectionPath>();
		path.add(interSQLInjectionPath);
		if (fact.get(jirValue) != null)
			path.addAll(fact.get(new InterJIRValue(jirValue, rightValueLevel)).getPath());
		interSQLInjectionInfo.setPath(path);
		interSQLInjectionInfo.setTaint(isTaint);
		fact.put(new InterJIRValue(leftValue, leftValueLevel), interSQLInjectionInfo);
	}

	// 加入一条fact，该fact只和本条语句相关
	private void addFact(HashMap<InterJIRValue, InterSQLInjectionInfo> fact, JIRValue leftValue, int leftValueLevel,
			JIRValue rightValue, int rightValueLevel, boolean isTaint) {
		InterSQLInjectionInfo interSQLInjectionInfo = new InterSQLInjectionInfo();
		InterSQLInjectionPath interSQLInjectionPath = new InterSQLInjectionPath();
		interSQLInjectionPath.setCurrentLine(currentLine);
		interSQLInjectionPath.setValue(new InterJIRValue(rightValue, rightValueLevel));
		List<InterSQLInjectionPath> path = new ArrayList<InterSQLInjectionPath>();
		path.add(interSQLInjectionPath);
		interSQLInjectionInfo.setPath(path);
		interSQLInjectionInfo.setTaint(isTaint);
		fact.put(new InterJIRValue(leftValue, leftValueLevel), interSQLInjectionInfo);
	}

	private void printTransferResult(HashMap<InterJIRValue, InterSQLInjectionInfo> fact) {
		for (InterJIRValue interJIRValue : fact.keySet()) {
			System.out.println(interJIRValue.toString() + "->");
			System.out.println(fact.get(interJIRValue).toString());
		}
	}

	private void initEntryBlock() {
		this.initEntryFact();
		HashMap<InterJIRValue, InterSQLInjectionInfo> result = this.transferVertex(logicalEntryBlock());
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

	private Iterator<Edge> logicalPredecessorEdgeIterator(BasicBlock block) {
		return isForwards ? block.inEdgeIterator() : block.outEdgeIterator();
	}

	private Iterator<Edge> logicalSuccessorEdgeIterator(BasicBlock block) {
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
				HashMap<InterJIRValue, InterSQLInjectionInfo> start = getStartFact(block);
				HashMap<InterJIRValue, InterSQLInjectionInfo> result = getResultFact(block);

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
					HashMap<InterJIRValue, InterSQLInjectionInfo> newStart = new HashMap<InterJIRValue, InterSQLInjectionInfo>();
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
						HashMap<InterJIRValue, InterSQLInjectionInfo> pred = null;
						// ////////////perhaps we need to distinguish different
						// handlers
						if (edge.getType() != EdgeType.HANDLED_EXCEPTION_EDGE) {
							if (this.blockOrder instanceof ReversePostOrder) {
								pred = createFact(getResultFact(logicalPred));
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

					HashMap<InterJIRValue, InterSQLInjectionInfo> newResult = transferVertex(block);

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
			HashMap<InterJIRValue, InterSQLInjectionInfo> start = this.getStartFact(block);

			// result 有问题，但下一个block的start是没有问题的
			HashMap<InterJIRValue, InterSQLInjectionInfo> result = this.getResultFact(block);

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

	public void printCallees() {
		System.out.println("*****************************************");
		System.out.println(this.cfg.getMethod().name);
		System.out.println(this.numIterations);

		for (String callee : InterSQLInjectionDataflowAnalysis.callees) {
			System.out.println(callee);
		}
	}

	public static void main(String[] args) {
		AnalysisFactoryManager.initial();

		try {
			System.setOut(new PrintStream(new FileOutputStream("output.txt")));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		/*
		 * Project project = new Project("bin/"); CodaProperties.isLibExpland =
		 * true; project.addLibPath("lib/"); ClassNodeLoader loader = new
		 * ClassNodeLoader("bin/"); String subjectClassName =
		 * "edu.pku.cn.testcase.TestSQLInjection"; ClassNode cc =
		 * loader.loadClassNode(subjectClassName, 0);
		 */
		Project project = new Project("D:/workspace/java/myeclipse8/webgoat/target/classes/");
		CodaProperties.isLibExpland = true;
		project.addLibPath("D:/workspace/java/myeclipse8/webgoat/lib/");
		ClassNodeLoader loader = new ClassNodeLoader("D:/workspace/java/myeclipse8/webgoat/target/classes/");
		ClassNode cc = loader.loadClassNode("org.owasp.webgoat.lessons.BlindStringSqlInjection", 0);
		InterSQLInjectionDataflowAnalysis.loader = loader;
		for (MethodNode method : cc.methods) {
			// if (method.name.contains("right")) {
			// method.getStmts();
			System.out.println(method.name + method.desc);
			CFG cfg = method.getCFG();
			InterSQLInjectionDataflowAnalysis anlysis = new InterSQLInjectionDataflowAnalysis(cfg);
			try {
				anlysis.execute();
				// anlysis.printCallees();
				anlysis.examineResults();
			} catch (AnalyzerException e) {
				e.printStackTrace();
			}
			// }
		}
	}
}

// end

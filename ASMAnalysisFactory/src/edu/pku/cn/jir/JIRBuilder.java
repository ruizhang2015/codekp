/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-12 ÏÂÎç03:25:41
 * @modifier: a
 * @time 2010-1-12 ÏÂÎç03:25:41
 * @reviewer: a
 * @time 2010-1-12 ÏÂÎç03:25:41
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.jir;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.analysis.BasicInterpreter;
import org.objectweb.asm.tree.analysis.Frame;
import org.objectweb.asm.tree.analysis.SimpleVerifier;

import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.classfile.ClassNodeLoader;
import edu.pku.cn.classfile.FieldNode;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.graph.cfg.BasicBlock;
import edu.pku.cn.graph.cfg.CFG;
import edu.pku.cn.graph.cfg.CFGBuilder;
import edu.pku.cn.ptg.PTGBuilder;
import edu.pku.cn.util.AnalysisFactoryManager;

/**
 * @author zhouzhiyi
 */
public class JIRBuilder {
	private CFG cfg;
	private List<Stmt> stmts;
	public boolean debug = false;
	int tempIndex = 0;

	public void build(MethodNode method) {
		stmts = new ArrayList<Stmt>();
		tempIndex = 0;
		List<FieldNode> cf = method.declaringClass.fields;
		JIRTransfer tran = new JIRTransfer();
		tran.putLocals(method.localVariables);
		tran.putHandles(method.tryCatchBlocks);
		tran.putParams(method.params);
		tran.tempIndex = tempIndex;
		cfg = method.getCFG();
		Frame[] frames = cfg.getFrames();
		int size = method.instructions.size() - 1;
		for (int i = 0; i <= size; i++) {
			Frame postFrame = null;
			if (i < size)
				postFrame = frames[i + 1];
			tran.generateJIR(method.instructions.get(i), frames[i], postFrame, stmts);
		}
		tempIndex = tran.tempIndex;
		if (debug)
			printStmts();
		applyStack(method.maxStack + 1);
		if (labelMap.size() > 0) {
			Iterator<BasicBlock> iter = cfg.blockIterator();
			while (iter.hasNext()) {
				BasicBlock block = iter.next();
				Integer i = labelMap.get(block.getStartInc());
				block.startStmt = i.intValue();
				block.endStmt = labelMap.get(block.getEndInc());
				block.stmts = stmts;
			}
		}
		method.setStmts(stmts);
		cfg.setJIRStmt(stmts);
	}

	private void applyStack(int maxStack) {
		JIRStack stack = new JIRStack(maxStack);
		Iterator<Stmt> iter = stmts.iterator();
		JIRApplyStmtSwitch ss = new JIRApplyStmtSwitch(stack);
		ss.tempIndex = tempIndex;
		while (iter.hasNext()) {
			Stmt stmt = iter.next();
			stmt.apply(ss);
		}
		tempMap = ss.tempMap;
		if (debug)
			printStmts();
		applyMoveSame(ss.tempMap);
	}

	private HashMap<Integer, Integer> labelMap = new HashMap<Integer, Integer>();
	HashMap<JIRValue, JIRValue> tempMap;

	private JIRValue getTemp(JIRValue a) {
		if (a instanceof TempRef && tempMap.containsKey(a))
			return tempMap.get(a);
		return a;
	}

	private boolean caseInvokeExpr(InvokeExpr expr) {
		if (expr.node.owner.equals("java/lang/Object") && expr.node.name.equals("<init>"))
			return true;
		expr.invoker = getTemp(expr.invoker);
		for (int i = 0; i < expr.params.size(); i++) {
			expr.params.set(i, getTemp(expr.params.get(i)));
		}
		return false;
	}

	private void applyMoveSame(HashMap<JIRValue, JIRValue> tempMap) {
		List<Stmt> oldStmts = stmts;
		stmts = new ArrayList<Stmt>();
		int start = 0;
		for (int i = 0; i < oldStmts.size(); i++) {
			Stmt stmt = oldStmts.get(i);
			int end = ((AbstractStmt) stmt).end;
			int index = stmts.size();
			labelMap.put(start, index);
			labelMap.put(end, index);
			if (stmt instanceof LabelStmt) {
				LabelStmt ls = ((LabelStmt) stmt);
				ls.labelIndex = stmts.size();
			} else if (stmt instanceof PopStmt) {
				continue;
			} else if (stmt instanceof AssignStmt) {
				AssignStmt as = (AssignStmt) stmt;
				as.left = getTemp(as.left);
				if (as.left.equals(as.right) || as.isStackOp)
					continue;
				if (as.right instanceof InvokeExpr)
					caseInvokeExpr((InvokeExpr) as.right);
				else if (as.right instanceof TempRef) {
					if (tempMap.containsKey(as.right))
						continue;
				}
			} else if (stmt instanceof InvokeStmt) {
				if (caseInvokeExpr((InvokeExpr) ((InvokeStmt) stmt).invoke))
					continue;
			} else if (stmt instanceof RetStmt) {
				RetStmt a = (RetStmt) stmt;
				a.value = getTemp(a.value);
			} else if (stmt instanceof ReturnStmt) {
				ReturnStmt a = (ReturnStmt) stmt;
				a.value = getTemp(a.value);
			} else if (stmt instanceof ThrowStmt) {
				ThrowStmt a = (ThrowStmt) stmt;
				a.value = getTemp(a.value);
			}
			stmt.setRegion(start, end);
			stmt.setIndex(index);
			stmts.add(stmt);
			start = end + 1;
		}
	}

	private void printStmts() {
		Iterator<Stmt> iter = stmts.iterator();
		int i = 0;
		while (iter.hasNext()) {
			System.out.println(i++ + ": " + iter.next());
		}
		System.err.println("...............................");
	}

	public static void main(String[] args) {
		AnalysisFactoryManager.initial();
		ClassReader cr;
		try {
			ClassNodeLoader loader = new ClassNodeLoader("D:/eclipse/workspace/test-apache-tomcat6.0.10/bin/");
			ClassNode cn = loader.loadClassNode(
					"org.apache.catalina.connector.RequestFacade$GetCookiesPrivilegedAction", 0);
			List methods = cn.methods;
			for (int i = 0; i < methods.size(); ++i) {
				MethodNode method = (MethodNode) methods.get(i);
				if (true && method.name.equals("<init>"))
					if (method.instructions.size() > 0) {
						// Repository.getInstance().setPackageResource(new
						// PackageResource());
						CFGBuilder builder = new CFGBuilder(new BasicInterpreter());// new
																					// CFGBuilder(new
																					// RealInterpreter(cn.fields));
						builder.analyze(cn.name, method);
						CFG cfg = builder.create();
						JIRBuilder bu = new JIRBuilder();
						bu.debug = true;
						bu.build(method);
						bu.printStmts();
						System.out.println();
						PTGBuilder ppg = new PTGBuilder();
						ppg.analyze(method);
						ppg.print(cn.name, method);
					}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

// end

/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author liuxi
 * @time 2010-3-25
 * @modifier: liuxi
 * @time 2010-3-25
 * @reviewer: liuxi
 * @time 2010-3-25
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package autoAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.Type;

import automachine.AutoMachine;
import automachine.AutoMachineException;
import automachine.AutomaUtil;
import automachine.VisitMethodInsnEdge;

import edu.pku.cn.Project;
import edu.pku.cn.analysis.InterValueDataflowAnalysis;
import edu.pku.cn.bugreport.BugInstance;
import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.classfile.ClassNodeLoader;
import edu.pku.cn.classfile.LocalVariableNode;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.detector.Detector;
import edu.pku.cn.detector.JIRDetector;
import edu.pku.cn.graph.cfg.CFG;
import edu.pku.cn.hierarchy.Repository;
import edu.pku.cn.jir.AbstractJIRVisitor;
import edu.pku.cn.jir.AnyNewExpr;
import edu.pku.cn.jir.ArrayRef;
import edu.pku.cn.jir.AssignStmt;
import edu.pku.cn.jir.BinopExpr;
import edu.pku.cn.jir.CastExpr;
import edu.pku.cn.jir.CaughtExceptionRef;
import edu.pku.cn.jir.Constant;
import edu.pku.cn.jir.EnterMonitorStmt;
import edu.pku.cn.jir.ExitMonitorStmt;
import edu.pku.cn.jir.Expr;
import edu.pku.cn.jir.FieldRef;
import edu.pku.cn.jir.GotoStmt;
import edu.pku.cn.jir.IfStmt;
import edu.pku.cn.jir.InvokeExpr;
import edu.pku.cn.jir.InvokeStmt;
import edu.pku.cn.jir.JIRValue;
import edu.pku.cn.jir.JIRVisitor;
import edu.pku.cn.jir.LabelStmt;
import edu.pku.cn.jir.LineStmt;
import edu.pku.cn.jir.LocalRef;
import edu.pku.cn.jir.LookupSwitchStmt;
import edu.pku.cn.jir.NewArrayExpr;
import edu.pku.cn.jir.NewExpr;
import edu.pku.cn.jir.Null;
import edu.pku.cn.jir.Ref;
import edu.pku.cn.jir.RetStmt;
import edu.pku.cn.jir.ReturnStmt;
import edu.pku.cn.jir.TableSwitchStmt;
import edu.pku.cn.jir.TempRef;
import edu.pku.cn.jir.ThrowStmt;
import edu.pku.cn.util.AnalysisFactoryManager;
import edu.pku.cn.util.CodaProperties;
import edu.pku.cn.util.HASelect;

/**
 * inter-procedural called-methods collector
 * 
 * @author guangtai
 * 
 */
public class CalledMethodsCollector implements JIRVisitor {

	public boolean Debug = false;
	int currentLine;
	private int level = 0;
	private int maxLevel = 5;
	static public Set<String> callees = new HashSet<String>();

	public CalledMethodsCollector() {
		super();
	}

	public CalledMethodsCollector(int level) {
		this.level = level;
	}

	void printResult() {
		for (String type : callees) {
			System.out.println(type.toString());
		}
	}

	public void visitParameters(List<LocalVariableNode> params) {

	}

	public void visitStart(String name, String desc, String signature) {

	}

	@Override
	public void visit(AssignStmt as) {
		JIRValue rightValue = as.right;
		if (rightValue instanceof InvokeExpr) {
			analyzeInvokeExpr((InvokeExpr) rightValue, this.level);
		}
	}

	private void analyzeInvokeExpr(InvokeExpr invokeExpr, int level) {
		this.callees.add(invokeExpr.getMethodNodeName());
		if (this.level <= this.maxLevel) {
			String classNameToAnalyze = invokeExpr.node.owner;
			ClassNode cn = getClassNode(classNameToAnalyze);
			if (cn != null) {
				MethodNode me = getMethod(invokeExpr, cn);
				if (me != null) {
					// 成功加载callee方法，进行跨过程分析流程
					if (invokeExpr.isStatic() || !me.isAbstract()) {// 递归分析
						CalledMethodsCollector calleeAnalysis = new CalledMethodsCollector(this.level++);
						me.accept(calleeAnalysis);
					}
				} else {
					System.out.println("无法加载method：" + invokeExpr.getMethodNodeName());
				}
			} else {
				System.out.println("无法加载class：" + classNameToAnalyze);
			}	
		}
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

	private ClassNode getClassNode(String className) {
		Repository cha = Repository.getInstance();
		return cha.findClassNode(className);
	}

	@Override
	public void visit(EnterMonitorStmt as) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ExitMonitorStmt as) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(GotoStmt as) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(IfStmt as) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(InvokeStmt as) {
		// TODO Auto-generated method stub
		this.analyzeInvokeExpr((InvokeExpr) as.invoke, this.level);
	}

	@Override
	public void visit(LookupSwitchStmt as) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(LabelStmt as) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(LineStmt as) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(RetStmt as) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ReturnStmt as) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(TableSwitchStmt as) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ThrowStmt as) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visitEnd() {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {

		AnalysisFactoryManager.initial();

		Project project = new Project("bin/");
		CodaProperties.isLibExpland = true;
		project.addLibPath("lib/");

		try {
			System.setOut(new PrintStream(new FileOutputStream("output.txt")));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ClassNodeLoader loader = new ClassNodeLoader("bin/");
		ClassNode cc = loader.loadClassNode("edu.pku.cn.testcase.TestInterPointsToInfo", 0);

		CalledMethodsCollector calleesCollector = new CalledMethodsCollector();

		for (MethodNode method : cc.methods) {
			calleesCollector.callees.clear();
			method.accept(calleesCollector);
			System.out.println("分析method：" + method.getFullName());
			calleesCollector.printResult();
			System.out.println("");
		}
	}

}

// end

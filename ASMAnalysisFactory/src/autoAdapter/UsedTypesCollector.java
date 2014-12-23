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
import edu.pku.cn.bugreport.BugInstance;
import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.classfile.ClassNodeLoader;
import edu.pku.cn.classfile.LocalVariableNode;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.detector.Detector;
import edu.pku.cn.detector.JIRDetector;
import edu.pku.cn.graph.cfg.CFG;
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
 * automachine java intrenet
 * 
 * @author guangtai
 * 
 */
public class UsedTypesCollector implements JIRVisitor {

	public boolean Debug = false;
	int currentLine;
	Set<String> usedTypes = new HashSet<String>();

	public UsedTypesCollector() {
		super();
	}

	void printResult(){
		for (String type : usedTypes) {
			System.out.println(type.toString());
		}
	}
	
	public void visitParameters(List<LocalVariableNode> params) {
		for (LocalVariableNode node : params) {
			if (node.getType().getSort() >= Type.ARRAY){
				this.usedTypes.add(node.getType().toString());
			}
		}	
	}

	public void visitStart(String name, String desc, String signature) {

	}

	@Override
	public void visit(AssignStmt as) {
		JIRValue leftValue = as.left;
		JIRValue rightValue = as.right;
		analyzeJIRValue(leftValue);
		analyzeJIRValue(rightValue);
	}

	private void analyzeJIRValue(JIRValue value) {
		if (value instanceof AnyNewExpr) {
			// e.g.: a = new B();
			// e.g.: a = new B[]();
			this.usedTypes.add(value.getType().toString());
			
		} else if (value instanceof Constant) {
			// e.g.: a = "string";
			
		} else if (value instanceof Ref) {
			// e.g.: a = b; a = b.c; a = @CaughException; a
			// ="string";
			// a = b[i];

			// e.g.: e=@CaughException
			if (value instanceof CaughtExceptionRef) {
				this.usedTypes.add(value.getType().toString());
			} else if (value instanceof LocalRef ){
				this.usedTypes.add(value.getType().toString());
			} else if (value instanceof FieldRef ) {
				this.usedTypes.add(value.getType().toString());
				this.usedTypes.add(((FieldRef)value).owner.toString());
			} else if (value instanceof ArrayRef ) {
				this.usedTypes.add(value.getType().toString());
			}
			
		} else if (value instanceof Expr ) {
			if (value instanceof InvokeExpr) {
				// a = b.f();
				InvokeExpr invokeExpr = (InvokeExpr)value;
				this.usedTypes.add(invokeExpr.invoker.getType().toString());
				
				if (invokeExpr.invoker instanceof FieldRef){
					this.usedTypes.add(((FieldRef)invokeExpr.invoker).owner);
				}
				
			} else if (value instanceof CastExpr) {
				// e.g.: a = (A)b;
				CastExpr castExpr = (CastExpr) value;
				this.usedTypes.add(castExpr.type.toString());
			} else if (value instanceof BinopExpr) {
				BinopExpr binopExpr = (BinopExpr) value;
				this.analyzeJIRValue(binopExpr.op1);
				this.analyzeJIRValue(binopExpr.op2);
			}
		}
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
		this.analyzeJIRValue(as.condition);
	}

	@Override
	public void visit(InvokeStmt as) {
		// TODO Auto-generated method stub
		this.analyzeJIRValue(as.invoke);
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
		this.analyzeJIRValue(as.value);
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

		UsedTypesCollector typesCollector = new UsedTypesCollector();
		
		for (MethodNode method : cc.methods) {
			method.accept(typesCollector);
		}
		
		typesCollector.printResult();
	}

}

// end

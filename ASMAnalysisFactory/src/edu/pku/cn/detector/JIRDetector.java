/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author liuxi
 * @time 2010-4-1
 * @modifier: liuxi
 * @time 2010-4-1
 * @reviewer: liuxi
 * @time 2010-4-1
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.detector;

import java.util.ArrayList;
import java.util.List;

import edu.pku.cn.classfile.LocalVariableNode;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.jir.AssignStmt;
import edu.pku.cn.jir.EnterMonitorStmt;
import edu.pku.cn.jir.ExitMonitorStmt;
import edu.pku.cn.jir.GotoStmt;
import edu.pku.cn.jir.IfStmt;
import edu.pku.cn.jir.InvokeStmt;
import edu.pku.cn.jir.JIRVisitor;
import edu.pku.cn.jir.LabelStmt;
import edu.pku.cn.jir.LineStmt;
import edu.pku.cn.jir.LookupSwitchStmt;
import edu.pku.cn.jir.RetStmt;
import edu.pku.cn.jir.ReturnStmt;
import edu.pku.cn.jir.TableSwitchStmt;
import edu.pku.cn.jir.ThrowStmt;

/**
 * @author liuxi
 */
public abstract class JIRDetector extends Detector<MethodNode> implements JIRVisitor {
	public void visitParameters(List<LocalVariableNode> params){
		
	}
	public void visitStart(String name,String desc,String signature){
		
	}
	@Override
	public void visit(AssignStmt as) {
		// TODO Auto-generated method stub

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

}

// end

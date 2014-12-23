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
package edu.pku.cn.jir;

import java.util.List;

import edu.pku.cn.classfile.LocalVariableNode;

/**
 * @author liuxi
 */
public interface JIRVisitor {
	public void visitStart(String name,String desc,String signature);
	public void visitParameters(List<LocalVariableNode> params);
	public void visit(AssignStmt as);
	public void visit(EnterMonitorStmt as);
	public void visit(ExitMonitorStmt as);
	public void visit(GotoStmt as);
	public void visit(IfStmt as);
	public void visit(InvokeStmt as);
	public void visit(LookupSwitchStmt as);
	public void visit(LabelStmt as);
	public void visit(LineStmt as);
	public void visit(RetStmt as);
	public void visit(ReturnStmt as);
	public void visit(TableSwitchStmt as);
	public void visit(ThrowStmt as);
    /**
     * Visits the end of the method. This method, which is the last one to be
     * called, is used to inform the visitor that all the annotations and
     * attributes of the method have been visited.
     */
    void visitEnd();
}

// end

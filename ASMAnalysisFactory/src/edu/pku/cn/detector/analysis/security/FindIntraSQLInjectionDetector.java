/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-3-20 锟斤拷锟斤拷10:46:37
 * @modifier: Administrator
 * @time 2009-3-20 锟斤拷锟斤拷10:46:37
 * @reviewer: Administrator
 * @time 2009-3-20 锟斤拷锟斤拷10:46:37
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.detector.analysis.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import edu.pku.cn.analysis.factory.security.IntraSQLIjectionDataflowFactory;
import edu.pku.cn.analysis.sercurity.IntraSQLInjectionDataflowAnalysis;
import edu.pku.cn.analysis.vo.security.SQLInjectionInfo;
import edu.pku.cn.analysis.vo.security.SQLInjectionPath;
import edu.pku.cn.detector.Detector;
import edu.pku.cn.detector.JIRDetector2;
import edu.pku.cn.graph.cfg.BasicBlock;
import edu.pku.cn.jir.AssignStmt;
import edu.pku.cn.jir.InvokeExpr;
import edu.pku.cn.jir.JIRValue;
import edu.pku.cn.jir.LineStmt;
import edu.pku.cn.util.AnalysisFactoryManager;

/**
 * 测试过程内的数据流分析得detector
 *
 * @author wuling
 */
public class FindIntraSQLInjectionDetector extends JIRDetector2 {

	private boolean DEBUG = false;

	private InsnList insns = null;

	private IntraSQLInjectionDataflowAnalysis rvAnalysis = null;
	
	private int currentLine;

	@Override
	public ArrayList<Detector> getInstances() {
		ArrayList<Detector> detectors = new ArrayList<Detector>();
		detectors.add(new FindIntraSQLInjectionDetector());
		return detectors;
	}

	public void visit(AssignStmt as) {
		super.visit(as);
		if (as.right instanceof InvokeExpr)
			visit((InvokeExpr) as.right, as.left);
	}
	
	

	@Override
	public void visit(LineStmt as) {
		super.visit(as);
		this.currentLine  = as.line;
	}

	private void visit(InvokeExpr ie, JIRValue leftValue){
//两种方式获得
//方法一
	String owner  = ie.node.owner;
	String name  = ie.node.name;
	String desc = ie.node.desc;
	//System.out.println("owner: "+owner+"\n name:"+name+"\n desc:"+desc);

	if(isDatabaseSink(owner, name, desc)){
		BasicBlock basicBlock =  this.node.getBlockOfStmt(this.currentIndex);
		//System.out.println(ie.params.get(0));
		HashMap <JIRValue, SQLInjectionInfo> resultFact = rvAnalysis.getResultFact(basicBlock);
		//System.out.println(startFact.get(ie.params.get(0)));
		SQLInjectionInfo sqlInjectionInfo = resultFact.get(ie.params.get(0));
		if (sqlInjectionInfo!=null&&sqlInjectionInfo.isTaint()){
			System.out.println("---------------------------------------");
			System.out.println(this.node.declaringClass.name);
			/*BugInstance instance = new BugInstance(BugInstance.format(getBugPattern(
					"UNINITIALIZED_LOCAL_VARIABLE").getLongDescription(), new String[] { liveAnalysis
					.getLocalVariableNode(i).name }), this.currentLine);
			reportor.report(node.owner, instance);*/
			System.out.println(ie.node.toString()+" currentline: "+currentLine);
			List<SQLInjectionPath> path = sqlInjectionInfo.getPath();
			for (SQLInjectionPath sqlInjectionPath : path) {
				System.out.println(sqlInjectionPath);
			}
		}
//方法二
	}
	}

	public void visitStart(String name, String desc, String signature) {
		super.visitStart(name, desc, signature);
			//已经分析后的analysis，会执行TestIntrapointsToDataflowFactory中的analyze方法，就会执行这个TestIntrapointsToDataflowFactory种的execute方法
			try {
/*				System.out.println("---------------------------------------");
				System.out.println(this.node.declaringClass.name);*/
				rvAnalysis = (IntraSQLInjectionDataflowAnalysis) AnalysisFactoryManager.lookup(IntraSQLIjectionDataflowFactory.NAME)
						.getAnalysis(node.getCFG());
			} catch (AnalyzerException e) {
				e.printStackTrace();
			}
	}

	//与ASM无关的功能函数
	private boolean isPreparedStatementDatabaseSink(String owner, String name,String desc) {
		if (name.equals("prepareStatement") && owner.equals("java/sql/Connection")  && desc.startsWith("(Ljava/lang/String;")) {
			return true;
		}
		return false;
	}

	private boolean isExecuteDatabaseSink(String owner, String name,String desc) {
		if (name.startsWith("execute") && owner.equals("java/sql/Statement") && desc.startsWith("(Ljava/lang/String;")) {
			return true;
		}
		return false;
	}

	private boolean isDatabaseSink(String owner, String name,String desc) {
		return isPreparedStatementDatabaseSink(owner, name, desc) || isExecuteDatabaseSink(owner, name, desc);
	}


	@Override
	public String getClassName() {
		return node.owner;
	}
	

}

// end

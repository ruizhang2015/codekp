/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-6-30 ����01:00:47
 * @modifier: Liuxizhiyi
 * @time 2008-6-30 ����01:00:47
 * @reviewer: Liuxizhiyi
 * @time 2008-6-30 ����01:00:47
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.detector;

import java.util.ArrayList;

import org.objectweb.asm.tree.MemberNode;

import edu.pku.cn.bugreport.BugReporter;
import edu.pku.cn.xml.messages.BugPattern;
import edu.pku.cn.xml.messages.MessageService;

/**
 * 
 * @author Liuxizhiyi
 */
public abstract class Detector<Node extends MemberNode> {
	// /**
	// * visit the class file
	// * @param node
	// */
	// public void visitClass(ClassNode node);

	// protected CFG cfg;
	protected Node node;
	protected edu.pku.cn.xml.messages.Detector detectorMessage;
	protected BugReporter reportor;
	// protected boolean needCFG;
	/**
	 * the name is depended on the type of detector
	 */
	protected String name;

	// protected String className;
	// protected ClassContext context;

	public abstract ArrayList<Detector> getInstances();

	public void setBugReporter(BugReporter reporter) {
		this.reportor = reporter;
	}

	public BugPattern getBugPattern(String name) {
		return detectorMessage.getBugPattern(name);
	}

	// protected void setNeedCFG(boolean need){
	// needCFG=need;
	// }
	//	
	// public boolean isNeedCFG(){
	// return needCFG;
	// }
	// public void setClassContext(ClassContext context){
	// this.context=context;
	// }
	public void setReference(Node node) {
		this.node = node;
	}

	// public void initCFG(MethodNode method,String className){
	// try {
	//			
	// if(needCFG){
	// CFGFactory factory=CFGFactory.newInstance();
	// factory.setOwner(className);
	// factory.setInterpreter(new BasicInterpreter());
	// cfg=factory.getAnalysis(method);
	// }
	//			
	//			
	// // CFGFactory factory=CFGFactory.newInstance();
	// // factory.setOwner(context.getName());
	// // factory.setInterpreter(new BasicInterpreter());
	// // cfg=factory.getAnalysis(method);
	//			
	// } catch (AnalyzerException e) {
	// e.printStackTrace();
	// }
	// }
	public void initBugPattern(String category, String className) {
		detectorMessage = MessageService.getDetector(category, className);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public abstract String getClassName();
	// public String getClassName() {
	// //return this.context.name;
	// return className;
	// }
	//
	// public void setClassName(String name) {
	// this.className = name;
	// }
	public boolean visitJIR(){
		return false;
	}
}

// end

/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-6-22 锟斤拷锟斤拷11:55:34
 * @modifier: Liuxizhiyi
 * @time 2008-6-22 锟斤拷锟斤拷11:55:34
 * @reviewer: Liuxizhiyi
 * @time 2008-6-22 锟斤拷锟斤拷11:55:34
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.classfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import autoAdapter.AMJIRDataflowAnalysis;
import autoAdapter.AMJIRDataflowAnalysis2;
import autoAdapter.AMJIRInterDataflowAnalysis;
import autoAdapter.AMJIRInterDataflowAnalysis2;
import automachine.AutoMachine;

import com.sun.org.apache.bcel.internal.classfile.LocalVariable;

import edu.pku.cn.Project;
import edu.pku.cn.analysis.AnalysisFactory;
import edu.pku.cn.analysis.DataflowAnalysis;
import autoAdapter.DataflowAnalysisDetector;
import edu.pku.cn.analysis.RefactoredDataflow;
import edu.pku.cn.detector.ClassDetector;
import edu.pku.cn.detector.Detector;
import edu.pku.cn.detector.JIRDetector;
import edu.pku.cn.detector.MethodDetector;
import edu.pku.cn.graph.cfg.BasicBlock;
import edu.pku.cn.graph.cfg.CFG;
import edu.pku.cn.graph.cfg.CFGFactory;
import edu.pku.cn.hierarchy.Repository;
import edu.pku.cn.jir.JIRBuilder;
import edu.pku.cn.jir.JIRValue;
import edu.pku.cn.jir.JIRVisitor;
import edu.pku.cn.jir.Stmt;
import edu.pku.cn.ptg.Edge;
import edu.pku.cn.ptg.PTGFactory;
import edu.pku.cn.ptg.PointToGraph;
import edu.pku.cn.util.AnalysisFactoryManager;
import edu.pku.cn.util.OpcodeUtil;

/**
 * 
 * @author Liuxizhiyi
 */
public class MethodNode extends org.objectweb.asm.tree.MethodNode {

	public ClassNode declaringClass;
	public String owner;
	private CFG cfg;
	public boolean needCFG;
	public MethodNodeSumary sumary;
	private PointToGraph ptg;
	protected List<Stmt> stmts;

	public List<Stmt> getStmts() {
		if (stmts == null) {
			CFG cfg=getCFG();
			if(cfg!=null)
				stmts = cfg.getJIRStmt();
			else
				return null;
		}
		return stmts;
	}

	public String getFullName() {
		StringBuilder builder = new StringBuilder();
//		builder.append(this.owner.replace('.', '/') + "::");
		builder.append(this.name).append(this.desc);
		return builder.toString();
	}

	public String getOriginalFullName() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.owner + "::");
		builder.append(this.name).append(this.desc);
		return builder.toString();
	}

	public void setStmts(List<Stmt> stmts) {
		this.stmts = stmts;
	}

	public List<LocalVariableNode> params;
	public Set<Type> returnType = new HashSet<Type>();

	public PointToGraph getPointToGraph() {
		if (ptg == null) {
			try {
				ptg = ((PTGFactory) AnalysisFactoryManager.lookup(PTGFactory.NAME)).getAnalysis(this);
			} catch (AnalyzerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ptg;
	}
	
	public List<String> getExceptions(){
		return (List<String>)this.exceptions;
	}
	
	public ArrayList<ClassNode> getExceptionNodes(){
		ArrayList<ClassNode> exNodes=new ArrayList<ClassNode>();
		for(int i=0;i<this.exceptions.size();i++){
			Repository cha=Repository.getInstance();
			ClassNode exNode=cha.findClassNode((String)this.exceptions.get(i));
			if(exNode!=null)
				exNodes.add(exNode);
			else{
				System.out.println("can't find exception class:"+this.exceptions.get(i));
			}
		}	
		return exNodes;
	}

	/**
	 * Constructs an unitialized {@link MethodNode}.
	 */
	public MethodNode() {
		super();
	}

	/**
	 * Constructs a new {@link MethodNode}.
	 * 
	 * @param access
	 *            the method's access flags (see {@link Opcodes}). This
	 *            parameter also indicates if the method is synthetic and/or
	 *            deprecated.
	 * @param name
	 *            the method's name.
	 * @param desc
	 *            the method's descriptor (see {@link Type}).
	 * @param signature
	 *            the method's signature. May be <tt>null</tt>.
	 * @param exceptions
	 *            the internal names of the method's exception classes (see
	 *            {@link Type#getInternalName() getInternalName}). May be
	 *            <tt>null</tt>.
	 */
	public MethodNode(final int access, final String name, final String desc, final String signature,
			final String[] exceptions) {
		super(access, name, desc, signature, exceptions);
	}

	public void visitLocalVariable(final String name, final String desc, final String signature, final Label start,
			final Label end, final int index) {
		localVariables.add(new LocalVariableNode(name, desc, signature, getLabelNode(start), getLabelNode(end), index));
	}

	/**
	 * Visits the end of the method. This method, which is the last one to be
	 * called, is used to inform the visitor that all the annotations and
	 * attributes of the method have been visited.
	 */
	public void visitEnd() {
		try {
			if (localVariables == null || localVariables.size() == 0)
				return;
			Type[] param = Type.getArgumentTypes(this.desc);
			params = new ArrayList<LocalVariableNode>(param.length);
			int j = 1;
			if (OpcodeUtil.isStatic(access))
				j = 0;
			for (int i = j; i < param.length + j && i < localVariables.size(); i++) {
				params.add((LocalVariableNode) localVariables.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isConstructor() {
		return name.equals("<init>");
	}

	public boolean isStatic() {
		return OpcodeUtil.isStatic(access);
	}

	public boolean isSynthetic() {
		return OpcodeUtil.isSynthetic(access);
	}

	public boolean isNative() {
		return OpcodeUtil.isNative(access);
	}

	public boolean isPrivate() {
		return OpcodeUtil.isPrivate(access);
	}

	public boolean isPublic() {
		return OpcodeUtil.isPublic(access);
	}

	public boolean isProtected() {
		return OpcodeUtil.isProtected(access);
	}

	public boolean isAbstract() {
		return OpcodeUtil.isAbstract(access);
	}

	public boolean isBridge() {
		return OpcodeUtil.isBridge(access);
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(OpcodeUtil.printMethodAccess(access)).append(name).append(" ");
		builder.append(desc).append("{\n");
		if (localVariables != null)
			for (Object node : localVariables) {
				builder.append(node);
			}
		builder.append("MaxStack=").append(maxStack).append("\n");
		builder.append("MaxLocal=").append(maxLocals).append("\n}\n");
		return builder.toString();
	}

	public CFG getCFG() {
		if (cfg == null) {
			CFGFactory factory = (CFGFactory) AnalysisFactoryManager.lookup(CFGFactory.NAME);
			try {
				cfg = factory.getAnalysis(this);
			} catch (AnalyzerException e) {
				System.out.println(this.getFullName());
				System.out.println(e.getStackTrace());
				//e.printStackTrace();
				return null;
			}
		}
		return cfg;
	}

	public <Target, Analysis> Analysis getAnalysis(AnalysisFactory<Target, Analysis> factory, Target object)
			throws AnalyzerException {
		return factory.getAnalysis(object);
	}

	public void accept(Detector detector) {
		if (detector instanceof ClassDetector)
			accept((ClassVisitor) detector);
		else if (detector instanceof MethodDetector)
			accept((MethodVisitor) detector);
		else if (detector instanceof JIRDetector)
			accept((JIRVisitor) detector);
		else if (detector instanceof DataflowAnalysisDetector) {
			acceptDataflowAnalysisDetector((DataflowAnalysisDetector) detector);
		}
	}

	private void acceptDataflowAnalysisDetector(DataflowAnalysisDetector detector) {
		
		//this.getOriginalFullName().startsWith("org.jfree.chart.ChartPanel::paintComponent(Ljava/awt/Graphics;)")
		//内存溢出异常方法过滤
		
		if(this.getOriginalFullName().startsWith(Project.skipBeforeMethod[Project.skipBeforeMethod.length - 1])){
			Project.skipable = false;
			return;
		}
		
		if (Project.skipable == true)
		{
			return;
		}
		
		String fullName = this.getOriginalFullName();
		
		//调试状态开启时，不分析其他方法, 直接定位待调试方法
		if( Project.debugStatus == true && !fullName.contains(Project.debugMethodOriginalFullName)){
			return;
		}
		
		//调试状态时,便于设置断点从该处启动调试过程
		if ( Project.debugStatus == true && fullName.contains(Project.debugMethodOriginalFullName)) {
			System.out.println("Debug Point:");
		}
		
		System.out.println("==>" + this.getOriginalFullName() + "== AutomataFile" + detector.analysis.specificFile);
		
		DataflowAnalysisDetector newDetector = new  DataflowAnalysisDetector( detector.analysis.specificFile);
		
		
//		RefactoredDataflow<HashMap<String, HashSet<AutoMachine>>, AMJIRInterDataflowAnalysis2> dataflow = new RefactoredDataflow<HashMap<String, HashSet<AutoMachine>>, AMJIRInterDataflowAnalysis2>(
//				this.getCFG(), newDetector.analysis);
		
		if (this.getCFG() != null) {
			
			newDetector.analysis.setAnalyzedMethod(this.getCFG());
			newDetector.analysis.setPTAnalysis(this.getCFG());
			newDetector.analysis.setPreconditionsAnalysis(this.getCFG());
			
			try {
				newDetector.analysis.execute();
			} catch (AnalyzerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			newDetector.examineResults();
		}
		
		System.out.println("<== \n");
	}

	public void accept(JIRVisitor jv) {
		jv.visitStart(name, desc, signature);
		jv.visitParameters(this.params);
		Iterator<Stmt> iter = getStmts().iterator();
		while (iter.hasNext()) {
			iter.next().accept(jv);
		}
		jv.visitEnd();
	}
}

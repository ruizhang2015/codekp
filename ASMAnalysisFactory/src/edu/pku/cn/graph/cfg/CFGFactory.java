/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-5-22 ����03:20:02
 * @modifier: Liuxizhiyi
 * @time 2008-5-22 ����03:20:02
 * @reviewer: Liuxizhiyi
 * @time 2008-5-22 ����03:20:02
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.cfg;

import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicInterpreter;
import org.objectweb.asm.tree.analysis.Frame;
import org.objectweb.asm.tree.analysis.SimpleVerifier;

import edu.pku.cn.analysis.AnalysisFactory;
import edu.pku.cn.classfile.MethodNode;

/**
 * 
 * @author Liuxizhiyi
 */
public class CFGFactory extends AnalysisFactory<MethodNode, CFG> {
	public CFGFactory() {
		//builder = new CFGBuilder(new BasicInterpreter());
		//builder=new CFGBuilder(new CFGInterpreter());
		builder=new CFGBuilder(new CFGVerifix());
	}

	public boolean DEBUG = false;
	private CFGBuilder builder;

	public final static String NAME = "CFGFactory";

	// private String owner;
	// private Interpreter interpreter;
	// private static CFGFactory instance=null;
	//	
	// private SimpleCFGBuilder cfgBuilder;
	/**
	 * Constructor
	 */
	public String getName() {
		return NAME;
	}

	/**
	 * @see edu.pku.cn.analysis.AnalysisFactory#analysis(java.lang.Object)
	 */
	@Override
	public CFG analyze(MethodNode target) throws AnalyzerException {
		// CFGBuilder builder=new CFGBuilder(interpreter);
		
		Frame[] frames=builder.analyze(target.owner, target);
		if(frames==null)
			return null;
		
		return builder.create();
	}

	// public CFG analyze(MethodNode target)throws AnalyzerException{
	// CFG cfg = cfgBuilder.getCFG(owner, target);
	//		
	// if(DEBUG){
	// System.out.println(cfg.toString());
	// }
	// return cfg;
	// }

	@Override
	public CFGFactory clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return this;
	}
}

// end

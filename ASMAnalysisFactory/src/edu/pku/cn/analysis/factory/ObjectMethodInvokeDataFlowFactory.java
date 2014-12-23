/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-4-24 ����09:15:39
 * @modifier: Administrator
 * @time 2009-4-24 ����09:15:39
 * @reviewer: Administrator
 * @time 2009-4-24 ����09:15:39
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.analysis.factory;

import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Frame;
import org.objectweb.asm.tree.analysis.Interpreter;

import edu.pku.cn.analysis.AnalysisFactory;
import edu.pku.cn.analysis.IsNullValueDataflowAnalysis;
import edu.pku.cn.analysis.ObjectInvokeMethodAnalysis;
import edu.pku.cn.analysis.SimpleDataflow;
import edu.pku.cn.asm.tree.analysis.HeapObjectInterpreter;
import edu.pku.cn.asm.tree.analysis.IsNullValueInterpreter;
import edu.pku.cn.graph.cfg.CFG;

public class ObjectMethodInvokeDataFlowFactory extends AnalysisFactory<CFG, ObjectInvokeMethodAnalysis> {
	// private Interpreter interpreter;

	private static ObjectMethodInvokeDataFlowFactory factoryTest = new ObjectMethodInvokeDataFlowFactory();

	// private RealValueDataflowAnalysis rvAnalysis = null;

	public static ObjectMethodInvokeDataFlowFactory getInstance() {
		return factoryTest;
	}

	public ObjectMethodInvokeDataFlowFactory() {
	}

	// public ObjectMethodInvokeDataFlowFactory(Interpreter interpreter){
	// this.interpreter = interpreter;
	// }
	//	
	// public void setInterpreter(Interpreter interpreter){
	// this.interpreter = interpreter;
	// }

	// @Override
	// public ObjectInvokeMethodAnalysis getAnalysis(CFG target)throws
	// AnalyzerException{
	// ObjectInvokeMethodAnalysis analysis = analysisMap.get(target);
	// if(analysis == null){
	// analysis = analyze(target);
	// analysisMap.put(target, analysis);
	// }
	// return analysis;
	// }

	@Override
	protected ObjectInvokeMethodAnalysis analyze(CFG target) throws AnalyzerException {
		ObjectInvokeMethodAnalysis analysis = new ObjectInvokeMethodAnalysis(target);
		SimpleDataflow<Frame, ObjectInvokeMethodAnalysis> simpleDataflow = new SimpleDataflow<Frame, ObjectInvokeMethodAnalysis>(
				target, analysis);
		simpleDataflow.execute();

		return analysis;
	}

	@Override
	public ObjectMethodInvokeDataFlowFactory clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return new ObjectMethodInvokeDataFlowFactory();
	}

	public static final String NAME = "ObjectMethodInvokeDataFlowFactory";

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return NAME;
	}
}

// end

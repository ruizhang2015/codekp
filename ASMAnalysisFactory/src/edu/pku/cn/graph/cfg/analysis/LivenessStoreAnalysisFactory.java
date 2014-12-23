///*
// * PKU Confidential
// * 
// * OCO Source Materials
// * 
// * PKU Software Lab
// * @author Liuxizhiyi
// * @time 2008-7-20 ����09:50:46
// * @modifier: Liuxizhiyi
// * @time 2008-7-20 ����09:50:46
// * @reviewer: Liuxizhiyi
// * @time 2008-7-20 ����09:50:46
// * (C) Copyright PKU Software Lab. 2008
// * 
// * The source code for this program is not published or otherwise divested of
// * its trade secrets.
// * 
// */
//package edu.pku.cn.graph.cfg.analysis;
//
//import org.objectweb.asm.tree.MethodNode;
//import org.objectweb.asm.tree.analysis.AnalyzerException;
//
//import edu.pku.cn.analysis.AnalysisFactory;
//
///**
// *
// * @author Liuxizhiyi
// */
//public class LivenessStoreAnalysisFactory extends AnalysisFactory<MethodNode,LivenessStoreAnalysis> {
//
//	String className;
//	public void setClassName(String className){
//		this.className=className;
//	}
//	/** 
//	 * @see edu.pku.cn.analysis.AnalysisFactory#analysis(java.lang.Object)
//	 */
//	@Override
//	public LivenessStoreAnalysis analysis(MethodNode method) throws AnalyzerException {
//		LivenessStoreAnalysis analysis=new LivenessStoreAnalysis();
//        analysis.owner=className;
//        //analysis.analysis(method);
//        return analysis;
//	}
//
//}
//
// // end

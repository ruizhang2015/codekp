///*
// * PKU Confidential
// * 
// * OCO Source Materials
// * 
// * PKU Software Lab
// * @author Administrator
// * @time 2009-1-11 ионГ10:23:32
// * @modifier: Administrator
// * @time 2009-1-11 ионГ10:23:32
// * @reviewer: Administrator
// * @time 2009-1-11 ионГ10:23:32
// * (C) Copyright PKU Software Lab. 2009
// * 
// * The source code for this program is not published or otherwise divested of
// * its trade secrets.
// * 
// */
//package edu.pku.cn.analysis;
//
//import org.objectweb.asm.tree.analysis.AnalyzerException;
//
//public class BasicAnalysisFactory<Target, Analysis> extends AnalysisFactory<Target, Analysis> {
//
//	
//	@Override
//	public Object analysis(Object target) throws AnalyzerException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	@Override
//	public Analysis getAnalysis(Target target)throws AnalyzerException{
//		Analysis analysis = analysisMap.get(target);
//		if(analysis == null){
//			analysis = analyze(target);
//			analysisMap.put(target, analysis);
//		}
//		return analysis;
//	}
//    
//    public static BasicAnalysisFactory getInstance(){
//    	return null;
//    }
//}
//
//// end

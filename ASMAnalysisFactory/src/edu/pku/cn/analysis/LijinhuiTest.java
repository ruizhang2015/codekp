/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-4-24 ����11:00:12
 * @modifier: Administrator
 * @time 2009-4-24 ����11:00:12
 * @reviewer: Administrator
 * @time 2009-4-24 ����11:00:12
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.analysis;

import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Frame;

import autoAdapter.ExecuteMethod;
import autoAdapter.MethodSummaryUtil;
import autoAdapter.MethodSummary;

import edu.pku.cn.analysis.factory.ObjectMethodInvokeDataFlowFactory;
import edu.pku.cn.asm.tree.analysis.HeapObjectInterpreter;
import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.detector.DataflowTestDriver;
import edu.pku.cn.graph.cfg.CFG;

public class LijinhuiTest {

	/**
	 * @param args
	 */
//	public static void main(String[] args) {
//		DataflowTestDriver<Frame, ObjectInvokeMethodAnalysis> driver = new DataflowTestDriver<Frame, ObjectInvokeMethodAnalysis>() {
//
//			public SimpleDataflow<Frame, ObjectInvokeMethodAnalysis> createDataflow(ClassNode cc, MethodNode method) {
//				// TODO Auto-generated method stub
//				try {
//					CFG cfg = cc.getCFGFactory().analyze(method);
//					ObjectMethodInvokeDataFlowFactory factory = cc.getObjectMethodInvokeDataFlowFactory();
//					// ObjectInvokeMethodAnalysis omiAnalysis = factory
//					// .getAnalysis(cfg);
//
//					HeapObjectInterpreter interpreter = new HeapObjectInterpreter(cc.getFieldList());
//					interpreter.setInsns(cfg.getMethod().instructions);
//					ObjectInvokeMethodAnalysis analysis = new ObjectInvokeMethodAnalysis(cfg, interpreter);
//
//					SimpleDataflow<Frame, ObjectInvokeMethodAnalysis> dataflow = new SimpleDataflow<Frame, ObjectInvokeMethodAnalysis>(
//							analysis.cfg, analysis);
//					dataflow.execute();
//
//					System.out.println("----------all execute methods:-----------");
//					for (ExecuteMethod m : analysis.allExecuteMethods) {
//						System.out.println(m);
//					}
//					// int a=1/0;
//					System.out.println("--------summary  ------------");
//					System.out.println(analysis.getCfg().getOwner() + "." + analysis.getCfg().getMethod().name
//							+ analysis.getCfg().getMethod().desc);
//					MethodSummary ms = MethodSummaryUtil.extractSummary(analysis);
//
//					if (ms != null) {
//						MethodSummaryUtil.printSummary(ms);
//					}
//					return dataflow;
//				} catch (AnalyzerException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				return null;
//			}
//
//			@Override
//			public void examineResults(SimpleDataflow<Frame, ObjectInvokeMethodAnalysis> dataflow) {
//				// TODO Auto-generated method stub
//				// Frame[] facts = dataflow.getAnalysis().facts;
//				// for (int i = 0; i < facts.length; i++) {
//				// System.out.println(i + "   " + facts[i]);
//				// }
//			}
//
//			@Override
//			public SimpleDataflow<Frame, ObjectInvokeMethodAnalysis> createDataflow(ClassNode cc,
//					edu.pku.cn.classfile.MethodNode method) {
//				// TODO Auto-generated method stub
//				return null;
//			}
//		};
//		driver.execute("TestFileInputStreamOpenClose");
//		// driver.execute("FileInputStream");

//	}

}

// end

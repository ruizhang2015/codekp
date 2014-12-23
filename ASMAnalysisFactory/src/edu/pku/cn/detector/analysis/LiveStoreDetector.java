///*
// * PKU Confidential
// * 
// * OCO Source Materials
// * 
// * PKU Software Lab
// * @author Liuxizhiyi
// * @time 2008-7-20 ����09:26:15
// * @modifier: Liuxizhiyi
// * @time 2008-7-20 ����09:26:15
// * @reviewer: Liuxizhiyi
// * @time 2008-7-20 ����09:26:15
// * (C) Copyright PKU Software Lab. 2008
// * 
// * The source code for this program is not published or otherwise divested of
// * its trade secrets.
// * 
// */
//package edu.pku.cn.detector.analysis;
//
//import java.util.ArrayList;
//import java.util.BitSet;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import org.objectweb.asm.MethodVisitor;
//import org.objectweb.asm.tree.MethodNode;
//import org.objectweb.asm.tree.analysis.AnalyzerException;
//
//import edu.pku.cn.bugreport.BugInstance;
//import edu.pku.cn.detector.ClassDetector;
//import edu.pku.cn.detector.Detector;
//import edu.pku.cn.graph.cfg.analysis.LivenessStoreAnalysis;
//import edu.pku.cn.graph.cfg.analysis.LivenessStoreAnalysisFactory;
//import edu.pku.cn.graph.cfg.analysis.LivenessStoreResult;
//
///**
// * 
// * @author Liuxizhiyi
// */
//public class LiveStoreDetector extends ClassDetector {
//	public LiveStoreDetector(){
//		this.needCFG=true;
//	}
//	/** 
//	 * @see org.objectweb.asm.ClassVisitor#visit(int, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String[])
//	 */
////	@Override
////	public void visit(int version, int access, String name, String signature,
////			String superName, String[] interfaces) {
////		System.out.println(name);
////	}
//	/**
//	 * @see org.objectweb.asm.ClassVisitor#visitMethod(int, java.lang.String,
//	 *      java.lang.String, java.lang.String, java.lang.String[])
//	 */
//	@Override
//	public MethodVisitor visitMethod(int access, String name, String desc,
//			String signature, String[] exceptions) {
//		LivenessStoreAnalysisFactory factory = new LivenessStoreAnalysisFactory();
//		factory.setClassName(context.getName());
//		try {
//			MethodNode method = context.getMethod(name);
//			LivenessStoreAnalysis analysis = factory.getAnalysis(method);
//
//			if (method.localVariables != null&& method.localVariables.size() > 0)
//				if (method.instructions.size() > 0) {
//					//System.out.println("DFS result:" + method.name);
//
//					LivenessStoreResult result = analysis.analysis(method);
//					Iterator<Entry<Integer, Map<Integer, BitSet>>> entry = result.getDefOutOfBlock().entrySet().iterator();
//					while (entry.hasNext()) {
//						Entry<Integer, Map<Integer, BitSet>> node = entry.next();
//						if (node.getValue().size() > 0) {
//							BitSet out = result.getOut().get(node.getKey());
//							Iterator<Entry<Integer, BitSet>> defIter = node.getValue().entrySet().iterator();
//							while (defIter.hasNext()) {
//								Entry<Integer, BitSet> defOut = defIter.next();
//								if (out.get(defOut.getKey()) == false) {
//									BitSet temp = defOut.getValue();
//									for (int j = temp.nextSetBit(0); j >= 0; j = temp.nextSetBit(j + 1)) {
//										reportor.report(context.getName(), new BugInstance(
//												BugInstance.format(getBugPattern("LIVESTORE").getLongDescription(),
//														new String[]{"local variable "+
//													result.getLocalVariableTable().getNode(defOut.getKey()).name
//													+" has been stored but never read"}),
//														result.getLineNumberTable().getLine(j)
//												));
//									}
//								}
//							}
//						}
//					}
//				}
//
//		} catch (AnalyzerException e) {
//			// e.printStackTrace();
//		}
//		return null;
//	}
//
//	@Override
//	public ArrayList<Detector> getInstances() {
//		// TODO Auto-generated method stub
//
//		ArrayList<Detector> detectors = new ArrayList<Detector>();
//		detectors.add(new LiveStoreDetector());
//		return detectors;
//	}
//}
//
// // end

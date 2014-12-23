/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author liuxi
 * @time 2010-3-26
 * @modifier: liuxi
 * @time 2010-3-26
 * @reviewer: liuxi
 * @time 2010-3-26
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.callgraph;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;

import org.objectweb.asm.ClassReader;

import edu.pku.cn.Project;
import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.classfile.ClassNodeLoader;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.util.CodaProperties;
import edu.pku.cn.util.Timer;

/**
 * @author liuxi
 */
public class CallGraphTest {
//	public static void main(String[] args){
//		Object key = new Object();
//		Timer.start(key);
//		   String name="tomcatLib";
//	 		Project project = new Project("D:/eclipse/workspace/asm3/output/eclipse/");
//	 	CodaProperties.isLibExpland=true;
//		project.addLibPath("D:/eclipse/workspace/asm3/test/lib/");
//		project.addLibPath("D:/eclipse/plugins/org.junit_3.8.2.v20090203-1005/junit.jar");
//		ClassReader cr;
//		try {
//			ClassNodeLoader loader=new ClassNodeLoader("D:/eclipse/workspace/asm3/output/eclipse/");
//			ClassNode cn =loader.loadClassNode("Analysis",0); 			//loader.loadClassNode("edu.pku.cn.graph.cfg.test.LiveStoreTest");
//			List methods = cn.methods;
//			for (int i = 0; i < methods.size(); ++i) {
//				MethodNode method = (MethodNode) methods.get(i);
//				if (true&&method.name.equals("main"))
//					if (method.instructions.size() > 0) {
//						CHACallGraphBuilder builder=new CHACallGraphBuilder();
//						CallGraph call=builder.analyze(method);
//						System.out.println("*****************************");
//						FileOutputStream fs=new FileOutputStream(new File(name+"cha.txt"));
//						PrintWriter pw=new PrintWriter(fs);
//						pw.write(call.toString());
//						pw.write("Edges:"+call.edges.size()+"\n");
//						pw.write("All Edges:"+call.getEdgeSize()+"\n");
//						pw.write("Nodes:"+call.methodToEdge.keySet().size());
//						pw.close();
//						RTACallGraphBuilder rta=new RTACallGraphBuilder();
//						call=rta.analyze(call, method);
//						System.out.println("*****************************");
//						fs=new FileOutputStream(new File(name+"rta.txt"));
//						pw=new PrintWriter(fs);
//						pw.write(call.toString());
//						pw.write("Edges:"+call.edges.size()+"\n");
//						pw.write("All Edges:"+call.getEdgeSize()+"\n");
//						pw.write("Nodes:"+call.methodToEdge.keySet().size()+"\n");
//						pw.write(" takes time:" + Timer.stop(key) + "ms");
//						pw.close();
//						System.out.println("Edges:"+call.edges.size());
//						System.out.println("Nodes:"+call.methodToEdge.keySet().size());
//						
//					}
//			}
//			//System.out.println( " takes time:" + Timer.stop(key) + "ms");
//		}catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	public static void main(String[] args){
		Object key = new Object();
		Timer.start(key);
		   String name="soot";
	 		Project project = new Project("D:\\eclipseworkspace\\SootNewTry\\bin\\");
	 	CodaProperties.isLibExpland=true;
		project.addLibPath("D:\\eclipseworkspace\\SootNewTry\\lib\\");
		
		ClassReader cr;
		try {
			ClassNodeLoader loader=new ClassNodeLoader("D:\\eclipseworkspace\\SootNewTry\\bin\\");
			ClassNode cn =loader.loadClassNode("lgt.testCases.TestFileInputStreamOpenClose",0); 			//loader.loadClassNode("edu.pku.cn.graph.cfg.test.LiveStoreTest");
			List methods = cn.methods;
			for (int i = 0; i < methods.size(); ++i) {
				MethodNode method = (MethodNode) methods.get(i);
				if (true&&method.name.equals("main"))
					if (method.instructions.size() > 0) {
						CHACallGraphBuilder builder=new CHACallGraphBuilder();
						CallGraph call=builder.analyze(method);
						System.out.println("*****************************");
						FileOutputStream fs=new FileOutputStream(new File(name+"cha.txt"));
						PrintWriter pw=new PrintWriter(fs);
						pw.write(call.toString());
						pw.write("Edges:"+call.edges.size()+"\n");
						pw.write("All Edges:"+call.getEdgeSize()+"\n");
						pw.write("Nodes:"+call.methodToEdge.keySet().size());
						pw.close();
						RTACallGraphBuilder rta=new RTACallGraphBuilder();
						call=rta.analyze(call, method);
						System.out.println("*****************************");
						fs=new FileOutputStream(new File(name+"rta.txt"));
						pw=new PrintWriter(fs);
						pw.write(call.toString());
						pw.write("Edges:"+call.edges.size()+"\n");
						pw.write("All Edges:"+call.getEdgeSize()+"\n");
						pw.write("Nodes:"+call.methodToEdge.keySet().size()+"\n");
						pw.write(" takes time:" + Timer.stop(key) + "ms");
						pw.close();
						System.out.println("Edges:"+call.edges.size());
						System.out.println("Nodes:"+call.methodToEdge.keySet().size());
						
					}
			}
			//System.out.println( " takes time:" + Timer.stop(key) + "ms");
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}

// end

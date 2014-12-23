/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-5-29 ����11:24:41
 * @modifier: Liuxizhiyi
 * @time 2008-5-29 ����11:24:41
 * @reviewer: Liuxizhiyi
 * @time 2008-5-29 ����11:24:41
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.cfg.test;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;

import junit.framework.TestCase;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.BasicInterpreter;
import org.objectweb.asm.tree.analysis.Value;

import edu.pku.cn.asm.tree.analysis.RealValue;
import edu.pku.cn.graph.cfg.BasicBlock;
import edu.pku.cn.graph.cfg.CFG;
import edu.pku.cn.graph.cfg.CFGBuilder;
import edu.pku.cn.graph.cfg.Edge;
import edu.pku.cn.graph.print.BasicGraphPrinter;
import edu.pku.cn.graph.visit.BFS;
import edu.pku.cn.graph.visit.GraphVisitor;

/**
 * 
 * @author Liuxizhiyi
 */
public class BFSCFGtest extends TestCase {
	String text = "aa";

	public void testRem() {
		double i = Double.NaN;
		if (Double.isNaN(i)) {
			RealValue<Float> r1 = new RealValue<Float>(Type.FLOAT_TYPE, 0.12f);
			try {
				Object v = Array.newInstance(Class.forName("java.lang.Float"), 2);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	class visitors extends GraphVisitor<CFG, Edge, BasicBlock> {
		// public visitors(String a,int b){
		// a=text;
		// b=1;
		// }
		public void visitVertex(BasicBlock vertex) {
			System.out.println("Vertex:" + vertex.getLabel());
			System.out.println(text);
		}
	}

	public void testCFGDFS() {
		ClassReader cr;
		try {
			cr = new ClassReader("edu.pku.cn.graph.cfg.test.CFGBuilderTest");
			ClassNode cn = new ClassNode();
			cr.accept(cn, ClassReader.SKIP_DEBUG);

			List<MethodNode> methods = cn.methods;
			for (int i = 0; i < methods.size(); ++i) {
				MethodNode method = methods.get(i);
				if (method.name.equals("testa"))
					if (method.instructions.size() > 0) {
						CFGBuilder builder = new CFGBuilder(new BasicInterpreter());
						builder.analyze(cn.name, method);
						CFG cfg = builder.create();
						BasicGraphPrinter<Edge, BasicBlock> printer = new BasicGraphPrinter<Edge, BasicBlock>(cfg);
						printer.print(System.out);
						System.out.println("DFS result");
						BFS<CFG, Edge, BasicBlock> dfs = new BFS<CFG, Edge, BasicBlock>(cfg);
						visitors vi = new visitors();
						dfs.accept(vi);
					}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

// end

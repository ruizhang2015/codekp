/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-5-29 ����12:25:04
 * @modifier: Liuxizhiyi
 * @time 2008-5-29 ����12:25:04
 * @reviewer: Liuxizhiyi
 * @time 2008-5-29 ����12:25:04
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.cfg.test;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.BasicInterpreter;

import edu.pku.cn.graph.cfg.BasicBlock;
import edu.pku.cn.graph.cfg.CFG;
import edu.pku.cn.graph.cfg.CFGBuilder;
import edu.pku.cn.graph.cfg.Edge;
import edu.pku.cn.graph.cfg.analysis.ReachingAnalysis;
import edu.pku.cn.graph.visit.DFS;
import edu.pku.cn.graph.visit.GraphVisitor;
import edu.pku.cn.graph.visit.ReversePostOrder;

/**
 * 
 * @author Liuxizhiyi
 */
public class DFStest extends TestCase {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		// int i=111111;
		// try{
		// while(true){
		// int j=i;
		// i=3/0;
		// }
		// }
		// catch(Exception e){
		//
		// i=3;
		// }
		int i = 0;
		if (i > 0) {
			i++;
		} else {
			i--;
		}
		i++;
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	public void testCFGDFS() {
		class visitors extends GraphVisitor<CFG, Edge, BasicBlock> {
			public int postOrder[];
			int count = 1;
			int size;

			public visitors(int size) {
				postOrder = new int[size];
				this.size = size;
			}

			public void visitVertex(BasicBlock vertex) {
				System.out.print(vertex.getLabel() + ",");
				postOrder[vertex.getLabel()] = size - count++;
			}

			public void visitEdge(Edge edge) {
				visit(edge);
			}

			private void visit(Edge edge) {
				// System.out.println("from "+edge.getSource().getLabel()+" to "+edge.getTarget().getLabel());
			}

			public void visitCircleEdge(Edge edge) {
				visit(edge);
			}

			public void visitCrossEdge(Edge edge) {
				visit(edge);
			}
		}
		ClassReader cr;
		try {
			cr = new ClassReader("edu.pku.cn.graph.cfg.test.CFGBuilderTest");
			ClassNode cn = new ClassNode();
			cr.accept(cn, ClassReader.SKIP_DEBUG);

			List<MethodNode> methods = cn.methods;
			for (int i = 0; i < methods.size(); ++i) {
				MethodNode method = methods.get(i);
				if (method.name.equals("testNull"))
					if (method.instructions.size() > 0) {
						CFGBuilder builder = new CFGBuilder(new BasicInterpreter());
						builder.analyze(cn.name, method);
						CFG cfg = builder.create();
						// BasicGraphPrinter<Edge,BasicBlock> printer=new
						// BasicGraphPrinter<Edge, BasicBlock>(cfg);
						// printer.print(System.out);
						System.out.println("DFS result:" + method.name);
						// DFS<CFG,Edge,BasicBlock> dfs=new
						// DFS<CFG,Edge,BasicBlock>(cfg);
						// dfs.accept1(new visitors());
						System.out.println();
						DFS<CFG, Edge, BasicBlock> pdfs = new DFS<CFG, Edge, BasicBlock>(cfg, true);
						visitors visitor = new visitors(cfg.getVerticeSize());
						pdfs.accept(visitor);
						System.out.println();
						Iterator<BasicBlock> iter = cfg.vertexIterator();

						while (iter.hasNext()) {
							int block = iter.next().getLabel();
							System.out.print(block + ":" + visitor.postOrder[block] + " ");
						}
						System.out.println("FindBugs' order");
						DFS<CFG, Edge, BasicBlock> dfs = new DFS<CFG, Edge, BasicBlock>(cfg);
						dfs.myaccept(new GraphVisitor<CFG, Edge, BasicBlock>() {
						});
						ReversePostOrder order = new ReversePostOrder(cfg);
						iter = order.blockIterator();
						while (iter.hasNext()) {
							BasicBlock block = iter.next();
							System.out.print(block.getLabel() + ",");
						}

						ReachingAnalysis analysis = new ReachingAnalysis(cfg);
						analysis.doAnalysis();
					}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

// end

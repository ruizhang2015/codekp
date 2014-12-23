/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-6-2 ����03:30:14
 * @modifier: Liuxizhiyi
 * @time 2008-6-2 ����03:30:14
 * @reviewer: Liuxizhiyi
 * @time 2008-6-2 ����03:30:14
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.cfg.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.BasicInterpreter;

import edu.pku.cn.graph.cfg.BasicBlock;
import edu.pku.cn.graph.cfg.CFG;
import edu.pku.cn.graph.cfg.CFGBuilder;
import edu.pku.cn.graph.cfg.Edge;
import edu.pku.cn.graph.print.BasicGraphPrinter;
import edu.pku.cn.graph.visit.DFS;
import edu.pku.cn.graph.visit.GraphVisitor;
import edu.pku.cn.graph.visit.ReverseTopologic;

/**
 * 
 * @author Liuxizhiyi
 */
public class ReverseTopologicTest {

	/**
	 * Test method for
	 * {@link edu.pku.cn.graph.visit.AbstractTopologic#accept(edu.pku.cn.graph.visit.GraphVisitor)}
	 * .
	 */
	@Test
	public void testAccept() {
		class visitors extends GraphVisitor<CFG, Edge, BasicBlock> {
			public void visitVertex(BasicBlock vertex) {
				System.out.println("Vertex:" + vertex.getLabel());
			}
		}
		ClassReader cr;
		try {
			cr = new ClassReader("edu.pku.cn.graph.cfg.test.DFStest");
			ClassNode cn = new ClassNode();
			cr.accept(cn, 0);

			List<MethodNode> methods = cn.methods;
			for (int i = 0; i < methods.size(); ++i) {
				MethodNode method = methods.get(i);
				if (method.name.equals("testCFGDFS"))
					if (method.instructions.size() > 0) {
						CFGBuilder builder = new CFGBuilder(new BasicInterpreter());
						builder.analyze(cn.name, method);
						CFG cfg = builder.create();
						BasicGraphPrinter<Edge, BasicBlock> printer = new BasicGraphPrinter<Edge, BasicBlock>(cfg);
						printer.print(System.out);
						System.out.println("DFS result:" + method.name);
						DFS<CFG, Edge, BasicBlock> dfs = new DFS<CFG, Edge, BasicBlock>(cfg);
						dfs.accept(new visitors());
						System.out.println("Topologic result:" + method.name);
						ReverseTopologic<CFG, Edge, BasicBlock> topologic = new ReverseTopologic<CFG, Edge, BasicBlock>(
								cfg);
						topologic.accept(new visitors());
						System.out.println();
					}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

// end

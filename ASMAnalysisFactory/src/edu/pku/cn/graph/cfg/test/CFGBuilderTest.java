/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-5-26 ����03:18:41
 * @modifier: Liuxizhiyi
 * @time 2008-5-26 ����03:18:41
 * @reviewer: Liuxizhiyi
 * @time 2008-5-26 ����03:18:41
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.cfg.test;

import java.io.IOException;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.analysis.BasicInterpreter;

import edu.pku.cn.asm.tree.analysis.RealInterpreter;
import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.classfile.ClassNodeLoader;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.classfile.PackageResource;
import edu.pku.cn.graph.cfg.BasicBlock;
import edu.pku.cn.graph.cfg.CFG;
import edu.pku.cn.graph.cfg.CFGBuilder;
import edu.pku.cn.graph.cfg.Edge;
import edu.pku.cn.graph.print.BasicGraphPrinter;
import edu.pku.cn.graph.visit.DFS;
import edu.pku.cn.graph.visit.GraphVisitor;
import edu.pku.cn.graph.visit.ReverseDFS;
import edu.pku.cn.hierarchy.Repository;

import junit.framework.TestCase;

/**
 * 
 * @author Liuxizhiyi
 */
public class CFGBuilderTest extends TestCase {
	Object statc = "a";

	public CFGBuilderTest(int i) {
	}

	public CFGBuilderTest() {

	}

	public Object testNull(Object point, Object p) {
		Object a = statc;
		int i = 0;
		for (i = 0; i < 10; i++) {
			i = i + 1;
			Object c = new Object();
			for (int j = 0; j < 10; j++) {
				j = i - 1;
				c.toString();
			}
		}

		while (a != null && i == 0) {
			if (i == 1)
				continue;
			a.toString();
			if (i > 0)
				break;
			i++;
		}
		do {
			if (i == 1)
				break;
			a.hashCode();
			if (i == 1)
				continue;
			i++;
		} while (a != null);
		switch (i) {
		case 0:
			i++;
			break;
		case 1:
			i++;
			break;
		default:
			i++;
		}
		if (i == 0) {
			i--;
		} else if (i == 1) {
			i++;
		} else {
			i = 3;
		}
		// Object[] as=new Object[]{null,null,null,new
		// String("hello"),null,null};
		// Object[] as=new Object[6];
		// for(int i=0;i<as.length;i++){
		// if(i%2==0)
		// as[i]=new Object();
		// }
		// as[2]=new String();
		// as[3]=null;
		// as[2]=as[3];
		// a=as[1];
		// Object b=as[2];
		// a=null;
		// c=a.toString();
		// if(a==null){
		// a=null;
		// }
		// else if(b==null) {
		// a=b;
		// }
		// else{
		// a=c;
		// }
		return a;
	}

	public void testa() {
		int i = 0;
		i = i / 0;
		try {
			i = -i;
			i++;
			i = i / 0;
		} catch (ArithmeticException e) {
			i = i / 0;
		} catch (RuntimeException e) {
			i++;
		} catch (Exception e) {
			i++;
		} finally {
			i += 2;
		}
		System.out.println(i);
		i++;
	}

	public void testJSR() {
		ClassReader cr;
		try {
			cr = new ClassReader("Clown");
			ClassNode cn = new ClassNode();
			cr.accept(cn, ClassReader.SKIP_DEBUG);

			List methods = cn.methods;
			for (int i = 0; i < methods.size(); ++i) {
				MethodNode method = (MethodNode) methods.get(i);
				if (method.name.equals("hopAround"))

					if (method.instructions.size() > 0) {
						CFGBuilder builder = new CFGBuilder(new BasicInterpreter());
						builder.analyze(cn.name, method);
						CFG cfg = builder.create();
						BasicGraphPrinter<Edge, BasicBlock> printer = new BasicGraphPrinter<Edge, BasicBlock>(cfg);
						printer.print(System.out);
						DFS<CFG, Edge, BasicBlock> dfs = new DFS<CFG, Edge, BasicBlock>(cfg);
						dfs.accept(cfg.new CFGVisitor());
						System.out.println();
						ReverseDFS<CFG, Edge, BasicBlock> rdfs = new ReverseDFS<CFG, Edge, BasicBlock>(cfg);
						rdfs.accept(cfg.new CFGVisitor());
					}
			}
			Object o = new Object();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			cr = null;
			cr.getClassName();
		}
	}

	/**
	 * Test method for
	 * {@link edu.pku.cn.graph.cfg.CFGBuilder#analyze(java.lang.String, org.objectweb.asm.tree.MethodNode)}
	 * .
	 */
	public void testAnalyze() {

		ClassReader cr;
		try {
			ClassNodeLoader loader=new ClassNodeLoader("D:/eclipse/workspace/ASMAnalysisFactory/bin/");
			//cr = new ClassReader("edu.pku.cn.graph.cfg.test.LiveStoreTest");
			ClassNode cn = loader.loadClassNode("edu.pku.cn.graph.cfg.test.LiveStoreTest");
			//cr.accept(cn, 0);

			List methods = cn.methods;
			for (int i = 0; i < methods.size(); ++i) {
				MethodNode method = (MethodNode) methods.get(i);
				if (true&&method.name.equals("getCommonSuperClass"))
					if (method.instructions.size() > 0) {
						Repository.getInstance().setPackageResource(new PackageResource());
						CFGBuilder builder = new CFGBuilder(new BasicInterpreter());//new CFGBuilder(new RealInterpreter(cn.fields));
						builder.analyze(cn.name, method);
						CFG cfg = builder.create();
						// Topologic<CFG, Edge, BasicBlock> dfs=new
						// Topologic<CFG,Edge,BasicBlock>(cfg);
						// dfs.accept(new vistors());
						// dfs.accept(new vistors());
						BasicGraphPrinter<Edge, BasicBlock> printer = new BasicGraphPrinter<Edge, BasicBlock>(cfg);
						printer.print(System.out);
					}
			}
			Object o = null;
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void testEx() {
		try {
			int i = 0;
			i = 9 / i;
			i++;
		} catch (ArithmeticException e) {
			Object t = null;
			t.toString();
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Object t = null;
			t.toString();
		}
	}

	public static void main(String[] args) {

		CFGBuilderTest test = new CFGBuilderTest();
		test.testAnalyze();
	}
}

// end

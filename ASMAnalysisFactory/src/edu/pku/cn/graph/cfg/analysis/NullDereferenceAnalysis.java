/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-5-28 ����03:28:23
 * @modifier: Liuxizhiyi
 * @time 2008-5-28 ����03:28:23
 * @reviewer: Liuxizhiyi
 * @time 2008-5-28 ����03:28:23
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.cfg.analysis;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import edu.pku.cn.asm.tree.analysis.RealInterpreter;
import edu.pku.cn.graph.cfg.BasicBlock;
import edu.pku.cn.graph.cfg.CFG;
import edu.pku.cn.graph.cfg.CFGFactory;
import edu.pku.cn.graph.cfg.Edge;
import edu.pku.cn.graph.print.BasicGraphPrinter;
import edu.pku.cn.graph.visit.Topologic;

/**
 * 
 * @author Liuxizhiyi
 */
public class NullDereferenceAnalysis {
	public void analysis(ClassNode cn) throws AnalyzerException {
		// here may be replace by some factory share field
		CFGFactory cfgFactory = CFGFactory.newInstance();
		// end
		cfgFactory.setOwner(cn.name);

		List<MethodNode> methods = cn.methods;
		List<FieldNode> fields = cn.fields;
		RealInterpreter realInterpreter = new RealInterpreter(fields);
		cfgFactory.setInterpreter(realInterpreter);
		Topologic<CFG, Edge, BasicBlock> topologic = new Topologic<CFG, Edge, BasicBlock>();

		// ��ʼ��Field
		for (int i = 0; i < methods.size(); i++) {
			MethodNode method = methods.get(i);
			if (method.name.equals("<clinit>")) {
				cfgFactory.getAnalysis(method);
			}
			if (method.name.equals("<init>")) {
				cfgFactory.getAnalysis(method);
				break;
			}
		}
		for (int i = 0; i < methods.size(); i++) {
			MethodNode method = methods.get(i);
			if (method.name.contains("init"))
				continue;
			System.out.println(method.name);
			if (method.name.equals("intra0") == false)
				continue;
			CFG cfg = cfgFactory.getAnalysis(method);
			topologic.setGraph(cfg);

			BasicGraphPrinter<Edge, BasicBlock> printer = new BasicGraphPrinter<Edge, BasicBlock>(cfg);
			printer.print(System.out);

			NullDereferenceVisitor visitor = new NullDereferenceVisitor(cfg, method);
			visitor.setVisitAtom(true);
			// topologic.accept(visitor);
		}
	}

	public static void main(String[] args) {
		ClassReader cr;
		try {
			// cr = new ClassReader("edu.pku.cn.graph.cfg.test.CFGBuilderTest");
			// cr=new ClassReader("TestNullDeference");
			InputStream in = new FileInputStream(new File(
					"C:\\eclipse\\workspace\\integratedFindBugs\\bin\\invokerForFindBugs\\FInvoker.class"));
			cr = new ClassReader(in);
			ClassNode cn = new ClassNode();
			cr.accept(cn, 0);

			NullDereferenceAnalysis analysis = new NullDereferenceAnalysis();
			analysis.analysis(cn);
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

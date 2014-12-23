import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.tree.InsnList;

import edu.pku.cn.Project;
import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.classfile.ClassNodeLoader;
import edu.pku.cn.classfile.LocalVariableNode;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.graph.cfg.BasicBlock;
import edu.pku.cn.jir.Stmt;
import edu.pku.cn.ptg.Edge;
import edu.pku.cn.ptg.PointToGraph;
import edu.pku.cn.util.AnalysisFactoryManager;
import edu.pku.cn.util.CodaProperties;

/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2010-5-20
 * @modifier: Administrator
 * @time 2010-5-20
 * @reviewer: Administrator
 * @time 2010-5-20
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
/**
 * @author Administrator
 */
public class OutputStmt {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		AnalysisFactoryManager.initial();
		
//		String projectPath = "D:/eclipseworkspace/CODA20110114/bin/";
//		String classFileName = "edu.pku.cn.testcase.TestFileInputStreamOpenClose";
//		String methodName = "testFileInputStreamOpenClose";
		
		String projectPath = "D:\\eclipseworkspace\\CODA20110114\\bin\\";
		String classFileName = "edu.pku.cn.testcase.TestCloseDbConnection";
		String methodName = "";
		
		Project project = new Project(projectPath);
		ClassNodeLoader loader = new ClassNodeLoader(projectPath);
		ClassNode cc = loader.loadClassNode(classFileName, 0);;
		
		//Project project = new Project("bin/");
		
		CodaProperties.isLibExpland = true;
		project.addLibPath("lib/");

		for (MethodNode method : cc.methods) {
			
			if(!method.getOriginalFullName().contains(methodName)){
				//continue;
			}
			
			System.out.println("____________________________________________________");
			System.out.println(method.name + " " + method.desc);

			if (method.params != null) {
				for (LocalVariableNode lv : method.params) {
					System.out.println(lv.getType() + " " + lv.name);
				}
			}
			
			System.out.println("*******");
			System.out.println(method.name + " " + method.desc);
			List<Stmt> stmts = method.getStmts();
			
			method.getCFG();
			
			for (int m = 0; m < stmts.size(); m++) {
				System.out.println(m + ": " + stmts.get(m).toString());
			}

			System.out.println("********");

			
			System.out.println(method.getCFG().toString());
//			PointToGraph ptg = method.getPointToGraph();
//			List<Edge> edges = ptg.edges;
//			for (int m = 0; m < edges.size(); m++) {
//				System.out.println(m + ": " + edges.get(m).toString());
//			}

			System.out.println("____________________________________________________");

		}
	}

}

// end

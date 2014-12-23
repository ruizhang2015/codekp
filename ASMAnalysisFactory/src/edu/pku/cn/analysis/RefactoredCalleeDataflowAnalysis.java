/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-3-25 ����04:31:40
 * @modifier: Administrator
 * @time 2009-3-25 ����04:31:40
 * @reviewer: Administrator
 * @time 2009-3-25 ����04:31:40
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.analysis;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.TryCatchBlockNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import edu.pku.cn.analysis.factory.RealValueDataflowFactory;
import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.detector.DataflowTestDriver;
import edu.pku.cn.detector.RefactoredDataflowTestDriver;
import edu.pku.cn.graph.cfg.BasicBlock;
import edu.pku.cn.graph.cfg.CFG;
import edu.pku.cn.graph.visit.ReversePostOrder;
import edu.pku.cn.jir.AssignStmt;
import edu.pku.cn.jir.InvokeExpr;
import edu.pku.cn.jir.InvokeStmt;
import edu.pku.cn.jir.NewExpr;
import edu.pku.cn.jir.Stmt;
import edu.pku.cn.util.AnalysisFactoryManager;

public class RefactoredCalleeDataflowAnalysis extends RefactoredBasicDataflowAnalysis<HashSet<String>> {

	private InsnList insns;
	private List<Stmt> stmts;
	private CFG cfg;
	
	public RefactoredCalleeDataflowAnalysis(CFG cfg) {
		
		this.blockOrder = new ReversePostOrder(cfg);
		this.isForwards = true; 
		this.cfg = cfg;
		this.stmts = cfg.getJIRStmt();
		this.insns = cfg.getMethod().instructions;
		this.facts = new HashSet[stmts.size()];
	}
	
	@Override
	public HashSet<String> createFact() {
		HashSet<String> fact = new HashSet<String>();
		return fact;
	}

	@Override
	public HashSet<String> createFact(HashSet<String> fact) {
		HashSet<String> rdFact = (HashSet) fact.clone();
		return rdFact;
	}

//	@Override
//	public HashSet<String> getNewStartFact(BasicBlock block) {
//		return facts[block.startStmt];
//	}

	@Override
	public void initEntryFact() {
		HashSet rdFact = createFact();
		startFactMap.put(blockOrder.getEntry(), rdFact);
		facts[0] = rdFact;
	}

	@Override
	public boolean isEndBlock(BasicBlock block) {
		return block.getStartInc() >= insns.size();
	}

	@Override
	public HashSet<String> merge(HashSet start, HashSet pred) {
		HashSet result = createFact(start);
		if (!pred.equals(start)) {
			result.addAll(pred);
		}
		return result;
	}

	@Override
	public boolean same(HashSet fact1, HashSet fact2) {
		if (fact1 == null)
			return fact2 == null;
		return fact1.equals(fact2);
	}

//	@Override
//	public void setNewStartFact(BasicBlock block, HashSet<String> fact) {
//		facts[block.startStmt] = fact;
//	}

	@Override
	public HashSet<String> transferVertex(BasicBlock block) {
		
		HashSet<String> fact = getStartFact(block);
		if(fact == null)
			fact = new HashSet<String>();
		
		for(int i = block.startStmt; i <= block.endStmt; i++){
			Stmt stmt= this.stmts.get(i);
//			fact = facts[stmt.getIndex()];
			System.out.println("i: " + i);
			
			if(i == 60)
			{
				System.out.println("i: " + i);
			}
			
			if(stmt instanceof InvokeStmt){
				InvokeStmt is = (InvokeStmt)stmt;
				InvokeExpr ix = (InvokeExpr)is.invoke;
				
				fact.add("" + ix.toString());
				facts[i] = fact;
				
			} else if(stmt instanceof AssignStmt){
				AssignStmt as = (AssignStmt) stmt;
				if(as.right instanceof InvokeExpr) {
					InvokeExpr is = (InvokeExpr)as.right;
					//fact.add("" + is.node.getOpcode() + ":" + is.node.owner + "." + is.node.name);
					fact.add("" + is.toString());
					facts[i] = fact;
				} 
			}
		}
		return fact;
	}

	public static void main(String[] args) {
		RefactoredDataflowTestDriver<HashSet<String>, RefactoredCalleeDataflowAnalysis> driver = new RefactoredDataflowTestDriver<HashSet<String>, RefactoredCalleeDataflowAnalysis>() {
			public RefactoredDataflow<HashSet<String>, RefactoredCalleeDataflowAnalysis> createDataflow(
					ClassNode cc, MethodNode method) {
				// TODO Auto-generated method stub
				try {
					CFG cfg = method.getCFG();
					RefactoredCalleeDataflowAnalysis analysis = new RefactoredCalleeDataflowAnalysis(cfg);
					RefactoredDataflow<HashSet<String>, RefactoredCalleeDataflowAnalysis> dataflow = new RefactoredDataflow<HashSet<String>, RefactoredCalleeDataflowAnalysis>(
							cfg, analysis);
					dataflow.execute();
					return dataflow;
				} catch (AnalyzerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public void examineResults(
					RefactoredDataflow<HashSet<String>, RefactoredCalleeDataflowAnalysis> dataflow) {
				// TODO Auto-generated method stub
				HashSet<String>[] facts = dataflow.getAnalysis().facts;
				List<Stmt> stmts = dataflow.getAnalysis().cfg.getMethod().getStmts();
				for (int i = 0; i < facts.length; i++) {
					System.out.println(i + ": " + stmts.get(i).toString() +  " ->  " + facts[i]);
				}
			}
		};
		driver.execute("TestAnalysisLiveVariable");
	}
}

// end

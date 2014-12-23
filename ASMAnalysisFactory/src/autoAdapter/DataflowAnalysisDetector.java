/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-1-9 锟斤拷锟斤拷03:58:41
 * @modifier: Administrator
 * @time 2009-1-9 锟斤拷锟斤拷03:58:41
 * @reviewer: Administrator
 * @time 2009-1-9 锟斤拷锟斤拷03:58:41
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package autoAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.objectweb.asm.tree.analysis.AnalyzerException;

import automachine.AutoMachine;
import automachine.AutomaUtil;
import edu.pku.cn.analysis.EscapedObjsDataflowAnalysis;
import edu.pku.cn.analysis.InterJIRValue;
import edu.pku.cn.analysis.InterVarInfo;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.detector.Detector;
import edu.pku.cn.graph.cfg.BasicBlock;
import edu.pku.cn.hierarchy.Repository;
import edu.pku.cn.jir.JIRValue;

public class DataflowAnalysisDetector extends Detector<MethodNode> {

	public AMJIRInterDataflowAnalysis2 analysis = null;
	
	public DataflowAnalysisDetector() {
		
	}
	
	public DataflowAnalysisDetector(String specificFile) {
		// to initiate the static fields of AMJIRInterDataflowAnalysis2
		// to initiate the object of AMJIRInterDataflowAnalysis2
		analysis = new AMJIRInterDataflowAnalysis2(specificFile);
	}

	public DataflowAnalysisDetector(AutoMachine am) {
		analysis = new AMJIRInterDataflowAnalysis2(am);
	}

	@Override
	public String getClassName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Detector> getInstances() {
		// TODO Auto-generated method stub
		ArrayList<Detector> detectors;
		// boolean generateFromXml =
		// !System.getProperty("AutoMethodAdapter.usexmltoconfig",
		// "yes").equals("no");
		boolean generateFromXml = true;
		// generate automachines from xml specfic files
		if (generateFromXml) {
			System.out.println("use xml files to config ams");
			detectors = new ArrayList<Detector>();
			String specDir = "AutomachineSpecs";
			File dir = new File(specDir);
			if (dir == null || !dir.exists()) {
				System.out.println(specDir + "not exist!");
			}
			if (!dir.isDirectory()) {
				System.out.println(specDir + "is not a directory!");
			}
			File files[] = dir.listFiles();
			for (File file : files) {
				if (file.getName().toLowerCase().startsWith("detector")) {
					detectors.add(new DataflowAnalysisDetector(file.getAbsolutePath()));
				}
			}
		}
		
		// generate automachines form database
		else {
			System.out.println("use database to config ams");
			detectors = new ArrayList<Detector>();
			System.out.println("accessing database to generate automachines...");
			ArrayList<AutoMachine> ams = AutomaUtil.generateAllAutoMachineFromDatabase();
			for (AutoMachine am : ams) {
				detectors.add(new DataflowAnalysisDetector(am));
			}
			System.out.println("generate " + ams.size() + " automachines.");
		}
		return detectors;
	}
	
	public void examineResults() {
		
		Iterator<BasicBlock> i = this.analysis.cfg.blockIterator();
		
		//System.out.println("ExitNode:" + this.analysis.cfg.getExit().toString());	
		
		//System.out.println("cfg:" + this.analysis.cfg.toString());
//		for (BasicBlock basicBlock : this.analysis.cfg.getBlocks()) {
//			HashMap<String, HashSet<AutoMachine>> result = this.analysis.getResultFact(basicBlock);
//			System.out.println("BasicBlock:" + basicBlock.toString());
//			for (String key : result.keySet()) {
//				HashSet<AutoMachine> machines = (HashSet<AutoMachine>) result.get(key);
//				for (AutoMachine machine: machines) {
//					if (machine.currentState.getStateNumber() != 1) {
//						System.out.println("对象："+ key.toString());
//						System.out.println("	AutoMachine:" + machine.automachineName 
//								+ ";currentState：" + machine.currentState.getStateNumber() 
//								+ ";endableState：" + machine.currentState.isEndState()
//								+ ";lastLine：" + machine.lastStateChangingLine + ".");
//					}
//				}
//			}
//		}
		
		BasicBlock exitBlock = this.analysis.cfg.getExit();
		System.out.println("ExitBlock:" + exitBlock);
		HashMap<String, HashSet<AutoMachine>> result = this.analysis.getResultFact(exitBlock);
		
		for (String key : result.keySet()) {
			HashSet<AutoMachine> machines = (HashSet<AutoMachine>) result.get(key);
			for (AutoMachine machine: machines) {
//				if (machine.currentState.getStateNumber() != 1) {
					System.out.println("对象："+ key.toString());
					System.out.println("	AutoMachine:" + machine.automachineName 
							+ ";currentState：" + machine.currentState.getStateNumber() 
							+ ";endableState：" + machine.currentState.isEndState()
							+ ";lastLine：" + machine.lastStateChangingLine + ".");
//				}
				//System.out.println("    当前状态对应的discounts context：\n" + machine.currentState.discountsUnit.toString());
				//System.out.println("    ");
			}
		}
		
		if (this.analysis.escapeSensitive == true) {
			EscapedObjsDataflowAnalysis escapeAnalysis = new EscapedObjsDataflowAnalysis(this.analysis.cfg);
			try {
				escapeAnalysis.execute();
				// to filtering the open streams according to their eacaping relations
				HashMap<JIRValue, HashSet<JIRValue>> escapeResult = escapeAnalysis.getResultFact(exitBlock);
				HashMap<InterJIRValue, InterVarInfo> interPTResult = this.analysis.ptAnalysis.getResultFact(exitBlock);
				
				// 将escapeResult转换为指向信息
				HashMap<String, HashSet<String>> escapedObjectsResult = new HashMap<String, HashSet<String>> ();
				for (JIRValue key : escapeResult.keySet()) {
					InterJIRValue interKey = new InterJIRValue(key,0);
					
					if ( interPTResult.get(interKey) != null) {
						Set<String> objects = interPTResult.get(interKey).getObjects();
						if (objects != null && objects.size() > 0) {
							String object = (String)objects.toArray()[0];
							
							HashSet<JIRValue> jirInnerObjects = escapeResult.get(key);
							HashSet<String> strInnerObjects = new HashSet<String>();
							
							for (JIRValue jirInnerObj : jirInnerObjects) {
								if (interPTResult.get(new InterJIRValue(jirInnerObj,0)) != null) {
									Set<String> jirInnerObjs = interPTResult.get(new InterJIRValue(jirInnerObj,0)).getObjects();
									if (jirInnerObjs != null && jirInnerObjs.size() > 0) {
										String jirInnerObjStr = (String)jirInnerObjs.toArray()[0];
										strInnerObjects.add(jirInnerObjStr);
									}
								}
							}
							
							if (strInnerObjects.size() > 0)
								escapedObjectsResult.put(object, strInnerObjects);
							
						}
					}
					
				}
				
				// to filter the warnings
				HashMap<String, HashSet<AutoMachine>> filteredResult = this.analysis.createFact(result);
				for (String key : result.keySet()) {
					
					// to judege whether it is closed
					boolean isStoppable = true; 
					for (AutoMachine autoMachine : result.get(key)) {
						if(autoMachine.getCurrentState().isEndState() == false){
							isStoppable = false;
						}
					}
					// when it is stoppable, its inner objects will be changed as stoppable
					// get all of its inner objets first
					if(isStoppable == true)
						removeInnerObjects(escapedObjectsResult, filteredResult, key);
				}
				
				// to print the final result
				System.out.println("Filtered Result: ");
				for (String key : filteredResult.keySet()) {
					HashSet<AutoMachine> machines = (HashSet<AutoMachine>) filteredResult.get(key);
					for (AutoMachine machine: machines) {
						if (machine.currentState.getStateNumber() != 1) {
							System.out.println("对象："+ key.toString());
							System.out.println("	AutoMachine:" + machine.automachineName 
									+ ";currentState：" + machine.currentState.getStateNumber() 
									+ ";endableState：" + machine.currentState.isEndState()
									+ ";lastLine：" + machine.lastStateChangingLine + ".");
						}
					}
				}
			} catch (AnalyzerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
		}
		
		
		
		
		
		
//		while (i.hasNext()) {
//			BasicBlock block = i.next();
//			HashMap<String, HashSet<AutoMachine>> start = this.analysis.getStartFact(block);
//			
//			System.out.println("pt info:");
//			HashMap<InterJIRValue, InterPointsToInfo>  fact = this.analysis.ptAnalysis.getStartFact(block);
//			for (InterJIRValue interJIRValue : fact.keySet()) {
//				System.out.println(interJIRValue.toString() + "->");
//				System.out.println(fact.get(interJIRValue).toString());
//			}
//			System.out.println("pt info:");
			
			// result 有问题，但下一个block的start是没有问题的
//			HashMap<String, HashSet<AutoMachine>> result = this.analysis.getResultFact(block);
//
//			System.out.println("________________________________");
//			Set<String> keys = start.keySet();
//
//			for (String key : keys) {
//				System.out.println(key.toString());
//				HashSet<AutoMachine> machines = (HashSet<AutoMachine>) start.get(key);
//
//				for (AutoMachine machine: machines) {
//					System.out.println("	" + machine.automachineName + ";" + machine.currentState.getStateNumber() + " " + machine.lastStateChangingLine);
//				}
//			}
//			
//			System.out.println("");
//			for (int m = block.startStmt; m <= block.endStmt; m++) {
//				System.out.println(m + ": " + block.stmts.get(m).toString());
//			}
//			System.out.println("");
//			
//			keys = result.keySet();
//			for (String key : keys) {
//				System.out.println(key.toString());
//				HashSet<AutoMachine> machines = (HashSet<AutoMachine>) result.get(key);
//
//				for (AutoMachine machine: machines) {
//					System.out.println("	" + machine.automachineName + ";" + machine.currentState.getStateNumber() + " " + machine.lastStateChangingLine);
//				}
//			}
//			
//			System.out.println("________________________________");
//		}
	}

	private void removeInnerObjects(HashMap<String, HashSet<String>> escapedObjectsResult, HashMap<String, HashSet<AutoMachine>> filteredResult, String key) {
		HashSet<String> innerObjects = escapedObjectsResult.get(key);
		if (innerObjects != null && innerObjects.size() > 0) {
			for (String innerObject : innerObjects) {
				// remove all of its inner objects from the filtered result
				filteredResult.remove(innerObject);
				
				HashSet<String> innerObjectss = escapedObjectsResult.get(innerObject);
				if (innerObjectss != null && innerObjectss.size() > 0) {
					removeInnerObjects(escapedObjectsResult, filteredResult, innerObject);
				}
			}
		}
	}
}

// end

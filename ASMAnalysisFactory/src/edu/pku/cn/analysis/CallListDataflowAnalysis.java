/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-1-2 ����09:01:12
 * @modifier: Administrator
 * @time 2009-1-2 ����09:01:12
 * @reviewer: Administrator
 * @time 2009-1-2 ����09:01:12
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.analysis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import edu.pku.cn.asm.tree.analysis.Call;
import edu.pku.cn.asm.tree.analysis.CallList;
import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.detector.DataflowTestDriver;
import edu.pku.cn.graph.cfg.BasicBlock;
import edu.pku.cn.graph.cfg.CFG;
import edu.pku.cn.graph.visit.ReversePostOrder;

public class CallListDataflowAnalysis extends BasicDataflowAnalysis<CallList> {

	private Map<MethodInsnNode, Call> callMap;
	private InsnList insns;

	public CallListDataflowAnalysis(CFG cfg) {
		this.blockOrder = new ReversePostOrder(cfg);
		this.isForwards = true;
		insns = cfg.getMethod().instructions;
		this.facts = new CallList[insns.size()];
		this.callMap = buildCallMap(cfg);
		
	}

	private Map<MethodInsnNode, Call> buildCallMap(CFG cfg) {
		Map<MethodInsnNode, Call> callMap = new HashMap<MethodInsnNode, Call>();
		AbstractInsnNode insnNode = null;
		Iterator<AbstractInsnNode> insnIter = insns.iterator();
		boolean flag = false;
		MethodInsnNode invokeNode = null;
		Call call = null;
		while (insnIter.hasNext()) {
			insnNode = insnIter.next();
			if (insnNode instanceof MethodInsnNode) {
				invokeNode = (MethodInsnNode) insnNode;
				call = new Call(invokeNode.owner, invokeNode.name, invokeNode.desc);
				flag = true;
				break;
			}
		}
		if (flag)
			callMap.put(invokeNode, call);
		return callMap;
	}

	@Override
	public CallList createFact() {
		CallList callList = new CallList();
		return callList;
	}

	@Override
	public CallList createFact(CallList fact) {
		CallList callList = new CallList(fact);
		return callList;
	}

	// @Override
	// public CallList getFact(int index){
	// return facts[index];
	// }

	@Override
	public CallList getNewStartFact(BasicBlock block) {
		return facts[block.getStartInc()];
	}

	// @Override
	// public CallList getResultFact(BasicBlock block){
	// CallList callList = resultFactMap.get(block);
	// if(callList == null){
	// callList = new CallList();
	// resultFactMap.put(block, callList);
	// }
	// return callList;
	// }

	// @Override
	// public CallList getStartFact(BasicBlock block){
	// CallList callList = startFactMap.get(block);
	// if(callList == null){
	// callList = new CallList();
	// startFactMap.put(block, callList);
	// }
	// return callList;
	// }

	@Override
	public void initEntryFact() {
		CallList callList = new CallList();
		startFactMap.put(blockOrder.getEntry(), callList);
		facts[0] = new CallList();
	}

	@Override
	public boolean isEndBlock(BasicBlock block) {
		if (block.getStartInc() >= insns.size())
			return true;
		return false;
	}

	@Override
	public CallList merge(CallList start, CallList pred) {
		CallList result = new CallList(start);
		if (start.size() == 0) {
			result = new CallList(pred);
		} else if (!pred.equals(start)) {
			if (pred != null && pred.size() > 0) {
				result = start.merge(pred);
			} else 
				result = start;
		}
		return result;
	}

	@Override
	public boolean same(CallList fact1, CallList fact2) {
		return fact1.equals(fact2);
	}

	@Override
	public void setNewStartFact(BasicBlock block, CallList fact) {
		facts[block.getStartInc()] = fact;
	}

	// @Override
	// public void setResultFact(BasicBlock block, CallList result){
	// resultFactMap.put(block, result);
	// }

	// @Override
	// public void setStartFact(BasicBlock block, CallList fact){
	// startFactMap.put(block, fact);
	// }

	@Override
	public CallList transferVertex(BasicBlock block) {
		Iterator<AbstractInsnNode> insnIter = block.nodeIterator(false);
		CallList callList = new CallList(startFactMap.get(block));
		AbstractInsnNode insnNode = null;
		while (insnIter.hasNext()) {
			insnNode = insnIter.next();
			if (insnNode instanceof MethodInsnNode) {
				callList.add(callMap.get((MethodInsnNode) insnNode));
			}
			if (insnNode.index < insns.size() - 1) {
				facts[insnNode.index + 1] = new CallList(callList);
			}
		}
		return callList; // return result callList after the last insn in this
		// block.
	}

	public static void main(String[] args) {
		DataflowTestDriver<CallList, CallListDataflowAnalysis> driver = new DataflowTestDriver<CallList, CallListDataflowAnalysis>() {
			public SimpleDataflow<CallList, CallListDataflowAnalysis> createDataflow(ClassNode cc, MethodNode method) {
				// TODO Auto-generated method stub
				try {
					CallListDataflowAnalysis analysis = new CallListDataflowAnalysis(method.getCFG());
					SimpleDataflow<CallList, CallListDataflowAnalysis> dataflow = new SimpleDataflow<CallList, CallListDataflowAnalysis>(
							method.getCFG(), analysis);

					dataflow.execute();

					return dataflow;
				} catch (AnalyzerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public void examineResults(SimpleDataflow<CallList, CallListDataflowAnalysis> dataflow) {
				// TODO Auto-generated method stub
				CallList[] callLists = dataflow.getAnalysis().facts;
				for (int i = 0; i < callLists.length; i++) {
					if (callLists[i] != null)
						System.out.println(callLists[i]);
				}
			}

		};
		driver.execute("TestCallList");
	}
}

// end

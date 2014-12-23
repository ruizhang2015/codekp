/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author liuxizhiyi
 * @time 2008-12-15 ÏÂÎç02:44:26
 * @modifier: liuxizhiyi
 * @time 2008-12-15 ÏÂÎç02:44:26
 * @reviewer: liuxizhiyi
 * @time 2008-12-15 ÏÂÎç02:44:26
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.analysis;

import java.util.Iterator;
import java.util.List;

import edu.pku.cn.graph.BasicEdge;
import edu.pku.cn.graph.BasicGraph;
import edu.pku.cn.graph.BasicVertex;
import edu.pku.cn.graph.cfg.CFG;
import edu.pku.cn.graph.cfg.analysis.Region;

/**
 *
 * @author liuxizhiyi
 */
public abstract class ForwardFlowAnalysis<
GraphType extends BasicGraph<EdgeType, VertexType>,
EdgeType extends BasicEdge<EdgeType, VertexType>,
VertexType extends BasicVertex<EdgeType,VertexType>,
AnalysisResult
> extends FlowAnalysis<GraphType,EdgeType,VertexType,AnalysisResult> {

	public ForwardFlowAnalysis(GraphType graph,FlowInterpreter<VertexType, AnalysisResult> interpreter){
		super(graph,interpreter);
	}
	/** 
	 * @see edu.pku.cn.analysis.AbstractFlowAnalysis#isForward()
	 */
	@Override
	protected boolean isForward() {
		// TODO Auto-generated method stub
		return true;
	}
	
	protected List<VertexType> constructWorkList(){
		List<VertexType> workList=super.constructWorkList();
		Region region=new Region((CFG)graph);
		return workList;
	}
	public void analysis(){
		
	}
	public void doAnalysis(){
		List<VertexType> workList=constructWorkList();
		
		//initialize the state for all blocks
		Iterator<VertexType> iter=graph.vertexIterator();
		while(iter.hasNext()){
			VertexType vertex=iter.next();
			logicIn.put(vertex, interpreter.newInitialFlow());
			logicOut.put(vertex, interpreter.newInitialFlow());
		}
		//initialize the state of the entry with the entryInitialFlow
		logicIn.put(graph.getRoot(), interpreter.entryInitialFlow());
		
		//start the iteration
		AnalysisResult preBlockState;
		while(workList.size()>0){
			AnalysisResult in=interpreter.newInitialFlow();
			AnalysisResult out=interpreter.newInitialFlow();
			VertexType node=workList.get(0);
			workList.remove(0);
			
			boolean isEntry=node.equals(graph.getRoot());
			//save the state before we change the state of the block
			preBlockState=logicOut.get(node);
			//single branch
			Iterator<EdgeType> edgeIter=node.inEdgeIterator();
			if(node.inComingEdgeSize()==1){
				interpreter.copy(logicOut.get(edgeIter.next().getSource()),in);
			}
			//multiple branch. we will merge them on by on
			else if(node.inComingEdgeSize()>1){
				in=logicOut.get(edgeIter.next().getSource());
				while(edgeIter.hasNext()){
					merge(in,logicOut.get(edgeIter.next().getSource()));
				}
			}
			//the entry has no incoming edge.
			//we should merge it with the initialized entry flow state
			else if(isEntry){
				merge(in,interpreter.entryInitialFlow());
			}
			//now we have prepared the in state of the current block
			//we invoke the flowThrough to calculate with the summary function 
			interpreter.flowThrough(node, in, out);
			//now check the state change station
			//if it has change the out state we should add it into the workList
			if(!preBlockState.equals(out)){
				Iterator<EdgeType> iterEdge=node.outEdgeIterator();
				while(iterEdge.hasNext()){
					workList.add(iterEdge.next().getTarget());
				}
			}
		}
	}
}

// end

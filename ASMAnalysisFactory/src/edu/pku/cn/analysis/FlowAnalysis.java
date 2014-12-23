/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author liuxizhiyi
 * @time 2008-12-15 обнГ12:25:48
 * @modifier: liuxizhiyi
 * @time 2008-12-15 обнГ12:25:48
 * @reviewer: liuxizhiyi
 * @time 2008-12-15 обнГ12:25:48
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.analysis;

import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.pku.cn.graph.BasicEdge;
import edu.pku.cn.graph.BasicGraph;
import edu.pku.cn.graph.BasicVertex;
import edu.pku.cn.graph.visit.RevertPostDFSOrder;

/**
 * The abstract base class for Forward analysis and Backward analysis
 * @author liuxizhiyi
 */
public abstract class FlowAnalysis<
GraphType extends BasicGraph<EdgeType, VertexType>,
EdgeType extends BasicEdge<EdgeType, VertexType>,
VertexType extends BasicVertex<EdgeType,VertexType>,
AnalysisResult
> extends AbstractFlowAnalysis<GraphType,EdgeType,VertexType,AnalysisResult> {

	protected Map<VertexType, AnalysisResult> logicOut;
	protected Map<VertexType,BitSet> whileStruct;
	public FlowAnalysis(GraphType graph,FlowInterpreter<VertexType,AnalysisResult> interpreter){
		super(graph,interpreter);
		logicOut=new HashMap<VertexType, AnalysisResult>(graph.getVerticeSize()*2+1,0.7f);
		whileStruct=new HashMap<VertexType, BitSet>();
	}
   /** Accessor function returning value of OUT set for s. */
   public AnalysisResult getFlowAfter(VertexType node)
   {
       return logicOut.get(node);
   }
   
   protected List<VertexType> constructWorkList(){
	   RevertPostDFSOrder<GraphType, EdgeType, VertexType> order=
		   new RevertPostDFSOrder<GraphType, EdgeType, VertexType>(graph,true);   
	   return order.getOrder();
   }
}

// end

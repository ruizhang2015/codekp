/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author liuxizhiyi
 * @time 2008-12-15 обнГ12:01:43
 * @modifier: liuxizhiyi
 * @time 2008-12-15 обнГ12:01:43
 * @reviewer: liuxizhiyi
 * @time 2008-12-15 обнГ12:01:43
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.analysis;

import java.util.HashMap;
import java.util.Map;

import edu.pku.cn.graph.BasicEdge;
import edu.pku.cn.graph.BasicGraph;
import edu.pku.cn.graph.BasicVertex;

/**
 *
 * @author liuxizhiyi
 */
public abstract class AbstractFlowAnalysis
<
GraphType extends BasicGraph<EdgeType, VertexType>,
EdgeType extends BasicEdge<EdgeType, VertexType>,
VertexType extends BasicVertex<EdgeType,VertexType>,
AnalysisResult
>{
	protected GraphType graph;
	protected Map<VertexType, AnalysisResult> logicIn;
	protected FlowInterpreter<VertexType,AnalysisResult> interpreter;
	public AbstractFlowAnalysis(GraphType graph,FlowInterpreter<VertexType,AnalysisResult> interpreter){
		this.graph=graph;
		logicIn=new HashMap<VertexType, AnalysisResult>(graph.getVerticeSize()*2+1,0.7f);
		this.interpreter=interpreter;
	}
    /** Returns true if this analysis is forwards. */
    protected abstract boolean isForward();

    /** Carries out the actual flow analysis.  
     * Typically called from a concrete FlowAnalysis's constructor.*/
    protected abstract void doAnalysis();

    /** Accessor function returning value of IN set for s. */
    public AnalysisResult getFlowBefore(VertexType node)
    {
        return logicIn.get(node);
    }

    protected void merge(AnalysisResult inout, AnalysisResult in) {
        AnalysisResult tmp = interpreter.newInitialFlow();
        interpreter.merge(inout, in, tmp);
        interpreter.copy(tmp, inout);
    }
}

// end

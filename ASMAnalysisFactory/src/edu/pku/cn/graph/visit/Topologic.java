/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-6-2 ����02:21:26
 * @modifier: Liuxizhiyi
 * @time 2008-6-2 ����02:21:26
 * @reviewer: Liuxizhiyi
 * @time 2008-6-2 ����02:21:26
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.visit;

import java.util.Iterator;

import edu.pku.cn.graph.BasicEdge;
import edu.pku.cn.graph.BasicGraph;
import edu.pku.cn.graph.BasicVertex;

/**
 * �������������˳�򣬸ñ���Ľ������ block1; if(condition){ block 2-1; block 2-2; }
 * else{ block 3; } block 4; ����˳��Ϊ 1��2-1��2-2��3��4
 * 
 * @author Liuxizhiyi
 */
public class Topologic<GraphType extends BasicGraph<EdgeType, VertexType>, EdgeType extends BasicEdge<EdgeType, VertexType>, VertexType extends BasicVertex<EdgeType, VertexType>>
		extends AbstractTopologic<GraphType, EdgeType, VertexType> {

	/**
	 * @param graph
	 */
	public Topologic(GraphType graph) {
		super(graph);
	}

	public Topologic() {

	}

	/**
	 * @see edu.pku.cn.graph.visit.AbstractSearch#edgeIterator(edu.pku.cn.graph.BasicVertex)
	 */
	@Override
	protected Iterator<EdgeType> edgeIterator(VertexType vertex) {
		return vertex.outEdgeIterator();
	}

	/**
	 * @see edu.pku.cn.graph.visit.AbstractSearch#getRoot()
	 */
	@Override
	protected VertexType getRoot() {
		return graph.getRoot();
	}

	/**
	 * @see edu.pku.cn.graph.visit.AbstractSearch#getSource(edu.pku.cn.graph.BasicEdge)
	 */
	@Override
	public VertexType getSource(EdgeType edge) {
		return edge.getSource();
	}

	/**
	 * @see edu.pku.cn.graph.visit.AbstractSearch#getTarget(edu.pku.cn.graph.BasicEdge)
	 */
	@Override
	public VertexType getTarget(EdgeType edge) {
		return edge.getTarget();
	}

	/**
	 * @see edu.pku.cn.graph.visit.AbstractTopologic#edgeSize(edu.pku.cn.graph.BasicVertex)
	 */
	@Override
	public int edgeSize(VertexType vertex) {
		return vertex.inComingEdgeSize();
	}

	/**
	 * @see edu.pku.cn.graph.visit.AbstractSearch#isReverse()
	 */
	@Override
	protected boolean isReverse() {
		// TODO Auto-generated method stub
		return false;
	}
}

// end

/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-5-28 ����02:10:56
 * @modifier: Liuxizhiyi
 * @time 2008-5-28 ����02:10:56
 * @reviewer: Liuxizhiyi
 * @time 2008-5-28 ����02:10:56
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
 * the implement of AbstractDFS
 * 
 * @author Liuxizhiyi
 */
public class DFS<GraphType extends BasicGraph<EdgeType, VertexType>, EdgeType extends BasicEdge<EdgeType, VertexType>, VertexType extends BasicVertex<EdgeType, VertexType>>
		extends AbstractDFS<GraphType, EdgeType, VertexType> {

	/**
	 * @param graph
	 */
	public DFS(GraphType graph) {
		super(graph);
	}

	public DFS(GraphType graph, boolean isPost) {
		super(graph, isPost);
	}

	/**
	 * @see edu.pku.cn.graph.visit.AbstractDFS#edgeIterator(edu.pku.cn.graph.BasicVertex)
	 */
	@Override
	protected Iterator<EdgeType> edgeIterator(VertexType vertex) {
		return vertex.outEdgeIterator();
	}

	/**
	 * @see edu.pku.cn.graph.visit.AbstractDFS#getRoot()
	 */
	@Override
	protected VertexType getRoot() {
		return graph.getRoot();
	}

	/**
	 * @see edu.pku.cn.graph.visit.AbstractDFS#getSource(edu.pku.cn.graph.BasicEdge)
	 */
	@Override
	public VertexType getSource(EdgeType edge) {
		return edge.getSource();
	}

	/**
	 * @see edu.pku.cn.graph.visit.AbstractDFS#getTarget(edu.pku.cn.graph.BasicEdge)
	 */
	@Override
	public VertexType getTarget(EdgeType edge) {
		return edge.getTarget();
	}

	public EdgeType firstEdge(VertexType vertex) {
		return vertex.getFirstOutgoingEdge();
	}

	public EdgeType nextEdge(EdgeType edge) {
		EdgeType newEdge = edge.getNextOutgoingEdge();
		if (newEdge != null && newEdge.getType() != edu.pku.cn.graph.cfg.EdgeType.UNHANDLED_EXCEPTION_EDGE)
			return newEdge;
		return null;
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

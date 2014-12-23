/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-5-29 ����11:21:56
 * @modifier: Liuxizhiyi
 * @time 2008-5-29 ����11:21:56
 * @reviewer: Liuxizhiyi
 * @time 2008-5-29 ����11:21:56
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
 * 
 * @author Liuxizhiyi
 */
public class BFS<GraphType extends BasicGraph<EdgeType, VertexType>, EdgeType extends BasicEdge<EdgeType, VertexType>, VertexType extends BasicVertex<EdgeType, VertexType>>
		extends AbstractBFS<GraphType, EdgeType, VertexType> {

	/**
	 * @param graph
	 */
	public BFS(GraphType graph) {
		super(graph);
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

	/**
	 * @see edu.pku.cn.graph.visit.AbstractSearch#isReverse()
	 */
	@Override
	protected boolean isReverse() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String toString() {
		return "BFS Search";
	}
}

// end

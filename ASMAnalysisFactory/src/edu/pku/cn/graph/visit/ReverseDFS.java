/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-5-28 ����02:14:56
 * @modifier: Liuxizhiyi
 * @time 2008-5-28 ����02:14:56
 * @reviewer: Liuxizhiyi
 * @time 2008-5-28 ����02:14:56
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
 * ��������ȱ���
 * 
 * @author Liuxizhiyi
 */
public class ReverseDFS<GraphType extends BasicGraph<EdgeType, VertexType>, EdgeType extends BasicEdge<EdgeType, VertexType>, VertexType extends BasicVertex<EdgeType, VertexType>>
		extends AbstractDFS<GraphType, EdgeType, VertexType> {

	/**
	 * @param graph
	 */
	public ReverseDFS(GraphType graph) {
		super(graph);
	}

	/**
	 * @see edu.pku.cn.graph.visit.AbstractDFS#edgeIterator(edu.pku.cn.graph.BasicVertex)
	 */
	@Override
	protected Iterator<EdgeType> edgeIterator(VertexType vertex) {
		// TODO Auto-generated method stub
		return vertex.inEdgeIterator();
	}

	/**
	 * @see edu.pku.cn.graph.visit.AbstractDFS#getRoot()
	 */
	@Override
	protected VertexType getRoot() {
		return graph.getExit();
	}

	/**
	 * @see edu.pku.cn.graph.visit.AbstractDFS#getSource(edu.pku.cn.graph.BasicEdge)
	 */
	@Override
	public VertexType getSource(EdgeType edge) {
		return edge.getTarget();
	}

	/**
	 * @see edu.pku.cn.graph.visit.AbstractDFS#getTarget(edu.pku.cn.graph.BasicEdge)
	 */
	@Override
	public VertexType getTarget(EdgeType edge) {
		return edge.getSource();
	}

	public EdgeType firstEdge(VertexType vertex) {
		return vertex.getFirstIncomingEdge();
	}

	public EdgeType nextEdge(EdgeType edge) {
		return edge.getNextIncomingEdge();
	}

	/**
	 * @see edu.pku.cn.graph.visit.AbstractSearch#isReverse()
	 */
	@Override
	protected boolean isReverse() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String toString() {
		return "ReverseDFS Search";
	}
}

// end

/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-6-2 ����03:27:07
 * @modifier: Liuxizhiyi
 * @time 2008-6-2 ����03:27:07
 * @reviewer: Liuxizhiyi
 * @time 2008-6-2 ����03:27:07
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.visit;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.pku.cn.graph.BasicEdge;
import edu.pku.cn.graph.BasicGraph;
import edu.pku.cn.graph.BasicVertex;

/**
 * 
 * @author Liuxizhiyi
 */
public class ReverseTopologic<GraphType extends BasicGraph<EdgeType, VertexType>, EdgeType extends BasicEdge<EdgeType, VertexType>, VertexType extends BasicVertex<EdgeType, VertexType>>
		extends AbstractTopologic<GraphType, EdgeType, VertexType> {
	/**
	 * @param graph
	 */
	public ReverseTopologic(GraphType graph) {
		super(graph);
	}

	/**
	 * @see edu.pku.cn.graph.visit.AbstractTopologic#edgeSize(edu.pku.cn.graph.BasicVertex)
	 */
	@Override
	public int edgeSize(VertexType vertex) {
		return vertex.outGoingEdgeSize();
	}

	/**
	 * @see edu.pku.cn.graph.visit.AbstractSearch#edgeIterator(edu.pku.cn.graph.BasicVertex)
	 */
	@Override
	protected Iterator<EdgeType> edgeIterator(VertexType vertex) {
		Iterator<EdgeType> iter = vertex.inEdgeIterator();
		List<EdgeType> revert = new LinkedList<EdgeType>();
		while (iter.hasNext()) {
			revert.add(0, iter.next());
		}
		return revert.iterator();
	}

	/**
	 * @see edu.pku.cn.graph.visit.AbstractSearch#getRoot()
	 */
	@Override
	protected VertexType getRoot() {
		return graph.getExit();
	}

	/**
	 * @see edu.pku.cn.graph.visit.AbstractSearch#getSource(edu.pku.cn.graph.BasicEdge)
	 */
	@Override
	public VertexType getSource(EdgeType edge) {
		return edge.getTarget();
	}

	/**
	 * @see edu.pku.cn.graph.visit.AbstractSearch#getTarget(edu.pku.cn.graph.BasicEdge)
	 */
	@Override
	public VertexType getTarget(EdgeType edge) {
		return edge.getSource();
	}

	/**
	 * @see edu.pku.cn.graph.visit.AbstractSearch#isReverse()
	 */
	@Override
	protected boolean isReverse() {
		// TODO Auto-generated method stub
		return true;
	}

}

// end

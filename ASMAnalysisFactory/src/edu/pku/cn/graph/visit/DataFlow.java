/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author liuxizhiyi
 * @time 2008-12-10 ����05:16:03
 * @modifier: liuxizhiyi
 * @time 2008-12-10 ����05:16:03
 * @reviewer: liuxizhiyi
 * @time 2008-12-10 ����05:16:03
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
 * @author liuxizhiyi
 */
public class DataFlow<GraphType extends BasicGraph<EdgeType, VertexType>, EdgeType extends BasicEdge<EdgeType, VertexType>, VertexType extends BasicVertex<EdgeType, VertexType>>
		extends AbstractSearch<GraphType, EdgeType, VertexType> {

	int postOrder[];

	protected void init() {

	}

	/**
	 * @see edu.pku.cn.graph.visit.AbstractSearch#accept(edu.pku.cn.graph.visit.GraphVisitor)
	 */
	@Override
	public void accept(GraphVisitor<GraphType, EdgeType, VertexType> visitor) {
		// TODO Auto-generated method stub
		postOrder = new int[graph.getVerticeSize()];
		ReverseDFS<GraphType, EdgeType, VertexType> reverseDfs = new ReverseDFS<GraphType, EdgeType, VertexType>(graph);
		reverseDfs.accept(new GraphVisitor<GraphType, EdgeType, VertexType>() {
			int count = 0;

			public void visitVertex(VertexType vertex) {
				postOrder[vertex.getLabel()] = count++;
			}
		});

	}

	/**
	 * @see edu.pku.cn.graph.visit.AbstractSearch#edgeIterator(edu.pku.cn.graph.BasicVertex)
	 */
	@Override
	protected Iterator<EdgeType> edgeIterator(VertexType vertex) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see edu.pku.cn.graph.visit.AbstractSearch#getRoot()
	 */
	@Override
	protected VertexType getRoot() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see edu.pku.cn.graph.visit.AbstractSearch#getSource(edu.pku.cn.graph.BasicEdge)
	 */
	@Override
	public VertexType getSource(EdgeType edge) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see edu.pku.cn.graph.visit.AbstractSearch#getTarget(edu.pku.cn.graph.BasicEdge)
	 */
	@Override
	public VertexType getTarget(EdgeType edge) {
		// TODO Auto-generated method stub
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

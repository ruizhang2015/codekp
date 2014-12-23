/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-5-29 ����11:04:12
 * @modifier: Liuxizhiyi
 * @time 2008-5-29 ����11:04:12
 * @reviewer: Liuxizhiyi
 * @time 2008-5-29 ����11:04:12
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.visit;

import java.util.BitSet;
import java.util.Iterator;
import java.util.LinkedList;

import edu.pku.cn.graph.BasicEdge;
import edu.pku.cn.graph.BasicGraph;
import edu.pku.cn.graph.BasicVertex;

/**
 * ����ͼ������࣬��Ҫʵ��������� accept(GraphVisitor<GraphType,EdgeType,VertexType>)
 * �����㷨 Iterator<EdgeType> edgeIterator(VertexType) ��α���� VertexType
 * getSource(EdgeType) ��ȡ����ʼ�� VertexType getTarget(EdgeType) ��ȡ���յ�
 * VertexType getRoot() ��ȡͼ�ı������
 * 
 * @author Liuxizhiyi
 */
public abstract class AbstractSearch<GraphType extends BasicGraph<EdgeType, VertexType>, EdgeType extends BasicEdge<EdgeType, VertexType>, VertexType extends BasicVertex<EdgeType, VertexType>> {
	protected GraphType graph;
	protected BitSet visited;
	protected boolean hasCircle = false;

	/**
	 * add some fields to indicate block search order related info
	 * 
	 * @author Meng Na
	 */
	protected int[] discoveryTimeList;
	protected int[] finishTimeList;
	protected int[] colorList;
	protected int timestamp;
	protected LinkedList<VertexType> topologicalSortList;

	/**
	 * Color for vertex not visited
	 */
	protected static final int WHITE = 0;
	/**
	 * Color for vertex whose descendants have been visited partially
	 */
	protected static final int GRAY = 1;
	/**
	 * Color for vertex visited totally
	 */
	protected static final int BLACK = 2;

	public AbstractSearch(GraphType graph) {
		this.graph = graph;

		int numBlocks = graph.getVerticeSize();
		discoveryTimeList = new int[numBlocks];
		finishTimeList = new int[numBlocks];

		timestamp = 0;
		topologicalSortList = new LinkedList<VertexType>();
		colorList = new int[numBlocks];
	}

	public AbstractSearch() {
	}

	public GraphType getGraph() {
		return graph;
	}

	public void setGraph(GraphType graph) {
		this.graph = graph;
	}

	public boolean hasCircle() {
		return hasCircle;
	}

	public void setVisited(VertexType vertex) {
		visited.set(vertex.getLabel(), true);
	}

	public void setUnVisited(VertexType vertex) {
		visited.set(vertex.getLabel(), false);
	}

	public void flip(VertexType vertex) {
		visited.flip(vertex.getLabel());
	}

	public abstract void accept(GraphVisitor<GraphType, EdgeType, VertexType> visitor);

	/**
	 * how to iterator the "logical" outgoing edge
	 * 
	 * @param vertex
	 * @return
	 */
	protected abstract Iterator<EdgeType> edgeIterator(VertexType vertex);

	/**
	 * choose the next search tree root if nodes in the current tree have all
	 * been visited
	 * 
	 * @return
	 */
	// protected VertexType getNextSearchTreeRoot(){
	// for(Iterator<VertexType> i = graph.vertexIterator(); i.hasNext();){
	// VertexType vertex = i.next();
	// if(!visited.get(vertex.getLabel()))
	// return vertex;
	// }return null;
	// }
	/**
	 * how to get the "logical" source node of a edge
	 * 
	 * @param edge
	 * @return
	 */
	public abstract VertexType getSource(EdgeType edge);

	/**
	 * how to get the "logical" target node of a edge
	 * 
	 * @param edge
	 * @return
	 */
	public abstract VertexType getTarget(EdgeType edge);

	/**
	 * how to get the entry of a graph
	 * 
	 * @return
	 */
	protected abstract VertexType getRoot();

	protected abstract boolean isReverse();

	protected class Visit {
		private VertexType vertex;
		private Iterator<EdgeType> outgoingEdgeIterator;

		public Visit(VertexType vertex) {
			if (vertex == null)
				throw new IllegalStateException();
			this.vertex = vertex;
			this.outgoingEdgeIterator = edgeIterator(vertex);

			setColor(vertex, GRAY);
			setDiscoveryTime(vertex, timestamp++);
		}

		public VertexType getVertex() {
			return vertex;
		}

		public boolean hasNextEdge() {
			return outgoingEdgeIterator.hasNext();
		}

		public EdgeType getNextEdge() {
			return outgoingEdgeIterator.next();
		}
	}

	protected void setColor(VertexType vertex, int color) {
		colorList[vertex.getLabel()] = color;
	}

	protected int getColor(VertexType vertex) {
		return colorList[vertex.getLabel()];
	}

	protected void setDiscoveryTime(VertexType vertex, int ts) {
		discoveryTimeList[vertex.getLabel()] = ts;
	}

	public int getDiscoveryTime(VertexType vertex) {
		return discoveryTimeList[vertex.getLabel()];
	}

	protected void setFinishTime(VertexType vertex, int ts) {
		finishTimeList[vertex.getLabel()] = ts;
	}

	public int getFinishTime(VertexType vertex) {
		return finishTimeList[vertex.getLabel()];
	}
}

// end

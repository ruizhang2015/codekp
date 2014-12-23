/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-5-22 锟斤拷锟斤拷09:51:49
 * @modifier: Liuxizhiyi
 * @time 2008-5-22 锟斤拷锟斤拷09:51:49
 * @reviewer: Liuxizhiyi
 * @time 2008-5-22 锟斤拷锟斤拷09:51:49
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A simple Graph implementation where the vertex objects store a list of
 * incoming and outgoing edges. The edge link fields are stored in the edge
 * objects, which means a fairly low space overhead.
 * 
 * <p>
 * The abstract allocateEdge() method must be implemented.
 * 
 * @author Liuxizhiyi
 */
public abstract class BasicGraph<EdgeType extends BasicEdge<EdgeType, VertexType>, VertexType extends BasicVertex<EdgeType, VertexType>>
		implements Graph<EdgeType, VertexType> {
	// Set<VertexType> vertexList;
	protected ArrayList<VertexType> vertexList;
	protected ArrayList<EdgeType> edgeList;
	protected int vertexSize = 0, edgeSize = 0;

	public BasicGraph() {
		this.vertexList = new ArrayList<VertexType>();
		this.edgeList = new ArrayList<EdgeType>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pku.cn.graph.Graph#addVertex(edu.pku.cn.graph.GraphVertex)
	 */
	@Override
	public void addVertex(VertexType v) {
		// v.setLabel(vertexSize++);
		vertexList.add(v);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pku.cn.graph.Graph#containsVertex(edu.pku.cn.graph.GraphVertex)
	 */
	@Override
	public boolean containsVertex(VertexType v) {
		return vertexList.contains(v);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pku.cn.graph.Graph#createEdge(edu.pku.cn.graph.GraphVertex,
	 * edu.pku.cn.graph.GraphVertex)
	 */
	@Override
	public EdgeType createEdge(VertexType source, VertexType target) {
		EdgeType edge = allocateEdge(source, target);
//		Iterator<EdgeType> edgeIter = edgeList.iterator();
//		boolean existent = false;
//
//		while (edgeIter.hasNext()) {
//			EdgeType temp = edgeIter.next();
//			if (temp.getSource().equals(source) && temp.getTarget().equals(target)) {
//				existent = true;
//				break;
//			}
//		}
		if (true) {// to avoid replicates
			edge.setLabel(edgeSize++);
			edgeList.add(edge);
//			if (!vertexList.contains(source))
//				vertexList.add(source);
//			if (!vertexList.contains(target))
//				vertexList.add(target);
		}
		return edge;
	}

	public VertexType createVertex() {
		VertexType vertex = allocateVertex();
		vertex.setLabel(vertexSize++);
		//vertexList.add(vertex);
		return vertex;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pku.cn.graph.Graph#edgeIterator()
	 */
	@Override
	public Iterator<EdgeType> edgeIterator() {
		return edgeList.iterator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pku.cn.graph.Graph#getEdgeSize()
	 */
	@Override
	public int getEdgeSize() {
		return edgeList.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pku.cn.graph.Graph#getVerticeSize()
	 */
	@Override
	public int getVerticeSize() {
		return vertexList.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pku.cn.graph.Graph#lookupEdge(edu.pku.cn.graph.VertexType,
	 * edu.pku.cn.graph.VertexType)
	 */
	@Override
	public EdgeType lookupEdge(VertexType source, VertexType target) {
		Iterator<EdgeType> iter = source.outEdgeIterator();
		while (iter.hasNext()) {
			EdgeType edge = iter.next();
			if (edge.getTarget().equals(target))
				return edge;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pku.cn.graph.Graph#removeEdge(edu.pku.cn.graph.GraphEdge)
	 */
	@Override
	public void removeEdge(EdgeType e) {
		if (e.remove())
			edgeList.remove(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pku.cn.graph.Graph#removeVertex(edu.pku.cn.graph.VertexType)
	 */
	@Override
	public void removeVertex(VertexType v) {
		Iterator<EdgeType> iter = v.inEdgeIterator();
		while (iter.hasNext()) {
			removeEdge(iter.next());
		}
		iter = v.outEdgeIterator();
		while (iter.hasNext()) {
			removeEdge(iter.next());
		}
		vertexList.remove(v);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.pku.cn.graph.Graph#replaceVertex(edu.pku.cn.graph.VertexType,edu.
	 * pku.cn.graph.VertexType)
	 */
	@Override
	public void replaceVertex(VertexType src, VertexType desc) {
		int i = vertexList.indexOf(src);
		if (i >= 0) {
			vertexList.remove(i);
			vertexList.add(i, desc);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pku.cn.graph.Graph#vertexIterator()
	 */
	@Override
	public Iterator<VertexType> vertexIterator() {
		return vertexList.iterator();
	}

	public VertexType getVertex(int index) {
		return vertexList.get(index);
	}

	/**
	 * 锟斤拷锟斤拷一锟斤拷锟斤拷
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	protected abstract EdgeType allocateEdge(VertexType source, VertexType target);

	/**
	 * 锟斤拷锟斤拷一锟斤拷锟节碉拷
	 * 
	 * @return
	 */
	protected abstract VertexType allocateVertex();

	/**
	 * 锟斤拷锟酵硷拷锟斤拷锟斤拷
	 * 
	 * @return
	 */
	public abstract VertexType getRoot();

	/**
	 * 锟斤拷锟酵硷拷锟斤拷盏锟�
	 * 
	 * @return
	 */
	public abstract VertexType getExit();
}

// end

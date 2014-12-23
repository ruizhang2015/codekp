/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-5-21 ����03:12:42
 * @modifier: Liuxizhiyi
 * @time 2008-5-21 ����03:12:42
 * @reviewer: Liuxizhiyi
 * @time 2008-5-21 ����03:12:42
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph;

import java.util.Iterator;

/**
 * Graph interface; defines the operations used to access and manipulate a
 * graph.
 * 
 * @author Liuxizhiyi
 */
public interface Graph<EdgeType extends GraphEdge<EdgeType, VertexType>, VertexType extends GraphVertex<VertexType>> {
	/**
	 * Get number of edges in the graph.
	 */
	public int getEdgeSize();

	/**
	 * Get number of vertices in the graph.
	 */
	public int getVerticeSize();

	/**
	 * Get Iterator over all edges in the graph.
	 */
	public Iterator<EdgeType> edgeIterator();

	/**
	 * Get Iterator over all vertices in the graph.
	 */
	public Iterator<VertexType> vertexIterator();

	/**
	 * Add given vertex to the graph. The vertex should not be part of any other
	 * graph.
	 * 
	 * @param v
	 *            the vertex to add
	 */
	public void addVertex(VertexType v);

	/**
	 * Determine if the graph contains the given vertex.
	 * 
	 * @param v
	 *            the vertex
	 * @return true if the vertex is part of the graph, false if not
	 */
	public boolean containsVertex(VertexType v);

	/**
	 * Add a new edge to the graph. Duplicate edges (with same source and target
	 * vertices) are allowed.
	 * 
	 * @param source
	 *            the source vertex
	 * @param target
	 *            the target vertex
	 * @return the new edge
	 */
	public EdgeType createEdge(VertexType source, VertexType target);

	/**
	 * Look up an edge by source and target vertex. If multiple edges with same
	 * source and target vertex exist, one is selected arbitrarily.
	 * 
	 * @param source
	 *            the source vertex
	 * @param target
	 *            the target vertex
	 * @return a matching edge, or null if there is no matching edge
	 */
	public EdgeType lookupEdge(VertexType source, VertexType target);

	/**
	 * Remove given edge from the graph.
	 */
	public void removeEdge(EdgeType e);

	/**
	 * Remove given vertex from the graph. Note that all edges referencing the
	 * vertex will be removed.
	 */
	public void removeVertex(VertexType v);

	/**
	 * replace the VertexType src with desc
	 * 
	 * @param src
	 * @param desc
	 */
	public void replaceVertex(VertexType src, VertexType desc);
}

// end

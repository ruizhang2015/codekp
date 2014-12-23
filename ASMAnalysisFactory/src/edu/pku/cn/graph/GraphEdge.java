/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-5-21 ����10:04:45
 * @modifier: Liuxizhiyi
 * @time 2008-5-21 ����10:04:45
 * @reviewer: Liuxizhiyi
 * @time 2008-5-21 ����10:04:45
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph;

/**
 * GraphEdge interface; represents an edge in a graph.
 * 
 * @author Liuxizhiyi
 */
public interface GraphEdge<EdgeType extends GraphEdge<EdgeType, VertexType>, VertexType extends GraphVertex<VertexType>>
		extends Comparable<EdgeType> {
	/**
	 * Get the source vertex.
	 * 
	 * @return VertexType
	 */
	public VertexType getSource();

	/**
	 * Get the target vertex.
	 * 
	 * @return VertexType
	 */
	public VertexType getTarget();

	/**
	 * Get the integer label.
	 * 
	 * @return Integer
	 */
	public int getLabel();

	/**
	 * Set the integer label.
	 * 
	 * @return Integer
	 */
	public void setLabel(int label);

	/**
	 * remove the edge
	 * 
	 * @return
	 */
	public boolean remove();
}

// end

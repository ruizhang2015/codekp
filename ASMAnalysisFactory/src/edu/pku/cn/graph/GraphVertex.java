/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-5-21 ����01:59:41
 * @modifier: Liuxizhiyi
 * @time 2008-5-21 ����01:59:41
 * @reviewer: Liuxizhiyi
 * @time 2008-5-21 ����01:59:41
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph;

import java.util.Iterator;

/**
 * GraphVertex interface; represents a vertex in a graph.
 * 
 * @author Liuxizhiyi
 */
public interface GraphVertex<VertexType> extends Comparable<VertexType> {
	/**
	 * Get the numeric label for this vertex.
	 */
	public int getLabel();

	/**
	 * Set the numeric label for this vertex.
	 */
	public void setLabel(int label);

	/**
	 * get the type of the vertex
	 * 
	 * @return
	 */
	public int getType();

	/**
	 * set the type of the vertex
	 * 
	 * @param type
	 */
	public void setType(int type);

	/**
	 * test if there exits a edge from this node to the target directly
	 * 
	 * @param target
	 * @return
	 */
	public boolean pointTo(VertexType target);

	/**
	 * remove the edge
	 * 
	 * @return
	 */
	public boolean remove();

	/**
	 * Get an iterator over the successors of this vertex; i.e., the targets of
	 * the vertex's outgoing edges.
	 * 
	 * @return an Iterator over the successors of the vertex
	 */
	public Iterator<VertexType> predecessorIterator();

	/**
	 * Get an iterator over the predecessors of this vertex; i.e., the sources
	 * of the vertex's incoming edges.
	 * 
	 * @return an Iterator over the predecessors of the vertex
	 */
	public Iterator<VertexType> successorIterator();

	/**
	 * return the iterator of node contains in vertex. if reverse is true. it
	 * iterator from back to front. if reverse is false, it iterator from top to
	 * bottom.
	 * 
	 * @param <T>
	 * @param reverse
	 * @return
	 */
	public <T> Iterator<T> nodeIterator(boolean reverse);
}

// end

/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author liuxizhiyi
 * @time 2008-12-17 ����12:43:40
 * @modifier: liuxizhiyi
 * @time 2008-12-17 ����12:43:40
 * @reviewer: liuxizhiyi
 * @time 2008-12-17 ����12:43:40
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.cfg.analysis;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.pku.cn.graph.BasicEdge;
import edu.pku.cn.graph.BasicGraph;
import edu.pku.cn.graph.BasicVertex;

/**
 * 
 * @author liuxizhiyi
 */
public class GeneratlizedList<GraphType extends BasicGraph<EdgeType, VertexType>, EdgeType extends BasicEdge<EdgeType, VertexType>, VertexType extends BasicVertex<EdgeType, VertexType>> {
	public static enum NodeType {
		ATOM, NORMAL_LIST, IF_LIST, CASE_LIST, WHILE_LIST, DO_WHILE_LIST, LOOP_BODY;
	}

	NodeType type;
	List<GeneratlizedList<GraphType, EdgeType, VertexType>> subList;
	VertexType node;

	public GeneratlizedList() {
	}

	public GeneratlizedList(NodeType type) {
		if (type != NodeType.ATOM) {
			subList = new LinkedList<GeneratlizedList<GraphType, EdgeType, VertexType>>();
		}
		this.type = type;
	}

	public GeneratlizedList(VertexType node) {
		type = NodeType.ATOM;
		this.node = node;
	}

	/**
	 * @return Returns the type.
	 */
	public NodeType getType() {
		return type;
	}

	/**
	 * @param type
	 *            The type to set.
	 */
	public void setType(NodeType type) {
		this.type = type;
	}

	/**
	 * @return Returns the node.
	 */
	public VertexType getNode() {
		return node;
	}

	/**
	 * @param node
	 *            The node to set.
	 */
	public void setNode(VertexType node) {
		this.node = node;
	}

	/**
	 * @return Returns the subList.
	 */
	public List<GeneratlizedList<GraphType, EdgeType, VertexType>> getSubList() {
		return subList;
	}

	/**
	 * @param e
	 * @return
	 * @see java.util.List#add(java.lang.Object)
	 */
	public boolean add(GeneratlizedList<GraphType, EdgeType, VertexType> e) {
		return subList.add(e);
	}

	/**
	 * @param index
	 * @param element
	 * @see java.util.List#add(int, java.lang.Object)
	 */
	public void add(int index, GeneratlizedList<GraphType, EdgeType, VertexType> element) {
		subList.add(index, element);
	}

	/**
	 * 
	 * @see java.util.List#clear()
	 */
	public void clear() {
		subList.clear();
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		return subList.equals(o);
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.List#get(int)
	 */
	public GeneratlizedList<GraphType, EdgeType, VertexType> get(int index) {
		return subList.get(index);
	}

	/**
	 * @return
	 * @see java.util.List#hashCode()
	 */
	public int hashCode() {
		return subList.hashCode();
	}

	/**
	 * @return
	 * @see java.util.List#isEmpty()
	 */
	public boolean isEmpty() {
		return subList.isEmpty();
	}

	/**
	 * @return
	 * @see java.util.List#iterator()
	 */
	public Iterator<GeneratlizedList<GraphType, EdgeType, VertexType>> iterator() {
		return subList.iterator();
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.List#remove(int)
	 */
	public GeneratlizedList<GraphType, EdgeType, VertexType> remove(int index) {
		return subList.remove(index);
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#remove(java.lang.Object)
	 */
	public boolean remove(Object o) {
		return subList.remove(o);
	}

	/**
	 * @param index
	 * @param element
	 * @return
	 * @see java.util.List#set(int, java.lang.Object)
	 */
	public GeneratlizedList<GraphType, EdgeType, VertexType> set(int index,
			GeneratlizedList<GraphType, EdgeType, VertexType> element) {
		if (index > size()) {
			for (int i = size(); i <= index; i++)
				subList.add(new GeneratlizedList<GraphType, EdgeType, VertexType>());
		}
		return subList.set(index, element);
	}

	/**
	 * @return
	 * @see java.util.List#size()
	 */
	public int size() {
		return subList.size();
	}

	/**
	 * @param fromIndex
	 * @param toIndex
	 * @return
	 * @see java.util.List#subList(int, int)
	 */
	public List<GeneratlizedList<GraphType, EdgeType, VertexType>> subList(int fromIndex, int toIndex) {
		return subList.subList(fromIndex, toIndex);
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (this.getType() == NodeType.ATOM) {
			builder.append(node.getLabel()).append(",");
		} else {
			builder.append("{").append(type).append(":");
			for (int i = 0; i < subList.size(); i++) {
				builder.append(subList.get(i));
			}
			builder.append("},");
		}
		return builder.toString();
	}
}

// end

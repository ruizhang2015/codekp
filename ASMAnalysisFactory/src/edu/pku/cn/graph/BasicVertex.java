/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-5-21 ����02:12:06
 * @modifier: Liuxizhiyi
 * @time 2008-5-21 ����02:12:06
 * @reviewer: Liuxizhiyi
 * @time 2008-5-21 ����02:12:06
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
 * 
 * @author Liuxizhiyi
 */
public abstract class BasicVertex<EdgeType extends BasicEdge<EdgeType, VertexType>, VertexType extends BasicVertex<EdgeType, VertexType>>
		implements GraphVertex<VertexType> {

	// the type of the vertex
	protected int type;
	// the index of the vertex
	private int label;
	// private int size=0;
	// the firstIncomingEdge and lastIncomingEdge of current Node
	EdgeType firstIncomingEdge, lastIncomingEdge;
	// the firstOutgoingEdge and lastOutgoingEdge of current Node
	EdgeType firstOutgoingEdge, lastOutgoingEdge;

	protected BasicVertex() {
		// label=size++;
	}

	protected BasicVertex(VertexType vertex, int type) {
		this.type = type;
		firstIncomingEdge = vertex.firstIncomingEdge;
		Iterator<EdgeType> in = inEdgeIterator();
		while (in.hasNext()) {
			EdgeType edge = in.next();
			edge.changeTarget((VertexType) this);
		}
		lastIncomingEdge = vertex.lastIncomingEdge;
		if (lastIncomingEdge != null)
			lastIncomingEdge.changeTarget((VertexType) this);

		firstOutgoingEdge = vertex.firstOutgoingEdge;
		Iterator<EdgeType> out = outEdgeIterator();
		while (out.hasNext()) {
			EdgeType edge = out.next();
			edge.changeSource((VertexType) this);
		}
		lastOutgoingEdge = vertex.lastOutgoingEdge;
		if (lastOutgoingEdge != null)
			lastOutgoingEdge.changeSource((VertexType) this);
	}

	@Override
	public int getLabel() {
		return label;
	}

	@Override
	public void setLabel(int label) {
		this.label = label;
	}

	@Override
	public int hashCode() {
		return label;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof BasicVertex)
			return label == ((VertexType) o).label;
		return false;
	}

	@Override
	public int compareTo(VertexType o) {
		return label - o.getLabel();
	}

	// public Iterator<EdgeType> iterator(VertexType target){
	/**
	 * add a edge as the OutgoingEdge of the last OutgoingEdge of this node
	 * 
	 * @param edge
	 *            {@link BasicEdge}
	 */
	void addOutgoingEdge(EdgeType edge) {
		if (firstOutgoingEdge == null) {
			firstOutgoingEdge = lastOutgoingEdge = edge;
		} else {
			lastOutgoingEdge.setNextOutgoingEdge(edge);
			lastOutgoingEdge = edge;
		}
	}

	/**
	 * get the first outgoing edge
	 * 
	 * @return {@link BasicEdge}
	 */
	public EdgeType getFirstOutgoingEdge() {
		return firstOutgoingEdge;
	}

	/**
	 * add a edge as the IncomingEdge of the last IncomingEdge of this node
	 * 
	 * @param edge
	 *            {@link BasicEdge}
	 */
	void addIncomingEdge(EdgeType edge) {
		if (firstIncomingEdge == null) {
			firstIncomingEdge = lastIncomingEdge = edge;
		} else {
			// start
			lastIncomingEdge.setNextIncomingEdge(edge);
			lastIncomingEdge = edge;
			// end
			// /���Ҫʵ�ֵ�������ڵ���ʣ������ǰ��սڵ�����˳����ʣ�ע���������룬ʹ�����д���
			// edge.setNextIncomingEdge(firstIncomingEdge);
			// firstIncomingEdge=edge;
		}
	}

	/**
	 * get the first IncomingEdge of current node
	 * 
	 * @return {@link BasicEdge}
	 */
	public EdgeType getFirstIncomingEdge() {
		return firstIncomingEdge;
	}

	/**
	 * remove the edge
	 * @deprecated
	 * Modified by WuQian. Invoking this method to remove edges from BasicBlock will end up 
	 * in an inconsistency between edges of BasicBlockes and of CFG. It is recommended to use {@link CFG.removeEdge()} .
	 * 
	 * @throws IllegalArgumentException
	 * @param edge
	 *            {@link BasicEdge}
	 */
	public void removeEdge(EdgeType edge) {
		edge.remove();
	}

	/**
	 * Get an Iterator over outgoing edges from given vertex.
	 * 
	 * @return an Iterator over outgoing edges
	 */
	public Iterator<EdgeType> outEdgeIterator() {
		return BasicEdge.newIterator((VertexType) this, false);
	}

	/**
	 * Get an Iterator over incoming edges to a given vertex.
	 * 
	 * @return an Iterator over incoming edges
	 */
	public Iterator<EdgeType> inEdgeIterator() {
		return BasicEdge.newIterator((VertexType) this, true);
	}

	public int inComingEdgeSize() {
		int count = 0;
		EdgeType cur = firstIncomingEdge;
		while (cur != null) {
			cur = cur.getNextIncomingEdge();
			count++;
		}
		return count;
	}

	public int outGoingEdgeSize() {
		int count = 0;
		EdgeType cur = firstOutgoingEdge;
		while (cur != null) {
			cur = cur.getNextOutgoingEdge();
			count++;
		}
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pku.cn.graph.GraphVertex#pointTo(java.lang.Object)
	 */
	@Override
	public boolean pointTo(VertexType target) {
		Iterator<VertexType> iter = successorIterator();
		boolean flag = false;
		// ################################################################
		while (iter.hasNext() && !flag) {
			if (iter.next().equals(target))
				flag = true;
		}
		return flag;
	}

	/**
	 * the abstract Iterator for the Vertex
	 * <p>
	 * The abstract initial() and getNext() method must be implemented.
	 * 
	 * @param <EdgeType>
	 * @param <VertexType>
	 */
	protected abstract static class VertexIterator<EdgeType extends BasicEdge<EdgeType, VertexType>, VertexType extends BasicVertex<EdgeType, VertexType>>
			implements Iterator<VertexType> {
		protected Iterator<EdgeType> iter;

		public VertexIterator() {
			iter = initial();
		}

		public boolean hasNext() {
			return iter.hasNext();
		}

		public VertexType next() {
			return getNext();
		}

		public void remove() {
			iter.remove();
		}

		public abstract VertexType getNext();

		public abstract Iterator<EdgeType> initial();
	}

	/**
	 * Get an iterator over the predecessors of this vertex; i.e., the targets of
	 * the vertex's outgoing edges.
	 * 
	 * @return an Iterator over the predecessors of the vertex
	 */
	public Iterator<VertexType> predecessorIterator() {
		return new VertexIterator<EdgeType, VertexType>() {
			public Iterator<EdgeType> initial() {
				return inEdgeIterator();
			}

			public VertexType getNext() {
				return iter.next().getSource();
			}
		};
	}

	public abstract <T> T getValue(int index);

	/**
	 * Get an iterator over the successors of this vertex; i.e., the sources
	 * of the vertex's incoming edges.
	 * 
	 * @return an Iterator over the successors of the vertex
	 */
	public Iterator<VertexType> successorIterator() {
		return new VertexIterator<EdgeType, VertexType>() {
			public Iterator<EdgeType> initial() {
				return outEdgeIterator();
			}

			public VertexType getNext() {
				return iter.next().getTarget();
			}
		};
	}
	
	/**
	 * return all direct successors of the vertex
	 * @return
	 */
	public ArrayList<VertexType> getSuccessors(){
		ArrayList<VertexType> sucessors=new ArrayList<VertexType>();
		Iterator<VertexType> ite=this.successorIterator();
		while(ite.hasNext()){
			sucessors.add(ite.next());
		}
		return sucessors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pku.cn.graph.GraphVertex#remove()
	 */
	@Override
	public boolean remove() {
		boolean isRemove = true;
		Iterator<EdgeType> iter = inEdgeIterator();
		while (iter.hasNext()) {
			EdgeType edge = iter.next();
			isRemove &= edge.remove();
		}
		if (isRemove) {
			iter = outEdgeIterator();
			while (iter.hasNext()) {
				EdgeType edge = iter.next();
				isRemove &= edge.remove();
			}
			return isRemove;
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		Iterator<EdgeType> iter = outEdgeIterator();
		while (iter.hasNext()) {
			EdgeType edge = iter.next();
			buf.append(edge.toString()).append("\n");
		}
		
		iter = inEdgeIterator();
		while (iter.hasNext()) {
			EdgeType edge = iter.next();
			buf.append(edge.toString()).append("\n");
		}
		
		return buf.toString();
	}
}

// end

/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-5-21 ����02:11:41
 * @modifier: Liuxizhiyi
 * @time 2008-5-21 ����02:11:41
 * @reviewer: Liuxizhiyi
 * @time 2008-5-21 ����02:11:41
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * GraphEdge implementation for use with BasicEdge.
 * 
 * @author Liuxizhiyi
 */
public abstract class BasicEdge<EdgeType extends BasicEdge<EdgeType, VertexType>, VertexType extends BasicVertex<EdgeType, VertexType>>
		implements GraphEdge<EdgeType, VertexType> {
	/**
	 * the source where this edge linked with
	 */
	private VertexType source;
	/**
	 * the target this edge point to
	 */
	private VertexType target;
	private int label;

	private int type;
	/**
	 * the next Edge of this edge
	 */
	private EdgeType nextOutgoingEdge;
	private EdgeType nextIncomingEdge;

	// private static int size=0;

	protected BasicEdge(VertexType source, VertexType target) {
		this.source = source;
		this.target = target;
		// this.label=size++;
		source.addOutgoingEdge((EdgeType) this);
		target.addIncomingEdge((EdgeType) this);
	}

	/**
	 * Get the type of edge.
	 */
	public int getType() {
		return type;
	}

	/**
	 * Set the type of edge.
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * abstract Iterator over edges.
	 */
	protected abstract static class EdgeIterator<EdgeType extends BasicEdge<EdgeType, VertexType>, VertexType extends BasicVertex<EdgeType, VertexType>>
			implements Iterator<EdgeType> {
		protected EdgeType edge;

		public boolean hasNext() {
			return edge != null;
		}

		public EdgeType next() {
			if (!hasNext())
				throw new NoSuchElementException();
			EdgeType result = edge;
			edge = getNext();
			return result;
		}

		public void remove() {
			edge.remove();
		}

		public abstract EdgeType getNext();
	}

	/**
	 * Outgoing Edge Iterator
	 * 
	 * @param <EdgeType>
	 * @param <VertexType>
	 */
	protected static class OutEdgeIterator<EdgeType extends BasicEdge<EdgeType, VertexType>, VertexType extends BasicVertex<EdgeType, VertexType>>
			extends EdgeIterator<EdgeType, VertexType> {
		private int label;

		public OutEdgeIterator(VertexType source) {
			edge = source.getFirstOutgoingEdge();
			label = source.getLabel();
		}

		@Override
		public EdgeType getNext() {
			return edge.nextOutgoingEdge;
		}

		public boolean equals(Object o) {
			if (o instanceof OutEdgeIterator) {
				return this.label == ((OutEdgeIterator) o).label;
			}
			return false;
		}
	}

	/**
	 * InComing Edge Iterator
	 * 
	 * @param <EdgeType>
	 * @param <VertexType>
	 */
	protected static class InEdgeIterator<EdgeType extends BasicEdge<EdgeType, VertexType>, VertexType extends BasicVertex<EdgeType, VertexType>>
			extends EdgeIterator<EdgeType, VertexType> {
		private int label;

		public InEdgeIterator(VertexType source) {
			edge = source.getFirstIncomingEdge();
			label = source.getLabel();
		}

		@Override
		public EdgeType getNext() {
			return edge.nextIncomingEdge;
		}

		public boolean equals(Object o) {
			if (o instanceof InEdgeIterator) {
				return this.label == ((InEdgeIterator) o).label;
			}
			return false;
		}

		public String toString() {
			return "" + label;
		}
	}

	@Override
	public int getLabel() {
		return label;
	}

	@Override
	public VertexType getSource() {
		return source;
	}

	protected void changeSource(VertexType source) {
		this.source = source;
	}

	@Override
	public VertexType getTarget() {
		return target;
	}

	@Override
	public void setLabel(int label) {
		this.label = label;
	}

	protected void changeTarget(VertexType target) {
		this.target = target;
	}

	@Override
	public int compareTo(EdgeType o) {
		int cmp = source.compareTo(o.getSource());
		if (cmp != 0)
			return cmp;
		return target.compareTo(o.getTarget());
	}

	@Override
	public boolean equals(Object edge) {
		if (edge instanceof BasicEdge) {
			EdgeType o = (EdgeType) edge;
			return source.equals(o.getSource()) && target.equals(o.getTarget());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return source.hashCode() + target.hashCode() * 3;
	}

	void setNextOutgoingEdge(EdgeType edge) {
		nextOutgoingEdge = edge;
	}

	public EdgeType getNextOutgoingEdge() {
		return nextOutgoingEdge;
	}

	void setNextIncomingEdge(EdgeType edge) {
		nextIncomingEdge = edge;
	}

	public EdgeType getNextIncomingEdge() {
		return nextIncomingEdge;
	}

	/**
	 * remove the edge
	 * 
	 * @return
	 */
	public boolean remove() {
		boolean isRemove = false;
		Iterator<EdgeType> iter = this.outIterator();
		EdgeType prev = null;
		while (iter.hasNext()) {
			EdgeType cur = iter.next();
			if (equals(cur)) {
				if (prev == null)
					source.firstOutgoingEdge = cur.nextOutgoingEdge;
				else {
					prev.nextOutgoingEdge = cur.nextOutgoingEdge;
				}
				if (cur.nextOutgoingEdge == null)
					source.lastOutgoingEdge = prev;
				isRemove = true;
				break;
			}
			prev = cur;
		}
		if (isRemove) {
			prev = null;
			iter = this.inIterator();
			while (iter.hasNext()) {
				EdgeType cur = iter.next();
				if (equals(cur)) {
					if (prev == null)
						target.firstIncomingEdge = cur.nextIncomingEdge;
					else
						prev.nextIncomingEdge = cur.nextIncomingEdge;
					if (cur.nextIncomingEdge == null)
						target.lastIncomingEdge = prev;
					return true;
				}
				prev = cur;
			}
		}
		return false;
	}

	/**
	 * the outgoing iterator for edge
	 * 
	 * @param target
	 * @return
	 */
	public Iterator<EdgeType> outIterator() {
		return new OutEdgeIterator<EdgeType, VertexType>(source);
	}

	public Iterator<EdgeType> inIterator() {
		return new InEdgeIterator<EdgeType, VertexType>(target);
	}

	public static <EdgeType extends BasicEdge<EdgeType, VertexType>, VertexType extends BasicVertex<EdgeType, VertexType>> Iterator<EdgeType> newIterator(
			VertexType source, boolean in) {
		if (in)
			return new InEdgeIterator<EdgeType, VertexType>(source);
		return new OutEdgeIterator<EdgeType, VertexType>(source);
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Edge ").append(label);
		buffer.append(": from ").append(source.getLabel());
		buffer.append(" to ").append(target.getLabel());
		return buffer.toString();
	}
}

// end

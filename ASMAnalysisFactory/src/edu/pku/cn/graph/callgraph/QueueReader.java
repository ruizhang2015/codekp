/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author "zhouzhiyi"
 * @time 2009-12-30
 * @modifier: "zhouzhiyi"
 * @time 2009-12-30
 * @reviewer: "zhouzhiyi"
 * @time 2009-12-30
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.callgraph;

import java.util.NoSuchElementException;

/**
 * A queue of Object's. One can add objects to the queue, and they are later
 * read by a QueueReader. One can create arbitrary numbers of QueueReader's for
 * a queue, and each one receives all the Object's that are added. Only objects
 * that have not been read by all the QueueReader's are kept. A QueueReader only
 * receives the Object's added to the queue <b>after</b> the QueueReader was
 * created.
 * 
 * @author Ondrej Lhotak
 */
public class QueueReader<E> implements java.util.Iterator<E> {
	private E[] q;
	private int index;

	QueueReader(E[] q, int index) {
		this.q = q;
		this.index = index;
	}

	/**
	 * Returns (and removes) the next object in the queue, or null if there are
	 * none.
	 */
	@SuppressWarnings("unchecked")
	public final E next() {
		if (q[index] == null)
			throw new NoSuchElementException();
		if (index == q.length - 1) {
			q = (E[]) q[index];
			index = 0;
			if (q[index] == null)
				throw new NoSuchElementException();
		}
		E ret = q[index];
		if (ret == ChunkedQueue.NULL_CONST)
			ret = null;
		index++;
		return ret;
	}

	/** Returns true iff there is currently another object in the queue. */
	@SuppressWarnings("unchecked")
	public final boolean hasNext() {
		if (q[index] == null)
			return false;
		if (index == q.length - 1) {
			q = (E[]) q[index];
			index = 0;
			if (q[index] == null)
				return false;
		}
		return true;
	}

	public final void remove() {
		throw new UnsupportedOperationException();
	}

	public final QueueReader<E> clone() {
		return new QueueReader<E>(q, index);
	}
}

// end

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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.pku.cn.classfile.MethodNode;

/**
 * Represents the edges in a call graph. This class is meant to act as
 * only a container of edges; code for various call graph builders should
 * be kept out of it, as well as most code for accessing the edges.
 * @author "zhouzhiyi"
 */
public class CallGraph {
	protected Set<CallGraphEdge> edges = new HashSet<CallGraphEdge>();
	protected ChunkedQueue<CallGraphEdge> stream = new ChunkedQueue<CallGraphEdge>();
	protected QueueReader<CallGraphEdge> reader = stream.reader();
	protected Map<MethodNode,CallGraphEdge> methodToEdge=new HashMap<MethodNode, CallGraphEdge>();
	protected Map<MethodNode,CallGraphEdge> nextToEdge=new HashMap<MethodNode, CallGraphEdge>();
	protected CallGraphEdge NullEdge=new CallGraphEdge(null, -1, null,InvokeType.INVALID);

	public CallGraphEdge getCallGraphEdge(MethodNode mn){
		return methodToEdge.get(mn);
	}
	public boolean hasNext(CallGraphEdge cge){
		return cge.next!=null;
	}
	public int getEdgeSize(){
		int count=0;
		Iterator<CallGraphEdge> iter=edges.iterator();
		while(iter.hasNext()){
			count+=iter.next().getCalleeSize();
		}
		return count;
	}
	/**
	 * Used to add an edge to the call graph. Returns true iff the edge was not
	 * already present.
	 */
	public boolean addEdge( CallGraphEdge e ) {
		if( !edges.add( e ) ) return false;
		stream.add( e );
		CallGraphEdge position = null;
		position=nextToEdge.get(e.getCaller());
		nextToEdge.put(e.getCaller(), e);
		if(position==null){
			methodToEdge.put(e.getCaller(), e);			
			return true;
			//position=NullEdge;
		}
		e.insertAfter(position);
		return true;
	}

	/**
	 * Removes the edge e from the call graph. Returns true iff the edge was
	 * originally present in the call graph.
	 */
	public boolean removeEdge( CallGraphEdge e ) {
		if( !edges.remove( e ) ) return false;
		e.remove();
		if(methodToEdge.get(e.getCaller())==e)
			if(e.getNext().getCaller()==e.getCaller())
				methodToEdge.put(e.getCaller(), e.getNext());
			else
				methodToEdge.put(e.getCaller(), null);
		return true;
	}
	/**
	 * Returns a QueueReader object containing all edges added so far, and which
	 * will be informed of any new edges that are later added to the graph.
	 */
	public QueueReader<CallGraphEdge> listener() {
		return reader.clone();
	}

	/**
	 * Returns a QueueReader object which will contain ONLY NEW edges which will
	 * be added to the graph.
	 */
	public QueueReader<CallGraphEdge> newListener() {
		return stream.reader();
	}

	public String toString() {
		QueueReader<CallGraphEdge> reader = listener();
		StringBuffer out = new StringBuffer();
		while (reader.hasNext()) {
			CallGraphEdge e = (CallGraphEdge) reader.next();
			out.append(e.toString() + "\n");
		}
		return out.toString();
	}

	/** Returns the number of edges in the call graph. */
	public int size() {
		return edges.size();
	}
}

// end

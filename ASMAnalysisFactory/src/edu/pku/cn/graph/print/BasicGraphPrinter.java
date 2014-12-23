/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-5-26 ����06:12:45
 * @modifier: Liuxizhiyi
 * @time 2008-5-26 ����06:12:45
 * @reviewer: Liuxizhiyi
 * @time 2008-5-26 ����06:12:45
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.print;

import java.io.PrintStream;
import java.util.Iterator;

import edu.pku.cn.graph.BasicEdge;
import edu.pku.cn.graph.BasicGraph;
import edu.pku.cn.graph.BasicVertex;

/**
 * 
 * @author Liuxizhiyi
 */
public class BasicGraphPrinter<EdgeType extends BasicEdge<EdgeType, VertexType>, VertexType extends BasicVertex<EdgeType, VertexType>> {
	private BasicGraph<EdgeType, VertexType> graph;

	public BasicGraphPrinter(BasicGraph<EdgeType, VertexType> graph) {
		this.graph = graph;
	}

	public void print(PrintStream out) {
		Iterator<VertexType> iter = graph.vertexIterator();
		out.println(graph.getVerticeSize() + " Basic Block: ");
		while (iter.hasNext()) {
			VertexType vertex = iter.next();
			out.println(vertex);
		}
		out.println("Basic Block End");
		out.println(graph.getEdgeSize() + " Edges:");
		Iterator<EdgeType> edgeIter = graph.edgeIterator();
		while (edgeIter.hasNext()) {
			EdgeType edge = edgeIter.next();
			out.println(edge);
		}
	}

}

// end

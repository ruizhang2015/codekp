/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author liuxizhiyi
 * @time 2008-12-16 ����04:51:14
 * @modifier: liuxizhiyi
 * @time 2008-12-16 ����04:51:14
 * @reviewer: liuxizhiyi
 * @time 2008-12-16 ����04:51:14
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.visit;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.pku.cn.graph.BasicEdge;
import edu.pku.cn.graph.BasicGraph;
import edu.pku.cn.graph.BasicVertex;

/**
 * 
 * @author liuxizhiyi
 */
public class RevertPostDFSOrder<GraphType extends BasicGraph<EdgeType, VertexType>, EdgeType extends BasicEdge<EdgeType, VertexType>, VertexType extends BasicVertex<EdgeType, VertexType>>
		implements BlockOrder<EdgeType, VertexType> {
	int sequence[];
	Map<Integer, Integer> location;
	List<Integer> loop;
	Set<Integer> loopPoint;

	/**
	 * ��������������
	 * 
	 * @param graph
	 *            ������ͼ
	 */
	public RevertPostDFSOrder(GraphType graph) {
		initialize(graph, false);
	}

	/**
	 * ��������������,�����Ҫ���µ������˳������needReArrangeΪtrue
	 * 
	 * @param graph
	 *            ������ͼ
	 * @param needReArrange
	 *            �Ƿ���Ҫ���ѭ���ṹ�Ż�����˳����������������
	 */
	public RevertPostDFSOrder(GraphType graph, boolean needReArrange) {
		initialize(graph, needReArrange);
	}

	private void initialize(GraphType graph, final boolean needReArrange) {
		DFS<GraphType, EdgeType, VertexType> rPostDfs = new DFS<GraphType, EdgeType, VertexType>(graph, true);
		final int size = graph.getVerticeSize();
		sequence = new int[size];
		for (int i = 0; i < size; i++)
			sequence[i] = -1;
		if (needReArrange) {
			location = new HashMap<Integer, Integer>(size);
			loop = new LinkedList<Integer>();
			loopPoint = new HashSet<Integer>();
		}
		rPostDfs.accept(new GraphVisitor<GraphType, EdgeType, VertexType>() {
			int count = 1;

			public void visitVertex(VertexType vertex) {
				int index = size - count++;
				sequence[index] = vertex.getLabel();
				if (needReArrange)
					location.put(vertex.getLabel(), index);
			}

			public void visitCircleEdge(EdgeType edge) {
				// we find a loop structure here
				if (needReArrange) {
					loop.add(edge.getTarget().getLabel());
					loop.add(edge.getSource().getLabel());
					loopPoint.add(edge.getTarget().getLabel());
				}
			}

			public void visitStackInLoop(final List<EdgeType> stack) {
				assert (loop.size() > 0);
				int loopHead = loop.get(loop.size() - 2);
				int i = stack.size() - 1;
				while (i >= 0) {
					EdgeType edge = stack.get(i);
					if (edge.getSource().getLabel() == loopHead)
						break;
					i--;
				}
				loop.set(loop.size() - 2, stack.get(i).getTarget().getLabel());
			}
		});
		reArrange(needReArrange);

	}

	private void reArrange(final boolean needReArrange) {
		if (!needReArrange)
			return;

	}

	/**
	 * @see edu.pku.cn.graph.visit.BlockOrder#blockIterator()
	 */
	@Override
	public Iterator<VertexType> blockIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<VertexType> getOrder() {
		return new LinkedList<VertexType>();
	}

	@Override
	public VertexType getEntry() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VertexType getExit() {
		// TODO Auto-generated method stub
		return null;
	}
}

// end

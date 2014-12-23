/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-5-27 ����09:48:51
 * @modifier: Liuxizhiyi
 * @time 2008-5-27 ����09:48:51
 * @reviewer: Liuxizhiyi
 * @time 2008-5-27 ����09:48:51
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.visit;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.analysis.Frame;

import edu.pku.cn.graph.BasicEdge;
import edu.pku.cn.graph.BasicGraph;
import edu.pku.cn.graph.BasicVertex;

/**
 * ������ȱ���ͼ������࣬��Ҫʵ���ĸ����� Iterator<EdgeType> edgeIterator(VertexType)
 * ��α���� VertexType getSource(EdgeType) ��ȡ����ʼ�� VertexType
 * getTarget(EdgeType) ��ȡ���յ� VertexType getRoot() ��ȡͼ�ı������
 * 
 * @author Liuxizhiyi
 */
public abstract class AbstractDFS<GraphType extends BasicGraph<EdgeType, VertexType>, EdgeType extends BasicEdge<EdgeType, VertexType>, VertexType extends BasicVertex<EdgeType, VertexType>>
		extends AbstractSearch<GraphType, EdgeType, VertexType> {
	boolean isPost = false;

	public AbstractDFS(GraphType graph) {
		super(graph);

	}

	public AbstractDFS(GraphType graph, boolean postDFS) {
		super(graph);
		isPost = postDFS;
	}

	/**
	 * ������ȱ���ͼ
	 * 
	 * @param visitor
	 *            ������ʽڵ�ͱߵķ���
	 */
	public void myaccept(GraphVisitor<GraphType, EdgeType, VertexType> visitor) {
		if (graph.getRoot() == null)
			return;
		visited = new BitSet(graph.getVerticeSize());
		visited.set(0, visited.size() - 1, false);
		VertexType root = getRoot();
		// visitor.visitVertex(root);

		if (visitor.getVisitAtom() == false)
			visitor.visitVertex(root);
		else {
			int i = 0;
			Iterator<AbstractInsnNode> insnIter = root.nodeIterator(false);
			while (insnIter.hasNext()) {
				AbstractInsnNode insn = insnIter.next();
				visitor.visitInsn(insn, (Frame) root.getValue(i++));
			}
		}
		if (visitor.setVisitBit())
			visited.set(root.getLabel());

		ArrayList<Visit> stack = new ArrayList<Visit>(graph.getVerticeSize());
		stack.add(new Visit(graph.getRoot()));

		while (!stack.isEmpty()) {
			Visit visit = stack.get(stack.size() - 1);
			if (visit.hasNextEdge()) {
				EdgeType edge = visit.getNextEdge();
				VertexType vertex = edge.getTarget();
				if (!visited.get(vertex.getLabel())) {
					visited.set(vertex.getLabel());
					stack.add(new Visit(vertex));
				} else {
					hasCircle = true;
					// visitor.visitCircleEdge(edge);
				}
			} else {
				VertexType vertex = visit.getVertex();
				topologicalSortList.addFirst(vertex);
				setFinishTime(vertex, timestamp++);
				stack.remove(stack.size() - 1);
			}
		}
		// visitor.visitEnd();
	}

	protected void visitVertex(VertexType vertex, GraphVisitor<GraphType, EdgeType, VertexType> visitor) {
		if (visitor.getVisitAtom() == false)
			visitor.visitVertex(vertex);
		else {
			int i = 0;
			Iterator<AbstractInsnNode> insnIter = vertex.nodeIterator(false);
			while (insnIter.hasNext()) {
				AbstractInsnNode insn = insnIter.next();
				visitor.visitInsn(insn, (Frame) vertex.getValue(i++));
			}
		}
	}

	/**
	 * ������ȱ���ͼ
	 * 
	 * @param visitor
	 *            ������ʽڵ�ͱߵķ���
	 */
	public void accept(GraphVisitor<GraphType, EdgeType, VertexType> visitor) {
		if (graph.getRoot() == null)
			return;
		visited = new BitSet(graph.getVerticeSize());
		visited.set(0, visited.size() - 1, false);
		BitSet vertexInStack = new BitSet(graph.getVerticeSize());
		BitSet edgeVisited = new BitSet(graph.getEdgeSize());

		VertexType root = getRoot();
		if (!isPost)
			visitVertex(root, visitor);

		List<EdgeType> stack = new LinkedList<EdgeType>();
		EdgeType edge = firstEdge(root);
		if (edge != null)
			stack.add(edge);
		vertexInStack.set(root.getLabel());

		while (stack.size() > 0) {
			edge = stack.get(stack.size() - 1);
			int edgeIndex = edge.getLabel();
			VertexType target = getTarget(edge);
			int targetIndex = target.getLabel();

			EdgeType nextEdge;
			if (visited.get(targetIndex) == false && edgeVisited.get(edgeIndex) == false) {
				if (!isPost) {
					visitVertex(target, visitor);
					visitor.visitEdge(edge);
				}
				visited.set(target.getLabel());
				vertexInStack.set(targetIndex);
				edgeVisited.set(edgeIndex);
				nextEdge = firstEdge(target);
				if (nextEdge != null)
					stack.add(nextEdge);
				else {
					stack.remove(stack.size() - 1);
					vertexInStack.clear(targetIndex);
					if (isPost) {
						visitVertex(target, visitor);
						visitVertex(getSource(edge), visitor);
						visitor.visitEdge(edge);
					}
				}
			} else {
				if (edgeVisited.get(edgeIndex) == false) {
					edgeVisited.set(edgeIndex);
					if (vertexInStack.get(targetIndex)) {
						hasCircle = true;
						visitor.visitCircleEdge(edge);
						visitor.visitStackInLoop(stack);
					} else
						visitor.visitCrossEdge(edge);
					nextEdge = nextEdge(edge);
					if (nextEdge != null) {
						if (edgeVisited.get(nextEdge.getLabel()) == false)
							stack.add(nextEdge);
					} else {
						stack.remove(stack.size() - 1);
						if (isPost)
							visitVertex(getSource(edge), visitor);
						vertexInStack.clear(getSource(edge).getLabel());
					}
				} else {
					nextEdge = nextEdge(edge);
					stack.remove(stack.size() - 1);
					vertexInStack.clear(targetIndex);
					if (nextEdge != null) {
						if (edgeVisited.get(nextEdge.getLabel()) == false)
							stack.add(nextEdge);
					} else {
						if (isPost)
							visitVertex(getSource(edge), visitor);
					}
					visitor.visitEdge(edge);
				}
			}
		}
		visitor.visitEnd();
	}

	public abstract EdgeType firstEdge(VertexType vertex);

	public abstract EdgeType nextEdge(EdgeType edge);
}

// end

/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-5-29 ����10:57:46
 * @modifier: Liuxizhiyi
 * @time 2008-5-29 ����10:57:46
 * @reviewer: Liuxizhiyi
 * @time 2008-5-29 ����10:57:46
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.visit;

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
 * getTarget(EdgeType) ��ȡ���յ� VertexType getRoot() ��ȡͼ�ı�����ￄ1�7
 * 
 * @author Liuxizhiyi
 */
public abstract class AbstractBFS<GraphType extends BasicGraph<EdgeType, VertexType>, EdgeType extends BasicEdge<EdgeType, VertexType>, VertexType extends BasicVertex<EdgeType, VertexType>>
		extends AbstractSearch<GraphType, EdgeType, VertexType> {

	public AbstractBFS(GraphType graph) {
		super(graph);
	}

	/**
	 * ������ȱ���̈́1�7
	 * 
	 * @param visitor
	 *            ������ʽڵ�ͱߵķ���
	 */
	public void accept(GraphVisitor<GraphType, EdgeType, VertexType> visitor) {
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

		List<Iterator<EdgeType>> queue = new LinkedList<Iterator<EdgeType>>();
		queue.add(edgeIterator(root));

		while (queue.size() > 0) {
			// ȡ���е�һ��Ԫ��
			Iterator<EdgeType> iter = queue.remove(0);
			while (iter.hasNext()) {
				EdgeType edge = iter.next();
				visitor.visitEdge(edge);
				VertexType vertex = getTarget(edge);
				if (visited.get(vertex.getLabel()) == false) {
					queue.add(edgeIterator(vertex));
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
					if (visitor.setVisitBit())
						visited.set(vertex.getLabel());
				} else {
					if (queue.contains(edgeIterator(vertex))) {
						hasCircle = true;
						visitor.visitCircleEdge(edge);
					}
				}
			}
		}
		visitor.visitEnd();
	}
}

// end

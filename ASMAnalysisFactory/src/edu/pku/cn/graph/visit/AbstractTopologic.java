/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-6-2 ����02:02:34
 * @modifier: Liuxizhiyi
 * @time 2008-6-2 ����02:02:34
 * @reviewer: Liuxizhiyi
 * @time 2008-6-2 ����02:02:34
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
import edu.pku.cn.graph.cfg.BlockType;
import edu.pku.cn.graph.cfg.DoWhileBlock;
import edu.pku.cn.graph.cfg.WhileBlock;

/**
 * 
 * @author Liuxizhiyi
 */
public abstract class AbstractTopologic<GraphType extends BasicGraph<EdgeType, VertexType>, EdgeType extends BasicEdge<EdgeType, VertexType>, VertexType extends BasicVertex<EdgeType, VertexType>>
		extends AbstractSearch<GraphType, EdgeType, VertexType> {

	/**
	 * @param graph
	 */
	public AbstractTopologic(GraphType graph) {
		super(graph);
	}

	public AbstractTopologic() {
	};

	protected int[] visitCount;

	/**
	 * @see edu.pku.cn.graph.visit.AbstractSearch#accept(edu.pku.cn.graph.visit.GraphVisitor)
	 */
	@Override
	public void accept(GraphVisitor<GraphType, EdgeType, VertexType> visitor) {
		visitCount = new int[graph.getVerticeSize()];
		visited = new BitSet();
		VertexType root = getRoot();
		// visitor.visitVertex(root);
		if (isReverse()) {
			Iterator<VertexType> iter = graph.vertexIterator();
			while (iter.hasNext()) {
				VertexType vertex = iter.next();
				Iterator<VertexType> succIter = vertex.successorIterator();
				while (succIter.hasNext()) {
					if (succIter.next().getType() == BlockType.EXCEPTION_BLOCK) {
						visitCount[vertex.getLabel()]++;
					}
				}
			}
		}
		visitor.visitVertex(root);
		if (visitor.getVisitAtom()) {
			int i = 0;
			Iterator<AbstractInsnNode> insnIter = root.nodeIterator(false);
			while (insnIter.hasNext()) {
				AbstractInsnNode insn = insnIter.next();
				visitor.visitInsn(insn, (Frame) root.getValue(i++));
			}
		}
		visitCount[root.getLabel()] = 0;
		if (visitor.setVisitBit())
			visited.set(root.getLabel());
		else
			visitCount[root.getLabel()]--;
		List<Iterator<EdgeType>> queue = new LinkedList<Iterator<EdgeType>>();
		List<Iterator<EdgeType>> stack = new LinkedList<Iterator<EdgeType>>();

		queue.add(edgeIterator(root));
		VertexType source = root;

		while (queue.size() > 0) {
			Iterator<EdgeType> iter = queue.remove(0);
			boolean first = false;
			while (iter.hasNext()) {
				EdgeType edge = iter.next();
				source = getSource(edge);
				VertexType vertex = getTarget(edge);
				int index = vertex.getLabel();
				visitCount[index]++;
				if (visited.get(index) == false) {
					if (visitCount[index] >= edgeSize(vertex)) {
						if (first == false) {
							queue.add(edgeIterator(vertex));
							visitor.visitEdge(edge);
							{
								visitor.visitVertex(vertex);
								if (visitor.getVisitAtom()) {
									int i = 0;
									Iterator<AbstractInsnNode> insnIter = vertex.nodeIterator(false);
									while (insnIter.hasNext()) {
										AbstractInsnNode insn = insnIter.next();
										visitor.visitInsn(insn, (Frame) vertex.getValue(i++));
									}
								}
							}
							if (visitor.setVisitBit())
								visited.set(index);
							else
								visitCount[index]--;
							if (visitor.setVisitBit() == false && vertex.getType() == BlockType.WHILE_BLOCK) {
								visited.clear(index);
							}
							first = true;
						} else {
							stack.add(0, edgeIterator(source));
							break;
						}
					} else if (vertex.getType() == BlockType.WHILE_BLOCK
							|| (vertex.getType() == BlockType.DOWHILE_BLOCK)) {
						if (vertex.getType() == BlockType.DOWHILE_BLOCK) {
							if (isReverse() != ((DoWhileBlock) vertex).reverse) {
								stack.add(0, iter);
								break;
							}
						}
						index = vertex.getLabel();
						int circleSize = ((WhileBlock) vertex).getEntrySize();
						if (isReverse())
							circleSize = edgeSize(vertex) - visitCount[index];
						if (visitCount[index] + circleSize == edgeSize(vertex) && visited.get(index) == false) {
							// �õ㹹�ɻ�������check it carefully
							visitCount[index] = edgeSize(vertex) - 1;
							// queue.add(edgeIterator(source));
							stack.add(0, edgeIterator(source));
							break;
						}
					}
				}
			}
			if (queue.size() == 0 && stack.size() != 0) {
				for (int i = 0; i < stack.size(); i++) {
					// while(stack.size()>0){
					queue.add(stack.remove(0));
				}
			}
		}
		visitor.visitEnd();
	}

	public abstract int edgeSize(VertexType vertex);
}

// end

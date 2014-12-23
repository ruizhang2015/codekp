/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author liuxizhiyi
 * @time 2008-12-17 ����04:48:03
 * @modifier: liuxizhiyi
 * @time 2008-12-17 ����04:48:03
 * @reviewer: liuxizhiyi
 * @time 2008-12-17 ����04:48:03
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.cfg.analysis;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.pku.cn.graph.BasicEdge;
import edu.pku.cn.graph.BasicGraph;
import edu.pku.cn.graph.BasicVertex;
import edu.pku.cn.graph.cfg.BlockType;
import edu.pku.cn.graph.cfg.analysis.GeneratlizedList.NodeType;
import edu.pku.cn.graph.visit.DFS;
import edu.pku.cn.graph.visit.GraphVisitor;

/**
 * 
 * @author liuxizhiyi
 */
public class Region<GraphType extends BasicGraph<EdgeType, VertexType>, EdgeType extends BasicEdge<EdgeType, VertexType>, VertexType extends BasicVertex<EdgeType, VertexType>> {
	GeneratlizedList<GraphType, EdgeType, VertexType> list;
	GraphType graph;
	int realSize = 0;
	int sequence[];
	int copySequence[];
	Map<Integer, Integer> location;
	Map<Integer, BitSet> loopRegion;
	Map<Integer, Integer> loopEnd;

	public Region(GraphType graph) {
		this.graph = graph;
		initialize();
	}

	private void initialize() {
		DFS<GraphType, EdgeType, VertexType> rPostDfs = new DFS<GraphType, EdgeType, VertexType>(graph, true);
		final int size = graph.getVerticeSize();
		sequence = new int[size];
		location = new HashMap<Integer, Integer>();
		loopRegion = new HashMap<Integer, BitSet>();

		rPostDfs.accept(new GraphVisitor<GraphType, EdgeType, VertexType>() {
			int count = 1;

			public void visitVertex(VertexType vertex) {
				int index = size - count++;
				sequence[index] = vertex.getLabel();
				location.put(sequence[index], index);
			}

			public void visitCircleEdge(EdgeType edge) {
				int start = edge.getTarget().getLabel();
				if (!loopRegion.containsKey(start)) {
					loopRegion.put(start, new BitSet());
				}
				loopRegion.get(start).set(edge.getSource().getLabel());
			}

			public void visitEnd() {
				realSize = count;
			}
		});
		loopEnd = new HashMap<Integer, Integer>();
		copySequence = sequence.clone();
		for (Entry<Integer, BitSet> entry : loopRegion.entrySet()) {
			int max = -1;
			BitSet temp = entry.getValue();
			for (int i = temp.nextSetBit(0); i >= 0; i = temp.nextSetBit(i + 1)) {
				int index = location.get(i);
				if (index > max)
					max = index;
			}
			loopEnd.put(entry.getKey(), max);
		}
		list = createRegion(null, 1, realSize);
	}

	/**
	 * @return Returns the list.
	 */
	public GeneratlizedList<GraphType, EdgeType, VertexType> getList() {
		return list;
	}

	private GeneratlizedList<GraphType, EdgeType, VertexType> createRegion(
			GeneratlizedList<GraphType, EdgeType, VertexType> currentList, int currentIndex, int end) {
		// GeneratlizedList currentList=null;
		while (currentIndex < end) {
			for (; currentIndex < end && copySequence[currentIndex] == -1; currentIndex++)
				;
			if (currentIndex >= end)
				break;
			VertexType block = graph.getVertex(copySequence[currentIndex]);
			switch (block.getType()) {
			case BlockType.IF_BLOCK:
				GeneratlizedList<GraphType, EdgeType, VertexType> ifHead = createIfRegion(currentIndex, end);
				if (currentList == null)
					currentList = ifHead;
				else
					currentList.add(ifHead);
				break;
			case BlockType.WHILE_BLOCK:
			case BlockType.DOWHILE_BLOCK:
				GeneratlizedList<GraphType, EdgeType, VertexType> loopHead = null;
				if (loopRegion.containsKey(block.getLabel())) {
					loopHead = createLoopRegion(currentIndex, end);
					if (block.getType() == BlockType.DOWHILE_BLOCK)
						loopHead.setType(NodeType.DO_WHILE_LIST);
				} else { // just the next condition for loop
					loopHead = new GeneratlizedList<GraphType, EdgeType, VertexType>(block);
					copySequence[currentIndex] = -1;
				}
				if (currentList == null)
					currentList = loopHead;
				else
					currentList.add(loopHead);
				break;
			case BlockType.SWITCH_BLOCK:
				GeneratlizedList<GraphType, EdgeType, VertexType> caseHead = createCaseRegion(currentIndex, end);
				if (currentList == null)
					currentList = caseHead;
				else
					currentList.add(caseHead);
				break;
			default:
				if (currentList == null)
					currentList = new GeneratlizedList<GraphType, EdgeType, VertexType>(NodeType.NORMAL_LIST);
				currentList.add(new GeneratlizedList<GraphType, EdgeType, VertexType>(block));
				// set the flag that has been visited
				copySequence[currentIndex] = -1;
				break;
			}
			currentIndex++;
		}
		return currentList;
	}

	private void createBranch(GeneratlizedList<GraphType, EdgeType, VertexType> head, int currentIndex, int end) {
		VertexType block = graph.getVertex(copySequence[currentIndex]);
		head.add(new GeneratlizedList<GraphType, EdgeType, VertexType>(block));
		// set the flag that has been visited
		copySequence[currentIndex] = -1;

		Iterator<EdgeType> iter = block.outEdgeIterator();
		List<Integer> out = new ArrayList<Integer>();
		int max = -1;
		while (iter.hasNext()) {
			EdgeType edge = iter.next();
			if (edge.getType() != edu.pku.cn.graph.cfg.EdgeType.UNHANDLED_EXCEPTION_EDGE) {
				int target = location.get(edge.getTarget().getLabel());
				out.add(target);
				if (target > max)
					max = target;
			}
		}
		Collections.sort(out);
		iter = graph.getVertex(copySequence[out.get(1) - 1]).outEdgeIterator();
		while (iter.hasNext()) {
			block = iter.next().getTarget();
			if (block.getType() != BlockType.EXCEPTION_BLOCK || block.getType() != BlockType.UNHANDLE_EXCEPTION_BLOCK) {
				int endIf = location.get(block.getLabel());
				if (endIf > max) {
					out.add(endIf);
					break;
				} else if (copySequence[endIf] == -1) {// back edge
					out.add(max + 1);
					break;
				}
			}
		}
		assert (out.get(out.size() - 1) > max);
		for (int i = 1; i < out.size(); i++) {
			head.add(createRegion(null, out.get(i - 1), out.get(i)));
		}
	}

	private GeneratlizedList<GraphType, EdgeType, VertexType> createCaseRegion(int currentIndex, int end) {
		GeneratlizedList<GraphType, EdgeType, VertexType> caseHead = new GeneratlizedList<GraphType, EdgeType, VertexType>(
				NodeType.CASE_LIST);
		createBranch(caseHead, currentIndex, end);
		return caseHead;
	}

	private GeneratlizedList<GraphType, EdgeType, VertexType> createIfRegion(int currentIndex, int end) {
		GeneratlizedList<GraphType, EdgeType, VertexType> ifHead = new GeneratlizedList<GraphType, EdgeType, VertexType>(
				NodeType.IF_LIST);
		createBranch(ifHead, currentIndex, end);
		return ifHead;
	}

	private GeneratlizedList<GraphType, EdgeType, VertexType> createLoopRegion(int currentIndex, int end) {
		GeneratlizedList<GraphType, EdgeType, VertexType> loopHead = new GeneratlizedList<GraphType, EdgeType, VertexType>(
				NodeType.WHILE_LIST);
		VertexType block = graph.getVertex(copySequence[currentIndex]);
		loopHead.add(new GeneratlizedList<GraphType, EdgeType, VertexType>(block));
		// set the flag that has been visited
		copySequence[currentIndex] = -1;

		for (++currentIndex; currentIndex < end && copySequence[currentIndex] == -1; currentIndex++)
			;
		end = loopEnd.get(block.getLabel()) + 1;
		// rearrange the loop
		Iterator<EdgeType> iter = block.outEdgeIterator();
		List<Integer> out = new ArrayList<Integer>();
		int max = -1;
		while (iter.hasNext()) {
			EdgeType edge = iter.next();
			if (edge.getType() != edu.pku.cn.graph.cfg.EdgeType.UNHANDLED_EXCEPTION_EDGE) {
				int target = location.get(edge.getTarget().getLabel());
				out.add(target);
				if (target > max)
					max = target;
			}
		}
		if (end > max)
			out.add(end);
		Collections.sort(out);
		int bodyIndex = -1;
		for (int i = 1; i < out.size(); i++) {
			NodeType type = NodeType.NORMAL_LIST;
			int loopStart = out.get(i - 1);
			int loopEnd = out.get(i);
			if (loopStart <= end - 1 && end - 1 < loopEnd)
				type = NodeType.LOOP_BODY;
			loopHead.add(createRegion(new GeneratlizedList<GraphType, EdgeType, VertexType>(type), loopStart, loopEnd));
			bodyIndex = loopHead.size() - 1;
		}
		if (bodyIndex != 1) {
			loopHead.add(1, loopHead.remove(bodyIndex));
		}
		return loopHead;
	}
}

// end

/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-3-3 ����07:12:07
 * @modifier: Administrator
 * @time 2009-3-3 ����07:12:07
 * @reviewer: Administrator
 * @time 2009-3-3 ����07:12:07
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.visit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Comparator;
import java.util.Iterator;

import edu.pku.cn.graph.cfg.BasicBlock;
import edu.pku.cn.graph.cfg.CFG;
import edu.pku.cn.graph.cfg.Edge;

/**
 * 
 * @author Administrator
 */
public class PostOrder extends AbstractBlockOrder {

	CFG cfg;
	int count;
	BitSet visited = null;
	int[] finishedTimes;

	public PostOrder(CFG cfg) {
		this.cfg = cfg;
		count = 1;
		int size = cfg.getVerticeSize();
		visited = new BitSet(size);
		finishedTimes = new int[size];
		visit(cfg.getRoot());
	}

	private void visit(BasicBlock block) {
		visited.set(block.getLabel());
		Iterator<Edge> edgeIter = block.outEdgeIterator();
		BasicBlock temp = null;
		while (edgeIter.hasNext()) {
			temp = edgeIter.next().getTarget();
			if (!visited.get(temp.getLabel()))
				visit(temp);
		}
		finishedTimes[block.getLabel()] = count;
		count++;
	}

	private class PostfixComparator implements Comparator<BasicBlock> {

		@Override
		public int compare(BasicBlock aa, BasicBlock bb) {
			// TODO Auto-generated method stub
			return finishedTimes[aa.getLabel()] - finishedTimes[bb.getLabel()];
		}

	}

	@Override
	public Iterator<BasicBlock> blockIterator() {
		if (blockList.size() == 0) {
			int numBlocks = cfg.getVerticeSize();
			BasicBlock[] blocks = new BasicBlock[numBlocks];
			int count = 0;
			for (Iterator<BasicBlock> i = cfg.blockIterator(); i.hasNext();) {
				blocks[count++] = i.next();
			}
			Arrays.sort(blocks, new PostfixComparator());

			blockList = new ArrayList<BasicBlock>(numBlocks);
			for (int i = 0; i < numBlocks; i++) {
				blockList.add(blocks[i]);
			}
		}
		return blockList.iterator();
	}

	public int[] getFinishedTimes() {
		return finishedTimes;
	}
}

// end

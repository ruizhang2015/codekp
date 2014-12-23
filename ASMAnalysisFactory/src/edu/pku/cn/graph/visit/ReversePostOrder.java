/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2008-12-11 ����12:25:10
 * @modifier: Administrator
 * @time 2008-12-11 ����12:25:10
 * @reviewer: Administrator
 * @time 2008-12-11 ����12:25:10
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.visit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

import edu.pku.cn.graph.cfg.BasicBlock;
import edu.pku.cn.graph.cfg.CFG;

public class ReversePostOrder extends AbstractBlockOrder {

	private int[] reversePostOrderFinishedTimes;

	private class ReversePostfixComparator implements Comparator<BasicBlock> {
		public int compare(BasicBlock aa, BasicBlock bb) {
			// TODO Auto-generated method stub
			return reversePostOrderFinishedTimes[aa.getLabel()]
					- reversePostOrderFinishedTimes[((BasicBlock) bb).getLabel()];
		}
	}

	public ReversePostOrder(CFG cfg) {
		PostOrder postOrder = new PostOrder(cfg);
		int[] postOrderFinishedTimes = postOrder.getFinishedTimes();

		int numBlocks = cfg.getVerticeSize();
		reversePostOrderFinishedTimes = new int[numBlocks];
		for (int i = 0; i < numBlocks; i++) {
			reversePostOrderFinishedTimes[i] = numBlocks - postOrderFinishedTimes[i];
		}

		int count = 0;
		BasicBlock[] blocks = new BasicBlock[numBlocks];
		for (Iterator<BasicBlock> i = cfg.blockIterator(); i.hasNext();) {
			blocks[count++] = i.next();
		}

		Arrays.sort(blocks, new ReversePostfixComparator());

		blockList = new ArrayList<BasicBlock>(numBlocks);
		for (int i = 0; i < numBlocks; i++) {
			blockList.add(blocks[i]);
		}
	}

	public String toString() {
		StringBuffer buf = new StringBuffer("ReversePostOrder:");
		Iterator<BasicBlock> blockIter = this.blockIterator();
		while (blockIter.hasNext()) {
			BasicBlock block = blockIter.next();
			buf.append("  " + block.getLabel());
		}
		return buf.toString();
	}
}

// end

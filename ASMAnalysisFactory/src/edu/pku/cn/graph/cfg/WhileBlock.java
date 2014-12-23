/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-6-15 ����01:19:50
 * @modifier: Liuxizhiyi
 * @time 2008-6-15 ����01:19:50
 * @reviewer: Liuxizhiyi
 * @time 2008-6-15 ����01:19:50
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.cfg;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author Liuxizhiyi
 */
public class WhileBlock extends BasicBlock {
	List<BasicBlock> entry = new ArrayList<BasicBlock>();

	protected WhileBlock() {
		type = BlockType.WHILE_BLOCK;
	}

	protected WhileBlock(BasicBlock block) {
		super(block, BlockType.WHILE_BLOCK);
	}

	protected WhileBlock(BasicBlock block, int type) {
		super(block, type);
	}

	public int getEntrySize() {
		return entry.size();
	}

	public void addEntryBlock(BasicBlock block) {
		entry.add(block);
	}

	public Iterator<BasicBlock> entryIterator() {
		return entry.iterator();
	}
}

// end

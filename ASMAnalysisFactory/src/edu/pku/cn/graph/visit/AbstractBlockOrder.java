/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2008-12-4 ����09:34:58
 * @modifier: Administrator
 * @time 2008-12-4 ����09:34:58
 * @reviewer: Administrator
 * @time 2008-12-4 ����09:34:58
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.visit;

import java.util.ArrayList;
import java.util.Iterator;

import edu.pku.cn.graph.cfg.BasicBlock;
import edu.pku.cn.graph.cfg.Edge;

/**
 * Abstract base class for all kinds of block orders. It allows the subclass to
 * specify just a Comparator for BasicBlocks, and handles the work of sorting.
 * 
 * @author Administrator
 */
public abstract class AbstractBlockOrder implements BlockOrder<Edge, BasicBlock> {

	protected ArrayList<BasicBlock> blockList = new ArrayList<BasicBlock>();

	@Override
	public Iterator<BasicBlock> blockIterator() {
		return blockList.iterator();
	}

	@Override
	public BasicBlock getEntry() {
		// TODO Auto-generated method stub
		return blockList.get(0);
	}

	@Override
	public BasicBlock getExit() {
		// TODO Auto-generated method stub
		return blockList.get(blockList.size() - 1);
	}

}

// end

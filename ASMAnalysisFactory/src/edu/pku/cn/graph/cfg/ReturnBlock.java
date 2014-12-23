/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-3-19 ����08:06:00
 * @modifier: Administrator
 * @time 2009-3-19 ����08:06:00
 * @reviewer: Administrator
 * @time 2009-3-19 ����08:06:00
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.cfg;

public class ReturnBlock extends BasicBlock {
	protected ReturnBlock() {
		type = BlockType.RETURN_BLOCK;
	}

	protected ReturnBlock(BasicBlock block) {
		super(block, BlockType.RETURN_BLOCK);
	}

}

// end

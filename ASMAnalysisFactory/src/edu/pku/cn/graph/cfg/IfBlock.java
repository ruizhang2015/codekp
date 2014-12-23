/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-6-15 ����01:16:24
 * @modifier: Liuxizhiyi
 * @time 2008-6-15 ����01:16:24
 * @reviewer: Liuxizhiyi
 * @time 2008-6-15 ����01:16:24
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.cfg;

/**
 * 
 * @author Liuxizhiyi
 */
public class IfBlock extends BasicBlock {
	public BasicBlock fallThrough;
	public BasicBlock jump;

	protected IfBlock() {
		type = BlockType.IF_BLOCK;
	}

	protected IfBlock(BasicBlock block) {
		super(block, BlockType.IF_BLOCK);
	}
}

// end

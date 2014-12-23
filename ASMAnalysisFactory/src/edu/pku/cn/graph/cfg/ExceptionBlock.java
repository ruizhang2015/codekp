/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-6-15 ����01:30:51
 * @modifier: Liuxizhiyi
 * @time 2008-6-15 ����01:30:51
 * @reviewer: Liuxizhiyi
 * @time 2008-6-15 ����01:30:51
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
public class ExceptionBlock extends BasicBlock {
	protected ExceptionBlock() {
		type = BlockType.EXCEPTION_BLOCK;
	}

	protected ExceptionBlock(BasicBlock block) {
		super(block, BlockType.EXCEPTION_BLOCK);
	}
}

// end

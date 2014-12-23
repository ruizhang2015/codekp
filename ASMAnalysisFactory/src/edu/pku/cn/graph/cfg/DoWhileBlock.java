/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-6-16 ����02:49:06
 * @modifier: Liuxizhiyi
 * @time 2008-6-16 ����02:49:06
 * @reviewer: Liuxizhiyi
 * @time 2008-6-16 ����02:49:06
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
public class DoWhileBlock extends WhileBlock {
	public boolean hasCondition = false;
	public boolean reverse = false;

	protected DoWhileBlock() {
		type = BlockType.DOWHILE_BLOCK;
	}

	protected DoWhileBlock(BasicBlock block) {
		super(block, BlockType.DOWHILE_BLOCK);
	}
}

// end

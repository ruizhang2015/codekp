/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-6-19 ����10:41:24
 * @modifier: Liuxizhiyi
 * @time 2008-6-19 ����10:41:24
 * @reviewer: Liuxizhiyi
 * @time 2008-6-19 ����10:41:24
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.cfg;

import java.util.HashMap;

/**
 * 
 * @author Liuxizhiyi
 */
public class CaseBlock extends BasicBlock {
	public BasicBlock defaultCase;
	public HashMap<Object, BasicBlock> switchCase = new HashMap<Object, BasicBlock>();

	protected CaseBlock() {
		type = BlockType.SWITCH_BLOCK;
	}

	protected CaseBlock(BasicBlock block) {
		super(block, BlockType.SWITCH_BLOCK);
	}

	public void addCase(Object condition, BasicBlock caseIndex) {
		switchCase.put(condition, caseIndex);
	}
}

// end

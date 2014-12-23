/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-6-15 ����01:18:50
 * @modifier: Liuxizhiyi
 * @time 2008-6-15 ����01:18:50
 * @reviewer: Liuxizhiyi
 * @time 2008-6-15 ����01:18:50
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
public class SwitchBlock extends BasicBlock {
	public BasicBlock defaultCase;
	public HashMap<Object, BasicBlock> switchCase = new HashMap<Object, BasicBlock>();

	protected SwitchBlock() {
		type = BlockType.SWITCH_BLOCK;
	}

	protected SwitchBlock(BasicBlock block) {
		super(block, BlockType.SWITCH_BLOCK);
	}

	public void addCase(Object condition, BasicBlock caseIndex) {
		switchCase.put(condition, caseIndex);
	}
}

// end

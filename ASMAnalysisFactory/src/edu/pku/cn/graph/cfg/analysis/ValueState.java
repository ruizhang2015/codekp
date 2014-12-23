/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-6-18 ����08:31:02
 * @modifier: Liuxizhiyi
 * @time 2008-6-18 ����08:31:02
 * @reviewer: Liuxizhiyi
 * @time 2008-6-18 ����08:31:02
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.cfg.analysis;

import java.util.List;

import edu.pku.cn.asm.tree.analysis.RealValue;

/**
 * 
 * @author Liuxizhiyi
 */
public class ValueState<T extends Object> {
	public RealValue<T> value;
	public List<Condition> conditions;
	public int index;
}

// end

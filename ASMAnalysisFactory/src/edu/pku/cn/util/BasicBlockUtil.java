/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-3-8 ����07:22:01
 * @modifier: Administrator
 * @time 2009-3-8 ����07:22:01
 * @reviewer: Administrator
 * @time 2009-3-8 ����07:22:01
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.util;

import java.util.Iterator;

import edu.pku.cn.graph.cfg.BasicBlock;

public class BasicBlockUtil {

	public static void printBasicBlocks(Iterator<BasicBlock> blockIter) {
		while (blockIter.hasNext()) {
			System.out.println(blockIter.next());
		}
	}
}

// end

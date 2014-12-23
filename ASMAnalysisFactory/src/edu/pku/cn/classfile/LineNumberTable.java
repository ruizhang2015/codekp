/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-6-1 ����01:15:55
 * @modifier: Liuxizhiyi
 * @time 2008-6-1 ����01:15:55
 * @reviewer: Liuxizhiyi
 * @time 2008-6-1 ����01:15:55
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.classfile;

import java.util.Iterator;
import java.util.Vector;

/**
 * LineNumber Table in a method
 * 
 * @author Liuxizhiyi
 */
public class LineNumberTable {
	// private HashMap<Region,Integer> lineNumberTable=new
	// java.util.HashMap<Region, Integer>();
	private Vector<LineNode> lines = new Vector<LineNode>();

	class LineNode {
		public Region region;
		public int line;

		public boolean equals(int index) {
			return region.equals(index);
		}

		public LineNode(int start, int end, int line) {
			region = new Region(start, end, 0);
			this.line = line;
		}
	}

	public void addLine(int start, int end, int line) {
		// lineNumberTable.put(new Region(start,end,0), line);
		lines.add(new LineNode(start, end, line));
	}

	public void addLine(int start, int line) {
		addLine(start, start, line);
	}

	public int getLine(int index) {
		Iterator<LineNode> iter = lines.iterator();
		while (iter.hasNext()) {
			LineNode node = iter.next();
			if (node.equals(index))
				return node.line;
		}
		return -1;
	}
}

// end

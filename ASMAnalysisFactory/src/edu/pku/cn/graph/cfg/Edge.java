/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-5-22 ����01:05:08
 * @modifier: Liuxizhiyi
 * @time 2008-5-22 ����01:05:08
 * @reviewer: Liuxizhiyi
 * @time 2008-5-22 ����01:05:08
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.cfg;

import edu.pku.cn.graph.BasicEdge;

/**
 * 
 * @author Liuxizhiyi
 */
public class Edge extends BasicEdge<Edge, BasicBlock> implements EdgeType {

	/**
	 * @param source
	 * @param target
	 */
	public Edge(BasicBlock source, BasicBlock target) {
		super(source, target);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Edge o) {
		// TODO Auto-generated method stub
		return super.compareTo(o);
	}

	@Override
	public String toString() {
		return super.toString() + " Type:" + EdgeType.Type[getType()];
	}

}

// end

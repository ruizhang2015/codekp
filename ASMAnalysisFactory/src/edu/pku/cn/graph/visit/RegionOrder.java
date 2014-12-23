/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author liuxizhiyi
 * @time 2008-12-19 ����04:51:49
 * @modifier: liuxizhiyi
 * @time 2008-12-19 ����04:51:49
 * @reviewer: liuxizhiyi
 * @time 2008-12-19 ����04:51:49
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.visit;

import java.util.ArrayList;
import java.util.List;

import edu.pku.cn.graph.BasicEdge;
import edu.pku.cn.graph.BasicGraph;
import edu.pku.cn.graph.BasicVertex;
import edu.pku.cn.graph.cfg.analysis.GeneratlizedList;
import edu.pku.cn.graph.cfg.analysis.Region;

/**
 * 
 * @author liuxizhiyi
 */
public class RegionOrder<GraphType extends BasicGraph<EdgeType, VertexType>, EdgeType extends BasicEdge<EdgeType, VertexType>, VertexType extends BasicVertex<EdgeType, VertexType>> {
	Region<GraphType, EdgeType, VertexType> region;

	/**
	 * 
	 */
	public RegionOrder(Region<GraphType, EdgeType, VertexType> region) {
		this.region = region;
	}

	public void accept() {
		List<VertexType> workList = new ArrayList<VertexType>();
		List<GeneratlizedList<GraphType, EdgeType, VertexType>> list = region.getList().getSubList();
		for (int i = 0; i < list.size(); i++) {

		}
	}
}

// end

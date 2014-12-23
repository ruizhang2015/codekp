/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2008-12-4 ����11:15:02
 * @modifier: Administrator
 * @time 2008-12-4 ����11:15:02
 * @reviewer: Administrator
 * @time 2008-12-4 ����11:15:02
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.visit;

import java.util.Iterator;

import edu.pku.cn.graph.BasicEdge;
import edu.pku.cn.graph.BasicVertex;

public interface BlockOrder<EdgeType extends BasicEdge<EdgeType, VertexType>, VertexType extends BasicVertex<EdgeType, VertexType>> {

	public Iterator<VertexType> blockIterator();

	public VertexType getEntry();

	public VertexType getExit();
}

// end

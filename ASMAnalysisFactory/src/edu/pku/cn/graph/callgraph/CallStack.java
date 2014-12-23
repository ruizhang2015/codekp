/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author liuxi
 * @time 2010-3-22
 * @modifier: liuxi
 * @time 2010-3-22
 * @reviewer: liuxi
 * @time 2010-3-22
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.callgraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.Type;

import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.ptg.Node;

/**
 * @author liuxi
 */
public class CallStack {
	public MethodNode node;
	public int index;
	public HashMap<Node,Set<Type>> pt;
	public HashSet<Type> reType;
	public CallStack(MethodNode node, int index, HashMap<Node, Set<Type>> pt, HashSet<Type> reType) {
		super();
		this.node = node;
		this.index = index;
		this.pt = pt;
		this.reType = reType;
	}
	public Object clone(){
		return new CallStack(node, index, new HashMap<Node, Set<Type>>(pt), new HashSet<Type>(reType));
	}
}

// end

/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author liuxi
 * @time 2010-3-16
 * @modifier: liuxi
 * @time 2010-3-16
 * @reviewer: liuxi
 * @time 2010-3-16
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.ptg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import edu.pku.cn.classfile.LocalVariableNode;
import edu.pku.cn.classfile.MethodNode;

/**
 * @author liuxi
 */
public class PointToGraph {
	/**
	 * changed next. wrap all the visiting method of Edge. define an operation interface for other 
	 * method which will base on this point-to-graph
	 */
	public HashMap<LocalVariableNode,Node> local2Node;
	public LocalNode This;
	public List<LocalNode> params;
	public Node get(Object arg0) {
		return local2Node.get(arg0);
	}
	public Node put(LocalVariableNode arg0, Node arg1) {
		return local2Node.put(arg0, arg1);
	}
	public void setParams(List<LocalVariableNode> param){
		if(local2Node==null)
			return;
		int size=0;
		if(param!=null)
			size=param.size();
		this.params=new ArrayList<LocalNode>(size);
		for(int i=0;i<size;i++){
			params.add((LocalNode) local2Node.get(param.get(i)));
		}
	}
	public List<Edge> edges;
	public Node returnNodes;
	public Node getReturnNodes() {
		return returnNodes;
	}
	public void setReturnNodes(Node returnNodes) {
		this.returnNodes = returnNodes;
	}
	public void print(){
		Iterator<Edge> iter=edges.iterator();
		while(iter.hasNext()){
			System.out.println(iter.next());
		}
	}
}

// end

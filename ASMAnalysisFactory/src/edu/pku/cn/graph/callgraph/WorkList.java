/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author liuxi
 * @time 2010-3-23
 * @modifier: liuxi
 * @time 2010-3-23
 * @reviewer: liuxi
 * @time 2010-3-23
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.callgraph;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.pku.cn.classfile.MethodNode;

/**
 * @author liuxi
 */
class WorkList{
	List<CallStack> work=new LinkedList<CallStack>();
	HashMap<MethodNode,CallStack> inWorkList=new HashMap<MethodNode,CallStack>();
	public void push(CallStack cs){
		work.add(cs);
		inWorkList.put(cs.node,cs);
	}
	public boolean contain(MethodNode node){
		return inWorkList.containsKey(node);
	}
	public CallStack refind(MethodNode m){
		return (CallStack) inWorkList.get(m).clone();
	}
	public Iterator<CallStack> iterator(MethodNode m){
		CallStack cs=inWorkList.get(m);
		int index=work.indexOf(cs);
		return work.subList(index, work.size()).iterator();
	}
	public CallStack top(){
		return work.get(0);
	}
	public CallStack pop(){
		CallStack cs=work.remove(0);
		inWorkList.remove(cs.node);
		return cs;
	}
	public boolean empty(){
		return work.size()<=0;
	}
}
// end

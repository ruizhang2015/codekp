/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-6-1 ����12:06:37
 * @modifier: Liuxizhiyi
 * @time 2008-6-1 ����12:06:37
 * @reviewer: Liuxizhiyi
 * @time 2008-6-1 ����12:06:37
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.classfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Local variable table: [pc: 0, pc: 70] local: this index: 0 type: ...
 * 
 * @author Liuxizhiyi
 */
public class LocalVariableTable {
//	public class LocalVariableNode {
//		public List<Region> region = new ArrayList<Region>();
//		// public int index;
//		public String name;
//		// public int line;
//		public Type type;
//
//		public int allIndex;
//
//		public String toString() {
//			StringBuffer buf = new StringBuffer();
//			buf.append("Name:").append(name).append(" ");
//			buf.append("Type:").append(type.getClassName()).append(" ");
//			buf.append(region);
//			return buf.toString();
//		}
//
//		public LocalVariableNode(Region region, String name, Type type) {
//			this.region.add(region);
//			this.name = name;
//			this.type = type;
//		}
//
//		public LocalVariableNode(int start, int end, int index) {
//			region.add(new Region(start, end, index));
//		}
//
//		public LocalVariableNode(int start, int end, int index, String name, Type type) {
//			this(new Region(start, end, index), name, type);
//		}
//
//		public boolean equals(int start, int index) {
//			for (int i = 0; i < region.size(); i++) {
//				if (region.get(i).equals(start, index))
//					return true;
//			}
//			return false;
//		}
//	}

	// protected HashMap<Region,LocalVariableNode> localVariable=new
	// HashMap<Region,LocalVariableNode>();
	protected List<LocalVariableNode> locals = new ArrayList<LocalVariableNode>();
	protected HashMap<Region,LocalVariableNode> region2Node=new HashMap<Region, LocalVariableNode>(); 
	public LocalVariableNode getLastNode(){
		return locals.get(locals.size()-1);
	}
	public void addNode(int start, int end, int index, LocalVariableNode node) {
//		if (index <= locals.size()) {
//			for (int i = 0; i < locals.size(); i++) {
//				LocalVariableNode old = locals.get(i);
//				if (old.name.equals(node.name) && old.type.equals(Type.getType(node.desc))) {
//
//				}
//			}
//		}
		//if(index<=locals.size())
			//throw new RuntimeException("LocalTable");
		//LocalVariableNode newNode = new LocalVariableNode(start, end, index);
		//newNode.name = node.name;
		//newNode.type = Type.getType(node.desc);		
		node.allIndex = locals.size();
		//node.addRegion(start, end, index);
		locals.add(node);
	}

	public void addNode(int start, int index, LocalVariableNode node) {
		addNode(start, start, index, node);
	}

	/**
	 * return the local variable node represent the InsnNode will invoke
	 * actually
	 * 
	 * @param start
	 *            : the pc value of the InsnNode
	 * @param index
	 *            : the stack index of the InsnNode operate on. Ex: 12: aload 1,
	 *            that means start=12,index=1
	 * @return
	 */
	public LocalVariableNode getNode(int start, int index) {
		// return localVariable.get(new Region(start,start,index));
		Iterator<LocalVariableNode> iter = locals.iterator();
		while (iter.hasNext()) {
			LocalVariableNode node = iter.next();
			if (node.equals(start, index))
				return node;
		}
		return null;
	}

	public void addNode(LocalVariableNode node) {
		locals.add(node);
	}

	/**
	 * 
	 * @param index
	 * @param node
	 */
	public void insertNode(int index, LocalVariableNode node) {
		assert (index >= 0 && index < locals.size());
		node.allIndex = index;
		for (int i = index + 1; i < locals.size(); i++) {
			locals.get(i).allIndex++;
		}
		locals.add(index, node);
	}

	public LocalVariableNode getNode(int allIndex) {
		return locals.get(allIndex);
	}

	public int size() {
		return locals.size();
	}
}

// end

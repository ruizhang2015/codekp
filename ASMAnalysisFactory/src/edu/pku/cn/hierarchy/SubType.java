/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author liuxi
 * @time 2010-3-18
 * @modifier: liuxi
 * @time 2010-3-18
 * @reviewer: liuxi
 * @time 2010-3-18
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.hierarchy;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.Type;

import edu.pku.cn.classfile.ClassNode;

/**
 * @author liuxi
 */
public class SubType {
	static final long ObjectHash=ELFhash("java.lang.Object");
	static final Repository rep=Repository.getInstance();
	public ClassNode node;
	public Set<SubType> childs;
	long childHash=0;
	public long getChildHash() {
		return childHash;
	}
	long childsSet=0;
	public Set<ClassNode> getSubClass(){
		Set<ClassNode> subs=new HashSet<ClassNode>();
		if(childs!=null){
			Iterator<SubType> iter=childs.iterator();
			while(iter.hasNext()){
				SubType sub=iter.next();
				subs.addAll(sub.getSubClass());
				subs.add(sub.node);
			}
		}
		return subs;
	}
	public Set<Type> getSubType(){
		Set<Type> subs=new HashSet<Type>();
		if(childs!=null){
			Iterator<SubType> iter=childs.iterator();
			while(iter.hasNext()){
				SubType sub=iter.next();
				subs.addAll(sub.getSubType());
				subs.add(sub.node.getType());
			}
		}
		return subs;
	}
	public static long ELFhash(String str){
		long h=0;
		long g;
		for(int i=0;i<str.length();i++){
			h=(h<<4)+str.charAt(i);
			g=h&0xF0000000;
			if(g==0)
				h^=g>>24;
			h&=~g;
		}
		return h;
	}
	public SubType(ClassNode node){
		this.node=node;		
		childHash=ELFhash(node.name);
	}
	public void addChild(SubType child){
		if(childHash==ObjectHash)
			return;		
		if(childs==null)
			childs=new HashSet<SubType>();
		childs.add(child);
	}
	public String toString(){
		return node.name;
	}
}

// end

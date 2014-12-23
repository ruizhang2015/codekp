/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-3-10
 * @modifier: a
 * @time 2010-3-10
 * @reviewer: a
 * @time 2010-3-10
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.ptg;

import org.objectweb.asm.Type;

import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.hierarchy.Repository;

/**
 * @author a
 */
public abstract class Node {
	public static final int ALLOCATENODE=0;
	public static final int ALLOCFIELDNODE=1;
	public static final int ARRAYNODE=2;
	public static final int FIELDNODE=3;
	public static final int INVOKENODE=4;
	public static final int LOCALNODE=5;
	public static final int PARAMETERNODE=6;
	public Type type;
	public int hashNum;
	public static final Node NULL=new AllocateNode();
	private static int num=0;
	protected Node(){
		hashNum=num++;
		type=Type.NULL;
	}
	public String toString(){		
		return type.toString();		
	}
	public int hashCode(){
		return hashNum;
	}
	public int hashNum(){
		return hashNum;
	}
	public Type getType(){
		return type;
	}
	public abstract int getSort();
	public abstract boolean isStatic();
	public ClassNode getClassNode(){
		Repository cha=Repository.getInstance();
		return cha.findClassNode(type.getClassName());
	}
}

// end

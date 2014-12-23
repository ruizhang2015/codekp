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
/**
 * @author a
 */
public abstract class Edge {
	public Node from;
	public Node to;
	public int stmtPc;
	public int getStmtPc() {
		return stmtPc;
	}
	public void setStmtPc(int stmtPc) {
		this.stmtPc = stmtPc;
	}
	public static final int ALLOCEDGE=0;
	public static final int ASSIGNEDGE=1;
	public static final int LOADEDGE=2;
	public static final int STOREEDGE=3;
	public String toString(){
		return from+"->"+to;
	}
	public abstract int getSort();
}

// end

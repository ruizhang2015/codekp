/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-12 обнГ01:41:48
 * @modifier: a
 * @time 2010-1-12 обнГ01:41:48
 * @reviewer: a
 * @time 2010-1-12 обнГ01:41:48
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.jir;
/**
 * @author zhouzhiyi
 */
public abstract class AbstractStmt implements Stmt {
	/**the index of instructions which this operation start*/
	public int start;
	/**the index of instructions which this operation end*/
	public int end;
	/**the index this Stmt in all Stmts*/
	public int index;
	public void setRegion(int start,int end){
		this.start=start;
		this.end=end;
	}
	public void setRegion(int start){
		setRegion(start,start);
	}
	public int getIndex(){
		return index;
	}
	public void setIndex(int index){
		this.index=index;
	}
	
	public String toString(){
		return " ["+start+","+end+"]";
	}
}

// end

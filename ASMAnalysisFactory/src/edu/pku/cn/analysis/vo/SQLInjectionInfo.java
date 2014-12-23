/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-5 ÏÂÎç03:42:13
 * @modifier: a
 * @time 2010-1-5 ÏÂÎç03:42:13
 * @reviewer: a
 * @time 2010-1-5 ÏÂÎç03:42:13
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.analysis.vo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.Type;

import edu.pku.cn.jir.JIRValue;

/**
 * @author wuling
 */
public class SQLInjectionInfo extends Object {

	private List<SQLInjectionPath> path = new ArrayList<SQLInjectionPath>();
	private boolean isTaint;
	
	
	public List<SQLInjectionPath> getPath() {
		return path;
	}

	public void setPath(List<SQLInjectionPath> path) {
		this.path = path;
	}

	public boolean isTaint() {
		return isTaint;
	}

	public void setTaint(boolean isTaint) {
		this.isTaint = isTaint;
	}

	public SQLInjectionInfo mergeWith(SQLInjectionInfo guest) {
		this.path.addAll(guest.path);
        if(this.isTaint&&guest.isTaint){
        	this.isTaint = true;
        }
		return this;
	}

	public SQLInjectionInfo clone() {
		SQLInjectionInfo newInfo = new SQLInjectionInfo();
		for (SQLInjectionPath sqlInjectionPath : this.path) {
			newInfo.path.add(sqlInjectionPath);
		}
		newInfo.isTaint = this.isTaint;
		return newInfo;
	}

	public boolean equals(SQLInjectionInfo info) {
		if (this.path.size() != info.path.size()) {
			return false;
		}

		for (SQLInjectionPath value : this.path) {
			if (!info.path.contains(value)) {
				return false;
			}
		}
		
		if(this.isTaint!=info.isTaint){
			return false;
		}
		
		return true;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("path:");
		for (SQLInjectionPath sqlInjectionPath : this.path) {
			builder.append(sqlInjectionPath.toString() + "; ");
		}
		builder.append("\n isTaint:	"+isTaint);
		return builder.toString();
	}
}

// end

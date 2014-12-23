/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-5 обнГ03:42:13
 * @modifier: a
 * @time 2010-1-5 обнГ03:42:13
 * @reviewer: a
 * @time 2010-1-5 обнГ03:42:13
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.analysis.vo.security;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wuling
 */
public class InterSQLInjectionInfo extends Object {

	private List<InterSQLInjectionPath> path = new ArrayList<InterSQLInjectionPath>();
	private boolean isTaint;
	
	
	public List<InterSQLInjectionPath> getPath() {
		return path;
	}

	public void setPath(List<InterSQLInjectionPath> path) {
		this.path = path;
	}

	public boolean isTaint() {
		return isTaint;
	}

	public void setTaint(boolean isTaint) {
		this.isTaint = isTaint;
	}

	public InterSQLInjectionInfo mergeWith(InterSQLInjectionInfo guest) {
		this.path.addAll(guest.path);
        if(this.isTaint&&guest.isTaint){
        	this.isTaint = true;
        }
		return this;
	}

	public InterSQLInjectionInfo clone() {
		InterSQLInjectionInfo newInfo = new InterSQLInjectionInfo();
		for (InterSQLInjectionPath sqlInjectionPath : this.path) {
			newInfo.path.add(sqlInjectionPath);
		}
		newInfo.isTaint = this.isTaint;
		return newInfo;
	}

	public boolean equals(InterSQLInjectionInfo info) {
		if (this.path.size() != info.path.size()) {
			return false;
		}

		for (InterSQLInjectionPath value : this.path) {
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
		for (InterSQLInjectionPath sqlInjectionPath : this.path) {
			builder.append(sqlInjectionPath.toString() + "; ");
		}
		builder.append("\n isTaint:	"+isTaint);
		return builder.toString();
	}
}

// end

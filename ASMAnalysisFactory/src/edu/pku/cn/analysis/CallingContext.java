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
package edu.pku.cn.analysis;

import edu.pku.cn.classfile.MethodNode;

/**
 * @author liangguangtai
 */
public class CallingContext extends Object {

	public MethodNode callingMethodNode;
	public int callingSite; // line number of calling site

	public CallingContext(MethodNode method, int currentLine) {
		this.callingMethodNode = method;
		this.callingSite = currentLine;
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof CallingContext) {
			CallingContext callingContext = (CallingContext) object;
			if (this.callingSite != this.callingSite) {
				return false;
			}
			if (this.callingMethodNode == null && callingContext != null) {
				return false;
			}
			if (this.callingMethodNode != null && !this.callingMethodNode.equals(callingContext.callingMethodNode)) {
				return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CallingMethod:");
		builder.append(this.callingMethodNode.owner + "." + this.callingMethodNode.name + ":" + this.callingMethodNode.desc);
		builder.append("::");
		builder.append(this.callingSite);
		return builder.toString();
	}

	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
	
}

// end

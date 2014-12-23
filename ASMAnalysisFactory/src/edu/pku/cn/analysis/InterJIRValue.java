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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.objectweb.asm.Type;

import edu.pku.cn.jir.ArrayRef;
import edu.pku.cn.jir.Expr;
import edu.pku.cn.jir.JIRValue;
import edu.pku.cn.jir.Ref;

/**
 * @author liangguangtai
 */
public class InterJIRValue extends Object {

	public JIRValue intraJIRValue;
	public int scopeLevel; 

	public InterJIRValue(JIRValue jirValue, int level) {
		this.intraJIRValue = jirValue;
		this.scopeLevel = level;	
	}

	@Override
	public boolean equals(Object interJIRValue) {
		if ( interJIRValue instanceof InterJIRValue) {
			InterJIRValue jirValue = (InterJIRValue)interJIRValue;
			if (this.scopeLevel != jirValue.scopeLevel) {
				return false;
			}
			if (this.intraJIRValue != null && !this.intraJIRValue.toString().equals(jirValue.intraJIRValue.toString())){
				return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		builder.append(this.intraJIRValue.toString());
		builder.append("::");
		builder.append(this.scopeLevel);
		builder.append("]");
		return builder.toString();
	}
	
	@Override
	public int hashCode(){
		return this.toString().hashCode();
	}
	
}

// end

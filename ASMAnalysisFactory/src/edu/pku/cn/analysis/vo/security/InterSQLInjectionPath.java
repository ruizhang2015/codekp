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

import edu.pku.cn.analysis.InterJIRValue;
import edu.pku.cn.jir.JIRValue;

/**
 * @author wuling
 */
public class InterSQLInjectionPath extends Object {

	private InterJIRValue value;
	private int currentLine;
	
	
	public InterJIRValue getValue() {
		return value;
	}
	public void setValue(InterJIRValue value) {
		this.value = value;
	}
	public int getCurrentLine() {
		return currentLine;
	}
	public void setCurrentLine(int currentLine) {
		this.currentLine = currentLine;
	}
	@Override
	public String toString() {
		return " value:"+value+" currentLine:"+currentLine;
	}
	
	
}

// end

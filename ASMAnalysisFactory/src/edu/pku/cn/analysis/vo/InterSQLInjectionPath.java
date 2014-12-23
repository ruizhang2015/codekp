/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-5 ����03:42:13
 * @modifier: a
 * @time 2010-1-5 ����03:42:13
 * @reviewer: a
 * @time 2010-1-5 ����03:42:13
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.analysis.vo;

import edu.pku.cn.jir.JIRValue;

/**
 * @author wuling
 */
public class InterSQLInjectionPath extends Object {

	private JIRValue value;
	private int currentLine;
	
	
	public JIRValue getValue() {
		return value;
	}
	public void setValue(JIRValue value) {
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
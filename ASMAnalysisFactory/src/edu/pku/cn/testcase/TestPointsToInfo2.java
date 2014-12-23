/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time Oct 20, 2008 4:22:59 PM
 * @modifier: Administrator
 * @time Oct 20, 2008 4:22:59 PM
 * @reviewer: Administrator
 * @time Oct 20, 2008 4:22:59 PM
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.testcase;

import java.io.FileNotFoundException;
import java.sql.*;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.objectweb.asm.Type;

import edu.pku.cn.jir.JIRValue;
import edu.pku.cn.jir.Switch;

public class TestPointsToInfo2 {
 
	Set<String> set;
	String str;
	int i;
	boolean b = true;
	private Set<String> set2;
	Test test;
	
	public Set<String> isSetNull(){
		Test tmpTest = this.test;
		this.test.set = new HashSet<String>();
		return this.test.set;
	}

}

// end

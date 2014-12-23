/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-5 ����02:01:13
 * @modifier: a
 * @time 2010-1-5 ����02:01:13
 * @reviewer: a
 * @time 2010-1-5 ����02:01:13
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.point;

import org.objectweb.asm.Type;

import edu.pku.cn.jir.JIRValue;

/**
 * @author zhouzhiyi
 */
public abstract class Ref {
	public JIRValue node;
	public Ref pointTo=null;
	public Ref refs=null;
	public Type getType() {
		// TODO Auto-generated method stub
		return null;
	}
	public PointsToSet getPointsToSet() {
		// TODO Auto-generated method stub
		return null;
	}
}

// end

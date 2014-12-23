/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author liuxi
 * @time 2010-3-26
 * @modifier: liuxi
 * @time 2010-3-26
 * @reviewer: liuxi
 * @time 2010-3-26
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.util;

import org.objectweb.asm.Type;

/**
 * @author liuxi
 */
public interface HA {
	boolean isAssignableFrom(final Type t, final Type u); 
	boolean isInstanceOf(final Type t, final Type u); 
	boolean isInterface(final Type t);
}
// end

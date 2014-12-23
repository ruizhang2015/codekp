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
public class HASelect {
	public static boolean useClass=true;
	public static HA se;
	static{
		if(useClass)
			se=new Verifier();
		else
			se=new RepositoryHA();
	}
	public static boolean isAssignableFrom(final Type t, final Type u){
		return se.isAssignableFrom(t, u);
	}
	public static boolean isInstanceOf(final Type t, final Type u){
		return se.isInstanceOf(t, u);
	}
	public static boolean isInterface(final Type t){
		return se.isInterface(t);
	}
}

// end

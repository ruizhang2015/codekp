/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author liuxi
 * @time 2010-3-17
 * @modifier: liuxi
 * @time 2010-3-17
 * @reviewer: liuxi
 * @time 2010-3-17
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.hierarchy;
/**
 * @author liuxi
 */
public class ClassNodeFinder {
	public static long ELFhash(String str){
		long h=0;
		long g;
		for(int i=0;i<str.length();i++){
			h=(h<<4)+str.charAt(i);
			g=h&0xF0000000;
			if(g==0)
				h^=g>>24;
			h&=~g;
		}
		return h;
	}
	
}

// end

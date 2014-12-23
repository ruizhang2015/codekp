/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-7-21 ����04:12:58
 * @modifier: Liuxizhiyi
 * @time 2008-7-21 ����04:12:58
 * @reviewer: Liuxizhiyi
 * @time 2008-7-21 ����04:12:58
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.cfg.test;

import java.util.HashMap;

import edu.pku.cn.Project;

/**
 * 
 * @author Liuxizhiyi
 */
public class LiveStoreTest {
	int x = 0;
	String xx;

	protected String getCommonSuperClass(final String type1, final String type2) {
		
		HashMap<Integer,String> pp=new HashMap<Integer, String>();
		Object os[][]=new Object[2][3];
		os[0][2]=type1;
		xx=testSwitch();	
		xx=(String) os[0][2];
		pp.put(1, xx);		
		String str=pp.get(1);
		Class c;
		try {			
			c = Class.forName(type1.replace('/', '.'));
			//d=c;
			int i=this.x;
			x=i;
			do{
				i=(i+2)/i;
			}while(c.isInterface()||i>0);
			return str;
//			d = Class.forName(type2.replace('/', '.'));
		} catch (Exception e) {
			
			throw new RuntimeException(e.toString());
		}
	}

	//	   
	// public void t(){
	// boolean t=false;
	// if(t){
	// int a=1;
	// //t=(a+1)>0;
	// }
	// else{
	// int a=0;
	// t=(a+1)>0;
	// a=2;
	// a=a<<2;
	// }
	// if(t){
	// Object.class.toString();
	// }
	// }
	//	   
	// public void testReaching(){
	// int i=1;
	// int j=2;
	// int a=3;
	// do{
	// i=i+1;
	// j=j-1;
	// if(j>0)
	// a=i-1;
	// //i=a+1;
	// a=1;
	// }while(i>10);
	// }
	String testSwitch() {

		String desc = "";
		getCommonSuperClass(desc, desc);
		int dims = 1;
		Integer data;
		try {
			while (desc.charAt(dims) == '[') {
				dims=dims+2*dims;
			}
		} catch (RuntimeException e) {
			e.toString();
		} catch (Exception e) {
			e.toString();
		}
		switch (desc.charAt(dims)) {
		case 'Z':
			data = 1;
			break;
		case 'C':
			data = 2;
			break;
		case 'B':
			data = 3;
			break;
		case 'S':
			data = 4;
			break;
		case 'I':
			data = 5;
			break;
		case 'F':
			data = 6;
			break;
		case 'J':
			data = 7;
			break;
		case 'D':
			data = 8;
			break;
		// case 'L':
		default:
			// stores the internal name, not the descriptor
			// t = desc.substring(dims + 1, desc.length() - 1);
			data = 10; // | cw.addType(t);
		}
		Object d;
		switch (data) {
		case 1:
			
			d = "";
			{
				Object c="";
				c.toString();
			}
			break;
		case 2:
		{
			Object c="";
			c.toString();
		}
			d = "d";
			d.toString();
			break;
		}
		if (data > 0) {
			Object cc = null;
			Object dd = null;
			cc = "".toCharArray();
			dd = "".toString();
		} else {
			Object dd = null;
			dd = "asdf";
			dd.toString();
		}
		Object ee;
		if (data > 0) {
			ee = "";
		} else {
			ee = new Object();
		}
		ee.toString();
		// dd.toString();
		return desc;
	}

	public static void main(String[] args) {
		Project project = new Project("C:/eclipse/workspace/ASMRebuild/bin/");
		// Project project=new Project();
		// project.setProjectPath("C:/eclipse/workspace/ASMRebuild/bin/");
		// project.addClass("org.objectweb.asm.ClassReader");
		project.excute();
	}
}

// end

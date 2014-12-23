/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author root
 * @time 2009-12-9 下午02:50:27
 * @modifier: root
 * @time 2009-12-9 下午02:50:27
 * @reviewer: root
 * @time 2009-12-9 下午02:50:27
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.util;

public class MemorySpace {
	static long freememory;

	public static void start() {
		Runtime runtime = Runtime.getRuntime();
		freememory = runtime.freeMemory();
		System.out.println("total:" + runtime.totalMemory() + "max memory:" + runtime.maxMemory() + "free memory:"
				+ runtime.freeMemory());
	}

	public static long stop() {
		Runtime runtime = Runtime.getRuntime();
		runtime.runFinalization();
		runtime.gc();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long free = runtime.freeMemory() - freememory;
		System.out.println("total:" + runtime.totalMemory() + "max memory:" + runtime.maxMemory() + "free memory:"
				+ runtime.freeMemory());
		return free;
	}
}

// end

/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author root
 * @time 2009-12-9 下午02:06:48
 * @modifier: root
 * @time 2009-12-9 下午02:06:48
 * @reviewer: root
 * @time 2009-12-9 下午02:06:48
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.util;

import java.util.Date;
import java.util.HashMap;

public class Timer {
	static Timer instance = null;
	static HashMap<Object, Date> timers = new HashMap<Object, Date>();

	private Timer() {
	}

	public synchronized static Timer getInstance() {
		if (instance == null)
			instance = new Timer();
		return instance;
	}

	public synchronized static void start(Object key) {
		timers.put(key, new Date());
	}

	public synchronized static long showTime(Object key, boolean reStart) {
		Date newTime = new Date();
		Date date = timers.get(key);
		if (reStart)
			timers.put(key, newTime);
		return newTime.getTime() - date.getTime();
	}

	public synchronized static long stop(Object key) {
		Date newTime = new Date();
		Date date = timers.get(key);
		timers.remove(key);
		return newTime.getTime() - date.getTime();
	}
}

// end

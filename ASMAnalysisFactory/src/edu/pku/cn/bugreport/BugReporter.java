/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-6-22 ����09:10:24
 * @modifier: Liuxizhiyi
 * @time 2008-6-22 ����09:10:24
 * @reviewer: Liuxizhiyi
 * @time 2008-6-22 ����09:10:24
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.bugreport;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import java.util.Map.Entry;

/**
 * 
 * @author Liuxizhiyi
 */
public class BugReporter {

	PrintStream stream = null;
	PrintWriter writer = null;

	public void setPrint(PrintStream stream) {
		this.stream = stream;
	}

	public void setPrint(PrintWriter writer) {
		this.writer = writer;
	}

	Map<String, TreeSet<BugInstance>> bugMap = new HashMap<String, TreeSet<BugInstance>>();;

	public int getBugCount(){
		return bugMap.size();
	}
	public void report(String className, BugInstance instance) {

		TreeSet<BugInstance> bugs = bugMap.get(className);
		if (bugs == null) {
			bugs = new TreeSet<BugInstance>();
			bugMap.put(className, bugs);
		}
		if (!bugs.contains(instance))
			bugs.add(instance);
	}

	public void write() {
		if (stream == null)
			write(System.out);
		else
			write(stream);
	}

	public void write(PrintStream ps) {
		Iterator<Entry<String, TreeSet<BugInstance>>> mapIter = bugMap.entrySet().iterator();
		while (mapIter.hasNext()) {
			Entry<String, TreeSet<BugInstance>> entry = mapIter.next();
			if (entry.getValue().size() > 0) {
				String key = entry.getKey();
				// comment by lijinhui
				// ps.format("%s\n", entry.getKey());
				Iterator<BugInstance> iter = entry.getValue().iterator();
				while (iter.hasNext()) {
					iter.next().write(key, ps);
				}
			}
		}
	}

	public void write(PrintWriter pw) {
		Iterator<Entry<String, TreeSet<BugInstance>>> mapIter = bugMap.entrySet().iterator();
		while (mapIter.hasNext()) {
			Entry<String, TreeSet<BugInstance>> entry = mapIter.next();
			if (entry.getValue().size() > 0) {
				String key = entry.getKey();
				Iterator<BugInstance> iter = entry.getValue().iterator();
				while (iter.hasNext()) {
					iter.next().write(key, pw);
				}
			}
		}
	}
}

// end

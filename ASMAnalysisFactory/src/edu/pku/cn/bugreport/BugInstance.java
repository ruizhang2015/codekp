/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-6-22 ����05:02:19
 * @modifier: Liuxizhiyi
 * @time 2008-6-22 ����05:02:19
 * @reviewer: Liuxizhiyi
 * @time 2008-6-22 ����05:02:19
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.bugreport;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.MessageFormat;

import edu.pku.cn.user.Priorities;

/**
 * 
 * @author Liuxizhiyi
 */
public class BugInstance implements Comparable<BugInstance>, Serializable, Cloneable {
	private static final long serialVersionUID = -1529960120624178473L;

	int type;
	int priority = Priorities.NORMAL_PRIORITY;

	int line;
	String message;

	// tempory
	public BugInstance(String message, int line) {
		this.message = message;
		this.line = line;
	}

	// added by Meng Na, this is added for ClassVisitor since some defects do
	// not correspond to any line
	public BugInstance(String message) {
		this.message = message;
	}

	public static String format(String pattern, Object[] args) {
		return MessageFormat.format(pattern, args);
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(BugInstance o) {
		int cmp = type - o.type;
		if (cmp != 0)
			return cmp;
		cmp = priority - o.priority;
		if (cmp != 0)
			return cmp;
		cmp = line - o.line;
		if (cmp != 0)
			return cmp;
		return message.compareTo(o.message);
	}

	@Override
	public Object clone() {
		BugInstance dup;
		try {
			dup = (BugInstance) super.clone();

			return dup;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError(e);
		}
	}

	public void raisePriority() {
		Priorities.boundedPriority(priority + 1);
	}

	public void lowerPriority() {
		Priorities.boundedPriority(priority - 1);
	}

	public void setPriority(int priority) {
		this.priority = Priorities.boundedPriority(priority);
	}

	public void write(PrintStream ps) {
		ps.format("Line:%d %s  %s\n", line, "Normal", message);
	}

	// add by lijinhui
	public void write(String preStr, PrintStream ps) {
		if(preStr.contains("$")){
			preStr=preStr.substring(0,preStr.indexOf('$'));
	}
		ps.format("(" + preStr + ".java:%d) %s  %s\n", line, "Normal", message);
	}

	// added by MengNa
	public void write(String preStr, PrintWriter pw) {
		pw.format("(" + preStr + ".java:%d) %s  %s\n", line, "Normal", message);
	}
}

// end

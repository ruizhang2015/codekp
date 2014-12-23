/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-6-1 ����01:15:22
 * @modifier: Liuxizhiyi
 * @time 2008-6-1 ����01:15:22
 * @reviewer: Liuxizhiyi
 * @time 2008-6-1 ����01:15:22
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.classfile;

/**
 * 
 * @author Liuxizhiyi
 */
public class Region implements Comparable<Region> {
	public int start;
	public int end;
	public int index;

	public Region(int start, int end, int index) {
		this.start = start;
		this.end = end;
		this.index = index;
	}

	public Region(int start, int index) {
		this(start, start, index);
	}

	public boolean equals(Object o) {
		if (o instanceof Region) {
			Region r = (Region) o;
			if (r.start >= start && r.end <= end && r.index == index)
				return true;
		}
		return false;
	}

	public boolean equals(int index) {
		return (index >= start && index <= end) ? true : false;
	}

	public boolean equals(int start, int index) {
		return (start >= this.start && start <= end && this.index == index) ? true : false;
	}

	public String toString() {
		return "pc " + start + " to " + end;
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Region o) {
		// TODO Auto-generated method stub
		return 0;
	}
}

// end

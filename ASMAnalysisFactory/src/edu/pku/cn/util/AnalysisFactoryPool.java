/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author root
 * @time 2009-12-21 下午02:20:36
 * @modifier: root
 * @time 2009-12-21 下午02:20:36
 * @reviewer: root
 * @time 2009-12-21 下午02:20:36
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.util;

import java.util.ArrayList;

import edu.pku.cn.analysis.AnalysisFactory;

public class AnalysisFactoryPool {
	private int pos;//
	private ArrayList<AnalysisFactory> analysises;// free pools
	private int size;
	AnalysisFactory fac;

	public AnalysisFactoryPool(int size, AnalysisFactory fac) {
		this.size = size;
		pos = 0;
		this.fac = fac;
		analysises = new ArrayList<AnalysisFactory>(size);
		for (int i = 0; i < this.size; i++)
			try {
				analysises.add(fac.clone());
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public synchronized AnalysisFactory getFactory() {
		pos = (pos + 1) % size;
		return analysises.get(pos);
		// if the execute time of program is huge and much of time is spend
		// here. used the code as follow
		// try {
		// return fac.clone();
		// } catch (CloneNotSupportedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// return null;
	}

	public synchronized void resize() {
		analysises.ensureCapacity(size * 2);
		for (int i = size; i < analysises.size(); i++) {
			try {
				analysises.add(fac.clone());
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		size = analysises.size();
	}
}

// end

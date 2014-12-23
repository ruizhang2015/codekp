/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author root
 * @time 2009-12-22 上午11:11:02
 * @modifier: root
 * @time 2009-12-22 上午11:11:02
 * @reviewer: root
 * @time 2009-12-22 上午11:11:02
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.util;

import java.util.HashMap;
import java.util.IdentityHashMap;

public class AnalysisMap {

	static AnalysisMap instance = null;
	HashMap<String, IdentityHashMap<Object, Object>> factorys;

	private AnalysisMap() {
		factorys = new HashMap<String, IdentityHashMap<Object, Object>>();
	}

	public synchronized static AnalysisMap value() {
		if (instance == null)
			instance = new AnalysisMap();
		return instance;
	}

	public synchronized void registe(String name) {
		factorys.put(name, new IdentityHashMap<Object, Object>());
	}

	public synchronized Object getAnalysis(String name, Object target) {
		IdentityHashMap<Object, Object> map;
		// synchronized (this) {
		map = factorys.get(name);
		if (map == null)
			return null;
		// }
		// synchronized(map){
		return map.get(target);
		// }
	}

	public synchronized void put(String name, Object target, Object value) {
		IdentityHashMap<Object, Object> map;
		// synchronized (this) {
		map = factorys.get(name);
		if (map == null)
			return;
		// }
		map.put(target, value);
	}
}

// end

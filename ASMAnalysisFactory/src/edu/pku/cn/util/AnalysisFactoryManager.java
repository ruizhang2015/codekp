/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author root
 * @time 2009-12-14 下午02:08:30
 * @modifier: root
 * @time 2009-12-14 下午02:08:30
 * @reviewer: root
 * @time 2009-12-14 下午02:08:30
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.util;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import edu.pku.cn.analysis.AnalysisFactory;
import edu.pku.cn.analysis.factory.IsNullValueDataflowFactory;
import edu.pku.cn.analysis.factory.LiveVariableDataflowFactory;
import edu.pku.cn.analysis.factory.LockDataflowFactory;
import edu.pku.cn.analysis.factory.ObjectMethodInvokeDataFlowFactory;
import edu.pku.cn.analysis.factory.RealValueDataflowFactory;
import edu.pku.cn.graph.cfg.CFGFactory;
import edu.pku.cn.ptg.PTGFactory;

public class AnalysisFactoryManager {
	// public static RealValueDataflowFactory realValueDataflowFactory; //Meng
	// Na
	// public static LockDataflowFactory lockDataflowFactory; //Meng Na
	// public static LiveVariableDataflowFactory liveVariableDataflowFactory;
	// //Meng Na
	// public static IsNullValueDataflowFactory isNullValueDataflowFactory;
	// //Meng Na
	// public static ObjectMethodInvokeDataFlowFactory
	// objectMethodInvokeDataFlowFactory;//lijinhui
	public static int max = 10;// maximum size of pool
	// private static AnalysisFactoryManager instance=new
	// AnalysisFactoryManager();
	private static AnalysisMap analysisMap = AnalysisMap.value();
	private static HashMap<String, AnalysisFactoryPool> factorys = new HashMap<String, AnalysisFactoryPool>();

	private AnalysisFactoryManager() {
		factorys = new HashMap<String, AnalysisFactoryPool>();
	}

	// public synchronized static AnalysisFactoryManager value(){
	// if(instance==null)
	// instance=new AnalysisFactoryManager();
	// return instance;
	// }

	public synchronized static void registe(String key, Class<?> c) throws AnalysisFactoryManagerException {
		try {
			if (factorys.containsKey(key) == false) {
				Constructor<?> constructor = c.getConstructor(null);
				factorys.put(key, new AnalysisFactoryPool(max, (AnalysisFactory) constructor.newInstance(null)));
			}
		} catch (Exception e) {
			throw new AnalysisFactoryManagerException(e);
		}
	}

	public synchronized static void registe(String key, AnalysisFactory c) {
		if (factorys.containsKey(key) == false) {
			factorys.put(key, new AnalysisFactoryPool(max, c));
			analysisMap.registe(key);
		}
	}

	public synchronized static AnalysisFactory lookup(String key) {
		if (factorys.containsKey(key)) {
			AnalysisFactoryPool pool = factorys.get(key);
			return pool.getFactory();
		}
		return null;
	}

	@Deprecated
	/*
	 * deprecated, it will be replace later by the read from configuration files
	 */
	public static void initial() {
		// instance=new AnalysisFactoryManager();
		registe("RealValueDataflowFactory", new RealValueDataflowFactory());
		registe("LockDataflowFactory", new LockDataflowFactory());
		registe("LiveVariableDataflowFactory", new LiveVariableDataflowFactory());
		registe("IsNullValueDataflowFactory", new IsNullValueDataflowFactory());
		registe("ObjectMethodInvokeDataFlowFactory", new ObjectMethodInvokeDataFlowFactory());
		registe(CFGFactory.NAME, new CFGFactory());
		registe(PTGFactory.NAME,new PTGFactory());
	}
}

// end

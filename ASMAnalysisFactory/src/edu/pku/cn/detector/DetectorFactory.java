/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-6-30 ����12:29:19
 * @modifier: Liuxizhiyi
 * @time 2008-6-30 ����12:29:19
 * @reviewer: Liuxizhiyi
 * @time 2008-6-30 ����12:29:19
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.detector;

import java.util.ArrayList;

import edu.pku.cn.bugreport.BugReporter;

/**
 * 
 * @author Liuxizhiyi
 */
public class DetectorFactory {
	private static DetectorFactory instance;

	private DetectorFactory() {
	}

	public static DetectorFactory instance() {
		if (instance == null)
			instance = new DetectorFactory();
		return instance;
	}

	// private static final Class[] constructorArgTypes = new Class[]{};
	public ArrayList<Detector> getDector(BugReporter reporter, Class<Detector> detectorClass) {
		try {
			Detector detector = detectorClass.newInstance();
			ArrayList<Detector> detectors = detector.getInstances();
			for (Detector detec : detectors)
				detec.setBugReporter(reporter);
			return detectors;
		} catch (Exception e) {
			throw new RuntimeException("Could not instantiate " + detectorClass.getName() + " as Detector", e);
		}
	}
}

// end

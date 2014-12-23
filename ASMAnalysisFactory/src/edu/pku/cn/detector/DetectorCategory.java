/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-6-22 ����09:19:49
 * @modifier: Liuxizhiyi
 * @time 2008-6-22 ����09:19:49
 * @reviewer: Liuxizhiyi
 * @time 2008-6-22 ����09:19:49
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.detector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.pku.cn.bugreport.BugReporter;
import edu.pku.cn.xml.detectorConfiger.CategoryType;
import edu.pku.cn.xml.detectorConfiger.DetectorType;

/**
 * 
 * @author Liuxizhiyi
 */
public class DetectorCategory {

	String name;
	int priority;
	String packageName;
	public List<Detector> detectors;
	List<BugReporter> reporters;

	public void clear() {
		if (detectors != null)
			detectors.clear();
		if (reporters != null)
			reporters.clear();
		bugCount=0;
	}

	public DetectorCategory(CategoryType category, String path) {
		detectors = new ArrayList<Detector>();
		reporters = new ArrayList<BugReporter>();

		DetectorLoader loader = new DetectorLoader(path);
		DetectorFactory factory = DetectorFactory.instance();

		name = category.getName();
		priority = Integer.valueOf(category.getPriority());
		packageName = category.getPackageName();
		Iterator<DetectorType> iter = category.getDetector().iterator();
		while (iter.hasNext()) {
			try {
				DetectorType detectorType = iter.next();
				if (Boolean.valueOf(detectorType.getDisabled()) == false) {
					Class<Detector> c = (Class<Detector>) loader.loadClass(detectorType.getClassName());
					BugReporter reporter = new BugReporter();
					reporters.add(reporter);

					ArrayList<Detector> detectorsInstances = factory.getDector(reporter, c);
					// if(detectors!=null)
					for (Detector detector : detectorsInstances) {
						detector.initBugPattern(name, detectorType.getClassName());
						// if(!detector.isNeedCFG())
						// detector.setNeedCFG(Boolean.valueOf(detectorType.getHidden()));
						detectors.add(detector);
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public String getName() {
		return name;
	}

	public Iterator<Detector> detectorIteraotr() {
		return detectors.iterator();
	}
	int bugCount=0;
	public int getBugCount(){
		return bugCount;
	}
	public int getDetectorSize(){
		return detectors.size();
	}
	public void report() {
		for (int i = 0; i < reporters.size(); i++) {
			BugReporter repo=reporters.get(i);
			bugCount+=repo.getBugCount();
			repo.write();
		}
		// try {
		// PrintWriter pw = new PrintWriter(
		// new BufferedWriter(
		// new FileWriter("result_for_jboss-remoting.txt", true)), true);
		// for(int i = 0; i < reporters.size(); i ++){
		// reporters.get(i).write(pw);
		// }
		// pw.close();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}
}

// end

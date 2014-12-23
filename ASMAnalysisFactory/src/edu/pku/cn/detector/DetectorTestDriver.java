/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author root
 * @time 2009-12-23 下午03:54:41
 * @modifier: root
 * @time 2009-12-23 下午03:54:41
 * @reviewer: root
 * @time 2009-12-23 下午03:54:41
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.detector;

import edu.pku.cn.bugreport.BugReporter;
import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.classfile.ClassNodeLoader;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.util.AnalysisFactoryManager;
import edu.pku.cn.util.CodaProperties;
import edu.pku.cn.xml.messages.MessageService;

public abstract class DetectorTestDriver {
	public DetectorTestDriver() {
		AnalysisFactoryManager.initial();
		MessageService.loadMessage(System.getProperty("user.dir") + CodaProperties.e().getMessage());
	}

	public void execute(String className) {

		ClassNodeLoader loader = new ClassNodeLoader("bin/edu/pku/cn/testcase/");
		ClassNode cc = loader.loadClassNode(className,0);

		for (MethodNode method : cc.methods) {
			execute(cc, method);
		}
	}

	public void execute(ClassNode cc, MethodNode method) {
		BugReporter reporter = new BugReporter();
		Detector detector = createDetector(reporter);
		if (detector instanceof ClassDetector)
			cc.accept(detector);
		else {
			detector.setReference(method);
			method.accept(detector);
		}
		reporter.write();
	}

	public abstract Detector createDetector(BugReporter reporter);
}

// end

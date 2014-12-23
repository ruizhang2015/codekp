/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author root
 * @time 2009-11-9 下午02:17:51
 * @modifier: root
 * @time 2009-11-9 下午02:17:51
 * @reviewer: root
 * @time 2009-11-9 下午02:17:51
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import org.jboss.remoting.InvokerLocator;
import org.jboss.remoting.detection.Detection;
import org.jboss.remoting.detection.Detector;
import org.jboss.remoting.ident.Identity;

public class testJboss {
	private void getTest(testJboss o) {
		o.methodB();
	}

	public void testMessageConsumer(int i) {
		if (i > 0)
			getTest(new testJboss());
		else {
			testJboss b = new testJboss();
			getTest(b);
		}
	}

	public void methodA(org.jboss.remoting.detection.Detector dec) throws Exception {
		try {
			dec.start();
		} catch (Exception e) {
			return;
		} finally {
			try {
				dec.stop();
			} catch (Exception e) {
			}
		}
		for (int i = 0; i < 10; i++) {
			i++;
		}
	}

	public void methodB() {
		String[] defaultExclusions = {};
		Vector fExcluded = new Vector(10);
		for (int i = 0; i < defaultExclusions.length; i++)
			fExcluded.addElement(defaultExclusions[i]);
		InputStream is = getClass().getResourceAsStream("");
		if (is == null)
			return;
		Properties p = new Properties();
		try {
			p.load(is);
		} catch (IOException e) {
			return;
		} finally {
			try {
				is.close();
			} catch (IOException e) {

			}
		}
		for (Enumeration e = p.propertyNames(); e.hasMoreElements();) {
			String key = (String) e.nextElement();
			if (key.startsWith("excluded.")) {
				String path = p.getProperty(key);
				path = path.trim();
				if (path.endsWith("*"))
					path = path.substring(0, path.length() - 1);
				if (path.length() > 0)
					fExcluded.addElement(path);
			}
		}
	}

}

// end

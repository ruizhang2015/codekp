/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-10-28 ����02:35:14
 * @modifier: Liuxizhiyi
 * @time 2008-10-28 ����02:35:14
 * @reviewer: Liuxizhiyi
 * @time 2008-10-28 ����02:35:14
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.xml.JavaLib;

import java.io.File;
import java.net.URL;

import org.apache.commons.jxpath.Container;
import org.apache.commons.jxpath.xml.DocumentContainer;

/**
 * 
 * @author Liuxizhiyi
 */
public class JavaConfig {
	private Container javaConfigContainer;
	public String fileName;

	public JavaConfig(String url) {
		fileName = url;
	}

	public Container getJavaConfigContainer() {
		if (javaConfigContainer == null) {
			try {
				File file = new File(fileName);
				URL url = file.toURI().toURL();
				javaConfigContainer = new DocumentContainer(url);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return javaConfigContainer;
	}
}

// end

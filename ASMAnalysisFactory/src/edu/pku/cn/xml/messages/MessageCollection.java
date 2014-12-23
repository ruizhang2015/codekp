/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-7-20 ����02:35:54
 * @modifier: Liuxizhiyi
 * @time 2008-7-20 ����02:35:54
 * @reviewer: Liuxizhiyi
 * @time 2008-7-20 ����02:35:54
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.xml.messages;

import java.io.File;
import java.net.URL;

import org.apache.commons.jxpath.Container;
import org.apache.commons.jxpath.xml.DocumentContainer;

/**
 * 
 * @author Liuxizhiyi
 */
public class MessageCollection {
	private Container messageContainer;
	public String fileName;

	public MessageCollection(String url) {
		fileName = url;
	}

	public Container getMessageContainer() {
		if (messageContainer == null) {
			try {
				File file = new File(fileName);
				URL url = file.toURI().toURL();
				messageContainer = new DocumentContainer(url);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return messageContainer;
	}
}

// end

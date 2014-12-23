/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-7-20 ����02:37:22
 * @modifier: Liuxizhiyi
 * @time 2008-7-20 ����02:37:22
 * @reviewer: Liuxizhiyi
 * @time 2008-7-20 ����02:37:22
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.xml.messages;

import org.apache.commons.jxpath.JXPathContext;
import org.w3c.dom.Element;

/**
 * 
 * @author Liuxizhiyi
 */
public class MessageService {
	public static JXPathContext messageContext;
	private static final String BASE = "messageContainer/MessageCollection/";

	public static void loadMessage(String url) {
		messageContext = JXPathContext.newContext(new MessageCollection(url));
		messageContext.setLenient(true);
	}

	public static BugCategory getBugCategory(String name) {
		String context = BASE + "BugCategory[@category='" + name + "']";
		Element element = (Element) messageContext.selectSingleNode(context);
		BugCategory category = MessagesFactory.eINSTANCE.createBugCategory(element);
		return category;
	}

	public static Detector getDetector(String category, String className) {
		String context = BASE + "BugCategory[@category='" + category + "']/Detector[@className='" + className + "']";
		return MessagesFactory.eINSTANCE.createDetector((Element) messageContext.selectSingleNode(context));
	}
}

// end

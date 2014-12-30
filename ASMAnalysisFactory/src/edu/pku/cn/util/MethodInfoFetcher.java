/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author ZR-Private
 * @time Dec 30, 2014
 * @modifier: ZR-Private
 * @time Dec 30, 2014
 * @reviewer: ZR-Private
 * @time Dec 30, 2014
 * (C) Copyright PKU Software Lab. 2014
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tmatesoft.svn.core.wc.SVNRevision;

/**
 * @author ZR-Private
 */
public class MethodInfoFetcher {
	Map<String, MethodInfoPack> mipacks = new HashMap<String, MethodInfoPack>();

	public MethodInfoFetcher() {
		// TODO Auto-generated constructor stub
	}

	public void process(String url, String issueUrl, String rootPath, SVNRevision startRevision, SVNRevision endRevision) {
		SVNUtil su = new SVNUtil(url);
		
		System.out.println("SVN info fetch begins...");
		List<String> logPaths = new ArrayList<String>();
		for (String fileName : su.svnList(url + rootPath, endRevision, endRevision)) {
			logPaths.add(rootPath + fileName);
		}

		su.svnLog(logPaths.toArray(new String[0]), startRevision, endRevision);
		// su.getCommitinfo();
		for (String annotatePath : logPaths) {
			// clear linemsg! [Done]
			//String annotatePath1 =
			//"/tomcat/trunk/java/org/apache/catalina/valves/StuckThreadDetectionValve.java";
			su.svnAnnotate(url + annotatePath, startRevision, endRevision);
			List<String[]> linemsgs = su.getLinemsg();
			List<String> issueIds = su.getIssueidFrLinemsg();
			System.out.println("Issue info fetch begins...");
			IssueTrackerUtil itu = new IssueTrackerUtil();
			XmlUtils xu = new XmlUtils();
			
			String[] res = null;
			for (String id : issueIds) {
				System.out.println("get issue info for issueId " + id);
				String filename = id + ".xml";
				try {
					File file = new File(filename);
					if (!file.exists()) {
						file.createNewFile();
						String xmlctnt = itu.getIssueXml(issueUrl);
						FileWriter fw = new FileWriter(file.getAbsoluteFile());
						BufferedWriter bw = new BufferedWriter(fw);
						bw.write(xmlctnt);
						bw.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				res = xu.parserXml(filename);
				//System.out.println(res[0]);
				//System.out.println(res[1]);
			}
			String tmp = "";
			if (res != null){
				for (int i = 0; i < res.length; i++)
					tmp += res[i] + "\n";
			}
			mipacks.put(annotatePath, new MethodInfoPack(annotatePath, linemsgs.get(0)[1], tmp));
			//break;
		}
		
		System.out.println();
		for (MethodInfoPack mip : mipacks.values()){
			System.out.println(mip);
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		String url = "http://svn.apache.org/repos/asf"; //svn url
		String issueUrl = "https://issues.apache.org/bugzilla/show_bug.cgi?ctype=xml&id=";
		String rootPath = "/tomcat/trunk/java/org/apache/catalina/valves/";
		//String rootPath = "/tomcat/trunk/java/org/apache/catalina/";
		SVNRevision startRevision = SVNRevision.create(1090003);
		SVNRevision endRevision = SVNRevision.create(1090003);
		
		MethodInfoFetcher miFetcher = new MethodInfoFetcher();
		
		long startMili=System.currentTimeMillis();
		miFetcher.process(url, issueUrl, rootPath, startRevision, endRevision);
		long endMili=System.currentTimeMillis();
		System.out.println("Process End!\nTotal time cost: "+(endMili-startMili)/1000 +"s");
	}

}

// end

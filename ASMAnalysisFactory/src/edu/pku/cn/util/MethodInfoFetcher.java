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

import org.junit.experimental.max.MaxCore;
import org.tmatesoft.svn.core.wc.SVNRevision;

import com.mysql.jdbc.MiniAdmin;
import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * @author ZR-Private
 */
public class MethodInfoFetcher {

	public MethodInfoFetcher() {
		// TODO Auto-generated constructor stub
	}

	public Map<String, Map<String, MethodInfoPack>> process(String url, String issueUrl, String rootPath, SVNRevision startRevision, SVNRevision endRevision) throws Exception {
		SVNUtil su = new SVNUtil(url);
		
		System.out.println("SVN info fetch begins...");
		List<String> logPaths = new ArrayList<String>();
		for (String fileName : su.svnList(url + rootPath, endRevision, endRevision)) {
			logPaths.add(rootPath + fileName);
		}

		su.svnLog(logPaths.toArray(new String[0]), startRevision, endRevision);
		
		MethodAnalysis ma = new MethodAnalysis(); //bound analysis
		Map<String, Map<String, MethodInfoPack>> fres = new HashMap<String, Map<String, MethodInfoPack>>();
		// su.getCommitinfo();
		for (String annotatePath : logPaths) {
			// clear linemsg! [Done]
			String annotatePath1 = "/tomcat/trunk/java/org/apache/catalina/valves/ValveBase.java";
			//"/tomcat/trunk/java/org/apache/catalina/valves/StuckThreadDetectionValve.java";
			su.svnAnnotate(url + annotatePath, startRevision, endRevision);
			List<String[]> linemsgs = su.getLinemsg();		
			
			//List<String> issueIds = su.getIssueidFrLinemsg();
			System.out.println("Issue info fetch begins...");
			IssueTrackerUtil itu = new IssueTrackerUtil();
			XmlUtils xu = new XmlUtils();
			
			Map<String, String[]> res = new HashMap<String, String[]>();
			/*String tmp = "";
			for (String id : issueIds) {
				System.out.println("get issue info for issueId " + id);
				String filename = "xml/" + id + ".xml";
				try {
					File file = new File(filename);
					if (!file.exists()) {
						file.createNewFile();
						String xmlctnt = itu.getIssueXml(issueUrl + id);
						FileWriter fw = new FileWriter(file.getAbsoluteFile());
						BufferedWriter bw = new BufferedWriter(fw);
						bw.write(xmlctnt);
						bw.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				String[] tmpr = xu.parserXml(filename);
				res.put(id, tmpr);
				
				//for output need
				tmp += "(" + id + ")";
					for (int i = 0; i < tmpr.length; i++)
						tmp += tmpr[i] + "\n";
				//System.out.println(res[0]);
				//System.out.println(res[1]);
			}*/
			Map<String, Integer[]> bounds = ma.analysis(annotatePath.substring(0, annotatePath.length()-5));	
			
			
			Map<String, MethodInfoPack> classres = new HashMap<String, MethodInfoPack>();
			for (String method : bounds.keySet()){
				Map<String, String[]> commitInfoList = new HashMap<String, String[]>();
				Map<String, String[]> issueInfoList = new HashMap<String, String[]>();
				Integer[] bound = bounds.get(method);
				for (int i = bound[0]-1; i<bound[1]-1; i++){
					String[] lineStrings = linemsgs.get(i);
					String revison = lineStrings[0];
					if (!commitInfoList.containsKey(revison)){
						commitInfoList.put(revison, lineStrings);
					}
					String id = su.getIssueidFrLinemsg(lineStrings);
					if (id != null){
						if (!res.containsKey(id)){
						System.out.println("get issue info for issueId " + id);
						String filename = "xml/" + id + ".xml";
						try {
							File file = new File(filename);
							if (!file.exists()) {
								file.createNewFile();
								String xmlctnt = itu.getIssueXml(issueUrl + id);
								FileWriter fw = new FileWriter(file.getAbsoluteFile());
								BufferedWriter bw = new BufferedWriter(fw);
								bw.write(xmlctnt);
								bw.close();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
						String[] tmpr = xu.parserXml(filename);
						res.put(id, tmpr);
						}
						issueInfoList.put(revison, res.get(id));	
					}
				}
				classres.put(method, new MethodInfoPack(annotatePath, method, commitInfoList, issueInfoList, bound[0], bound[1]));
			}
			fres.put(annotatePath1, classres);
			break;
		}
		
		return fres;
	}
	
	public static void main(String[] args) throws Exception {
		String url = "http://svn.apache.org/repos/asf"; //svn url
		String issueUrl = "https://issues.apache.org/bugzilla/show_bug.cgi?ctype=xml&id=";
		String rootPath = "/tomcat/trunk/java/org/apache/catalina/valves/";
		//String rootPath = "/tomcat/trunk/java/org/apache/catalina/";
		SVNRevision startRevision = SVNRevision.create(0);
		SVNRevision endRevision = SVNRevision.create(1090003);
		
		MethodInfoFetcher miFetcher = new MethodInfoFetcher();
		
		long startMili=System.currentTimeMillis();
		miFetcher.process(url, issueUrl, rootPath, startRevision, endRevision);
		long endMili=System.currentTimeMillis();
		System.out.println("Process End!\nTotal time cost: "+(endMili-startMili)/1000 +"s");
	}

}

// end

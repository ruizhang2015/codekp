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
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.tmatesoft.sqljet.core.internal.lang.SqlParser.analyze_stmt_return;
import org.tmatesoft.svn.core.wc.SVNRevision;

import com.mysql.jdbc.MiniAdmin;
import com.sun.org.apache.bcel.internal.generic.NEW;

import edu.pku.cn.analysis.InterValueDataflowAnalysis;
import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.classfile.ClassNodeLoader;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.graph.cfg.CFG;
import edu.pku.cn.jir.JIRValue;
import edu.pku.cn.jir.Stmt;

/**
 * @author ZR-Private
 */
public class MethodInfoFetcher {
	SVNUtil su;
	JavadocUtils ju;
	XmlUtils xu;
	IssueTrackerUtil itu;

	String url;
	String issueUrl;
	String rootPath;
	SVNRevision startRevision;
	SVNRevision endRevision;

	public MethodInfoFetcher(String url, String issueUrl, String rootPath, SVNRevision startRevision,
			SVNRevision endRevision) {
		su = new SVNUtil(url);
		ju = new JavadocUtils();
		xu = new XmlUtils();
		itu = new IssueTrackerUtil();
		this.url = url;
		this.issueUrl = issueUrl;
		this.rootPath = rootPath;
		this.startRevision = startRevision;
		this.endRevision = endRevision;
	}

	public Map<String, Map<String, MethodInfoPack>> process() throws Exception {

		AnalysisFactoryManager.initial();

		System.out.println("SVN info fetch begins...");
		List<String> logPaths = new ArrayList<String>();
		for (String fileName : su.svnList(url + rootPath, endRevision, endRevision)) {
			logPaths.add(rootPath + fileName);
		}

		su.svnLog(logPaths.toArray(new String[0]), startRevision, endRevision);

		// MethodAnalysis ma = new MethodAnalysis(); //bound analysis
		Map<String, Map<String, MethodInfoPack>> fres = new HashMap<String, Map<String, MethodInfoPack>>();
		// su.getCommitinfo();
		// String classname = ".." +
		// "/tomcat/trunk/java/org/apache/catalina/valves/AccessLogValve.java";
		for (String annotatePath1 : logPaths) {
			// clear linemsg! [Done]
			String annotatePath = "/tomcat/trunk/java/org/apache/catalina/valves/StuckThreadDetectionValve.java";
			// "/tomcat/trunk/java/org/apache/catalina/valves/StuckThreadDetectionValve.java";

			Map<String, MethodInfoPack> classres = processClass(annotatePath);

			fres.put(annotatePath, classres);
			break;
		}

		return fres;
	}

	private Map<String, MethodInfoPack> processClass(String annotatePath) {
		// Map<String, Integer[]> bounds = ma.analysis(annotatePath.substring(0,
		// annotatePath.length()-5));
		Map<String, MethodInfoPack> classres = new HashMap<String, MethodInfoPack>();
		Map<String, String[]> res = new HashMap<String, String[]>();
		ClassNodeLoader loader = new ClassNodeLoader("tomcatbin");
		String subjectClassName = annotatePath.substring(0, annotatePath.length() - 5);
		ClassNode cc = loader.loadClassNode(subjectClassName, 0);
		InterValueDataflowAnalysis.loader = loader;
		Map<JIRValue, String> tempValueMap = new HashMap<JIRValue, String>();

		System.out.println("Commit info fetch begins...");
		su.svnAnnotate(url + annotatePath, startRevision, endRevision);
		List<String[]> linemsgs = su.getLinemsg();

		System.out.println("Doc info fetch begins...");
		Map<String, String> docMap = ju.processDoc(".." + annotatePath);

		for (MethodNode method : cc.methods) {
			System.out.println();
			CFG cfg = method.getCFG();
			InterValueDataflowAnalysis anlysis = new InterValueDataflowAnalysis(tempValueMap, cfg);
			try {
				anlysis.execute();
				anlysis.examineResults();
				/*System.out.print("printList: ");
				for (String str : anlysis.printList) {
					System.out.println(str);
				}
				System.out.print("loggingList: ");
				for (String str : anlysis.loggingList) {
					System.out.println(str);
				}
				System.out.println();*/
			} catch (AnalyzerException e) {
				e.printStackTrace();
			}

			// bounds anaysis
			List<Stmt> stmtsList = method.getStmts();
			int min = Integer.MAX_VALUE;
			int max = Integer.MIN_VALUE;
			for (Stmt stmt : stmtsList) {

				String tmp = (stmt.toString());
				// System.out.println(tmp);
				if (tmp.startsWith("Line:")) {
					int no = Integer.parseInt(tmp.split(" ")[0].split(":")[1]);
					if (no > max)
						max = no;
					if (no < min)
						min = no;
				}
			}

			// info associate
			Map<String, String[]> commitInfoList = new HashMap<String, String[]>();
			Map<String, String[]> issueInfoList = new HashMap<String, String[]>();
			System.out.println("Issue info fetch begins...");
			for (int i = min - 1; i < max; i++) {
				String[] lineStrings = linemsgs.get(i);
				String revison = lineStrings[0];
				if (!commitInfoList.containsKey(revison)) {
					commitInfoList.put(revison, lineStrings);
				}
				String id = su.getIssueidFrLinemsg(lineStrings);
				if (id != null) {
					if (!res.containsKey(id)) {
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
						String[] tmpr = xu.parserXml(id, filename);
						res.put(id, tmpr);
					}
					issueInfoList.put(revison, res.get(id));
				}
			}
			String methodName = method.getFullName();
			// System.out.println(method);
			String mString = methodName;
			String method0 = methodName.split("\\(")[0];
			String doc = null;
			int[] is = { 0, 1, -1, 2, -2, 3, -3, 4, -4, 5, -5 };
			for (int i : is) {
				methodName = method0 + (min + i);
				if ((doc = docMap.get(methodName)) != null) {
					break;
				}
			}

			if (doc == null) {
				System.out.println(mString);
			}

			// System.out.println(method);
			if (classres.containsKey(methodName)) {
				System.out.println("repeated !!" + methodName);
			}

			classres.put(methodName, new MethodInfoPack(annotatePath, methodName, commitInfoList, issueInfoList, min,
					max, docMap.get(methodName), anlysis.getPrintList(), anlysis.getLoggingList()));
		}
		System.out.println(classres);
		/*
		 * for (String method : bounds.keySet()){ //System.out.println(method);
		 * Map<String, String[]> commitInfoList = new HashMap<String,
		 * String[]>(); Map<String, String[]> issueInfoList = new
		 * HashMap<String, String[]>(); Integer[] bound = bounds.get(method);
		 * for (int i = bound[0]-1; i<bound[1]; i++){ String[] lineStrings =
		 * linemsgs.get(i); String revison = lineStrings[0]; if
		 * (!commitInfoList.containsKey(revison)){ commitInfoList.put(revison,
		 * lineStrings); } String id = su.getIssueidFrLinemsg(lineStrings); if
		 * (id != null){ if (!res.containsKey(id)){
		 * System.out.println("get issue info for issueId " + id); String
		 * filename = "xml/" + id + ".xml"; try { File file = new
		 * File(filename); if (!file.exists()) { file.createNewFile(); String
		 * xmlctnt = itu.getIssueXml(issueUrl + id); FileWriter fw = new
		 * FileWriter(file.getAbsoluteFile()); BufferedWriter bw = new
		 * BufferedWriter(fw); bw.write(xmlctnt); bw.close(); } } catch
		 * (IOException e) { e.printStackTrace(); } String[] tmpr =
		 * xu.parserXml(filename); res.put(id, tmpr); }
		 * issueInfoList.put(revison, res.get(id)); } }
		 * //System.out.println(method); String mString = method; String method0
		 * = method.split("\\(")[0]; String doc = null; int[] is = {0, 1, -1, 2,
		 * -2, 3, -3, 4, -4, 5 ,-5}; for (int i: is){ method = method0 +
		 * (bound[0] + i); if ((doc=docMap.get(method))!=null){ break; } }
		 * 
		 * if (doc == null ){ System.out.println(mString); }
		 * 
		 * //System.out.println(method); if (classres.containsKey(method)){
		 * System.out.println("repeated !!" + method); }
		 * 
		 * classres.put(method, new MethodInfoPack(annotatePath, method,
		 * commitInfoList, issueInfoList, bound[0], bound[1],
		 * docMap.get(method))); } System.out.println(classres);
		 */
		return classres;

	}

	public static void main(String[] args) throws Exception {
		String url = "http://svn.apache.org/repos/asf"; // svn url
		String issueUrl = "https://issues.apache.org/bugzilla/show_bug.cgi?ctype=xml&id=";
		String rootPath = "/tomcat/trunk/java/org/apache/catalina/valves/";
		// String rootPath = "/tomcat/trunk/java/org/apache/catalina/";tttt
		SVNRevision startRevision = SVNRevision.create(0);
		SVNRevision endRevision = SVNRevision.create(1090003);

		MethodInfoFetcher miFetcher = new MethodInfoFetcher(url, issueUrl, rootPath, startRevision, endRevision);

		long startMili = System.currentTimeMillis();
		miFetcher.process();
		long endMili = System.currentTimeMillis();
		System.out.println("Process End!\nTotal time cost: " + (endMili - startMili) / 1000 + "s");
	}

}

// end

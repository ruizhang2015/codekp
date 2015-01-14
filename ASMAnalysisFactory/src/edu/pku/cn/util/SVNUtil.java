/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author ZR-Private
 * @time Dec 24, 2014
 * @modifier: ZR-Private
 * @time Dec 24, 2014
 * @reviewer: ZR-Private
 * @time Dec 24, 2014
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
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.tmatesoft.svn.core.ISVNDirEntryHandler;
import org.tmatesoft.svn.core.ISVNLogEntryHandler;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.ISVNConnector;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.ISVNAnnotateHandler;
import org.tmatesoft.svn.core.wc.ISVNConflictHandler;
import org.tmatesoft.svn.core.wc.ISVNMergerFactory;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNLogClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 * @author ZR-Private
 */

public class SVNUtil {
	private String svnRoot;
	private SVNLogClient svnlogclient;
	HashMap<Integer, String> commitinfo = new HashMap<Integer, String>();
	List<String[]> linemsg = new ArrayList<String[]>();
	List<String> paths = new ArrayList<String>();

	public SVNUtil(String svnRoot) {
		this.svnRoot = svnRoot;
		ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager();
		svnlogclient = new SVNLogClient(authManager, null);
	}

	public HashMap<Integer, String> getCommitinfo() {
		Set<Entry<Integer, String>> set = commitinfo.entrySet();
		Iterator<Entry<Integer, String>> iterator = set.iterator();
		while (iterator.hasNext()) {
			Map.Entry mapentry = (Map.Entry) iterator.next();
			System.out.println("[" + mapentry.getKey() + "]\n" + mapentry.getValue());
		}

		return this.commitinfo;
	}

	public List<String[]> getLinemsg() {
		/*
		 * int i = 0; for (String[] strs : linemsg) { System.out.println(i++);
		 * System.out.println("[" + strs[0] + "]" + strs[1]);
		 * System.out.println(strs[2]); }
		 */
		return this.linemsg;
	}

	public List<String> getPaths() {
		for (String strs : paths) {
			System.out.println(strs);
		}
		return this.paths;
	}

	public List<String> getIssueidFrLinemsg() {
		List<String> res = new ArrayList<String>();

		for (String[] strs : linemsg) {
			if (strs[1] != null && strs[1].contains("bugzilla") && strs[1].contains("id=")) {
				int startIndex = strs[1].indexOf("id=") + 3;
				int i = startIndex;
				while ((strs[1].charAt(++i) >= '0') && (strs[1].charAt(i) <= '9'))
					;
				String r = strs[1].substring(startIndex, i);
				if (!res.contains(r)) {
					res.add(r);
					// System.out.println(strs[]);
				}
			}
		}
		return res;
	}
	
	public String getIssueidFrLinemsg(String[] strs) {

			if (strs[1] != null && strs[1].contains("bugzilla") && strs[1].contains("id=")) {
				int startIndex = strs[1].indexOf("id=") + 3;
				int i = startIndex;
				while ((strs[1].charAt(++i) >= '0') && (strs[1].charAt(i) <= '9'))
					;
				return strs[1].substring(startIndex, i);
					// System.out.println(strs[]);
			}
		return null;
	}

	/*
	 * private static void setupLibrary() { //
	 * å¯¹äºŽä½¿ç”¨http://å’Œhttpsï¼ 1�77// DAVRepositoryFactory.setup(); //
	 * å¯¹äºŽä½¿ç”¨svnï¼ 1�77/ /å’Œsvn+xxxï¼ 1�77/ /
	 * SVNRepositoryFactoryImpl.setup(); // å¯¹äºŽä½¿ç”¨file://
	 * FSRepositoryFactory.setup(); }
	 * 
	 * public boolean login() { setupLibrary(); try { // åˆ›å»ºåº�1�7�è¿žæŽ�1�7�1�77
	 * repository =
	 * SVNRepositoryFactoryImpl.create(SVNURL.parseURIEncoded(this.svnRoot)); //
	 * èº«ä»½éªŒè¯ￄ1�7 ISVNAuthenticationManager authManager =
	 * SVNWCUtil.createDefaultAuthenticationManager();
	 * repository.setAuthenticationManager(authManager); return true; } catch
	 * (SVNException svne) { svne.printStackTrace(); return false; } }
	 * 
	 * 
	 * @SuppressWarnings("rawtypes") public void listEntries(String path) throws
	 * SVNException { System.out.println(path); Collection entries =
	 * repository.getDir(path, -1, null, (Collection) null); Iterator iterator =
	 * entries.iterator(); while (iterator.hasNext()) { SVNDirEntry entry =
	 * (SVNDirEntry) iterator.next(); System.out.println("@@@@@@@@" +
	 * entry.getCommitMessage()); entry.getDate();
	 * System.out.println(entry.getKind().toString());
	 * System.out.println(entry.getName());
	 * System.out.println(entry.getRepositoryRoot().toString());
	 * System.out.println(entry.getRevision());
	 * System.out.println(entry.getSize() / 1024);
	 * System.out.println(path.equals("") ? "/" + entry.getName() : path + "/" +
	 * entry.getName()); System.out.println(entry.getAuthor()); } }
	 */

	public List<String> svnList(String path, SVNRevision startRevision, SVNRevision endRevision) {
		try {
			SVNURL url = SVNURL.parseURIEncoded(path);
			svnlogclient.doList(url, startRevision, endRevision, true, SVNDepth.INFINITY, SVNDirEntry.DIRENT_ALL,
					new ISVNDirEntryHandler() {

						@Override
						public void handleDirEntry(SVNDirEntry entry) throws SVNException {

							if (entry.getKind().equals(SVNNodeKind.FILE) && entry.getPath().endsWith(".java")) {
								paths.add(entry.getPath());
							}
						}

					});
		} catch (SVNException e) {
			e.printStackTrace();
		}

		System.out.println(paths.size() + " files to be processed.");
		return paths;
	}

	public HashMap<Integer, String> svnLog(String[] paths, SVNRevision startRevision, SVNRevision endRevision) {
		try {
			SVNURL url = SVNURL.parseURIEncoded(this.svnRoot);
			svnlogclient.doLog(url, paths, endRevision, startRevision, endRevision, true, false, 10000,
					new ISVNLogEntryHandler() {
						@Override
						public void handleLogEntry(SVNLogEntry svnlogentry) throws SVNException {
							// System.out.println(svnlogentry.getChangedPaths());
							int revision = (int) svnlogentry.getRevision();
							if (!commitinfo.containsKey(revision)) {
								commitinfo.put(revision, svnlogentry.getMessage());
							} else {
								System.out.println("revision repeated!");
							}
						}
					});

		} catch (SVNException e) {
			e.printStackTrace();
		}
		return commitinfo;
	}

	public List<String[]> svnAnnotate(String path, SVNRevision startRevision, SVNRevision endRevision) {
		try {
			//System.out.println();
			linemsg.clear();
			System.out.println("processing " + path);
			SVNURL url = SVNURL.parseURIEncoded(path);
			svnlogclient.doAnnotate(url, endRevision, startRevision, endRevision, new ISVNAnnotateHandler() {
				public void handleLine(Date date, long revision, String author, String line) {
				}

				@Override
				public void handleEOF() throws SVNException {
				}

				@Override
				public void handleLine(Date date, long revision, String author, String line, Date arg4, long arg5,
						String arg6, String arg7, int lineNo) throws SVNException {
					//System.out.println(date + "  " + revision + "  " + author + "  " + line + "  " + arg4 + "  " + arg5
							//+ "  " + arg6 + "  " + arg7 + "  " + lineNo);
					// System.out.println(commitinfo.get((int)revision));
					linemsg.add(new String[] { revision + "", commitinfo.get((int) revision), line, author,
							date.toString(), lineNo + "" });
				}

				@Override
				public boolean handleRevision(Date arg0, long arg1, String arg2, File arg3) throws SVNException {
					return false;
				}
			});
		} catch (SVNException e) {
			e.printStackTrace();
		}
		return linemsg;
	}

	/*
	 * public void filterCommitHistoryTest() throws Exception { // è¿‡æ»¤æ�¡ä»¶
	 * // final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); //
	 * final Date begin = format.parse("2014-02-13"); // final Date end =
	 * format.parse("2014-02-14"); final String author = ""; // long
	 * startRevision = 50306; SVNRevision startRevision =
	 * SVNRevision.create(1090003); SVNRevision endRevision = SVNRevision.HEAD;
	 * final List<String> history = new ArrayList<String>(); // String[]
	 * ä¸ºè¿‡æ»¤çš�1�7�æ�1�7��1�7�ä»¶è·¯å¾�1�7�å�1�7��ç¼€ï¼Œä¸ºç©ºè¡¨ç¤ºä¸�è¿›è¡Œè¿�1�7�æ»1�7
	 * repository.log(new String[] {
	 * "/tomcat/trunk/java/org/apache/catalina/valves/StuckThreadDetectionValve.java"
	 * }, startRevision.getNumber(), endRevision.getNumber(), true, true, new
	 * ISVNLogEntryHandler() {
	 * 
	 * @Override public void handleLogEntry(SVNLogEntry svnlogentry) throws
	 * SVNException { System.out.println(svnlogentry); } }); }
	 */

	public static void main(String[] args) throws Exception {
		String url = "http://svn.apache.org/repos/asf";
		String rootPath = "/tomcat/trunk/java/org/apache/catalina/valves/";
		// String annotatePath =
		// "/tomcat/trunk/java/org/apache/catalina/valves/StuckThreadDetectionValve.java";
		SVNRevision startRevision = SVNRevision.create(1090003);
		SVNRevision endRevision = SVNRevision.create(-1);
		// SVNRevision endRevision = SVNRevision.HEAD;
		// String[] paths = {
		// "/tomcat/trunk/java/org/apache/catalina/valves/StuckThreadDetectionValve.java",
		// "/tomcat/trunk/java/org/apache/catalina/valves/AbstractAccessLogValve.java"
		// };
		// String path = url +
		// "/tomcat/trunk/java/org/apache/catalina/valves/StuckThreadDetectionValve.java";

		SVNUtil su = new SVNUtil(url);

		System.out.println("SVN info fetch begins...");
		List<String> logPaths = new ArrayList<String>();
		for (String fileName : su.svnList(url + rootPath, endRevision, endRevision)) {
			logPaths.add(rootPath + fileName);
		}

		su.svnLog(logPaths.toArray(new String[0]), startRevision, endRevision);
		// su.getCommitinfo();
		for (String annotatePath : logPaths) {
			// clear linemsg?
			String annotatePath1 = "/tomcat/trunk/java/org/apache/catalina/valves/StuckThreadDetectionValve.java";
			su.svnAnnotate(url + annotatePath1, startRevision, endRevision);
			break;
			// su.getLinemsg();

		}
		List<String> issueIds = su.getIssueidFrLinemsg();

		System.out.println("Issue info fetch begins...");
		IssueTrackerUtil itu = new IssueTrackerUtil();
		XmlUtils xu = new XmlUtils();
		String issueUrl = "https://issues.apache.org/bugzilla/show_bug.cgi?ctype=xml&id=";
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
			String[] res = xu.parserXml("0", filename);
			System.out.println(res[0]);
			System.out.println(res[1]);
		}

	}

}

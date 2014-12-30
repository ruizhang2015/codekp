/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author zr
 * @time 2014-12-26
 * @modifier: zr
 * @time 2014-12-26
 * @reviewer: zr
 * @time 2014-12-26
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

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * @author zr
 */
public class IssueTrackerUtil {

	private CloseableHttpClient httpclient = HttpClients.createDefault();

	public String getIssueXml(String url) {
		System.out.println("IssueXml HttpGet...");
		HttpGet httpget = new HttpGet(url);
		CloseableHttpResponse response;
		String res = null;
		try {
			response = httpclient.execute(httpget);
			res = EntityUtils.toString(response.getEntity());
			response.close();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		IssueTrackerUtil itu = new IssueTrackerUtil();
		XmlUtils xu = new XmlUtils();
		String id = "50306";
		//String id = "57004";
		String url = "https://issues.apache.org/bugzilla/show_bug.cgi?ctype=xml&id=" + id;
		String filename = id + ".xml";
		try {
			File file = new File(filename);
			if (!file.exists()) {
				file.createNewFile();
				String xmlctnt = itu.getIssueXml(url);
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(xmlctnt);
				bw.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// System.out.println(xmlctnt);

		String[] res = xu.parserXml(filename);
		System.out.println(res[0]);
		System.out.println(res[1]);

	}

}

// end

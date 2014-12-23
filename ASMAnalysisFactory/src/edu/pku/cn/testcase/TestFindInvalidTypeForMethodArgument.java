/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-1-8 ����09:58:17
 * @modifier: Administrator
 * @time 2009-1-8 ����09:58:17
 * @reviewer: Administrator
 * @time 2009-1-8 ����09:58:17
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.testcase;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TestFindInvalidTypeForMethodArgument {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				System.out.println("This is a runnable object");
			}
		});
		Thread t2 = new Thread(t1);
		t2.start();

		try {
			HashMap<URL, Integer> urlMap = new HashMap<URL, Integer>();
			urlMap.put(new URL("http://www.pku.edu.cn"), Integer.valueOf(1));

			Map<URL, Integer> urlMap2 = urlMap;

			HashSet<URL> urlSet = new HashSet<URL>();
			urlSet.add(new URL("http://www.pku.edu.cn"));

			Set<URL> urlSet2 = urlSet;

			Object o = urlMap.get(new URL("http://www.pku.edu.cn"));
			o = urlMap2.get(new URL("http://www.pku.edu.cn"));
			urlSet.contains((URL) o);
			urlSet2.contains((URL) o);

			String s = new String("Hello, this is a test");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

// end

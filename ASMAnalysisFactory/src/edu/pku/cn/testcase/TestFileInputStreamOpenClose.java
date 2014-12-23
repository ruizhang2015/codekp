/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time Oct 14, 2008 5:54:55 PM
 * @modifier: Administrator
 * @time Oct 14, 2008 5:54:55 PM
 * @reviewer: Administrator
 * @time Oct 14, 2008 5:54:55 PM
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.testcase;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

public class TestFileInputStreamOpenClose extends Object {

	String str1; 
	// public File testFileInputStreamOpenClose(HashMap<Integer, String> map,
	// String[] args) {
	// // TODO Auto-generated method stub
	// try {
	// BufferedInputStream bis = new BufferedInputStream(
	// new FileInputStream(new File("c:\\1.txt")));
	// BufferedInputStream bis2 = bis;
	// bis2 = bis;
	// FileInputStream fin = new FileInputStream("c:\\");
	//			
	//			
	// int a = fin.read();
	//			
	// //fin.close();
	// bis2.close();
	//			
	// File f = new File("");
	// f.createNewFile();
	//			
	// // if (bis2 != null) {
	// // fin.close();
	// // }
	//			
	// return f;
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return null;
	// }

	TestFileInputStreamOpenClose(){
		String str = super.toString();
		str1 = str;
	}
	
	public void test2FileInputStreamOpenClose(HashMap<Integer, String> map, String[] args) {
		// TODO Auto-generated method stub
		BufferedInputStream bis = null, bis2 = null;
		FileInputStream[] fin;
		try {
			bis = new BufferedInputStream(new FileInputStream(new File("c:\\1.txt")));

			fin = new FileInputStream[2];
			fin[1] = new FileInputStream("c:/file1");
			bis2 = new BufferedInputStream(fin[1]);
			int a = fin[1].read();

			File f = new File("");
			f.createNewFile();

			bis2.close();
			// return f;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// try {
			// bis.close();
			// } catch (Exception e) {
			// // TODO Auto-generated catch block
			// //e.printStackTrace();
			// }

			// if (bis != null) {
			// try {
			// bis.close();
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// }
		}
		// return null;
	}

	public void output2() throws IOException {
		OutputStream ostrm = null;
		try {
			ostrm = new FileOutputStream(new File("c:\\1.txt"));
			ostrm.write('y');
		} finally {
			if (ostrm != null) {
				ostrm.close();
			}
			return;
		}
	}

	public void output() throws IOException {
		BufferedOutputStream ostrm = null;
		ostrm = new BufferedOutputStream(new FileOutputStream(new File("c:\\1.txt")));
		ostrm.write('y');
		if (ostrm != null) {
			ostrm.close();
		}
	}

}

// end

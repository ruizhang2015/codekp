/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time Oct 20, 2008 3:54:39 PM
 * @modifier: Administrator
 * @time Oct 20, 2008 3:54:39 PM
 * @reviewer: Administrator
 * @time Oct 20, 2008 3:54:39 PM
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.testcase;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class TestObjectOutputStreamCreateandClose {
	void right() {

		FileOutputStream fis = null;
		ObjectOutputStream oin = null;
		try {
			fis = new FileOutputStream("");
			oin = new ObjectOutputStream(fis);
			fis.close();
			oin.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				fis.close();
				oin.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	void wrong() {
		try {
			FileOutputStream fis = new FileOutputStream("");
			ObjectOutputStream oin = new ObjectOutputStream(fis);
			// fis.close();
			// oin.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

// end

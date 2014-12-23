/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time Oct 20, 2008 3:32:42 PM
 * @modifier: Administrator
 * @time Oct 20, 2008 3:32:42 PM
 * @reviewer: Administrator
 * @time Oct 20, 2008 3:32:42 PM
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.testcase;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.sound.sampled.AudioInputStream;

public class TestObjectInputStreamCreateandClose {

	void right() {

		try {
			FileInputStream fis = new FileInputStream("");
			ObjectInputStream oin = new ObjectInputStream(fis);
			fis.close();
			oin.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	void wrongObjectInputStream() {
		try {
			FileInputStream fis = new FileInputStream("");
			ObjectInputStream oin = new ObjectInputStream(fis);
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

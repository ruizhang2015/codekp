/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-6-30 ����01:22:20
 * @modifier: Liuxizhiyi
 * @time 2008-6-30 ����01:22:20
 * @reviewer: Liuxizhiyi
 * @time 2008-6-30 ����01:22:20
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.detector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * @author Liuxizhiyi
 */
public class DetectorLoader extends ClassLoader {
	String path;

	public DetectorLoader(String path) {
		super(getSystemClassLoader());
		this.path = path;
		if (this.path.endsWith("/") == false && this.path.endsWith("\\") == false) {
			this.path += "/";
		}
		this.path += "bin/";

	}

	public Class<?> loadClass(String name) throws ClassNotFoundException {
		Class<?> c = null;
		try {
			c = super.loadClass(name);
			if (c != null)
				return c;
		} catch (ClassNotFoundException e) {
		}
		String fileName = path;
		if (name.endsWith(".class"))
			name = name.substring(0, name.lastIndexOf('.'));
		fileName += name.replace('.', '/') + ".class";
		try {
			InputStream in = new FileInputStream(new File(fileName));
			byte[] bytes = readClass(in);
			c = defineClass(name, bytes, 0, bytes.length);
		} catch (Exception e) {
			throw new ClassNotFoundException(e.getMessage(), e);
		}
		return c;
	}

	/**
	 * Reads the bytecode of a class.
	 * 
	 * @param is
	 *            an input stream from which to read the class.
	 * @return the bytecode read from the given input stream.
	 * @throws IOException
	 *             if a problem occurs during reading.
	 */
	private static byte[] readClass(final InputStream is) throws IOException {
		if (is == null) {
			throw new IOException("Class not found");
		}
		byte[] b = new byte[is.available()];
		int len = 0;
		while (true) {
			int n = is.read(b, len, b.length - len);
			if (n == -1) {
				if (len < b.length) {
					byte[] c = new byte[len];
					System.arraycopy(b, 0, c, 0, len);
					b = c;
				}
				return b;
			}
			len += n;
			if (len == b.length) {
				byte[] c = new byte[b.length + 1000];
				System.arraycopy(b, 0, c, 0, len);
				b = c;
			}
		}
	}
}

// end

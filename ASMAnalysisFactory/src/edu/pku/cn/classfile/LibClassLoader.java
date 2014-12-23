/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author liuxi
 * @time 2010-3-27
 * @modifier: liuxi
 * @time 2010-3-27
 * @reviewer: liuxi
 * @time 2010-3-27
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.classfile;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author liuxi
 */
public class LibClassLoader extends ClassNodeLoader {
	public ZipFile lib;
	public LibClassLoader(String fileName){
		super(fileName);
		try {			
			lib=new ZipFile(fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	public LibClassLoader(ZipFile lib) {
		super(lib.getName());
		this.lib = lib;
	}
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		Class<?> c = null;
		try {
			c = super.loadClass(name);
			if (c != null)
				return c;
		} catch (ClassNotFoundException e) {
		}

		try {
			ZipEntry je=lib.getEntry(name);
			InputStream in=lib.getInputStream(je);
			byte[] bytes = readClass(in);
			c = defineClass(name, bytes, 0, bytes.length);
		} catch (Exception e) {
			throw new ClassNotFoundException(e.getMessage(), e);
		}
		return c;
	}
	public ClassNode loadClassNode(InputStream in,int lib){
		LibClassNode cc = new LibClassNode();
		cc.setLibAccess(lib);
		return fillClassNode(cc, in);
	}
	public ClassNode loadClassNode(String className,String classPath,int lib){
		LibClassNode cc = new LibClassNode();
		cc.setLibAccess(lib);
		return fillClassNode(cc,className, classPath);
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
	public static void main(String[] args){
		LibClassLoader loader=new LibClassLoader("");
	}
}

// end

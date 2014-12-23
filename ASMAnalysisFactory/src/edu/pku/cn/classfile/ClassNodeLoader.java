/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-6-22 ����11:55:34
 * @modifier: Liuxizhiyi
 * @time 2008-6-22 ����11:55:34
 * @reviewer: Liuxizhiyi
 * @time 2008-6-22 ����11:55:34
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.classfile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.objectweb.asm.ClassReader;

import edu.pku.cn.util.CodaProperties;

/**
 * 
 * @author Liuxizhiyi
 */
public class ClassNodeLoader extends ClassLoader{

	private String projectPath;

	// private HashMap<String,ClassContext> classContext;

	public ClassNodeLoader(String projectPath) {
		this.projectPath = projectPath;
		// classContext=new HashMap<String, ClassContext>();
	}
	private void dumpException(ClassNode cc,Exception e){
		if(cc!=null)
			System.err.println(cc.name+" is not found");
		e.printStackTrace();
	}
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		Class<?> c = null;
		try {
			c = super.loadClass(name);
			if (c != null)
				return c;
		} catch (ClassNotFoundException e) {
		}catch(java.lang.NoClassDefFoundError err){
			err.printStackTrace();
		}
		String fileName = projectPath;
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
	protected ClassNode fillClassNode(ClassNode cc,InputStream in){
		try {
			ClassReader cr = new ClassReader(in);
			cr.accept(cc, ClassReader.EXPAND_FRAMES);
		} catch (Exception e) {
			dumpException(cc,e);
			return null;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
		return cc;
	}
	protected ClassNode fillClassNode(ClassNode cc,String className,String projectPath){
		String fileName;
		if (className.endsWith(CodaProperties.e().getClassAtt()))
			fileName = className.substring(0, className.length() - 6);
		else
			fileName = className;
		fileName = fileName.replace('.', '/') + ".class";
		InputStream in = null;
		try {
			in = new FileInputStream(new File(projectPath + fileName));
			// ###############################################################################
			// modified by Meng Na, in order to construct correct path
			// in = new FileInputStream(new File(projectPath
			// + fileName.substring(fileName.lastIndexOf('/') + 1)));
			ClassReader cr = new ClassReader(in);
			cr.accept(cc, ClassReader.EXPAND_FRAMES);
		} catch (Exception e) {
			//dumpException(cc,e);
			return null;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
		return cc;
	}
	public ClassNode loadClassNode(String className,int lib) {
		ClassNode cc = new ClassNode();
		return fillClassNode(cc, className,this.projectPath);
	}

	public ClassNode loadClassNode(InputStream in,int lib) {
		ClassNode cc = new ClassNode();
		return fillClassNode(cc, in);
	}
//	public ClassNode loadLibClassNode(InputStream in){
//		LibClassNode cc = new LibClassNode();
//		return fillClassNode(cc, in);
//	}
//	public ClassNode loadLibClassNode(String className,String classPath){
//		LibClassNode cc = new LibClassNode();
//		return fillClassNode(cc,className, classPath);
//	}
}

// end

/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-6-22 ����03:58:51
 * @modifier: Liuxizhiyi
 * @time 2008-6-22 ����03:58:51
 * @reviewer: Liuxizhiyi
 * @time 2008-6-22 ����03:58:51
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.classfile;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import edu.pku.cn.detector.Detector;
import edu.pku.cn.detector.DetectorCategory;
import edu.pku.cn.util.CodaProperties;
import edu.pku.cn.xml.JavaLib.JavaLibService;

/**
 * PackageResource represent a package in a project it is organized as a tree
 * map by the true project's organization
 * 
 * @author Liuxizhiyi
 */
public class PackageResource {
	static long classNum=0;
	static CodaProperties coda = CodaProperties.e();
	String projectPath;
	String packageName;
	List<String> fileList;
	HashMap<String, PackageResource> packageList;
	HashMap<String, ClassNode> classMaps;
	boolean isLib=false;

	public boolean isLib() {
		return isLib;
	}

	public void setLib(boolean isLib) {
		this.isLib = isLib;
	}

	public void clear() {
		if (fileList != null)
			fileList.clear();
		if (classMaps != null)
			classMaps.clear();
		if (packageList != null) {
			for (PackageResource pr : packageList.values())
				pr.clear();
			packageList.clear();
		}
	}

	public PackageResource() {
		classMaps = new HashMap<String, ClassNode>();
		fileList = new ArrayList<String>();
		packageList = new HashMap<String, PackageResource>();
		packageName = "";
		projectPath = "";
	}

	/**
	 * create a PackageResource with the resource in the projectPath
	 * 
	 * @param projectPath
	 */
	public PackageResource(String projectPath) {
		this();
		setProjectPath(projectPath);
		initResource();
	}

	public PackageResource(String packageName, String projectPath) {
		this();
		this.packageName = packageName;// ���úð���
		setProjectPath(projectPath);// ���ú�projectPath
		initResource();// ��ʼ�Ӱ��н������ܼ���ￄ1�7
	}

	public void resolvePackageResource() {
		classMaps.clear();
		fileList.clear();
		packageList.clear();
		initResource();
	}

	private void initResource() {
		File file = new File(projectPath + packageName.replace('.', File.separatorChar));// �滻projectPath������.Ϊ/�����������ַ�������������ļￄ1�7
		if (file.exists() && file.isDirectory()) {// ����File������һ���ļ�Ŀ¼(������ļ�Ŀ¼���������κβ��ￄ1�7)
			File[] files = file.listFiles(new java.io.FilenameFilter() {// �г���Ŀ¼�����к�׺Ϊ".class"�ļ�
						public boolean accept(File dir, String name) {
							return name.endsWith(coda.getClassAtt());
						}
					});

			File[] jars = file.listFiles(new java.io.FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith(coda.getJar()) || name.endsWith(coda.getZip());
				}
			});
			for (File f : jars) {
				if (f.getName().endsWith(coda.getJar()))
					addJar(f,coda.PROJECT_EXPLAND);
				else
					addZip(f,coda.PROJECT_EXPLAND);
			}
			File[] dirs = file.listFiles(new FileFilter() {// ���г���Ŀ¼��������Ŀ¼
						public boolean accept(File pathname) {
							return pathname.isDirectory();
						}
					});

			String packageName = this.packageName;
			if (packageName != "")// ���ǰĿ¼��Ϊ�ￄ1�7
				// ���򲻽����κβ�����������Ŀ¼�������ӡ�.����Ϊ����������������Ŀ¼��׼��
				packageName += ".";

			if (files.length == 0) {// ����Ŀ¼�¼�û���ļ�Ҳû����Ŀ¼����Ϊ��Ŀ¼����ֱ�ӷ���
				if (dirs.length == 0)
					return;
				
//				else if (dirs.length == 1) {// ����Ŀ¼��ֻ��һ����Ŀ¼��û���κ��ļ�����ֱ���޸ĵ�ǰĿ¼��Ϊ��Ŀ¼��ݹ���ñ�����
//					this.packageName = packageName + dirs[0].getName();
//					initResource();
//					return;
//				}
			}

			// ClassContextLoader loader=new ClassContextLoader(projectPath);
			for (File f : files) {// ���ڱ�Ŀ¼�µ������ļ���ȥ���ļ����еĺ�׺".class"���������ľ��·�ￄ1�7(����
				// + ����)�󣬰������ӵ�fileList��
				String className = packageName + f.getName().replace(coda.getClassAtt(), "");
				fileList.add(className);
				// classMaps.put(className, loader.loadClassContext(className));
			}

			for (File f : dirs) {// ���ڱ�Ŀ¼�µ�������Ŀ¼��������Ŀ¼�ľ��·�ￄ1�7
				// ������Ϊ��Ŀ¼�����߳�Ϊ�Ӱ���PackageResource���󣬷ŵ���ǰPackageResource��packageList��
				String pName = packageName + f.getName();
				PackageResource pr = new PackageResource(pName, projectPath);
				if (pr.packageList.size() > 0 || pr.fileList.size() > 0)
					packageList.put(pr.getPackageName(), pr);
			}
		}
	}

	/**
	 * ��õ�ǰPackageResource��projectPath
	 * 
	 * @return
	 */
	public String getProjectPath() {
		return projectPath;
	}

	/**
	 * ���õ�ǰPackageResource��Ӧ��projectPath
	 * 
	 * @param projectPath
	 */
	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
		if (this.projectPath.endsWith("\\") == false && this.projectPath.endsWith("/") == false)
			this.projectPath += "/";// �����projectPath�����ԡ�\\������"/"���������һ���ￄ1�7/��
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	/**
	 * add a class which belong to this package
	 * 
	 * @param className
	 */
	public void addFile(String className) {// ��className����ľ��·��������java.lang.Object��������Package�м����className
		if (className == null)// ���classNameΪ�գ��򲻽����κӴ���
			return;
		if (className.endsWith(coda.getClassAtt()))// ���className��".class"��β����ȥ����׺
			className = className.substring(0, className.lastIndexOf("."));
		if (getClassNode(className) == null) {// ����ڸ�className������PackageResource��δ�������򴴽�PackageResource���󣬽�ClassLoader�����ClassContext����PackageResource�У����Ѹö������ö����packageResourceList��
			PackageResource pr = new PackageResource();
			pr.setProjectPath(projectPath);
			pr.setPackageName(className.substring(0, className.lastIndexOf('.')));
			ClassNodeLoader loader = new ClassNodeLoader(projectPath);
			pr.addClassNode(loader.loadClassNode(className,CodaProperties.PROJECT_EXPLAND));
			pr.addPackageResource(pr);
		} else {// ���򣬻�ȡ�������ڵ�PackageResource������className���뵽��fileList��
			String packageName = className.substring(0, className.lastIndexOf('.'));
			PackageResource pr = this.getPackageResource(packageName);
			pr.fileList.add(className);
		}
	}

	/**
	 * add a jar file to this package
	 * 
	 * @param jarName
	 */
	public void addJar(File file,int lib) {
		// if(!path.startsWith(projectPath))
		// path=projectPath+path;
		try {
			JarFile jar = new JarFile(file);
			addZipOrJar(jar,lib);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * add a zip file to this package
	 * 
	 * @param zipName
	 */
	public void addZip(File file,int lib) {
		try {
			ZipFile zip = new ZipFile(file);
			addZipOrJar(zip,lib);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void addLib(String path,String filename){
		File f=new File(this.projectPath+path+"/"+filename);
		if(filename.endsWith("zip"))
			addZip(f,coda.LIB_EXPLAND);
		else
			addJar(f, coda.LIB_EXPLAND);
	}
	
	public void addLibPath(String file){
		File f=new File(file);
		if(file.endsWith("zip"))
			addZip(f,coda.LIB_EXPLAND);
		else
			addJar(f, coda.LIB_EXPLAND);
	}
	
	@SuppressWarnings("unchecked")
	private void addZipOrJar(ZipFile zip,int lib) {
		try {
			ClassNodeLoader loader =null;
			if(lib>coda.PROJECT_EXPLAND)
				loader=new LibClassLoader(zip);
			else
				loader=new ClassNodeLoader(projectPath);
			Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zip.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				if (entry.getName().endsWith(".class")) {
					String className = entry.getName().replace('/', '.');
					className = className.substring(0, className.lastIndexOf("."));
					String packageName = "";
					int index = -1;
					if ((index = className.lastIndexOf('.')) > 0)
						packageName = className.substring(0, index);
					ClassNode cc = loader.loadClassNode(zip.getInputStream(entry),lib);
					PackageResource pr = getPackageResource(packageName);
	
				{// added by WuQian.
				 //so that all packageResources loaded from jars or zips can be distinguished from other normal packages
					if(lib>coda.PROJECT_EXPLAND){
						//if the package in lib has the same same with the current package, then the lib package should be a different one with the current package
						if(pr.equals(this)){
							pr = new PackageResource();
							pr.setPackageName(packageName);
							pr.setProjectPath(projectPath);
							addPackageResource(pr);
						}						
						pr.setLib(true);
					}
				}
					
					pr.addClassNode(cc);
					

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * add a sub package to this package
	 * 
	 * @param pr
	 */
	public void addPackageResource(PackageResource pr) {// ��packageResource���뵽��ǰPackageResource��packageList��
		String packageName = pr.getPackageName();
		if (packageList.containsKey(pr.getPackageName())) {
			packageList.get(pr.getPackageName()).mergePackageResource(pr);
			return;
		}
		for (PackageResource prsub : packageList.values()) {// ���ǰpackageResource��packageList�в����д�packageResource�������Ӱ����Ƿ��ܼ����PackageResource
			if (prsub.packageName.contains(packageName)) {
				packageList.remove(prsub.getPackageName());
				pr.addPackageResource(prsub);
				packageList.put(pr.getPackageName(), pr);
				break;
			}
		}
		if (packageList.containsKey(pr.getPackageName()) == false) {
			packageList.put(pr.getPackageName(), pr);
		}
	}

	public PackageResource removePackageResource(PackageResource pr) {
		if (packageList.containsValue(pr)) {
			packageList.remove(pr.getPackageName());
			return pr;
		}
		return null;
	}

	public void addClassNode(ClassNode cc) {
		if (classMaps.containsKey(cc.name))
			return;
		classMaps.put(cc.name, cc);
	}

	private void mergePackageResource(PackageResource pr) {
		if (pr == this)
			return;
		if (packageName.equals(pr.getPackageName()) == false)
			return;
		for (PackageResource prsub : pr.packageList.values()) {// ͬ��packageResource�ϲ�
			if (packageList.containsKey(prsub.getPackageName())) {
				packageList.get(pr.getPackageName()).mergePackageResource(prsub);
			} else
				packageList.put(prsub.getPackageName(), prsub);
		}
		for (ClassNode cc : pr.classMaps.values()) {// ��ӱ�packageResourceû�ж�pr�к��е�classContext
			if (classMaps.containsKey(cc.name) == false)
				classMaps.put(cc.name, cc);
		}

		fileList.clear();
		fileList.addAll(classMaps.keySet());
	}

	public PackageResource getPackageResource(String packageName) {
		if (packageName.contains(this.packageName)) {
			if (packageName.equals(this.packageName)) {
				return this;
			} else {
				PackageResource prs = packageList.get(packageName);
				if (prs == null) {
					for (PackageResource pr : packageList.values()) {
						prs = pr.getPackageResource(packageName);
						if (prs != null)
							break;
					}
					if (prs == null) {
						prs = new PackageResource();
						prs.setPackageName(packageName);
						prs.setProjectPath(projectPath);
						addPackageResource(prs);
					}
				}
				return prs;
			}
		}
		return null;
	}

	public ClassNode getClassNode(String className) {
		if (className == null)
			return null;
		try {
			String packName = className.substring(0, className.lastIndexOf('.'));
			return getClassNode(packName, className);
		} catch (IndexOutOfBoundsException e) {
			// e.printStackTrace();
			return getClassNode(this.packageName, className);
		}

	}

	public Iterator<PackageResource> getPackageIterator() {
		return packageList.values().iterator();
	}
	public Collection<PackageResource> getPackageList(){
		return packageList.values();
	}
	
	/**
	 * get all the classes in the package, including all classes in its subPackages, excluding those from zip or jar files.
	 * @return
	 */
	public List<ClassNode> getAllClassNodeList(){
		List <ClassNode> cur_list=this.getClassNodeList();
		Iterator<PackageResource> ite_pkg=this.getPackageIterator();
		while(ite_pkg.hasNext()){
			PackageResource pr=ite_pkg.next();
			if(!pr.isLib()){
				cur_list.addAll(pr.getAllClassNodeList());
			}
				
		}
		return cur_list;
	}
	
	/**
	 * get the iterator of all the classes in the package, including all classes in its subPackages, excluding those from zip or jar files.
	 * @return
	 */
	public Iterator<ClassNode> getAllClassNodeIterator(){
		return this.getAllClassNodeList().iterator();
	}
	
	/**
	 * get the classes in the package, without those in the subPackages
	 * @return
	 */
	public List<ClassNode> getClassNodeList(){
		if (fileList.size() > classMaps.size()) {
			for (int i = 0; i < fileList.size(); i++) {
				getClassNode(fileList.get(i));
			}
			for (String fileName : classMaps.keySet()) {
				if (fileList.contains(fileName)) {
					break;
				} else {// if the fileName does not contain package information,
					if (fileList.contains(fileName.substring(fileName.lastIndexOf('.') + 1))) {
						fileList.remove(fileName.substring(fileName.lastIndexOf('.') + 1));
						fileList.add(fileName);
					} else {
						System.out.println("There is a big error in PackageResource's accept()");
					}
				}
			}
		}
		return new ArrayList<ClassNode>(classMaps.values());
	}
	
	/**
	 * get the iterator of the classes in the package, without those in the subPackages
	 * @return
	 */
	public Iterator<ClassNode> getClassNodeIterator() {
		if (fileList.size() > classMaps.size()) {
			for (int i = 0; i < fileList.size(); i++) {
				getClassNode(fileList.get(i));
			}
			for (String fileName : classMaps.keySet()) {
				if (fileList.contains(fileName)) {
					break;
				} else {// if the fileName does not contain package information,
					if (fileList.contains(fileName.substring(fileName.lastIndexOf('.') + 1))) {
						fileList.remove(fileName.substring(fileName.lastIndexOf('.') + 1));
						fileList.add(fileName);
					} else {
						System.out.println("There is a big error in PackageResource's accept()");
					}
				}
			}
		}
		return classMaps.values().iterator();
	}

	protected ClassNode getClassNode(String packageName, String className) {
		PackageResource pr = getPackageResource(packageName);
		if (pr == null)
			return null;
		// ClassNode cc=pr.classMaps.get(className);
		ClassNode cc = pr.classMaps.get(className);
		if (cc == null) {
			if(isLib)
				return null;
			ClassNodeLoader loader = new ClassNodeLoader(projectPath);
			cc = loader.loadClassNode(className,coda.PROJECT_EXPLAND);
			if (cc != null) {
				pr.addClassNode(cc);
//				System.out.println("load class "+cc.name);
			}
		}
		return cc;
	}

	public void accept(Detector cv) {
		if (fileList.size() > classMaps.size()) {
			for (int i = 0; i < fileList.size(); i++) {
				getClassNode(fileList.get(i));
			}
			// ���μ��������һ�޸�fileList���������fileName
			for (String fileName : classMaps.keySet()) {
				if (fileList.contains(fileName)) {
					break;
					// do nothing, the fileName already contains package name in
					// it
				} else {// if the fileName does not contain package information,
					// change it
					if (fileList.contains(fileName.substring(fileName.lastIndexOf('.') + 1))) {
						fileList.remove(fileName.substring(fileName.lastIndexOf('.') + 1));
						fileList.add(fileName);
					} else {
						System.out.println("There is a big error in PackageResource's accept()");
					}
				}
			}
		}
		for(ClassNode cc:classMaps.values()){
			cc.accept(cv);
		}
	}

	public void accept(DetectorCategory detectors) {
		Iterator<Detector> iter = detectors.detectorIteraotr();
		if (this.fileList.size() > 0 || this.classMaps.size() > 0) {
			while (iter.hasNext()) {
				accept(iter.next());
			}
		}

		Object[] subPac = packageList.values().toArray();
		for (int i = 0; i < subPac.length; i++) {
			PackageResource pr = (PackageResource) subPac[i];
			// System.out.println(pr);
			pr.accept(detectors);
		}
	}

	public String toString() {
		return packageName;
	}

	// public boolean writeToXML(String fileName,String libName,String version){
	// return JavaLibService.writePackageToXML(fileName, this, libName,
	// version);
	// }

	public static void main(String[] args) {
		// PackageResource pr=new
		// PackageResource("","C:\\eclipse\\workspace\\integratedFindBugs\\bin\\");
		// ClassNode cc=pr.getClassNode("invokerForFindBugs.FInvoker");
		 PackageResource pr=new PackageResource();
		 pr.setProjectPath("C:\\Program Files/Java/jre1.6.0_05");
		 pr.addLib("lib","resources.jar");
		 pr.addLib("lib","rt.jar");
		 pr.addLib("lib","jsse.jar");
		 pr.addLib("lib","jce.jar");
		 pr.addLib("lib","charsets.jar");
		 pr.addLib("lib/ext","sunjce_provider.jar");
		 pr.addLib("lib/ext","sunmscapi.jar");
		 pr.addLib("lib/ext","dnsns.jar");
		 pr.addLib("lib/ext","localedata.jar");
//		 ClassNode cc=pr.getClassNode("org.objectweb.asm.ClassVisitor");
//		 cc.accpet(null);
		 JavaLibService.writePackageToXML("etc/JavaLib.xml",pr, "jre", "1.6.0_05");
	}
}

// end

/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-10-27 ����03:49:56
 * @modifier: Liuxizhiyi
 * @time 2008-10-27 ����03:49:56
 * @reviewer: Liuxizhiyi
 * @time 2008-10-27 ����03:49:56
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.hierarchy;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.pku.cn.Project;
import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.classfile.PackageResource;
import edu.pku.cn.util.CodaProperties;
import edu.pku.cn.xml.JavaLib.JavaLibService;

/**
 * 
 * @author Liuxizhiyi
 */
public class Repository {

	final HashMap<String, SoftReference<ClassNode>> classMap = new HashMap<String, SoftReference<ClassNode>>();
	static Repository instance;
	static boolean isInitial=false;
	static boolean isChaBuild=false;
	static HashSet<String> buildInType = new HashSet<String>();
	{
		buildInType.add("void");
		buildInType.add("boolean");
		buildInType.add("byte");
		buildInType.add("short");
		buildInType.add("int");
		buildInType.add("float");
		buildInType.add("long");
		buildInType.add("double");
	}
	PackageResource resource;
	PackageResource libResource;
	CodaProperties coda=CodaProperties.e();
	private Repository() {
//		initialLib();
		//initial(libResource);
	}
//	private void loadLib(){
//		isInitial=true;
//		JavaLibService.loadJavaConfig("etc/JavaLib.xml");
//		// delete by lijinhui for some exceptions
//		libResource = JavaLibService.getPackageResource("jre", "1.6.0_05");
//		libResource.setLib(true);
//		if(resource!=null)
//		initial(resource);
//		initial(libResource);
//	}
	private void initialLib(){
		isInitial=true;
		libResource=new PackageResource();
		libResource.setLib(true);
		String path=coda.getJRELibPath();
		File file = new File(path);
		if(file!=null && file.isDirectory())
			addLibPath(file);
		for(String p:coda.libPath){
			if(p.endsWith(coda.getJar())){
			file=new File(p);
			if(file!=null){
				libResource.addJar(file, coda.LIB_EXPLAND);
			}
			}
		}
	}
	private void addLibPath(File file){
		File[] files=file.listFiles(new FilenameFilter() {			
			@Override
			public boolean accept(File dir, String name) {
				// TODO Auto-generated method stub
				if(name.equals(".svn"))
					return false;
				return true;
			}
		});
		for(File f:files){
			if(f.isDirectory()){
				addLibPath(f);
			}else{
				if(f.getName().endsWith(coda.getJar()))
					libResource.addJar(f, CodaProperties.JRE_EXPLAND);
			}
		}
	}
	public void prepareCHA(){
		initialLibResource();
		if(isChaBuild==false){
			isChaBuild=true;
			initial(libResource);
			initial(resource);
		}
	}
	private void initial(PackageResource re){
		List<ClassNode> cns=re.getClassNodeList();
		for(int i=0;i<cns.size();i++){
			ClassNode node=cns.get(i);
			ClassNode father=node.getSuperClass();
			if(father!=null){
				father.addChild(node.getSubType());
			}
			Iterator<String> imps=node.interfaces.iterator();
			while(imps.hasNext()){
				ClassNode fi=findClassNode(imps.next());
				if(fi!=null){
					fi.addChild(node.getSubType());
				}
			}
		}
		List<PackageResource> prs=new LinkedList<PackageResource>(re.getPackageList());
		for(int i=0;i<prs.size();i++)
			initial(prs.get(i));
	}
	public void setPackageResource(PackageResource re) {
		resource = re;
	}
	
	public synchronized static Repository getInstance() {
		if (instance == null)
			instance = new Repository();
		return instance;
	}
	public synchronized void initialLibResource(){
		if(!isInitial){
			initialLib();
		}
	}
	public synchronized ClassNode findClassNode(String className) {
		if(!isInitial){
			initialLib();
		}
		if (buildInType.contains(className) || className.contains("["))
			return null;
		className = className.replace('/', '.');
		if (classMap.containsKey(className))
			return classMap.get(className).get();
		else {
			if (resource == null)// modified by Meng Na, so that once we find a
				// class in the lib, we will not continue look
				// for its super classes/interfaces
				return null;
			ClassNode cc = libResource.getClassNode(className);			
			if (cc == null)
				cc = resource.getClassNode(className);
			if (cc != null) {
				classMap.put(className, new SoftReference<ClassNode>(cc));
			}
			return cc;
		}
	}

	public boolean instanceOf(ClassNode dest, String className) {
		return dest.instanceOf(findClassNode(className));
	}

	public boolean instanceOf(ClassNode dest, ClassNode superClass) {
		return dest.instanceOf(superClass);
	}

	public boolean instanceOf(String dest, String className) {
		ClassNode cc = findClassNode(dest);
		if (cc == null)
			return false;
		return instanceOf(cc, className);
	}

	public boolean instanceOf(String dest, ClassNode superClass) {
		ClassNode cc = findClassNode(dest);
		if (cc == null)
			return false;
		return instanceOf(cc, superClass);
	}

	public boolean implementOf(ClassNode dest, String className) {
		return dest.implementOf(findClassNode(className));
	}

	public boolean implementOf(ClassNode dest, ClassNode superClass) {
		return dest.implementOf(superClass);
	}

	public boolean implementOf(String dest, String className) {
		ClassNode cc = findClassNode(dest);
		if (cc == null)
			return false;
		return implementOf(cc, className);
	}

	public boolean implementOf(String dest, ClassNode superClass) {
		ClassNode cc = findClassNode(dest);
		if (cc == null)
			return false;
		return implementOf(cc, superClass);
	}

	public static void main(String[] args) {
		Project project=new Project("D:/eclipse/workspace/ASMAnalysisFactory/bin/");
		//Project project = new Project("D:/eclipse/workspace/asm/bin/");
		//project.setLibPath("/output");
		// project.excute();
		Repository repository = Repository.getInstance();
		ClassNode methodA=repository.findClassNode("edu.pku.cn.detector.Detector");
		Iterator<ClassNode> cha=methodA.getSubType().getSubClass().iterator();
		while(cha.hasNext())
		System.out.println(cha.next().name);

//		System.out.println(repository.instanceOf("org.objectweb.asm.tree.ClassNode",
//				"org.objectweb.asm.tree.MemberNode"));
//		System.out.println(repository.implementOf("org.objectweb.asm.tree.ClassNode", "org.objectweb.asm.ClassVisitor"));
//		System.out.println(repository.instanceOf("java.io.File", "java.io.FilterInputStream"));
//		System.out.println(repository.implementOf("java.io.File", "java.io.Closeable"));
//		System.out.println(repository.instanceOf("org.objectweb.asm.xml.SAXClassAdapter",
//				"org.objectweb.asm.xml.SAXAdapter"));
	}
}

// end

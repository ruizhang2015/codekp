/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-6-22 ����11:35:24
 * @modifier: Liuxizhiyi
 * @time 2008-6-22 ����11:35:24
 * @reviewer: Liuxizhiyi
 * @time 2008-6-22 ����11:35:24
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import edu.pku.cn.analysis.InterValueDataflowAnalysis;
import edu.pku.cn.classfile.ClassNodeLoader;
import edu.pku.cn.classfile.PackageResource;
import edu.pku.cn.detector.DetectorCategory;
import edu.pku.cn.detector.DetectorCategoryCollection;
import edu.pku.cn.hierarchy.Repository;
import edu.pku.cn.user.UserPreference;
import edu.pku.cn.util.AnalysisFactoryManager;
import edu.pku.cn.util.CodaProperties;
import edu.pku.cn.util.MemorySpace;
import edu.pku.cn.util.Timer;
import edu.pku.cn.xml.messages.MessageService;

/**
 * 
 * @author Liuxizhiyi
 */
class PathUtil {
	static String replace(String path){
		switch (File.separatorChar) {
		case '/':
			return path.replace('\\', File.separatorChar);
		case '\\':
			return path.replace('/', File.separatorChar);
		default:
			return path.replace('\\', File.separatorChar).replace('/', File.separatorChar);
		}
	}
	static String getCorrectPath(String path) {
		path=replace(path);
		if(!path.endsWith(File.separator))
			path+=File.separator;
		return path;
	}
}

public class Project {
	
	static final String myPath = System.getProperty("user.dir");
	static final char pathSeparatorChar = File.pathSeparatorChar;
	static CodaProperties coda = CodaProperties.e();
	String projectPath;
	String srcPath;

	UserPreference userPreference;
	static PackageResource packageResource;
	public boolean loadRepository=false;
	public static PackageResource getPackageResource(){
		if(packageResource==null)
			packageResource=new PackageResource();
		return packageResource;
	}
	DetectorCategoryCollection categorys;
	static {
		AnalysisFactoryManager.initial();
	}

	public void clear() {
		if (packageResource != null)
			packageResource.clear();
		if (categorys != null)
			categorys.clear();
	}

	private void init(String projectPath) {
		this.projectPath = PathUtil.getCorrectPath(projectPath);
		CodaProperties.setProjectPath(this.projectPath );
		MessageService.loadMessage(myPath + coda.getMessage());
		packageResource = new PackageResource("", projectPath);
		
		Repository.getInstance().setPackageResource(packageResource);
		
		categorys = new DetectorCategoryCollection();
		//if(loadRepository)
		
	}

//	private Project() {
//		init("");
//	}

	public void setProjectPath(String path) {
		projectPath = path;
		projectPath = PathUtil.getCorrectPath(projectPath);
		CodaProperties.setProjectPath(projectPath);
		packageResource.setProjectPath(projectPath);
	}
	public void addLibPath(String path){
		path=PathUtil.getCorrectPath(path);
//		if(!path.contains(projectPath))
//			path=projectPath+path;
		File file = new File(path);
		if(file!=null && file.isDirectory()){
			addLibPath(file);
		}else if(file.isFile() && file.getName().endsWith(coda.getJar())){
			coda.add(file.getPath());
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
					coda.add(f.getPath());
			}
		}
	}
	/**
	 * create the project to analysis your resource
	 * 
	 * @param projectPath
	 */
	public Project(String projectPath) {
		init(projectPath);
	}
	public void addLib(String path, String libName,boolean asLib) {
		path = PathUtil.getCorrectPath(path);
		if (!path.startsWith(projectPath))
			path = projectPath + path;
		if (!path.endsWith(File.separator))
			path += File.separatorChar;
		File file = new File(path + libName);
		if(asLib)
			addLib(file,coda.LIB_EXPLAND);
		else
			addLib(file,coda.PROJECT_EXPLAND);
	}

	private void addLib(File file,int isLib) {
		coda.add(file.getAbsolutePath());
		if (file.getName().endsWith(coda.getJar()))
			packageResource.addJar(file,isLib);
		else if (file.getName().endsWith(coda.getZip()))
			packageResource.addZip(file,isLib);
	}

	public void setLibPath(String libPath) {
		if (libPath.startsWith(File.separator))
			libPath = libPath.substring(1);
		if (!libPath.startsWith(projectPath))
			libPath = projectPath + libPath;
		File file = new File(libPath);
		if (file.exists() && file.isDirectory()) {
			File[] files = file.listFiles(new java.io.FilenameFilter() {
				public boolean accept(File dir, String name) {
					if (name.endsWith(coda.getJar()) || name.endsWith(coda.getZip()))
						return true;
					return false;
				}
			});

			// ClassContextLoader loader=new ClassContextLoader(projectPath);
			for (File f : files) {
				addLib(f,coda.LIB_EXPLAND);
				// classMaps.put(className, loader.loadClassContext(className));
			}
		}
	}

	public void excute() {
		ExecutorService es=Executors.newFixedThreadPool(categorys.getCategorySize()) ;
		int counts[]=new int[categorys.getCategorySize()];
		Iterator<DetectorCategory> iter = categorys.categoryIterator();
		int bugCount=0;
		Future<Integer> count;
		int i=0;
		while (iter.hasNext()) {
			DetectorCategory category = iter.next();
			// packageResource.accept(category);
			// category.report();
			SubWork work = new SubWork(packageResource, category);
			Timer.start(work);
			count=es.submit(work);
//			bugCount+=work.call();
//			work.run();
			try {
				counts[i++]=count.get();
				//bugCount+=count.get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(work.category.getName() + " takes time:" + Timer.stop(work) + "ms");
		}
		es.shutdown();
		for(i=0;i<counts.length;i++)
			bugCount+=counts[i];
		System.out.println("total bug detected:"+bugCount);
	}

	/**
	 * add a package resource
	 * 
	 * @param pr
	 */
	public void addPackageResource(PackageResource pr) {
		packageResource.addPackageResource(pr);
	}

	/**
	 * add a Class Context which located in the path you set in the project
	 * 
	 * @param className
	 */
	public void addClass(String className) {
		packageResource.addFile(className);
	}

	public void finalize() {

	}

	//org.eclipse.core.internal.filebuffers.JavaTextFileBuffer
	public static String debugMethodOriginalFullName = "TestCloseDbConnection";
	public static boolean debugStatus = true;
	public static boolean skipable = false;
	
	public static String[] skipBeforeMethod = {
		"org.jfree.chart.JFreeChart::draw(Ljava/awt/Graphics2D;Ljava/awt/geom/Rectangle2D;Ljava/awt/geom/Point2D;Lorg/jfree/chart/ChartRenderingInfo;)",
		"org.jfree.chart.JFreeChart::createBufferedImage(IIILorg/jfree/chart/ChartRenderingInfo;)",
		"org.jfree.chart.JFreeChart::createBufferedImage(IIDDLorg/jfree/chart/ChartRenderingInfo;)Ljava/awt/image/BufferedImage;",
		"org.jfree.chart.ChartPanel::paintComponent(Ljava/awt/Graphics;)",
		"org.jfree.chart.ChartPanel::print(Ljava/awt/Graphics;Ljava/awt/print/PageFormat;I)",
		"org.jfree.chart.ChartFactory::createPieChart(Ljava/lang/String;Lorg/jfree/data/general/PieDataset;Lorg/jfree/data/general/PieDataset;IZZZZZZ)Lorg/jfree/chart/JFreeChart"
		,"org.jfree.chart.ChartFactory::createMultiplePieChart(Ljava/lang/String;Lorg/jfree/data/category/CategoryDataset;Lorg/jfree/util/TableOrder;ZZZ)Lorg/jfree/chart/JFreeChart;"
	};
	
	public static String transferEdgeOwner = "java.io.BufferedInputStream";
	public static String transferEdgeName = "getConnection";
	public static String transferEdgeDesc = "(Ljava/io/InputStream;)V";
	
	public static void main(String[] args) {
		
		String proPath = "D:\\eclipseworkspace\\CODA20110114\\bin\\edu\\pku\\cn\\testcase";
		String libPath = "D:\\test\\lib";
		
		try {
			PrintStream fout = new PrintStream(new FileOutputStream(new File("output.txt")));
			System.setOut(fout);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MemorySpace.start();
		String path = System.getProperty("user.dir");
		
		System.out.println(path);
		Object key = new Object();
		Timer.start(key);
//		 Project project = new Project("D:/eclipse/workspace/test-apache-tomcat6.0.10/bin/");
//		 project.addLibPath("D:/eclipse/workspace/test-apache-tomcat6.0.10/lib/");
//		 coda.libPath.add("D:/eclipse/plugins/org.eclipse.jdt.core_3.5.1.v_972_R35x.jar");
//		 coda.libPath.add("D:/eclipse/plugins/org.eclipse.jdt.compiler.apt_1.0.201.R35x_v20090825-1530.jar");
//		 coda.libPath.add("D:/eclipse/plugins/org.junit4_4.5.0.v20090824/junit.jar");
//		 coda.libPath.add("D:/eclipse/plugins/org.hamcrest.core_1.1.0.v20090501071000.jar");
//		 Project project = new Project("D:/eclipse/workspace/jonas4.10.7-src/bin/");
//		 project.addLibPath("D:/eclipse/workspace/jonas4.10.7-src/externals/");
//		System.out.println("times:" + Timer.stop(key) + "ms");
		 
		Project project = new Project(proPath);
		CodaProperties.isLibExpland = true;
		
		ClassNodeLoader loader = new ClassNodeLoader(CodaProperties.projectPath);
		InterValueDataflowAnalysis.loader = loader;
		project.addLibPath(libPath);

		Repository.getInstance().prepareCHA();
		
		project.excute();
		System.out.println("Space used:" + MemorySpace.stop() + "byte");
	}
}

// end

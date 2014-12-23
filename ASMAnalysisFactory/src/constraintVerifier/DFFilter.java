package constraintVerifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.Type;

import classParse.Converter;

import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.classfile.PackageResource;
import edu.pku.cn.hierarchy.Repository;

/**
 * 为两个方法进行数据流依赖的判断
 * @author wuqian
 *
 */
public class DFFilter {

	private static PackageResource pr=new PackageResource();
	
	/**
	 * 判断两个方法是否存在数据流关系
	 * @param projectPath 包含所有涉及到的类的bytecode的路径
	 * @param types1 包括所有参数、返回值，以及所在的类本身
	 * @param argTypes2	包括所在的类和所有参数
	 * @return
	 */
	public static boolean hasDep(String projectPath, String[] types1, String[] argTypes2){
		//先加载所有的类
		pr.setProjectPath(projectPath);
		
		Repository.getInstance().setPackageResource(pr);
		
		//判断方法2中的每个参数是否出现在方法1中参数或者返回值中，有继承或者实现关系的也可以。
		for(int i=0;i<argTypes2.length;i++){
			ClassNode node2=pr.getClassNode(argTypes2[i]);		
			if(node2==null){
				System.out.println("can't find Class "+argTypes2[i]);
				return false;
			}
			String name2=node2.name;
			for(int j=0;j<types1.length;j++){
				ClassNode node1=pr.getClassNode(types1[j]);
				if(node1==null){
					System.out.println("can't find Class "+types1[j]);
					return false;
				}
				String name1=node1.name;
				
				if(name2.equals(name1))
					return true;
				
//				if(node1.isInterface()&&node2.implementOf(node1))
//					return true;
//				
//				if(node2.isInterface()&&node1.implementOf(node2))
//					return true;
//				
//				Iterator<ClassNode> supers=node1.getSuperClasses().iterator();
//				while(supers.hasNext()){
//					ClassNode superClass=supers.next();
//					if(superClass.name.equals(node2.name))
//						return true;
//				}
//				
//				supers=node2.getSuperClasses().iterator();
//				while(supers.hasNext()){
//					ClassNode superClass=supers.next();
//					if(superClass.name.equals(node1.name))
//						return true;
//				}
								
			}
			
		}
				
		return false;
	}
	
	
	public static ArrayList<String> findFarthers(String projectPath,String classNm){
		ArrayList<String> farthers=new ArrayList<String>();
		
		pr.setProjectPath(projectPath);		
		Repository.getInstance().setPackageResource(pr);
		
		ClassNode cn=pr.getClassNode(classNm);
		if(cn==null){
			System.out.println("can't find class: "+classNm);
			return null;
		}
		
		List<ClassNode> list=cn.getSuperClasses();
		list.addAll(cn.getInterfaces());
		
		Iterator<ClassNode> ite=list.iterator();
		while(ite.hasNext()){
			String farther=ite.next().name;
			if(farther!=null&&!farther.trim().equals("")){
				farthers.add(farther);
//				System.out.println(farther);
			}
				
		}
		
		
		
		return farthers;
	}
	
	public static String getReturnType(String projectPath, String fullClassNm,String methodNm, String[] args){
		String returnT="";
		
		pr.setProjectPath(projectPath);		
		Repository.getInstance().setPackageResource(pr);
		
		
		
		String descriptor=Converter.mDescriptor(args);
		if(descriptor==null){
//			System.out.println("can't find method "+fullClassNm+"."+methodNm);
			return null;
		}
		
		ClassNode cn=pr.getClassNode(fullClassNm);
		if(cn==null){
//			System.out.println("can't find class "+fullClassNm);
			return null;
		}
		Iterator<MethodNode> ite=cn.methods.iterator();
		while(ite.hasNext()){
			
			MethodNode m=ite.next();
			if(m.name.equals(methodNm)&&m.desc.startsWith(descriptor)){
				return Type.getReturnType(m.desc).getClassName();								
			}
		}
		
		if(returnT.equals("")){//没找到，则试着找一下父类的
			ArrayList<String>farthers=findFarthers(projectPath, fullClassNm);
			for(int i=0;i<farthers.size();i++){
				returnT=getReturnType(projectPath, farthers.get(i), methodNm, args);
				if(returnT!=null)
					return returnT;
			}
		}
		
		
//		System.out.println("can't find method "+fullClassNm+"."+methodNm);
		return null;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String project="F:\\workspace\\j2se\\bin";
//		PackageResource pr=new PackageResource();
//		pr.setProjectPath(project);
//		ClassNode c=pr.getClassNode("java.io.PrintWriter");
//		Iterator<ClassNode> ite=c.getSuperClasses().iterator();
//		while(ite.hasNext()){
//			System.out.println(ite.next().name);
//		}
		
//		findFarthers("F:\\workspace\\j2se\\bin", "java.io.PrintStream");
		
//		String[] types1=new String[]{"java.sql.Statement","java.io.PrintStream"};
//		String[] types2=new String[]{"java.io.OutputStream"};
//		System.out.println(hasDep(project,types1,types2));
//		
//		types1=new String[]{"org.hsqldb.jdbc.JDBCCallableStatement","java.io.PrintStream"};
//		types2=new String[]{"java.sql.Statement"};
//		System.out.println(hasDep(project,types1,types2));
		
//		System.out.println(getReturnType(project, "java.sql.Statement", "close",new String[]{}));

		File file=new File("F:\\workspace\\constraitVerifier\\dataSets\\train\\cleanup");
		File[] files=file.listFiles();
		for(int i=0;i<files.length;i++){
			String name=files[i].getName();
			if(name.endsWith(".txt")){
				try{
					BufferedReader br=new BufferedReader(new FileReader(files[i]));
					String line=br.readLine();
					if(line!=null&&line.contains("no effect"))
						System.out.println(name);
				}
				catch(Exception e){
					
				}
			}
		}
		
	}

}

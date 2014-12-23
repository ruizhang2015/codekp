/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-10-28 ����12:33:56
 * @modifier: Liuxizhiyi
 * @time 2008-10-28 ����12:33:56
 * @reviewer: Liuxizhiyi
 * @time 2008-10-28 ����12:33:56
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.xml.JavaLib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.JXPathContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.classfile.FieldNode;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.classfile.PackageResource;
import edu.pku.cn.util.CodaProperties;
import edu.pku.cn.xml.JavaLib.util.JavaLibResourceUtil;

/**
 * 
 * @author Liuxizhiyi
 */
public class JavaLibService {
	public static JXPathContext javaContext;
	private static final String BASE = "javaConfigContainer/JavaConfig/";

	public static void loadJavaConfig(String url) {
		javaContext = JXPathContext.newContext(new JavaConfig(url));
		javaContext.setLenient(true);
	}

	public static PackageResource getPackageResource(String name, String version) {
		String context = BASE + "JavaLib[@name='" + name + "' and @version='" + version + "']";
//		String context = BASE + "JavaLib";
		Element elem = (Element) javaContext.selectSingleNode(context);
		
		NodeList packages; 
		PackageResource resource = null;
		
		if(elem != null){
			packages = elem.getChildNodes();
			
			resource = new PackageResource();
			for (int i = 0; i < packages.getLength(); i++) {
				Node node = packages.item(i);
				if (node.getNodeName().equals("Package"))
					resource.addPackageResource(createPackageResource((Element) node, ""));
				else if (node.getNodeName().equals("Class"))
					fillClassNode(resource, (Element) node);
			}
		}
		
		return resource;
	}

	protected static void fillClassNode(PackageResource resource, Element c) {
		String parent = resource.getPackageName();
		if (parent == "")
			parent = c.getAttribute("name");
		else
			parent = parent + "." + c.getAttribute("name");
		ClassNode cc = new ClassNode();
		cc.name = parent;
		cc.superName = c.getAttribute("extends");
		if (Boolean.parseBoolean(c.getAttribute("isInterface")))
			cc.access = Opcodes.ACC_INTERFACE;
		cc.interfaces = new HashSet<String>();
		NodeList imp = c.getElementsByTagName("Implement");
		for (int j = 0; j < imp.getLength(); j++) {
			cc.interfaces.add(((Element) imp.item(j)).getAttribute("className"));
		}
		imp=c.getElementsByTagName("Field");
		for(int i=0;i<imp.getLength();i++){
			Element e=(Element)imp.item(i);
			FieldNode f=new FieldNode(Integer.parseInt(e.getAttribute("access")),
					e.getAttribute("name"), 
					e.getAttribute("desc"),
					e.getAttribute("signature"), null);
			cc.fields.add(f);
			cc.fieldMap.put(f.name, i);
		}
		imp=c.getElementsByTagName("Method");
		for(int i=0;i<imp.getLength();i++){
			Element e=(Element)imp.item(i);
			MethodNode m=new MethodNode(Integer.parseInt(e.getAttribute("access")),
					e.getAttribute("name"), 
					e.getAttribute("desc"),
					e.getAttribute("signature"), null);
			m.declaringClass=cc;
			NodeList rt=e.getElementsByTagName("ReturnType");
			for(int j=0;j<rt.getLength();j++){
				Element re=(Element)rt.item(j);
				m.returnType.add(Type.getObjectType(re.getAttribute("name")));
			}
			cc.methods.add(m);
			cc.methodMap.put(m.name+m.desc, i);
		}
//		cc.setLibClass(true);
		cc.setLibAccess(CodaProperties.LIB_EXPLAND);
		resource.addClassNode(cc);
	}

	protected static PackageResource createPackageResource(Element node, String parent) {
		PackageResource resource = new PackageResource();
		if (parent == "")
			parent = node.getAttribute("name");
		else
			parent = parent + "." + node.getAttribute("name");
		resource.setPackageName(parent);
		NodeList classes = node.getChildNodes();
		for (int i = 0; i < classes.getLength(); i++) {
			Node item = classes.item(i);
			if (item.getNodeName().equals("Package"))
				resource.addPackageResource(createPackageResource((Element) item, parent));
			else if (item.getNodeName().equals("Class"))
				fillClassNode(resource, (Element) item);
		}
		return resource;
	}

	public static boolean JavaLibService(String fileName, PackageResource resource, String libName, String version) {
		File file = new File(fileName);
		DocumentRoot root = null;
		JavaLibFactory libFactory = JavaLibFactory.eINSTANCE;
		JavaLibType lib = null;
		if (file.exists()) {
			try {
				root = JavaLibResourceUtil.getInstance().load(new FileInputStream(file));
				JXPathContext javaContext = JXPathContext.newContext(new JavaConfig(file.getPath()));
				javaContext.setLenient(true);
				String context = BASE + "JavaLib[@name='" + libName + "' and @version='" + version + "']";
				if (javaContext.selectSingleNode(context) != null) // ���Ƿ��ظ�
					return false;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			root = JavaLibFactory.eINSTANCE.createDocumentRoot();
			root.setJavaConfig(libFactory.createJavaConfigType());
		}
		if (root == null)
			return false;
		JavaConfigType javaConfig = root.getJavaConfig();
		lib = libFactory.createJavaLibType();
		lib.setName(libName);
		lib.setVersion(version);
		javaConfig.getJavaLib().add(lib);
		if (resource.getPackageName().equals("")) {
			Iterator<PackageResource> packageIter = resource.getPackageIterator();
			while (packageIter.hasNext()) {
				PackageResource subRe = packageIter.next();
				PackageType pack = libFactory.createPackageType();
				pack.setName(subRe.getPackageName());
				writePackageToXML(pack, subRe);
				lib.getPackage().add(pack);
			}
		} else {
			PackageType pack = libFactory.createPackageType();
			pack.setName(resource.getPackageName());
			writePackageToXML(pack, resource);
			lib.getPackage().add(pack);
		}
		try {
			JavaLibResourceUtil.getInstance().save(root, new FileOutputStream(file));
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * ��resource�µ�ClassContextд��pack�ڵ���
	 * 
	 * @param pack
	 * @param resource
	 * @return
	 */
	protected static boolean writeClassContextToXML(PackageType pack, PackageResource resource) {
		Iterator<ClassNode> iter = resource.getClassNodeIterator();
		JavaLibFactory libFactory = JavaLibFactory.eINSTANCE;
		while (iter.hasNext()) {
			ClassNode cc = iter.next();
			ClassType cType = libFactory.createClassType();
			String name = cc.name.substring(cc.name.lastIndexOf(".") + 1);
			cType.setName(name);
			if (cc.superName != null)
				cType.setExtends(cc.superName.replace('/', '.'));
			cType.setIsInterface((cc.access & Opcodes.ACC_INTERFACE) != 0);
			Iterator<String> interIter = cc.interfaces.iterator();
			while (interIter.hasNext()) {
				ImplementType implType = libFactory.createImplementType();
				implType.setClassName(interIter.next().replace('/', '.'));
				cType.getImplement().add(implType);
			}
			Iterator<FieldNode> fi=cc.fields.iterator();
			List cf=cType.getField();
			while(fi.hasNext()){
				FieldNode f=fi.next();
				FieldType ft=libFactory.createFieldType();
				ft.setAccess(Integer.toString(f.access));
				ft.setName(f.name);
				ft.setDesc(f.desc);
				ft.setSignature(f.signature);
				cf.add(ft);
			}
			Iterator<MethodNode> mi=cc.methods.iterator();
			List ms=cType.getMethod();
			while(mi.hasNext()){
				MethodNode m=mi.next();
				MethodType mt=libFactory.createMethodType();
				mt.setAccess(Integer.toString(m.access));
				mt.setName(m.name);
				mt.setDesc(m.desc);
				mt.setSignature(m.signature);
				Iterator<Type> re=m.returnType.iterator();
				if(cc.isLibClass()){
					ReturnTypeType rtt=libFactory.createReturnTypeType();
					rtt.setName(Type.getReturnType(m.desc).getClassName());
					mt.getReturnType().add(rtt);
				}
//				List t=mt.getReturnType();
//				while(re.hasNext()){
//					ReturnTypeType rtt=libFactory.createReturnTypeType();
//					rtt.setName(re.next().getClassName());
//					t.add(rtt);
//				}
				ms.add(mt);
			}
			pack.getClass_().add(cType);
		}
		return true;
	}
	public static boolean writePackageToXML(String fileName, PackageResource resource, String libName, String version) {
		File file = new File(fileName);
		DocumentRoot root = null;
		JavaLibFactory libFactory = JavaLibFactory.eINSTANCE;
		JavaLibType lib = null;
		if (file.exists()) {
			try {
				root = JavaLibResourceUtil.getInstance().load(new FileInputStream(file));
				JXPathContext javaContext = JXPathContext.newContext(new JavaConfig(file.getPath()));
				javaContext.setLenient(true);
				String context = BASE + "JavaLib[@name='" + libName + "' and @version='" + version + "']";
				if (javaContext.selectSingleNode(context) != null) // ���Ƿ��ظ�
					return false;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			root = JavaLibFactory.eINSTANCE.createDocumentRoot();
			root.setJavaConfig(libFactory.createJavaConfigType());
		}
		if (root == null)
			return false;
		JavaConfigType javaConfig = root.getJavaConfig();
		lib = libFactory.createJavaLibType();
		lib.setName(libName);
		lib.setVersion(version);
		javaConfig.getJavaLib().add(lib);
		if (resource.getPackageName().equals("")) {
			Iterator<PackageResource> packageIter = resource.getPackageIterator();
			while (packageIter.hasNext()) {
				PackageResource subRe = packageIter.next();
				PackageType pack = libFactory.createPackageType();
				pack.setName(subRe.getPackageName());
				writePackageToXML(pack, subRe);
				lib.getPackage().add(pack);
			}
		} else {
			PackageType pack = libFactory.createPackageType();
			pack.setName(resource.getPackageName());
			writePackageToXML(pack, resource);
			lib.getPackage().add(pack);
		}
		try {
			JavaLibResourceUtil.getInstance().save(root, new FileOutputStream(file));
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * ��resource���µ��Ӱ����д��XML
	 * 
	 * @param pack
	 * @param resource
	 * @return
	 */
	protected static boolean writePackageToXML(PackageType pack, PackageResource resource) {
		PackageType packageType = JavaLibFactory.eINSTANCE.createPackageType();
		packageType.setName(resource.getPackageName());

		Iterator<PackageResource> packageIter = resource.getPackageIterator();
		while (packageIter.hasNext()) {
			writePackageToXML(packageType, packageIter.next());
		}
		pack.getPackage().add(packageType);// �����Ӱ�
		writeClassContextToXML(pack, resource);
		return true;
	}
}

// end

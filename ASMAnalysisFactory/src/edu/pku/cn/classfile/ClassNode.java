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

import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.InnerClassNode;
import org.objectweb.asm.tree.MemberNode;

import edu.pku.cn.detector.ClassDetector;
import edu.pku.cn.detector.Detector;
import edu.pku.cn.detector.MethodDetector;
import edu.pku.cn.hierarchy.Repository;
import edu.pku.cn.hierarchy.SubType;
import edu.pku.cn.util.CodaProperties;
import edu.pku.cn.util.OpcodeUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 
 * @author Liuxizhiyi
 */
public class ClassNode extends MemberNode implements ClassVisitor {
	public HashMap<String, Integer> fieldMap;
	public HashMap<String, Integer> methodMap;

	/**
	 * The class version.
	 */
	public int version;

	/**
	 * The class's access flags (see {@link org.objectweb.asm.Opcodes}). This
	 * field also indicates if the class is deprecated.
	 */
	public int access;

	/**
	 * The internal name of the class (see
	 * {@link org.objectweb.asm.Type#getInternalName() getInternalName}).
	 */
	public String name;

	/**
	 * The signature of the class. Mayt be <tt>null</tt>.
	 */
	public String signature;

	/**
	 * The internal of name of the super class (see
	 * {@link org.objectweb.asm.Type#getInternalName() getInternalName}). For
	 * interfaces, the super class is {@link Object}. May be <tt>null</tt>, but
	 * only for the {@link Object} class.
	 */
	public String superName;

	/**
	 * The internal names of the class's interfaces (see
	 * {@link org.objectweb.asm.Type#getInternalName() getInternalName}). This
	 * list is a list of {@link String} objects.
	 */
	public HashSet<String> interfaces;

	/**
	 * The name of the source file from which this class was compiled. May be
	 * <tt>null</tt>.
	 */
	public String sourceFile;

	/**
	 * Debug information to compute the correspondance between source and
	 * compiled elements of the class. May be <tt>null</tt>.
	 */
	public String sourceDebug;

	/**
	 * The internal name of the enclosing class of the class. May be
	 * <tt>null</tt>.
	 */
	public String outerClass;

	/**
	 * The name of the method that contains the class, or <tt>null</tt> if the
	 * class is not enclosed in a method.
	 */
	public String outerMethod;

	/**
	 * The descriptor of the method that contains the class, or <tt>null</tt> if
	 * the class is not enclosed in a method.
	 */
	public String outerMethodDesc;

	/**
	 * Informations about the inner classes of this class. This list is a list
	 * of {@link InnerClassNode} objects.
	 * 
	 * @associates org.objectweb.asm.tree.InnerClassNode
	 */
	public List<InnerClassNode> innerClasses;

	/**
	 * The fields of this class. This list is a list of {@link FieldNode}
	 * objects.
	 * 
	 */
	public List<FieldNode> fields;

	/**
	 * The methods of this class. This list is a list of {@link MethodNode}
	 * objects.
	 * 
	 */
	public List<MethodNode> methods;

	public Type type;
	
	private int libAccess=0;
	
	public void setLibAccess(int libAccess) {
		this.libAccess = libAccess;
	}

	SubType child;
	public void addChild(SubType child) {
		getSubType().addChild(child);
	}
	public SubType getSubType() {
		if(child==null)
			child=new SubType(this);
		return child;
	}
	public void setSubType(SubType child) {
		this.child = child;
	}
	public boolean isLibClass() {
		return CodaProperties.isLibExpland(libAccess);
	}
	public boolean isJREClass(){
		return CodaProperties.isJreExpland(libAccess);
	}
	/**
	 * Constructs a new {@link ClassNode}.
	 */
	public ClassNode() {
		this.interfaces = new HashSet<String>();
		this.innerClasses = new ArrayList<InnerClassNode>();
		this.fields = new ArrayList<FieldNode>();
		this.methods = new ArrayList<MethodNode>();
		this.methodMap = new HashMap<String, Integer>();
		this.fieldMap = new HashMap<String, Integer>();
	}
	public boolean containMethod(String name,String desc){
		return methodMap.containsKey(name+desc);
	}
	public MethodNode getMethod(String name,String desc){
		String key=name+desc;
		if(methodMap.containsKey(key)){
			int i=methodMap.get(key);		
			return methods.get(i);
		}
		else{//this method may be a inherited method, so find it from its superClasses or interfaces
			List<ClassNode> farthers=this.getSuperClasses();
			farthers.addAll(this.getInterfaces());
			for(int i=0;i<farthers.size();i++){
				MethodNode mn=farthers.get(i).getMethod(name, desc);
				if(mn!=null)
					return mn;
			}
		}
		return null;
	}
	public FieldNode getField(String name){
		int i=fieldMap.get(name);
		return fields.get(i);
	}
	public Type getType(){
		if(type==null)
			type=Type.getObjectType(name);
		return type;
	}
	public boolean isInterface() {
		return (access & Opcodes.ACC_INTERFACE) != 0;
	}

	public ClassNode getSuperClass() {
		if(superName==null || superName.equals(""))
			return null;
		return Repository.getInstance().findClassNode(superName);
	}

	public List<ClassNode> getSuperClasses() {
		List<ClassNode> supers = new LinkedList<ClassNode>();
		ClassNode cc = getSuperClass();
		while (cc != null) {
			supers.add(cc);
			cc = cc.getSuperClass();
		}
		return supers;
	}
	
	/**
	 * added by Qian.
	 * to return all interfaces as well as super interfaces of this class
	 * @param loader 
	 * @return
	 */
	public List<ClassNode> getInterfaces(ClassNodeLoader loader){
		List<ClassNode> interfaces = new LinkedList<ClassNode>();
		if(this.interfaces!=null&&this.interfaces.size()!=0){
			Iterator<String> ite=this.interfaces.iterator();
			while(ite.hasNext()){
				String interNm=ite.next();
				if((interNm!=null)&&!interNm.equals("")){
					ClassNode inter= loader.loadClassNode(interNm, 0);
					interfaces.add(inter);
					if (inter != null){
						List<ClassNode> supers=inter.getInterfaces(loader);
						if(supers != null && supers.size()!=0)
							interfaces.addAll(supers);
					}
				}
				
			}
		}
		
		return interfaces;
	}
	
	public List<ClassNode> getInterfaces(){
		List<ClassNode> interfaces = new LinkedList<ClassNode>();
		if(this.interfaces!=null&&this.interfaces.size()!=0){
			Iterator<String> ite=this.interfaces.iterator();
			while(ite.hasNext()){
				String interNm=ite.next();
				if((interNm!=null)&&!interNm.equals("")){
					ClassNode inter=Repository.getInstance().findClassNode(interNm);
					if (inter != null) {
						interfaces.add(inter);
						List<ClassNode> supers=inter.getInterfaces();
						if(supers != null && supers.size()!=0)
							interfaces.addAll(supers);
					}
						
				}
				
			}
		}
		
		return interfaces;
	}
	
	public boolean instanceOf(ClassNode superClass) {
		if (superClass != null) {
			if (superClass.isInterface())
				return implementOf(superClass);
			ClassNode cc = this;
			ClassNode temp = null;
			while (cc != null) {
				if (cc.name.equals(superClass.name))
					return true;
				cc = cc.getSuperClass();
			}
		}
		return false;
	}

	public boolean implementOf(ClassNode superClass) {
		if (superClass.isInterface()) {
			if (interfaces != null && interfaces.contains(superClass.name))
				return true;
			if (interfaces == null) {
				ClassNode node=getSuperClass();
				if(node!=null)
					return node.implementOf(superClass);
			} else {
				Iterator<String> iter = interfaces.iterator();
				while (iter.hasNext()) {
					if (Repository.getInstance().findClassNode(iter.next()).implementOf(superClass))
						return true;
				}
			}
		}
		return false;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(OpcodeUtil.printClassAccess(access)).append(name);
		if (superName != null)
			builder.append(" extends ").append(superName);
		if (interfaces.size() > 0) {
			builder.append(" implement ");
			for (Object o : interfaces.toArray()) {
				builder.append(o).append(",");
			}
			builder.deleteCharAt(builder.length() - 1);
		}
		builder.append("{\n");
		for (FieldNode node : fields) {
			builder.append(node);
		}
		for (MethodNode node : methods) {
			builder.append(node);
		}
		builder.append("}");
		return builder.toString();
	}

	// ------------------------------------------------------------------------
	// Implementation of the ClassVisitor interface
	// ------------------------------------------------------------------------

	public void visit(final int version, final int access, final String name, final String signature,
			final String superName, final String[] interfaces) {
		this.version = version;
		this.access = access;		
		this.name = name.replace('/', '.');
		this.type=Type.getObjectType(name);
		this.signature = signature;
		if(superName!=null)
			this.superName = superName.replace('/', '.');
		if (interfaces != null) {
			for(int i=0;i<interfaces.length;i++)
				this.interfaces.add(interfaces[i].replace('/', '.'));
		}
	}

	public void visitSource(final String file, final String debug) {
		sourceFile = file;
		sourceDebug = debug;
	}

	public void visitOuterClass(final String owner, final String name, final String desc) {
		outerClass = owner;
		outerMethod = name;
		outerMethodDesc = desc;
	}

	public void visitInnerClass(final String name, final String outerName, final String innerName, final int access) {
		InnerClassNode icn = new InnerClassNode(name, outerName, innerName, access);
		innerClasses.add(icn);
	}

	public FieldVisitor visitField(final int access, final String name, final String desc, final String signature,
			final Object value) {
		FieldNode fn = new FieldNode(access, name, desc, signature, value);
		fn.decalringClass=this;
		fields.add(fn);
		fieldMap.put(name, fields.size() - 1);
		return fn;
	}

	public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature,
			final String[] exceptions) {
		MethodNode mn = new MethodNode(access, name, desc, signature, exceptions);
		mn.owner = this.name;
        mn.declaringClass=this;
		methods.add(mn);

		methodMap.put(name + desc, methods.size() - 1);
		return mn;
	}

	// ------------------------------------------------------------------------
	// Accept method
	// ------------------------------------------------------------------------

	/**
	 * Makes the given class visitor visit this class.
	 * 
	 * @param cv
	 *            a class visitor.
	 */
	public void accept(final ClassVisitor cv) {
		// visits header
		String[] interfaces = new String[this.interfaces.size()];
		this.interfaces.toArray(interfaces);
		cv.visit(version, access, name, signature, superName, interfaces);
		// visits source
		if (sourceFile != null || sourceDebug != null) {
			cv.visitSource(sourceFile, sourceDebug);
		}
		// visits outer class
		if (outerClass != null) {
			cv.visitOuterClass(outerClass, outerMethod, outerMethodDesc);
		}
		// visits attributes
		int i, n;
		n = visibleAnnotations == null ? 0 : visibleAnnotations.size();
		for (i = 0; i < n; ++i) {
			AnnotationNode an = (AnnotationNode) visibleAnnotations.get(i);
			an.accept(cv.visitAnnotation(an.desc, true));
		}
		n = invisibleAnnotations == null ? 0 : invisibleAnnotations.size();
		for (i = 0; i < n; ++i) {
			AnnotationNode an = (AnnotationNode) invisibleAnnotations.get(i);
			an.accept(cv.visitAnnotation(an.desc, false));
		}
		n = attrs == null ? 0 : attrs.size();
		for (i = 0; i < n; ++i) {
			cv.visitAttribute((Attribute) attrs.get(i));
		}
		// visits inner classes
		for (i = 0; i < innerClasses.size(); ++i) {
			((InnerClassNode) innerClasses.get(i)).accept(cv);
		}
		// visits fields
		for (i = 0; i < fields.size(); ++i) {
			((FieldNode) fields.get(i)).accept(cv);
		}
		// visits methods
		for (i = 0; i < methods.size(); ++i) {
			((MethodNode) methods.get(i)).accept(cv);
		}
		// visits end
		cv.visitEnd();
	}

	public void accept(Detector detector) {
		if (detector instanceof ClassDetector) {
			accept((ClassVisitor) detector);
			return;
		}
		else{
			for (int i = 0; i < methods.size(); i++) {
				MethodNode method = (MethodNode) methods.get(i);
				detector.setReference(method);
				// detector.initCFG(method,name);
				// detector.setName(method.name + method.desc);//method.name
				// -->method.name + method.desc, modified by Meng, Na
				method.accept(detector);
			}
		}
	}
}

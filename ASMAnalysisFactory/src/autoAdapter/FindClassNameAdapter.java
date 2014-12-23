package autoAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

//import com.sun.org.apache.bcel.internal.generic.Type;

public class FindClassNameAdapter extends ClassAdapter {

	public static String getClassNameFromFile(FileInputStream f) {
		FindClassNameAdapter eca = new FindClassNameAdapter(new ClassWriter(0));
		try {
			ClassReader cr = new ClassReader(f);
			cr.accept(eca, 0);
			return eca.className;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return "";
	}

	public FindClassNameAdapter(ClassVisitor cv) {
		super(cv);
		// TODO Auto-generated constructor stub
	}

	String className = "";

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		// TODO Auto-generated method stub

		this.className = name;
		// System.out.println("��"+name);
		super.visit(version, access, name, signature, superName, interfaces);
	}

}

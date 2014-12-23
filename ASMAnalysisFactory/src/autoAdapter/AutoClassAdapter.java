package autoAdapter;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import automachine.AutoMachine;
import automachine.AutoMachinePool;
import automachine.State;

public class AutoClassAdapter extends ClassAdapter {

	boolean DEBUG = true;
	AutoMachine am;
	State state;

	public AutoClassAdapter(ClassVisitor cv) {
		super(cv);
		// TODO Auto-generated constructor stub

	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		// TODO Auto-generated method stub
		super.visit(version, access, name, signature, superName, interfaces);
		if (DEBUG) {
			System.out.println("class" + name);
		}
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		// TODO Auto-generated method stub
		return new AutoMethodAdapter();
	}

	public FieldVisitor visitField(final int access, final String name, final String desc, final String signature,
			final Object value) {
		return null;
	}
}

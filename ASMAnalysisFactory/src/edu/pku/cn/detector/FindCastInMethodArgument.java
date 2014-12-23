package edu.pku.cn.detector;

import java.util.ArrayList;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

import edu.pku.cn.bugreport.BugInstance;

public class FindCastInMethodArgument extends MethodDetector {

	private int state = 0;

	public void visitIincInsn(int var, int increment) {
		// TODO Auto-generated method stub
		state = 0;
	}

	public void visitInsn(int opcode) {
		// TODO Auto-generated method stub
		switch (state) {
		case 0:
			if (opcode == Opcodes.I2L) {
				state = 1;
			}
			break;
		case 1:
			state = 0;
			break;
		}
	}

	public void visitIntInsn(int opcode, int operand) {
		// TODO Auto-generated method stub
		state = 0;
	}

	public void visitJumpInsn(int opcode, Label label) {
		// TODO Auto-generated method stub
		state = 0;
	}

	public void visitLdcInsn(Object cst) {
		// TODO Auto-generated method stub
		state = 0;
	}

	public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
		// TODO Auto-generated method stub
		state = 0;
	}

	public void visitMethodInsn(int opcode, String owner, String name, String desc) {
		// TODO Auto-generated method stub
		switch (state) {
		case 0:
			break;
		case 1:
			if (opcode == Opcodes.INVOKESTATIC && owner.equals("java.lang.Double") && name.equals("longBitsToDouble")
					&& desc.equals("(J)D")) {
				BugInstance instance = new BugInstance(getBugPattern("CONVERT_INT2DOUBLE").getShortDescription(),
						getLineNumber());
				reportor.report(node.owner, instance);
				// System.out.println("The parameter of
				// java.lang.Double.longBitsToDouble() is cast from int");
			}
			state = 0;
			break;
		}
	}

	public void visitMultiANewArrayInsn(String desc, int dims) {
		// TODO Auto-generated method stub
		state = 0;
	}

	public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels) {
		// TODO Auto-generated method stub
		state = 0;
	}

	public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
		// TODO Auto-generated method stub
		state = 0;
	}

	public void visitTypeInsn(int opcode, String type) {
		// TODO Auto-generated method stub
		state = 0;
	}

	public void visitVarInsn(int opcode, int var) {
		// TODO Auto-generated method stub
		state = 0;
	}

	@Override
	public ArrayList<Detector> getInstances() {
		// TODO Auto-generated method stub

		ArrayList<Detector> detectors = new ArrayList<Detector>();
		detectors.add(new FindCastInMethodArgument());
		return detectors;
	}

}

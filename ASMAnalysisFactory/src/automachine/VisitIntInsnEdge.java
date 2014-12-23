package automachine;

import org.objectweb.asm.Opcodes;

/**
 * MethodVisitor.visitIntInsn Visits an instruction with a single int operand.
 * 
 * @param opcode
 *            the opcode of the instruction to be visited. This opcode is either
 *            BIPUSH, SIPUSH or NEWARRAY.
 * @param operand
 *            the operand of the instruction to be visited.<br>
 *            When opcode is BIPUSH, operand value should be between
 *            Byte.MIN_VALUE and Byte.MAX_VALUE.<br>
 *            When opcode is SIPUSH, operand value should be between
 *            Short.MIN_VALUE and Short.MAX_VALUE.<br>
 *            When opcode is NEWARRAY, operand value should be one of
 *            {@link Opcodes#T_BOOLEAN}, {@link Opcodes#T_CHAR},
 *            {@link Opcodes#T_FLOAT}, {@link Opcodes#T_DOUBLE},
 *            {@link Opcodes#T_BYTE}, {@link Opcodes#T_SHORT},
 *            {@link Opcodes#T_INT} or {@link Opcodes#T_LONG}.
 */
public class VisitIntInsnEdge extends Edge {

	int opcode;
	int operand;

	public VisitIntInsnEdge(State fromState, State toState, int opcode, int operand) {
		super(fromState, toState);
		this.opcode = opcode;
		this.operand = operand;
	}

	public VisitIntInsnEdge(State fromState, State toState) {
		super(fromState, toState);
	}

	public int getOpcode() {
		return opcode;
	}

	public void setOpcode(int opcode) {
		this.opcode = opcode;
	}

	public int getOperand() {
		return operand;
	}

	public void setOperand(int operand) {
		this.operand = operand;
	}

	@Override
	public
	boolean isSameCondition(Edge otherEdge) {
		// TODO Auto-generated method stub
		if (!(otherEdge instanceof VisitIntInsnEdge))
			return false;
		VisitIntInsnEdge oe = (VisitIntInsnEdge) (otherEdge);

		return oe.getOpcode() == this.getOpcode() && oe.getOperand() == this.operand;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String str = this.getClass().getName() + " :";
		if (this.fromState != null)
			str += " from state " + this.fromState.stateNumber;
		if (this.toState != null)
			str += " to state " + this.toState.stateNumber;

		str += " opcode=" + AutomaUtil.convertOpcodeToString(this.opcode) + " operand=" + this.operand;

		return str;
	}

}

package automachine;

import org.objectweb.asm.Label;

/**
 * Visits a jump instruction. A jump instruction is an instruction that may jump
 * to another instruction.
 * 
 * @param opcode
 *            the opcode of the type instruction to be visited. This opcode is
 *            either IFEQ, IFNE, IFLT, IFGE, IFGT, IFLE, IF_ICMPEQ, IF_ICMPNE,
 *            IF_ICMPLT, IF_ICMPGE, IF_ICMPGT, IF_ICMPLE, IF_ACMPEQ, IF_ACMPNE,
 *            GOTO, JSR, IFNULL or IFNONNULL.
 * @param label
 *            the operand of the instruction to be visited. This operand is a
 *            label that designates the instruction to which the jump
 *            instruction may jump.
 */
public class VisitJumpInsnEdge extends Edge {

	int opcode;
	Label label;

	public VisitJumpInsnEdge(State fromState, State toState, int opcode, Label label) {
		super(fromState, toState);
		this.opcode = opcode;
		this.label = label;
	}

	public VisitJumpInsnEdge(State fromState, State toState) {
		super(fromState, toState);
	}

	public int getOpcode() {
		return opcode;
	}

	public void setOpcode(int opcode) {
		this.opcode = opcode;
	}

	public Label getLabel() {
		return label;
	}

	public void setLabel(Label label) {
		this.label = label;
	}

	@Override
	public
	boolean isSameCondition(Edge otherEdge) {
		// TODO Auto-generated method stub
		if (!(otherEdge instanceof VisitJumpInsnEdge))
			return false;
		VisitJumpInsnEdge oe = (VisitJumpInsnEdge) (otherEdge);

		return oe.getOpcode() == this.getOpcode()
		// not compare lables
		// && oe.getLabel().equals(this.label)
		;

	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String str = this.getClass().getName() + " :";
		if (this.fromState != null)
			str += " from state " + this.fromState.stateNumber;
		if (this.toState != null)
			str += " to state " + this.toState.stateNumber;

		str += " opcode=" + AutomaUtil.convertOpcodeToString(this.opcode) + " lable=" + this.label.toString();

		return str;
	}

}

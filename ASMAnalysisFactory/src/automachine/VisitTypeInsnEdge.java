package automachine;

import org.objectweb.asm.Type;

/**
 * Visits a type instruction. A type instruction is an instruction that takes
 * the internal name of a class as parameter.
 * 
 * @param opcode
 *            the opcode of the type instruction to be visited. This opcode is
 *            either NEW, ANEWARRAY, CHECKCAST or INSTANCEOF.
 * @param type
 *            the operand of the instruction to be visited. This operand must be
 *            the internal name of an object or array class (see
 *            {@link Type#getInternalName() getInternalName}).
 */
public class VisitTypeInsnEdge extends Edge {

	int opcode;
	String type;

	public VisitTypeInsnEdge(State fromState, State toState) {
		super(fromState, toState);
	}

	public int getOpcode() {
		return opcode;
	}

	public void setOpcode(int opcode) {
		this.opcode = opcode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public VisitTypeInsnEdge(State fromState, State toState, int opcode, String type) {
		super(fromState, toState);
		this.opcode = opcode;
		this.type = type;
	}

	@Override
	public
	boolean isSameCondition(Edge otherEdge) {
		// TODO Auto-generated method stub
		return otherEdge instanceof VisitTypeInsnEdge
				// &&
				// this.fromState.getStateNumber()==otherEdge.fromState.getStateNumber()
				// &&
				// this.toState.getStateNumber()==otherEdge.toState.getStateNumber()
				&& ((VisitTypeInsnEdge) otherEdge).getOpcode() == this.opcode
				&& AutomaUtil.stringEqual(this.type, ((VisitTypeInsnEdge) otherEdge).getType());
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String str = this.getClass().getName() + " :";
		if (this.fromState != null)
			str += " from state " + this.fromState.stateNumber;
		if (this.toState != null)
			str += " to state " + this.toState.stateNumber;

		str += " opcode=" + AutomaUtil.convertOpcodeToString(this.opcode) + " type=" + this.type;

		return str;
	}

}

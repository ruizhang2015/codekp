package automachine;

import java.util.Arrays;

/**
 * Visits a local variable instruction. A local variable instruction is an
 * instruction that loads or stores the value of a local variable.
 * 
 * @param opcode
 *            the opcode of the local variable instruction to be visited. This
 *            opcode is either ILOAD, LLOAD, FLOAD, DLOAD, ALOAD, ISTORE,
 *            LSTORE, FSTORE, DSTORE, ASTORE or RET.
 * @param var
 *            the operand of the instruction to be visited. This operand is the
 *            index of a local variable.
 */

public class VisitVarInsnEdge extends Edge {
	int opcode;
	int var;

	public VisitVarInsnEdge(State fromState, State toState, int opcode, int var) {
		super(fromState, toState);
		this.opcode = opcode;
		this.var = var;
	}

	public VisitVarInsnEdge(State fromState, State toState) {
		super(fromState, toState);
	}

	public int getOpcode() {
		return opcode;
	}

	public void setOpcode(int opcode) {
		this.opcode = opcode;
	}

	public int getVar() {
		return var;
	}

	public void setVar(int var) {
		this.var = var;
	}

	@Override
	public
	boolean isSameCondition(Edge otherEdge) {
		// TODO Auto-generated method stub
		if (!(otherEdge instanceof VisitVarInsnEdge))
			return false;
		VisitVarInsnEdge oe = (VisitVarInsnEdge) otherEdge;
		return oe.getOpcode() == this.opcode ;//&& oe.getVar() == this.var;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String str = this.getClass().getName() + " :";
		if (this.fromState != null)
			str += " from state " + this.fromState.stateNumber;
		if (this.toState != null)
			str += " to state " + this.toState.stateNumber;

		str += " opcode=" + AutomaUtil.convertOpcodeToString(this.opcode) + " var=" + this.var;

		return str;
	}

}

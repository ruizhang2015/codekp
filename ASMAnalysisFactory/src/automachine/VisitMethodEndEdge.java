package automachine;

import org.objectweb.asm.Type;

/**
 * Visits a method instruction. A method instruction is an instruction that
 * invokes a method.
 * 
 * @param opcode
 *            the opcode of the type instruction to be visited. This opcode is
 *            either INVOKEVIRTUAL, INVOKESPECIAL, INVOKESTATIC or
 *            INVOKEINTERFACE.
 * @param owner
 *            the internal name of the method's owner class (see
 *            {@link Type#getInternalName() getInternalName}).
 * @param name
 *            the method's name.
 * @param desc
 *            the method's descriptor (see {@link Type Type}).
 */
public class VisitMethodEndEdge extends Edge {

	public VisitMethodEndEdge(State fromState, State toState) {
		super(fromState, toState);
	}

	@Override
	public boolean isSameCondition(Edge otherEdge) {
		// TODO Auto-generated method stub
		if (!(otherEdge instanceof VisitMethodEndEdge))
			return false;
		VisitMethodEndEdge oe = (VisitMethodEndEdge)(otherEdge);
		if (this.isConditional == true) {
			if (AutomaUtil.stringMatch(oe.getConditionsRegExp(),this.conditionsRegExp))
				return true;
			else
				return false;
		} else {
			return true;
		}
	}

	@Override
	public String toString() {
		String str = " :";
		if (this.fromState != null)
			str += " from state " + this.fromState.stateNumber;
		if (this.toState != null)
			str += " to state " + this.toState.stateNumber;
		str += " EndOfTheMethod ";
		str = str + "ConditionsExpr: " + this.conditionsRegExp;
		return str;
	}

}

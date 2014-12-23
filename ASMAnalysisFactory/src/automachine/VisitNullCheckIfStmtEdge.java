package automachine;

/**
 * Added by Guangtai Liang on Jan 5 2012 Visits a null-check if instruction.
 * 
 * @param opType
 *            the opType of the null-check if instruction to be visited. This opcode is
 *            either == or !=
 */

public class VisitNullCheckIfStmtEdge extends Edge {

	public String opType;

	public VisitNullCheckIfStmtEdge(State fromState, State toState) {
		super(fromState, toState);
	}

	public VisitNullCheckIfStmtEdge(State fromState, State toState, String opType) {
		super(fromState, toState);
		this.opType = opType;
	}

	@Override
	public boolean isSameCondition(Edge otherEdge) {
		if (!(otherEdge instanceof VisitNullCheckIfStmtEdge))
			return false;

		boolean result = false;
		VisitNullCheckIfStmtEdge oe = (VisitNullCheckIfStmtEdge) (otherEdge);

		if (this.opType.equals(oe.opType)) {
			result = true;
		}

		if (result && this.isConditional == true) {
			if (!AutomaUtil.stringMatch(oe.getConditionsRegExp(), this.conditionsRegExp))
				result = false;
		}

		return result;
	}

	@Override
	public String toString() {
		String str = " :";
		if (this.fromState != null)
			str += " from state " + this.fromState.stateNumber;
		if (this.toState != null)
			str += " to state " + this.toState.stateNumber;
		str += " opType = " + this.opType;
		return str;
	}

}

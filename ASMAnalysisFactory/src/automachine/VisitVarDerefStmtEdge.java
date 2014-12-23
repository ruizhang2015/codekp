package automachine;

/**
 * Added by Guangtai Liang on Jan 5 2012 Visits a null-check if instruction.
 */

public class VisitVarDerefStmtEdge extends Edge {
	public VisitVarDerefStmtEdge(State fromState, State toState) {
		super(fromState, toState);
	}
	
	@Override
	public boolean isSameCondition(Edge otherEdge) {
		if (!(otherEdge instanceof VisitVarDerefStmtEdge))
			return false;

		boolean result = true;
		VisitVarDerefStmtEdge oe = (VisitVarDerefStmtEdge) (otherEdge);

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
		str += " variable reference";
		return str;
	}
}

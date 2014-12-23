package automachine;

/**
 * Visits an IINC instruction.
 * 
 * @param var
 *            index of the local variable to be incremented.
 * @param increment
 *            amount to increment the local variable by.
 */
public class VisitIincInsnEdge extends Edge {

	int var;
	int increment;

	public int getVar() {
		return var;
	}

	public void setVar(int var) {
		this.var = var;
	}

	public int getIncrement() {
		return increment;
	}

	public void setIncrement(int increment) {
		this.increment = increment;
	}

	public VisitIincInsnEdge(State fromState, State toState, int var, int increment) {
		super(fromState, toState);
		this.var = var;
		this.increment = increment;
	}

	public VisitIincInsnEdge(State fromState, State toState) {
		super(fromState, toState);
	}

	@Override
	public
	boolean isSameCondition(Edge otherEdge) {
		// TODO Auto-generated method stub
		if (!(otherEdge instanceof VisitIincInsnEdge))
			return false;
		VisitIincInsnEdge oe = (VisitIincInsnEdge) otherEdge;
		return oe.getIncrement() == this.increment && oe.getVar() == this.var;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String str = this.getClass().getName() + " :";
		if (this.fromState != null)
			str += " from state " + this.fromState.stateNumber;
		if (this.toState != null)
			str += " to state " + this.toState.stateNumber;

		str += " var=" + this.var + " increment=" + this.increment;

		return str;
	}

}

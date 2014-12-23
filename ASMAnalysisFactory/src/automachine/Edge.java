package automachine;

public abstract class Edge extends Object {
	State fromState;
	State toState;
	// database id;
	public int edgeId;
	public boolean isConditional = false;
	public String conditionsRegExp = "";

	public abstract boolean isSameCondition(Edge otherEdge);

	public State getFromState() {
		return fromState;
	}

	public void setFromState(State fromState) {
		this.fromState = fromState;
	}

	public State getToState() {
		return toState;
	}

	public void setToState(State toState) {
		this.toState = toState;
	}

	public Edge(State fromState, State toState) {
		super();
		this.fromState = fromState;
		this.toState = toState;
	}

	public Edge() {
		super();
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String edgeStr = "";
		
		if (this.isConditional){
			edgeStr = " conditional edge (" + this.conditionsRegExp + ")";	
		}
		else
			edgeStr = " non-conditional edge";
		
		if (this.fromState != null)
			edgeStr += " from " + this.fromState.stateNumber;
		if (this.toState != null)
			edgeStr += " to " + this.toState.stateNumber;
		
		return edgeStr;
	}

	public String getConditionsRegExp() {
		return conditionsRegExp;
	}

	public void setConditionsRegExp(String conditionsRegExp) {
		this.conditionsRegExp = conditionsRegExp;
	}

	public boolean isConditional() {
		return isConditional;
	}

	public void setConditional(boolean isConditional) {
		this.isConditional = isConditional;
	}

}

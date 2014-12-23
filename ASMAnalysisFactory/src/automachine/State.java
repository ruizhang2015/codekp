package automachine;

import java.util.ArrayList;
import java.util.HashMap;

import autoAdapter.PrecisionDiscountsUnit;

public class State {
	// is it one of the end states of auto machine
	boolean isEndState = false;
	boolean isOriginState = false;
	private boolean isFixedState = false;

	// database id
	public int stateId;
	// the number of this sate
	int stateNumber = 0;

	// exception target state
	State exceptionState;

	// the last line achieved this sate
	int lastLine = 0;

	// out edges
	ArrayList<Edge> outEdges = new ArrayList<Edge>();

	// record the current preciison discounts context
	public HashMap<Integer, PrecisionDiscountsUnit> precisionDiscountSources = new HashMap<Integer, PrecisionDiscountsUnit>();
	public PrecisionDiscountsUnit discountsUnit = new PrecisionDiscountsUnit();

	// add a edge
	boolean addEdge(Edge e) {

		for (Edge ee : outEdges) {
			if (ee.isSameCondition(e))
				return true;
		}
		outEdges.add(e);
		return true;
	}

	State goOneStep(Edge e) {
		for (Edge ee : outEdges) {
			if (ee.isSameCondition(e))
				return ee.toState;
		}
		return this.exceptionState;
	}

	//
	//	
	// set and get
	public boolean isEndState() {
		return isEndState;
	}

	public void setEndState(boolean isEndState) {
		this.isEndState = isEndState;
	}

	public int getStateNumber() {
		return stateNumber;
	}

	public void setStateNumber(int stateNumber) {
		this.stateNumber = stateNumber;
	}

	public State getExceptionState() {
		return exceptionState;
	}

	public void setExceptionState(State exceptionState) {
		this.exceptionState = exceptionState;
	}

	public ArrayList<Edge> getOutEdges() {
		return outEdges;
	}

	public void setOutEdges(ArrayList<Edge> outEdges) {
		this.outEdges = outEdges;
	}

	public State() {
		super();
	}

	public boolean isOriginState() {
		return isOriginState;
	}

	public void setOriginState(boolean isOriginState) {
		this.isOriginState = isOriginState;
	}

	public State(boolean isEndState, boolean isOriginState, int stateNumber, State exceptionState,
			ArrayList<Edge> outEdges) {
		super();
		this.isEndState = isEndState;
		this.isOriginState = isOriginState;
		this.stateNumber = stateNumber;
		this.exceptionState = exceptionState;
		this.outEdges = outEdges;
	}

	public int getLastLine() {
		return lastLine;
	}

	public void setLastLine(int lastLine) {
		this.lastLine = lastLine;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String stateStr = "";
		// stateStr += "stateId = " + this.stateId + "\tstateNumber = " +
		// this.stateNumber + "\tisEndState = "
		// + this.isEndState + "\tisOriginState=" + this.isOriginState + "\n\t
		// has " + this.outEdges.size()
		// + " out edges\n";
		stateStr += "\tstateNumber = " + this.stateNumber + " isEndState = " + this.isEndState + " isOriginState="
				+ this.isOriginState + " isFixedState =" + this.isFixedState + "\n";
		for (Edge e : this.outEdges) {
			stateStr += "\t" + e.toString() + "\n";
		}

		return stateStr;
	}

	public void setFixedState(boolean isFixed) {
		this.isFixedState = isFixed;
	}

	public boolean isFixedState() {
		return isFixedState;
	}
}

package automachine;

import java.util.ArrayList;
import java.util.HashMap;

import autoAdapter.PrecisionDiscountsUnit;

public class AutoMachine extends Object {

	public AutoMachine() {
		super();

	}

	// database id
	public int automachineId;
	// is the machine must end ? if not, then there's some defects
	public String automachineName;
	public boolean endMeanViolateDefect = false;

	// need CFG to get information of program context
	boolean needCFG = true;

	// all Opcode push objects' auto machine
	boolean allOpcodePushOjectAutomachine = false;

	// concern class
	String onlyConcernClass;

	// severe level
	int severeLevel = 0;

	public int lastStateChangingLine = 0; 
	// the current state
	public State currentState;

	// the origin state
	State originState;

	// all states,including exceptional state
	ArrayList<State> states = new ArrayList<State>();

	// description
	String description;
	String errorMessage;

	boolean Debug = false;

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public void goOneStep(Edge e, int currentLine) throws AutoMachineException {
		if (Debug) {
			System.out.println("am hashcode=" + this.hashCode());
			System.out.println("old state=" + this.currentState.getStateNumber());
			if (e instanceof VisitMethodInsnEdge) {
				System.out.println("****************************");
				System.out.println(((VisitMethodInsnEdge) e).name);
			}
		}
		State newState = currentState.goOneStep(e);
		if (newState.getStateNumber() != currentState.getStateNumber()) {
			
			// System.out.println("go one step");
			this.currentState = newState;
			this.currentState.setLastLine(currentLine);
			this.lastStateChangingLine = currentLine;
			
		}
		
		if (Debug) {
			System.out.println("new state=" + this.currentState.getStateNumber());
		}
		if (this.endMeanViolateDefect && this.currentState.isEndState()) {
			// System.out.println(this.getErrorMessage());
			initial();
			throw new AutoMachineException(this.getErrorMessage());
		}
	}

	public void goOneStep(Edge e, int currentLine, HashMap<Integer, PrecisionDiscountsUnit> precisionDiscountSources, int currentLevel) throws AutoMachineException {
		if (Debug) {
			System.out.println("am hashcode=" + this.hashCode());
			System.out.println("old state=" + this.currentState.getStateNumber());
			if (e instanceof VisitMethodInsnEdge) {
				System.out.println("****************************");
				System.out.println(((VisitMethodInsnEdge) e).name);
			} else if (e instanceof VisitNewInsnEdge) {
				System.out.println("****************************");
				System.out.println(((VisitNewInsnEdge) e).toString());
			}
		}
		State newState = currentState.goOneStep(e);
		if (newState.getStateNumber() != currentState.getStateNumber()) {
			
			// System.out.println("go one step");
			this.currentState = newState;
			this.currentState.discountsUnit = precisionDiscountSources.get(currentLevel).clone();
			this.currentState.setLastLine(currentLine);
			this.lastStateChangingLine = currentLine; 
		}
		
		
		if (Debug) {
			System.out.println("new state=" + this.currentState.getStateNumber());
		}
		if (this.endMeanViolateDefect && this.currentState.isEndState()) {
			// System.out.println(this.getErrorMessage());
			initial();
			throw new AutoMachineException(this.getErrorMessage());
		}

	}
	
	public void checkOutWhenVistEnd() throws AutoMachineException {

		if ((this.endMeanViolateDefect && this.currentState.isEndState())
				|| (!this.endMeanViolateDefect && !this.currentState.isEndState() && !this.currentState.isOriginState())) {
			initial();
			throw new AutoMachineException(this.getErrorMessage());

		}

	}

	// //////

	public void initial() {

		for (State s : this.states) {
			if (s.isOriginState()) {
				this.originState = s;

				break;
			}
		}
		currentState = originState;

	}

	private State getStateByStateNumber(int num) {
		for (State s : this.states) {
			if (s.stateNumber == num)
				return s;
		}

		return null;
	}

	public AutoMachine getCopy() {
		AutoMachine copy = new AutoMachine();

		copy.automachineId = this.automachineId;
		copy.endMeanViolateDefect = this.endMeanViolateDefect;
		copy.needCFG = this.needCFG;
		copy.allOpcodePushOjectAutomachine = this.allOpcodePushOjectAutomachine;
		copy.onlyConcernClass = this.onlyConcernClass;
		copy.severeLevel = this.severeLevel;
		copy.description = this.description;
		copy.errorMessage = this.errorMessage;
		copy.automachineName = this.automachineName;

		for (State s : this.states) {
			State state = copy.getStateByNumber(s.stateNumber);
			if (state == null) {
				state = new State();
				copy.states.add(state);
			}
			state.isEndState = s.isEndState;
			state.isOriginState = s.isOriginState;
			state.stateId = s.stateId;
			state.stateNumber = s.stateNumber;
			state.lastLine = s.lastLine;
			state.discountsUnit = s.discountsUnit.clone();
			state.setFixedState(s.isFixedState());
			
			// 需要深复制
			for(Edge e : s.outEdges) {
				AutomaUtil.addAnEdgeBetweenTwoStates(state, copy.getStateByNumber((new Integer(e.toState.getStateNumber()))), 
						e);
			}
						
			State exceptionState = copy.getStateByNumber(s.exceptionState.stateNumber);
			if (exceptionState == null) {
				exceptionState = new State();
				exceptionState.stateNumber = s.exceptionState.stateNumber;
				copy.states.add(exceptionState);
			}
			state.exceptionState = exceptionState;
		}
		copy.initial();

		// System.out.println("get one copy:");
		// System.out.println(copy);
		return copy;
	}

	public AutoMachine getCompleteCopy() {
		AutoMachine copy = new AutoMachine();

		copy.automachineId = this.automachineId;
		copy.endMeanViolateDefect = this.endMeanViolateDefect;
		copy.needCFG = this.needCFG;
		copy.allOpcodePushOjectAutomachine = this.allOpcodePushOjectAutomachine;
		copy.onlyConcernClass = this.onlyConcernClass;
		copy.severeLevel = this.severeLevel;
		copy.description = this.description;
		copy.errorMessage = this.errorMessage;
		copy.automachineName = this.automachineName;
		copy.states = this.states;
		copy.currentState = this.getStateByStateNumber(this.currentState.stateNumber);
		
		copy.initial();

		// System.out.println("get one copy:");
		// System.out.println(copy);

		return copy;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String amStr = "";
		amStr += "am dbID= " + this.automachineId + "\tname=" + this.automachineName + "\tdesc=" + this.description
				+ "\t errmsg=" + this.errorMessage + "\tonlyConcernClass=" + this.onlyConcernClass + "\tsevereLevel="
				+ this.severeLevel + "\tallOpcodePushOjectAutomachine=" + this.allOpcodePushOjectAutomachine
				+ "\tendMeanViolateDefect=" + this.endMeanViolateDefect + "\tneedCFG=" + this.needCFG + "\thave "
				+ this.states.size() + " states:\n";
		amStr += currentState.toString() + "\n";
		for (State s : this.states) {
			amStr += "\t" + s.toString() + "\n";
		}
		return amStr;
	}

	public boolean equals(AutoMachine obj) {
		if (this.automachineName == obj.automachineName && this.currentState.stateId == obj.automachineId) {
			return true;
		} else
			return false;
	}

	public void setEndMeanViolateDefect(boolean mustEnd) {
		this.endMeanViolateDefect = mustEnd;
	}

	public State getCurrentState() {
		return currentState;
	}

	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}

	public State getOriginState() {
		return originState;
	}

	public void setOriginState(State originState) {
		this.originState = originState;
	}

	public ArrayList<State> getStates() {
		return states;
	}

	public void setStates(ArrayList<State> states) {
		this.states = states;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	// get existed state by number ,or create an empty state with the number and
	// returns it
	public State getStateByNumber(int number) {
		for (State state : this.states) {
			if (state.getStateNumber() == number)
				return state;
		}
		State s = new State();
		s.setStateNumber(number);
		addState(s);
		return s;
	}

	private void addState(State state) {
		if (state != null)
			this.states.add(state);
	}

	public int getSevereLevel() {
		return severeLevel;
	}

	public void setSevereLevel(int severeLevel) {
		this.severeLevel = severeLevel;
	}

	public boolean isEndMeanViolateDefect() {
		return endMeanViolateDefect;
	}

	public boolean isNeedCFG() {
		return needCFG;
	}

	public void setNeedCFG(boolean needCFG) {
		this.needCFG = needCFG;
	}

	public String getOnlyConcernClass() {
		return onlyConcernClass;
	}

	public void setOnlyConcernClass(String concernClass) {
		this.onlyConcernClass = concernClass;
	}

	public boolean isAllOpcodePushOjectAutomachine() {
		return allOpcodePushOjectAutomachine;
	}

	public void setAllOpcodePushOjectAutomachine(boolean allOpcodePushOjectAutomachine) {
		this.allOpcodePushOjectAutomachine = allOpcodePushOjectAutomachine;
	}

}

package automachine;

import org.objectweb.asm.Label;

/**
 * Visits a local variable declaration.
 * 
 * @param name
 *            the name of a local variable.
 * @param desc
 *            the type descriptor of this local variable.
 * @param signature
 *            the type signature of this local variable. May be <tt>null</tt> if
 *            the local variable type does not use generic types.
 * @param start
 *            the first instruction corresponding to the scope of this local
 *            variable (inclusive).
 * @param end
 *            the last instruction corresponding to the scope of this local
 *            variable (exclusive).
 * @param index
 *            the local variable's index.
 * @throws IllegalArgumentException
 *             if one of the labels has not already been visited by this visitor
 *             (by the {@link #visitLabel visitLabel} method).
 */
public class VisitLocalVariableEdge extends Edge {

	String name;
	String desc;
	String signature;
	Label start;
	Label end;
	int index;

	public VisitLocalVariableEdge(State fromState, State toState) {
		super(fromState, toState);
	}

	public VisitLocalVariableEdge(State fromState, State toState, String name, String desc, String signature,
			Label start, Label end, int index) {
		super(fromState, toState);
		this.name = name;
		this.desc = desc;
		this.signature = signature;
		this.start = start;
		this.end = end;
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public Label getStart() {
		return start;
	}

	public void setStart(Label start) {
		this.start = start;
	}

	public Label getEnd() {
		return end;
	}

	public void setEnd(Label end) {
		this.end = end;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public
	boolean isSameCondition(Edge otherEdge) {
		// TODO Auto-generated method stub
		if (!(otherEdge instanceof VisitLocalVariableEdge))
			return false;
		VisitLocalVariableEdge oe = (VisitLocalVariableEdge) otherEdge;
		return AutomaUtil.stringEqual(oe.getDesc(), this.desc) && AutomaUtil.stringEqual(oe.getName(), this.name)
				&& AutomaUtil.stringEqual(oe.getSignature(), this.signature)
		// not compare lables and index
		// && oe.getIndex()==this.index
		// && oe.getStart().equals(this.start)
		// && oe.getEnd().equals(this.end)
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

		str += " index=" + this.index + " name=" + this.name + " signature" + this.signature + " desc=" + this.desc
				+ " start=" + this.start + " end" + this.end;

		return str;
	}

}

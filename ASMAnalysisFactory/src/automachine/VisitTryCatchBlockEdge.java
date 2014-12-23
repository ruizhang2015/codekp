package automachine;

import java.util.Arrays;

import org.objectweb.asm.Label;

/**
 * Visits a try catch block.
 * 
 * @param start
 *            beginning of the exception handler's scope (inclusive).
 * @param end
 *            end of the exception handler's scope (exclusive).
 * @param handler
 *            beginning of the exception handler's code.
 * @param type
 *            internal name of the type of exceptions handled by the handler, or
 *            <tt>null</tt> to catch any exceptions (for "finally" blocks).
 * @throws IllegalArgumentException
 *             if one of the labels has already been visited by this visitor (by
 *             the {@link #visitLabel visitLabel} method).
 */
public class VisitTryCatchBlockEdge extends Edge {

	Label start;
	Label end;
	Label handler;
	String type;

	public VisitTryCatchBlockEdge(State fromState, State toState) {
		super(fromState, toState);
	}

	public VisitTryCatchBlockEdge(State fromState, State toState, Label start, Label end, Label handler, String type) {
		super(fromState, toState);
		this.start = start;
		this.end = end;
		this.handler = handler;
		this.type = type;
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

	public Label getHandler() {
		return handler;
	}

	public void setHandler(Label handler) {
		this.handler = handler;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public
	boolean isSameCondition(Edge otherEdge) {
		// TODO Auto-generated method stub
		if (!(otherEdge instanceof VisitTryCatchBlockEdge))
			return false;
		VisitTryCatchBlockEdge oe = (VisitTryCatchBlockEdge) otherEdge;
		return AutomaUtil.stringEqual(oe.getType(), this.type)
		/*
		 * not campare lables && oe.getStart().equals(this.start) &&
		 * oe.getEnd().equals(this.end) && oe.getHandler().equals(this.handler)
		 */
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

		str += " start=" + this.start + " end=" + this.end + " handler" + this.handler + " type=" + this.type;

		return str;
	}

}

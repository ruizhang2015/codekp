package automachine;

import org.objectweb.asm.Type;

/**
 * Visits a LDC instruction.
 * 
 * @param cst
 *            the constant to be loaded on the stack. This parameter must be a
 *            non null {@link Integer}, a {@link Float}, a {@link Long}, a
 *            {@link Double} a {@link String} (or a {@link Type} for
 *            <tt>.class</tt> constants, for classes whose version is 49.0 or
 *            more).
 */
public class VisitLdcInsnEdge extends Edge {

	Object cst;

	public VisitLdcInsnEdge(State fromState, State toState, Object cst) {
		super(fromState, toState);
		this.cst = cst;
	}

	public VisitLdcInsnEdge(State fromState, State toState) {
		super(fromState, toState);
	}

	public Object getCst() {
		return cst;
	}

	public void setCst(Object cst) {
		this.cst = cst;
	}

	@Override
	public
	boolean isSameCondition(Edge otherEdge) {
		// TODO Auto-generated method stub
		if (!(otherEdge instanceof VisitLdcInsnEdge))
			return false;
		VisitLdcInsnEdge oe = (VisitLdcInsnEdge) otherEdge;
		return oe.getCst().equals(this.cst);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String str = this.getClass().getName() + " :";
		if (this.fromState != null)
			str += " from state " + this.fromState.stateNumber;
		if (this.toState != null)
			str += " to state " + this.toState.stateNumber;

		str += " cst=" + this.cst;

		return str;
	}

}

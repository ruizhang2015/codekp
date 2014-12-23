package automachine;

import java.util.Arrays;

import org.objectweb.asm.Type;

/**
 * Visits a MULTIANEWARRAY instruction.
 * 
 * @param desc
 *            an array type descriptor (see {@link Type Type}).
 * @param dims
 *            number of dimensions of the array to allocate.
 */
public class VisitNewInsnEdge extends Edge {

	String type;
	
	public VisitNewInsnEdge(State fromState, State toState) {
		super(fromState, toState);
	}

	public VisitNewInsnEdge(State fromState, State toState, String type) {
		super(fromState, toState);
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public
	boolean isSameCondition(Edge otherEdge) {
		// TODO Auto-generated method stub
		if (!(otherEdge instanceof VisitNewInsnEdge))
			return false;
		VisitNewInsnEdge oe = (VisitNewInsnEdge) otherEdge;
		return AutomaUtil.stringEqual(oe.getType(), this.type);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String str = this.getClass().getName() + " :";
		if (this.fromState != null)
			str += " from state " + this.fromState.stateNumber;
		if (this.toState != null)
			str += " to state " + this.toState.stateNumber;

		str += " type=" + this.type;
		return str;
	}

}

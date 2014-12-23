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
public class VisitMultiANewArrayInsnEdge extends Edge {

	String desc;
	int dims;

	public VisitMultiANewArrayInsnEdge(State fromState, State toState) {
		super(fromState, toState);
	}

	public VisitMultiANewArrayInsnEdge(State fromState, State toState, String desc, int dims) {
		super(fromState, toState);
		this.desc = desc;
		this.dims = dims;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getDims() {
		return dims;
	}

	public void setDims(int dims) {
		this.dims = dims;
	}

	@Override
	public
	boolean isSameCondition(Edge otherEdge) {
		// TODO Auto-generated method stub
		if (!(otherEdge instanceof VisitMultiANewArrayInsnEdge))
			return false;
		VisitMultiANewArrayInsnEdge oe = (VisitMultiANewArrayInsnEdge) otherEdge;
		return AutomaUtil.stringEqual(oe.getDesc(), this.desc) && oe.getDims() == this.dims;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String str = this.getClass().getName() + " :";
		if (this.fromState != null)
			str += " from state " + this.fromState.stateNumber;
		if (this.toState != null)
			str += " to state " + this.toState.stateNumber;

		str += " desc=" + this.desc + " dims=" + this.dims;

		return str;
	}

}

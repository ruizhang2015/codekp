package automachine;

import java.util.Arrays;

import org.objectweb.asm.Label;

/**
 * Visits a TABLESWITCH instruction.
 * 
 * @param min
 *            the minimum key value.
 * @param max
 *            the maximum key value.
 * @param dflt
 *            beginning of the default handler block.
 * @param labels
 *            beginnings of the handler blocks. <tt>labels[i]</tt> is the
 *            beginning of the handler block for the <tt>min + i</tt> key.
 */
public class VisitTableSwitchInsnEdge extends Edge {

	int min;
	int max;
	Label dflt;
	Label[] labels;

	public VisitTableSwitchInsnEdge(State fromState, State toState) {
		super(fromState, toState);
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public Label getDflt() {
		return dflt;
	}

	public void setDflt(Label dflt) {
		this.dflt = dflt;
	}

	public Label[] getLabels() {
		return labels;
	}

	public void setLabels(Label[] labels) {
		this.labels = labels;
	}

	public VisitTableSwitchInsnEdge(State fromState, State toState, int min, int max, Label dflt, Label[] labels) {
		super(fromState, toState);
		this.min = min;
		this.max = max;
		this.dflt = dflt;
		this.labels = labels;
	}

	@Override
	public
	boolean isSameCondition(Edge otherEdge) {
		// TODO Auto-generated method stub

		if (!(otherEdge instanceof VisitTableSwitchInsnEdge))
			return false;
		VisitTableSwitchInsnEdge oe = (VisitTableSwitchInsnEdge) otherEdge;
		return oe.getDflt().equals(this.dflt) && Arrays.equals(oe.getLabels(), this.labels) && oe.getMin() == this.min
				&& oe.getMax() == this.max;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String str = this.getClass().getName() + " :";
		if (this.fromState != null)
			str += " from state " + this.fromState.stateNumber;
		if (this.toState != null)
			str += " to state " + this.toState.stateNumber;

		str += " min=" + this.min + " max=" + this.max + " dflt" + this.dflt;

		return str;
	}

}

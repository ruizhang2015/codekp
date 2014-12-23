package automachine;

import java.util.Arrays;

import org.objectweb.asm.Label;

/**
 * Visits a LOOKUPSWITCH instruction.
 * 
 * @param dflt
 *            beginning of the default handler block.
 * @param keys
 *            the values of the keys.
 * @param labels
 *            beginnings of the handler blocks. <tt>labels[i]</tt> is the
 *            beginning of the handler block for the <tt>keys[i]</tt> key.
 */
public class VisitLookupSwitchInsnEdge extends Edge {
	Label dflt;
	int[] keys;
	Label[] labels;

	public VisitLookupSwitchInsnEdge(State fromState, State toState) {
		super(fromState, toState);
	}

	public Label getDflt() {
		return dflt;
	}

	public void setDflt(Label dflt) {
		this.dflt = dflt;
	}

	public int[] getKeys() {
		return keys;
	}

	public void setKeys(int[] keys) {
		this.keys = keys;
	}

	public Label[] getLabels() {
		return labels;
	}

	public void setLabels(Label[] labels) {
		this.labels = labels;
	}

	public VisitLookupSwitchInsnEdge(State fromState, State toState, Label dflt, int[] keys, Label[] labels) {
		super(fromState, toState);
		this.dflt = dflt;
		this.keys = keys;
		this.labels = labels;
	}

	@Override
	public
	boolean isSameCondition(Edge otherEdge) {
		// TODO Auto-generated method stub
		if (!(otherEdge instanceof VisitLookupSwitchInsnEdge))
			return false;
		VisitLookupSwitchInsnEdge oe = (VisitLookupSwitchInsnEdge) otherEdge;
		return Arrays.equals(oe.getKeys(), this.keys)
		// not compare lables
		// && oe.getDflt().equals(this.dflt)
		// && Arrays.equals(oe.getLabels(),this.labels)
		;
	}

}

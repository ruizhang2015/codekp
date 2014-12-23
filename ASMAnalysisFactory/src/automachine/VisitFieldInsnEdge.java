package automachine;

import org.objectweb.asm.Type;

/**
 * Visits a field instruction. A field instruction is an instruction that loads
 * or stores the value of a field of an object.
 * 
 * @param opcode
 *            the opcode of the type instruction to be visited. This opcode is
 *            either GETSTATIC, PUTSTATIC, GETFIELD or PUTFIELD.
 * @param owner
 *            the internal name of the field's owner class (see
 *            {@link Type#getInternalName() getInternalName}).
 * @param name
 *            the field's name.
 * @param desc
 *            the field's descriptor (see {@link Type Type}).
 */
public class VisitFieldInsnEdge extends Edge {

	int opcode;
	String owner;
	String name;
	String desc;

	public VisitFieldInsnEdge(State fromState, State toState) {
		super(fromState, toState);
	}

	@Override
	public
	boolean isSameCondition(Edge otherEdge) {

		if (!(otherEdge instanceof VisitFieldInsnEdge))
			return false;
		VisitFieldInsnEdge oe = (VisitFieldInsnEdge) (otherEdge);

		return oe.getOpcode() == this.getOpcode()
				&& AutomaUtil.stringEqual(this.owner.replace('/', '.'), oe.getOwner().replace('/', '.'))
				&& AutomaUtil.stringEqual(this.name, oe.getName()) && AutomaUtil.stringEqual(this.desc, oe.getDesc());

	}

	public VisitFieldInsnEdge(State fromState, State toState, int opcode, String owner, String name, String desc) {
		super(fromState, toState);
		this.opcode = opcode;
		this.owner = owner;
		this.name = name;
		this.desc = desc;
	}

	public int getOpcode() {
		return opcode;
	}

	public void setOpcode(int opcode) {
		this.opcode = opcode;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
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

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String str = this.getClass().getName() + " :";
		if (this.fromState != null)
			str += " from state " + this.fromState.stateNumber;
		if (this.toState != null)
			str += " to state " + this.toState.stateNumber;

		str += " opcode=" + AutomaUtil.convertOpcodeToString(this.opcode) + " name=" + this.name + " owner"
				+ this.owner + " desc=" + this.desc;

		return str;
	}

}

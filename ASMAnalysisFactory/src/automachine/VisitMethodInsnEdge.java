package automachine;

import org.objectweb.asm.Type;

/**
 * Visits a method instruction. A method instruction is an instruction that
 * invokes a method.
 * 
 * @param opcode
 *            the opcode of the type instruction to be visited. This opcode is
 *            either INVOKEVIRTUAL, INVOKESPECIAL, INVOKESTATIC or
 *            INVOKEINTERFACE.
 * @param owner
 *            the internal name of the method's owner class (see
 *            {@link Type#getInternalName() getInternalName}).
 * @param name
 *            the method's name.
 * @param desc
 *            the method's descriptor (see {@link Type Type}).
 */
public class VisitMethodInsnEdge extends Edge {

	int opcode;
	String owner;
	String name;
	String desc;
	// add by zhouzhiyi to describe if we need the leftvalue or rightvalue as
	// our variable
	boolean bindToReturnValue = false;
	public boolean isApplyForChild = false;

	public boolean isApplyForChild() {
		return isApplyForChild;
	}

	public void setApplyForChild(boolean isApplyForChild) {
		this.isApplyForChild = isApplyForChild;
	}

	public VisitMethodInsnEdge(State fromState, State toState) {
		super(fromState, toState);
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

	// add by zhouzhiyi
	public void setBindToReturnValue(boolean bindToReturnValue) {
		this.bindToReturnValue = bindToReturnValue;
	}

	public boolean getBindToReturnValue() {
		return this.bindToReturnValue;
	}

	// add by zhouzhiyi end
	public VisitMethodInsnEdge(State fromState, State toState, int opcode, String owner, String name, String desc) {
		super(fromState, toState);
		this.opcode = opcode;
		this.owner = owner;
		this.name = name;
		this.desc = desc;
	}

	@Override
	public boolean isSameCondition(Edge otherEdge) {
		// TODO Auto-generated method stub
		if (!(otherEdge instanceof VisitMethodInsnEdge))
			return false;
		
		boolean result = false;
		VisitMethodInsnEdge oe = (VisitMethodInsnEdge) (otherEdge);
		if (oe.isApplyForChild) {
			if ((AutomaUtil.stringMatch(oe.getName(), this.name) || Boolean.parseBoolean(name))
					&& (AutomaUtil.stringMatch(oe.getDesc(), this.desc) || Boolean.parseBoolean(desc))) {
				result = true;
			}
		} else {
			if (bindToReturnValue)
				result = AutomaUtil.stringEqualWithReturnValue(oe.owner, desc);
			else
				result = AutomaUtil.stringEqualIgnoreDotSlash(this.owner, oe.getOwner());
			result = result && AutomaUtil.stringMatch(oe.getName(), this.name)
					&& AutomaUtil.stringMatch(oe.getDesc(), this.desc);
		}
		
		if (result) {
			if (this.isConditional == true) {
				if (!AutomaUtil.stringMatch(oe.getConditionsRegExp(), this.conditionsRegExp))
					result = false;
			}
		}
		
		return result;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String str = " :";
		if (this.fromState != null)
			str += " from state " + this.fromState.stateNumber;
		if (this.toState != null)
			str += " to state " + this.toState.stateNumber;

		str += " opcode=" + AutomaUtil.convertOpcodeToString(this.opcode) + " name=" + this.name + " owner="
				+ this.owner + " desc=" + this.desc;

		return str;
	}

}

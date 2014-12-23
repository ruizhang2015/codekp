package automachine;

/**
 * @author Lijinhui
 * time: 10:22:32 AM
 *
 */

/**
 * according to MethodVisitor.visitInsn()
 * 
 * Visits a zero operand instruction.
 * 
 * @param opcode
 *            the opcode of the instruction to be visited. This opcode is either
 *            NOP, ACONST_NULL, ICONST_M1, ICONST_0, ICONST_1, ICONST_2,
 *            ICONST_3, ICONST_4, ICONST_5, LCONST_0, LCONST_1, FCONST_0,
 *            FCONST_1, FCONST_2, DCONST_0, DCONST_1, IALOAD, LALOAD, FALOAD,
 *            DALOAD, AALOAD, BALOAD, CALOAD, SALOAD, IASTORE, LASTORE, FASTORE,
 *            DASTORE, AASTORE, BASTORE, CASTORE, SASTORE, POP, POP2, DUP,
 *            DUP_X1, DUP_X2, DUP2, DUP2_X1, DUP2_X2, SWAP, IADD, LADD, FADD,
 *            DADD, ISUB, LSUB, FSUB, DSUB, IMUL, LMUL, FMUL, DMUL, IDIV, LDIV,
 *            FDIV, DDIV, IREM, LREM, FREM, DREM, INEG, LNEG, FNEG, DNEG, ISHL,
 *            LSHL, ISHR, LSHR, IUSHR, LUSHR, IAND, LAND, IOR, LOR, IXOR, LXOR,
 *            I2L, I2F, I2D, L2I, L2F, L2D, F2I, F2L, F2D, D2I, D2L, D2F, I2B,
 *            I2C, I2S, LCMP, FCMPL, FCMPG, DCMPL, DCMPG, IRETURN, LRETURN,
 *            FRETURN, DRETURN, ARETURN, RETURN, ARRAYLENGTH, ATHROW,
 *            MONITORENTER, or MONITOREXIT.
 */
public class VisitInsnEdge extends Edge {

	int opcode;

	public VisitInsnEdge(State fromState, State toState, int opcode) {
		super(fromState, toState);
		this.opcode = opcode;
	}

	public VisitInsnEdge(State fromState, State toState) {
		super(fromState, toState);
	}

	public int getOpcode() {
		return opcode;
	}

	public void setOpcode(int opcode) {
		this.opcode = opcode;
	}

	@Override
	public
	boolean isSameCondition(Edge otherEdge) {
		// TODO Auto-generated method stub
		if (!(otherEdge instanceof VisitInsnEdge))
			return false;
		VisitInsnEdge oe = (VisitInsnEdge) (otherEdge);

		return oe.getOpcode() == this.getOpcode();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String str = this.getClass().getName() + " :";
		if (this.fromState != null)
			str += " from state " + this.fromState.stateNumber;
		if (this.toState != null)
			str += " to state " + this.toState.stateNumber;

		str += " opcode=" + AutomaUtil.convertOpcodeToString(this.opcode);

		return str;
	}

}

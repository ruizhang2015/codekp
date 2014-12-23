/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-6-17 ����11:16:24
 * @modifier: Liuxizhiyi
 * @time 2008-6-17 ����11:16:24
 * @reviewer: Liuxizhiyi
 * @time 2008-6-17 ����11:16:24
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */

/**
 * 
 * @author Liuxizhiyi
 */
public enum EdgeType {
	/*
	 * ----------------------------------------------------------------------
	 * Edge types
	 * ----------------------------------------------------------------------
	 */

	/**
	 * Edge type for fall-through to next instruction.
	 */
	FALL_THROUGH_EDGE,
	/**
	 * Edge type for IFCMP instructions when condition is true.
	 */
	IFCMP_EDGE_TRUE,
	/**
	 * Edge type for IFCMP instructions when condition is False.
	 */
	IFCMP_EDGE_FALSE,

	/**
	 * Edge type for switch instructions (explicit case).
	 */
	SWITCH_EDGE,
	/**
	 * Edge type for switch instructions (default case).
	 */
	SWITCH_DEFAULT_EDGE,
	/**
	 * Edge type for JSR instructions.
	 */
	JSR_EDGE,
	/**
	 * Edge type for RET instructions.
	 */
	RET_EDGE,
	/**
	 * Edge type for GOTO instructions.
	 */
	GOTO_EDGE,
	/**
	 * Edge type for RETURN instructions. (These must go to the exit node of the
	 * CFG).
	 */
	RETURN_EDGE,
	/**
	 * Edge representing the possibility that an exception might propagate out
	 * of the current method. Such edges always go to the exit node in the CFG.
	 */
	UNHANDLED_EXCEPTION_EDGE,
	/**
	 * Edge representing control flow from an exception-raising basic block to
	 * an explicit handler for the exception.
	 */
	HANDLED_EXCEPTION_EDGE,

	/**
	 * Edge type for ATHROW instructions.
	 */
	ATHROW_EDGE,

	FINALLY_EDGE,
	/**
	 * Unknown edge type.
	 */
	UNKNOWN_EDGE;

	public static void main(String[] args) {
		EdgeType type = EdgeType.ATHROW_EDGE;
		EdgeType type2 = EdgeType.ATHROW_EDGE;
		if (type.equals(type2)) {
			System.out.println("equals");
		}
		System.out.println(type2);

	}
}

// end

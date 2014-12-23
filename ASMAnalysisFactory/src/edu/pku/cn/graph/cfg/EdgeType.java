/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-5-22 ����03:46:26
 * @modifier: Liuxizhiyi
 * @time 2008-5-22 ����03:46:26
 * @reviewer: Liuxizhiyi
 * @time 2008-5-22 ����03:46:26
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
/*
 * Bytecode Analysis Framework
 * Copyright (C) 2003,2004 University of Maryland
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package edu.pku.cn.graph.cfg;

/**
 *
 * @author Liuxizhiyi
 */
/**
 * Constants defining the type of control flow edges, as well as flags defining
 * additional information about the edges.
 * 
 * @see Edge
 */
public interface EdgeType {
	/*
	 * ----------------------------------------------------------------------
	 * Edge types
	 * ----------------------------------------------------------------------
	 */

	/**
	 * Edge type for fall-through to next instruction.
	 */
	public static final int FALL_THROUGH_EDGE = 0;
	/**
	 * Edge type for IFCMP instructions when condition is true.
	 */
	public static final int IFCMP_EDGE_JUMP = 1;
	/**
	 * Edge type for IFCMP instructions when condition is False.
	 */
	public static final int IFCMP_EDGE_FALLTHROUGH = 2;

	/**
	 * Edge type for switch instructions (explicit case).
	 */
	public static final int SWITCH_EDGE = 3;
	/**
	 * Edge type for switch instructions (default case).
	 */
	public static final int SWITCH_DEFAULT_EDGE = 4;
	/**
	 * Edge type for JSR instructions.
	 */
	public static final int JSR_EDGE = 5;
	/**
	 * Edge type for RET instructions.
	 */
	public static final int RET_EDGE = 6;
	/**
	 * Edge type for GOTO instructions.
	 */
	public static final int GOTO_EDGE = 7;
	/**
	 * Edge type for RETURN instructions. (These must go to the exit node of the
	 * CFG).
	 */
	public static final int RETURN_EDGE = 8;
	/**
	 * Edge representing the possibility that an exception might propagate out
	 * of the current method. Such edges always go to the exit node in the CFG.
	 */
	public static final int UNHANDLED_EXCEPTION_EDGE = 9;
	/**
	 * Edge representing control flow from an exception-raising basic block to
	 * an explicit handler for the exception.
	 */
	public static final int HANDLED_EXCEPTION_EDGE = 10;

	/**
	 * Edge type for ATHROW instructions.
	 */
	public static final int ATHROW_EDGE = 11;

	public static final int FINALLY_EDGE = 12;
	/**
	 * Unknown edge type.
	 */
	public static final int UNKNOWN_EDGE = 13;

	/*
	 * ----------------------------------------------------------------------
	 * Edge flags
	 * ----------------------------------------------------------------------
	 */

	public static final int TREE_EDGE = 14;
	public static final int CIRCLE_EDGE = 15;
	public static final int CROSS_EDGE = 16;
	/**
	 * Checked exceptions can be thrown on edge.
	 */
	public static final int CHECKED_EXCEPTIONS_FLAG = 1;

	/**
	 * Explicit exceptions can be thrown on the edge.
	 */
	public static final int EXPLICIT_EXCEPTIONS_FLAG = 2;

	public static final String[] Type = { "FALL_THROUGH_EDGE", "IFCMP_EDGE_JUMP", "IFCMP_EDGE_FALLTHROUGH",
			"SWITCH_EDGE", "SWITCH_DEFAULT_EDGE", "JSR_EDGE", "RET_EDGE", "GOTO_EDGE", "RETURN_EDGE",
			"UNHANDLED_EXCEPTION_EDGE", "HANDLED_EXCEPTION_EDGE",

			"ATHROW_EDGE", "FINALLY_EDGE", "UNKNOWN_EDGE", "TREE_EDGE", "CIRCLE_EDGE", "CROSS_EDGE" };
}

// end

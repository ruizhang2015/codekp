/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author MENG Na
 * @time 2008-11-16 ����08:07:49
 * @modifier: MENG Na
 * @time 2008-11-16 ����08:07:49
 * @reviewer: MENG Na
 * @time 2008-11-16 ����08:07:49
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.analysis.common;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;

public class Subroutine {

	public LabelNode start;

	public boolean[] access;

	public List callers;

	private Subroutine() {
	}

	public Subroutine(final LabelNode start, final int maxLocals, final JumpInsnNode caller) {
		this.start = start;
		this.access = new boolean[maxLocals];
		this.callers = new ArrayList();
		callers.add(caller);
	}

	public Subroutine copy() {
		Subroutine result = new Subroutine();
		result.start = start;
		result.access = new boolean[access.length];
		System.arraycopy(access, 0, result.access, 0, access.length);
		result.callers = new ArrayList(callers);
		return result;
	}

	public boolean merge(final Subroutine subroutine) throws AnalyzerException {
		boolean changes = false;
		for (int i = 0; i < access.length; ++i) {
			if (subroutine.access[i] && !access[i]) {
				access[i] = true;
				changes = true;
			}
		}
		if (subroutine.start == start) {
			for (int i = 0; i < subroutine.callers.size(); ++i) {
				Object caller = subroutine.callers.get(i);
				if (!callers.contains(caller)) {
					callers.add(caller);
					changes = true;
				}
			}
		}
		return changes;
	}
}

// end

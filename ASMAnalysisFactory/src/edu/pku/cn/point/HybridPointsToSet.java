/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-5 обнГ03:28:07
 * @modifier: a
 * @time 2010-1-5 обнГ03:28:07
 * @reviewer: a
 * @time 2010-1-5 обнГ03:28:07
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.point;

import java.util.BitSet;
import java.util.Set;

import org.objectweb.asm.Type;

/**
 * @author zhouzhiyi
 */
public class HybridPointsToSet implements PointsToSet {
	public Ref refs[];
	public static final int smallSize=16;
	BitSet ref;
	@Override
	public boolean hasNonEmptyIntersection(PointsToSet other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<String> possibleStringConstants() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Type> possibleTypes() {
		// TODO Auto-generated method stub
		return null;
	}

}

// end

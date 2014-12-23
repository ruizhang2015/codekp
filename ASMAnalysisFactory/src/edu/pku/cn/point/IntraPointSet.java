/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-5 обнГ03:42:13
 * @modifier: a
 * @time 2010-1-5 обнГ03:42:13
 * @reviewer: a
 * @time 2010-1-5 обнГ03:42:13
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.point;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.objectweb.asm.Type;

/**
 * @author zhouzhiyi
 */
public class IntraPointSet implements PointsToSet {
	Set<Ref> refs=new HashSet<Ref>(16, 0.7f);
	@Override
	public boolean hasNonEmptyIntersection(PointsToSet other) {
		IntraPointSet points=(IntraPointSet)other;
		Iterator<Ref> iter=points.refs.iterator();
		while(iter.hasNext()){
			if(refs.contains(iter))
				return true;
		}
		return false;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return refs.isEmpty();
	}

	@Override
	public Set<String> possibleStringConstants() {
//		// TODO Auto-generated method stub
//		Set<String> strCons=new HashSet<String>();
//		Iterator<Ref> iter=refs.iterator();
//		while(iter.hasNext()){
//			Ref ref=iter.next();
//		}
		return null;
	}

	@Override
	public Set<Type> possibleTypes() {
		Set<Type> types=new HashSet<Type>();
		Iterator<Ref> iter=refs.iterator();
		while(iter.hasNext()){
			types.add(iter.next().getType());
		}
		return types;
	}

	public boolean add(Ref e) {
		return refs.add(e);
	}

	public boolean contains(Object o) {
		return refs.contains(o);
	}

}

// end

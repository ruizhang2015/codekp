/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-5 обнГ01:36:55
 * @modifier: a
 * @time 2010-1-5 обнГ01:36:55
 * @reviewer: a
 * @time 2010-1-5 обнГ01:36:55
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.point;

import java.util.Set;

import org.objectweb.asm.Type;

/** A generic interface to some set of runtime objects computed by a
 * pointer analysis.
 * @author zhouzhiyi
 */
public interface PointsToSet {
    /** Returns true if this set contains no run-time objects. */
    public boolean isEmpty();
    /** Returns true if this set shares some objects with other. */
    public boolean hasNonEmptyIntersection( PointsToSet other );
    /** Set of all possible run-time types of objects in the set. */
    public Set<Type> possibleTypes();

    /** If this points-to set consists entirely of string constants,
     * returns a set of these constant strings.
     * If this point-to set may contain something other than constant
     * strings, returns null. */
    public Set<String> possibleStringConstants();

    /** If this points-to set consists entirely of objects of
     * type java.lang.Class of a known class,
     * returns a set of ClassConstant's that are these classes.
     * If this point-to set may contain something else, returns null. */
//    public Set<ClassConstant> possibleClassConstants();
}

// end

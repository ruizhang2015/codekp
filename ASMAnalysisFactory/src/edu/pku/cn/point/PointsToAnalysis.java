/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-5 обнГ01:46:35
 * @modifier: a
 * @time 2010-1-5 обнГ01:46:35
 * @reviewer: a
 * @time 2010-1-5 обнГ01:46:35
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.point;

import edu.pku.cn.classfile.FieldNode;
import edu.pku.cn.classfile.LocalVariableNode;
import edu.pku.cn.classfile.MethodNode;

/**
 * A generic interface to any type of pointer analysis.
 * @author zhouzhiyi
 */
public interface PointsToAnalysis {
    /** Returns the set of objects pointed to by variable l. */
    public PointsToSet reachingObjects( LocalVariableNode l );

    /** Returns the set of objects pointed to by variable l in context c. */
    public PointsToSet reachingObjects( MethodNode c, LocalVariableNode l );

    /** Returns the set of objects pointed to by static field f. */
    public PointsToSet reachingObjects( FieldNode f );

    /** Returns the set of objects pointed to by instance field f
     * of the objects in the PointsToSet s. */
    public PointsToSet reachingObjects( PointsToSet s, FieldNode f );

    /** Returns the set of objects pointed to by instance field f
     * of the objects pointed to by l. */
    public PointsToSet reachingObjects( LocalVariableNode l, FieldNode f );

    /** Returns the set of objects pointed to by instance field f
     * of the objects pointed to by l in context c. */
    public PointsToSet reachingObjects( MethodNode c, LocalVariableNode l, FieldNode f );

    /** Returns the set of objects pointed to by elements of the arrays
     * in the PointsToSet s. */
    public PointsToSet reachingObjectsOfArrayElement( PointsToSet s );
}

// end

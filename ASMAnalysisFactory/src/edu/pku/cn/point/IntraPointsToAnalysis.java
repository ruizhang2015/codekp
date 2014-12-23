/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-5 обнГ04:30:11
 * @modifier: a
 * @time 2010-1-5 обнГ04:30:11
 * @reviewer: a
 * @time 2010-1-5 обнГ04:30:11
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.point;

import java.util.HashMap;

import org.objectweb.asm.tree.AbstractInsnNode;

import edu.pku.cn.classfile.FieldNode;
import edu.pku.cn.classfile.LocalVariableNode;
import edu.pku.cn.classfile.MethodNode;

/**
 * @author zhouzhiyi
 */
public class IntraPointsToAnalysis implements PointsToAnalysis {
	HashMap<LocalVariableNode, Ref> localRef;
	HashMap<FieldNode,Ref> fieldRef;
	public void analyze(MethodNode node){
		localRef=new HashMap<LocalVariableNode, Ref>(node.maxLocals,0.7f);
		fieldRef=new HashMap<FieldNode, Ref>();
		for(int i=0;i<node.instructions.size();i++){
			AbstractInsnNode insn=node.instructions.get(i);
			
		}
	}
	@Override
	public PointsToSet reachingObjects(LocalVariableNode l) {
		// TODO Auto-generated method stub
		Ref ref=localRef.get(l);
		if(ref==null)
			return null;
		return ref.getPointsToSet();
	}

	@Override
	public PointsToSet reachingObjects(MethodNode c, LocalVariableNode l) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PointsToSet reachingObjects(FieldNode f) {
		Ref ref=fieldRef.get(f);
		if(ref==null)
			return null;
		return ref.getPointsToSet();
	}

	@Override
	public PointsToSet reachingObjects(PointsToSet s, FieldNode f) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PointsToSet reachingObjects(LocalVariableNode l, FieldNode f) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PointsToSet reachingObjects(MethodNode c, LocalVariableNode l,
			FieldNode f) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PointsToSet reachingObjectsOfArrayElement(PointsToSet s) {
		// TODO Auto-generated method stub
		return null;
	}

}

// end

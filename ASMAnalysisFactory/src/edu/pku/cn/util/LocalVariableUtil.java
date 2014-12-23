/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-3-25 ����08:52:41
 * @modifier: Administrator
 * @time 2009-3-25 ����08:52:41
 * @reviewer: Administrator
 * @time 2009-3-25 ����08:52:41
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;

public class LocalVariableUtil {

	/**
	 * index local variables in localVariableMap
	 * 
	 * @param localVariableMap
	 * @return
	 */
	public static Map<LocalVariableNode, Integer> indexAllLocalVariables(Map localVariableMap) {
		Map<LocalVariableNode, Integer> lvIndexMap = new HashMap<LocalVariableNode, Integer>();
		int allIndex = 0;
		Set<Entry<Integer, List>> entries = localVariableMap.entrySet();
		for (Entry<Integer, List> entry : entries) {
			List lvNodes = entry.getValue();
			for (int i = 0; i < lvNodes.size(); i++) {
				lvIndexMap.put((LocalVariableNode) lvNodes.get(i), allIndex++);
			}
		}
		return lvIndexMap;
	}

	public static LocalVariableNode lookForLvNode(Map<Integer, List> localVariableMap, int index, int var) {
		LocalVariableNode tempNode = null;
		List lvList = localVariableMap.get(var);
		int maxStart = -1;
		int minEnd = Integer.MAX_VALUE;
		for (int i = 0; i < lvList.size(); i++) {
			LocalVariableNode lvNode = (LocalVariableNode) lvList.get(i);
			if (lvNode.start.index <= index + 1 && lvNode.end.index >= index && lvNode.start.index >= maxStart
					&& lvNode.end.index <= minEnd) {
				maxStart = lvNode.start.index;
				minEnd = lvNode.end.index;
				tempNode = lvNode;
			}
		}
		if (tempNode == null) {
			try {
				throw new AnalyzerException("The wanted localVariable in LiveVariableDatflowAnalysis is not found!");
			} catch (AnalyzerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return tempNode;
	}

	public static int getAllIndex(Map<Integer, List> localVariableMap, Map<LocalVariableNode, Integer> lvIndexMap,
			int index, int var) {
		LocalVariableNode tempNode = lookForLvNode(localVariableMap, index, var);
		return lvIndexMap.get(tempNode);
	}
}

// end

/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Guangtai Liang
 * @time 2010-11-16
 * @modifier: Administrator
 * @time 2010-11-16
 * @reviewer: Administrator
 * @time 2010-11-16
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */

package autoAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import automachine.AutoMachine;

/**
 * @author Guangtai Liang
 */
/**
 * 
 * @author Guangtai Liang
 */
public class PrecisionDiscountsStack extends Object {

	// Integer: level
	HashMap<Integer, PrecisionDiscountsUnit> stack = new HashMap<Integer, PrecisionDiscountsUnit>();

	public PrecisionDiscountsStack() {
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Integer level : stack.keySet()) {
			builder.append("level:" + level).append("->" + ((PrecisionDiscountsUnit) stack.get(level)).toString())
					.append("\n");
		}
		return builder.toString();
	}

	public void updateDiscountsWhenObfuscateInterObjects(Integer level, List<String> objects, String callerSigniture,
			int lineNumber, String calleeSigniture,
			double interObjPrecision) {

		if (this.stack.get(level) == null) {
			this.stack.put(new Integer(level), new PrecisionDiscountsUnit(1));
		}
		PrecisionDiscountsUnit unit = this.stack.get(level);
		
		String interMethod = unit.getCallStackStr() + level + "-" + callerSigniture + "-" + lineNumber
				+ ":" + calleeSigniture;
		for (String object : objects) {
			if (unit.getHistoricalInterAmbiguities().get(object) == null) {
				unit.getHistoricalInterAmbiguities().put(object, new HashMap<String, Double>());
			}
			HashMap<String, Double> ambiguities = unit.getHistoricalInterAmbiguities().get(object);
			ambiguities.put(interMethod, interObjPrecision);
		}
	}

	public void updateDiscountsWhenUnableLoadMethods(Integer level, List<String> objects, String callerSigniture,
			int lineNumber, String calleeSigniture) {
		// key: objName; value: the object-related unableLoadedMethodsNumber
		// String：callStackStr:directCallerLevel-directCallerSigniture-lineNumberOfDirectCaller:calleeSigniture;
		// private HashMap<String, HashSet<String>> unableLoadedMethodsMethods =
		// new HashMap<String, HashSet<String>>();
		if (this.stack.get(level) == null) {
			this.stack.put(new Integer(level), new PrecisionDiscountsUnit(1));
		}
		PrecisionDiscountsUnit unit = this.stack.get(level);
		String unableLoadedMethod = unit.getCallStackStr() + level + "-" + callerSigniture + "-" + lineNumber
				+ ":" + calleeSigniture;
		for (String object : objects) {
			if (unit.getUnableLoadedMethods().get(object) == null) {
				unit.getUnableLoadedMethods().put(object, new HashSet<String>());
			}
			unit.getUnableLoadedMethods().get(object).add(unableLoadedMethod);
		}
	}

	public void updateDiscountsWhenExceedMaxInterDepth(Integer level, List<String> objects, String callerSigniture,
			int lineNumber, String calleeSigniture) {

		// 记录对于每个object来说，对其准确度会造成影响的 自启动分析以来 “超过最大跨过程深度” 的总次数
		// String：callStackStr:directCallerLevel-directCallerSigniture-lineNumberOfDirectCaller:calleeSigniture;
		// private HashMap<String, HashSet<String>>
		// historicalExceedMaxInterDeptMethods = new HashMap<String,
		// HashSet<String>>();
		// String由如下部分组成： 1. callStackStr
		// 2. 无法跟进函数信息（lineNumberOfDirectCaller-calleeSigniture）
		// 合并后：callStackStr:directCallerLevel-directCallerSigniture-lineNumberOfDirectCaller:calleeSigniture;
		// private HashSet<String> exceedMaxInterDepthMethods = new
		// HashSet<String>();

		if (this.stack.get(level) == null) {
			this.stack.put(new Integer(level), new PrecisionDiscountsUnit(1));
		}

		PrecisionDiscountsUnit unit = this.stack.get(level);
		String exceedMaxInterDeptMethod = unit.getCallStackStr() + level + "-" + callerSigniture + "-"
				+ lineNumber + ":" + calleeSigniture;
		unit.getExceedMaxInterDepthMethods().add(exceedMaxInterDeptMethod);
		for (String object : objects) {
			if (unit.getHistoricalExceedMaxInterDeptMethods().get(object) == null) {
				unit.getHistoricalExceedMaxInterDeptMethods().put(object, new HashSet<String>());
			}
			unit.getHistoricalExceedMaxInterDeptMethods().get(object).add(exceedMaxInterDeptMethod);
		}
	}

	public void updateDiscountsWhenExceedMaxIterTimes(Integer level, Set<String> objects, String callerSigniture) {
		if (this.stack.get(level) == null) {
			this.stack.put(new Integer(level), new PrecisionDiscountsUnit(1));
		}

		// 记录对于每个object来说，对其准确度会造成影响的 自启动分析以来 “超过最大迭代次数” 的 受检方法信息
		// String: 所在函数信息（callStackStr:directCallerLevel-directCallerSigniture）；
		// private HashMap<String, HashSet<String>>
		// historicalExceedMaxIterMethods = new HashMap<String,
		// HashSet<String>>();
		PrecisionDiscountsUnit unit = this.stack.get(level);
		String exceedMaxIterTimesMethod = unit.getCallStackStr() + level + "-" + callerSigniture;
		for (String object : objects) {
			if (unit.getHistoricalExceedMaxIterMethods().get(object) == null) {
				unit.getHistoricalExceedMaxIterMethods().put(object, new HashSet<String>());
			}
			unit.getHistoricalExceedMaxIterMethods().get(object).add(exceedMaxIterTimesMethod);
		}
	}

}

// end

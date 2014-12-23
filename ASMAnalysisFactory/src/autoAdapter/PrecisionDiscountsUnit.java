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

import edu.pku.cn.util.OpcodeUtil;

/**
 * @author Guangtai Liang
 */
/**
 * 
 * @author Guangtai Liang
 */
public class PrecisionDiscountsUnit extends Object {

	private double invokePrecision = 1; // ��ǰ����������������׼ȷ��
	private boolean exceedMaxIterNumber = false; // �������������޵��ۼƴ���
	// private int exceedMaxInterDepthTotalNumber = 0; // ���γ�����������ȵĴ���

	// String: callerName-callerSite(lineNumberOfCallerSite)
	public HashMap<Integer, String> callStack = new HashMap<Integer, String>();
	private String callStackStr = ""; // e.g.: 0-main-22:1-f1-23:(2-f2-24:)

	// String�����²�����ɣ� 1. callStackStr
	// 2. �޷�����������Ϣ��lineNumberOfDirectCaller-calleeSigniture��
	// �ϲ���callStackStr:directCallerLevel-directCallerSigniture-lineNumberOfDirectCaller:calleeSigniture;
	private HashSet<String> exceedMaxInterDepthMethods = new HashSet<String>();

	// ��¼����ÿ��object��˵������׼ȷ�Ȼ����Ӱ��� �������������� ������������������ �� �ܼ췽����Ϣ
	// String: ���ں�����Ϣ��callStackStr:directCallerLevel-directCallerSigniture����
	private HashMap<String, HashSet<String>> historicalExceedMaxIterMethods = new HashMap<String, HashSet<String>>();

	// ��¼����ÿ��object��˵������׼ȷ�Ȼ����Ӱ��� �������������� ���������������ȡ� ���ܴ���
	// String��callStackStr:directCallerLevel-directCallerSigniture-lineNumberOfDirectCaller:calleeSigniture;
	private HashMap<String, HashSet<String>> historicalExceedMaxInterDeptMethods = new HashMap<String, HashSet<String>>();

	// key: objName; value: the object-related unableLoadedMethodsNumber
	// String��callStackStr:directCallerLevel-directCallerSigniture-lineNumberOfDirectCaller:calleeSigniture;
	private HashMap<String, HashSet<String>> unableLoadedMethods = new HashMap<String, HashSet<String>>();

	// key: objName; value: the object-related historicalInterAmbiguities
	// HashMap<String,Double>: string ->
	// callStackStr:directCallerLevel-directCallerSigniture-lineNumberOfDirectCaller:calleeSigniture;
	// double: ģ����
	private HashMap<String, HashMap<String, Double>> historicalInterAmbiguities = new HashMap<String, HashMap<String, Double>>();

	public PrecisionDiscountsUnit() {
	}

	PrecisionDiscountsUnit(double invokePrecision) {
		this.invokePrecision = invokePrecision;
	}

	public void computeCallStackString() {
		StringBuffer str = new StringBuffer();
		for (Integer level : callStack.keySet()) {
			str.append(level).append("-").append(callStack.get(level)).append(":");
		}
		this.callStackStr = str.toString();
	}

	public PrecisionDiscountsUnit clone() {
		PrecisionDiscountsUnit newUnit = new PrecisionDiscountsUnit(this.invokePrecision);

		newUnit.exceedMaxIterNumber = this.exceedMaxIterNumber;

		newUnit.callStack = PrecisionDiscountsUnit.cloneCallStack(this.callStack);
		newUnit.computeCallStackString();

		newUnit.exceedMaxInterDepthMethods = (HashSet<String>) this.exceedMaxInterDepthMethods.clone();

		newUnit.historicalExceedMaxIterMethods = PrecisionDiscountsUnit
				.cloneHistoricalData(this.historicalExceedMaxIterMethods);
		newUnit.historicalExceedMaxInterDeptMethods = PrecisionDiscountsUnit
				.cloneHistoricalData(this.historicalExceedMaxInterDeptMethods);
		newUnit.unableLoadedMethods = PrecisionDiscountsUnit
				.cloneHistoricalData(this.unableLoadedMethods);

		newUnit.historicalInterAmbiguities = PrecisionDiscountsUnit
				.cloneHistoricalData2(this.historicalInterAmbiguities);

		return newUnit;
	}

	private static HashMap<Integer, String> cloneCallStack(HashMap<Integer, String> callStack2) {
		HashMap<Integer, String> newData = new HashMap<Integer, String>();
		for (Integer key : callStack2.keySet()) {
			String value = callStack2.get(key);
			newData.put(key, value);
		}
		return newData;
	}

	// ���
	static public HashMap<String, HashSet<String>> cloneHistoricalData(HashMap<String, HashSet<String>> data) {
		HashMap<String, HashSet<String>> newData = new HashMap<String, HashSet<String>>();
		for (String key : data.keySet()) {
			HashSet<String> newValue = (HashSet<String>) data.get(key).clone();
			String newKey = key;
			newData.put(newKey, newValue);
		}
		return newData;
	}

	static public HashMap<String, HashMap<String, Double>> cloneHistoricalData2(
			HashMap<String, HashMap<String, Double>> data) {
		HashMap<String, HashMap<String, Double>> newData = new HashMap<String, HashMap<String, Double>>();
		for (String key : data.keySet()) {
			HashMap<String, Double> oldValue = (HashMap<String, Double>) data.get(key);
			HashMap<String, Double> newValue = new HashMap<String, Double>();
			for (String str : oldValue.keySet()) {
				Double d = (Double) oldValue.get(str);
				newValue.put(str, new Double(d.doubleValue()));
			}
			newData.put(key, newValue);
		}
		return newData;
	}

	public boolean isExceedMaxIterNumber() {
		return exceedMaxIterNumber;
	}

	public void setExceedMaxIterNumber(boolean exceedMaxIterNumber) {
		this.exceedMaxIterNumber = exceedMaxIterNumber;
	}

	public HashMap<String, HashMap<String, Double>> getHistoricalInterAmbiguities() {
		return historicalInterAmbiguities;
	}

	public void setHistoricalInterAmbiguities(HashMap<String, HashMap<String, Double>> historicalInterAmbiguities) {
		this.historicalInterAmbiguities = historicalInterAmbiguities;
	}

	public double getInvokePrecision() {
		return invokePrecision;
	}

	public void setInvokePrecision(double invokePrecision) {
		this.invokePrecision = invokePrecision;
	}

	public HashMap<String, HashSet<String>> getUnableLoadedMethods() {
		return unableLoadedMethods;
	}

	public void setUnableLoadedMethods(HashMap<String, HashSet<String>> unableLoadedMethodsNumbers) {
		this.unableLoadedMethods = unableLoadedMethodsNumbers;
	}

	public HashMap<String, HashSet<String>> getHistoricalExceedMaxInterDeptMethods() {
		return historicalExceedMaxInterDeptMethods;
	}

	public void setHistoricalExceedMaxInterDeptMethods(
			HashMap<String, HashSet<String>> historicalExceedMaxInterDepthNumbers) {
		this.historicalExceedMaxInterDeptMethods = historicalExceedMaxInterDepthNumbers;
	}

	public HashMap<String, HashSet<String>> getHistoricalExceedMaxIterMethods() {
		return historicalExceedMaxIterMethods;
	}

	public void setHistoricalExceedMaxIterMethods(HashMap<String, HashSet<String>> historicalExceedMaxIterNumbers) {
		this.historicalExceedMaxIterMethods = historicalExceedMaxIterNumbers;
	}

	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder();
		
		builder.append("invokePrecision:" + this.invokePrecision).append(";exceedMaxIterNumber:" + this.exceedMaxIterNumber)
			.append(";callStackStr:" + this.callStackStr);
		
		builder.append("\n exceedMaxInterDepthMethods[");
		for (String str : this.exceedMaxInterDepthMethods) {
			builder.append(str + ":");
		}
		builder.append("];");
		
		builder.append("\n historicalExceedMaxIterMethods[\n");
		for (String key : this.historicalExceedMaxIterMethods.keySet()) {
			builder.append("   " + key + "->");
			HashSet<String> value = this.historicalExceedMaxIterMethods.get(key);
			builder.append("      ");
			for(String str: value){
				builder.append(str + "\n");
			}
		}
		builder.append("];");
		
		builder.append("\n historicalExceedMaxInterDeptMethods[\n");
		for (String key : this.historicalExceedMaxInterDeptMethods.keySet()) {
			builder.append("   " + key + "->");
			HashSet<String> value = this.historicalExceedMaxInterDeptMethods.get(key);
			builder.append("      ");
			for(String str: value){
				builder.append(str + "\n");
			}
		}
		builder.append("];");
		
		builder.append("\n unableLoadedMethodsMethods[\n");
		for (String key : this.unableLoadedMethods.keySet()) {
			builder.append("   " + key + "->");
			HashSet<String> value = this.unableLoadedMethods.get(key);
			builder.append("      ");
			for(String str: value){
				builder.append(str + "\n");
			}
		}
		builder.append("];");
		
		builder.append("\n historicalInterAmbiguities[\n");
		for (String key : this.historicalInterAmbiguities.keySet()) {
			builder.append("   " + key + "-> \n");
			HashMap<String,Double> value = this.historicalInterAmbiguities.get(key);
			for(String str: value.keySet()){
				builder.append("      ");
				builder.append(str + " -> " + value.get(str)+ "\n");
			}
		}
		builder.append("];");
		return builder.toString();
	}

	public HashSet<String> getExceedMaxInterDepthMethods() {
		return exceedMaxInterDepthMethods;
	}

	public void setExceedMaxInterDepthMethods(HashSet<String> exceedMaxInterDepthMethods) {
		this.exceedMaxInterDepthMethods = exceedMaxInterDepthMethods;
	}

	public HashMap<Integer, String> getCallStack() {
		return callStack;
	}

	public void setCallStack(HashMap<Integer, String> callStack) {
		this.callStack = callStack;
	}

	public String getCallStackStr() {
		return callStackStr;
	}

	public void setCallStackStr(String callStackStr) {
		this.callStackStr = callStackStr;
	}

}

// end

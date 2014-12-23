/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-1-2 ����09:05:11
 * @modifier: Administrator
 * @time 2009-1-2 ����09:05:11
 * @reviewer: Administrator
 * @time 2009-1-2 ����09:05:11
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.asm.tree.analysis;

public class Call {

	private final String className;
	private final String methodName;
	private final String methodSig;

	public Call(String className, String methodName, String methodSig) {
		this.className = className;
		this.methodName = methodName;
		this.methodSig = methodSig;
	}

	public String getClassName() {
		return className;
	}

	public String getMethodName() {
		return methodName;
	}

	public String getMethodSig() {
		return methodSig;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj.getClass() != this.getClass())
			return false;
		Call other = (Call) obj;
		return this.className.equals(other.className) && this.methodName.equals(other.methodName)
				&& this.methodSig.equals(other.methodSig);
	}

	@Override
	public int hashCode() {
		return className.hashCode() + methodName.hashCode() + methodSig.hashCode();
	}

	public int getSize() {
		// TODO Auto-generated method stub
		return 1;
	}

}

// end

/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author "zhouzhiyi"
 * @time 2009-12-30
 * @modifier: "zhouzhiyi"
 * @time 2009-12-30
 * @reviewer: "zhouzhiyi"
 * @time 2009-12-30
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.callgraph;

/**
 * Enumeration type representing the invoke type of a call graph edge.
 * 
 * @see soot.Kind
 * @author "zhouzhiyi"
 */
public final class InvokeType {

	public static final InvokeType INVALID = new InvokeType("INVALID");
	/** Due to explicit invokestatic instruction. */
	public static final InvokeType STATIC = new InvokeType("InvokeStatic");
	/** Due to explicit invokevirtual instruction. */
	public static final InvokeType VIRTUAL = new InvokeType("InvokeVirtual");
	/** Due to explicit invokeinterface instruction. */
	public static final InvokeType INTERFACE = new InvokeType("InvokeInterface");
	/** Due to explicit invokespecial instruction. */
	public static final InvokeType SPECIAL = new InvokeType("InvokeSpecial");
	/** Implicit call to static initializer. */
	public static final InvokeType CLINIT = new InvokeType("CLINIT");
	/** Implicit call to Thread.run() due to Thread.start() call. */
	public static final InvokeType THREAD = new InvokeType("THREAD");
	/** Implicit call to java.lang.ref.Finalizer.register from new bytecode. */
	public static final InvokeType FINALIZE = new InvokeType("FINALIZE");
	/**
	 * Implicit call to finalize() from
	 * java.lang.ref.Finalizer.invokeFinalizeMethod().
	 */
	public static final InvokeType INVOKE_FINALIZE = new InvokeType("INVOKE_FINALIZE");
	/** Implicit call to run() through AccessController.doPrivileged(). */
	public static final InvokeType PRIVILEGED = new InvokeType("PRIVILEGED");
	/** Implicit call to constructor from java.lang.Class.newInstance(). */
	public static final InvokeType NEWINSTANCE = new InvokeType("NEWINSTANCE");
	final String name;
	int num;//used in hashcode of Edge which is included in this type.it is used to divide different Edge 
						//the same Method
	
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public InvokeType(String name) {
		this.name = name;
	}

	public String name() {
		return name;
	}
	public String toString(){
		return name;
	}
	public boolean passesParameters() {
		return isExplicit() || this == THREAD || this == FINALIZE ||
		this == PRIVILEGED || this == NEWINSTANCE || this == INVOKE_FINALIZE;
    }
	/** Returns true if the call is due to an explicit invoke statement. */
	public boolean isExplicit() {
		return isInstance() || isStatic();
	}

	/**
	 * Returns true if the call is due to an explicit instance invoke statement.
	 */
	public boolean isInstance() {
		return this == VIRTUAL || this == INTERFACE || this == SPECIAL;
	}

	/** Returns true if the call is to static initializer. */
	public boolean isClinit() {
		return this == CLINIT;
	}

	/**
	 * Returns true if the call is due to an explicit static invoke statement.
	 */
	public boolean isStatic() {
		return this == STATIC;
	}
}

// end

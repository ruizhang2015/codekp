/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author root
 * @time 2009-12-25 下午02:54:45
 * @modifier: root
 * @time 2009-12-25 下午02:54:45
 * @reviewer: root
 * @time 2009-12-25 下午02:54:45
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.classfile;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MethodNodeSumary {
	public HashSet<LocalVariableEscapeNode> escapeNodes;

	public HashSet<LocalVariableEscapeNode> returnNodes;

	public HashMap<RealObject, InvokeMessage> objectInvokeMessages;

	public class InvokeMessage {
		public int opcode;
		public int invokePlace;
		public String methodName;
		public String methodDesc;
		public RealObject caller;
		public List<RealObject> parameters;
	}

	public MethodNodeSumary() {
		escapeNodes = new HashSet<LocalVariableEscapeNode>();
		returnNodes = new HashSet<LocalVariableEscapeNode>();
		objectInvokeMessages = new HashMap<RealObject, InvokeMessage>();
	}

	public void copy(MethodNodeSumary sumary) {
		escapeNodes.addAll(sumary.escapeNodes);
		returnNodes.addAll(sumary.returnNodes);
		objectInvokeMessages.putAll(sumary.objectInvokeMessages);
	}
}

// end

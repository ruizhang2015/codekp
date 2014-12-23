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

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;

import edu.pku.cn.classfile.MethodNode;

/**
 * Represents a single edge in a call graph.
 * 
 * @author "zhouzhiyi"
 */
public final class CallGraphEdge {
	MethodNode caller;
	int invokePC;
	MethodNode callee;
	HashSet<MethodNode> calleeSet;
	public boolean contains(Object o) {
		if(callee!=null)
			return callee.equals(o);
		else
			return calleeSet.contains(o);
	}
	public int size() {
		if(callee!=null)
			return 1;
		else
			return calleeSet.size();
	}
	public int getCalleeSize(){
		if(callee!=null)
			return 1;
		else
			return calleeSet.size();
	}
	public List<MethodNode> getCallees(){
		List<MethodNode> callees=new LinkedList<MethodNode>();
		if(callee!=null)
			callees.add(callee);
		else
			callees.addAll(calleeSet);
		return callees;
	}
	InvokeType type;
	
	public String toString(){
		StringBuilder bu=new StringBuilder();
		bu.append(caller.name).append(caller.desc).append("{").append(caller.declaringClass.name).append("}-->").append(invokePC);
		if(callee!=null){
			bu.append("\n\t\t\t").append(callee.name).append(callee.desc);
			bu.append("{").append(callee.declaringClass.name).append("}");
		}
		else{
			Iterator<MethodNode> iter=calleeSet.iterator();
			while(iter.hasNext()){
				MethodNode mn=iter.next();
				bu.append("\n\t\t\t").append(mn.name).append(mn.desc);
				bu.append("{").append(mn.declaringClass.name).append("}");
			}
		}		
		return bu.toString();
	}
	public CallGraphEdge(MethodNode caller, int invokePC, MethodNode callee, InvokeType type) {
		this.caller = caller;
		this.invokePC = invokePC;
		this.callee = callee;
		this.type = type;
		calleeSet=null;
	}

	public CallGraphEdge(MethodNode caller, int invokePC, MethodNode callee) {
		this.caller = caller;
		this.invokePC = invokePC;
		this.callee = callee;
		type=InsnToInvokeType(caller.instructions.get(invokePC));
		calleeSet=null;
	}
	public Object clone(){
//		try {
//			super.clone();
//			return new CallGraphEdge(caller, invokePC, callee,type);
//		} catch (CloneNotSupportedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		CallGraphEdge cge =new CallGraphEdge(caller, invokePC, callee,type);
		if(callee==null){
			cge.calleeSet=new HashSet<MethodNode>();
			cge.calleeSet.addAll(calleeSet);
		}
		cge.prev=prev;
		cge.next=next;
		return cge;
	}
	public void addCallee(MethodNode mn){
		if(calleeSet==null){
			calleeSet=new HashSet<MethodNode>();
			calleeSet.add(callee);
			callee=null;
		}
		calleeSet.add(mn);
	}
	public void removeCallee(MethodNode mn){
		if(callee!=null)
			callee=null;
		else if(calleeSet!=null){
			calleeSet.remove(mn);
			if(calleeSet.size()==1){
				callee=calleeSet.iterator().next();
				calleeSet=null;
			}else
			if(calleeSet.size()<=0)
				calleeSet=null;
		}
	}
	public boolean isValid(){
		return !(callee==null && calleeSet==null);
	}
	public MethodNode getCaller() {
		return caller;
	}
	public int getInvokePC() {
		return invokePC;
	}
	public MethodNode getCallee() {
		return callee;
	}
	public InvokeType getType() {
		return type;
	}
	public CallGraphEdge getNext() {
		return next;
	}
	public CallGraphEdge getPrev() {
		return prev;
	}

	CallGraphEdge next,prev;
	public void insertBefore(CallGraphEdge other){
		other.prev=this;
		next=other;
	}
	public void insertAfter(CallGraphEdge other){
        other.next=this;
        prev=other;
	}
	public void remove(){
		next.prev=prev;
		prev.next=next;
	}
	@Override
	public int hashCode() {		
		if(callee!=null)
			return callee.hashCode()+type.getNum();
		else
			return calleeSet.hashCode()+type.getNum();
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CallGraphEdge other = (CallGraphEdge) obj;
		if (callee == null) {
			if (other.callee != null)
				return false;
		} else if (!callee.equals(other.callee))			
			return false;
		if (calleeSet == null) {
			if (other.calleeSet != null)
				return false;
		} else if (!calleeSet.containsAll(other.calleeSet))
			return false;
		if (caller == null) {
			if (other.caller != null)
				return false;
		} else if (!caller.equals(other.caller))
			return false;
		if (invokePC != other.invokePC)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	public static InvokeType InsnToInvokeType(AbstractInsnNode insn) {
		switch (insn.getOpcode()) {
		case Opcodes.INVOKEINTERFACE:
			return InvokeType.INTERFACE;
		case Opcodes.INVOKESPECIAL:
			return InvokeType.SPECIAL;
		case Opcodes.INVOKESTATIC:
			return InvokeType.STATIC;
		case Opcodes.INVOKEVIRTUAL:
			return InvokeType.VIRTUAL;
		default:
			throw new RuntimeException("wrong opcode to InvokeType");
		}
	}

	public boolean isClinit() {
		return type.isClinit();
	}

	public boolean isExplicit() {
		return type.isExplicit();
	}

	public boolean isInstance() {
		return type.isInstance();
	}

	public boolean isStatic() {
		return type.isStatic();
	}

	public boolean passesParameters() {
		return type.passesParameters();
	}
	
}

// end

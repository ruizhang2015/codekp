/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-20
 * @modifier: a
 * @time 2010-1-20
 * @reviewer: a
 * @time 2010-1-20
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.jir;
/**
 * @author a
 */
public class JIRStack {
	private JIRValue[] stack;
	private int top=-1;
	public boolean empty(){
		return top==-1;
	}
	public JIRStack(int size){
		stack=new JIRValue[size];
	}
	public void push(JIRValue v){
		stack[++top]=v;
	}
	public void push(int index,JIRValue v){
//		if(index!=top+1)
//			System.err.println("error in stack :"+index +" "+top);
		top=index>top?index:top;
		stack[index]=v;
	}
	public JIRValue at(int index){
		return stack[index];
	}
	public JIRValue top(){
		return stack[top];
	}
	public JIRValue pop(){
		if (top > 0) {
			return stack[top--];
		} else 
			return stack[top];		
	}
	public int size(){
		return top;
	}
	public String toString(){
		StringBuilder builder=new StringBuilder();
		for(int i=0;i<=top;i++)
			builder.append(stack[i]).append(",");
		return builder.toString();
	}
}

// end

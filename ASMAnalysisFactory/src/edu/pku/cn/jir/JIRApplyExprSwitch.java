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

import java.util.Stack;

/**
 * @author a
 */
public class JIRApplyExprSwitch extends AbstractExprSwitch {

	public JIRStack stack;
	public JIRApplyExprSwitch(JIRStack stack){
		this.stack=stack;
	}
	private JIRValue getOP(JIRValue op){
		if(op instanceof StackRef)
			op=stack.pop();
		return op;
	}
	public void caseBinop(BinopExpr b){
		b.op2=getOP(b.op2);
		b.op1=getOP(b.op1);
	}
	public void caseCast(CastExpr c){
		c.value=getOP(c.value);
	}
	public void caseInstanceOf(InstanceOfExpr i){
		i.value=getOP(i.value);
	}
	public void caseInvoke(InvokeExpr ie){
		for(int i=ie.params.size()-1;i>=0;i--)
			ie.params.set(i, getOP(ie.params.get(i)));
		if(!ie.isStatic())
			ie.invoker=getOP(ie.invoker);
	}
	public void caseLengthOf(LengthOfExpr l){
		l.value=getOP(l.value);
	}
	public void caseNeg(NegExpr n){
		n.value=getOP(n.value);
	}
	public void caseNewArray(NewArrayExpr n){
		for(int i=n.sizes.length-1;i>=0;i--)
			n.sizes[i]=getOP(n.sizes[i]);
	}
	public void caseNew(NewExpr n){
		
	}
}

// end

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
public abstract class AbstractExprSwitch implements Switch {
	public void caseBinop(BinopExpr b){defaultCase();}
	public void caseCast(CastExpr c){defaultCase();}
	public void caseInstanceOf(InstanceOfExpr i){defaultCase();}
	public void caseInvoke(InvokeExpr i){defaultCase();}
	public void caseLengthOf(LengthOfExpr l){defaultCase();}
	public void caseNeg(NegExpr n){defaultCase();}
	public void caseNewArray(NewArrayExpr n){defaultCase();}
	public void caseNew(NewExpr n){defaultCase();}
	@Override
	public void defaultCase() {
		// TODO Auto-generated method stub

	}
	public void caseEnd(){}
	public <T> T getResult(){return null;}
	@Override
	public <T > void setResult(T result){
		
	}
}

// end

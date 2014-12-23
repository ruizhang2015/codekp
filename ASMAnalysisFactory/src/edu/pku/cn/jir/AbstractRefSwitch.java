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
public abstract class AbstractRefSwitch implements Switch {
	public void caseArray(ArrayRef a){defaultCase();}
	public void caseField(FieldRef f){defaultCase();}
	public void caseLocal(LocalRef l){defaultCase();}
	public void caseStack(StackRef s){defaultCase();}
	public void caseTemp(TempRef t){defaultCase();}
	public void caseThis(This t){defaultCase();}
	public void caseCaughtException(CaughtExceptionRef c){defaultCase();}
	@Override
	public void defaultCase() {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> T getResult() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> void setResult(T result) {
		// TODO Auto-generated method stub

	}

}

// end

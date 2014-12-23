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
public abstract class AbstractConstantSwitch implements Switch {
	public void caseDouble(DoubleConstant d){defaultCase();}
	public void caseFloat(FloatConstant f){defaultCase();}
	public void caseInt(IntConstant i){defaultCase();}
	public void caseLong(LongConstant l){defaultCase();}
	public void caseNull(Null n){defaultCase();}
	public void caseString(StringConstant s){defaultCase();}
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

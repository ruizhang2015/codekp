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
public interface Switch {
//	public void caseConstant(Constant con);
//	public void caseExpr(Expr ex);
//	public void caseAnyNewExpr(AnyNewExpr ane);
//	public void caseRef(Ref ref);
//	public void caseUnopExpr(UnopExpr nop);
	public void defaultCase();
	public void caseEnd();
	public <T> T getResult();
	public <T> void setResult(T result);
}

// end

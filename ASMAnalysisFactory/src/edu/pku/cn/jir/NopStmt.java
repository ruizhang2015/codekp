/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-18
 * @modifier: a
 * @time 2010-1-18
 * @reviewer: a
 * @time 2010-1-18
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
public abstract class NopStmt extends AbstractStmt {
}
class PopStmt extends NopStmt {
	public int size;
	public PopStmt(int size){
		this.size=size;
	}
	@Override
	public void apply(Switch sw) {
		// TODO Auto-generated method stub
		((AbstractStmtSwitch)sw).casePop(this);
	}
	@Override
	public void accept(JIRVisitor jv) {
		// TODO Auto-generated method stub
		//jv.visit(this);
	}
	@Override
	public int getSort() {
		// TODO Auto-generated method stub
		return POPSTMT;
	}

}
// end

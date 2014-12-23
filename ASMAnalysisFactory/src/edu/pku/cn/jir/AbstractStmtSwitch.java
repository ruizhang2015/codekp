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
public class AbstractStmtSwitch implements Switch {

	public void caseAssign(AssignStmt a){defaultCase();}
	public void caseEnterMonitor(EnterMonitorStmt a){defaultCase();}
	public void caseExitMonitor(ExitMonitorStmt a){defaultCase();}
	public void caseGoto(GotoStmt a){defaultCase();}
	public void caseIf(IfStmt a){defaultCase();}
	public void caseInvoke(InvokeStmt a){defaultCase();}
	public void caseLookupSwitch(LookupSwitchStmt a){defaultCase();}
	public void caseLabel(LabelStmt a){defaultCase();}
	public void caseLine(LineStmt a){defaultCase();}
	public void caseRet(RetStmt a){defaultCase();}
	public void caseReturn(ReturnStmt a){defaultCase();}
	public void caseTableSwitch(TableSwitchStmt a){defaultCase();}
	public void caseThrow(ThrowStmt a){defaultCase();}
	protected void casePop(PopStmt a){defaultCase();}
	@Override
	public void defaultCase() {
		// TODO Auto-generated method stub

	}
	public void caseEnd(){
		
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

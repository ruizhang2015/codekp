/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-3-11
 * @modifier: a
 * @time 2010-3-11
 * @reviewer: a
 * @time 2010-3-11
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.ptg;

import org.objectweb.asm.Type;

import edu.pku.cn.jir.AbstractStmtSwitch;
import edu.pku.cn.jir.AssignStmt;
import edu.pku.cn.jir.EnterMonitorStmt;
import edu.pku.cn.jir.ExitMonitorStmt;
import edu.pku.cn.jir.GotoStmt;
import edu.pku.cn.jir.IfStmt;
import edu.pku.cn.jir.InvokeStmt;
import edu.pku.cn.jir.LabelStmt;
import edu.pku.cn.jir.LineStmt;
import edu.pku.cn.jir.LookupSwitchStmt;
import edu.pku.cn.jir.RetStmt;
import edu.pku.cn.jir.ReturnStmt;
import edu.pku.cn.jir.TableSwitchStmt;
import edu.pku.cn.jir.ThrowStmt;

/**
 * @author a
 */
public class PTGApplyStmtSwitch extends AbstractStmtSwitch {
	public void caseAssign(AssignStmt a){
		Type type=a.left.getType();
		if(type.getSort()==Type.OBJECT){
			
		}
	}
	public void caseInvoke(InvokeStmt a){defaultCase();}
	public void caseLabel(LabelStmt a){defaultCase();}
	public void caseLine(LineStmt a){defaultCase();}
	public void caseReturn(ReturnStmt a){defaultCase();}
	public void caseThrow(ThrowStmt a){defaultCase();}

}

// end

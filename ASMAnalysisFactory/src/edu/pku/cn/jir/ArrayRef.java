/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-11 обнГ03:00:30
 * @modifier: a
 * @time 2010-1-11 обнГ03:00:30
 * @reviewer: a
 * @time 2010-1-11 обнГ03:00:30
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.jir;

import org.objectweb.asm.Type;

/**
 * @author zhouzhiyi
 */
public class ArrayRef implements Ref {
	public static final ArrayRef e=new ArrayRef(null, null);
	public JIRValue base;
	public JIRValue index;
	public ArrayRef(JIRValue base,JIRValue index){
		this.base=base;
		this.index=index;
	}
	public int getSort(){
		return ARRAYREF;
	}
	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return base.getType();
	}
	@Override
	public void apply(Switch sw) {
		// TODO Auto-generated method stub
		((AbstractRefSwitch)sw).caseArray(this);
	}
	public Object clone(){
		return new ArrayRef(JIR.cloneIfNeed(base),JIR.cloneIfNeed(index));
	}
	@Override
	public String toString() {
		if(base==null)
			return "Array";
		return base.toString() + "[" + index.toString() + "]";
	}
}

// end

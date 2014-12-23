/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-1-12 обнГ03:27:45
 * @modifier: a
 * @time 2010-1-12 обнГ03:27:45
 * @reviewer: a
 * @time 2010-1-12 обнГ03:27:45
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
public class TempRef implements Ref {
	public TempRef(int index, Type type) {
		super();
		this.index = index;
		this.type = type;
	}
	public int getSort(){
		return TEMPREF;
	}
	public static final String TEMP="temp$";
	public int index;
	public Type type;
	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return type;
	}
	@Override
	public void apply(Switch sw) {
		// TODO Auto-generated method stub
		((AbstractRefSwitch)sw).caseTemp(this);
	}
	public Object clone(){
		return this;
	}
	public String getName(){
		return TEMP+index;
	}
	@Override
	public String toString() {
		return getName();
	}
	@Override
	public int hashCode() {
		return type.hashCode()+index;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TempRef other = (TempRef) obj;
		if (index != other.index)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}

// end

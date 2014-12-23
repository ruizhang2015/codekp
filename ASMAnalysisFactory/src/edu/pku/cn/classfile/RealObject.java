/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author root
 * @time 2009-12-7 下午01:52:11
 * @modifier: root
 * @time 2009-12-7 下午01:52:11
 * @reviewer: root
 * @time 2009-12-7 下午01:52:11
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.classfile;

import org.objectweb.asm.Type;

public interface RealObject {
	/**
	 * @return Returns the name.
	 */
	public String getName();

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name);

	/**
	 * @return Returns the owner.
	 */
	public String getOwner();

	/**
	 * @param owner
	 *            The owner to set.
	 */
	public void setOwner(String owner);

	/**
	 * @return Returns the type.
	 */
	public Type getType();

	/**
	 * @param type
	 *            The type to set.
	 */
	public void setType(Type type);

	public boolean isFieldObject();
}

// end

/***
 * ASM: a very small and fast Java bytecode manipulation framework
 * Copyright (c) 2000-2007 INRIA, France Telecom
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holders nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package edu.pku.cn.classfile;

import java.util.HashSet;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.LabelNode;

/**
 * A node that represents a local variable declaration.
 * 
 * @author Eric Bruneton
 */
public class LocalVariableNode extends org.objectweb.asm.tree.LocalVariableNode implements RealObject {

	// public List<Region> region = new ArrayList<Region>();
	public int allIndex;
	/**
	 * The create position in insnlist
	 */
	public int createLocation;
	/**
	 * The pointTo reference. null means this object is point to a real object
	 * otherwise it is a alias this object directly point to
	 */
	public RealObject pointTo;
	/**
	 * the reference set
	 */
	public HashSet<RealObject> reference;

	/**
	 * Constructs a new {@link LocalVariableNode}.
	 * 
	 * @param name
	 *            the name of a local variable.
	 * @param desc
	 *            the type descriptor of this local variable.
	 * @param signature
	 *            the signature of this local variable. May be <tt>null</tt>.
	 * @param start
	 *            the first instruction corresponding to the scope of this local
	 *            variable (inclusive).
	 * @param end
	 *            the last instruction corresponding to the scope of this local
	 *            variable (exclusive).
	 * @param index
	 *            the local variable's index.
	 */
	public LocalVariableNode(final String name, final String desc, final String signature, final LabelNode start,
			final LabelNode end, final int index) {
		super(name, desc, signature, start, end, index);
		// region.add(new Region(start.index, end.index, index));
	}

	public LocalVariableNode(org.objectweb.asm.tree.LocalVariableNode superNode) {
		super(superNode.name, superNode.desc, superNode.signature, superNode.start, superNode.end, superNode.index);
	}

	// public void addRegion(int start,int end,int index){
	// region.add(new Region(start,end, index));
	// }

	public boolean equals(int start, int index) {
		if (this.start.index <= start && start <= end.index && index == this.index)
			return true;
		return false;
	}

	public boolean equals(Object obj) {
		if (obj != null && obj instanceof LocalVariableNode) {
			LocalVariableNode node = (LocalVariableNode) obj;
			if (signature != null)
				return name.equals(node.name) && desc.equals(node.desc) && signature.equals(node.signature)
						&& start.index == node.start.index && end.index == node.end.index && index == node.index;
			else
				return name.equals(node.name) && desc.equals(node.desc) && start.index == node.start.index
						&& end.index == node.end.index && index == node.index;
		}
		return false;
	}

	/**
	 * @see edu.pku.cn.classfile.RealObject#getName()
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	/**
	 * @see edu.pku.cn.classfile.RealObject#getOwner()
	 */
	@Override
	public String getOwner() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see edu.pku.cn.classfile.RealObject#getType()
	 */
	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return Type.getType(desc);
	}

	/**
	 * @see edu.pku.cn.classfile.RealObject#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		this.name = name;
	}

	/**
	 * @see edu.pku.cn.classfile.RealObject#setOwner(java.lang.String)
	 */
	@Override
	public void setOwner(String owner) {
		// TODO Auto-generated method stub

	}

	public boolean isFieldObject() {
		return false;
	}

	/**
	 * @see edu.pku.cn.classfile.RealObject#setType(org.objectweb.asm.Type)
	 */
	@Override
	public void setType(Type type) {
		// TODO Auto-generated method stub
		desc = type.getDescriptor();
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(index).append(" ");
		builder.append(name).append(" ");
		builder.append(desc).append(" ");
		// builder.append(allIndex).append(" ");
		// builder.append(region).append("\n");
		builder.append(start.index).append(" ");
		builder.append(end.index).append("\n");
		return builder.toString();
	}
}

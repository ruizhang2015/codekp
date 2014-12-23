package edu.pku.cn.classfile;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import edu.pku.cn.util.OpcodeUtil;

/**
 * A node that represents a field.
 * 
 * @author Eric Bruneton
 */
public class FieldNode extends org.objectweb.asm.tree.FieldNode implements RealObject {
    public ClassNode decalringClass;
	/**
	 * Constructs a new {@link FieldNode}.
	 * 
	 * @param access
	 *            the field's access flags (see
	 *            {@link org.objectweb.asm.Opcodes}). This parameter also
	 *            indicates if the field is synthetic and/or deprecated.
	 * @param name
	 *            the field's name.
	 * @param desc
	 *            the field's descriptor (see {@link org.objectweb.asm.Type
	 *            Type}).
	 * @param signature
	 *            the field's signature.
	 * @param value
	 *            the field's initial value. This parameter, which may be
	 *            <tt>null</tt> if the field does not have an initial value,
	 *            must be an {@link Integer}, a {@link Float}, a {@link Long}, a
	 *            {@link Double} or a {@link String}.
	 */
	public FieldNode(final int access, final String name, final String desc, final String signature, final Object value) {
		super(access, name, desc, signature, value);
	}
    public boolean isStatic(){
    	return (access&Opcodes.ACC_STATIC)>0;
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

	public Object getValue() {
		// TODO Auto-generated method stub
		return value;
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

	/**
	 * @see edu.pku.cn.classfile.RealObject#setType(org.objectweb.asm.Type)
	 */
	@Override
	public void setType(Type type) {
		// TODO Auto-generated method stub
		desc = type.getDescriptor();
	}

	public void setValue(Object value) {
		// TODO Auto-generated method stub
		this.value = value;
	}

	public boolean isFieldObject() {
		return true;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(OpcodeUtil.printFieldAccess(access)).append(desc).append(" ").append(name).append("\n");
		return builder.toString();
	}
}

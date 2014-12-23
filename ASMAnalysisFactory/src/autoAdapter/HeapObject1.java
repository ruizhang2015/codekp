/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-4-22 ����03:49:11
 * @modifier: Administrator
 * @time 2009-4-22 ����03:49:11
 * @reviewer: Administrator
 * @time 2009-4-22 ����03:49:11
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package autoAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.Value;


public class HeapObject1 implements Value{

	
	//new �����λ��(���󱻴����ĵص�),return value����صط�
	//��ʽ "��  ����  ָ����"
	public ProgramLocation createLocation;
	//�ڶ��ٴδ���
	public int createNth; 
	//heap object������
	public String classType;
	//������Ϣ���������
	public String desc;
	//heap object����
	//name -> ho
	public HashMap<String,HeapObject1> referenceFields=new HashMap<String,HeapObject1>();
	public Type type;//asm type : used for Value
	
	public static final String DescParam = "parameter:";
	public static final String DescNew = "new";
	public static final String DescReturn = "return";
	public static final String DescBuiltin = "builtin";
	public static final String DescThis = "this";
	public static final String DescInstanceField = "field:";
	public static final String DescStaticField = "static:";
	public static final String DescClass = "class:";
	
	
	public static HeapObject1 Basic=new HeapObject1(ProgramLocation.BuiltinPL,0,"basic",DescBuiltin,Type.INT_TYPE);
	public static HeapObject1 BasicNull=new HeapObject1(ProgramLocation.BuiltinPL,0,"null",DescBuiltin,Type.INT_TYPE);
	public static HeapObject1 BasicBoolean=new HeapObject1(ProgramLocation.BuiltinPL,0,"java.lang.Boolean",DescBuiltin,Type.BOOLEAN_TYPE);
	public static HeapObject1 BasicFloat=new HeapObject1(ProgramLocation.BuiltinPL,0,"java.lang.Float",DescBuiltin,Type.FLOAT_TYPE);
	public static HeapObject1 BasicDouble=new HeapObject1(ProgramLocation.BuiltinPL,0,"java.lang.Double",DescBuiltin,Type.DOUBLE_TYPE);
	public static HeapObject1 BasicByte=new HeapObject1(ProgramLocation.BuiltinPL,0,"java.lang.Byte",DescBuiltin,Type.BYTE_TYPE);
	public static HeapObject1 BasicShort=new HeapObject1(ProgramLocation.BuiltinPL,0,"java.lang.Short",DescBuiltin,Type.SHORT_TYPE);
	public static HeapObject1 BasicInt=new HeapObject1(ProgramLocation.BuiltinPL,0,"java.lang.Integer",DescBuiltin,Type.INT_TYPE);
	public static HeapObject1 BasicLong=new HeapObject1(ProgramLocation.BuiltinPL,0,"java.lang.Long",DescBuiltin,Type.LONG_TYPE);
	public static HeapObject1 BasicChar=new HeapObject1(ProgramLocation.BuiltinPL,0,"java.lang.Character",DescBuiltin,Type.CHAR_TYPE);
	public static HeapObject1 BasicVoid=new HeapObject1(ProgramLocation.BuiltinPL,0,"java.lang.Void",DescBuiltin,Type.VOID_TYPE);
	public static HeapObject1 BasicDontcare=new HeapObject1(ProgramLocation.BuiltinPL,0,"dontcare",DescBuiltin,Type.INT_TYPE);
	public static HeapObject1 BasicFieldDefault=new HeapObject1(ProgramLocation.BuiltinPL,0,"fieldDefault",DescBuiltin,Type.INT_TYPE);
	
	
	/*
	 * @createLocation
	 * @createNth
	 */
	public HeapObject1(ProgramLocation createLocation, int createNth,
			String classType, String desc,Type type) {
		super();
		this.createLocation = createLocation;
		this.createNth = createNth;
		this.classType = classType;
		this.desc = desc;
		this.type = type;
	}

	public HeapObject1(ProgramLocation createPosition, int createNth, String classType,String desc) {
		super();
		this.createLocation = createPosition;
		this.createNth = createNth;
		this.classType = classType;
		this.type=Type.INT_TYPE;	
		this.desc=desc;
	}
	
//	public HeapObject(ProgramLocation createPosition, int createNth, String classType) {
//		super();
//		this.createLocation = createPosition;
//		this.createNth = createNth;
//		this.classType = classType;
//		this.type=Type.INT_TYPE;
//		
//	}

	public HeapObject1() {
		// TODO Auto-generated constructor stub
	}

	
	public HeapObject1(HeapObject1 fact) {
		// TODO Auto-generated constructor stub
		this.classType=fact.classType;
		this.createNth=fact.createNth;
		this.createLocation=fact.createLocation;
		this.desc=fact.desc;
		for(String key: fact.referenceFields.keySet())
		{
			referenceFields.put(key, fact.referenceFields.get(key));
		}	
		
	}

//	
//	public HeapObject createParameter(String className,String methodDesc,int nth,String paraClass)
//	{
//		HeapObject p= new HeapObject(new ProgramLocation(className,methodDesc,0),
//				0,paraClass,HeapObject.DescParam+nth,);
//		
//		return p;
//	}
	
	public String toString()
	{
		String res = this.hashCode()+":type="+classType+" Desc="+this.desc+" \n\t����λ��="+createLocation+" ��"+createNth+"  �δ��� \n";	
		return res;
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return type == Type.LONG_TYPE || type == Type.DOUBLE_TYPE ? 2 : 1;
	}
	
}



// end

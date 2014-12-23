/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-5-3 ����09:01:30
 * @modifier: Administrator
 * @time 2009-5-3 ����09:01:30
 * @reviewer: Administrator
 * @time 2009-5-3 ����09:01:30
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package autoAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.objectweb.asm.tree.MethodNode;

public class MethodSummary {

	public MethodNode methodNode;
	public HashMap<HeapObject, ArrayList<ExecuteMethod>> ho2invokes = new HashMap<HeapObject, ArrayList<ExecuteMethod>>();

	public ArrayList<HeapObject> parameters = new ArrayList<HeapObject>();

	public HeapObject thisObject;

	public HashSet<HeapObject> newObjects = new HashSet<HeapObject>();

	public HashSet<HeapObject> returnedObjects = new HashSet<HeapObject>();

	public HashSet<HeapObject> escapeObjects = new HashSet<HeapObject>();

	public HashSet<HeapObject> innerObjects = new HashSet<HeapObject>();

	public ArrayList<ExecuteMethod> allExecuteMethods = new ArrayList<ExecuteMethod>();

	// �Ƿ��Ѿ������̵ĵ����
	public boolean propagated = false;
	public String key;

	public MethodSummary(MethodNode methodNode) {
		super();
		this.methodNode = methodNode;

	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String res = "����" + key + "��ժҪ��\n";
		for (ExecuteMethod em : allExecuteMethods) {
			res += em + "\n";
		}
		return res;
	}

}

// end

/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-4-26 ����02:36:47
 * @modifier: Administrator
 * @time 2009-4-26 ����02:36:47
 * @reviewer: Administrator
 * @time 2009-4-26 ����02:36:47
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package autoAdapter;

import java.util.ArrayList;

public class ExecuteMethod {
	// ִ�еص�
	// ��ʽ "��  ����  ָ����"
	public ProgramLocation executePosition;
	public ArrayList<HeapObject> parameters = new ArrayList<HeapObject>();
	public boolean isStatic = false;
	public String methodOwner = "";
	public String methodName = "";
	public String methodDesc = "";

	public String toString() {
		String res = "";
		res += "���÷�����" + methodOwner + "." + methodName + methodDesc + "\n\tִ�еص㣺" + executePosition + "\n\t��̬="
				+ isStatic + " ����\n{\n";
		for (int i = 0; i < parameters.size(); i++) {
			HeapObject ho = parameters.get(i);
			// res+="\t����="+ho.classType+" ����λ��="+ho.createLocation+" ��"+ho.createNth+"�δ��� \n";
			res += "����" + i + ":\t" + ho;
		}
		res += "\n}\n";
		return res;
	}
}

// end

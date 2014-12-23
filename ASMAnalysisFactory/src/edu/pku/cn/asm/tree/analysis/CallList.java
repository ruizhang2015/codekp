/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-1-10 ����10:08:42
 * @modifier: Administrator
 * @time 2009-1-10 ����10:08:42
 * @reviewer: Administrator
 * @time 2009-1-10 ����10:08:42
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.asm.tree.analysis;

import java.util.ArrayList;
import java.util.Iterator;

public class CallList {

	private ArrayList<Call> callList;

	public CallList() {
		this.callList = new ArrayList<Call>();
	}

	public CallList(CallList obj) {
		this();
		this.callList.addAll(obj.callList);
	}

	public Iterator<Call> callIterator() {
		return callList.iterator();
	}

	public void add(Call call) {
		callList.add(call);
	}

	public int size() {
		return callList.size();
	}

	public Call get(int index) {
		return callList.get(index);
	}

	public CallList merge(CallList pred) {// merge current CallList with pred
		// CallList
		// different from FindBugs, which uses a simple algorithm to get the
		// common serial, we use LCS algorithm
		// actually, the LCS is not enough, a better algorithm is to get all
		// mutex LCS.
		CallList result = new CallList();
		int m = this.size();
		int n = pred.size();
		int[][] C = new int[m][n];
		String[][] B = new String[m][n];
		int i, j;
		for (i = 0; i < m; i++) {
			if (this.get(i) != null && this.get(i).equals(pred.get(0)))
				C[i][0] = 1; // if the first Call in pred equals to one Call in
			// this, assign 1
			else
				C[i][0] = 0;
		}
		for (i = 1; i < n; i++) {
			if (this.get(0) != null && this.get(0).equals(pred.get(i)))
				C[0][i] = 1;
			else
				C[0][i] = 0;
		}
		for (i = 1; i < m; i++) {
			for (j = 1; j < n; j++) {
				if (this.get(i) != null && this.get(i).equals(pred.get(j))) {
					C[i][j] = C[i - 1][j - 1] + 1;
					B[i][j] = "diagonal";
				} else if (C[i - 1][j] >= C[i][j - 1]) {
					C[i][j] = C[i - 1][j];
					B[i][j] = "up";
				} else {
					C[i][j] = C[i][j - 1];
					B[i][j] = "left";
				}
			}
		}
		ArrayList<Call> temp = new ArrayList<Call>();
		i = m - 1;
		j = n - 1;
		int len = C[i][j];
		while (len > 0) {
			if (B[i][j] == null) {
				temp.add(this.get(i));
				len--;
			} else if (B[i][j].equals("diagonal")) {
				temp.add(this.get(i));
				i--;
				j--;
				len--;
			} else if (B[i][j].equals("up")) {
				i--;
			} else {
				j--;
			}
		}
		while (temp.size() > 0) {// reverse the sequence to get the right
			// sequence
			result.add(temp.remove(temp.size() - 1));
		}
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj.getClass() != this.getClass())
			return false;
		CallList other = (CallList) obj;
		return this.callList.equals(other.callList);
	}

	@Override
	public int hashCode() {
		return callList.hashCode();
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		for (Call call : callList) {
			if (buf.length() > 0)
				buf.append(',');
			if (call != null){
				buf.append(call.getMethodName());
			}
			
		}
		return buf.toString();
	}
}

// end

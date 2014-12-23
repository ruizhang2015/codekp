package edu.pku.cn.testcase;

import java.util.HashMap;
import java.util.LinkedList;

public class TestAnalysisLiveVariable {

	public void test1() {
		String ss;
		String ss1;
		String ss2 = null;
		int m = 39;
		if (m > 0) {
			ss1 = new String("Hi!");
		} else {
			ss2 = new String("Oh!");
		}
		System.out.println(ss2);
	}

	public void difficult() {
		boolean debug = false;
		HashMap map = new HashMap();
		int returnValue = map.size();
		if (debug)
			System.out.println("The size of the map is" + returnValue);
	}

	public void simple() {
		int a = -2;
		int b;
		b = a;
		if (a > 0) {
			b = a - 2;
		} else {
			b = a + 2;
		}

	}

	public static void main(String[] args) {
		int m = -2;
		int n = 3;
		int u1 = 1;

		int i = m - 1;
		int j = n;
		int a = u1;
		int x1;
		int x2;
		{
			x1 = 2;
			x1 = 3;
			int u2 = 0;
			int u3 = -1;
			do {

				i = i + 1;
				j = j - 1;
				if (a > i) {
					a = u2;
				} else {
					i = u3;
					break;
				}
			} while (i < j);
		}
		String ss = null;
		while (i != 0) {
			System.out.println(ss.toString());
		}
	}
}
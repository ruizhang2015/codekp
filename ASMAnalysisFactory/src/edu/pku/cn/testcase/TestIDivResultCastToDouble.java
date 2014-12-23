package edu.pku.cn.testcase;

public class TestIDivResultCastToDouble {

	/**
	 * @param args
	 */
	static int i = 1;

	int a = Math.abs(3);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int x = 2;
		int y = 5;
		Math.ceil((double) 2);
		Math.round((float) 1);
		// Wrong: yields result 0.0
		double value1 = x / y;
		// Right: yields result 0.4
		double value2 = x / (double) y;
	}

}

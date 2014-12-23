/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-6-3 ����01:57:08
 * @modifier: Liuxizhiyi
 * @time 2008-6-3 ����01:57:08
 * @reviewer: Liuxizhiyi
 * @time 2008-6-3 ����01:57:08
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */

/**
 * 
 * @author Liuxizhiyi
 */
public class TestNullDeference {
	public int intra0(int level) {
		Object x = new Object();
		level = 1;
		switch (level) {
		case 0:
			if (level > 0)
				x = new Object();
			if (level < 4)
				x.hashCode();
			break;
		case 1:
			int i = 0;
			while (x.equals(null) == false) {
				x = 'a' + i++;
			}
			x.hashCode();
			break;
		case 2:
			x = null;
			x.equals(x);
			break;
		default:
			x = 'a';
		}
		return x.hashCode();
	}

	public int intra1(int level) {
		Object x = null;
		level = 5;
		if (level > 0)
			x = new Object();
		if (level < 4)
			x.hashCode();
		long t = 0;
		if (t > 0)
			x = new Object();
		else
			return x.hashCode();
		x.equals(t);
		return 0;
	}

	public int intra2(boolean b) {
		Object x = null;
		if (b)
			x = new Object();
		if (!b)
			return x.hashCode();
		return 0;
	}

	public int intra3(Object x) {
		Object y = x;
		// assert y!=null;
		if (x == null)
			y = new Object();
		if (y != null)
			return x.hashCode() + y.hashCode();
		else
			return x.hashCode();
	}

	public int intra4(boolean b) {
		Object x = null, y = null;
		if (b)
			x = "x";
		if (x != null)
			y = "y";
		if (y != null)
			return x.hashCode() + y.hashCode();
		else
			return x.hashCode();
	}

	public int intra5(Object x) {
		if (x == null)
			return x.hashCode();
		return 0;
	}

	public int intra6(Object x) {
		if (x == null) {
			Object y = x;
			return y.hashCode();
		}
		return 0;
	}

	public int inter1(boolean b) {
		Object x = null;
		if (b)
			x = new Object();
		return helper1(x, b);
	}

	public int inter2() {
		return helper2(null);
	}

	public int inter3(boolean b) {
		Object x = null;
		if (b)
			x = "x";
		return helper2(x);
	}

	private int helper1(Object x, boolean b) {
		if (b)
			return 0;
		return x.hashCode();
	}

	private int helper2(Object x) {
		return x.hashCode();
	}
}

// end

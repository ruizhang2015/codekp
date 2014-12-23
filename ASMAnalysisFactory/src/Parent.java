/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author root
 * @time 2009-10-25 下午01:15:31
 * @modifier: root
 * @time 2009-10-25 下午01:15:31
 * @reviewer: root
 * @time 2009-10-25 下午01:15:31
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
abstract class ParentBase {
	int i = 99;
	int j = 10;
	public Object ob;
	public static Object os = new Object();

	public ParentBase() {
		System.out.println(i);
	}
	public  void println(){
		System.out.println("parent");
	}
	public static void me(){
		
	}
	private abstract void hello();
}

public class Parent extends ParentBase {
	int i = -1;
	int j = 4;

	public Parent() {
		System.out.println(i);
	}
	public static void me(){
		
	}
	public void println(){
		System.out.println("child");
	}
	public void test() {
		ob = new Object();
		String s = ob.toString();
		System.out.println(s + os.toString());
	}

	public static void main(String args[]) {
		{
			ParentBase pb = new Parent();
			pb.println();
			System.out.println(pb.i + Parent.os.toString());
		}
		{
			try {
				Parent pb = new Parent();
				pb.test();
			} catch (Exception e) {
				Parent pb = new Parent();
				pb.test();
			} finally {
				try {
					Parent pb = new Parent();
					pb.test();
				} catch (Exception e) {
					try {
						e.printStackTrace();
					} catch (Exception ee) {
						ee.printStackTrace();
					}
				} finally {
					System.out.println(args[0]);
				}
			}
		}
	}

}

// end

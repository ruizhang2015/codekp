import java.util.HashSet;

/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2010-5-14
 * @modifier: Administrator
 * @time 2010-5-14
 * @reviewer: Administrator
 * @time 2010-5-14
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
/**
 * @author Administrator
 */
class AB{
	public int i;
	public AB(int i){
		this.i=i;
	}
	public void print(){
		System.out.println(i);
	}
}
public class testparam {
	
	static public AB ab = new AB(3); 
	
	static void f(AB a){
//		a=new AB(1);
		a.i=1;
		ab = new AB(4);
		a.print();
	}
	
	public static void main(String[] args){
//		AB a=new AB(0);
//		System.out.println(testparam.ab.i);
//		a.print();
//		f(a);
//		a.print();
//		System.out.println(testparam.ab.i);
		
		HashSet<String> set1 = new HashSet<String>();
		set1.add("1");
		HashSet set2 = (HashSet)set1.clone();
		set1.add("2");
		set1.add("3");
		
		System.out.println(set1.size());
		System.out.println(set2.size());
		
		
		
		
		
	}
}

// end

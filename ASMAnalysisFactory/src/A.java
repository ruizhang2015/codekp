/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author a
 * @time 2010-3-6
 * @modifier: a
 * @time 2010-3-6
 * @reviewer: a
 * @time 2010-3-6
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
/**
 * @author a
 */
public class A {
	public AB f;
	public void get(){
		f=new AB();
	}
	static AB getField(AB obj){		
		Object o=null;
		return (AB)o;
	}
	static void main(String[] args){
		AB p=new AB();
		AB q=p;
		AB r=new AB();
		r.f=q;
		AB s=getField(p);
		p=r;
	}
}

// end

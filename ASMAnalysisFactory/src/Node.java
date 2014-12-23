import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.CharBuffer;

class Node {
	int d;

	private class Innner1 {
		public int i; 
		public int method1() {
			int i = 0, j = 0;
			char a='1';
			a=(char) (a+i);
			return d;
		}
	}

	static {
		System.out.println("static");
	}

	Node() {
		int i = 1;
		i = i++;
		new String().substring(i, 10).charAt(1);
		System.out.println("instance");
	}

	int getNumber(int num) {
		if (num <= 2)
			return 1;
		int sum = 0;
		for (int i = 1; i < num; i++) {
			sum += getNumber(i) * getNumber(num - i);
		}
		Innner1 in=new Innner1();
		sum=in.i;
		return sum;
	}

	public static void getCombind(String pre, String charSet) {
		if (charSet.length() == 0) {
			System.out.println(pre);
			return;
		}
		for (int i = 0; i < charSet.length(); i++) {
			pre += charSet.charAt(i);
			getCombind(pre, charSet.substring(0, i) + charSet.substring(i + 1));
			pre = pre.substring(0, pre.length() - 1);
		}
	}

	public static void main(String[] args) {
		Node[] temp=new Node[2];
		getCombind("", "abcd");
		int i=0;
		switch(args.length){
		case 1:
			i++;
		case 2:
			i-=2;
		case 3:
			i--;
			default:
				i++;
		}
		// Node node=new Node();
		// int i=0;
		// char[] buffer=new char[255];
		// BufferedReader reader=new BufferedReader(new
		// InputStreamReader(System.in));
		// do{
		// try {
		// i=new Integer(reader.readLine());
		// System.out.println("result is "+node.getNumber(i));
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }while(i!=0);
	}
}
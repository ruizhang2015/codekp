import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class App {
	double c;
	double m;
	double m0;
	int avgreq = 0;
	int req;
	int appid;
	List<Integer> sloc = new ArrayList<Integer>();
	List<Integer> dloc = new ArrayList<Integer>();

	public App(double cp, double mp, double m0p, int reqp, int id) {
		c = cp;
		m = mp;
		m0 = m0p;
		req = reqp;
		appid = id;
	}
	
	public void sdeploy(int sid){
		sloc.add(sid);
	}
	
	public void ddeploy(int sid){
		dloc.add(sid);
	}
	
	public void setReq(int r){
		req = r;
	}

	public void randomSetReq(){
		Random random = new Random();
		double delta = (random.nextFloat() - 0.5) * 0.4;
		req *= (1 + delta);
		
		String fname = appid + ".txt";
		PrintWriter pw;
		try {
			pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fname, true)),true);	    
			pw.println(req);    
			pw.close();  
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
	}
	
	public double computeCpu(){
		return req * c;
	}

	public double computeMem(){
		return req * m + m0;
	}

}

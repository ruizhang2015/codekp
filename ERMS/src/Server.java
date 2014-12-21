
public class Server {
	double cpu = 0;
	double mem = 0;
	int ins_num = 0;
	int id;
	
	public Server(int id){
		this.id = id;
	}
	
	public void allocateRes(double c, double m, int n){
		cpu += c;
		mem += m;
		ins_num += n;
	}
	
	public void releaseRes(double c, double m, int n){
		cpu -= c;
		mem -= m;
		ins_num -= n;
	}

}

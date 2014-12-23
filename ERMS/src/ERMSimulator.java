import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ERMSimulator {

	Map<Integer, Server> staticpool = new HashMap<Integer, Server>();
	Map<Integer, Server> dynamicpool = new HashMap<Integer, Server>();
	Set<App> Apps = new HashSet<App>();
	
	public ERMSimulator(int na){
		int sid = 0;
		Server server = new Server(sid);
		staticpool.put(sid, server);
		for (int i = 0; i < na; i++){
			App app = new App(0.01, 0.005, 1, 50, i);			
			if (server.mem + app.m0 > 100){
				server = new Server(++sid);
				staticpool.put(sid, server);
			}
			server.allocateRes(0, app.m0, 1);
			app.sdeploy(sid);
			Apps.add(app);
		}
		
	}
	
	public void findCondidate(List<App> active, List<App> inactive){
		active.add(new App(0, 0, 2, 50, 100));
		for(Iterator<App> it = Apps.iterator(); it.hasNext(); it.next()){
			 
		}
		
	}
	
	public void relocation(List<App> active, List<App> inactive, int least_sid){
		
	}
	
	public void startSimulation(int iteration){
		
	}
	
	public static void main(String[] args) {
		ERMSimulator simulator = new ERMSimulator(220);
		List<App> active = new ArrayList<App>();
		List<App> inactive = new ArrayList<App>();
		simulator.findCondidate(active, inactive);
		System.out.println(active.get(0).appid);
		/*Server server = new Server(1);
		simulator.staticpool.put(0, server);
		server.id = 2;
		server = new Server(2);
		System.out.println(simulator.staticpool.get(0).id);
		*/
		return;
	}

}

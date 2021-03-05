package finalproject;

import javax.swing.SwingWorker;

public class StartServer extends SwingWorker<Integer, Integer> {
	private Server server;
	
	StartServer(Server server){
		this.server = server;
	}
	protected Integer doInBackground(){
		server.execute();
	    return null;
	}
}

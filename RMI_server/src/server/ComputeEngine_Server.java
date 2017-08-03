package server;

import compute.Compute;
import compute.Task;
import library.MyRegistry;
import library.MyUnicast;

public class ComputeEngine_Server  implements Compute{
	public ComputeEngine_Server() {
		super();
	}

	public <T> T executeTask(Task<T> t) {
		return t.execute();
	}
	
	
	public static void main(String[] args) {
		try {
			
			String name3 = "Compute";
			Compute engine = new Server();
			Compute stub3 = (Compute) new MyUnicast().exportObject(engine, Compute.class, 0);

			MyRegistry registry = new MyRegistry().getRegistry("127.0.0.1");
			
			registry.rebind(name3, stub3);
			System.out.println("ComputeEngine bound");
			registry.disconnect();
		} catch (Exception e) {
			System.err.println("ComputeEngine exception:");
			e.printStackTrace();
		}
	}

}

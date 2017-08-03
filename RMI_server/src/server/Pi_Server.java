package server;

import library.MyRegistry;
import library.MyUnicast;
import pi.PiImp;
import pi._Pi;

public class Pi_Server {

	public static void main(String[] args) {
		try {
			String name4 = "_Pi";
			_Pi pi = new PiImp();
			_Pi stub4 = (_Pi) new MyUnicast().exportObject(pi, _Pi.class, 0);

			MyRegistry registry = new MyRegistry().getRegistry("127.0.0.1");
			registry.rebind(name4, stub4);			
			
			System.out.println("Pi Service bound");
			registry.disconnect();
		} catch (Exception e) {
			System.err.println("Pi Service exception:");
			e.printStackTrace();
		}
	}

}

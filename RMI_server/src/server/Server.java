package server;

import compute.*;
import helloWorld.HelloWorld;
import helloWorld.helloworldImp;
import library.*;
import pi.PiImp;
import pi._Pi;
import prime.Prime;
import prime.primeImp;

public class Server implements Compute {
	public Server() {
		super();
	}

	public <T> T executeTask(Task<T> t) {
		return t.execute();
	}

	public static void main(String[] args) {
//		if (System.getSecurityManager() == null) {
//			System.setSecurityManager(new SecurityManager());
//		}
		try {
//			new StartRegistry().createRegistry(Integer.parseInt(args[0]));
			new StartRegistry().createRegistry(11111);
			
			String name2 = "Prime";
			Prime prime = new primeImp();
			Prime stub2 = (Prime) new MyUnicast().exportObject(prime, Prime.class, 0);
//			
//			String name = "HelloWorld";
//			HelloWorld hello = new helloworldImp();
//			HelloWorld stub = (HelloWorld) new MyUnicast().exportObject(hello, HelloWorld.class, 0);
			
			String name3 = "Compute";
			Compute engine = new Server();
			Compute stub3 = (Compute) new MyUnicast().exportObject(engine, Compute.class, 0);
			
			String name4 = "_Pi";
			_Pi pi = new PiImp();
			_Pi stub4 = (_Pi) new MyUnicast().exportObject(pi, _Pi.class, 0);

			MyRegistry registry = new MyRegistry().getRegistry("127.0.0.1");
			
//			registry.rebind(name, stub);
			registry.rebind(name2, stub2);
			registry.rebind(name3, stub3);
			registry.rebind(name4, stub4);
			
			System.out.println("ComputeEngine bound");
			registry.disconnect();
		} catch (Exception e) {
			System.err.println("ComputeEngine exception:");
			e.printStackTrace();
		}
	}

}

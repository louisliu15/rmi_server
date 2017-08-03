package server;

import library.MyRegistry;
import library.MyUnicast;
import prime.Prime;
import prime.primeImp;

public class Prime_Server {
	public static void main(String[] args) {
		try {
			String name2 = "Prime";
			Prime prime = new primeImp();
			Prime stub2 = (Prime) new MyUnicast().exportObject(prime, Prime.class, 0);

			MyRegistry registry = new MyRegistry().getRegistry("127.0.0.1");

			registry.rebind(name2, stub2);
			System.out.println("Prime Service bound");
			registry.disconnect();
		} catch (Exception e) {
			System.err.println("Prime Service exception:");
			e.printStackTrace();
		}
	}
}

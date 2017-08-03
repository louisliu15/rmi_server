package server;

import library.StartRegistry;

public class StartLocalRegistry {
	public static void main(String[] args){
//		new StartRegistry().createRegistry(Integer.parseInt(args[0]));
		new StartRegistry().startRegistry(11111);
	}
}

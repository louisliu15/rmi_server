package library;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.*;

public class StartRegistry implements Runnable{
	private int port;
	private static HashMap<String, Object>lookupTable = new HashMap<String, Object>();
	private static ServerSocket ss;
    private static ExecutorService executorService;
    private final int POOL_SIZE = 10;
    
	
	public <T> void startRegistry(int port) {
		try {
			ss = new ServerSocket(port);
			System.out.println("Port opened, using "+port+"....");
		} catch (IOException e) {
			System.out.println("Port is used....");
			System.out.println("Please shutdwon related program and restart the server...");
			System.exit(0);
		}
    	
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
                .availableProcessors() * POOL_SIZE);
        System.out.println("Registry Service is Started...");
        
		try {
			while (true) {
				Socket s = ss.accept();
				executorService.execute(new Handler(s));
			}
		}catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static void main(String args[]){
		new StartRegistry().startRegistry(11111);
	}
	
	@Override
	public void run(){
		startRegistry(port);
	}
	
	public void createRegistry(int port){
		this.port = port;
		new Thread(this).start();
	}
	
	class Handler implements Runnable{
		private Socket s;
		private ObjectInputStream ois = null;
		private ObjectOutputStream oos = null;
		
		public Handler(Socket s) {
			this.s = s;
			try {
				ois = new ObjectInputStream(s.getInputStream());
				oos = new ObjectOutputStream(s.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void run() {
			try {
				while (true) {				
					String methodName = ois.readUTF();
					String remoteName = ois.readUTF();
					System.out.println(methodName+" "+remoteName);
					
					Object stub = null;
					if(methodName.equalsIgnoreCase("rebind")){
						stub = ois.readObject(); 
						lookupTable.put(remoteName, stub);
					}else if(methodName.equalsIgnoreCase("lookup")){
						stub = lookupTable.get(remoteName);
						oos.writeObject(stub);
						oos.flush();
					}else{
						System.out.println("Ignore invalid request...");
					}
				}
			} catch (Exception e) {
				System.out.println("Client closed!");
			} finally {
				try {
					if (ois != null)
						ois.close();
					if (oos != null)
						oos.close();
					if (s != null) {
						s.close();
					}

				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		}
	}
}

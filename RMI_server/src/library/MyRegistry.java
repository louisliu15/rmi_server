package library;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class MyRegistry {
	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;
	private Socket s = null;

	public MyRegistry() {
	}
	
	public MyRegistry(String host) {
		connect(host, 11111);
	}
	
	public MyRegistry(String host, int port) {
		connect(host, port);
	}
	
	public MyRegistry getRegistry(String host){
		connect(host,11111);
		return this;
	}
	
	public MyRegistry getRegistry(String host, int port){
		connect(host,port);
		return this;
	}

	public void connect(String host, int port) {
		try {
			s = new Socket(host, port);
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
			System.out.println("~~~~~~~~Connection Established~~~~~~~~!");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public <T> void rebind(String name, T obj) {
		try {
			oos.writeUTF("rebind");
			oos.writeUTF(name);
			oos.writeObject(obj);
			oos.flush();
//			disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public <T> T lookup(String name) {
		T obj = null;
		try {
			oos.writeUTF("lookup");
			oos.writeUTF(name);
			oos.flush();
			obj = (T) ois.readObject();
//			disconnect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
//			disconnect();
		}
		return obj;
	}

	public void disconnect() {
		try {
			ois.close();
			oos.close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}

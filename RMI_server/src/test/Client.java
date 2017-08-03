package test;
//
//import java.io.EOFException;
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.net.Socket;
//
//import beans.Argument;
//
public class Client implements Runnable {
//	private Socket s;
//	private ObjectInputStream ois = null;
//	private ObjectOutputStream oos = null;
//	private boolean bConnected = false;
//
//	public Client(Socket socket) {
//		try {
//			this.s = socket;
//			ois = new ObjectInputStream(s.getInputStream());
//			oos = new ObjectOutputStream(s.getOutputStream());
//			bConnected = true;
//			
//			System.out.println("New client on port "+s.getPort()+" created");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	public void send(String str) {
//		try {
////			oos.writeUTF(str);
//			oos.writeObject(new Argument("testFromServer", 0, null));
//		} catch (IOException e) {
//			// clients.remove(this);
//			System.out.println("Client closed");
//		}
//	}
//
	public void run() {
//		try {
//			while (bConnected) {
//				oos.writeObject(new Argument("testFromClient", 0, null));
//				Argument arg = (Argument) ois.readObject();
//				System.out.println("------------From Client-----------\n" + arg.toString());
//				send(null);
//				// oos.flush();
//
//			}
//		} catch (EOFException e) {
//			System.out.println("Transimission overflow");
//		} catch (IOException e) {
//			System.out.println("Client closed");
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (ois != null)
//					ois.close();
//				if (oos != null)
//					oos.close();
//				if (s != null) {
//					s.close();
//				}
//
//			} catch (IOException e1) {
//				System.out.println("------------Client closed-----------");
//			}
//
//		}
	}
}

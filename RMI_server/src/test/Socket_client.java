package test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class Socket_client {
	public static String send(String host, File file){
		String result = null;
		Socket socket =null;
		DataInputStream input = null;
		DataOutputStream output =null;
		try {
			socket = new Socket(host, 5000);
			output = new DataOutputStream(socket.getOutputStream());
			
			byte[] sendBytes = new byte[300];
			int length = 0;
			
			FileInputStream fileStream = new FileInputStream(file);
			while ((length = fileStream.read(sendBytes, 0, sendBytes.length)) > 0) {
				output.write(sendBytes, 0, length);
				output.flush();
			}
			fileStream.close();
			
			input = new DataInputStream(socket.getInputStream());
			result = input.readUTF();
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				input.close();
				output.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
}

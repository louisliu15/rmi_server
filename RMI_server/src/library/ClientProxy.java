package library;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

public class ClientProxy implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String host;
	private int port;

	public ClientProxy() {

	}

	public ClientProxy(String host, int port) {
		this.host = host;
		this.port = port;
	}

	@SuppressWarnings("unchecked")
	public <T> T refer(Class<T> interfaceClass) throws Exception {
		if (interfaceClass == null) {
			throw new IllegalAccessException("Interface class == null");
		}
		if (!interfaceClass.isInterface()) {
			throw new IllegalAccessException(interfaceClass.getName() + " must be interface");
		}
		if (host == null || host.length() == 0) {
			throw new IllegalAccessException("host == null");
		}
		if (port <= 0 || port > 65535) {
			throw new IllegalAccessException("Invalid port " + port);
		}

		System.out.println("Get remote service " + interfaceClass.getName() + " from server " + host + ":" + port);

		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[] { interfaceClass },
				new MyInvocationHandler());
	}

	class MyInvocationHandler implements InvocationHandler, java.io.Serializable {
		private static final long serialVersionUID = 1L;

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			Socket socket = new Socket(host, port);
			try {
				ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());

				try {
					output.writeUTF(method.getDeclaringClass().getName());
					output.writeUTF(method.getName());
					
					for (Object p : args) {
						if (!(p.getClass().getName().startsWith("java")) && !(p.getClass().getName().startsWith("javax"))) {
							output.writeBoolean(true);
							
							byte[] sendBytes = new byte[1024];
							int length = 0;

							String filePath = this.getClass().getResource("/").getPath().replaceAll("%20", " ");
							String fileName = p.getClass().getName().replaceAll("\\.", "/");
							String fullClassPath = filePath + fileName + ".class";
							
//							output.writeUTF(filePath);
							output.writeUTF(fileName);
							output.flush();
							
//							System.out.println(fullClassPath);

							File file = new File(fullClassPath);
							FileInputStream fileStream = new FileInputStream(file);
							output.writeLong(file.length());

							while ((length = fileStream.read(sendBytes, 0, sendBytes.length)) > 0) {
								output.write(sendBytes, 0, length);
								output.flush();
							}
							fileStream.close();
						}
					}
					output.writeBoolean(false);
					output.writeObject(method.getParameterTypes());
					output.writeObject(args);
					output.flush();

					ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

					try {
						Object result = input.readObject();
						if (result instanceof Throwable) {
							throw (Throwable) result;
						}
						return result;
					} finally {
						input.close();
					}

				} finally {
					output.close();
				}

			} finally {
				socket.close();
			}
		}
	}
}

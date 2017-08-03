package library;

import java.io.*;
import java.lang.reflect.Method;
import java.net.*;

public class ServerThread implements Runnable {
	private ServerSocket server;
	private Object service;
	private Class interfaceClass;

	public ServerThread() {

	}

	public ServerThread(ServerSocket server, Object service, Class interfaceClass) {
		this.server = server;
		this.service = service;
		this.interfaceClass = interfaceClass;
	}

	public void run() {
		for (;;) {
			try {
				final Socket socket = server.accept();
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							try {
								ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

								try {
									String interfaceName = input.readUTF();
									String methodName = input.readUTF();
									while (input.readBoolean()) {
										String filePath = this.getClass().getResource("/").getPath().replaceAll("%20", " ");
										String fileName = input.readUTF();
										
										String fullClassPath = filePath + fileName + ".class";
										Long fileLength = input.readLong();
										byte[] inputByte = new byte[1024];
										int length = 0;
										int total = 0;

										File file = new File(fullClassPath);
										FileOutputStream fileStream = new FileOutputStream(file);

										while ((length = input.read(inputByte, 0, inputByte.length)) > 0) {											
											fileStream.write(inputByte, 0, length);
											fileStream.flush();																										
											total += length;
											if(total==fileLength)
												break;
										}
										fileStream.close();
										
										System.out.println("File Download Finished...");
										URL url = file.toURL();
										URL[] urls = new URL[] {url};
										URLClassLoader loader = new URLClassLoader(urls);
										loader.loadClass(fileName.replaceAll("/", "."));
										loader.close();
									}
									Class<?>[] parameterTypes = (Class<?>[]) input.readObject();
									Object[] arguments = (Object[]) input.readObject();
									ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());

									try {
										if (!interfaceName.equals(interfaceClass.getName())) {
											throw new IllegalAccessException("Interface wrong, export:" + interfaceClass
													+ " refer:" + interfaceName);
										}
										Method method = service.getClass().getMethod(methodName, parameterTypes);
										Object result = method.invoke(service, arguments);
										output.writeObject(result);
									} catch (Throwable t) {
										output.writeObject(t);
									} finally {
										output.close();
									}
								} finally {
									input.close();
								}
							} finally {
								socket.close();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

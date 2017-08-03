package library;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MyUnicast<T>{
	private String host;
	private int port;
	private ServerSocket server;

	public T exportObject(T service, Class interfaceClass, int tport) throws Exception {
		init(service, tport);
		// export(service, service.getClass(), tport);
		ServerThread th = new ServerThread(server, service, interfaceClass);
		new Thread(th).start();
		ClientProxy proxy = new ClientProxy(host, port);
		return (T) proxy.refer(interfaceClass);
	}

	public void init(Object service, int tport) throws Exception {
		if (service == null) {
			throw new IllegalAccessException("service instance == null");
		}
		if (tport < 0 || tport > 65535) {
			throw new IllegalAccessException("Invalid port " + tport);
		}
		server = new ServerSocket(tport);
		host = server.getInetAddress().getHostAddress().toString();
		port = server.getLocalPort();
	}

//	@SuppressWarnings("unchecked")
//	public <T> T refer(Class<T> interfaceClass, String host, int port) throws Exception {
//		if (interfaceClass == null) {
//			throw new IllegalAccessException("Interface class == null");
//		}
//		if (!interfaceClass.isInterface()) {
//			throw new IllegalAccessException(interfaceClass.getName() + " must be interface");
//		}
//		if (host == null || host.length() == 0) {
//			throw new IllegalAccessException("host == null");
//		}
//		if (port <= 0 || port > 65535) {
//			throw new IllegalAccessException("Invalid port " + port);
//		}
//
//		System.out.println("Get remote service " + interfaceClass.getName() + " from server " + host + ":" + port);
////		Class proxyClass = Proxy.getProxyClass(interfaceClass.getClassLoader(), new Class<?>[] { interfaceClass });
////		InvocationHandler handler = new MyInvocationHandler();
////		return (T) proxyClass.getConstructor(new Class[] { InvocationHandler.class })
////				.newInstance(new Object[] { handler });
//
//		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[] { interfaceClass },
//				new MyInvocationHandler());
//	}
//
//	class MyInvocationHandler implements InvocationHandler, java.io.Serializable {
//		private static final long serialVersionUID = 1L;
//
//		@Override
//		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//			Socket socket = new Socket(host, port);
//			try {
//				ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
//
//				try {
//					output.writeUTF(method.getDeclaringClass().getName());
//					output.writeUTF(method.getName());
//					output.writeObject(method.getParameterTypes());
//					output.writeObject(args);
//
//					ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
//
//					try {
//						Object result = input.readObject();
//						if (result instanceof Throwable) {
//							throw (Throwable) result;
//						}
//						return result;
//					} finally {
//						input.close();
//					}
//
//				} finally {
//					output.close();
//				}
//
//			} finally {
//				socket.close();
//			}
//		}
//	}

	// public void export(Object service, Class interfaceClazz, int port) throws
	// Exception {
	// // if (service == null) {
	// // throw new IllegalAccessException("service instance == null");
	// // }
	// // if (port < 0 || port > 65535) {
	// // throw new IllegalAccessException("Invalid port " + port);
	// // }
	// System.out.println("Export service " + service.getClass().getName() + "
	// on port " + port);
	//
	// server = new ServerSocket(port);
	// host = server.getInetAddress().getHostAddress().toString();
	// for (;;) {
	// final Socket socket = server.accept();
	// try {
	// new Thread(new Runnable() {
	//
	// @Override
	// public void run() {
	// try {
	// try {
	// ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
	//
	// try {
	// String interfaceName = input.readUTF();
	// String methodName = input.readUTF();
	// Class<?>[] parameterTypes = (Class<?>[]) input.readObject();
	// Object[] arguments = (Object[]) input.readObject();
	// ObjectOutputStream output = new
	// ObjectOutputStream(socket.getOutputStream());
	//
	// try {
	// if (!interfaceName.equals(interfaceClazz.getName())) {
	// throw new IllegalAccessException("Interface wrong, export:" +
	// interfaceClazz
	// + " refer:" + interfaceName);
	// }
	// Method method = service.getClass().getMethod(methodName, parameterTypes);
	// Object result = method.invoke(service, arguments);
	// output.writeObject(result);
	// } catch (Throwable t) {
	// output.writeObject(t);
	// } finally {
	// output.close();
	// }
	// } finally {
	// input.close();
	// }
	// } finally {
	// socket.close();
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// }).start();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// }
	// }

}
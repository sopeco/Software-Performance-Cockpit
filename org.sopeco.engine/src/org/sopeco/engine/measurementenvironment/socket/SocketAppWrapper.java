package org.sopeco.engine.measurementenvironment.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SocketAppWrapper {

	private Socket socket;

	private ObjectInputStream oiStream;
	private ObjectOutputStream ooStream;

	private String[] controller;
	private Map<String, SocketMECWrapper> wrapperMap;

	public SocketAppWrapper(Socket pSocket) {
		wrapperMap = new HashMap<String, SocketMECWrapper>();

		socket = pSocket;

		try {
			ooStream = new ObjectOutputStream(socket.getOutputStream());
			oiStream = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

		controller = getAvailableMEController();
	}

	public Socket getSocket() {
		return socket;
	}

	public String[] getAvailableMEController() {
		synchronized (socket) {
			try {
				ooStream.writeObject(CallType.AVAILABLE_CONTROLLER);
				ooStream.flush();

				return (String[]) oiStream.readObject();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}

	public SocketMECWrapper getMECWrapper(String controllerName) {
		if (Arrays.asList(controller).contains(controllerName)) {
			if (!wrapperMap.containsKey(controllerName)) {
				wrapperMap.put(controllerName, new SocketMECWrapper(socket,
						controllerName));
			}
			return wrapperMap.get(controllerName);
		}
		return null;
	}
}

package org.sopeco.engine.measurementenvironment.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SocketAcception extends Thread {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SocketAcception.class);

	private static final int DEFAULT_PORT = 11300;

	private static SocketAcception singleton;

	public static void open() {
		open(DEFAULT_PORT);
	}

	public static void open(int port) {
		if (singleton != null) {
			throw new RuntimeException("Server already started.");
		}
		try {
			LOGGER.info("Bind listener to port " + port);
			singleton = new SocketAcception(port);
			singleton.setDaemon(true);
			singleton.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private ServerSocket serverSocket;

	private SocketAcception(int port) throws IOException {
		serverSocket = new ServerSocket(port);
	}

	public void run() {
		while (serverSocket.isBound()) {
			Socket incoming = null;

			try {
				LOGGER.debug("Waiting for new connections..");
				incoming = serverSocket.accept();
				handleSocket(incoming);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void handleSocket(Socket socket) {
		LOGGER.debug("New connection from "
				+ socket.getInetAddress().toString());
		SocketAppWrapper wrapper = new SocketAppWrapper(socket);

		// TODO: maybe security check like IP-white list...

		SocketManager.handle(wrapper);
	}
}

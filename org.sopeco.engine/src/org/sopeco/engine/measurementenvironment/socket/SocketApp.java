package org.sopeco.engine.measurementenvironment.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.engine.measurementenvironment.app.MECApplication;

public class SocketApp extends Thread {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SocketApp.class);

	private Socket socket;

	private String host;
	private int port;

	private ObjectInputStream oiStream;
	private ObjectOutputStream ooStream;
	
	public SocketApp (String pHost, int pPort) {
		host = pHost;
		port = pPort;
	}

	public void connect() {
		try {
			LOGGER.debug("Connect to {}:{}", host, port);
			socket = new Socket(host, port);

			ooStream = new ObjectOutputStream(socket.getOutputStream());
			oiStream = new ObjectInputStream(socket.getInputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		connect();

		boolean loop = true;
		while (loop) {
			try {
				LOGGER.debug("Waiting for call..");
				CallType call = (CallType) oiStream.readObject();

				LOGGER.debug("Call: {}", call.toString());
				CallExecutor.execute(call, oiStream, ooStream);
			} catch (IOException e) {
				e.printStackTrace();
				loop = false;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}

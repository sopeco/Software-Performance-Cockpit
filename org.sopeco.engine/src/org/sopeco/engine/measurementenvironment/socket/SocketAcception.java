/**
 * Copyright (c) 2013 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.sopeco.engine.measurementenvironment.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SocketAcception extends Thread {

	private static final Logger LOGGER = LoggerFactory.getLogger(SocketAcception.class);

	private static final int SOCKET_TIMEOUT = 1000 * 30;
	private static final int DEFAULT_PORT = 8089;

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
		serverSocket.setSoTimeout(SOCKET_TIMEOUT);
	}

	public void run() {
		while (serverSocket.isBound()) {
			Socket incoming = null;

			try {
				LOGGER.debug("Waiting for new connections..");
				incoming = serverSocket.accept();
				cleanUpConnections();
				handleSocket(incoming);
				LOGGER.debug("New connection established.");

			} catch (SocketTimeoutException ste) {
				// clean up dead connections and retry
				cleanUpConnections();
			} catch (IOException e) {
				LOGGER.error("Socket acceptence error! {}", e);
			}
		}
	}

	private void cleanUpConnections() {
		SocketManager.cleanUp();

	}

	private void handleSocket(Socket socket) {
		LOGGER.debug("New connection from " + socket.getInetAddress().getHostAddress().toString());

		// TODO: maybe security check like IP-white list...

		SocketManager.handle(socket);
	}
}

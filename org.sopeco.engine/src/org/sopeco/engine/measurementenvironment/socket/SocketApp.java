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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.config.Configuration;
import org.sopeco.config.IConfiguration;

public class SocketApp extends Thread {

	private static final Logger LOGGER = LoggerFactory.getLogger(SocketApp.class);

	private static final int RECONNECT_DELAY = Configuration.getSessionSingleton(Configuration.getGlobalSessionId())
			.getPropertyAsInteger(IConfiguration.CONF_MEC_SOCKET_RECONNECT_DELAY, 60);

	private Socket socket;

	private String host;
	private int port;
	private String identifier;
	private ObjectInputStream oiStream;
	private ObjectOutputStream ooStream;

	public SocketApp(String pHost, int pPort) {
		this(pHost, pPort, UUID.randomUUID().toString());

	}

	public SocketApp(String pHost, int pPort, String identifier) {
		host = pHost;
		port = pPort;
		this.setIdentifier(identifier);
	}

	public void connect() {
		do {
			try {
				LOGGER.debug("Connect to {}:{}", host, port);
				socket = new Socket(host, port);

				ooStream = new ObjectOutputStream(socket.getOutputStream());
				oiStream = new ObjectInputStream(socket.getInputStream());
			} catch (IOException e) {
				socket = null;
				LOGGER.error("Can not connect cause: {}", e.getMessage());
				LOGGER.info("Trying to reconnect in {} seconds", RECONNECT_DELAY);

				try {
					Thread.sleep(RECONNECT_DELAY * 1000);
				} catch (Exception e2) {
				}
			}
		} while (socket == null);
	}

	@Override
	public void run() {
		connect();

		boolean loop = true;
		while (loop) {
			try {
				Object firstObject = oiStream.readObject();

				// Skip if it is boolean
				if (firstObject instanceof Boolean) {
					continue;
				}

				String controllerName = (String) firstObject;
				String method = (String) oiStream.readObject();
				Object[] parameter = (Object[]) oiStream.readObject();

				CallExecutor.execute(controllerName, method, parameter, ooStream);
			} catch (IOException e) {
				LOGGER.error("IOException: {}", e.getMessage());
				connect();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @return the identifier
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * @param identifier
	 *            the identifier to set
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
}

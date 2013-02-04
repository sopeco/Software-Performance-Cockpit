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
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SocketManager {

	private SocketManager() {
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(SocketManager.class);

	private static Map<String, SocketAppWrapper> appWrapperMap = new HashMap<String, SocketAppWrapper>();

	public static void handle(Socket socket) {
		LOGGER.debug("Stored SocketMECWrapper from {}", socket.getInetAddress().getHostAddress());
		SocketAppWrapper appWrapper = new SocketAppWrapper(socket);
		String key = appWrapper.getSocket().getInetAddress().getHostAddress();
		appWrapperMap.put(key, appWrapper);
	}

	public static Collection<SocketAppWrapper> getAllSocketApps() {
		return new HashMap<String, SocketAppWrapper>(appWrapperMap).values();
	}

	public static SocketAppWrapper getSocketApp(String ip) {
		if (appWrapperMap.containsKey(ip)) {
			return appWrapperMap.get(ip);
		}
		return null;
	}

	public static void removeSocketApp(SocketAppWrapper appWrapper) {
		removeSocketApp(appWrapper.getSocket().getInetAddress().getHostAddress());
	}

	public static void removeSocketApp(String ip) {
		if (!appWrapperMap.get(ip).getSocket().isClosed()) {
			try {
				appWrapperMap.get(ip).getOoStream().close();
				appWrapperMap.get(ip).getOiStream().close();
			} catch (IOException e) {
			} finally {
				try {
					if (!appWrapperMap.get(ip).getSocket().isClosed()) {
						appWrapperMap.get(ip).getSocket().close();
					}
				} catch (IOException e) {
				}
				appWrapperMap.remove(ip);
			}
		}
	}

	public static SocketMECWrapper getSocketMEC(String ip, String mecName) {
		if (getSocketApp(ip) != null) {
			return getSocketApp(ip).getMECWrapper(mecName);
		}
		return null;
	}

}

package org.sopeco.engine.measurementenvironment.socket;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SocketManager {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SocketManager.class);
	private static SocketManager singleton;

	public static void handle(SocketAppWrapper socketMECWrapper) {
		if (singleton == null) {
			singleton = new SocketManager();
		}
		singleton.handleWrapper(socketMECWrapper);
	}

	public static SocketAppWrapper getSocketApp(String ip) {
		if (singleton == null) {
			return null;
		}
		return singleton.getAppWrapper(ip);
	}

	public static SocketMECWrapper getSocketMEC(String ip, String mecName) {
		return singleton.getMECWrapper(ip, mecName);
	}

	private Map<String, SocketAppWrapper> appWrapperMap;

	private SocketManager() {
		appWrapperMap = new HashMap<String, SocketAppWrapper>();
	}

	private void handleWrapper(SocketAppWrapper wrapper) {
		LOGGER.debug("Stored SocketMECWrapper from {}", wrapper.getSocket()
				.getInetAddress().getHostAddress());

		appWrapperMap.put(
				wrapper.getSocket().getInetAddress().getHostAddress(), wrapper);
	}

	private SocketAppWrapper getAppWrapper(String ip) {
		if (appWrapperMap.containsKey(ip)) {
			return appWrapperMap.get(ip);
		}
		return null;
	}

	private SocketMECWrapper getMECWrapper(String ip, String name) {
		return getAppWrapper(ip).getMECWrapper(name);
	}
}

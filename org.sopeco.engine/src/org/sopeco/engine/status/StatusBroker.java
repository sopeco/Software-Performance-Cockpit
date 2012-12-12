package org.sopeco.engine.status;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;

/**
 * 
 * @author Marius Oehler
 * 
 */
@Path("statusService")
public class StatusBroker {

	private static HttpServer server;
	private static StatusBroker singleton;

	private static final long TIMEOUT_MS = 1000;

	/**
	 * Returns the singleton object of the StatusBroker.
	 * 
	 * @return StatusBroker
	 */
	public static StatusBroker get() {
		if (singleton == null) {
			singleton = new StatusBroker();
		}
		return singleton;
	}

	/**
	 * Starts a HttpServer on the port 8088.
	 */
	public static void startHttpServer() {
		try {
			server = HttpServerFactory.create("http://localhost:8088/rest");
			server.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Stops the HttpServer.
	 */
	public static void stopHttpServer() {
		if (server != null) {
			server.stop(0);
			server = null;
		}
	}

	/** Token -> StatusManager */
	private Map<String, StatusManager> controllerMap;
	/** SessionId -> Token */
	private Map<String, String> tokenMap;

	/**
	 * Returns the number of items that are currently in the map.
	 * 
	 * @return number of items
	 */
	public int activeTokensCount() {
		return controllerMap.size();
	}

	/**
	 * Generates a token and creates a new StatusManager. The manager is stored
	 * in the managerMap, with the generated token as key. The token is an SHA
	 * hash out of the <code>System.nanoTime + id</code>. The manager is
	 * accessible threw the {@link #getManager(String)} method.
	 * 
	 * @param controller
	 * @return
	 */
	public String createToken(String sessionId, String controller) {
		initMap();
		String token = createHash(System.nanoTime() + sessionId);
		controllerMap.put(token, new StatusManager(controller));
		tokenMap.put(sessionId + controller, token);
		return token;
	}

	public String getToken(String sessionIdAndController) {
		initMap();
		if (tokenMap.containsKey(sessionIdAndController)) {
			return tokenMap.get(sessionIdAndController);
		}
		return null;
	}

	/**
	 * Returns a list with all tokens, that are assigned to a StatusManager who
	 * has currently a timeout.
	 * 
	 * @return
	 */
	public List<String> getExpiredToken() {
		List<String> returnList = new ArrayList<String>();
		for (String token : controllerMap.keySet()) {
			if (System.currentTimeMillis() - controllerMap.get(token).getLastPackageReceivedTime() > TIMEOUT_MS) {
				returnList.add(token);
			}
		}
		return returnList;
	}

	/**
	 * Generating a {@link InitializePackage}, which has the current host-URL,
	 * and the specified token.
	 * 
	 * @param token
	 *            token which is used by the sender.
	 * @return an {@link InitializePackage}
	 */
	public InitializePackage getInitializePackage(String token) {
		initMap();
		if (!controllerMap.containsKey(token)) {
			System.err.println("invalid token");
			return null;
		}
		InitializePackage initPackage = new InitializePackage();
		try {
			String host = "http://";
			host += InetAddress.getLocalHost().getHostAddress();
			host += ":8088/rest";
			initPackage.setHostUrl(host);
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
		initPackage.setToken(token);
		return initPackage;
	}

	/**
	 * Returns the manager which is stored under the given token.
	 * 
	 * @param token
	 * @return StatusManager
	 */
	public StatusManager getManager(String token) {
		initMap();
		if (controllerMap.containsKey(token)) {
			return controllerMap.get(token);
		}
		return null;
	}

	@PUT
	@Path("{token}")
	@Consumes(MediaType.APPLICATION_XML)
	public void postMessage(@PathParam("token") String token, StatusMessageWrapper statusMessageWrapper) {
		if (!(statusMessageWrapper.getStatusMessageObject() instanceof StatusMessage)) {
			System.err.println("Wrong object");
			return;
		}
		StatusMessage statusMessage = (StatusMessage) statusMessageWrapper.getStatusMessageObject();
		if (get().getManager(token) == null) {
			System.err.println("invalid token: " + token + " - " + statusMessage.getEventType());
			return;
		}

		get().getManager(token).newStatus(statusMessage);
	}

	/**
	 * Removes the StatusManager, which is stored under the given key.
	 * 
	 * @param token
	 */
	public void removeManager(String token) {
		controllerMap.remove(token);
	}

	/**
	 * Calculates an SHA256 hash out of the given string.
	 * 
	 * @param source
	 *            plain string
	 * @return sha256 string
	 */
	private String createHash(String source) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
			byte[] array = md.digest(source.getBytes());
			StringBuffer sb = new StringBuffer();
			for (byte element : array) {
				sb.append(Integer.toHexString((element & 0xFF) | 0x100).substring(1, 3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Initlializes the map.
	 */
	private void initMap() {
		if (controllerMap == null) {
			controllerMap = new HashMap<String, StatusManager>();
		}
		if (tokenMap == null) {
			tokenMap = new HashMap<String, String>();
		}
	}
}

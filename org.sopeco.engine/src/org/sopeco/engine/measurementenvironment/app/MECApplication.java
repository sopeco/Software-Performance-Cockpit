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
package org.sopeco.engine.measurementenvironment.app;

import java.net.URI;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.engine.measurementenvironment.socket.SocketApp;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class MECApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(MECApplication.class);

	/** Default port for the REST server. */
	private static final int DEFAULT_REST_PORT = 1300;

	/** The singleton instance of this class. */
	private static MECApplication singleton;

	private String socketIdentifier;
	/**
	 * Returns the singleton instance of this class.
	 * 
	 * @return instance of MECApplication
	 */
	public static MECApplication get() {
		if (singleton == null) {
			singleton = new MECApplication();
		}
		return singleton;
	}

	public static String nameOfMEC(IMeasurementEnvironmentController controller) {
		for (String key : get().meControllerMap.keySet()) {
			if (controller == get().meControllerMap.get(key)) {
				return key;
			}
		}
		return null;
	}

	/** The map, where the controller are stored. */
	private Map<String, IMeasurementEnvironmentController> meControllerMap;

	/**
	 * Constructor.
	 */
	private MECApplication() {
		meControllerMap = new HashMap<String, IMeasurementEnvironmentController>();
	}

	/**
	 * Adds the specified controller to the application. It will be reached
	 * under the given name.
	 * 
	 * @param name
	 *            where the MEC is reachable
	 * @param controller
	 *            which is binded to the specified name
	 */
	public void addMeasurementController(String name, IMeasurementEnvironmentController controller) {
		String pattern = "[a-zA-Z0-9_\\-]+";
		if (!name.matches(pattern)) {
			throw new RuntimeException("Only the following characters are allowed: 0-9 a-z A-Z _ -");
		}
		if (meControllerMap.containsKey(name)) {
			// throw new
			// RuntimeException("There is already a MEController named '" + name
			// + "'.");
			LOGGER.warn("There is already a MEController named '" + name + "'.");
		}
		meControllerMap.put(name, controller);
	}

	/**
	 * Returns a String array instance, which contains all registered controller
	 * names.
	 * 
	 * @return instance of a String array
	 */
	public String[] getControllerList() {
		Set<String> controller = meControllerMap.keySet();
		String[] controllerNames = new String[controller.size()];
		int count = 0;
		for (String name : controller) {
			controllerNames[count++] = name;
		}
		return controllerNames;
	}

	/**
	 * Returns the instance of the MEController, which is related to the given
	 * name.
	 * 
	 * @param name
	 * @return
	 */
	public IMeasurementEnvironmentController getMEController(String name) {
		if (!meControllerMap.containsKey(name)) {
			throw new RuntimeException("No Controller, named '" + name + "', available.");
		}
		return meControllerMap.get(name);
	}

	/**
	 * Starts a REST server on the default port 1300 over which the
	 * MEControllers are reachable.
	 */
	public void startREST() {
		startREST(DEFAULT_REST_PORT);
	}

	/**
	 * Starts a REST server on the given port over which the MEControllers are
	 * reachable.
	 * 
	 * @param port
	 *            on which the server will running.
	 */
	public void startREST(int port) {
		RESTServer.startREST(port);
	}

	/**
	 * Registers the specified MEController via RMI on the default RMI-Registry
	 * port 1099.
	 */
	public void startRMI() {
		startRMI(Registry.REGISTRY_PORT);
	}

	/**
	 * Registers the specified MEController via RMI on the given port.
	 * 
	 * @param port
	 *            on which the RMI-Registry will be.
	 */
	public void startRMI(int port) {
		RMIServer.startRemoteMEController(port);
	}

	@Deprecated
	public void startRemoteMEController(IMeasurementEnvironmentController meController, URI uri) {
		String name = uri.getPath().replaceAll("/", "");
		addMeasurementController(name, meController);
		startRMI(uri.getPort());
	}

	public void socketConnect(String host, int port) {
		SocketApp sApp = new SocketApp(host, port);
		setSocketIdentifier(sApp.getIdentifier());
		LOGGER.info("connecting to SoPeCo with the following identifier: {}", getSocketIdentifier());
		sApp.start();

	}

	public void socketConnect(String host, int port, String identifier) {
		SocketApp sApp = new SocketApp(host, port, identifier);
		setSocketIdentifier(sApp.getIdentifier());
		LOGGER.info("connecting to SoPeCo with the following identifier: {}", getSocketIdentifier());
		sApp.start();
	}

	/**
	 * @return the socketIdentifier
	 */
	public String getSocketIdentifier() {
		return socketIdentifier;
	}

	/**
	 * @param socketIdentifier the socketIdentifier to set
	 */
	public void setSocketIdentifier(String socketIdentifier) {
		this.socketIdentifier = socketIdentifier;
	}
}

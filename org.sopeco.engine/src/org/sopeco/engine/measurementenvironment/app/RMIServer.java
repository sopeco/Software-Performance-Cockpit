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

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.UnicastRemoteObject;
import java.security.AccessControlException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.util.system.OutputStreamLogger;

/**
 * 
 * @author Marius Oehler
 * 
 */
final class RMIServer {

	private static final Logger LOGGER = LoggerFactory.getLogger(RMIServer.class);

	/** singleton for RMI registry */
	private static Registry registry = null;

	private static boolean running = false;

	private static int port;

	/**
	 * Starts the given meController as an RMI service callable remotely.
	 * 
	 * @param meController
	 *            controller to be called remotely
	 * @param meURI
	 *            URI where the controller shall be published.
	 */
	public static void startRemoteMEController(int pPort) {
		try {
			running = true;
			port = pPort;
			
			LOGGER.info("Publishing MEController.");

			for (String controllerName : MECApplication.get().getControllerList()) {
				bindMEController(controllerName, MECApplication.get().getMEController(controllerName));
				LOGGER.info("  > rmi://localhost:{}/{}", port, controllerName);
			}

			configureRemoteLogger();

			LOGGER.info("Publishing finished.");

		} catch (RemoteException e) {
			running = false;

			LOGGER.error("Cannot start the RMI server. Error: {}", e.getMessage());
			throw new RuntimeException("Cannot start the RMI server. Error: ", e);

		}
	}

	private static void bindMEController(String name, IMeasurementEnvironmentController meController) throws RemoteException {
		IMeasurementEnvironmentController meControllerStub = (IMeasurementEnvironmentController) UnicastRemoteObject
				.exportObject(meController, 0);

		getRegistry(port).rebind(name, meControllerStub);
	}

	/**
	 * Sets the output stream for the remote logger.
	 */
	private static void configureRemoteLogger() {
		try {
			RemoteServer.setLog(new OutputStreamLogger(System.out, RemoteServer.class));
		} catch (AccessControlException ae) {
			LOGGER.warn("Cannot assign a logger to the remote server. RMI calls will not be logged.");
			LOGGER.warn("The error is: {}", ae.getMessage());
			LOGGER.warn("Try adding the following line to the " + "\"grant\" section of your '.java.policy' file:");
			LOGGER.warn("    permission java.util.logging.LoggingPermission \"control\";");
			LOGGER.warn("The '.java.policy' file can be located at Java home or your user home directory.");
		}

	}

	/**
	 * Gets or creates the RMI registry for the given URI.
	 * 
	 * @param meURI
	 * @throws RemoteException
	 */
	private static synchronized Registry getRegistry(int pPort) throws RemoteException {
		if (registry == null) {
			registry = LocateRegistry.createRegistry(pPort);
		}
		return registry;
	}
	
	private RMIServer() {
	}

}

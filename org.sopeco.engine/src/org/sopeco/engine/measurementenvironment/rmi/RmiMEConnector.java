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
package org.sopeco.engine.measurementenvironment.rmi;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
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
 * Creates a connector to a remote MEController using RMI.
 * 
 * @author Jens Happe
 * 
 */
public final class RmiMEConnector {

	/**
	 * This is a utility class, thus using private constructor.
	 */
	private RmiMEConnector() {

	}

	/** default logger */
	private static Logger logger = LoggerFactory.getLogger(RmiMEConnector.class);

	/** singleton for RMI registry */
	private static Registry registry = null;

	/**
	 * Connects to a remote measurement environment controller (via RMI)
	 * identified by the given URI and returns a local instance.
	 * 
	 * @param meURI
	 *            the URI of the RMI service
	 * @return a local instance of the controller
	 */
	public static IMeasurementEnvironmentController connectToMEController(URI meURI) {

		try {
			LocateRegistry.getRegistry(meURI.getHost(), meURI.getPort());

			logger.debug("Looking up {}", meURI);

			IMeasurementEnvironmentController meController = (IMeasurementEnvironmentController) Naming.lookup(meURI
					.toString());

			logger.info("Received SatelliteController instance from {}", meURI);

			return meController;

		} catch (RemoteException e) {
			logger.error("Cannot access remote controller. Error Message: '{}'", e.getMessage());
			throw new IllegalStateException("Cannot access remote controller.", e);
		} catch (MalformedURLException e) {
			logger.error("Malformed URI. Error Message: '{}'", e.getMessage());
			throw new IllegalStateException("Malformed URI.", e);
		} catch (NotBoundException e) {
			logger.error("Name not found in registry. Error Message: '{}'", e.getMessage());
			throw new IllegalStateException("Name not found in registry.", e);
		}

	}

	/**
	 * Starts the given meController as an RMI service callable remotely.
	 * 
	 * @param meController
	 *            controller to be called remotely
	 * @param meUriStr
	 *            String representation of the URI where the controller shall be
	 *            published.
	 */
	public static void startRemoteMEController(IMeasurementEnvironmentController meController, String meUriStr) {
		try {
			URI meURI = new URI(meUriStr);
			startRemoteMEController(meController, meURI);
		} catch (URISyntaxException e) {
			logger.error("Cannot parse URI.", e);
			throw new IllegalArgumentException("Cannot parse URI.", e);
		}
	}

	/**
	 * Starts the given meController as an RMI service callable remotely.
	 * 
	 * @param meController
	 *            controller to be called remotely
	 * @param meURI
	 *            URI where the controller shall be published.
	 */
	public static void startRemoteMEController(IMeasurementEnvironmentController meController, URI meURI) {

		try {

			logger.info("Publishing MEController under: {}", meURI.toString());

			Registry registry = getRegistry(meURI);

			IMeasurementEnvironmentController meControllerStub = (IMeasurementEnvironmentController) UnicastRemoteObject
					.exportObject(meController, 0);

			configureRemoteLogger();

			registry.rebind(getName(meURI), meControllerStub);

			logger.info("Publishing finished.", meURI.toString());

		} catch (RemoteException e) {

			logger.error("Cannot start the RMI server. Error: {}", e.getMessage());
			throw new RuntimeException("Cannot start the RMI server. Error: ", e);

		}
	}

	/**
	 * Gets or creates the RMI registry for the given URI.
	 * 
	 * @param meURI
	 * @throws RemoteException
	 */
	private static synchronized Registry getRegistry(URI meURI) throws RemoteException {
		if (registry == null) {
			registry = LocateRegistry.createRegistry(meURI.getPort());
		}
		return registry;
	}

	/**
	 * returns the path of the URI without the first slash.
	 * 
	 * @param meURI
	 */
	private static String getName(URI meURI) {
		String path = meURI.getPath();
		return path.substring(1);
	}

	/**
	 * Sets the output stream for the remote logger.
	 */
	private static void configureRemoteLogger() {
		try {
			RemoteServer.setLog(new OutputStreamLogger(System.out, RemoteServer.class));
		} catch (AccessControlException ae) {
			logger.warn("Cannot assign a logger to the remote server. RMI calls will not be logged.");
			logger.warn("The error is: {}", ae.getMessage());
			logger.warn("Try adding the following line to the " + "\"grant\" section of your '.java.policy' file:");
			logger.warn("    permission java.util.logging.LoggingPermission \"control\";");
			logger.warn("The '.java.policy' file can be located at Java home or your user home directory.");
		}

	}

}

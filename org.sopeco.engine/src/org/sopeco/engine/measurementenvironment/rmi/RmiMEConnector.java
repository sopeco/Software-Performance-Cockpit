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
public class RmiMEConnector {
	
	/** default logger */
	private static Logger logger = LoggerFactory.getLogger(RmiMEConnector.class);

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

			IMeasurementEnvironmentController meController = (IMeasurementEnvironmentController) Naming.lookup(meURI.toString());

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
	 * @param meURI
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

			Registry registry = getRegistry(meURI);

			IMeasurementEnvironmentController meControllerStub = (IMeasurementEnvironmentController) UnicastRemoteObject.exportObject(meController, 0);

			configureRemoteLogger();

			registry.rebind(getName(meURI), meControllerStub);

		} catch (RemoteException e) {
			
			logger.error("Cannot start the RMI server.", e);
			throw new RuntimeException("Cannot start the RMI server. Error: ", e);
		
		}
	}

	
	/**
	 * Gets or creates the RMI registry for the given URI.
	 *  
	 * @param meURI
	 * @throws RemoteException
	 */
	private static Registry getRegistry(URI meURI) throws RemoteException {
		try {
			return LocateRegistry.createRegistry(meURI.getPort());
		} catch (RemoteException e) {
			return LocateRegistry.getRegistry(meURI.getHost(), meURI.getPort());
		}
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

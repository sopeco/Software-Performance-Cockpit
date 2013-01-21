package org.sopeco.engine.measurementenvironment.connector;

import java.net.URI;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.engine.measurementenvironment.rest.RestMECWrapper;
import org.sopeco.engine.measurementenvironment.rest.RestServices;
import org.sopeco.engine.measurementenvironment.rest.TransferPackage;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.UniformInterfaceException;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class RestMEConnector implements IMEConnector {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestMEConnector.class);
	private static final String ALIVE_PREFIX = "alive";
	private static final String AVAILABLE_CONTROLLER_PREFIX = "availableController";

	/**
	 * Checks whether a MEController is running on the given URL.
	 * 
	 * @param url
	 *            to check
	 * @return false if the host is not reachable or something other than the
	 *         alive message is returned
	 */
	public static boolean isMECApplicationRunning(String url) {
		url += ALIVE_PREFIX;

		ClientResponse response = null;
		try {
			response = Client.create().resource(url).get(ClientResponse.class);
		} catch (ClientHandlerException e) {
			LOGGER.error(e.getLocalizedMessage() + " - URL: " + url);
			return false;
		} catch (UniformInterfaceException e) {
			LOGGER.error(e.getLocalizedMessage() + " - URL: " + url);
			return false;
		}
		if (response.getStatus() != Status.OK.getStatusCode()) {
			LOGGER.error("Request was not successful. Server returned: " + response.getStatus());
			return false;
		} else if (!response.hasEntity() || !response.getEntity(String.class).equals(RestServices.ALIVE_MESSAGE)) {
			LOGGER.error("The host is probably not a valid MEController.");
			return false;
		}
		LOGGER.debug("Controller is reachable.");
		return true;
	}

	/**
	 * Returns an array with all available MEController.
	 * 
	 * @param url
	 * @return
	 */
	public static String[] getAvailableMEController(String url) {
		url += AVAILABLE_CONTROLLER_PREFIX;

		ClientResponse response = null;
		try {
			response = Client.create().resource(url).get(ClientResponse.class);
		} catch (ClientHandlerException e) {
			throw e;
		} catch (UniformInterfaceException e) {
			throw e;
		}

		if (response.getStatus() != Status.OK.getStatusCode()) {
			LOGGER.info("Request was not successful. Server returned: " + response.getStatus());
		} else if (response.hasEntity()) {
			TransferPackage result = response.getEntity(TransferPackage.class);
			return result.getA( String[].class);
		}
		return new String[0];
	}

	/*
	 * 
	 * ############################################################
	 * ############################################################
	 * ############################################################
	 * ############################################################
	 * ############################################################
	 */

	/**
	 * This is a utility class, thus using private constructor.
	 */
	RestMEConnector() {
	}

	@Override
	public IMeasurementEnvironmentController connectToMEController(URI uri) {
		String controllerName = getControllerName(uri);
		String host = getHostUrl(uri);

		if (!isMECApplicationRunning(host)) {
			throw new RuntimeException("Host '" + host + "' is not running..");
		}

		String[] controller = RestMEConnector.getAvailableMEController(host);
		if (!Arrays.asList(controller).contains(controllerName)) {
			throw new RuntimeException("MEController '" + controllerName + "' not available at '" + host + "'");
		}

		IMeasurementEnvironmentController meController = new RestMECWrapper(host, controllerName);
		return meController;
	}

	/**
	 * Returns the first part of the path of the given URI.<br>
	 * Example: <code>http://localhost/<u>test</u>/ => test</code>
	 * 
	 * @param uri
	 * @return first part of the path
	 */
	private static String getControllerName(URI uri) {
		return uri.getPath().split("/")[1];
	}

	/**
	 * Returns the given URI without the path.<br>
	 * Example: <code><u>http://localhost/</u>test/ => http://localhost/</code>
	 * 
	 * @param uri
	 * @return URI without path
	 */
	private static String getHostUrl(URI uri) {
		String host = uri.getScheme() + "://" + uri.getHost();
		if (uri.getPort() != -1) {
			host += ":" + uri.getPort();
		}
		return host + "/";
	}
}

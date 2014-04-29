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
package org.sopeco.engine.measurementenvironment.connector;

import java.net.URI;
import java.util.Arrays;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.engine.measurementenvironment.rest.RestMECWrapper;
import org.sopeco.engine.measurementenvironment.rest.RestServices;
import org.sopeco.engine.measurementenvironment.rest.TransferPackage;

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

		Response response = ClientBuilder.newClient().target(url).request().get();

		if (response.getStatus() != Status.OK.getStatusCode()) {
			LOGGER.error("Request was not successful. Server returned: " + response.getStatus());
			return false;
		} else if (!response.hasEntity() || !response.readEntity(String.class).equals(RestServices.ALIVE_MESSAGE)) {
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

		Response response = ClientBuilder.newClient().target(url).request().get();

		if (response.getStatus() != Status.OK.getStatusCode()) {
			LOGGER.info("Request was not successful. Server returned: " + response.getStatus());
		} else if (response.hasEntity()) {
			TransferPackage result = response.readEntity(TransferPackage.class);
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

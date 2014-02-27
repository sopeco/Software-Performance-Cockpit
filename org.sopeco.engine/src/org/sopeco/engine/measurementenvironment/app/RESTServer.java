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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Marius Oehler
 * 
 */
final class RESTServer {

	private static final Logger LOGGER = LoggerFactory.getLogger(RESTServer.class);

	private static final String REST_SERVICE_PACKAGE = "org.sopeco.engine.measurementenvironment.rest";

	private static HttpServer server;

	private static boolean running;

	/**
	 * Starts the server, which wraps the MEController.
	 */
	public static synchronized void startREST(int pPort) {
		LOGGER.debug("Starting REST MEController on port {}.", pPort);

		try {
			if (running) {
				LOGGER.error("Rest Server is already started..");
				return;
			}

			ResourceConfig resourceConfig = new ResourceConfig();
			resourceConfig.packages(REST_SERVICE_PACKAGE);
			
			URI uri = new URI("http://localhost:" + pPort + "/");
			
			server = GrizzlyHttpServerFactory.createHttpServer(uri, resourceConfig);
			server.start();

			LOGGER.info("Available MEController:");
			for (String name : MECApplication.get().getControllerList()) {
				LOGGER.info("  > http://localhost:" + pPort + "/" + name);
			}

			running = true;
			LOGGER.debug("Server started.", pPort);
		} catch (URISyntaxException e) {
			LOGGER.error("URI is invalid.");
		} catch (IOException e) {
			LOGGER.error("Server start on port {} has failed: {}", pPort, e.getLocalizedMessage());
		}
	}

	private RESTServer() {
	}

}

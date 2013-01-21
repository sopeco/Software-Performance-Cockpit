package org.sopeco.engine.measurementenvironment.app;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.net.httpserver.HttpServer;

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

			PackagesResourceConfig rescConfig = new PackagesResourceConfig(REST_SERVICE_PACKAGE);
			server = HttpServerFactory.create("http://localhost:" + pPort + "/", rescConfig);
			server.start();

			LOGGER.info("Available MEController:");
			for (String name : MECApplication.get().getControllerList()) {
				LOGGER.info("  > http://localhost:" + pPort + "/" + name);
			}

			running = true;
			LOGGER.debug("Server started.", pPort);
		} catch (IOException e) {
			LOGGER.error("Server start on port {} has failed: {}", pPort, e.getLocalizedMessage());
		}
	}

	private RESTServer() {
	}

}

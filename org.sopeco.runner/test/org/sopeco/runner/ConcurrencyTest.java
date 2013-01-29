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
package org.sopeco.runner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URI;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import org.junit.BeforeClass;
import org.junit.Test;
import org.sopeco.config.Configuration;
import org.sopeco.config.IConfiguration;
import org.sopeco.config.exception.ConfigurationException;
import org.sopeco.engine.EngineFactory;
import org.sopeco.engine.IEngine;
import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.engine.measurementenvironment.app.MECApplication;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;

/**
 * Tests concurrent usage of ME Controller.
 * 
 * @author Alexander Wert
 * 
 */
/* CHECKSTYLE:OFF*/
public class ConcurrencyTest {
	private static final String ME_URI = "rmi://localhost:1098/DummyMEController";

	private static IMeasurementEnvironmentController meController;
	private static final String SESSION_ID_1 = "testId";
	private static final String SESSION_ID_2 = "testId2";

	private static boolean exceptionThrown = false;

	/**
	 * Start ME Controller.
	 */
	@BeforeClass
	public static void startMEController() {
		try {
			// use this project folder as root.
			Configuration.getSessionSingleton(ConcurrencyTest.class, SESSION_ID_1);
			Configuration.getSessionSingleton(ConcurrencyTest.class, SESSION_ID_2);
			meController = new DummyMEController();
			URI meURI = URI.create(ME_URI);

			MECApplication.get().startRemoteMEController(meController, meURI);

			LocateRegistry.getRegistry(meURI.getHost(), meURI.getPort());

			assertNotNull(Naming.lookup(meURI.toString()));
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	/**
	 * Setup configuration.
	 */
	@BeforeClass
	public static void initialize() {
		Configuration.getSessionSingleton(ConcurrencyTest.class, SESSION_ID_1).setProperty(
				"sopeco.config.persistence.dbtype", "InMemory");
		Configuration.getSessionSingleton(ConcurrencyTest.class, SESSION_ID_2).setProperty(
				"sopeco.config.persistence.dbtype", "InMemory");

	}

	/**
	 * Test concurrent execution.
	 * 
	 * @throws InterruptedException
	 *             error during concurrent execution
	 */
	@Test
	public void testConcurrentExecution() throws InterruptedException {

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					ScenarioDefinition scenarioDefinition = DummyModelBuilder.getReferenceScenariodefinition();
					IConfiguration config = Configuration.getSessionSingleton(ConcurrencyTest.class, SESSION_ID_1);
					try {
						config.setMeasurementControllerURI(ME_URI);
					} catch (ConfigurationException e) {
						fail();
					}
					config.setProperty("sopeco.config.persistence.dbtype", "InMemory");
					config.setProperty(IConfiguration.CONF_APP_NAME, "sopeco");
					IEngine engine = EngineFactory.getInstance().createEngine(SESSION_ID_1);
					engine.run(scenarioDefinition);
				} catch (Exception e) {
					Throwable error = e;
					while (error != null) {
						if (error.getMessage().contains("MEController in use")) {
							exceptionThrown = true;
							break;
						}
						error = error.getCause();
					}

				}

			}
		});

		Thread t2 = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					ScenarioDefinition scenarioDefinition = DummyModelBuilder.getAnotherScenariodefinition();
					IConfiguration config = Configuration.getSessionSingleton(ConcurrencyTest.class, SESSION_ID_2);
					try {
						config.setMeasurementControllerURI(ME_URI);
					} catch (ConfigurationException e) {
						fail();
					}
					config.setProperty("sopeco.config.persistence.dbtype", "InMemory");
					config.setProperty(IConfiguration.CONF_APP_NAME, "sopeco2");
					IEngine engine = EngineFactory.getInstance().createEngine(SESSION_ID_2);
					engine.run(scenarioDefinition);
				} catch (Exception e) {
					Throwable error = e;
					while (error != null) {
						if (error.getMessage().contains("MEController in use")) {
							exceptionThrown = true;
							break;
						}
						error = error.getCause();
					}
				}

			}
		});

		t.start();
		t2.start();

		t.join();
		t2.join();

		assertTrue(exceptionThrown);

	}
}
/* CHECKSTYLE:ON*/
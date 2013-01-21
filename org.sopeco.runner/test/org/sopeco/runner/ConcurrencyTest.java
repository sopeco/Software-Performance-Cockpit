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

public class ConcurrencyTest {
	private static final String ME_URI = "rmi://localhost:1098/DummyMEController";

	private static IMeasurementEnvironmentController meController;
	private static final String SESSION_ID_1 = "testId";
	private static final String SESSION_ID_2 = "testId2";

	private static boolean exceptionThrown = false;

	@BeforeClass
	public static void startMEController() {
		try {
			// use this project folder as root.
			Configuration.getSessionSingleton(NewVersionTest.class, SESSION_ID_1);
			Configuration.getSessionSingleton(NewVersionTest.class, SESSION_ID_2);
			meController = new DummyMEController();
			URI meURI = URI.create(ME_URI);

			MECApplication.get().startRemoteMEController(meController, meURI);
			
			LocateRegistry.getRegistry(meURI.getHost(), meURI.getPort());

			assertNotNull(Naming.lookup(meURI.toString()));
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	@BeforeClass
	public static void initialize() {
		Configuration.getSessionSingleton(NewVersionTest.class, SESSION_ID_1).setProperty(
				"sopeco.config.persistence.dbtype", "InMemory");
		Configuration.getSessionSingleton(NewVersionTest.class, SESSION_ID_2).setProperty(
				"sopeco.config.persistence.dbtype", "InMemory");

	}

	@Test
	public void testConcurrentExecution() throws InterruptedException {

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					ScenarioDefinition scenarioDefinition = DummyModelBuilder.getReferenceScenariodefinition();
					IConfiguration config = Configuration.getSessionSingleton(NewVersionTest.class, SESSION_ID_1);
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
					if (e.getMessage().contains("MEController in use")) {
						exceptionThrown = true;
					}

				}

			}
		});

		Thread t2 = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					ScenarioDefinition scenarioDefinition = DummyModelBuilder.getAnotherScenariodefinition();
					IConfiguration config = Configuration.getSessionSingleton(NewVersionTest.class, SESSION_ID_2);
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
					if (e.getMessage().contains("MEController in use")) {
						exceptionThrown = true;
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

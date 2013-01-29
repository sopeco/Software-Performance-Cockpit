package org.sopeco.runner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URI;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sopeco.config.Configuration;
import org.sopeco.config.IConfiguration;
import org.sopeco.config.exception.ConfigurationException;
import org.sopeco.engine.EngineFactory;
import org.sopeco.engine.IEngine;
import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.engine.measurementenvironment.app.MECApplication;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.PersistenceProviderFactory;
import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.ScenarioInstance;
import org.sopeco.persistence.entities.definition.MeasurementSpecification;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;
import org.sopeco.persistence.exceptions.DataNotFoundException;

public class ExecutionTest {

	private static final String ME_URI = "rmi://localhost:1098/DummyMEController";

	private static final String CHANGE_HANDLILNG_MODE = "archive";
	private static IPersistenceProvider provider = null;
	private static IMeasurementEnvironmentController meController;
	private static final String SESSION_ID = "testId";

	@BeforeClass
	public static void startMEController() {
		try {
			// use this project folder as root.
			Configuration.getSessionSingleton(ExecutionTest.class, SESSION_ID);

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
		Configuration.getSessionSingleton(ExecutionTest.class, SESSION_ID).setProperty(
				"sopeco.config.persistence.dbtype", "InMemory");
		provider = PersistenceProviderFactory.getInstance().getPersistenceProvider(SESSION_ID);

	}

	@Before
	public void cleanPersistence() throws DataNotFoundException {
		for (ScenarioInstance si : provider.loadAllScenarioInstances()) {
			provider.remove(si);
		}
	}

	@Test
	public void testExecution() {
		try {
			ScenarioDefinition referenceDefinition = DummyModelBuilder.getReferenceScenariodefinition();

			runExperimentsWithScenarioDefinition(referenceDefinition);

			checkScenario(DummyModelBuilder.SCENARIO_NAME, 1);
			checkExperimentSeries(DummyModelBuilder.EXPERIMENT_SERIES_1, 1);
			checkInitializations(DummyModelBuilder.MEASUREMENT_SPEC_1, 1);

			// ********** Run 2
			runExperimentsWithScenarioDefinition(referenceDefinition);

			checkScenario(DummyModelBuilder.SCENARIO_NAME, 1);
			checkExperimentSeries(DummyModelBuilder.EXPERIMENT_SERIES_1, 2);
			checkInitializations(DummyModelBuilder.MEASUREMENT_SPEC_1, 1);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	private void runExperimentsWithScenarioDefinition(ScenarioDefinition scenarioDefinition)
			throws ConfigurationException {

		IConfiguration config = Configuration.getSessionSingleton(ExecutionTest.class, SESSION_ID);
		config.setProperty(IConfiguration.CONF_DEFINITION_CHANGE_HANDLING_MODE, CHANGE_HANDLILNG_MODE);
		config.setMeasurementControllerURI(ME_URI);
		config.setProperty("sopeco.config.persistence.dbtype", "InMemory");
		// config.setProperty(IConfiguration.CONF_PLUGINS_FOLDER, "plugins");
		config.setProperty(IConfiguration.CONF_APP_NAME, "sopeco");
		IEngine engine = EngineFactory.getInstance().createEngine(SESSION_ID);
		engine.run(scenarioDefinition);
	}

	private void checkScenario(String scenarioName, int numOfExpSeries) {
		List<ScenarioInstance> scenarios = null;
		try {
			scenarios = provider.loadAllScenarioInstances();
		} catch (DataNotFoundException e) {
			fail(e.getMessage());
		}
		assertTrue(scenarios.size() == 1);
		assertEquals(scenarios.get(0).getScenarioDefinition().getScenarioName(), scenarioName);

		List<ExperimentSeries> experimentSeries = scenarios.get(0).getExperimentSeriesList();
		assertTrue(experimentSeries.size() == numOfExpSeries);
	}

	private void checkExperimentSeries(String seriesName, int numOfRuns) {
		ExperimentSeries es = null;
		try {
			es = provider.loadExperimentSeries(seriesName, DummyModelBuilder.SCENARIO_NAME, ME_URI);
			assertNotNull(es.getExperimentSeriesRuns().get(0).getSuccessfulResultDataSet());

		} catch (DataNotFoundException e) {
			fail(e.getMessage());
		}
		List<ExperimentSeriesRun> runs = es.getExperimentSeriesRuns();
		assertTrue(runs.size() == numOfRuns);
	}

	private void checkInitializations(String measurementSpecName, int numOfInitializations) {
		List<ScenarioInstance> scenarios = null;
		try {
			scenarios = provider.loadAllScenarioInstances();
		} catch (DataNotFoundException e) {
			fail(e.getMessage());
		}
		MeasurementSpecification ms = scenarios.get(0).getScenarioDefinition()
				.getMeasurementSpecification(measurementSpecName);
		assertNotNull(ms);
		assertTrue(ms.getInitializationAssignemts().size() == numOfInitializations);

	}
}

package org.sopeco.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URI;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.config.Configuration;
import org.sopeco.config.IConfiguration;
import org.sopeco.config.exception.ConfigurationException;
import org.sopeco.engine.EngineFactory;
import org.sopeco.engine.IEngine;
import org.sopeco.engine.measurementenvironment.DummyMEController;
import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.engine.measurementenvironment.rmi.RmiMEConnector;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.PersistenceProviderFactory;
import org.sopeco.persistence.dataset.util.ParameterType;
import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.ScenarioInstance;
import org.sopeco.persistence.entities.definition.MeasurementSpecification;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterRole;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;
import org.sopeco.persistence.exceptions.DataNotFoundException;
import org.sopeco.persistence.util.ScenarioDefinitionBuilder;
import org.sopeco.persistence.util.ScenarioDefinitionBuilder.AssignmentType;

public class RunTest {
	Logger logger = LoggerFactory.getLogger(RunTest.class);
	private static final String SCENARIO_NAME = "TestScenario";
	private static final String INPUT_PARAM_1 = "DummyInput_1";
	private ParameterDefinition dummyInputParam_1 = null;
	private static final String INPUT_PARAM_2 = "DummyInput_2";
	private ParameterDefinition dummyInputParam_2 = null;
	private static final String OUTPUT_PARAM = "DummyOutput";
	private ParameterDefinition dummyOutputParam = null;

	private static final String MEASUREMENT_SPEC_1 = "DummyMeasurementSpec_1";
	private static final String MEASUREMENT_SPEC_2 = "DummyMeasurementSpec_2";

	private static final String EXPERIMENT_SERIES_1 = "ExperimentSeries_1";
	private static final String EXPERIMENT_SERIES_2 = "ExperimentSeries_2";
	private static final int numbetOfRuns = 2;

	private static Map<String, String> linearVariationConfig;

	private static final String ME_URI = "rmi://localhost:1099/DummyMEController";

	private static final String MODEL_CHANGE_HANDLILNG_MODE = "newVersion";

	private static IPersistenceProvider provider = null;
	private static IMeasurementEnvironmentController meController;

	@BeforeClass
	public static void startMEController() {
		try {
			// use this project folder as root.
			Configuration.getSingleton(RunTest.class);

			meController = new DummyMEController();
			URI meURI = URI.create(ME_URI);

			RmiMEConnector.startRemoteMEController(meController, meURI);
			LocateRegistry.getRegistry(meURI.getHost(), meURI.getPort());

			assertNotNull(Naming.lookup(meURI.toString()));
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	@BeforeClass
	public static void initialize() {
		provider = PersistenceProviderFactory.getPersistenceProvider();
		linearVariationConfig = new HashMap<String, String>();
		linearVariationConfig.put("min", "1");
		linearVariationConfig.put("step", "1");
		linearVariationConfig.put("max", "2");
	}

	@Before
	public void cleanPersistence() throws DataNotFoundException {
		for (ScenarioInstance si : provider.loadAllScenarioInstances()) {
			provider.remove(si);
		}
	}

	/**
	 * Tests the execution of some runs with equal scenario definitions
	 */
	@Test
	public void testRunsWithSameSD() {
		try {
			ScenarioDefinition referenceDefinition = getReferenceScenariodefinition();
			ScenarioDefinition newDefinition = getReferenceScenariodefinition();

			// ********** Run 1
			runExperimentsWithScenarioDefinition(referenceDefinition);

			checkScenario(SCENARIO_NAME, 1);
			checkExperimentSeries(EXPERIMENT_SERIES_1, 0L, 1);
			checkInitializations(MEASUREMENT_SPEC_1, 1);

			// ********** Run 2
			runExperimentsWithScenarioDefinition(newDefinition);

			checkScenario(SCENARIO_NAME, 1);
			checkExperimentSeries(EXPERIMENT_SERIES_1, 0L, 2);
			checkInitializations(MEASUREMENT_SPEC_1, 1);
			// ********** Run 3
			runExperimentsWithScenarioDefinition(newDefinition);

			checkScenario(SCENARIO_NAME, 1);
			checkExperimentSeries(EXPERIMENT_SERIES_1, 0L, 3);
			checkInitializations(MEASUREMENT_SPEC_1, 1);
		} catch (Exception e) {
			logger.error("Unit Test failed! Reason: {}", e);
			fail(e.getMessage());
		}
	}

	/**
	 * Tests the execution of some runs with different measurement
	 * specifications and experiment series definitions
	 */
	@Test
	public void testRunsWithDifferentMSAndESD() {
		try {
			ScenarioDefinition referenceDefinition = getReferenceScenariodefinition();
			ScenarioDefinition newDefinition = getScenariodefinitionWithDifferentMSAndESD();

			checkNewExperimentSeriesExpectation(referenceDefinition, newDefinition);
			checkInitializations(MEASUREMENT_SPEC_1, 1);
			checkInitializations(MEASUREMENT_SPEC_2, 1);
		} catch (Exception e) {
			logger.error("Unit Test failed! Reason: {}", e);
			fail(e.getMessage());
		}
	}

	/**
	 * Tests the execution of some runs with different measurement
	 * specifications and same series definitions
	 */
	@Test
	public void testRunsWithDifferentMSAndSameESD() {
		try {
			ScenarioDefinition referenceDefinition = getReferenceScenariodefinition();
			ScenarioDefinition newDefinition = getScenariodefinitionWithDifferentMSAndSameESD();

			// ********** Run 1
			runExperimentsWithScenarioDefinition(referenceDefinition);

			checkScenario(SCENARIO_NAME, 1);
			checkExperimentSeries(EXPERIMENT_SERIES_1, 0L, 1);
			checkInitializations(MEASUREMENT_SPEC_1, 1);

			// ********** Run 2
			runExperimentsWithScenarioDefinition(newDefinition);

			checkScenario(SCENARIO_NAME, 1);
			checkExperimentSeries(EXPERIMENT_SERIES_1, 0L, 2);
			checkInitializations(MEASUREMENT_SPEC_1, 1);
			// ********** Run 3
			runExperimentsWithScenarioDefinition(newDefinition);

			checkScenario(SCENARIO_NAME, 1);
			checkExperimentSeries(EXPERIMENT_SERIES_1, 0L, 3);
			checkInitializations(MEASUREMENT_SPEC_1, 1);

			// ********** Run 4
			runExperimentsWithScenarioDefinition(referenceDefinition);

			checkScenario(SCENARIO_NAME, 1);
			checkExperimentSeries(EXPERIMENT_SERIES_1, 0L, 4);
			checkInitializations(MEASUREMENT_SPEC_1, 1);

			// ********** Run 45
			runExperimentsWithScenarioDefinition(newDefinition);

			checkScenario(SCENARIO_NAME, 1);
			checkExperimentSeries(EXPERIMENT_SERIES_1, 0L, 5);
			checkInitializations(MEASUREMENT_SPEC_1, 1);
		} catch (Exception e) {
			logger.error("Unit Test failed! Reason: {}", e);
			fail(e.getMessage());
		}

	}

	/**
	 * Tests the execution of some runs with same measurement specifications and
	 * different series definitions
	 */
	@Test
	public void testRunsWithDifferentESD() {
		try {
			ScenarioDefinition referenceDefinition = getReferenceScenariodefinition();
			ScenarioDefinition newDefinition = getScenariodefinitionWithDifferentESD();

			checkNewExperimentSeriesExpectation(referenceDefinition, newDefinition);
			checkInitializations(MEASUREMENT_SPEC_1, 1);
		} catch (Exception e) {
			logger.error("Unit Test failed! Reason: {}", e);
			fail(e.getMessage());
		}
	}

	/**
	 * Tests the execution of some runs with same measurement specifications and
	 * changed series definitions
	 */
	@Test
	public void testRunsWithChangedESD() {
		try {
			ScenarioDefinition referenceDefinition = getReferenceScenariodefinition();
			ScenarioDefinition newDefinition = getScenariodefinitionWithModifiedESD();

			// ********** Run 1
			runExperimentsWithScenarioDefinition(referenceDefinition);

			checkScenario(SCENARIO_NAME, 1);

			checkExperimentSeriesVersions(EXPERIMENT_SERIES_1, 0L, 1);
			checkExperimentSeries(EXPERIMENT_SERIES_1, 0L, 1);

			// ********** Run 2
			runExperimentsWithScenarioDefinition(newDefinition);

			checkScenario(SCENARIO_NAME, 2);

			checkExperimentSeriesVersions(EXPERIMENT_SERIES_1, 1L, 2);
			checkExperimentSeries(EXPERIMENT_SERIES_1, 0L, 1);
			checkExperimentSeries(EXPERIMENT_SERIES_1, 1L, 1);

			// ********** Run 3
			runExperimentsWithScenarioDefinition(newDefinition);

			checkScenario(SCENARIO_NAME, 2);

			checkExperimentSeriesVersions(EXPERIMENT_SERIES_1, 1L, 2);
			checkExperimentSeries(EXPERIMENT_SERIES_1, 0L, 1);
			checkExperimentSeries(EXPERIMENT_SERIES_1, 1L, 2);
			// ********** Run 4
			runExperimentsWithScenarioDefinition(referenceDefinition);

			checkScenario(SCENARIO_NAME, 2);

			checkExperimentSeriesVersions(EXPERIMENT_SERIES_1, 1L, 2);
			checkExperimentSeries(EXPERIMENT_SERIES_1, 0L, 2);
			checkExperimentSeries(EXPERIMENT_SERIES_1, 1L, 2);
			// ********** Run 5
			runExperimentsWithScenarioDefinition(newDefinition);

			checkScenario(SCENARIO_NAME, 2);

			checkExperimentSeriesVersions(EXPERIMENT_SERIES_1, 1L, 2);
			checkExperimentSeries(EXPERIMENT_SERIES_1, 0L, 2);
			checkExperimentSeries(EXPERIMENT_SERIES_1, 1L, 3);

			checkInitializations(MEASUREMENT_SPEC_1, 1);
		} catch (Exception e) {
			logger.error("Unit Test failed! Reason: {}", e);
			fail(e.getMessage());
		}
	}

	/**
	 * Tests the execution of some runs with same measurement specifications,
	 * same series definitions and different initialization assignments
	 */
	@Test
	public void testRunsWithDifferentInitializations() {
		try {
			ScenarioDefinition referenceDefinition = getReferenceScenariodefinition();
			ScenarioDefinition newDefinition = getScenariodefinitionWithDifferentInitializations();

			// ********** Run 1
			runExperimentsWithScenarioDefinition(referenceDefinition);

			checkScenario(SCENARIO_NAME, 1);

			checkExperimentSeriesVersions(EXPERIMENT_SERIES_1, 0L, 1);
			checkExperimentSeries(EXPERIMENT_SERIES_1, 0L, 1);
			checkInitializations(MEASUREMENT_SPEC_1, 1);

			// ********** Run 2
			runExperimentsWithScenarioDefinition(newDefinition);

			checkScenario(SCENARIO_NAME, 1);

			checkExperimentSeriesVersions(EXPERIMENT_SERIES_1, 0L, 1);
			checkExperimentSeries(EXPERIMENT_SERIES_1, 0L, 2);
			checkInitializations(MEASUREMENT_SPEC_1, 2);

			// ********** Run 3
			runExperimentsWithScenarioDefinition(newDefinition);

			checkScenario(SCENARIO_NAME, 1);

			checkExperimentSeriesVersions(EXPERIMENT_SERIES_1, 0L, 1);
			checkExperimentSeries(EXPERIMENT_SERIES_1, 0L, 3);
			checkInitializations(MEASUREMENT_SPEC_1, 2);

			// ********** Run 4
			runExperimentsWithScenarioDefinition(referenceDefinition);

			checkScenario(SCENARIO_NAME, 1);

			checkExperimentSeriesVersions(EXPERIMENT_SERIES_1, 0L, 1);
			checkExperimentSeries(EXPERIMENT_SERIES_1, 0L, 4);
			checkInitializations(MEASUREMENT_SPEC_1, 2);

			// ********** Run 5
			runExperimentsWithScenarioDefinition(newDefinition);

			checkScenario(SCENARIO_NAME, 1);

			checkExperimentSeriesVersions(EXPERIMENT_SERIES_1, 0L, 1);
			checkExperimentSeries(EXPERIMENT_SERIES_1, 0L, 5);
			checkInitializations(MEASUREMENT_SPEC_1, 2);

		} catch (Exception e) {
			logger.error("Unit Test failed! Reason: {}", e);
			fail(e.getMessage());
		}
	}

	/**
	 * Tests the execution of some runs with same measurement specifications,
	 * different series definitions and different initialization assignments
	 */
	@Test
	public void testRunsWithDifferentInitializationsAndESD() {
		try {
			ScenarioDefinition referenceDefinition = getReferenceScenariodefinition();
			ScenarioDefinition newDefinition = getScenariodefinitionWithDifferentInitializationsAndESD();

			checkNewExperimentSeriesExpectation(referenceDefinition, newDefinition);
			checkInitializations(MEASUREMENT_SPEC_1, 2);
		} catch (Exception e) {
			logger.error("Unit Test failed! Reason: {}", e);
			fail(e.getMessage());
		}
	}

	private void checkNewExperimentSeriesExpectation(ScenarioDefinition referenceDefinition, ScenarioDefinition newDefinition) throws ConfigurationException {
		// ********** Run 1
		runExperimentsWithScenarioDefinition(referenceDefinition);

		checkScenario(SCENARIO_NAME, 1);

		checkExperimentSeries(EXPERIMENT_SERIES_1, 0L, 1);
		checkExperimentSeriesVersions(EXPERIMENT_SERIES_1, 0L, 1);

		// ********** Run 2
		runExperimentsWithScenarioDefinition(newDefinition);

		checkScenario(SCENARIO_NAME, 2);

		checkExperimentSeries(EXPERIMENT_SERIES_1, 0L, 1);
		checkExperimentSeriesVersions(EXPERIMENT_SERIES_1, 0L, 1);
		checkExperimentSeries(EXPERIMENT_SERIES_2, 0L, 1);
		checkExperimentSeriesVersions(EXPERIMENT_SERIES_2, 0L, 1);

		// ********** Run 3
		runExperimentsWithScenarioDefinition(newDefinition);

		checkScenario(SCENARIO_NAME, 2);

		checkExperimentSeries(EXPERIMENT_SERIES_1, 0L, 1);
		checkExperimentSeriesVersions(EXPERIMENT_SERIES_1, 0L, 1);
		checkExperimentSeries(EXPERIMENT_SERIES_2, 0L, 2);
		checkExperimentSeriesVersions(EXPERIMENT_SERIES_2, 0L, 1);

		// ********** Run 4
		runExperimentsWithScenarioDefinition(referenceDefinition);

		checkScenario(SCENARIO_NAME, 2);

		checkExperimentSeries(EXPERIMENT_SERIES_1, 0L, 2);
		checkExperimentSeriesVersions(EXPERIMENT_SERIES_1, 0L, 1);
		checkExperimentSeries(EXPERIMENT_SERIES_2, 0L, 2);
		checkExperimentSeriesVersions(EXPERIMENT_SERIES_2, 0L, 1);

		// ********** Run 5
		runExperimentsWithScenarioDefinition(newDefinition);

		checkScenario(SCENARIO_NAME, 2);

		checkExperimentSeries(EXPERIMENT_SERIES_1, 0L, 2);
		checkExperimentSeriesVersions(EXPERIMENT_SERIES_1, 0L, 1);
		checkExperimentSeries(EXPERIMENT_SERIES_2, 0L, 3);
		checkExperimentSeriesVersions(EXPERIMENT_SERIES_2, 0L, 1);
	}

	private void checkExperimentSeries(String seriesName, Long version, int numOfRuns) {
		ExperimentSeries es = null;
		try {
			es = provider.loadExperimentSeries(seriesName, version, SCENARIO_NAME, ME_URI);
		} catch (DataNotFoundException e) {
			fail(e.getMessage());
		}
		List<ExperimentSeriesRun> runs = es.getExperimentSeriesRuns();
		assertTrue(runs.size() == numOfRuns);
	}

	private void checkExperimentSeriesVersions(String seriesName, Long latestVersion, int numOfVersions) {
		List<ExperimentSeries> esList = null;
		try {
			esList = provider.loadAllExperimentSeries(seriesName, SCENARIO_NAME, ME_URI);
		} catch (DataNotFoundException e) {
			fail(e.getMessage());
		}
		assertTrue(esList.size() == numOfVersions);

		ExperimentSeries es = null;
		try {
			es = provider.loadExperimentSeries(seriesName, SCENARIO_NAME, ME_URI);
		} catch (DataNotFoundException e) {
			fail(e.getMessage());
		}
		assertEquals(es.getVersion(), latestVersion);
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

	private void checkInitializations(String measurementSpecName, int numOfInitializations) {
		List<ScenarioInstance> scenarios = null;
		try {
			scenarios = provider.loadAllScenarioInstances();
		} catch (DataNotFoundException e) {
			fail(e.getMessage());
		}
		MeasurementSpecification ms = scenarios.get(0).getScenarioDefinition().getMeasurementSpecification(measurementSpecName);
		assertNotNull(ms);
		assertTrue(ms.getInitializationAssignemts().size() == numOfInitializations);

	}

	private void runExperimentsWithScenarioDefinition(ScenarioDefinition scenarioDefinition) throws ConfigurationException {

		IConfiguration config = Configuration.getSingleton(RunTest.class);
		config.setProperty(IConfiguration.CONF_MODEL_CHANGE_HANDLING_MODE, MODEL_CHANGE_HANDLILNG_MODE);
		config.setMeasurementControllerURI(ME_URI);
		config.setProperty(IConfiguration.CONF_PLUGINS_FOLDER, "plugins");
		config.setProperty(IConfiguration.CONF_APP_NAME, "sopeco");
		IEngine engine = EngineFactory.INSTANCE.createEngine();
		engine.run(scenarioDefinition);
	}

	private ScenarioDefinition getReferenceScenariodefinition() {
		ScenarioDefinitionBuilder builder = new ScenarioDefinitionBuilder(SCENARIO_NAME);
		createMeasurementEnvironment(builder);

		builder.createMeasurementSpecification(MEASUREMENT_SPEC_1);
		createESD_1(builder);
		builder.createConstantValueAssignment(AssignmentType.Initialization, dummyInputParam_2, "1");

		return builder.getScenarioDefinition();
	}

	private ScenarioDefinition getScenariodefinitionWithDifferentMSAndESD() {
		ScenarioDefinitionBuilder builder = new ScenarioDefinitionBuilder(SCENARIO_NAME);
		createMeasurementEnvironment(builder);

		builder.createMeasurementSpecification(MEASUREMENT_SPEC_2);
		createESD_2(builder);
		builder.createConstantValueAssignment(AssignmentType.Initialization, dummyInputParam_2, "1");

		return builder.getScenarioDefinition();
	}

	private ScenarioDefinition getScenariodefinitionWithDifferentMSAndSameESD() {
		ScenarioDefinitionBuilder builder = new ScenarioDefinitionBuilder(SCENARIO_NAME);
		createMeasurementEnvironment(builder);

		builder.createMeasurementSpecification(MEASUREMENT_SPEC_2);
		createESD_1(builder);
		builder.createConstantValueAssignment(AssignmentType.Initialization, dummyInputParam_2, "1");

		return builder.getScenarioDefinition();
	}

	private ScenarioDefinition getScenariodefinitionWithDifferentESD() {
		ScenarioDefinitionBuilder builder = new ScenarioDefinitionBuilder(SCENARIO_NAME);
		createMeasurementEnvironment(builder);

		builder.createMeasurementSpecification(MEASUREMENT_SPEC_1);
		createESD_2(builder);
		builder.createConstantValueAssignment(AssignmentType.Initialization, dummyInputParam_2, "1");

		return builder.getScenarioDefinition();
	}

	private ScenarioDefinition getScenariodefinitionWithModifiedESD() {
		ScenarioDefinitionBuilder builder = new ScenarioDefinitionBuilder(SCENARIO_NAME);
		createMeasurementEnvironment(builder);

		builder.createMeasurementSpecification(MEASUREMENT_SPEC_1);
		createModifiedESD_1(builder);
		builder.createConstantValueAssignment(AssignmentType.Initialization, dummyInputParam_2, "1");

		return builder.getScenarioDefinition();
	}

	private ScenarioDefinition getScenariodefinitionWithDifferentInitializations() {
		ScenarioDefinitionBuilder builder = new ScenarioDefinitionBuilder(SCENARIO_NAME);
		createMeasurementEnvironment(builder);

		builder.createMeasurementSpecification(MEASUREMENT_SPEC_1);
		createESD_1(builder);
		builder.createConstantValueAssignment(AssignmentType.Initialization, dummyInputParam_2, "2");

		return builder.getScenarioDefinition();
	}

	private ScenarioDefinition getScenariodefinitionWithDifferentInitializationsAndESD() {
		ScenarioDefinitionBuilder builder = new ScenarioDefinitionBuilder(SCENARIO_NAME);
		createMeasurementEnvironment(builder);

		builder.createMeasurementSpecification(MEASUREMENT_SPEC_1);
		createESD_2(builder);
		builder.createConstantValueAssignment(AssignmentType.Initialization, dummyInputParam_2, "2");

		return builder.getScenarioDefinition();
	}

	private void createESD_1(ScenarioDefinitionBuilder builder) {
		builder.createExperimentSeriesDefinition(EXPERIMENT_SERIES_1);
		builder.createNumberOfRunsCondition(numbetOfRuns);
		builder.createDynamicValueAssignment(AssignmentType.Experiment, "Linear Numeric Variation", dummyInputParam_1, linearVariationConfig);

		builder.createExplorationStrategy("Full Exploration Strategy", Collections.EMPTY_MAP);
	}

	private void createESD_2(ScenarioDefinitionBuilder builder) {
		builder.createExperimentSeriesDefinition(EXPERIMENT_SERIES_2);
		builder.createNumberOfRunsCondition(numbetOfRuns);
		builder.createDynamicValueAssignment(AssignmentType.Experiment, "Linear Numeric Variation", dummyInputParam_1, linearVariationConfig);
		builder.createExplorationStrategy("Full Exploration Strategy", Collections.EMPTY_MAP);
	}

	private void createModifiedESD_1(ScenarioDefinitionBuilder builder) {
		builder.createExperimentSeriesDefinition(EXPERIMENT_SERIES_1);
		builder.createNumberOfRunsCondition(numbetOfRuns);
		builder.createConstantValueAssignment(AssignmentType.Experiment, dummyInputParam_1, "1");
		builder.createExplorationStrategy("Full Exploration Strategy", Collections.EMPTY_MAP);
	}

	private void createMeasurementEnvironment(ScenarioDefinitionBuilder builder) {
		builder.createNewNamespace("default");
		dummyInputParam_1 = builder.createParameter(INPUT_PARAM_1, ParameterType.INTEGER, ParameterRole.INPUT);
		dummyInputParam_2 = builder.createParameter(INPUT_PARAM_2, ParameterType.INTEGER, ParameterRole.INPUT);
		dummyOutputParam = builder.createParameter(OUTPUT_PARAM, ParameterType.INTEGER, ParameterRole.OBSERVATION);
	}
}

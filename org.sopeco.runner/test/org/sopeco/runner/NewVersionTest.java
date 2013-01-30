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
///**
// * Copyright (c) 2013 SAP
// * All rights reserved.
// *
// * Redistribution and use in source and binary forms, with or without
// * modification, are permitted provided that the following conditions are met:
// *     * Redistributions of source code must retain the above copyright
// *       notice, this list of conditions and the following disclaimer.
// *     * Redistributions in binary form must reproduce the above copyright
// *       notice, this list of conditions and the following disclaimer in the
// *       documentation and/or other materials provided with the distribution.
// *     * Neither the name of the SAP nor the
// *       names of its contributors may be used to endorse or promote products
// *       derived from this software without specific prior written permission.
// *
// * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
// * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
// * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
// * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
// * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
// * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
// * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
// */
//package org.sopeco.runner;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertTrue;
//import static org.junit.Assert.fail;
//
//import java.net.URI;
//import java.rmi.Naming;
//import java.rmi.registry.LocateRegistry;
//import java.util.List;
//
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.sopeco.config.Configuration;
//import org.sopeco.config.IConfiguration;
//import org.sopeco.config.exception.ConfigurationException;
//import org.sopeco.engine.EngineFactory;
//import org.sopeco.engine.IEngine;
//import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
//import org.sopeco.engine.measurementenvironment.app.MECApplication;
//import org.sopeco.engine.measurementenvironment.connector.RmiMEConnector;
//import org.sopeco.persistence.IPersistenceProvider;
//import org.sopeco.persistence.PersistenceProviderFactory;
//import org.sopeco.persistence.entities.ExperimentSeries;
//import org.sopeco.persistence.entities.ExperimentSeriesRun;
//import org.sopeco.persistence.entities.ScenarioInstance;
//import org.sopeco.persistence.entities.definition.MeasurementSpecification;
//import org.sopeco.persistence.entities.definition.ScenarioDefinition;
//import org.sopeco.persistence.exceptions.DataNotFoundException;
//
//public class NewVersionTest {
//	Logger logger = LoggerFactory.getLogger(NewVersionTest.class);
//
//	private static final String ME_URI = "rmi://localhost:1098/DummyMEController";
//
//	private static final String MODEL_CHANGE_HANDLILNG_MODE = "newVersion";
//	private static IPersistenceProvider provider = null;
//	private static IMeasurementEnvironmentController meController;
//	private static final String SESSION_ID = "testId";
//
//	@BeforeClass
//	public static void startMEController() {
//		try {
//			// use this project folder as root.
//			Configuration.getSessionSingleton(NewVersionTest.class, SESSION_ID);
//
//			meController = new DummyMEController();
//			URI meURI = URI.create(ME_URI);
//
//			MECApplication.get().startRemoteMEController(meController, meURI);
//			LocateRegistry.getRegistry(meURI.getHost(), meURI.getPort());
//
//			assertNotNull(Naming.lookup(meURI.toString()));
//		} catch (Exception e) {
//			fail(e.toString());
//		}
//	}
//
//	@BeforeClass
//	public static void initialize() {
//		Configuration.getSessionSingleton(NewVersionTest.class, SESSION_ID).setProperty("sopeco.config.persistence.dbtype",
//				"InMemory");
//		provider = PersistenceProviderFactory.getInstance().getPersistenceProvider(SESSION_ID);
//
//	}
//
//	@Before
//	public void cleanPersistence() throws DataNotFoundException {
//		for (ScenarioInstance si : provider.loadAllScenarioInstances()) {
//			provider.remove(si);
//		}
//	}
//
//	/**
//	 * Tests the execution of some runs with equal scenario definitions
//	 */
//	@Test
//	public void testRunsWithSameSD() {
//		try {
//			ScenarioDefinition referenceDefinition = DummyModelBuilder.getReferenceScenariodefinition();
//			ScenarioDefinition newDefinition = DummyModelBuilder.getReferenceScenariodefinition();
//
//			// ********** Run 1
//			runExperimentsWithScenarioDefinition(referenceDefinition);
//
//			checkScenario(DummyModelBuilder.SCENARIO_NAME, 1);
//			checkExperimentSeries(DummyModelBuilder.EXPERIMENT_SERIES_1, 0L, 1);
//			checkInitializations(DummyModelBuilder.MEASUREMENT_SPEC_1, 1);
//
//			// ********** Run 2
//			runExperimentsWithScenarioDefinition(newDefinition);
//
//			checkScenario(DummyModelBuilder.SCENARIO_NAME, 1);
//			checkExperimentSeries(DummyModelBuilder.EXPERIMENT_SERIES_1, 0L, 2);
//			checkInitializations(DummyModelBuilder.MEASUREMENT_SPEC_1, 1);
//			// ********** Run 3
//			runExperimentsWithScenarioDefinition(newDefinition);
//
//			checkScenario(DummyModelBuilder.SCENARIO_NAME, 1);
//			checkExperimentSeries(DummyModelBuilder.EXPERIMENT_SERIES_1, 0L, 3);
//			checkInitializations(DummyModelBuilder.MEASUREMENT_SPEC_1, 1);
//		} catch (Exception e) {
//			logger.error("Unit Test failed! Reason: {}", e);
//			fail(e.getMessage());
//		}
//	}
//
//	/**
//	 * Tests the execution of some runs with different measurement
//	 * specifications and experiment series definitions
//	 */
//	@Test
//	public void testRunsWithDifferentMSAndESD() {
//		try {
//			ScenarioDefinition referenceDefinition = DummyModelBuilder.getReferenceScenariodefinition();
//			ScenarioDefinition newDefinition = DummyModelBuilder.getScenariodefinitionWithDifferentMSAndESD();
//
//			checkNewExperimentSeriesExpectation(referenceDefinition, newDefinition);
//			checkInitializations(DummyModelBuilder.MEASUREMENT_SPEC_1, 1);
//			checkInitializations(DummyModelBuilder.MEASUREMENT_SPEC_2, 1);
//		} catch (Exception e) {
//			logger.error("Unit Test failed! Reason: {}", e);
//			fail(e.getMessage());
//		}
//	}
//
//	/**
//	 * Tests the execution of some runs with different measurement
//	 * specifications and same series definitions
//	 */
//	@Test
//	public void testRunsWithDifferentMSAndSameESD() {
//		try {
//			ScenarioDefinition referenceDefinition = DummyModelBuilder.getReferenceScenariodefinition();
//			ScenarioDefinition newDefinition = DummyModelBuilder.getScenariodefinitionWithDifferentMSAndSameESD();
//
//			// ********** Run 1
//			runExperimentsWithScenarioDefinition(referenceDefinition);
//
//			checkScenario(DummyModelBuilder.SCENARIO_NAME, 1);
//			checkExperimentSeries(DummyModelBuilder.EXPERIMENT_SERIES_1, 0L, 1);
//			checkInitializations(DummyModelBuilder.MEASUREMENT_SPEC_1, 1);
//
//			// ********** Run 2
//			runExperimentsWithScenarioDefinition(newDefinition);
//
//			checkScenario(DummyModelBuilder.SCENARIO_NAME, 1);
//			checkExperimentSeries(DummyModelBuilder.EXPERIMENT_SERIES_1, 0L, 2);
//			checkInitializations(DummyModelBuilder.MEASUREMENT_SPEC_1, 1);
//			checkInitializations(DummyModelBuilder.MEASUREMENT_SPEC_2, 1);
//			// ********** Run 3
//			runExperimentsWithScenarioDefinition(newDefinition);
//
//			checkScenario(DummyModelBuilder.SCENARIO_NAME, 1);
//			checkExperimentSeries(DummyModelBuilder.EXPERIMENT_SERIES_1, 0L, 3);
//			checkInitializations(DummyModelBuilder.MEASUREMENT_SPEC_1, 1);
//			checkInitializations(DummyModelBuilder.MEASUREMENT_SPEC_2, 1);
//
//			// ********** Run 4
//			runExperimentsWithScenarioDefinition(referenceDefinition);
//
//			checkScenario(DummyModelBuilder.SCENARIO_NAME, 1);
//			checkExperimentSeries(DummyModelBuilder.EXPERIMENT_SERIES_1, 0L, 4);
//			checkInitializations(DummyModelBuilder.MEASUREMENT_SPEC_1, 1);
//			checkInitializations(DummyModelBuilder.MEASUREMENT_SPEC_2, 1);
//
//			// ********** Run 45
//			runExperimentsWithScenarioDefinition(newDefinition);
//
//			checkScenario(DummyModelBuilder.SCENARIO_NAME, 1);
//			checkExperimentSeries(DummyModelBuilder.EXPERIMENT_SERIES_1, 0L, 5);
//			checkInitializations(DummyModelBuilder.MEASUREMENT_SPEC_1, 1);
//			checkInitializations(DummyModelBuilder.MEASUREMENT_SPEC_2, 1);
//		} catch (Exception e) {
//			logger.error("Unit Test failed! Reason: {}", e);
//			fail(e.getMessage());
//		}
//
//	}
//
//	/**
//	 * Tests the execution of some runs with same measurement specifications and
//	 * different series definitions
//	 */
//	@Test
//	public void testRunsWithDifferentESD() {
//		try {
//			ScenarioDefinition referenceDefinition = DummyModelBuilder.getReferenceScenariodefinition();
//			ScenarioDefinition newDefinition = DummyModelBuilder.getScenariodefinitionWithDifferentESD();
//
//			checkNewExperimentSeriesExpectation(referenceDefinition, newDefinition);
//			checkInitializations(DummyModelBuilder.MEASUREMENT_SPEC_1, 1);
//		} catch (Exception e) {
//			logger.error("Unit Test failed! Reason: {}", e);
//			fail(e.getMessage());
//		}
//	}
//
//	/**
//	 * Tests the execution of some runs with same measurement specifications and
//	 * changed series definitions
//	 */
//	@Test
//	public void testRunsWithChangedESD() {
//		try {
//			ScenarioDefinition referenceDefinition = DummyModelBuilder.getReferenceScenariodefinition();
//			ScenarioDefinition newDefinition = DummyModelBuilder.getScenariodefinitionWithModifiedESD();
//
//			// ********** Run 1
//			runExperimentsWithScenarioDefinition(referenceDefinition);
//
//			checkScenario(DummyModelBuilder.SCENARIO_NAME, 1);
//
//			checkExperimentSeriesVersions(DummyModelBuilder.EXPERIMENT_SERIES_1, 0L, 1);
//			checkExperimentSeries(DummyModelBuilder.EXPERIMENT_SERIES_1, 0L, 1);
//
//			// ********** Run 2
//			runExperimentsWithScenarioDefinition(newDefinition);
//
//			checkScenario(DummyModelBuilder.SCENARIO_NAME, 2);
//
//			checkExperimentSeriesVersions(DummyModelBuilder.EXPERIMENT_SERIES_1, 1L, 2);
//			checkExperimentSeries(DummyModelBuilder.EXPERIMENT_SERIES_1, 0L, 1);
//			checkExperimentSeries(DummyModelBuilder.EXPERIMENT_SERIES_1, 1L, 1);
//
//			// ********** Run 3
//			runExperimentsWithScenarioDefinition(newDefinition);
//
//			checkScenario(DummyModelBuilder.SCENARIO_NAME, 2);
//
//			checkExperimentSeriesVersions(DummyModelBuilder.EXPERIMENT_SERIES_1, 1L, 2);
//			checkExperimentSeries(DummyModelBuilder.EXPERIMENT_SERIES_1, 0L, 1);
//			checkExperimentSeries(DummyModelBuilder.EXPERIMENT_SERIES_1, 1L, 2);
//			// ********** Run 4
//			runExperimentsWithScenarioDefinition(referenceDefinition);
//
//			checkScenario(DummyModelBuilder.SCENARIO_NAME, 2);
//
//			checkExperimentSeriesVersions(DummyModelBuilder.EXPERIMENT_SERIES_1, 1L, 2);
//			checkExperimentSeries(DummyModelBuilder.EXPERIMENT_SERIES_1, 0L, 2);
//			checkExperimentSeries(DummyModelBuilder.EXPERIMENT_SERIES_1, 1L, 2);
//			// ********** Run 5
//			runExperimentsWithScenarioDefinition(newDefinition);
//
//			checkScenario(DummyModelBuilder.SCENARIO_NAME, 2);
//
//			checkExperimentSeriesVersions(DummyModelBuilder.EXPERIMENT_SERIES_1, 1L, 2);
//			checkExperimentSeries(DummyModelBuilder.EXPERIMENT_SERIES_1, 0L, 2);
//			checkExperimentSeries(DummyModelBuilder.EXPERIMENT_SERIES_1, 1L, 3);
//
//			checkInitializations(DummyModelBuilder.MEASUREMENT_SPEC_1, 1);
//		} catch (Exception e) {
//			logger.error("Unit Test failed! Reason: {}", e);
//			fail(e.getMessage());
//		}
//	}
//
//	/**
//	 * Tests the execution of some runs with same measurement specifications,
//	 * same series definitions and different initialization assignments
//	 */
//	@Test
//	public void testRunsWithDifferentInitializations() {
//		try {
//			ScenarioDefinition referenceDefinition = DummyModelBuilder.getReferenceScenariodefinition();
//			ScenarioDefinition newDefinition = DummyModelBuilder.getScenariodefinitionWithDifferentInitializations();
//
//			// ********** Run 1
//			runExperimentsWithScenarioDefinition(referenceDefinition);
//
//			checkScenario(DummyModelBuilder.SCENARIO_NAME, 1);
//
//			checkExperimentSeriesVersions(DummyModelBuilder.EXPERIMENT_SERIES_1, 0L, 1);
//			checkExperimentSeries(DummyModelBuilder.EXPERIMENT_SERIES_1, 0L, 1);
//			checkInitializations(DummyModelBuilder.MEASUREMENT_SPEC_1, 1);
//
//			// ********** Run 2
//			runExperimentsWithScenarioDefinition(newDefinition);
//
//			checkScenario(DummyModelBuilder.SCENARIO_NAME, 1);
//
//			checkExperimentSeriesVersions(DummyModelBuilder.EXPERIMENT_SERIES_1, 0L, 1);
//			checkExperimentSeries(DummyModelBuilder.EXPERIMENT_SERIES_1, 0L, 2);
//			checkInitializations(DummyModelBuilder.MEASUREMENT_SPEC_1, 2);
//
//			// ********** Run 3
//			runExperimentsWithScenarioDefinition(newDefinition);
//
//			checkScenario(DummyModelBuilder.SCENARIO_NAME, 1);
//
//			checkExperimentSeriesVersions(DummyModelBuilder.EXPERIMENT_SERIES_1, 0L, 1);
//			checkExperimentSeries(DummyModelBuilder.EXPERIMENT_SERIES_1, 0L, 3);
//			checkInitializations(DummyModelBuilder.MEASUREMENT_SPEC_1, 2);
//
//			// ********** Run 4
//			runExperimentsWithScenarioDefinition(referenceDefinition);
//
//			checkScenario(DummyModelBuilder.SCENARIO_NAME, 1);
//
//			checkExperimentSeriesVersions(DummyModelBuilder.EXPERIMENT_SERIES_1, 0L, 1);
//			checkExperimentSeries(DummyModelBuilder.EXPERIMENT_SERIES_1, 0L, 4);
//			checkInitializations(DummyModelBuilder.MEASUREMENT_SPEC_1, 2);
//
//			// ********** Run 5
//			runExperimentsWithScenarioDefinition(newDefinition);
//
//			checkScenario(DummyModelBuilder.SCENARIO_NAME, 1);
//
//			checkExperimentSeriesVersions(DummyModelBuilder.EXPERIMENT_SERIES_1, 0L, 1);
//			checkExperimentSeries(DummyModelBuilder.EXPERIMENT_SERIES_1, 0L, 5);
//			checkInitializations(DummyModelBuilder.MEASUREMENT_SPEC_1, 2);
//
//		} catch (Exception e) {
//			logger.error("Unit Test failed! Reason: {}", e);
//			fail(e.getMessage());
//		}
//	}
//
//	/**
//	 * Tests the execution of some runs with same measurement specifications,
//	 * different series definitions and different initialization assignments
//	 */
//	@Test
//	public void testRunsWithDifferentInitializationsAndESD() {
//		try {
//			ScenarioDefinition referenceDefinition = DummyModelBuilder.getReferenceScenariodefinition();
//			ScenarioDefinition newDefinition = DummyModelBuilder
//					.getScenariodefinitionWithDifferentInitializationsAndESD();
//
//			checkNewExperimentSeriesExpectation(referenceDefinition, newDefinition);
//			checkInitializations(DummyModelBuilder.MEASUREMENT_SPEC_1, 2);
//		} catch (Exception e) {
//			logger.error("Unit Test failed! Reason: {}", e);
//			fail(e.getMessage());
//		}
//	}
//
//	private void checkNewExperimentSeriesExpectation(ScenarioDefinition referenceDefinition,
//			ScenarioDefinition newDefinition) throws ConfigurationException {
//		// ********** Run 1
//		runExperimentsWithScenarioDefinition(referenceDefinition);
//
//		checkScenario(DummyModelBuilder.SCENARIO_NAME, 1);
//
//		checkExperimentSeries(DummyModelBuilder.EXPERIMENT_SERIES_1, 0L, 1);
//		checkExperimentSeriesVersions(DummyModelBuilder.EXPERIMENT_SERIES_1, 0L, 1);
//
//		ExperimentSeries es = null;
//		try {
//			es = provider.loadExperimentSeries(DummyModelBuilder.EXPERIMENT_SERIES_1, 0L,
//					DummyModelBuilder.SCENARIO_NAME, ME_URI);
//			assertNotNull(es.getExperimentSeriesRuns().get(0).getSuccessfulResultDataSet());
//
//		} catch (DataNotFoundException e) {
//			fail(e.getMessage());
//		}
//
//		// ********** Run 2
//		runExperimentsWithScenarioDefinition(newDefinition);
//
//		checkScenario(DummyModelBuilder.SCENARIO_NAME, 2);
//
//		checkExperimentSeries(DummyModelBuilder.EXPERIMENT_SERIES_1, 0L, 1);
//		checkExperimentSeriesVersions(DummyModelBuilder.EXPERIMENT_SERIES_1, 0L, 1);
//		checkExperimentSeries(DummyModelBuilder.EXPERIMENT_SERIES_2, 0L, 1);
//		checkExperimentSeriesVersions(DummyModelBuilder.EXPERIMENT_SERIES_2, 0L, 1);
//
//		// ********** Run 3
//		runExperimentsWithScenarioDefinition(newDefinition);
//
//		checkScenario(DummyModelBuilder.SCENARIO_NAME, 2);
//
//		checkExperimentSeries(DummyModelBuilder.EXPERIMENT_SERIES_1, 0L, 1);
//		checkExperimentSeriesVersions(DummyModelBuilder.EXPERIMENT_SERIES_1, 0L, 1);
//		checkExperimentSeries(DummyModelBuilder.EXPERIMENT_SERIES_2, 0L, 2);
//		checkExperimentSeriesVersions(DummyModelBuilder.EXPERIMENT_SERIES_2, 0L, 1);
//
//		// ********** Run 4
//		runExperimentsWithScenarioDefinition(referenceDefinition);
//
//		checkScenario(DummyModelBuilder.SCENARIO_NAME, 2);
//
//		checkExperimentSeries(DummyModelBuilder.EXPERIMENT_SERIES_1, 0L, 2);
//		checkExperimentSeriesVersions(DummyModelBuilder.EXPERIMENT_SERIES_1, 0L, 1);
//		checkExperimentSeries(DummyModelBuilder.EXPERIMENT_SERIES_2, 0L, 2);
//		checkExperimentSeriesVersions(DummyModelBuilder.EXPERIMENT_SERIES_2, 0L, 1);
//
//		// ********** Run 5
//		runExperimentsWithScenarioDefinition(newDefinition);
//
//		checkScenario(DummyModelBuilder.SCENARIO_NAME, 2);
//
//		checkExperimentSeries(DummyModelBuilder.EXPERIMENT_SERIES_1, 0L, 2);
//		checkExperimentSeriesVersions(DummyModelBuilder.EXPERIMENT_SERIES_1, 0L, 1);
//		checkExperimentSeries(DummyModelBuilder.EXPERIMENT_SERIES_2, 0L, 3);
//		checkExperimentSeriesVersions(DummyModelBuilder.EXPERIMENT_SERIES_2, 0L, 1);
//	}
//
//	private void checkExperimentSeries(String seriesName, Long version, int numOfRuns) {
//		ExperimentSeries es = null;
//		try {
//			es = provider.loadExperimentSeries(seriesName, version, DummyModelBuilder.SCENARIO_NAME, ME_URI);
//			assertNotNull(es.getExperimentSeriesRuns().get(0).getSuccessfulResultDataSet());
//
//		} catch (DataNotFoundException e) {
//			fail(e.getMessage());
//		}
//		List<ExperimentSeriesRun> runs = es.getExperimentSeriesRuns();
//		assertTrue(runs.size() == numOfRuns);
//	}
//
//	private void checkExperimentSeriesVersions(String seriesName, Long latestVersion, int numOfVersions) {
//		List<ExperimentSeries> esList = null;
//		try {
//			esList = provider.loadAllExperimentSeries(seriesName, DummyModelBuilder.SCENARIO_NAME, ME_URI);
//		} catch (DataNotFoundException e) {
//			fail(e.getMessage());
//		}
//		assertTrue(esList.size() == numOfVersions);
//
//		ExperimentSeries es = null;
//		try {
//			es = provider.loadExperimentSeries(seriesName, DummyModelBuilder.SCENARIO_NAME, ME_URI);
//		} catch (DataNotFoundException e) {
//			fail(e.getMessage());
//		}
//		assertEquals(es.getVersion(), latestVersion);
//	}
//
//	private void checkScenario(String scenarioName, int numOfExpSeries) {
//		List<ScenarioInstance> scenarios = null;
//		try {
//			scenarios = provider.loadAllScenarioInstances();
//		} catch (DataNotFoundException e) {
//			fail(e.getMessage());
//		}
//		assertTrue(scenarios.size() == 1);
//		assertEquals(scenarios.get(0).getScenarioDefinition().getScenarioName(), scenarioName);
//
//		List<ExperimentSeries> experimentSeries = scenarios.get(0).getExperimentSeriesList();
//		assertTrue(experimentSeries.size() == numOfExpSeries);
//	}
//
//	private void checkInitializations(String measurementSpecName, int numOfInitializations) {
//		List<ScenarioInstance> scenarios = null;
//		try {
//			scenarios = provider.loadAllScenarioInstances();
//		} catch (DataNotFoundException e) {
//			fail(e.getMessage());
//		}
//		MeasurementSpecification ms = scenarios.get(0).getScenarioDefinition()
//				.getMeasurementSpecification(measurementSpecName);
//		assertNotNull(ms);
//		assertTrue(ms.getInitializationAssignemts().size() == numOfInitializations);
//
//	}
//
//	private void runExperimentsWithScenarioDefinition(ScenarioDefinition scenarioDefinition)
//			throws ConfigurationException {
//
//		IConfiguration config = Configuration.getSessionSingleton(NewVersionTest.class, SESSION_ID);
//		config.setProperty(IConfiguration.CONF_MODEL_CHANGE_HANDLING_MODE, MODEL_CHANGE_HANDLILNG_MODE);
//		config.setMeasurementControllerURI(ME_URI);
//		config.setProperty("sopeco.config.persistence.dbtype", "InMemory");
//		// config.setProperty(IConfiguration.CONF_PLUGINS_FOLDER, "plugins");
//		config.setProperty(IConfiguration.CONF_APP_NAME, "sopeco");
//		IEngine engine = EngineFactory.getInstance().createEngine(SESSION_ID);
//		engine.run(scenarioDefinition);
//	}
//
//}

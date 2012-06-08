package org.sopeco.persistence;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.DataSetInputColumn;
import org.sopeco.persistence.dataset.DataSetRowBuilder;
import org.sopeco.persistence.dataset.ParameterValueFactory;
import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.ScenarioInstance;
import org.sopeco.persistence.entities.analysis.IStorableAnalysisResult;
import org.sopeco.persistence.entities.definition.AnalysisConfiguration;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterRole;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;
import org.sopeco.persistence.exceptions.DataNotFoundException;

/**
 * Test class for the {@link IPersistenceProvider} interface.
 * 
 * @author Dennis Westermann
 * 
 */
public class PersistenceProviderTest {

	static JPAPersistenceProvider provider;
	static ScenarioInstance dummyScenarioInstance;

	@BeforeClass
	public static void init() throws Exception {

		provider = (JPAPersistenceProvider) PersistenceProviderFactory.getPersistenceProvider();
		dummyScenarioInstance = DummyFactory.createDummyScenarioInstance();
	}

	@Before
	public void setUp() {
		// database tables should be empty
		assertEquals(0, provider.getSize(ExperimentSeriesRun.class));
		assertEquals(0, provider.getSize(ExperimentSeries.class));
		assertEquals(0, provider.getSize(ScenarioInstance.class));
	}

	@After
	public void tearDown() {
		provider.disposeAll();
	}

	@Test
	public void testStoreAndLoadScenarioInstance() {

		// sub tree checks
		checkAvailableExperimentSeries(dummyScenarioInstance);

		try {
			provider.store(dummyScenarioInstance);
		} catch (Exception e) {
			fail(e.getMessage());
		}

		// tables should only contain the data of the recently stored instance
		checkTableSizes();

		ScenarioInstance loadedInstance = null;
		try {
			loadedInstance = provider.loadScenarioInstance(dummyScenarioInstance.getName(), dummyScenarioInstance.getMeasurementEnvironmentUrl());
		} catch (DataNotFoundException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		// correct instance should be loaded
		assertNotNull(loadedInstance);
		assertTrue(dummyScenarioInstance.equals(loadedInstance));

		// sub tree checks
		checkAvailableExperimentSeries(loadedInstance);
	}

	@Test
	public void testLoadScenarioInstancesByName() {

		try {
			// init
			provider.store(dummyScenarioInstance);
			checkTableSizes();

			// load instances
			List<ScenarioInstance> loadedInstances = provider.loadScenarioInstances(dummyScenarioInstance.getName());

			// there should be only one scenario instance which is the one that
			// we stored
			assertNotNull(loadedInstances);
			assertTrue(loadedInstances.size() == 1);
			assertTrue(dummyScenarioInstance.equals(loadedInstances.get(0)));

			// check sub tree
			checkAvailableExperimentSeries(loadedInstances.get(0));
		} catch (DataNotFoundException e) {
			fail(e.getMessage());
		}

	}

	@Test
	public void testLoadAllScenarioInstances() {

		try {
			// init
			provider.store(dummyScenarioInstance);
			checkTableSizes();

			// load instances
			List<ScenarioInstance> loadedInstances = provider.loadAllScenarioInstances();

			// there should be only one scenario instance which is the one that
			// we stored
			assertNotNull(loadedInstances);
			assertTrue(loadedInstances.size() == 1);
			assertTrue(dummyScenarioInstance.equals(loadedInstances.get(0)));

			// check sub tree
			checkAvailableExperimentSeries(loadedInstances.get(0));
		} catch (DataNotFoundException e) {
			fail(e.getMessage());
		}

	}

	@Test
	public void testUpdateViaScenarioInstance() {

		try {
			// init
			provider.store(dummyScenarioInstance);
			checkTableSizes();

			// load
			final ScenarioInstance loaded1 = provider.loadScenarioInstance(dummyScenarioInstance.getName(),
					dummyScenarioInstance.getMeasurementEnvironmentUrl());
			assertNotNull(loaded1);
			assertEquals(2, loaded1.getExperimentSeriesList().size());
			assertEquals(10, loaded1.getExperimentSeriesList().get(0).getExperimentSeriesRuns().size());
			assertTrue(loaded1.getDescription() == null);

			// update
			loaded1.getExperimentSeriesList().remove(1);
			loaded1.getExperimentSeriesList().get(0).getExperimentSeriesRuns().remove(0);
			loaded1.setDescription("Dummy");

			// store updated entity
			provider.store(loaded1);

			// load updated entity, should equal stored updated entity
			final ScenarioInstance loaded2 = provider.loadScenarioInstance(dummyScenarioInstance.getName(),
					dummyScenarioInstance.getMeasurementEnvironmentUrl());
			assertNotNull(loaded2);
			assertEquals(dummyScenarioInstance, loaded2);
			assertEquals(1, loaded2.getExperimentSeriesList().size());
			assertEquals(9, loaded2.getExperimentSeriesList().get(0).getExperimentSeriesRuns().size());
			assertEquals("Dummy", loaded2.getDescription());

			// table sizes should have changed
			assertEquals(1, provider.getSize(ExperimentSeries.class));
			assertEquals(9, provider.getSize(ExperimentSeriesRun.class));

		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

	@Test
	public void testRemoveScenarioInstance() {

		// init
		try {
			provider.store(dummyScenarioInstance);
			checkTableSizes();
			provider.remove(dummyScenarioInstance);
		} catch (DataNotFoundException e) {
			fail(e.getMessage());
		}

		// should not be able to load the instance from the database
		assertTrue(isRemoved(dummyScenarioInstance));

		// experiment series and runs should be removed as well
		assertTrue(isRemoved(dummyScenarioInstance.getExperimentSeriesList().get(0)));
		assertTrue(isRemoved(dummyScenarioInstance.getExperimentSeriesList().get(0).getExperimentSeriesRuns().get(0)));

		// database should be empty
		assertTrue(isDatabaseEmpty());

	}

	@Test
	public void testStoreAndLoadExperimentSeries() {

		ExperimentSeries dummySeries = dummyScenarioInstance.getExperimentSeriesList().get(0);

		try {
			// store
			provider.store(dummySeries);
			checkTableSizes();

			// load
			ExperimentSeries loadedSeries = provider.loadExperimentSeries(dummySeries.getName(), dummySeries.getScenarioInstance().getName(), dummySeries
					.getScenarioInstance().getMeasurementEnvironmentUrl());

			// loaded series should equal stored series
			assertNotNull(loadedSeries);
			assertTrue(dummySeries.equals(loadedSeries));

			// scenario instance should have been stored as well
			assertNotNull(loadedSeries.getScenarioInstance());
			checkAvailableExperimentSeries(loadedSeries.getScenarioInstance());
		} catch (DataNotFoundException e) {
			fail(e.getMessage());
		}

	}

	@Test
	public void testUpdateViaExperimentSeries() {

		ExperimentSeries dummySeries = dummyScenarioInstance.getExperimentSeriesList().get(0);

		try {
			// store
			provider.store(dummySeries);
			checkTableSizes();

			// load
			final ExperimentSeries loaded1 = provider.loadExperimentSeries(dummySeries.getName(), dummySeries.getScenarioInstance().getName(), dummySeries
					.getScenarioInstance().getMeasurementEnvironmentUrl());
			assertNotNull(loaded1);
			assertEquals(10, loaded1.getExperimentSeriesRuns().size());
			assertTrue(loaded1.getScenarioInstance().getDescription() == null);

			// update
			loaded1.getExperimentSeriesRuns().remove(0);
			for (ExperimentSeriesRun run : DummyFactory.createDummyExperimentSeriesRuns(2)) {
				run.setExperimentSeries(loaded1);
				loaded1.getExperimentSeriesRuns().add(run);
			}
			loaded1.getScenarioInstance().setDescription("Dummy");

			// store updated entity
			provider.store(loaded1);

			// load updated entity, should equal stored updated entity
			final ExperimentSeries loaded2 = provider.loadExperimentSeries(dummySeries.getName(), dummySeries.getScenarioInstance().getName(), dummySeries
					.getScenarioInstance().getMeasurementEnvironmentUrl());
			assertNotNull(loaded2);
			assertEquals(loaded1, loaded2);

			// number of experiment runs should have changed
			assertEquals(11, loaded2.getExperimentSeriesRuns().size());

			// scenario instance should have been updated as well
			assertEquals("Dummy", loaded2.getScenarioInstance().getDescription());

			// table size of ExperimentSeries should not change
			assertEquals(2, provider.getSize(ExperimentSeries.class));
			// table size of ExperimentSeriesRuns should have been changed
			assertEquals(21, provider.getSize(ExperimentSeriesRun.class));

		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

	@Test
	public void testRemoveExperimentSeries() {

		ExperimentSeries dummySeries = dummyScenarioInstance.getExperimentSeriesList().get(0);

		try {

			// init
			provider.store(dummySeries);
			checkTableSizes();
			provider.remove(dummySeries);

			// should not be able to load the instance from the database
			assertTrue(isRemoved(dummySeries));

			// experiment series runs should be removed as well
			assertTrue(isRemoved(dummySeries.getExperimentSeriesRuns().get(0)));

			// scenario instance should not be removed
			assertFalse(isRemoved(dummySeries.getScenarioInstance()));

			// entries for series and corresponding runs should be removed from
			// the database
			assertEquals(1, provider.getSize(ExperimentSeries.class));
			assertEquals(10, provider.getSize(ExperimentSeriesRun.class));

			// ScenarioInstance should no longer refer to the removed series
			List<ScenarioInstance> loadedList = provider.loadScenarioInstances(dummySeries.getScenarioInstance().getName());
			final ScenarioInstance loaded1 = loadedList.get(0);
			assertNotNull(loaded1);
			assertEquals(1, provider.getSize(ExperimentSeries.class));
			assertEquals(1, loaded1.getExperimentSeriesList().size());
			final ScenarioInstance loaded2 = provider.loadScenarioInstance(dummySeries.getScenarioInstance().getPrimaryKey());
			assertNotNull(loaded2);
			assertEquals(1, provider.getSize(ExperimentSeries.class));
			assertEquals(1, loaded2.getExperimentSeriesList().size());

		} catch (DataNotFoundException e) {
			fail(e.getMessage());
			return;
		}

	}

	@Test
	public void testStoreAndLoadExperimentSeriesRun() {
		ExperimentSeriesRun dummySeriesRun = dummyScenarioInstance.getExperimentSeriesList().get(0).getExperimentSeriesRuns().get(0);

		try {
			// store
			provider.store(dummySeriesRun);
			checkTableSizes();

			// load
			ExperimentSeriesRun loadedSeriesRun = provider.loadExperimentSeriesRun(dummySeriesRun.getTimestamp());

			// loaded run should equal stored run
			assertNotNull(loadedSeriesRun);
			assertTrue(dummySeriesRun.equals(loadedSeriesRun));

			// ExperimentSeries and ScenarioInstance should have been stored as
			// well
			assertNotNull(loadedSeriesRun.getExperimentSeries());
			assertTrue(dummySeriesRun.getExperimentSeries().equals(loadedSeriesRun.getExperimentSeries()));
			assertNotNull(loadedSeriesRun.getExperimentSeries().getScenarioInstance());

			checkAvailableExperimentSeriesRuns(loadedSeriesRun.getExperimentSeries());

		} catch (DataNotFoundException e) {
			fail(e.getMessage());
			return;
		}
	}

	@Test
	public void testUpdateViaExperimentSeriesRun() {

		ExperimentSeriesRun dummyRun = dummyScenarioInstance.getExperimentSeriesList().get(0).getExperimentSeriesRuns().get(0);

		try {
			// store
			provider.store(dummyRun);
			checkTableSizes();

			// load
			final ExperimentSeriesRun loaded1 = provider.loadExperimentSeriesRun(dummyRun.getPrimaryKey());
			assertNotNull(loaded1);
			assertTrue(loaded1.getSuccessfulResultDataSet().getObservationColumns().size() == 1);
			assertTrue(loaded1.getSuccessfulResultDataSet().getInputColumns().size() == 1);
			assertTrue(loaded1.getExperimentSeries().getScenarioInstance().getDescription() == null);

			// update
			DataSetAggregated resultDataSet = loaded1.getSuccessfulResultDataSet();
			DummyFactory.addDummyObservationColumn(resultDataSet);
			assertTrue(loaded1.getSuccessfulResultDataSet().getObservationColumns().size() == 2);
			loaded1.getExperimentSeries().getScenarioInstance().setDescription("Dummy");

			// store updated entity
			provider.store(loaded1);

			// load updated entity, should equal stored updated entity
			final ExperimentSeriesRun loaded2 = provider.loadExperimentSeriesRun(dummyRun.getPrimaryKey());
			assertNotNull(loaded2);
			assertEquals(loaded1, loaded2);

			// number of observation columns should have changed
			assertTrue(loaded2.getSuccessfulResultDataSet().getObservationColumns().size() == 2);
			checkRetrievalViaExperimentSeries(dummyRun.getExperimentSeries());

			// scenario instance should have been updated as well
			loaded2.getExperimentSeries().getScenarioInstance().setDescription("Dummy");

			// table sizes should not have been changed
			checkTableSizes();

		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

	private void checkRetrievalViaExperimentSeries(ExperimentSeries series) throws DataNotFoundException {

		final ExperimentSeries loaded1 = provider.loadExperimentSeries(series.getPrimaryKey());
		for (ExperimentSeriesRun run : loaded1.getExperimentSeriesRuns()) {
			if (run.getSuccessfulResultDataSet().getObservationColumns().size() == 2)
				return;
		}
		fail("No run with 2 observation columns found.");
	}

	@Test
	public void testRemoveExperimentSeriesRun() {

		ExperimentSeriesRun dummySeriesRun = dummyScenarioInstance.getExperimentSeriesList().get(0).getExperimentSeriesRuns().get(0);

		try {
			// init
			provider.store(dummySeriesRun);
			checkTableSizes();
			provider.remove(dummySeriesRun);

			// should not be able to load the instance from the database
			assertTrue(isRemoved(dummySeriesRun));

			// experiment series should not be removed
			assertFalse(isRemoved(dummySeriesRun.getExperimentSeries()));
			assertEquals(2, provider.getSize(ExperimentSeries.class));

			// experiment series should no longer refer to the removed series
			// run
			final ExperimentSeries loaded1 = provider.loadExperimentSeries(dummySeriesRun.getExperimentSeries().getPrimaryKey());
			assertNotNull(loaded1);
			assertEquals(9, loaded1.getExperimentSeriesRuns().size());

			// entry for series run should be removed from the database
			assertEquals(19, provider.getSize(ExperimentSeriesRun.class));

		} catch (DataNotFoundException e) {
			fail(e.getMessage());
			return;
		}

	}

	@Test
	public void testSoPeCoWorkflow() {

		// simulate engine
		ScenarioDefinition sd;
		try {
			sd = DummyFactory.loadScenarioDefinition();

			ScenarioInstance scenarioInstance = EntityFactory.createScenarioInstance(sd, "Dummy");
			provider.store(scenarioInstance);

			// simulate experiment series manager
			ExperimentSeries expSeries = EntityFactory.createExperimentSeries(sd.getExperimentSeriesDefinition("Dummy0"));
			scenarioInstance.getExperimentSeriesList().add(expSeries);
			expSeries.setScenarioInstance(scenarioInstance);
			provider.store(expSeries);

			// simulate experiment controller
			ExperimentSeriesRun run = EntityFactory.createExperimentSeriesRun();
			expSeries.getExperimentSeriesRuns().add(run);
			run.setExperimentSeries(expSeries);
			DataSetAggregated dataSet1;

			dataSet1 = DummyFactory.createDummyResultDataSet();

			run.appendSuccessfulResults(dataSet1);
			provider.store(run);
			run.appendSuccessfulResults(simulateExperimentRun(dataSet1));
			provider.store(run);

//			provider.store(scenarioInstance);
			ScenarioInstance loadedInstance = provider.loadScenarioInstance(scenarioInstance.getPrimaryKey());
			
			assertTrue(loadedInstance.getExperimentSeriesList().size() == 1);
			assertNotNull(loadedInstance.getExperimentSeriesList().get(0).getAllExperimentSeriesRunSuccessfulResultsInOneDataSet());
			assertTrue(loadedInstance.getExperimentSeriesList().get(0).getAllExperimentSeriesRunSuccessfulResultsInOneDataSet().size() > 0);
			assertNotNull(loadedInstance.getExperimentSeriesList().get(0).getExperimentSeriesRuns().get(0).getSuccessfulResultDataSet());
			assertTrue(loadedInstance.getExperimentSeriesList().get(0).getExperimentSeriesRuns().get(0).getSuccessfulResultDataSet().getObservationColumns().size() == 1);
			

		} catch (IOException e1) {
			fail(e1.getMessage());

		} catch (DataNotFoundException e) {
			fail(e.getMessage());
		}

	}

	@Test
	public void testStoreAndLoadAnalysisResult(){
		AnalysisConfiguration dummyAnalysisConfig = dummyScenarioInstance.getScenarioDefinition().getMeasurementSpecifications().get(0).getExperimentSeriesDefinitions().get(0).getExplorationStrategy().getAnalysisConfigurations().get(0);
		DummyAnalysisResult dummyAnalysisResult = new DummyAnalysisResult("a = b + c", dummyAnalysisConfig.getDependentParameters().get(0), dummyAnalysisConfig);
		provider.store("DummyResult", dummyAnalysisResult);
		try {
			IStorableAnalysisResult analysisResult = provider.loadAnalysisResult("DummyResult");
			assertNotNull(analysisResult);
			assertNotNull(((DummyAnalysisResult)analysisResult).getAnalysisStrategyConfiguration());
			assertEquals(1, ((DummyAnalysisResult)analysisResult).getAnalysisStrategyConfiguration().getDependentParameters().size());
			assertEquals("DummyOutput", ((DummyAnalysisResult)analysisResult).getAnalysisStrategyConfiguration().getDependentParameters().get(0).getName());
			assertEquals("MARS", ((DummyAnalysisResult)analysisResult).getAnalysisStrategyConfiguration().getName());
			assertNotNull(((DummyAnalysisResult)analysisResult).getDependentParameter());
			assertEquals("DummyOutput", ((DummyAnalysisResult)analysisResult).getDependentParameter().getName());
		} catch (DataNotFoundException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testUpdateAnalysisResult() {
		AnalysisConfiguration dummyAnalysisConfig = dummyScenarioInstance.getScenarioDefinition().getMeasurementSpecifications().get(0).getExperimentSeriesDefinitions().get(0).getExplorationStrategy().getAnalysisConfigurations().get(0);
		DummyAnalysisResult dummyAnalysisResult = new DummyAnalysisResult("a = b + c", dummyAnalysisConfig.getDependentParameters().get(0), dummyAnalysisConfig);
		provider.store("DummyResult", dummyAnalysisResult);
		try {
			IStorableAnalysisResult loadedResult1 = provider.loadAnalysisResult("DummyResult");

			assertEquals("a = b + c", ((DummyAnalysisResult) loadedResult1).getFunctionAsString());

			((DummyAnalysisResult) loadedResult1).setFunctionAsString("x = y + z");

			provider.store("DummyResult", loadedResult1);

			IStorableAnalysisResult loadedResult2 = provider.loadAnalysisResult("DummyResult");

			assertEquals("x = y + z", ((DummyAnalysisResult) loadedResult2).getFunctionAsString());

		} catch (DataNotFoundException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testRemoveAnalysisResult() {
		AnalysisConfiguration dummyAnalysisConfig = dummyScenarioInstance.getScenarioDefinition().getMeasurementSpecifications().get(0).getExperimentSeriesDefinitions().get(0).getExplorationStrategy().getAnalysisConfigurations().get(0);
		DummyAnalysisResult dummyAnalysisResult = new DummyAnalysisResult("a = b + c", dummyAnalysisConfig.getDependentParameters().get(0), dummyAnalysisConfig);
		
		provider.store("DummyResult", dummyAnalysisResult);
		try {
			provider.remove("DummyResult");
		} catch (DataNotFoundException e) {
			e.printStackTrace();
			fail();
		}

		try {
			provider.loadAnalysisResult("DummyResult");
			fail();
		} catch (DataNotFoundException e) {
			// this is the expected case
		}

	}

	private DataSetAggregated simulateExperimentRun(DataSetAggregated dataset) {
		DataSetRowBuilder builder = new DataSetRowBuilder();
		builder.startRow();
		for (ParameterDefinition parameter : dataset.getParameterDefinitions()) {
			if (parameter.getRole().equals(ParameterRole.INPUT)) {
				builder.addInputParameterValue(parameter, ParameterValueFactory.createParameterValue(parameter, 0).getValue());
			} else {
				builder.addObservationParameterValue(parameter, ParameterValueFactory.createParameterValue(parameter, 0).getValue());
			}

		}
		builder.finishRow();
		return builder.createDataSet();
	}

	public static void printTableSizes() {
		System.out.println("ScenarioInstance: " + provider.getSize(ScenarioInstance.class));
		System.out.println("ExperimentSeries: " + provider.getSize(ExperimentSeries.class));
		System.out.println("ExperimentSeriesRun: " + provider.getSize(ExperimentSeriesRun.class));

	}

	private void checkTableSizes() {
		// based on the values set in the DummyFactory and the fact that we
		// store only one instance of a ScenarioInstance
		assertEquals(1, provider.getSize(ScenarioInstance.class));
		assertEquals(2, provider.getSize(ExperimentSeries.class));
		assertEquals(20, provider.getSize(ExperimentSeriesRun.class));
	}

	private boolean isDatabaseEmpty() {
		if (provider.getSize(ScenarioInstance.class) == 0 && provider.getSize(ExperimentSeries.class) == 0 && provider.getSize(ExperimentSeriesRun.class) == 0) {
			return true;
		}

		return false;
	}

	private boolean isRemoved(ScenarioInstance instance) {
		ScenarioInstance loadedInstance;
		try {
			loadedInstance = provider.loadScenarioInstance(instance.getName(), instance.getMeasurementEnvironmentUrl());
		} catch (DataNotFoundException e) {
			loadedInstance = null;
		}
		if (loadedInstance == null) {
			return true;
		}
		return false;
	}

	private boolean isRemoved(ExperimentSeries series) {
		ExperimentSeries loadedSeries;
		try {
			loadedSeries = provider.loadExperimentSeries(series.getName(), series.getScenarioInstance().getName(), series.getScenarioInstance()
					.getMeasurementEnvironmentUrl());
		} catch (DataNotFoundException e) {
			loadedSeries = null;
		}
		if (loadedSeries == null) {
			return true;
		}
		return false;
	}

	private boolean isRemoved(ExperimentSeriesRun run) {
		ExperimentSeriesRun loadedRun;
		try {
			loadedRun = provider.loadExperimentSeriesRun(run.getTimestamp());
		} catch (DataNotFoundException e) {
			loadedRun = null;
		}
		if (loadedRun == null) {
			return true;
		}
		return false;
	}

	private void checkAvailableExperimentSeries(ScenarioInstance scenarioInstance) {

		// We should have 2 ExperimentSeries in the database
		assertTrue(scenarioInstance.getExperimentSeriesList().size() == 2);

		// ExperimentSeries should reference the ScenarioInstance
		assertNotNull(scenarioInstance.getExperimentSeriesList().get(0).getScenarioInstance());
		assertTrue(scenarioInstance.getExperimentSeriesList().get(0).getScenarioInstance().getName().equalsIgnoreCase("Dummy"));
		assertTrue(scenarioInstance.equals(scenarioInstance.getExperimentSeriesList().get(0).getScenarioInstance()));

		// ExperimentSeries should reference the ExperimentSeriesDefintion
		assertNotNull(scenarioInstance.getExperimentSeriesList().get(0).getExperimentSeriesDefinition());

		// Name of ExperimentSeries should be equal to the name of its
		// definition
		assertTrue(scenarioInstance.getExperimentSeriesList().get(0).getName()
				.equals(scenarioInstance.getExperimentSeriesList().get(0).getExperimentSeriesDefinition().getName()));

		// Parameters should be loaded
		assertNotNull(scenarioInstance.getExperimentSeriesList().get(0).getExperimentSeriesDefinition().getExperimentAssignments().get(0).getParameter()
				.getName());
		assertNotNull(scenarioInstance.getExperimentSeriesList().get(0).getExperimentSeriesDefinition().getExperimentAssignments().get(0).getParameter()
				.getType());

		// Experiment run results should be accessible via experiment series
		assertNotNull(scenarioInstance.getExperimentSeriesList().get(0).getAllExperimentSeriesRunSuccessfulResultsInOneDataSet());
		assertTrue(scenarioInstance.getExperimentSeriesList().get(0).getAllExperimentSeriesRunSuccessfulResultsInOneDataSet().size() > 0);
		
		// ExperimentSeries should reference ExperimentSeriesRuns
		checkAvailableExperimentSeriesRuns(scenarioInstance.getExperimentSeriesList().get(0));
	}

	private void checkAvailableExperimentSeriesRuns(ExperimentSeries expSeries) {

		// We should have 10 ExperimentSeriesRuns in the database
		assertTrue(expSeries.getExperimentSeriesRuns().size() == 10);

		// ExperimentSeriesRun should reference the ExperimentSeries
		assertNotNull(expSeries.getExperimentSeriesRuns().get(0).getExperimentSeries().getName());
		assertTrue(expSeries.equals(expSeries.getExperimentSeriesRuns().get(0).getExperimentSeries()));

		// ExperimentSeriesRun should contain a result data set
		assertNotNull(expSeries.getExperimentSeriesRuns().get(0).getSuccessfulResultDataSet());
		assertTrue(expSeries.getExperimentSeriesRuns().get(0).getSuccessfulResultDataSet().getObservationColumns().size() == 1);
		assertTrue(expSeries.getExperimentSeriesRuns().get(0).getSuccessfulResultDataSet().getInputColumns().size() == 1);

		DataSetInputColumn col = ((DataSetInputColumn<?>) expSeries.getExperimentSeriesRuns().get(0).getSuccessfulResultDataSet().getInputColumns().toArray()[0]);

		// Result Data Set should have the ParameterDefinitions
		assertNotNull(((DataSetInputColumn<?>) expSeries.getExperimentSeriesRuns().get(0).getSuccessfulResultDataSet().getInputColumns().toArray()[0]).getParameter());

	}

}

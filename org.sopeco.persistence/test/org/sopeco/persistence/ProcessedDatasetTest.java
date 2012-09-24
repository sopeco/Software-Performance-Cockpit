package org.sopeco.persistence;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import java.util.Collections;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sopeco.config.Configuration;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.util.ParameterType;
import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.ProcessedDataSet;
import org.sopeco.persistence.entities.ScenarioInstance;
import org.sopeco.persistence.entities.definition.ParameterRole;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;
import org.sopeco.persistence.exceptions.DataNotFoundException;
import org.sopeco.persistence.util.ScenarioDefinitionBuilder;

public class ProcessedDatasetTest {
	private static final String EXPERIMENT_SERIES = "EmpSeries";
	private static final String SCENARIO_NAME = "ScenarioName";
	private static final String ME_URL = "meURL";
	private static ProcessedDataSet dummyPDS;
	private static ExperimentSeriesRun dummyRun;
	private static ExperimentSeries dummyES;
	private static ScenarioInstance dummySI;
	private static IPersistenceProvider provider = null;
	private static DataSetAggregated dataSetAggregated = null;
	private static String sessionId = "testId";

	@Before
	public void setUp() throws Exception {
		Configuration.getSessionSingleton(sessionId).setProperty("sopeco.config.persistence.dbtype", "InMemory");
		provider = PersistenceProviderFactory.getInstance().getPersistenceProvider(sessionId);
		dummySI = createScenarioInstance();
		dummyES = createExpSeries(dummySI);
		dummyPDS = new ProcessedDataSet();
		dummyPDS.setId(String.valueOf(System.nanoTime()));
		dataSetAggregated = DummyFactory.createDummyResultDataSet();
		dummyPDS.setDataSet(dataSetAggregated);
		dummyES.addProcessedDataSet(dummyPDS);
		dummyRun = new ExperimentSeriesRun();
		dummyRun.setExperimentSeries(dummyES);
		dummyRun.setSuccessfulResultDataSet(dataSetAggregated);
		dummyRun.setTimestamp(System.nanoTime());
		dummyES.getExperimentSeriesRuns().add(dummyRun);
	}

	@After
	public void cleanUp() throws DataNotFoundException {
		provider.remove(dummySI);
		dummySI = null;
		dummyES = null;
		dummyPDS = null;
		dummyRun = null;

	}

	@Test
	public void testProcessedDatasetStoreAndLoad() {
		try {
			provider.store(dummyPDS);
			ScenarioInstance loadedSi = provider.loadScenarioInstance(SCENARIO_NAME, ME_URL);
			assertTrue(loadedSi.getExperimentSeriesList().get(0).getProcessedDataSets().size() == 1);
			assertEquals(dataSetAggregated.getID(), loadedSi.getExperimentSeriesList().get(0).getProcessedDataSets()
					.get(0).getDataSet().getID());
		} catch (DataNotFoundException e) {
			fail(e.getMessage());
		}

	}

	@Test
	public void testProcessedDatasetRemove() {
		try {
			provider.store(dummyPDS);
			provider.remove(dummyPDS);
			ScenarioInstance loadedSi = provider.loadScenarioInstance(SCENARIO_NAME, ME_URL);
			assertTrue(loadedSi.getExperimentSeriesList().get(0).getProcessedDataSets().size() == 0);
		} catch (DataNotFoundException e) {
			fail(e.getMessage());
		}
	}

	private ScenarioInstance createScenarioInstance() {
		ScenarioInstance si = new ScenarioInstance();
		si.setScenarioDefinition(getScenariodefinition());
		si.setMeasurementEnvironmentUrl(ME_URL);
		return si;
	}

	private ExperimentSeries createExpSeries(ScenarioInstance si) {
		ExperimentSeries es = new ExperimentSeries();
		es.setScenarioInstance(si);
		es.setVersion(0L);
		es.setName(EXPERIMENT_SERIES);
		es.setExperimentSeriesDefinition(si.getScenarioDefinition().getExperimentSeriesDefinition(EXPERIMENT_SERIES));
		return es;
	}

	private ScenarioDefinition getScenariodefinition() {
		ScenarioDefinitionBuilder builder = new ScenarioDefinitionBuilder(SCENARIO_NAME);
		createMeasurementEnvironment(builder);

		builder.createMeasurementSpecification("measSpec");
		createESD(builder);

		return builder.getScenarioDefinition();
	}

	private void createMeasurementEnvironment(ScenarioDefinitionBuilder builder) {
		builder.createNewNamespace("default");
		builder.createParameter("param 1", ParameterType.INTEGER, ParameterRole.INPUT);
		builder.createParameter("param 2", ParameterType.INTEGER, ParameterRole.INPUT);
		builder.createParameter("param 3", ParameterType.INTEGER, ParameterRole.OBSERVATION);
	}

	private void createESD(ScenarioDefinitionBuilder builder) {
		builder.createExperimentSeriesDefinition(EXPERIMENT_SERIES);
		builder.createNumberOfRunsCondition(1);

		builder.createExplorationStrategy("Full Exploration Strategy", Collections.EMPTY_MAP);
	}

}

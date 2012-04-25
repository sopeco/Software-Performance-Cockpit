package org.sopeco.persistence;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.sopeco.model.configuration.ScenarioDefinition;
import org.sopeco.model.configuration.SoPeCoModelFactoryHandler;
import org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition;
import org.sopeco.model.util.ScenarioDefinitionUtil;
import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.ScenarioInstance;

/**
 * Test class for the {@link PersistenceProviderFactory}.
 * 
 * @author Dennis Westermann
 * 
 */
public class PersistenceProviderFactoryTest {

	private static ScenarioDefinition dummyScenarioDefinition;

	@Before
	public void setUp() throws Exception {
		SoPeCoModelFactoryHandler.initFactories();

		dummyScenarioDefinition = DummyFactory.loadScenarioDefinition();

	}

	@Test
	public void testCreateScenarioInstance() {

		try {

			ScenarioInstance scenarioInstance = PersistenceProviderFactory.createScenarioInstance(dummyScenarioDefinition, "Dummy");

			assertNotNull(scenarioInstance);
			assertEquals("Dummy", scenarioInstance.getName());
			assertEquals("Dummy", scenarioInstance.getMeasurementEnvironmentUrl());
			assertEquals(dummyScenarioDefinition, scenarioInstance.getScenarioDefinition());

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

	}

	@Test
	public void testCreateExperimentSeries() {

		try {
			ScenarioInstance scenarioInstance = PersistenceProviderFactory.createScenarioInstance(dummyScenarioDefinition, "Dummy");
			ExperimentSeriesDefinition esd = ScenarioDefinitionUtil.getExperimentSeriesDefinition("Dummy0", dummyScenarioDefinition);
			ExperimentSeries series = PersistenceProviderFactory.createExperimentSeries(scenarioInstance, esd);
		
			assertNotNull(series);
			assertEquals("Dummy0", series.getName());
			assertEquals(scenarioInstance, series.getScenarioInstance());
			assertEquals(esd.getName(), series.getExperimentSeriesDefinition().getName());
			assertEquals(0, series.getExperimentSeriesRuns().size());
			assertEquals(1, scenarioInstance.getExperimentSeries().size());
			assertEquals(series, scenarioInstance.getExperimentSeries().get(0));
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

	}
	
	
	@Test
	public void testCreateExperimentSeriesRun() {

		try {
			ScenarioInstance scenarioInstance = PersistenceProviderFactory.createScenarioInstance(dummyScenarioDefinition, "Dummy");
			ExperimentSeriesDefinition esd = ScenarioDefinitionUtil.getExperimentSeriesDefinition("Dummy0", dummyScenarioDefinition);
			ExperimentSeries series = PersistenceProviderFactory.createExperimentSeries(scenarioInstance, esd);
			ExperimentSeriesRun seriesRun = PersistenceProviderFactory.createExperimentSeriesRun(series);
			
			assertNotNull(seriesRun);
			assertEquals(series, seriesRun.getExperimentSeries());
			assertEquals(scenarioInstance, seriesRun.getExperimentSeries().getScenarioInstance());
			assertNotNull(seriesRun.getTimestamp());
			assertEquals(null, seriesRun.getResultDataSet());
			assertEquals(1, series.getExperimentSeriesRuns().size());
			assertEquals(seriesRun, series.getExperimentSeriesRuns().get(0));
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

	}
}

package org.sopeco.persistence;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.ScenarioInstance;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;

/**
 * Test class for the {@link EntityFactory}.
 * 
 * @author Dennis Westermann
 * 
 */
public class EntityFactoryTest {

	private static ScenarioDefinition dummyScenarioDefinition;

	@Before
	public void setUp() throws Exception {

		dummyScenarioDefinition = DummyFactory.loadScenarioDefinition();

	}

	@Test
	public void testCreateScenarioInstance() {

		try {

			ScenarioInstance scenarioInstance = EntityFactory.createScenarioInstance(dummyScenarioDefinition, "Dummy");

			assertNotNull(scenarioInstance);
			assertEquals("Dummy", scenarioInstance.getName());
			assertEquals("Dummy", scenarioInstance.getMeasurementEnvironmentUrl());
			assertEquals(dummyScenarioDefinition, scenarioInstance.getScenarioDefinition("DummyDef"));

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

	}

	@Test
	public void testCreateExperimentSeries() {

		try {
			ScenarioInstance scenarioInstance = EntityFactory.createScenarioInstance(dummyScenarioDefinition, "Dummy");
			ExperimentSeriesDefinition esd = dummyScenarioDefinition.getExperimentSeriesDefinition("Dummy0");
			ExperimentSeries series = EntityFactory.createExperimentSeries(esd);
			scenarioInstance.getExperimentSeriesList().add(series);
			series.setScenarioInstance(scenarioInstance);
		
			assertNotNull(series);
			assertEquals("Dummy0", series.getName());
			assertEquals(scenarioInstance, series.getScenarioInstance());
			assertEquals(esd.getName(), series.getExperimentSeriesDefinition().getName());
			assertEquals(0, series.getExperimentSeriesRuns().size());
			assertEquals(1, scenarioInstance.getExperimentSeriesList().size());
			assertEquals(series, scenarioInstance.getExperimentSeriesList().get(0));
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

	}
	
	
	@Test
	public void testCreateExperimentSeriesRun() {

		try {
			ScenarioInstance scenarioInstance = EntityFactory.createScenarioInstance(dummyScenarioDefinition, "Dummy");
			
			ExperimentSeriesDefinition esd = dummyScenarioDefinition.getExperimentSeriesDefinition("Dummy0");
			ExperimentSeries series = EntityFactory.createExperimentSeries(esd);
			scenarioInstance.getExperimentSeriesList().add(series);
			series.setScenarioInstance(scenarioInstance);
			
			ExperimentSeriesRun seriesRun = EntityFactory.createExperimentSeriesRun();
			series.getExperimentSeriesRuns().add(seriesRun);
			seriesRun.setExperimentSeries(series);
			
			assertNotNull(seriesRun);
			assertEquals(series, seriesRun.getExperimentSeries());
			assertEquals(scenarioInstance, seriesRun.getExperimentSeries().getScenarioInstance());
			assertNotNull(seriesRun.getTimestamp());
			assertEquals(null, seriesRun.getSuccessfulResultDataSet());
			assertEquals(1, series.getExperimentSeriesRuns().size());
			assertEquals(seriesRun, series.getExperimentSeriesRuns().get(0));
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

	}
}

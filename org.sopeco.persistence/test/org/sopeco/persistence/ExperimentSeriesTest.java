package org.sopeco.persistence;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.ScenarioInstance;

/**
 * Test class for the {@link ExperimentSeries} entity.
 * 
 * @author Dennis Westermann
 *
 */
public class ExperimentSeriesTest {
	
	private static ScenarioInstance dummyScenarioInstance;

	@Before
	public void setUp() throws Exception {
		
		dummyScenarioInstance = DummyFactory.createDummyScenarioInstance();
	
	}

	@Test
	public void testGetAllExperimentRunResultsInOneDataSet() {
	
		ExperimentSeries dummySeries = dummyScenarioInstance.getExperimentSeriesList().get(0);
		ExperimentSeriesRun dummySeriesRun = dummySeries.getExperimentSeriesRuns().get(0);
		
		assertEquals(2, dummySeriesRun.getSuccessfulResultDataSet().size());
		assertNotNull(dummySeries.getAllExperimentSeriesRunSuccessfulResultsInOneDataSet());
		assertEquals(20, dummySeries.getAllExperimentSeriesRunSuccessfulResultsInOneDataSet().size());
		
	}
	
	@Test
	public void testGetLatestExperimentRun() {
	
		ExperimentSeries dummySeries = dummyScenarioInstance.getExperimentSeriesList().get(0);
		
		ExperimentSeriesRun dummySeriesRun1 = EntityFactory.createExperimentSeriesRun();
		dummySeriesRun1.setExperimentSeries(dummySeries);
		ExperimentSeriesRun dummySeriesRun2 = EntityFactory.createExperimentSeriesRun();
		dummySeriesRun2.setExperimentSeries(dummySeries);
		
		dummySeries.getExperimentSeriesRuns().add(dummySeriesRun2);
		dummySeries.getExperimentSeriesRuns().add(dummySeriesRun1);
		
		assertEquals(dummySeriesRun2, dummySeries.getLatestExperimentSeriesRun());
	}
	
}

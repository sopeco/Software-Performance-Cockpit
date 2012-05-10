package org.sopeco.persistence;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.entities.ExperimentSeriesRun;

/**
 * Test class for the {@link ExperimentSeriesRun} entity.
 * 
 * @author Dennis Westermann
 *
 */
public class ExperimentSeriesRunTest {
	
	private static ExperimentSeriesRun dummyRun;

	@Before
	public void setUp() throws Exception {
		
		dummyRun = new ExperimentSeriesRun();
		dummyRun.setTimestamp(System.nanoTime());
	}

	@Test
	public void testAppendExperimentRunResultsOnNotInitializedResultDataSet() {
	
		try {
			DataSetAggregated dataSet = DummyFactory.createDummyResultDataSet();
			dummyRun.appendSuccessfulResults(dataSet);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertNotNull(dummyRun.getSuccessfulResultDataSet());
		assertEquals(2, dummyRun.getSuccessfulResultDataSet().getColumns().size());
		assertEquals(2, dummyRun.getSuccessfulResultDataSet().size());
		
	}
	
	@Test
	public void testAppendExperimentRunResultsOnInitializedResultDataSet() {
	
		try {
			DataSetAggregated dataSet = DummyFactory.createDummyResultDataSet();
			dummyRun.setSuccessfulResultDataSet(dataSet);
			dummyRun.appendSuccessfulResults(dataSet);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertNotNull(dummyRun.getSuccessfulResultDataSet());
		assertEquals(2, dummyRun.getSuccessfulResultDataSet().getColumns().size());
		assertEquals(4, dummyRun.getSuccessfulResultDataSet().size());
		
	}
	
}

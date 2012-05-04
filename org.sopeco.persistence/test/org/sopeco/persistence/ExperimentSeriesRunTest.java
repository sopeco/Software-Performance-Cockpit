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
		SoPeCoModelFactoryHandler.initFactories();
		
		dummyRun = new ExperimentSeriesRun();
		dummyRun.setTimestamp(System.nanoTime());
	}

	@Test
	public void testAppendExperimentRunResultsOnNotInitializedResultDataSet() {
	
		try {
			DataSetAggregated dataSet = DummyFactory.createDummyResultDataSet();
			dummyRun.append(dataSet);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertNotNull(dummyRun.getResultDataSet());
		assertEquals(2, dummyRun.getResultDataSet().getColumns().size());
		assertEquals(2, dummyRun.getResultDataSet().size());
		
	}
	
	@Test
	public void testAppendExperimentRunResultsOnInitializedResultDataSet() {
	
		try {
			DataSetAggregated dataSet = DummyFactory.createDummyResultDataSet();
			dummyRun.setResultDataSet(dataSet);
			dummyRun.append(dataSet);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertNotNull(dummyRun.getResultDataSet());
		assertEquals(2, dummyRun.getResultDataSet().getColumns().size());
		assertEquals(4, dummyRun.getResultDataSet().size());
		
	}
	
}

package org.sopeco.persistence;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.sopeco.persistence.entities.ScenarioInstance;

/**
 * Test class for the {@link ScenarioInstance} entity.
 * 
 * @author Dennis Westermann
 *
 */
public class ScenarioInstanceTest {
	
	private static ScenarioInstance dummyScenarioInstance;

	@Before
	public void setUp() throws Exception {
		
		dummyScenarioInstance = DummyFactory.createDummyScenarioInstance();
	
	}

	@Test
	public void testGetExperimentSeries() {
	
		assertNotNull(dummyScenarioInstance.getExperimentSeries("Dummy0"));
		assertEquals(null, dummyScenarioInstance.getExperimentSeries("foo"));
		
	}

}

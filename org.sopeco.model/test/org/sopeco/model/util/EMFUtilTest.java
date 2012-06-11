package org.sopeco.model.util;

import static junit.framework.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.sopeco.model.configuration.SoPeCoModelFactoryHandler;
import org.sopeco.model.util.EMFUtil;
import org.sopeco.persistence.entities.definition.AnalysisConfiguration;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;

/**
 * Test class for the extension implementation of the parameter definition.
 * 
 * @author Dennis Westermann
 *
 */
public class EMFUtilTest {
	
	private final static String PATH_TO_SCENARIO_DEFINITION = "test/test.configuration";

	@Before
	public void setUp() throws Exception {
		SoPeCoModelFactoryHandler.initFactories();
	
	}

	@Test
	public void testLoadFromFilePath() {
		
		ScenarioDefinition scenarioDefinition;
		try {
			scenarioDefinition = (ScenarioDefinition) EMFUtil.loadFromFilePath(PATH_TO_SCENARIO_DEFINITION);
		} catch (IOException e) {
			e.printStackTrace();
			fail();return;
		}
		assertEquals("Test Scenario", scenarioDefinition.getScenarioName());
		try
		{
			assertEquals("default.A", scenarioDefinition.getMeasurementEnvironmentDefinition().getRoot().getParameters().get(0).getFullName());
//			assertEquals("default.DummyInput", scenarioDefinition.getMeasurementSpecifications().get(0).getExperimentSeriesDefinitions().get(0).getExplorationStrategy().getAnalysisConfigurations().get(0).getIndependentParameters().get(0).getFullName());
			
		} catch (UnsupportedOperationException uoe){
			fail("Factory override did not work");
		}
	}
	
}

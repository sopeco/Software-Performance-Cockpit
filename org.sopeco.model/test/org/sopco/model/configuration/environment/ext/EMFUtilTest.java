package org.sopco.model.configuration.environment.ext;

import static junit.framework.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.sopeco.model.configuration.ScenarioDefinition;
import org.sopeco.model.configuration.SoPeCoModelFactoryHandler;
import org.sopeco.model.util.EMFUtil;

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
		assertEquals("Test Scenario", scenarioDefinition.getName());
		try
		{
			assertEquals("default.A", scenarioDefinition.getMeasurementEnvironmentDefinition().getRoot().getParameters().get(0).getFullName());
		} catch (UnsupportedOperationException uoe){
			fail("Factory override did not work");
		}
	}
	
}

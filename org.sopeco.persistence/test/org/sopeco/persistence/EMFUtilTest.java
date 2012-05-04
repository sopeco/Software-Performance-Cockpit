package org.sopeco.persistence;

import static junit.framework.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.sopeco.model.configuration.SoPeCoModelFactoryHandler;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;
import org.sopeco.persistence.util.EMFUtil;

/**
 * Test class for the extension implementation of the parameter definition.
 * 
 * @author Dennis Westermann
 *
 */
public class EMFUtilTest {
	
	private final static String PATH_TO_SCENARIO_DEFINITION = "test/dummy.configuration";

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
		assertEquals("Dummy", scenarioDefinition.getName());
		try
		{
			assertEquals("default.DummyInput", scenarioDefinition.getMeasurementEnvironmentDefinition().getRoot().getParameters().get(0).getFullName());
		} catch (UnsupportedOperationException uoe){
			fail("Factory override did not work");
		}
	}
	
}

package org.sopco.model.configuration.environment.ext;

import static junit.framework.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.sopco.model.configuration.helper.ConfigurationBuilder;
import org.sopeco.model.configuration.SoPeCoModelFactoryHandler;
import org.sopeco.model.configuration.environment.ParameterDefinition;
import org.sopeco.model.configuration.environment.ParameterRole;

/**
 * Test class for the extension implementation of the parameter definition.
 * 
 * @author Dennis Westermann
 *
 */
public class ParameterDefinitionExtTest {
	
	private static ParameterDefinition paramDef;

	@Before
	public void setUp() throws Exception {
		SoPeCoModelFactoryHandler.initFactories();
		
		ConfigurationBuilder builder = new ConfigurationBuilder("Dummy");
		builder.createNamespace("org");
		builder.createNamespace("sopeco");
		builder.createParameter("dummy", "Double", ParameterRole.INPUT);
		paramDef = builder.getCurrentParameter();
	}

	@Test
	public void testGetFullName() {
		assertEquals("org.sopeco.dummy", paramDef.getFullName());
	}
	
	@Test
	public void testGetFullNameWithDelimiter() {
		assertEquals("org_sopeco_dummy", paramDef.getFullName("_"));
	}
	
}

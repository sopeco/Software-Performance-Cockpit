package org.sopeco.persistence.entities.definition;

import static junit.framework.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.sopeco.persistence.EntityFactory;

/**
 * Test class for the extension implementation of the parameter definition.
 * 
 * @author Dennis Westermann
 *
 */
public class ParameterDefinitionTest {
	
	private static ParameterDefinition paramDef;

	@Before
	public void setUp() throws Exception {
		
		ParameterNamespace org = EntityFactory.createNamespace("org");
		ParameterNamespace sopeco = EntityFactory.createNamespace("sopeco");
		
		org.getChildren().add(sopeco);
		sopeco.setParent(org);
		
		paramDef = EntityFactory.createParameterDefinition("dummy", "Double", ParameterRole.INPUT);
		sopeco.getParameters().add(paramDef);
		paramDef.setNamespace(sopeco);
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

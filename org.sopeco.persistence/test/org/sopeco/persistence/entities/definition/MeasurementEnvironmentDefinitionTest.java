package org.sopeco.persistence.entities.definition;

import static junit.framework.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.sopeco.persistence.EntityFactory;

/**
 * Test class for the extension implementation of the {@link MeasurementEnvironmentDefinition}.
 * 
 * @author Dennis Westermann
 *
 */
public class MeasurementEnvironmentDefinitionTest {
	
	private static ParameterNamespace org;
	private static ParameterNamespace sopeco;
	private static ParameterNamespace emptyRoot;
	private static MeasurementEnvironmentDefinition med; 
	@Before
	public void setUp() throws Exception {
		
		emptyRoot = EntityFactory.createNamespace("");
		org = EntityFactory.createNamespace("org");
		sopeco = EntityFactory.createNamespace("sopeco");
		
		emptyRoot.getChildren().add(org);
		org.setParent(emptyRoot);
		org.getChildren().add(sopeco);
		sopeco.setParent(org);
		

		med = EntityFactory.createMeasurementEnvironmentDefinition();
		med.setRoot(emptyRoot);
	}

	@Test
	public void testGetNamespace() {
		assertEquals("", med.getNamespace("").getName());
		assertEquals("org", med.getNamespace("org").getName());
		assertEquals("sopeco", med.getNamespace("org.sopeco").getName());
		assertEquals(null, med.getNamespace("foo"));
	}
	
}

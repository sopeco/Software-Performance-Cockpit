package org.sopeco.persistence.entities.definition;

import static junit.framework.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.sopeco.persistence.EntityFactory;

/**
 * Test class for the extension implementation of the parameter namespace.
 * 
 * @author Dennis Westermann
 *
 */
public class ParameterNamespaceTest {
	
	private static ParameterNamespace org;
	private static ParameterNamespace sopeco;
	private static ParameterNamespace emptyRoot;

	@Before
	public void setUp() throws Exception {
		emptyRoot = EntityFactory.createNamespace("");
		org = EntityFactory.createNamespace("org");
		sopeco = EntityFactory.createNamespace("sopeco");
		
		emptyRoot.getChildren().add(org);
		org.setParent(emptyRoot);
		org.getChildren().add(sopeco);
		sopeco.setParent(org);
	}

	@Test
	public void testGetFullName() {
		assertEquals("", emptyRoot.getFullName());
		assertEquals("org", org.getFullName());
		assertEquals("org.sopeco", sopeco.getFullName());
	}
	
}

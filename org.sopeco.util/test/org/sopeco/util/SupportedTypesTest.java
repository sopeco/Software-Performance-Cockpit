package org.sopeco.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.sopeco.util.Tools.SupportedTypes;

public class SupportedTypesTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGet() {
		assertEquals(SupportedTypes.Double, SupportedTypes.get(Double.class));
		assertEquals(SupportedTypes.Integer, SupportedTypes.get(Integer.class));
		assertNull(SupportedTypes.get(SupportedTypesTest.class));
	
		assertEquals(SupportedTypes.Integer, SupportedTypes.get("intEger"));
	}
	

}

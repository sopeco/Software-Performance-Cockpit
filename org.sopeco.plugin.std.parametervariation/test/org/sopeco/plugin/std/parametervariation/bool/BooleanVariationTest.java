package org.sopeco.plugin.std.parametervariation.bool;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.sopeco.engine.helper.ConfigurationBuilder;
import org.sopeco.model.configuration.environment.ParameterDefinition;
import org.sopeco.model.configuration.environment.ParameterRole;
import org.sopeco.model.configuration.measurements.DynamicValueAssignment;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.util.ParameterType;
import org.sopeco.plugin.std.parametervariation.linear.LinearNumericVariation;
import org.sopeco.plugin.std.parametervariation.linear.LinearNumericVariationExtension;

public class BooleanVariationTest {

	DynamicValueAssignment dva = null;
	BooleanVariation bv = null;
	
	@Before
	public void setUp() throws Exception {
		ConfigurationBuilder builder = new ConfigurationBuilder("test");
		builder.createNamespace("initialization");
		ParameterDefinition pdef = builder.createParameter("initParameter", ParameterType.BOOLEAN, ParameterRole.INPUT);
		Map<String, String> config = new HashMap<String, String>();
		
		dva = builder.createDynamicValueAssignment(BooleanVariationExtension.NAME, pdef, config);
		bv = new BooleanVariation(null);
		bv.initialize(dva);
	}

	@Test
	public void testGet() {
		assertTrue(!bv.get(0).getValueAsBoolean());
		assertTrue(bv.get(1).getValueAsBoolean());
	}

	@Test
	public void testSize() {
		assertEquals(2, bv.size());
	}

	@Test
	public void testIterator() {
		Iterator<ParameterValue<?>> iterator = bv.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			System.out.println(iterator.next().getValueAsBoolean());
			i++;
		}
		
		assertEquals(i, bv.size());
	}

	@Test
	public void testReset() {
		testIterator();
		bv.reset();
		testIterator();
	}

	@Test
	public void testCanVary() {
		assertTrue(bv.canVary(dva));
	}

}

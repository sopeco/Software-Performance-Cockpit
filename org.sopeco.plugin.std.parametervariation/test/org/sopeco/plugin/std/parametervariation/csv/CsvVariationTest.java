package org.sopeco.plugin.std.parametervariation.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.sopeco.persistence.dataset.util.ParameterType;
import org.sopeco.persistence.entities.definition.DynamicValueAssignment;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterRole;
import org.sopeco.persistence.util.ScenarioDefinitionBuilder;
import org.sopeco.persistence.util.ScenarioDefinitionBuilder.AssignmentType;

public class CsvVariationTest {

	static final double MIN = 4;
	static final double MAX = 28.99999999999;
	static final double STEP = 12.5;
	
	DynamicValueAssignment dva = null;
	CSVVariation csvv = null;
	
	@Before
	public void setUp() throws Exception {
		ScenarioDefinitionBuilder builder = new ScenarioDefinitionBuilder("test");
		builder.createNewNamespace("initialization");
		ParameterDefinition pdef = builder.createParameter("initParameter", ParameterType.STRING, ParameterRole.INPUT);
		Map<String, String> config = new HashMap<String, String>();
		config.put("values", "testValue");
		
		builder.createMeasurementSpecification("testSpecification");
		builder.createExperimentSeriesDefinition("ES");
		dva = builder.createDynamicValueAssignment(AssignmentType.Experiment, CSVVariationExtension.NAME, pdef, config);
		csvv = new CSVVariation(null);
		csvv.initialize(dva);
	}

	@Test
	public void testGet() {
		assertEquals("testValue", csvv.get(0).getValue());
	}

	@Test
	public void testSize() {
		assertEquals(1, csvv.size());
	}

	@Test
	public void testCanVary() {
		assertTrue(csvv.canVary(dva));
	}

}

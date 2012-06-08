package org.sopeco.engine.validation;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.sopeco.engine.analysis.PredictionFunctionResult;
import org.sopeco.persistence.EntityFactory;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.entities.definition.ParameterRole;


/**
 * Test class for the {@link Validator} component.
 * 
 * @author Dennis Westermann
 * 
 */
public class ValidatorTest {

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void testValidation() throws IOException {

		DataSetAggregated measuredDataSet = DummyFactory.createDummyResultDataSet();
	
		List<String> inputParameterNames = new ArrayList<String>();
		inputParameterNames.add("default.DummyInput");
		ValidationObject valObj = new ValidationObject(measuredDataSet, "default.DummyOutput", inputParameterNames);
		
		PredictionFunctionResult predObj1 = new PredictionFunctionResult(
				"default_DummyOutput = default_DummyInput * 10", 
				EntityFactory.createParameterDefinition("default.DummyOutput", "Integer", ParameterRole.OBSERVATION), null, null);
		
		ValidationResult validationResult1 = Validator.validate(predObj1, valObj);
			
		assertNotNull(validationResult1);
		assertEquals(1, validationResult1.getNumIndepParameters());
		assertEquals(0.0, validationResult1.getMeanRelativeError());
		System.out.println("MRE: " + validationResult1.getMeanRelativeError());
		
		PredictionFunctionResult predObj2 = new PredictionFunctionResult(
				"default_DummyOutput = default_DummyInput * 100", 
				EntityFactory.createParameterDefinition("default.DummyOutput", "Integer", ParameterRole.OBSERVATION), null, null);
		
		ValidationResult validationResult2 = Validator.validate(predObj2, valObj);
			
		assertNotNull(validationResult2);
		assertEquals(1, validationResult2.getNumIndepParameters());
		assertTrue(validationResult2.getMeanRelativeError() > 0.0);
		System.out.println("MRE: " + validationResult2.getMeanRelativeError());
		
	}

}

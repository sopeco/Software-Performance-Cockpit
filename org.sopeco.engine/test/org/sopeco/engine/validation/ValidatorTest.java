/**
 * Copyright (c) 2013 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.sopeco.engine.validation;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

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

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
package org.sopeco.plugin.std.parametervariation.bool;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.util.ParameterType;
import org.sopeco.persistence.entities.definition.DynamicValueAssignment;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterRole;
import org.sopeco.persistence.util.ScenarioDefinitionBuilder;
import org.sopeco.persistence.util.ScenarioDefinitionBuilder.AssignmentType;

public class BooleanVariationTest {

	DynamicValueAssignment dva = null;
	BooleanVariation bv = null;
	
	@Before
	public void setUp() throws Exception {
		ScenarioDefinitionBuilder builder = new ScenarioDefinitionBuilder("test");
		builder.createNewNamespace("initialization");
		ParameterDefinition pdef = builder.createParameter("initParameter", ParameterType.BOOLEAN, ParameterRole.INPUT);
		Map<String, String> config = new HashMap<String, String>();
		builder.createMeasurementSpecification("testSpecification");
		builder.createExperimentSeriesDefinition("ES");
		dva = builder.createDynamicValueAssignment(AssignmentType.Experiment, BooleanVariationExtension.NAME, pdef, config);
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

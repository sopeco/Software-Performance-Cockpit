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
package org.sopeco.plugin.std.parametervariation.linear;

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

public class LinearNumericVariationTest {

	static final double MIN = 4;
	static final double MAX = 28.99999999999;
	static final double STEP = 12.5;
	
	DynamicValueAssignment dva = null;
	LinearNumericVariation lnv = null;
	
	@Before
	public void setUp() throws Exception {
		ScenarioDefinitionBuilder builder = new ScenarioDefinitionBuilder("test");
		builder.createNewNamespace("initialization");
		ParameterDefinition pdef = builder.createParameter("initParameter", ParameterType.DOUBLE, ParameterRole.INPUT);
		Map<String, String> config = new HashMap<String, String>();
		config.put("min", String.valueOf(MIN));
		config.put("max", String.valueOf(MAX));
		config.put("step", String.valueOf(STEP));
		
		builder.createMeasurementSpecification("testSpecification");
		builder.createExperimentSeriesDefinition("ES");
		dva = builder.createDynamicValueAssignment(AssignmentType.Experiment, LinearNumericVariationExtension.NAME, pdef, config);
		lnv = new LinearNumericVariation(null);
		lnv.initialize(dva);
	}

	@Test
	public void testGet() {
		assertEquals(MIN, lnv.get(0).getValueAsDouble(), 0.0001);
		assertEquals(MIN + STEP, lnv.get(1).getValueAsDouble(), 0.0001);
	}

	@Test
	public void testSize() {
		assertEquals(2, lnv.size());
	}

	@Test
	public void testIterator() {
		Iterator<ParameterValue<?>> iterator = lnv.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			System.out.println(iterator.next().getValueAsDouble());
			i++;
		}
		
		assertEquals(i, lnv.size());
	}

	@Test
	public void testReset() {
		testIterator();
		lnv.reset();
		testIterator();
	}

	@Test
	public void testCanVary() {
		assertTrue(lnv.canVary(dva));
	}

}

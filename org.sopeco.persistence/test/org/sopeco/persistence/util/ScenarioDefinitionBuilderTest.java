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
package org.sopeco.persistence.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.sopeco.persistence.dataset.util.ParameterType;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.persistence.entities.definition.ParameterRole;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;
import org.sopeco.persistence.util.ScenarioDefinitionBuilder.AssignmentType;

/**
 * Test class for the {@link ScenarioDefinitionBuilder}.
 * 
 * @author Dennis Westermann
 * 
 */
public class ScenarioDefinitionBuilderTest {

	ScenarioDefinitionBuilder builder; 

	@Before
	public void setUp() throws Exception {
		this.builder = new ScenarioDefinitionBuilder("TestScenario");
	}

	@Test
	public void testCreateMeasurementEnvironmentDefinition() {
		
		buildMeasurementEnvironmentDefinition();
		
		ScenarioDefinition scenarioDefinition = builder.getScenarioDefinition();
		assertNotNull(scenarioDefinition);
		assertNotNull(scenarioDefinition.getMeasurementEnvironmentDefinition());
		assertEquals("", scenarioDefinition.getMeasurementEnvironmentDefinition().getRoot().getName());
		assertEquals(2, scenarioDefinition.getMeasurementEnvironmentDefinition().getRoot().getChildren().size());
		assertNotNull(scenarioDefinition.getMeasurementEnvironmentDefinition().getNamespace("testns1.testns1-1"));
		assertEquals(2, scenarioDefinition.getMeasurementEnvironmentDefinition().getNamespace("testns1.testns1-1").getParameters().size());
		assertNotNull(scenarioDefinition.getMeasurementEnvironmentDefinition().getNamespace("testns1.testns1-2"));
		assertEquals("testns2.testparam2-1", scenarioDefinition.getMeasurementEnvironmentDefinition().getNamespace("testns2").getParameters().get(0).getFullName());
	}

	private void buildMeasurementEnvironmentDefinition() {
		builder.createNewNamespace("testns1");
		builder.createChildNamespace("testns1-1");
		builder.createParameter("testparam1-1-1", ParameterType.INTEGER, ParameterRole.INPUT);
		builder.createParameter("testparam1-1-2", ParameterType.INTEGER, ParameterRole.INPUT);
		builder.createSiblingNamespace("testns1-2");
		builder.createParameter("testparam1-2-1", ParameterType.INTEGER, ParameterRole.INPUT);
		
		builder.createNewNamespace("testns2");
		builder.createParameter("testparam2-1", ParameterType.INTEGER, ParameterRole.OBSERVATION);
	}
	
	@Test
	public void testCreateMeasurementSpecification() {
		
		buildMeasurementEnvironmentDefinition();
		
		buildMeasurementSpecification();
		
		ScenarioDefinition scenarioDefinition = builder.getScenarioDefinition();
		assertNotNull(scenarioDefinition.getMeasurementSpecifications());
		assertNotNull(scenarioDefinition.getMeasurementSpecification("TestMeasurementSpecification"));
		assertEquals(2, scenarioDefinition.getMeasurementSpecification("TestMeasurementSpecification").getExperimentSeriesDefinitions().size());
		assertEquals(1, scenarioDefinition.getMeasurementSpecification("TestMeasurementSpecification").getInitializationAssignemts().size());
		assertNotNull(scenarioDefinition.getMeasurementSpecification("TestMeasurementSpecification").getInitializationAssignemts().get(0));
		
		ExperimentSeriesDefinition testExpSeries1 = scenarioDefinition.getExperimentSeriesDefinition("TestExpSeries1");
		assertNotNull(testExpSeries1);
		assertEquals(1, testExpSeries1.getPreperationAssignments().size());
		assertNotNull(testExpSeries1.getPreperationAssignments().get(0));
		assertEquals(2, testExpSeries1.getExperimentAssignments().size());
		assertNotNull(testExpSeries1.getExperimentAssignments().get(0));
		assertNotNull(testExpSeries1.getExplorationStrategy());
		assertEquals("Random Breakdown", testExpSeries1.getExplorationStrategy().getName());
		assertEquals(2, testExpSeries1.getExplorationStrategy().getAnalysisConfigurations().size());
		assertEquals(1, testExpSeries1.getExplorationStrategy().getAnalysisConfigurations().get(0).getDependentParameters().size());
		assertEquals(2, testExpSeries1.getExplorationStrategy().getAnalysisConfigurations().get(0).getIndependentParameters().size());
	
		ExperimentSeriesDefinition testExpSeries2 = scenarioDefinition.getExperimentSeriesDefinition("TestExpSeries2");
		assertNotNull(testExpSeries2);
		assertEquals(2, testExpSeries2.getExperimentAssignments().size());
		assertNotNull(testExpSeries2.getExperimentAssignments().get(0));
		assertNotNull(testExpSeries2.getExplorationStrategy());
		assertEquals("Full Exploration", testExpSeries2.getExplorationStrategy().getName());
		assertTrue(testExpSeries2.getExplorationStrategy().getAnalysisConfigurations().isEmpty());
		
	}
	
	@SuppressWarnings("unchecked")
	private void buildMeasurementSpecification() {
		
		builder.createMeasurementSpecification("TestMeasurementSpecification");
		builder.createConstantValueAssignment(AssignmentType.Initialization, builder.getScenarioDefinition().getParameterDefinition("testns2.testparam2-1"), "1");
		
		builder.createExperimentSeriesDefinition("TestExpSeries1");
		builder.createConstantValueAssignment(AssignmentType.Preparation, builder.getScenarioDefinition().getParameterDefinition("testns1.testns1-2.testparam1-2-1"), "1");
		builder.createDynamicValueAssignment(AssignmentType.Experiment, "Linear Numeric", builder.getScenarioDefinition().getParameterDefinition("testns1.testns1-1.testparam1-1-1"), Collections.EMPTY_MAP);
		builder.createDynamicValueAssignment(AssignmentType.Experiment, "Linear Numeric", builder.getScenarioDefinition().getParameterDefinition("testns1.testns1-1.testparam1-1-2"), Collections.EMPTY_MAP);
		builder.createExplorationStrategy("Random Breakdown", Collections.EMPTY_MAP);
		builder.createAnalysisConfiguration("MARS", Collections.EMPTY_MAP);
		builder.addDependentParameter(builder.getScenarioDefinition().getParameterDefinition("testns2.testparam2-1"));
		builder.addIndependentParameter(builder.getScenarioDefinition().getParameterDefinition("testns1.testns1-1.testparam1-1-1"));
		builder.addIndependentParameter(builder.getScenarioDefinition().getParameterDefinition("testns1.testns1-1.testparam1-1-2"));
		builder.createAnalysisConfiguration("Linear Regression", Collections.EMPTY_MAP);
		builder.addDependentParameter(builder.getScenarioDefinition().getParameterDefinition("testns2.testparam2-1"));
		builder.addIndependentParameter(builder.getScenarioDefinition().getParameterDefinition("testns1.testns1-1.testparam1-1-1"));
		builder.addIndependentParameter(builder.getScenarioDefinition().getParameterDefinition("testns1.testns1-1.testparam1-1-2"));
		
		
		builder.createExperimentSeriesDefinition("TestExpSeries2");
		builder.createDynamicValueAssignment(AssignmentType.Experiment, "Linear Numeric", builder.getScenarioDefinition().getParameterDefinition("testns1.testns1-1.testparam1-1-1"), Collections.EMPTY_MAP);
		builder.createDynamicValueAssignment(AssignmentType.Experiment, "Linear Numeric", builder.getScenarioDefinition().getParameterDefinition("testns1.testns1-1.testparam1-1-2"), Collections.EMPTY_MAP);
		builder.createExplorationStrategy("Full Exploration", Collections.EMPTY_MAP);		
		
	}
}

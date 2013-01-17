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
package org.sopeco.plugin.std.analysis.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.sopeco.persistence.dataset.util.ParameterType;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterRole;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;
import org.sopeco.persistence.util.ScenarioDefinitionBuilder;
import org.sopeco.persistence.util.ScenarioDefinitionBuilder.AssignmentType;

public class DummyScenarioDefinitionFactory {

	
	@SuppressWarnings("unchecked")
	public static ScenarioDefinition createScenarioDefinition() {
		ScenarioDefinitionBuilder builder = new ScenarioDefinitionBuilder("Dummy");
		
		builder.createNewNamespace("default");
		ParameterDefinition dummyInputParam = builder.createParameter("DummyInput", ParameterType.INTEGER, ParameterRole.INPUT);
		ParameterDefinition dummyInputParam1 = builder.createParameter("DummyInput1", ParameterType.INTEGER, ParameterRole.INPUT);
		ParameterDefinition dummyInputParam2 = builder.createParameter("DummyInput2", ParameterType.INTEGER, ParameterRole.INPUT);
		ParameterDefinition dummyOutputParam = builder.createParameter("DummyOutput", ParameterType.INTEGER, ParameterRole.OBSERVATION);
		
		builder.createMeasurementSpecification("DummyMeasurementSpecification");
		
		builder.createExperimentSeriesDefinition("Dummy0");
		builder.createDynamicValueAssignment(AssignmentType.Experiment, "Linear Numeric Variation", dummyInputParam, Collections.EMPTY_MAP);
		builder.createExplorationStrategy("Full Exploration Strategy", Collections.EMPTY_MAP);	
		builder.createAnalysisConfiguration("MARS", Collections.EMPTY_MAP);
		builder.addDependentParameter(dummyOutputParam);
		builder.addIndependentParameter(dummyInputParam);
		
		builder.createExperimentSeriesDefinition("Dummy1");
		builder.createDynamicValueAssignment(AssignmentType.Experiment, "Linear Numeric Variation", dummyInputParam, Collections.EMPTY_MAP);
		builder.createExplorationStrategy("Full Exploration Strategy", Collections.EMPTY_MAP);	
		builder.createAnalysisConfiguration("MARS", Collections.EMPTY_MAP);
		builder.addDependentParameter(dummyOutputParam);
		builder.addIndependentParameter(dummyInputParam);
		
		return builder.getScenarioDefinition();
	}
}

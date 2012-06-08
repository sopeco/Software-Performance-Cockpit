package org.sopeco.plugin.std.analysis.util;

import java.util.Collections;

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
		ParameterDefinition dummyOutputParam = builder.createParameter("DummyOutput", ParameterType.INTEGER, ParameterRole.OBSERVATION);
		
		builder.createMeasurementSpecification("DummyMeasurementSpecification");
		
		builder.createExperimentSeriesDefinition("Dummy0");
		builder.createNumberOfRunsCondition(4);
		builder.createDynamicValueAssignment(AssignmentType.Experiment, "Linear Numeric Variation", dummyInputParam, Collections.EMPTY_MAP);
		builder.createExplorationStrategy("Full Exploration Strategy", Collections.EMPTY_MAP);	
		builder.createAnalysisConfiguration("MARS", Collections.EMPTY_MAP);
		builder.addDependentParameter(dummyOutputParam);
		builder.addIndependentParameter(dummyInputParam);
		
		builder.createExperimentSeriesDefinition("Dummy1");
		builder.createNumberOfRunsCondition(4);
		builder.createDynamicValueAssignment(AssignmentType.Experiment, "Linear Numeric Variation", dummyInputParam, Collections.EMPTY_MAP);
		builder.createExplorationStrategy("Full Exploration Strategy", Collections.EMPTY_MAP);	
		builder.createAnalysisConfiguration("MARS", Collections.EMPTY_MAP);
		builder.addDependentParameter(dummyOutputParam);
		builder.addIndependentParameter(dummyInputParam);
		
		return builder.getScenarioDefinition();
	}
}

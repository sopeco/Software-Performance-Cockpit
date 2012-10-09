package org.sopeco.runner;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.sopeco.persistence.dataset.util.ParameterType;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterRole;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;
import org.sopeco.persistence.util.ScenarioDefinitionBuilder;
import org.sopeco.persistence.util.ScenarioDefinitionBuilder.AssignmentType;

public class DummyModelBuilder {
	public static final String SCENARIO_NAME = "TestScenario";
	public static final String INPUT_PARAM_1 = "inputParameter";
	public static ParameterDefinition dummyInputParam_1 = null;
	public static final String INPUT_PARAM_2 = "inputParameter2";
	public static ParameterDefinition dummyInputParam_2 = null;
	public static final String OUTPUT_PARAM = "observationParameterOne";
	public static ParameterDefinition dummyOutputParam = null;

	public static final String MEASUREMENT_SPEC_1 = "DummyMeasurementSpec_1";
	public static final String MEASUREMENT_SPEC_2 = "DummyMeasurementSpec_2";

	public static final String EXPERIMENT_SERIES_1 = "ExperimentSeries_1";
	public static final String EXPERIMENT_SERIES_2 = "ExperimentSeries_2";
	private static final int numbetOfRuns = 2;

	private static Map<String, String> linearVariationConfig;
	private static Map<String, String> linearVariationConfig2;

	static {
		linearVariationConfig = new HashMap<String, String>();
		linearVariationConfig.put("min", "1");
		linearVariationConfig.put("step", "1");
		linearVariationConfig.put("max", "2");

		linearVariationConfig2 = new HashMap<String, String>();
		linearVariationConfig2.put("min", "1");
		linearVariationConfig2.put("step", "1");
		linearVariationConfig2.put("max", "3");
	}

	public static ScenarioDefinition getReferenceScenariodefinition() {
		ScenarioDefinitionBuilder builder = new ScenarioDefinitionBuilder(SCENARIO_NAME);
		createMeasurementEnvironment(builder);

		builder.createMeasurementSpecification(MEASUREMENT_SPEC_1);
		createESD_1(builder);
		builder.createConstantValueAssignment(AssignmentType.Initialization, dummyInputParam_2, "1");

		return builder.getScenarioDefinition();
	}

	public static ScenarioDefinition getAnotherScenariodefinition() {
		ScenarioDefinitionBuilder builder = new ScenarioDefinitionBuilder(SCENARIO_NAME+"2");
		createMeasurementEnvironment(builder);

		builder.createMeasurementSpecification(MEASUREMENT_SPEC_1);
		createESD_1(builder);
		builder.createConstantValueAssignment(AssignmentType.Initialization, dummyInputParam_2, "1");

		return builder.getScenarioDefinition();
	}
	
	public static ScenarioDefinition getScenariodefinitionWithDifferentMSAndESD() {
		ScenarioDefinitionBuilder builder = new ScenarioDefinitionBuilder(SCENARIO_NAME);
		createMeasurementEnvironment(builder);

		builder.createMeasurementSpecification(MEASUREMENT_SPEC_2);
		createESD_2(builder);
		builder.createConstantValueAssignment(AssignmentType.Initialization, dummyInputParam_2, "1");

		return builder.getScenarioDefinition();
	}

	public static ScenarioDefinition getScenariodefinitionWithDifferentMSAndSameESD() {
		ScenarioDefinitionBuilder builder = new ScenarioDefinitionBuilder(SCENARIO_NAME);
		createMeasurementEnvironment(builder);

		builder.createMeasurementSpecification(MEASUREMENT_SPEC_2);
		createESD_1(builder);
		builder.createConstantValueAssignment(AssignmentType.Initialization, dummyInputParam_2, "1");

		return builder.getScenarioDefinition();
	}

	public static ScenarioDefinition getScenariodefinitionWithDifferentESD() {
		ScenarioDefinitionBuilder builder = new ScenarioDefinitionBuilder(SCENARIO_NAME);
		createMeasurementEnvironment(builder);

		builder.createMeasurementSpecification(MEASUREMENT_SPEC_1);
		createESD_2(builder);
		builder.createConstantValueAssignment(AssignmentType.Initialization, dummyInputParam_2, "1");

		return builder.getScenarioDefinition();
	}

	public static ScenarioDefinition getScenariodefinitionWithModifiedESD() {
		ScenarioDefinitionBuilder builder = new ScenarioDefinitionBuilder(SCENARIO_NAME);
		createMeasurementEnvironment(builder);

		builder.createMeasurementSpecification(MEASUREMENT_SPEC_1);
		createModifiedESD_1(builder);
		builder.createConstantValueAssignment(AssignmentType.Initialization, dummyInputParam_2, "1");

		return builder.getScenarioDefinition();
	}

	public static ScenarioDefinition getScenariodefinitionWithDifferentInitializations() {
		ScenarioDefinitionBuilder builder = new ScenarioDefinitionBuilder(SCENARIO_NAME);
		createMeasurementEnvironment(builder);

		builder.createMeasurementSpecification(MEASUREMENT_SPEC_1);
		createESD_1(builder);
		builder.createConstantValueAssignment(AssignmentType.Initialization, dummyInputParam_2, "2");

		return builder.getScenarioDefinition();
	}

	public static ScenarioDefinition getScenariodefinitionWithDifferentInitializationsAndESD() {
		ScenarioDefinitionBuilder builder = new ScenarioDefinitionBuilder(SCENARIO_NAME);
		createMeasurementEnvironment(builder);

		builder.createMeasurementSpecification(MEASUREMENT_SPEC_1);
		createESD_2(builder);
		builder.createConstantValueAssignment(AssignmentType.Initialization, dummyInputParam_2, "2");

		return builder.getScenarioDefinition();
	}

	public static void createESD_1(ScenarioDefinitionBuilder builder) {
		builder.createExperimentSeriesDefinition(EXPERIMENT_SERIES_1);
		builder.createNumberOfRunsCondition(numbetOfRuns);
		builder.createDynamicValueAssignment(AssignmentType.Experiment, "Linear Numeric Variation", dummyInputParam_1,
				linearVariationConfig);

		builder.createExplorationStrategy("Full Exploration Strategy", Collections.EMPTY_MAP);
	}

	public static void createESD_2(ScenarioDefinitionBuilder builder) {
		builder.createExperimentSeriesDefinition(EXPERIMENT_SERIES_2);
		builder.createNumberOfRunsCondition(numbetOfRuns);
		builder.createDynamicValueAssignment(AssignmentType.Experiment, "Linear Numeric Variation", dummyInputParam_1,
				linearVariationConfig);
		builder.createExplorationStrategy("Full Exploration Strategy", Collections.EMPTY_MAP);
	}

	public static void createModifiedESD_1(ScenarioDefinitionBuilder builder) {
		builder.createExperimentSeriesDefinition(EXPERIMENT_SERIES_1);
		builder.createNumberOfRunsCondition(numbetOfRuns);
		builder.createDynamicValueAssignment(AssignmentType.Experiment, "Linear Numeric Variation", dummyInputParam_1,
				linearVariationConfig2);
		builder.createExplorationStrategy("Full Exploration Strategy", Collections.EMPTY_MAP);
	}

	public static void createMeasurementEnvironment(ScenarioDefinitionBuilder builder) {
		builder.createNewNamespace("test");
		builder.createChildNamespace("input");
		dummyInputParam_1 = builder.createParameter(INPUT_PARAM_1, ParameterType.INTEGER, ParameterRole.INPUT);
		dummyInputParam_2 = builder.createParameter(INPUT_PARAM_2, ParameterType.INTEGER, ParameterRole.INPUT);
		builder.createSiblingNamespace("observation");
		dummyOutputParam = builder.createParameter(OUTPUT_PARAM, ParameterType.INTEGER, ParameterRole.OBSERVATION);
	}
}

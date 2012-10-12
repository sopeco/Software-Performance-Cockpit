package org.sopeco.persistence.util;

import java.util.List;
import java.util.Map;

import org.sopeco.persistence.EntityFactory;
import org.sopeco.persistence.dataset.util.ParameterType;
import org.sopeco.persistence.entities.definition.AnalysisConfiguration;
import org.sopeco.persistence.entities.definition.ConstantValueAssignment;
import org.sopeco.persistence.entities.definition.DynamicValueAssignment;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.persistence.entities.definition.ExperimentTerminationCondition;
import org.sopeco.persistence.entities.definition.ExplorationStrategy;
import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.entities.definition.MeasurementSpecification;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterNamespace;
import org.sopeco.persistence.entities.definition.ParameterRole;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;

/**
 * Builder to create SoPeCo Scenario Definitions from code instead of the EMF
 * editor.
 * 
 * @author Dennis Westermann
 * 
 */
public class ScenarioDefinitionBuilder {

	private ScenarioDefinition scenarioDefinition;
	private ParameterNamespace currentNamespace;
	private ExperimentSeriesDefinition currentExperimentSeriesDefintition;
	private ExplorationStrategy currentExplorationStrategy;
	private AnalysisConfiguration currentAnalysisConfiguration;
	private MeasurementSpecification currentMeasurementSpecification;

	/**
	 * Defines the type of assignment for a parameter in an
	 * {@link MeasurementSpecification}.
	 */
	public enum AssignmentType {
		Initialization, Preparation, Experiment
	};

	/**
	 * Creates an instance of {@link ScenarioDefinition} with the given name.
	 * Moreover it creates an empty {@link MeasurementEnvironmentDefinition},
	 * {@link MeasurementSpecification} and root namespace.
	 * 
	 * @param scenarioName
	 *            the name of the scenario definition to create
	 */
	public ScenarioDefinitionBuilder(String scenarioName) {
		scenarioDefinition = EntityFactory.createScenarioDefinition(scenarioName);
		scenarioDefinition.setScenarioName(scenarioName);

		MeasurementEnvironmentDefinition meDefinition = EntityFactory.createMeasurementEnvironmentDefinition();
		scenarioDefinition.setMeasurementEnvironmentDefinition(meDefinition);

		ParameterNamespace root = EntityFactory.createNamespace("");
		meDefinition.setRoot(root);
		currentNamespace = root;
	}

	/*
	 * Create MeasurementEnvironmentDefinition
	 */

	/**
	 * Creates a new namespace and adds it to the root namespace of the
	 * {@link MeasurementEnvironmentDefinition}.
	 * 
	 * @param name
	 *            the name of the namespace to create (Note: not the full name)
	 */
	public void createNewNamespace(String name) {
		ParameterNamespace namespace = EntityFactory.createNamespace(name);
		ParameterNamespace root = this.scenarioDefinition.getMeasurementEnvironmentDefinition().getRoot();
		root.getChildren().add(namespace);
		namespace.setParent(root);
		currentNamespace = namespace;
	}

	/**
	 * Creates a new namespace and adds it to the last created namespace.
	 * 
	 * @param name
	 *            the name of the namespace to create (Note: not the full name)
	 */
	public void createChildNamespace(String name) {
		ParameterNamespace namespace = EntityFactory.createNamespace(name);
		currentNamespace.getChildren().add(namespace);
		namespace.setParent(currentNamespace);
		currentNamespace = namespace;
	}

	/**
	 * Creates a new namespace and adds it to the parent namespace of the last
	 * created namespace.
	 * 
	 * @param name
	 *            the name of the namespace to create (Note: not the full name)
	 */
	public void createSiblingNamespace(String name) {
		ParameterNamespace namespace = EntityFactory.createNamespace(name);
		currentNamespace.getParent().getChildren().add(namespace);
		namespace.setParent(currentNamespace.getParent());
		currentNamespace = namespace;
	}

	/**
	 * Creates a new {@link ParameterDefinition} object with the given
	 * properties and adds it to the last created namespace.
	 * 
	 * @param name
	 *            the name of the parameter
	 * @param type
	 *            the type of the parameter
	 * @param role
	 *            the role of the parameter
	 * @return the created {@link ParameterRole} definition
	 */
	public ParameterDefinition createParameter(String name, ParameterType type, ParameterRole role) {
		ParameterDefinition parameter = EntityFactory.createParameterDefinition(name, type.toString(), role);
		currentNamespace.getParameters().add(parameter);
		parameter.setNamespace(currentNamespace);
		return parameter;
	}

	/*
	 * Create MeasurementSpecification
	 */

	/**
	 * Creates a new {@link MeasurementSpecification} with the given name and
	 * adds it to the current {@link ScenarioDefinition}.
	 * 
	 * @param name
	 *            the name of the measurement specification to create
	 */
	public void createMeasurementSpecification(String name) {
		MeasurementSpecification measurementSpecification = EntityFactory.createMeasurementSpecification(name);
		this.scenarioDefinition.getMeasurementSpecifications().add(measurementSpecification);
		this.currentMeasurementSpecification = measurementSpecification;
	}

	/**
	 * Creates a new {@link ExperimentSeriesDefinition} with the given name and
	 * adds it to the {@link MeasurementSpecification}.
	 * 
	 * @param name
	 *            the name of the experiment series to create
	 */
	public void createExperimentSeriesDefinition(String name) {
		ExperimentSeriesDefinition expSeriesDef = new ExperimentSeriesDefinition();
		expSeriesDef.setName(name);
		this.currentMeasurementSpecification.getExperimentSeriesDefinitions().add(expSeriesDef);
		this.currentExperimentSeriesDefintition = expSeriesDef;
	}

	/**
	 * Creates a new {@link ExperimentTerminationCondition} with the given
	 * properties and adds it to the last created
	 * {@link ExperimentSeriesDefinition}.
	 * 
	 * @param name
	 *            the name of the termination condition strategy to create
	 * @param configuration
	 *            the configuration of the termination condition to create
	 */
	public void createExperimentTerminationCondition(String name, Map<String, String> configuration) {
		ExperimentTerminationCondition nor = EntityFactory.createTerminationCondition(name, configuration);
		this.currentExperimentSeriesDefintition.setExperimentTerminationCondition(nor);
	}

	/**
	 * Creates a new {@link DynamicValueAssignment} with the given properties
	 * and adds it to the experiment assignment list of the last created
	 * {@link ExperimentSeriesDefinition}.
	 * 
	 * @param type
	 *            the type of the assignment (Note: only Experiment type is
	 *            allowed for {@link DynamicValueAssignment}s)
	 * @param name
	 *            the name of the assignment
	 * @param parameter
	 *            the parameter definition for which the assignment should be
	 *            created
	 * @param configuration
	 *            the configuration values for the assignment
	 * @return the created {@link DynamicValueAssignment} instance
	 */
	public DynamicValueAssignment createDynamicValueAssignment(AssignmentType type, String name,
			ParameterDefinition parameter, Map<String, String> configuration) {
		DynamicValueAssignment dva = EntityFactory.createDynamicValueAssignment(name, parameter, configuration);
		switch (type) {
		case Experiment:
			this.currentExperimentSeriesDefintition.getExperimentAssignments().add(dva);
			return dva;
		default:
			throw new IllegalArgumentException(
					"Invalid assignment type. DynamicValueAssignments are only allowed as ExperimentAssignments.");
		}
	}

	/**
	 * Creates a new {@link ConstantValueAssignment} with the given properties.
	 * Based on the the given type it either adds it to the initialization
	 * assignment list in the {@link MeasurementSpecification} or to the
	 * preparation/experiment assignment lists of the last created
	 * {@link ExperimentSeriesDefinition}.
	 * 
	 * @param type
	 *            the type of the assignment (Note: only Experiment type is
	 *            allowed for {@link DynamicValueAssignment}s)
	 * @param parameter
	 *            the parameter definition for which the assignment should be
	 *            created
	 * @param value
	 *            the constant value for the assignment in String representation
	 * @return the created {@link DynamicValueAssignment} instance
	 */
	public ConstantValueAssignment createConstantValueAssignment(AssignmentType type, ParameterDefinition parameter,
			String value) {
		ConstantValueAssignment cva = EntityFactory.createConstantValueAssignment(parameter, value);

		switch (type) {
		case Initialization:
			this.currentMeasurementSpecification.getInitializationAssignemts().add(cva);
			break;
		case Preparation:
			this.currentExperimentSeriesDefintition.getPreperationAssignments().add(cva);
			break;
		case Experiment:
			this.currentExperimentSeriesDefintition.getExperimentAssignments().add(cva);
			break;
		default:
			throw new IllegalArgumentException("Invalid assignment type.");
		}

		return cva;
	}

	/**
	 * Creates a new {@link ExplorationStrategy} with the given properties and
	 * adds it to the last created {@link ExperimentSeriesDefinition}.
	 * 
	 * @param name
	 *            the name of the exploration strategy to create
	 * @param configuration
	 *            the configuration of the exploration strategy to create
	 */
	public void createExplorationStrategy(String name, Map<String, String> configuration) {
		ExplorationStrategy explStrategy = EntityFactory.createExplorationStrategy(name, configuration);
		this.currentExperimentSeriesDefintition.setExplorationStrategy(explStrategy);
		this.currentExplorationStrategy = explStrategy;
	}

	/**
	 * Creates a new {@link AnalysisConfiguration} with the given properties and
	 * adds it to the last created {@link ExplorationStrategy}.
	 * 
	 * @param name
	 *            the name of the analysis configuration to create
	 * @param configuration
	 *            the configuration of the analysis configuration to create
	 */
	public void createAnalysisConfiguration(String name, Map<String, String> configuration) {
		AnalysisConfiguration analysisConfig = EntityFactory.createAnalysisConfiguration(name, configuration);
		this.currentExplorationStrategy.getAnalysisConfigurations().add(analysisConfig);
		this.currentAnalysisConfiguration = analysisConfig;
	}

	/**
	 * Adds the given parameter to the dependent parameter list of the last
	 * created {@link AnalysisConfiguration}
	 * 
	 * @param dependentParameter
	 *            the dependent parameter to add
	 */
	public void addDependentParameter(ParameterDefinition dependentParameter) {
		this.currentAnalysisConfiguration.getDependentParameters().add(dependentParameter);
	}

	/**
	 * Adds the given list of parameters to the dependent
	 * {@link ParameterDefinition} list of the last created
	 * {@link AnalysisConfiguration}.
	 * 
	 * @param dependentParameterList
	 *            the list of dependent parameters to add
	 */
	public void addAllDependentParameters(List<ParameterDefinition> dependentParameterList) {
		this.currentAnalysisConfiguration.getDependentParameters().addAll(dependentParameterList);
	}

	/**
	 * Adds the given parameter to the independent parameter list of the last
	 * created {@link AnalysisConfiguration}
	 * 
	 * @param independentParameter
	 *            the independent parameter to add
	 */
	public void addIndependentParameter(ParameterDefinition independentParameter) {
		this.currentAnalysisConfiguration.getIndependentParameters().add(independentParameter);
	}

	/**
	 * Adds the given list of parameters to the independent
	 * {@link ParameterDefinition} list of the last created
	 * {@link AnalysisConfiguration}.
	 * 
	 * @param independentParameterList
	 *            the list of independent parameters to add
	 */
	public void addAllIndependentParameters(List<ParameterDefinition> independentParameterList) {
		this.currentAnalysisConfiguration.getIndependentParameters().addAll(independentParameterList);
	}

	/*
	 * Return created entities
	 */

	/**
	 * @return the scenario definition that has been created by the previous
	 *         method calls
	 */
	public ScenarioDefinition getScenarioDefinition() {
		return this.scenarioDefinition;
	}

	/**
	 * @return the experiment series definition that has been created by the
	 *         previous method calls
	 */
	public ExperimentSeriesDefinition getCurrentExperimentSeriesDefinition() {
		return this.currentExperimentSeriesDefintition;
	}

}

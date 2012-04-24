package org.sopco.model.configuration.helper;

import java.util.Map;
import java.util.Map.Entry;

import org.sopeco.model.configuration.ConfigurationFactory;
import org.sopeco.model.configuration.ScenarioDefinition;
import org.sopeco.model.configuration.SoPeCoModelFactoryHandler;
import org.sopeco.model.configuration.environment.EnvironmentFactory;
import org.sopeco.model.configuration.environment.MeasurementEnvironmentDefinition;
import org.sopeco.model.configuration.environment.ParameterDefinition;
import org.sopeco.model.configuration.environment.ParameterNamespace;
import org.sopeco.model.configuration.environment.ParameterRole;
import org.sopeco.model.configuration.measurements.DynamicValueAssignment;
import org.sopeco.model.configuration.measurements.ExperimentTerminationCondition;
import org.sopeco.model.configuration.measurements.MeasurementsFactory;
import org.sopeco.model.configuration.measurements.NumberOfRepetitions;


public class ConfigurationBuilder {
	
	ScenarioDefinition scenarioDefinition;
	
	ParameterNamespace currentNamespace;
	
	ParameterDefinition currentParameter;

	private ExperimentTerminationCondition terminationCondition;
	
	public ConfigurationBuilder(String scenarioName){
		scenarioDefinition = ConfigurationFactory.eINSTANCE.createScenarioDefinition();
		scenarioDefinition.setName(scenarioName);
		
		MeasurementEnvironmentDefinition meDefinition = EnvironmentFactory.eINSTANCE.createMeasurementEnvironmentDefinition();
		scenarioDefinition.setMeasurementEnvironmentDefinition(meDefinition);

		ParameterNamespace root = EnvironmentFactory.eINSTANCE.createParameterNamespace();
		root.setName("");
		
		meDefinition.setRoot(root);
		currentNamespace = root;
	}

	public void createNamespace(String name) {
		ParameterNamespace root = scenarioDefinition.getMeasurementEnvironmentDefinition().getRoot();
		ParameterNamespace namespace = EnvironmentFactory.eINSTANCE.createParameterNamespace();
		namespace.setName(name);
		namespace.setParent(currentNamespace);
		root.getChildren().add(namespace);
		currentNamespace = namespace;
	}

	public ParameterDefinition createParameter(String name, String type, ParameterRole role) {
		ParameterDefinition parameter = SoPeCoModelFactoryHandler.getEnvironmentFactory().createParameterDefinition();
		parameter.setName(name);
		parameter.setType(type.toString());
		parameter.setRole(role);
		parameter.setNamespace(currentNamespace);
		currentNamespace.getParameters().add(parameter);
		currentParameter = parameter;
		return parameter;
	}

	public DynamicValueAssignment createDynamicValueAssignment(String name, ParameterDefinition parameter, Map<String, String> configuration) {
		DynamicValueAssignment pva = MeasurementsFactory.eINSTANCE.createDynamicValueAssignment();
		pva.setParameter(parameter);
		for (Entry<String, String> e: configuration.entrySet())
			pva.getConfiguration().put(e.getKey(), e.getValue());
		pva.setName(name);
		return pva;
	}

	public ParameterDefinition getCurrentParameter() {
		return currentParameter;
	}

	public ExperimentTerminationCondition getTerminationCondition() {
		return terminationCondition;
	}

	public void createNumberOfRunsCondition(int numberOfRepetitions) {
		NumberOfRepetitions nor = MeasurementsFactory.eINSTANCE.createNumberOfRepetitions();
		nor.setNumberOfRepetitions(numberOfRepetitions);
		terminationCondition = nor;
	}

}
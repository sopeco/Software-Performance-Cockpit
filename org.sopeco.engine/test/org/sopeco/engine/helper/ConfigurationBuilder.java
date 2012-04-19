package org.sopeco.engine.helper;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.model.configuration.ConfigurationFactory;
import org.sopeco.model.configuration.ScenarioDefinition;
import org.sopeco.model.configuration.environment.EnvironmentFactory;
import org.sopeco.model.configuration.environment.MeasurementEnvironmentDefinition;
import org.sopeco.model.configuration.environment.ParameterDefinition;
import org.sopeco.model.configuration.environment.ParameterNamespace;
import org.sopeco.model.configuration.environment.ParameterRole;
import org.sopeco.model.configuration.measurements.ExperimentTerminationCondition;
import org.sopeco.model.configuration.measurements.MeasurementsFactory;
import org.sopeco.model.configuration.measurements.NumberOfRepetitions;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.ParameterValueFactory;
import org.sopeco.persistence.dataset.util.ParameterType;


public class ConfigurationBuilder {
	
	ScenarioDefinition scenarioDefinition;
	
	ParameterNamespace currentNamespace;
	
	ParameterDefinition currentParameter;

	private List<ParameterValue<?>> pvList = new ArrayList<ParameterValue<?>>();

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
		root.getChildren().add(namespace);
		currentNamespace = namespace;
	}

	public void createParameter(String name, ParameterType type, ParameterRole role) {
		ParameterDefinition parameter = EnvironmentFactory.eINSTANCE.createParameterDefinition();
		parameter.setName(name);
		parameter.setType(type.toString());
		parameter.setRole(role);
		currentNamespace.getParameters().add(parameter);
		currentParameter = parameter;
	}

	public void createParameterValue(Object value) {
		org.sopeco.persistence.dataset.ParameterValue<?> parameterValue = ParameterValueFactory.createParameterValue(currentParameter, value);
		pvList.add(parameterValue);
	}

	public List<ParameterValue<?>> getPVList() {
		return pvList;
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

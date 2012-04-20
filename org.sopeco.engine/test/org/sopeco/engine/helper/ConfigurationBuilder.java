package org.sopeco.engine.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.common.util.EMap;
import org.sopeco.model.configuration.ConfigurationFactory;
import org.sopeco.model.configuration.ScenarioDefinition;
import org.sopeco.model.configuration.environment.EnvironmentFactory;
import org.sopeco.model.configuration.environment.MeasurementEnvironmentDefinition;
import org.sopeco.model.configuration.environment.ParameterDefinition;
import org.sopeco.model.configuration.environment.ParameterNamespace;
import org.sopeco.model.configuration.environment.ParameterRole;
import org.sopeco.model.configuration.measurements.DynamicValueAssignment;
import org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition;
import org.sopeco.model.configuration.measurements.ExperimentTerminationCondition;
import org.sopeco.model.configuration.measurements.MeasurementsFactory;
import org.sopeco.model.configuration.measurements.NumberOfRepetitions;
import org.sopeco.model.configuration.measurements.ParameterValueAssignment;
import org.sopeco.model.configuration.measurements.impl.ParameterValueAssignmentImpl;
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

	public ParameterDefinition createParameter(String name, ParameterType type, ParameterRole role) {
		ParameterDefinition parameter = EnvironmentFactory.eINSTANCE.createParameterDefinition();
		parameter.setName(name);
		parameter.setType(type.toString());
		parameter.setRole(role);
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

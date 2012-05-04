package org.sopeco.engine.helper;

import java.util.Map;
import java.util.Map.Entry;

import org.sopeco.engine.experiment.IExperimentController;
import org.sopeco.engine.experiment.impl.ExperimentController;
import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.engine.util.ParameterCollection;
import org.sopeco.engine.util.ParameterCollectionFactory;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.ParameterValueFactory;
import org.sopeco.persistence.dataset.util.ParameterType;
import org.sopeco.persistence.entities.definition.DynamicValueAssignment;
import org.sopeco.persistence.entities.definition.ExperimentTerminationCondition;
import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.entities.definition.NumberOfRepetitions;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterNamespace;
import org.sopeco.persistence.entities.definition.ParameterRole;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;


public class ConfigurationBuilder {
	
	ScenarioDefinition scenarioDefinition;
	
	ParameterNamespace currentNamespace;
	
	ParameterDefinition currentParameter;
	
	private ParameterCollection<ParameterValue<?>> pvList = ParameterCollectionFactory.createParameterValueCollection();

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

	public IExperimentController createExperimentController(IMeasurementEnvironmentController meController) {
		ExperimentController expController = new ExperimentController();
		expController.setMeasurementEnvironmentController(meController);
		return expController;
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

	public ParameterCollection<ParameterValue<?>> getPVList() {
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

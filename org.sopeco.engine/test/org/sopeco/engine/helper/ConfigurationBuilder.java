package org.sopeco.engine.helper;

import java.util.Map;

import org.sopeco.engine.experiment.IExperimentController;
import org.sopeco.engine.experiment.impl.ExperimentController;
import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.persistence.EntityFactory;
import org.sopeco.persistence.PersistenceProviderFactory;
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
import org.sopeco.persistence.util.ParameterCollection;
import org.sopeco.persistence.util.ParameterCollectionFactory;


public class ConfigurationBuilder {
	
	ScenarioDefinition scenarioDefinition;
	
	ParameterNamespace currentNamespace;
	
	ParameterDefinition currentParameter;
	
	private ParameterCollection<ParameterValue<?>> pvList = ParameterCollectionFactory.createParameterValueCollection();

	private ExperimentTerminationCondition terminationCondition;
	
	public ConfigurationBuilder(String scenarioName){
		scenarioDefinition = EntityFactory.createScenarioDefinition(scenarioName);
		scenarioDefinition.setName(scenarioName);
		
		MeasurementEnvironmentDefinition meDefinition = EntityFactory.createMeasurementEnvironmentDefinition();
		scenarioDefinition.setMeasurementEnvironmentDefinition(meDefinition);
		
		ParameterNamespace root = EntityFactory.createNamespace("");
		meDefinition.setRoot(root);
		currentNamespace = root;
	}

	public void createNamespace(String name) {
		ParameterNamespace namespace = EntityFactory.createNamespace(name);
		currentNamespace.getChildren().add(namespace);
		namespace.setParent(currentNamespace);
		currentNamespace = namespace;
	}

	public IExperimentController createExperimentController(IMeasurementEnvironmentController meController) {
		ExperimentController expController = new ExperimentController();
		expController.setMeasurementEnvironmentController(meController);
		expController.setPersistenceProvider(PersistenceProviderFactory.getPersistenceProvider());
		return expController;
	}

	public ParameterDefinition createParameter(String name, ParameterType type, ParameterRole role) {
		ParameterDefinition parameter = EntityFactory.createParameterDefinition(
				name, type.toString(), role);
		currentNamespace.getParameters().add(parameter);
		currentParameter = parameter;
		return parameter;
	}

	public DynamicValueAssignment createDynamicValueAssignment(String name, ParameterDefinition parameter, Map<String, String> configuration) {
		DynamicValueAssignment pva = EntityFactory.createDynamicValueAssignment(name, parameter, configuration);
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
		NumberOfRepetitions nor = EntityFactory.createNumberOfRepetitionsTerminationCondition(numberOfRepetitions);
		terminationCondition = nor;
	}

}

package org.sopeco.persistence;

import java.util.Map;

import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.ScenarioInstance;
import org.sopeco.persistence.entities.definition.AnalysisConfiguration;
import org.sopeco.persistence.entities.definition.ConstantValueAssignment;
import org.sopeco.persistence.entities.definition.DynamicValueAssignment;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.persistence.entities.definition.ExperimentTerminationCondition;
import org.sopeco.persistence.entities.definition.ExplorationStrategy;
import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.entities.definition.MeasurementSpecification;
import org.sopeco.persistence.entities.definition.NumberOfRepetitions;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterNamespace;
import org.sopeco.persistence.entities.definition.ParameterRole;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;
import org.sopeco.persistence.entities.definition.TimeOut;

/**
 * The factory is used to create new instances of the SoPeCo persistence entities.
 * 
 * @author Dennis Westermann
 *
 */
public class EntityFactory {
	
	/**
	 * Creates a new instance of the {@link ScenarioInstance} entity. Sets the name and measurementEnvironmentUrl according to the given parameters.
	 * 
	 * @param scenarioDefinition
	 * @param measurementEnvironmentUrl
	 * 
	 * @return a new instance of {@link ScenarioInstance} based on the given parameters
	 */
	public static ScenarioInstance createScenarioInstance(ScenarioDefinition scenarioDefinition, String measurementEnvironmentUrl){
		ScenarioInstance si = new ScenarioInstance();
		si.setScenarioDefinition(scenarioDefinition);
		si.setName(scenarioDefinition.getName());
		si.setMeasurementEnvironmentUrl(measurementEnvironmentUrl);
		return si;
	}
	
	/**
	 * Creates a new instance of the {@link ExperimentSeries} entity. 
	 * Sets the name, experimentDefintion and scenarioInstance according to the given parameters.
	 * Adds the newly created instance to the given scenarioInstance (resolving the relationship).
	 * 
	 * @param scenarioInstance
	 * @param expSeriesDefinition
	 * @return a new instance of {@link ExperimentSeries} based on the given parameters
	 */
	public static ExperimentSeries createExperimentSeries(ScenarioInstance scenarioInstance, ExperimentSeriesDefinition expSeriesDefinition){
		ExperimentSeries expSeries = new ExperimentSeries();
		expSeries.setName(expSeriesDefinition.getName());
		expSeries.setScenarioInstance(scenarioInstance);
		scenarioInstance.getExperimentSeriesList().add(expSeries);
		
		return expSeries;
	}
	
	
	/**
	 * Creates a new instance of the {@link ExperimentSeriesRun} entity. 
	 * Sets the given experimentSeries as well as the timestamp (to the current {@link System.nanoTime()}).
	 * Adds the newly created instance to the given experiment series (resolving the relationship).
	 * 
	 * @param scenarioInstance
	 * @param expSeriesDefinition
	 * @return a new instance of {@link ExperimentSeriesRun} based on the given parameters
	 */
	public static ExperimentSeriesRun createExperimentSeriesRun(ExperimentSeries experimentSeries){
		ExperimentSeriesRun expSeriesRun = new ExperimentSeriesRun();
		
		expSeriesRun.setTimestamp(System.nanoTime());
		expSeriesRun.setExperimentSeries(experimentSeries);
		experimentSeries.getExperimentSeriesRuns().add(expSeriesRun);
		return expSeriesRun;
	}
	
	
	public static ScenarioDefinition createScenarioDefinition(String name) {
		ScenarioDefinition sd = new ScenarioDefinition();
		sd.setName(name);
		return sd;
	}
	
	public static MeasurementEnvironmentDefinition createMeasurementEnvironmentDefinition(ScenarioDefinition sd) {
		MeasurementEnvironmentDefinition med = new MeasurementEnvironmentDefinition();
		sd.setMeasurementEnvironmentDefinition(med);
		return med;
	}
	
	public static ParameterNamespace createRootNamespace(String name, MeasurementEnvironmentDefinition med){
		ParameterNamespace root = new ParameterNamespace();
		med.setRoot(root);
		root.setName(name);
		return root;
	}
	
	public static ParameterNamespace createChildNamespace(String name, ParameterNamespace parent){
		ParameterNamespace child = new ParameterNamespace();
		child.setName(name);
		child.setParent(parent);
		parent.getChildren().add(child);
		return child;
	}
	
	public static ParameterDefinition createParameterDefinition(String name, String type, ParameterRole role, ParameterNamespace namespace){
		ParameterDefinition pd = new ParameterDefinition();
		pd.setName(name);
		pd.setRole(role);
		pd.setType(type);
		pd.setNamespace(namespace);
		return pd;
	}
	
	public static MeasurementSpecification createMeasurementSpecification(ScenarioDefinition scenarioDefinition){
		MeasurementSpecification ms = new MeasurementSpecification();
		scenarioDefinition.setMeasurementSpecification(ms);
		return ms;
	}
	
	public static ExperimentSeriesDefinition createExperimentSeriesDefinition(String name, ExperimentTerminationCondition terminationCondition, MeasurementSpecification ms){
		ExperimentSeriesDefinition esd = new ExperimentSeriesDefinition();
		esd.setName(name);
		esd.setExperimentTerminationCondition(terminationCondition);
		ms.getExperimentSeriesDefinitions().add(esd);
		return esd;
	}

	public static ExplorationStrategy createExplorationStrategy(String name, Map<String, String> config, ExperimentSeriesDefinition expSeriesDef){
		ExplorationStrategy es = new ExplorationStrategy();
		es.setName(name);
		es.getConfiguration().putAll(config);
		expSeriesDef.setExplorationStrategy(es);
		return es;
	}
	
	public static AnalysisConfiguration createAnalysisConfiguration(String name, Map<String, String> config, ExplorationStrategy explorationStategy){
		AnalysisConfiguration ac = new AnalysisConfiguration();
		ac.setName(name);
		ac.getConfiguration().putAll(config);
		explorationStategy.getAnalysisConfigurations().add(ac);
		return ac;
	}
	
	public static TimeOut createTimeOutTerminationCondition(long maxDuration){
		TimeOut timeOut = new TimeOut();
		timeOut.setMaxDuration(maxDuration);
		return timeOut;
	}
	
	public static NumberOfRepetitions createNumberOfRepetitionsTerminationCondition(long numberOfRepetitions){
		NumberOfRepetitions numRep = new NumberOfRepetitions();
		numRep.setNumberOfRepetitions(numberOfRepetitions);
		return numRep;
	}
	
	public static ConstantValueAssignment createConstantValueAssignment(ParameterDefinition parameter, String value){
		ConstantValueAssignment cva = new ConstantValueAssignment();
		cva.setParameter(parameter);
		cva.setValue(value);
		return cva;
	}

	public static DynamicValueAssignment createDynamicValueAssignment(String name, ParameterDefinition parameter, Map<String, String> config){
		DynamicValueAssignment dva = new DynamicValueAssignment();
		dva.setParameter(parameter);
		dva.setName(name);
		dva.getConfiguration().putAll(config);
		return dva;
	}
}

package org.sopeco.persistence;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.ScenarioInstance;
import org.sopeco.persistence.entities.analysis.AnalysisResultStorageContainer;
import org.sopeco.persistence.entities.analysis.IStorableAnalysisResult;
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
		si.setMeasurementEnvironmentUrl(measurementEnvironmentUrl);
		return si;
	}
	
	/**
	 * Creates a new instance of the {@link ExperimentSeries} entity. 
	 * Sets the name and experimentDefintion according to the given parameters.
	 * 
	 * @param scenarioInstance
	 * @param expSeriesDefinition
	 * @return a new instance of {@link ExperimentSeries} based on the given parameters
	 */
	public static ExperimentSeries createExperimentSeries(ExperimentSeriesDefinition expSeriesDefinition){
		ExperimentSeries expSeries = new ExperimentSeries();
		expSeries.setName(expSeriesDefinition.getName());
		expSeries.setExperimentSeriesDefinition(expSeriesDefinition);
		expSeries.setVersion(expSeriesDefinition.getVersion());
		return expSeries;
	}
	
	
	/**
	 * Creates a new instance of the {@link ExperimentSeriesRun} entity. 
	 * Sets the timestamp value to the current {@link System.nanoTime()}).
	 * 
	 * @return a new instance of {@link ExperimentSeriesRun} based on the given parameters
	 */
	public static ExperimentSeriesRun createExperimentSeriesRun(){
		ExperimentSeriesRun expSeriesRun = new ExperimentSeriesRun();
		
		expSeriesRun.setTimestamp(System.nanoTime());
		expSeriesRun.setLabel(getTimeStamp());
		return expSeriesRun;
	}
	
	public static AnalysisResultStorageContainer createAnalysisResultStorageContainer(String resultId, IStorableAnalysisResult resultObject) {
		AnalysisResultStorageContainer container = new AnalysisResultStorageContainer();
		container.setResultId(resultId);
		container.setResultObject(resultObject);
		return container;
	}
	
	public static ScenarioDefinition createScenarioDefinition(String scenarioName) {
		ScenarioDefinition sd = new ScenarioDefinition();
		sd.setScenarioName(scenarioName);
		return sd;
	}
	
	public static MeasurementEnvironmentDefinition createMeasurementEnvironmentDefinition() {
		MeasurementEnvironmentDefinition med = new MeasurementEnvironmentDefinition();
		return med;
	}
	
	
	public static ParameterNamespace createNamespace(String name){
		ParameterNamespace child = new ParameterNamespace();
		child.setName(name);
		return child;
	}
	
	public static ParameterDefinition createParameterDefinition(String name, String type, ParameterRole role){
		ParameterDefinition pd = new ParameterDefinition();
		pd.setName(name);
		pd.setRole(role);
		pd.setType(type);
		return pd;
	}
	
	public static MeasurementSpecification createMeasurementSpecification(String name){
		MeasurementSpecification ms = new MeasurementSpecification();
		ms.setName(name);
		return ms;
	}
	
	public static ExperimentSeriesDefinition createExperimentSeriesDefinition(String name, ExperimentTerminationCondition terminationCondition){
		ExperimentSeriesDefinition esd = new ExperimentSeriesDefinition();
		esd.setName(name);
		esd.setExperimentTerminationCondition(terminationCondition);
		return esd;
	}

	public static ExplorationStrategy createExplorationStrategy(String name, Map<String, String> config){
		ExplorationStrategy es = new ExplorationStrategy();
		es.setName(name);
		if (config != null){
			es.getConfiguration().putAll(config);
		}
		return es;
	}
	
	public static AnalysisConfiguration createAnalysisConfiguration(String name, Map<String, String> config){
		AnalysisConfiguration ac = new AnalysisConfiguration();
		ac.setName(name);
		ac.getConfiguration().putAll(config);
		return ac;
	}
	
	public static TimeOut createTimeOutTerminationCondition(long maxDuration){
		TimeOut timeOut = new TimeOut();
		timeOut.setMaxDuration(maxDuration);
		return timeOut;
	}
	
	public static NumberOfRepetitions createNumberOfRepetitionsTerminationCondition(int numberOfRepetitions){
		NumberOfRepetitions numRep = new NumberOfRepetitions();
		numRep.setNumberOfRepetitions(numberOfRepetitions);
		return numRep;
	}
	
	public static ConstantValueAssignment createConstantValueAssignment(final ParameterDefinition parameter, String value){
		ConstantValueAssignment cva = new ConstantValueAssignment();
		cva.setParameter(parameter);
		cva.setValue(value);
		return cva;
	}

	public static DynamicValueAssignment createDynamicValueAssignment(String name, final ParameterDefinition parameter, Map<String, String> config){
		DynamicValueAssignment dva = new DynamicValueAssignment();
		dva.setParameter(parameter);
		dva.setName(name);
		dva.getConfiguration().putAll(config);
		return dva;
	}

	// TODO remove and replace by Tools.getTimeStamp(...)
	/**
	 * Returns a time stamp of the format "yy.MM.dd - HH:mm" for the given date.
	 * 
	 * @param date
	 */
	public static String getTimeStamp(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yy.MM.dd - HH:mm");
		return formatter.format(date);
	}

	/**
	 * Returns a time stamp for the current time and date.
	 * 
	 * @see #getTimeStamp(Date)
	 */
	public static String getTimeStamp() {
		return getTimeStamp(new Date());
	}


}

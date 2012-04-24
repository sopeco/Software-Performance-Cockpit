package org.sopeco.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.emf.ecore.util.EcoreEMap;
import org.sopeco.model.configuration.analysis.AnalysisConfiguration;
import org.sopeco.model.configuration.analysis.AnalysisFactory;
import org.sopeco.model.configuration.environment.EnvironmentFactory;
import org.sopeco.model.configuration.environment.ParameterDefinition;
import org.sopeco.model.configuration.environment.ParameterRole;
import org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition;
import org.sopeco.model.configuration.measurements.ExperimentTerminationCondition;
import org.sopeco.model.configuration.measurements.ExplorationStrategy;
import org.sopeco.model.configuration.measurements.MeasurementsFactory;
import org.sopeco.model.configuration.measurements.MeasurementsPackage;
import org.sopeco.model.configuration.measurements.NumberOfRepetitions;
import org.sopeco.model.configuration.measurements.impl.ConfigurationNodeImpl;
import org.sopeco.model.configuration.measurements.impl.ExtensibleElementImpl;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.DataSetColumnBuilder;
import org.sopeco.persistence.dataset.DataSetModifier;
import org.sopeco.persistence.dataset.ParameterValueList;
import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.ScenarioInstance;

public class DummyFactory {

	
	public static ScenarioInstance createDummyScenarioInstance(){
		ScenarioInstance scenarioInstance = new ScenarioInstance();
		scenarioInstance.setName("Dummy");
		scenarioInstance.setMeasurementEnvironmentUrl("Dummy");
		scenarioInstance.getExperimentSeries().addAll(createDummyExperimentSeries(2, scenarioInstance));
		return scenarioInstance;
	}
	
	
	public static List<ExperimentSeries> createDummyExperimentSeries(int numberOfSeriesToCreate, ScenarioInstance scenarioInstance){
		ArrayList<ExperimentSeries> series = new ArrayList<ExperimentSeries>();
		
		for (int i = 0; i < numberOfSeriesToCreate; i++) {
			series.add(createDummyExperimentSeries("Dummy"+i, scenarioInstance));
		}
		
		return series;
	}
	
	
	public static ExperimentSeries createDummyExperimentSeries(String name, ScenarioInstance scenarioInstance){
		ExperimentSeries expSeries = new ExperimentSeries();
		expSeries.setExperimentSeriesDefinition(createDummyExperimentSeriesDefinition(name));
		expSeries.setName(name);
		expSeries.setScenarioInstance(scenarioInstance);
		expSeries.getExperimentSeriesRuns().addAll(createDummyExperimentSeriesRuns(10, expSeries));
		return expSeries;
	}
	
	
	public static Collection<? extends ExperimentSeriesRun> createDummyExperimentSeriesRuns(
			int numberOfRunsToCreate, ExperimentSeries expSeries) {
		ArrayList<ExperimentSeriesRun> runs = new ArrayList<ExperimentSeriesRun>();
		for (int i = 0; i < numberOfRunsToCreate; i++) {
			ExperimentSeriesRun run = new ExperimentSeriesRun();
			run.setTimestamp(System.nanoTime());
			run.setExperimentSeries(expSeries);
			run.setResultDataSet(createDummyResultDataSet());
			runs.add(run);
		}
		
		return runs;
	}


	public static DataSetAggregated createDummyResultDataSet() {
		DataSetColumnBuilder builder = new DataSetColumnBuilder();
		builder.startInputColumn(createDummyInputParameterDefinition("DummyInput"));
		builder.addInputValue(1);
		builder.addInputValue(2);
		builder.finishColumn();
		
		builder.startObservationColumn(createDummyObservationParameterDefinition("DummyOutput"));
		ArrayList<Object> obsValues1 = new ArrayList<Object>();
		obsValues1.add(10);
		obsValues1.add(10);
		builder.addObservationValues(obsValues1);
		ArrayList<Object> obsValues2 = new ArrayList<Object>();
		obsValues2.add(20);
		obsValues2.add(20);
		builder.addObservationValues(obsValues2);
		builder.finishColumn();
		
		return builder.createDataSet();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void addDummyObservationColumn(DataSetAggregated dataset){
		DataSetModifier modifier = new DataSetModifier(dataset);
		ArrayList<Object> obsValuesAdded = new ArrayList<Object>();
		obsValuesAdded.add(30);
		obsValuesAdded.add(30);
		ParameterDefinition paramDef = createDummyObservationParameterDefinition("DummyOutputAdded");
		List<ParameterValueList<?>> listOfValueLists = new ArrayList<ParameterValueList<?>>();
		listOfValueLists.add(new ParameterValueList(paramDef,obsValuesAdded));
		modifier.addObservationColumn(paramDef, listOfValueLists);
		
	}

	public static ParameterDefinition createDummyInputParameterDefinition(String name) {
		ParameterDefinition paramDef = EnvironmentFactory.eINSTANCE.createParameterDefinition();
		paramDef.setName(name);
		paramDef.setRole(ParameterRole.INPUT);
		paramDef.setType("INTEGER");
		return paramDef;
	}
	
	public static ParameterDefinition createDummyObservationParameterDefinition(String name) {
		ParameterDefinition paramDef = EnvironmentFactory.eINSTANCE.createParameterDefinition();
		paramDef.setName(name);
		paramDef.setRole(ParameterRole.OBSERVATION);
		paramDef.setType("INTEGER");
		return paramDef;
	}


	public static ExperimentSeriesDefinition createDummyExperimentSeriesDefinition(String name){
		ExperimentSeriesDefinition expSeriesDef = MeasurementsFactory.eINSTANCE.createExperimentSeriesDefinition();
		expSeriesDef.setName(name);
		expSeriesDef.setExperimentTerminationCondition(createDummyExperimentTerminationCondition());
		expSeriesDef.setExplorationStrategy(createDummyExplorationStrategy());
		return expSeriesDef;
	}
	
	public static ExperimentTerminationCondition createDummyExperimentTerminationCondition(){
		NumberOfRepetitions numRepTermCond = MeasurementsFactory.eINSTANCE.createNumberOfRepetitions();
		numRepTermCond.setNumberOfRepetitions(1);
		return numRepTermCond;
	}
	
	public static ExplorationStrategy createDummyExplorationStrategy(){
		ExplorationStrategy explorationStrategy = MeasurementsFactory.eINSTANCE.createExplorationStrategy();
		explorationStrategy.setName("Dummy");
		explorationStrategy.getAnalysisConfigurations().add(createDummyAnalysisConfiguration());
		explorationStrategy.getConfiguration().addAll(createDummyConfigurationMap());
		
		return explorationStrategy;
		
	}


	public static AnalysisConfiguration createDummyAnalysisConfiguration() {
		AnalysisConfiguration analysisConfig = AnalysisFactory.eINSTANCE.createAnalysisConfiguration();
		analysisConfig.setName("Dummy");
		analysisConfig.getConfiguration().addAll(createDummyConfigurationMap());
		return analysisConfig;
	}


	public static Collection<? extends Entry<String, String>> createDummyConfigurationMap() {
		
		EcoreEMap<String,String> emap = new EcoreEMap<String,String>(MeasurementsPackage.Literals.CONFIGURATION_NODE, ConfigurationNodeImpl.class, new ExtensibleElementImpl() {
			private static final long serialVersionUID = 1L;
		} , MeasurementsPackage.EXTENSIBLE_ELEMENT__CONFIGURATION);
		emap.put("Dummy", "Dummy");
		return emap.entrySet();
	}


	
}

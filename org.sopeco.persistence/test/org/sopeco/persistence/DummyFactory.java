package org.sopeco.persistence;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.sopeco.model.configuration.ScenarioDefinition;
import org.sopeco.model.configuration.SoPeCoModelFactoryHandler;
import org.sopeco.model.configuration.environment.EnvironmentFactory;
import org.sopeco.model.configuration.environment.ParameterDefinition;
import org.sopeco.model.configuration.environment.ParameterRole;
import org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition;
import org.sopeco.model.util.EMFUtil;
import org.sopeco.model.util.ScenarioDefinitionUtil;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.DataSetColumnBuilder;
import org.sopeco.persistence.dataset.DataSetModifier;
import org.sopeco.persistence.dataset.ParameterValueList;
import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.ScenarioInstance;

public class DummyFactory {

	private static ParameterDefinition inputParam;
	private static ParameterDefinition observationParam;
	private static ScenarioDefinition scenarioDefinition; 
	
	public static ScenarioInstance createDummyScenarioInstance() throws IOException{
		SoPeCoModelFactoryHandler.initFactories();
		scenarioDefinition = loadScenarioDefinition();
		ScenarioInstance scenarioInstance = PersistenceProviderFactory.createScenarioInstance(scenarioDefinition, "Dummy");
//		ScenarioInstance scenarioInstance = new ScenarioInstance();
//		scenarioInstance.setName("Dummy");
//		scenarioInstance.setMeasurementEnvironmentUrl("Dummy");
		createDummyExperimentSeries(scenarioInstance);
		return scenarioInstance;
	}
	
	public static ScenarioDefinition loadScenarioDefinition() throws IOException{
		return (ScenarioDefinition) EMFUtil.loadFromFilePath("test/dummy.configuration");
	}
	
	private static void createDummyExperimentSeries(ScenarioInstance scenarioInstance) throws IOException{
		
		for (ExperimentSeriesDefinition esd : scenarioInstance.getScenarioDefinition().getMeasurementSpecification().getExperimentSeriesDefinitions()) {
			ExperimentSeries es = PersistenceProviderFactory.createExperimentSeries(scenarioInstance, esd);
			createDummyExperimentSeriesRuns(10, es);
		}
		
	}
	
	
	
	static Collection<? extends ExperimentSeriesRun> createDummyExperimentSeriesRuns(
			int numberOfRunsToCreate, ExperimentSeries expSeries) throws IOException {
		ArrayList<ExperimentSeriesRun> runs = new ArrayList<ExperimentSeriesRun>();
		for (int i = 0; i < numberOfRunsToCreate; i++) {
			
			ExperimentSeriesRun run = PersistenceProviderFactory.createExperimentSeriesRun(expSeries);
			run.setResultDataSet(createDummyResultDataSet());
			runs.add(run);
		}
		
		return runs;
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static DataSetAggregated createDummyResultDataSet() throws IOException {
		if(scenarioDefinition==null) 
			scenarioDefinition = loadScenarioDefinition();
		
		DataSetColumnBuilder builder = new DataSetColumnBuilder();
		builder.startInputColumn(ScenarioDefinitionUtil.getParameterDefinition("default.DummyInput", scenarioDefinition));
		builder.addInputValue(1);
		builder.addInputValue(2);
		builder.finishColumn();
		
		ParameterDefinition paramDef = ScenarioDefinitionUtil.getParameterDefinition("default.DummyOutput", scenarioDefinition);
		ArrayList<ParameterValueList> obsValueLists = new ArrayList<ParameterValueList>();
		builder.startObservationColumn(paramDef);
		ArrayList<Object> obsValues1 = new ArrayList<Object>();
		obsValues1.add(10);
		obsValues1.add(10);
		obsValueLists.add(new ParameterValueList<Object>(paramDef, obsValues1));
		
		ArrayList<Object> obsValues2 = new ArrayList<Object>();
		obsValues2.add(20);
		obsValues2.add(20);
//		builder.addObservationValues(obsValues2);
		obsValueLists.add(new ParameterValueList(paramDef, obsValues2));
		
		builder.addObservationValueLists(obsValueLists);
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

//	public static ParameterDefinition createDummyInputParameterDefinition(String name) {
//		ParameterDefinition paramDef = EnvironmentFactory.eINSTANCE.createParameterDefinition();
//		paramDef.setName(name);
//		paramDef.setRole(ParameterRole.INPUT);
//		paramDef.setType("INTEGER");
//		inputParam = paramDef;
//		return paramDef;
//	}
//	
	public static ParameterDefinition createDummyObservationParameterDefinition(String name) {
		ParameterDefinition paramDef = EnvironmentFactory.eINSTANCE.createParameterDefinition();
		paramDef.setName(name);
		paramDef.setRole(ParameterRole.OBSERVATION);
		paramDef.setType("INTEGER");
		observationParam = paramDef;
		return paramDef; 
	}
//
//
//	public static ExperimentSeriesDefinition createDummyExperimentSeriesDefinition(String name){
//		ExperimentSeriesDefinition expSeriesDef = MeasurementsFactory.eINSTANCE.createExperimentSeriesDefinition();
//		expSeriesDef.setName(name);
//		expSeriesDef.setExperimentTerminationCondition(createDummyExperimentTerminationCondition());
//		expSeriesDef.setExplorationStrategy(createDummyExplorationStrategy());
//		expSeriesDef.getExperimentAssignments().add(createDummyLinearNumericAssigment());
//		return expSeriesDef;
//	}
//	
//	public static ParameterValueAssignment createDummyLinearNumericAssigment(){
//		ParameterValueAssignment ass = MeasurementsFactory.eINSTANCE.createDynamicValueAssignment();
//		ass.setParameter(inputParam);
//		return ass;
//	}
//	
//	public static ExperimentTerminationCondition createDummyExperimentTerminationCondition(){
//		NumberOfRepetitions numRepTermCond = MeasurementsFactory.eINSTANCE.createNumberOfRepetitions();
//		numRepTermCond.setNumberOfRepetitions(1);
//		return numRepTermCond;
//	}
//	
//	public static ExplorationStrategy createDummyExplorationStrategy(){
//		ExplorationStrategy explorationStrategy = MeasurementsFactory.eINSTANCE.createExplorationStrategy();
//		explorationStrategy.setName("Dummy");
//		explorationStrategy.getAnalysisConfigurations().add(createDummyAnalysisConfiguration());
//		explorationStrategy.getConfiguration().addAll(createDummyConfigurationMap());
//		
//		return explorationStrategy;
//		
//	}
//
//
//	public static AnalysisConfiguration createDummyAnalysisConfiguration() {
//		AnalysisConfiguration analysisConfig = AnalysisFactory.eINSTANCE.createAnalysisConfiguration();
//		analysisConfig.setName("Dummy");
//		analysisConfig.getConfiguration().addAll(createDummyConfigurationMap());
//		return analysisConfig;
//	}
//
//
//	public static Collection<? extends Entry<String, String>> createDummyConfigurationMap() {
//		
//		EcoreEMap<String,String> emap = new EcoreEMap<String,String>(MeasurementsPackage.Literals.CONFIGURATION_NODE, ConfigurationNodeImpl.class, new ExtensibleElementImpl() {
//			private static final long serialVersionUID = 1L;
//		} , MeasurementsPackage.EXTENSIBLE_ELEMENT__CONFIGURATION);
//		emap.put("Dummy", "Dummy");
//		return emap.entrySet();
//	}


	
}

package org.sopeco.engine.experiment.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.sopeco.engine.experiment.IExperimentController;
import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.model.configuration.environment.MeasurementEnvironmentDefinition;
import org.sopeco.model.configuration.environment.ParameterDefinition;
import org.sopeco.model.configuration.measurements.ExperimentTerminationCondition;
import org.sopeco.model.util.ScenarioDefinitionUtil;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.DataSetRowBuilder;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.ParameterValueList;
import org.sopeco.persistence.entities.ExperimentSeriesRun;

/**
 * This class implements the functionality required to execute a single experiment.
 * It is responsible for the communication between the SoPeCo engine and a measurement environment.
 * 
 * @author Dennis Westermann, Roozbeh Farahbod
 *
 */
public class ExperimentController implements IExperimentController {
	
	MeasurementEnvironmentDefinition meDefinition = null;
	IMeasurementEnvironmentController meController = null;
	IPersistenceProvider persistenceProvider = null;
	ExperimentSeriesRun currentExperimentSeriesRun = null;
	List<ParameterValue<?>> initializationPVList = Collections.emptyList();
	Collection<ParameterValue<?>> preparationPVList = Collections.emptyList();

	@Override
	public void initialize(List<ParameterValue<?>> initializationPVList, MeasurementEnvironmentDefinition meDefinition) {
		
		if (meController == null) {
			throw new IllegalStateException("No MeasurementEnvironmentController has been set.");
		}
		if (persistenceProvider == null) {
			throw new IllegalStateException("No PersistenceProvider has been set.");
		}
		
		this.meDefinition = meDefinition;
		
		this.initializationPVList = initializationPVList;
		
		meController.initialize(initializationPVList);
	}

	@Override
	public void prepareExperimentSeries(ExperimentSeriesRun experimentSeriesRun, Collection<ParameterValue<?>> preparationPVList) {
		if(experimentSeriesRun==null){
			throw new IllegalArgumentException("ExperimentSeriesRun must not be null.");
		}
		
		this.currentExperimentSeriesRun = experimentSeriesRun;
		this.preparationPVList = preparationPVList;
		
		meController.prepareExperimentSeries(preparationPVList);
		
		List<ParameterDefinition> observationParameterList = new ArrayList<ParameterDefinition>();
		ScenarioDefinitionUtil.collectObservationParameters(meDefinition.getRoot(), observationParameterList);
		meController.setObservationParameters(observationParameterList);
	}
	
	
	
	@Override
	public DataSetAggregated runExperiment(List<ParameterValue<?>> inputPVList, ExperimentTerminationCondition terminationCondition) {
		if (terminationCondition == null)
			throw new IllegalArgumentException("TerminationCondition must be set (not null).");
		

		DataSetAggregated experimentRunResult =  runExperimentOnME(meController, inputPVList, terminationCondition);
		
		currentExperimentSeriesRun.append(experimentRunResult);
		persistenceProvider.store(currentExperimentSeriesRun);
		
		return experimentRunResult;
	}

	public void setPersistenceProvider(IPersistenceProvider persistenceProvider) {
		this.persistenceProvider = persistenceProvider;
	}

	public void setMeasurementEnvironmentController(IMeasurementEnvironmentController meController) {
		this.meController = meController;
	}

	public void setMeasurementEnvironmentDefintion(MeasurementEnvironmentDefinition meDefinition) {
		this.meDefinition = meDefinition;
	}

	@Override
	public void finalizeExperimentSeries() {
		meController.finalizeExperimentSeries();
	}

	/**
	 * Runs the experiment defined by the input parameter-value list and the termination condition, on 
	 * the given measurement environment controller, and aggregates the results into an instance of
	 * {@link DataSetAggregated}.
	 */
	private DataSetAggregated runExperimentOnME(IMeasurementEnvironmentController meController, List<ParameterValue<?>> inputPVList, ExperimentTerminationCondition terminationCondition) {

		// 1. run the experiment
		Collection<ParameterValueList<?>> observations = meController.runExperiment(inputPVList, terminationCondition);
		
		// 2. aggregate the results
		DataSetRowBuilder builder = new DataSetRowBuilder();
		builder.startRow();
		
		// 2.1. add initialization values
		for (ParameterValue<?> pv: initializationPVList)
			builder.addInputParameterValue(pv.getParameter(), pv.getValue());

		// 2.2. add preparation values
		for (ParameterValue<?> pv: preparationPVList)
			builder.addInputParameterValue(pv.getParameter(), pv.getValue());
		
		// 2.3. add input values
		for (ParameterValue<?> parameterValue : inputPVList) 
			builder.addInputParameterValue(parameterValue.getParameter(), parameterValue.getValue());
		
		// 2.4. add observation values
		for (ParameterValueList<?> pvl: observations)
			builder.addObservationParameterValues(pvl);
		
		builder.finishRow();

		return builder.createDataSet();
	}
}

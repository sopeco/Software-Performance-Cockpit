package org.sopeco.engine.experiment.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.sopeco.engine.experiment.IExperimentController;
import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.model.configuration.environment.MeasurementEnvironmentDefinition;
import org.sopeco.model.configuration.environment.ParameterDefinition;
import org.sopeco.model.configuration.environment.ParameterNamespace;
import org.sopeco.model.configuration.environment.ParameterRole;
import org.sopeco.model.configuration.measurements.ExperimentTerminationCondition;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.entities.ExperimentSeriesRun;

/**
 * This class implements the functionality required to execute a single experiment.
 * It is responsible for the communication between the SoPeCo engine and a measurement environment.
 * 
 * @author Dennis Westermann
 *
 */
public class ExperimentController implements IExperimentController {
	
	MeasurementEnvironmentDefinition meDefinition = null;
	IMeasurementEnvironmentController meController = null;
	IPersistenceProvider persistenceProvider = null;
	ExperimentSeriesRun currentExperimentSeriesRun = null;

	@Override
	public void initialize(List<ParameterValue<?>> initializationPVList, MeasurementEnvironmentDefinition meDefinition) {
		
		if(meController == null){
			throw new IllegalStateException("No MeasurementEnvironmentController has been set.");
		}
		if(persistenceProvider == null){
			throw new IllegalStateException("No PersistenceProvider has been set.");
		}
		this.meDefinition = meDefinition;
		meController.initialize(initializationPVList);
	}

	@Override
	public void prepareExperimentSeries(ExperimentSeriesRun experimentSeriesRun, Collection<ParameterValue<?>> preparationPVList) {
		if(experimentSeriesRun==null){
			throw new IllegalArgumentException("ExperimentSeriesRun must not be null.");
		}
		
		this.currentExperimentSeriesRun = experimentSeriesRun;
		
		meController.prepareExperimentSeries(preparationPVList);
		
		List<ParameterDefinition> observationParameterList = new ArrayList<ParameterDefinition>();
		collectObservationParameters(meDefinition.getRoot(), observationParameterList);
		meController.setObservationParameters(observationParameterList);
	}

	/**
	 * Collects all observation parameters included in the given namespace. 
	 * 
	 * @param namespace
	 * @param observationParameterList - the list in which the observation parameters should be stored (must not be null)
	 */
	private void collectObservationParameters(ParameterNamespace namespace, List<ParameterDefinition> observationParameterList) {
		
		
		for(ParameterDefinition parameter : namespace.getParameters()){
			if(parameter.getRole().equals(ParameterRole.OBSERVATION)){
				observationParameterList.add(parameter);
			}
		}
		
		for(ParameterNamespace child : namespace.getChildren()){
			collectObservationParameters(child, observationParameterList);
		}
		
	}
	
	
	

	
	
	
	@Override
	public DataSetAggregated runExperiment(List<ParameterValue<?>> inputPVList, ExperimentTerminationCondition terminationCondition) {
		if (terminationCondition == null)
			throw new IllegalArgumentException("TerminationCondition must be set (not null).");
		
		DataSetAggregated experimentRunResult =  meController.runExperiment(inputPVList, terminationCondition);
		
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

}

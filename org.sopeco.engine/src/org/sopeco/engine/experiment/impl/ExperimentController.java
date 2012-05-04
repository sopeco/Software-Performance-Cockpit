package org.sopeco.engine.experiment.impl;

import java.util.Collection;

import org.sopeco.engine.experiment.IExperimentController;
import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.engine.util.ParameterCollection;
import org.sopeco.engine.util.ParameterCollectionFactory;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.DataSetRowBuilder;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.ParameterValueList;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.definition.ExperimentTerminationCondition;
import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.exceptions.DataNotFoundException;

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
	ParameterCollection<ParameterValue<?>> initializationPVs = ParameterCollectionFactory.createParameterValueCollection();
	ParameterCollection<ParameterValue<?>> preparationPVs = ParameterCollectionFactory.createParameterValueCollection();

	@Override
	public void initialize(ParameterCollection<ParameterValue<?>> initializationPVs, MeasurementEnvironmentDefinition meDefinition) {
		
		if (meController == null) {
			throw new IllegalStateException("No MeasurementEnvironmentController has been set.");
		}
		if (persistenceProvider == null) {
			throw new IllegalStateException("No PersistenceProvider has been set.");
		}
		
		this.meDefinition = meDefinition;
		
		this.initializationPVs = initializationPVs;
		
		meController.initialize(initializationPVs);
	}

	@Override
	public void prepareExperimentSeries(ExperimentSeriesRun experimentSeriesRun, ParameterCollection<ParameterValue<?>> preparationPVs) {
		if(experimentSeriesRun==null){
			throw new IllegalArgumentException("ExperimentSeriesRun must not be null.");
		}
		
		this.currentExperimentSeriesRun = experimentSeriesRun;
		this.preparationPVs = preparationPVs;
		
		meController.prepareExperimentSeries(preparationPVs);
		
		ParameterCollection<ParameterDefinition> observationParams = 
				ParameterCollectionFactory.createParameterDefinitionCollection(meDefinition.getRoot().getObservationParameters());
		meController.setObservationParameters(observationParams);
	}
	
	
	
	@Override
	public DataSetAggregated runExperiment(ParameterCollection<ParameterValue<?>> inputPVs, ExperimentTerminationCondition terminationCondition) {
		if (terminationCondition == null)
			throw new IllegalArgumentException("TerminationCondition must be set (not null).");
		

		DataSetAggregated experimentRunResult =  runExperimentOnME(meController, inputPVs, terminationCondition);
		
		try {
			currentExperimentSeriesRun = persistenceProvider.loadExperimentSeriesRun(currentExperimentSeriesRun.getPrimaryKey());
			currentExperimentSeriesRun.append(experimentRunResult);
			persistenceProvider.store(currentExperimentSeriesRun);	
		} catch (DataNotFoundException e) {
			throw new IllegalStateException("ExperimentSeriesRun with Id " + currentExperimentSeriesRun + " could not be loaded from database.", e);
		}
		
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
	private DataSetAggregated runExperimentOnME(IMeasurementEnvironmentController meController, ParameterCollection<ParameterValue<?>> inputPVs, ExperimentTerminationCondition terminationCondition) {

		// 1. run the experiment
		Collection<ParameterValueList<?>> observations = meController.runExperiment(inputPVs, terminationCondition);
		
		// 2. aggregate the results
		DataSetRowBuilder builder = new DataSetRowBuilder();
		builder.startRow();
		
		// 2.1. add initialization values
		for (ParameterValue<?> pv: initializationPVs)
			builder.addInputParameterValue(pv.getParameter(), pv.getValue());

		// 2.2. add preparation values
		for (ParameterValue<?> pv: preparationPVs)
			builder.addInputParameterValue(pv.getParameter(), pv.getValue());
		
		// 2.3. add input values
		for (ParameterValue<?> parameterValue : inputPVs) 
			builder.addInputParameterValue(parameterValue.getParameter(), parameterValue.getValue());
		
		// 2.4. add observation values
		for (ParameterValueList<?> pvl: observations)
			builder.addObservationParameterValues(pvl);
		
		builder.finishRow();

		return builder.createDataSet();
	}
}

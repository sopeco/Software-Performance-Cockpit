package org.sopeco.engine.experiment.impl;

import java.rmi.RemoteException;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.engine.experiment.IExperimentController;
import org.sopeco.engine.measurementenvironment.ExperimentFailedException;
import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.engine.util.ParameterCollection;
import org.sopeco.engine.util.ParameterCollectionFactory;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.DataSetRowBuilder;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.ParameterValueFactory;
import org.sopeco.persistence.dataset.ParameterValueList;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.definition.ExperimentTerminationCondition;
import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.exceptions.DataNotFoundException;

/**
 * This class implements the functionality required to execute a single
 * experiment. It is responsible for the communication between the SoPeCo engine
 * and a measurement environment.
 * 
 * @author Dennis Westermann, Roozbeh Farahbod
 * 
 */
public class ExperimentController implements IExperimentController {

	private final static Logger logger = LoggerFactory.getLogger(ExperimentController.class);
	
	MeasurementEnvironmentDefinition meDefinition = null;
	IMeasurementEnvironmentController meController = null;
	IPersistenceProvider persistenceProvider = null;
	ExperimentSeriesRun currentExperimentSeriesRun = null;
	ParameterCollection<ParameterValue<?>> initializationPVs = ParameterCollectionFactory.createParameterValueCollection();
	ParameterCollection<ParameterValue<?>> preparationPVs = ParameterCollectionFactory.createParameterValueCollection();

	private DataSetAggregated failedDataSet = null;
	private DataSetAggregated successfulDataSet = null;
	
	@Override
	public void initialize(ParameterCollection<ParameterValue<?>> initializationPVs, MeasurementEnvironmentDefinition meDefinition) {

		if (meController == null) {
			throw new IllegalStateException("No MeasurementEnvironmentController has been set.");
		}
		if (persistenceProvider == null) {
			throw new IllegalStateException("No PersistenceProvider has been set.");
		}

		try {

			this.meDefinition = meDefinition;

			this.initializationPVs = initializationPVs;

			meController.initialize(initializationPVs);

		} catch (RemoteException e) {
			throw new RuntimeException("RemoteException.", e);
		}
	}

	@Override
	public void prepareExperimentSeries(ExperimentSeriesRun experimentSeriesRun, ParameterCollection<ParameterValue<?>> preparationPVs) {
		if (experimentSeriesRun == null) {
			throw new IllegalArgumentException("ExperimentSeriesRun must not be null.");
		}

		try {

			this.currentExperimentSeriesRun = experimentSeriesRun;
			this.preparationPVs = preparationPVs;

			meController.prepareExperimentSeries(preparationPVs);

			ParameterCollection<ParameterDefinition> observationParams = ParameterCollectionFactory.createParameterDefinitionCollection(meDefinition.getRoot()
					.getObservationParameters());
			meController.setObservationParameters(observationParams);

		} catch (RemoteException e) {
			throw new RuntimeException("RemoteException.", e);
		}
	}

	@Override
	public void runExperiment(ParameterCollection<ParameterValue<?>> inputPVs, ExperimentTerminationCondition terminationCondition) {
		if (terminationCondition == null)
			throw new IllegalArgumentException("TerminationCondition must be set (not null).");

		boolean experimentWasSuccessful = runExperimentOnME(meController, inputPVs, terminationCondition);

		try {
			currentExperimentSeriesRun = persistenceProvider.loadExperimentSeriesRun(currentExperimentSeriesRun.getPrimaryKey());
			if (experimentWasSuccessful) {
				currentExperimentSeriesRun.appendSuccessfulResults(successfulDataSet);
			} else {
				currentExperimentSeriesRun.appendFailedResults(failedDataSet);
			}
			persistenceProvider.store(currentExperimentSeriesRun);
		} catch (DataNotFoundException e) {
			throw new IllegalStateException("ExperimentSeriesRun with Id " + currentExperimentSeriesRun + " could not be loaded from database.", e);
		}

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
		try {
			
			meController.finalizeExperimentSeries();
			
		} catch (RemoteException e) {
			throw new RuntimeException("RemoteException.", e);
		}
	}

	/**
	 * Runs the experiment defined by the input parameter-value list and the
	 * termination condition, on the given measurement environment controller,
	 * and aggregates the results into an instance of {@link DataSetAggregated}.
	 */
	private boolean runExperimentOnME(IMeasurementEnvironmentController meController, ParameterCollection<ParameterValue<?>> inputPVs,
			ExperimentTerminationCondition terminationCondition) {

		try {

			boolean experimentSuccessful = false;
			
			ParameterCollection<ParameterValue<?>> paramCollection = ParameterCollectionFactory.createParameterValueCollection(inputPVs);
			
			// 0. prepare to pass all parameter values to me controller
			paramCollection.addAll(initializationPVs);
			paramCollection.addAll(preparationPVs);
			
			// 1. run the experiment
			Collection<ParameterValueList<?>> observations = null;
			try {
				observations = meController.runExperiment(paramCollection, terminationCondition);
				experimentSuccessful = true;
			} catch (ExperimentFailedException e) {
				logger.warn("An experiment failed.");
			}

			// 2. aggregate the results
			DataSetRowBuilder builder = new DataSetRowBuilder();
			builder.startRow();

//			// 2.1. add initialization values
//			for (ParameterValue<?> pv : initializationPVs)
//				builder.addInputParameterValue(pv.getParameter(), pv.getValue());
//
//			// 2.2. add preparation values
//			for (ParameterValue<?> pv : preparationPVs)
//				builder.addInputParameterValue(pv.getParameter(), pv.getValue());

			// 2.3. add input values
			for (ParameterValue<?> parameterValue : paramCollection)
				builder.addInputParameterValue(parameterValue.getParameter(), parameterValue.getValue());

			// 2.4. add observation values, if the experiment was successful
			if (experimentSuccessful) {
				for (ParameterValueList<?> pvl : observations)
					builder.addObservationParameterValues(pvl);
			} 

			builder.finishRow();

			if (experimentSuccessful) {
				successfulDataSet = builder.createDataSet();
				failedDataSet = null;
			} else {
				successfulDataSet = null;
				failedDataSet = builder.createDataSet();
			}

			return experimentSuccessful;

		} catch (RemoteException e) {
			throw new RuntimeException("RemoteException.", e);
		}

	}

	@Override
	public DataSetAggregated getLastSuccessfulExperimentResults() {
		return  successfulDataSet;
	}

	@Override
	public DataSetAggregated getLastFailedExperimentResults() {
		return failedDataSet;
	}
}

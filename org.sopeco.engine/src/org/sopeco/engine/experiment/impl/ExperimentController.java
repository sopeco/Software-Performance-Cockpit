/**
 * Copyright (c) 2013 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.sopeco.engine.experiment.impl;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.config.Configuration;
import org.sopeco.config.IConfiguration;
import org.sopeco.engine.exception.ExperimentAbortException;
import org.sopeco.engine.experiment.IExperimentController;
import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.engine.status.EventType;
import org.sopeco.engine.status.StatusBroker;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.PersistenceProviderFactory;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.DataSetRowBuilder;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.ParameterValueList;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.definition.ExperimentTerminationCondition;
import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.entities.exceptions.ExperimentFailedException;
import org.sopeco.persistence.exceptions.DataNotFoundException;
import org.sopeco.persistence.util.ParameterCollection;
import org.sopeco.persistence.util.ParameterCollectionFactory;
import org.sopeco.util.session.SessionAwareObject;

/**
 * This class implements the functionality required to execute a single
 * experiment. It is responsible for the communication between the SoPeCo engine
 * and a measurement environment.
 * 
 * @author Dennis Westermann, Roozbeh Farahbod
 * 
 */
public class ExperimentController extends SessionAwareObject implements IExperimentController {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3792616812815582063L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentController.class);

	private static final String USE_CACHE_PROPERTY = "sopeco.config.engine.useExperimentResultCache";

	/**
	 * Constructor.<br />
	 * Opens a connection to the persistence provider with the configuration data fetched via the
	 * given session ID.
	 * 
	 * @param sessionId
	 *            Session id to be used for this experiment controller.
	 */
	public ExperimentController(String sessionId) {
		super(sessionId);
		persistenceProvider = PersistenceProviderFactory.getInstance().getPersistenceProvider(sessionId);
	}

	private MeasurementEnvironmentDefinition meDefinition = null;
	private IMeasurementEnvironmentController meController = null;
	private IPersistenceProvider persistenceProvider = null;
	private ExperimentSeriesRun currentExperimentSeriesRun = null;
	private ParameterCollection<ParameterValue<?>> initializationPVs = ParameterCollectionFactory
			.createParameterValueCollection();
	private ParameterCollection<ParameterValue<?>> preparationPVs = ParameterCollectionFactory
			.createParameterValueCollection();

	private DataSetAggregated successfulDataSet = null;
	private ExperimentFailedException experimentFailedException = null;

	@Override
	public void initialize(ParameterCollection<ParameterValue<?>> initializationPVs,
			MeasurementEnvironmentDefinition meDefinition) {
		checkRunToBeAborted();
		if (meController == null) {
			throw new IllegalStateException("No MeasurementEnvironmentController has been set.");
		}
		if (persistenceProvider == null) {
			throw new IllegalStateException("No PersistenceProvider has been set.");
		}

		try {

			StatusBroker.getManagerViaSessionID(getSessionId()).newStatus(EventType.INIT_MEC);

			this.meDefinition = meDefinition;

			this.initializationPVs = initializationPVs;

			meController.initialize(getSessionId(), initializationPVs);

		} catch (RemoteException e) {
			throw new RuntimeException("RemoteException.", e);
		}
	}

	@Override
	public void prepareExperimentSeries(ExperimentSeriesRun experimentSeriesRun,
			ParameterCollection<ParameterValue<?>> preparationPVs) {
		checkRunToBeAborted();
		if (experimentSeriesRun == null) {
			throw new IllegalArgumentException("ExperimentSeriesRun must not be null.");
		}

		if (experimentSeriesRun.getExperimentSeries().getName()
				.equals("ElementPropertyCostTest_uipage.table.NUMBER_OF_TEXT_VIEW_COLUMNS_1")) {
			boolean x = true;
		}

		try {
			String seriesName = experimentSeriesRun.getExperimentSeries().getName();
			StatusBroker.getManagerViaSessionID(getSessionId()).newStatus(EventType.PREPARE_EXPERIMENTSERIES, seriesName);

			this.currentExperimentSeriesRun = experimentSeriesRun;
			this.preparationPVs = preparationPVs;

			Set<ExperimentTerminationCondition> terminationConditions = currentExperimentSeriesRun
					.getExperimentSeries().getExperimentSeriesDefinition().getTerminationConditions();
			meController.prepareExperimentSeries(getSessionId(), preparationPVs, terminationConditions);

			// ParameterCollection<ParameterDefinition> observationParams =
			// ParameterCollectionFactory
			// .createParameterDefinitionCollection(meDefinition.getRoot().getObservationParameters());
			// meController.setObservationParameters(observationParams);

		} catch (RemoteException e) {
			throw new RuntimeException("RemoteException.", e);
		}
	}

	@Override
	public void runExperiment(ParameterCollection<ParameterValue<?>> inputPVs) {
		boolean experimentWasSuccessful = false;

		try {
			checkRunToBeAborted();
			StatusBroker.getManagerViaSessionID(getSessionId()).newStatus(EventType.EXECUTE_EXPERIMENTRUN);

			// update experiment series run instance with latest database
			// content
			currentExperimentSeriesRun = persistenceProvider.loadExperimentSeriesRun(currentExperimentSeriesRun
					.getPrimaryKey());

			experimentWasSuccessful = runExperimentOnME(meController, inputPVs);

			if (experimentWasSuccessful) {
				currentExperimentSeriesRun.appendSuccessfulResults(successfulDataSet);
			} else {
				currentExperimentSeriesRun.addExperimentFailedException(experimentFailedException);
			}
			persistenceProvider.store(currentExperimentSeriesRun);
		} catch (DataNotFoundException e) {
			throw new IllegalStateException("ExperimentSeriesRun with Id " + currentExperimentSeriesRun
					+ " could not be loaded from database.", e);
		}

	}

	/**
	 * Sets the measurement environment controller.
	 * {@link IMeasurementEnvironmentController}
	 * 
	 * @param meController
	 *            measurement environment controller to be used
	 */
	public void setMeasurementEnvironmentController(IMeasurementEnvironmentController meController) {
		this.meController = meController;
	}

	/**
	 * Sets the measurement environment definition.
	 * {@link MeasurementEnvironmentDefinition}
	 * 
	 * @param meDefinition
	 *            measurement environment definition describes all known
	 *            parameter of the measurement environment controller
	 */
	public void setMeasurementEnvironmentDefintion(MeasurementEnvironmentDefinition meDefinition) {
		this.meDefinition = meDefinition;
	}

	@Override
	public void finalizeExperimentSeries() {
		try {
			checkRunToBeAborted();
			StatusBroker.getManagerViaSessionID(getSessionId()).newStatus(EventType.FINALIZE_EXPERIMENTSERIES);

			meController.finalizeExperimentSeries(getSessionId());

		} catch (RemoteException e) {
			throw new RuntimeException("RemoteException.", e);
		}
	}

	/**
	 * Runs the experiment defined by the input parameter-value list on the
	 * given measurement environment controller, and aggregates the results into
	 * an instance of {@link DataSetAggregated}.
	 */
	private boolean runExperimentOnME(IMeasurementEnvironmentController meController,
			ParameterCollection<ParameterValue<?>> inputPVs) {

		try {

			boolean experimentSuccessful = false;
			ExperimentFailedException error = null;

			ParameterCollection<ParameterValue<?>> inputParamCollection = ParameterCollectionFactory
					.createParameterValueCollection(inputPVs);

			// 0. prepare to pass all parameter values to me controller
			inputParamCollection.addAll(initializationPVs);
			inputParamCollection.addAll(preparationPVs);

			// 1. run the experiment
			Collection<ParameterValueList<?>> observations = null;

			// 1.1 check whether experiment result cache should be used and if
			// yes, whether the experiment is in the cache
			boolean runExperimentOnME = true;

			IConfiguration config = Configuration.getSessionSingleton(getSessionId());
			boolean useCache = Boolean.parseBoolean(config.getPropertyAsStr(USE_CACHE_PROPERTY));
			if (useCache) {
				LOGGER.debug("Trying to use cached results.");

				DataSetAggregated allMeasurementsOfExperimentSeries = currentExperimentSeriesRun.getExperimentSeries()
						.getAllExperimentSeriesRunSuccessfulResultsInOneDataSet();
				if (allMeasurementsOfExperimentSeries.containsRowWithInputValues(inputParamCollection)) {
					try {
						observations = allMeasurementsOfExperimentSeries
								.getObservationParameterValues(inputParamCollection);
						experimentSuccessful = true;
						runExperimentOnME = false;
						LOGGER.debug("Successfully read experiment results from cache.");
					} catch (DataNotFoundException e) {
						throw new IllegalStateException(e);
					}
				} else {
					LOGGER.debug("Experiment is not in the cache");
					runExperimentOnME = true; // experiment is not in cache
				}
			} else {
				runExperimentOnME = true;
			}

			// 1.2 run the experiment on the measurement environment
			if (runExperimentOnME) {
				try {
					LOGGER.debug("Starting experiment on measurement environment...");
					observations = meController.runExperiment(getSessionId(), inputParamCollection);
					experimentSuccessful = true;
					LOGGER.debug("Experiment finished successfully on measurement environment.");
				} catch (ExperimentFailedException e) {
					error = e;
					LOGGER.warn("The experiment failed. Reason: {}", error.getMessage());
				}
			}

			// 2. aggregate the results
			DataSetRowBuilder builder = new DataSetRowBuilder();
			builder.startRow();

			// // 2.1. add initialization values
			// for (ParameterValue<?> pv : initializationPVs)
			// builder.addInputParameterValue(pv.getParameter(), pv.getValue());
			//
			// // 2.2. add preparation values
			// for (ParameterValue<?> pv : preparationPVs)
			// builder.addInputParameterValue(pv.getParameter(), pv.getValue());

			// 2.3. add input values
			for (ParameterValue<?> parameterValue : inputParamCollection) {
				builder.addInputParameterValue(parameterValue.getParameter(), parameterValue.getValue());
			}

			// 2.4. add observation values, if the experiment was successful
			if (experimentSuccessful) {
				for (ParameterValueList<?> pvl : observations) {
					builder.addObservationParameterValues(pvl);
				}
			}

			builder.finishRow();

			if (experimentSuccessful) {
				successfulDataSet = builder.createDataSet();
				experimentFailedException = null;
			} else {
				successfulDataSet = null;
				experimentFailedException = error;
				experimentFailedException.setInputParameterValues(inputParamCollection);
			}

			return experimentSuccessful;

		} catch (RemoteException e) {
			StatusBroker.getManagerViaSessionID(getSessionId()).newStatus(EventType.EXECUTION_FAILED, e);

			throw new RuntimeException("RemoteException.", e);
		} catch (Exception e) {
			StatusBroker.getManagerViaSessionID(getSessionId()).newStatus(EventType.EXECUTION_FAILED, e);

			LOGGER.error("Exception was thrown. Reason: {}", e.getMessage());
			throw new RuntimeException(e);
		}
	}

	@Override
	public DataSetAggregated getLastSuccessfulExperimentResults() {
		return successfulDataSet;
	}

	@Override
	public ExperimentFailedException getLastFailedExperimentException() {
		return experimentFailedException;
	}

	@Override
	public void acquireMEController() {
		long timeout = Configuration.getSessionSingleton(getSessionId()).getPropertyAsLong(
				IConfiguration.CONF_MEC_ACQUISITION_TIMEOUT, 0);

		StatusBroker.getManagerViaSessionID(getSessionId()).newStatus(EventType.ACQUIRE_MEC);

		try {
			boolean acquired = meController.acquireMEController(getSessionId(), timeout);
			if (!acquired) {

				StatusBroker.getManagerViaSessionID(getSessionId()).newStatus(EventType.ACQUIRE_MEC_FAILED);

				throw new RuntimeException(
						"MEController in use. Were not able to acquire the controller within a time frame of "
								+ timeout + " seconds");
			}
		} catch (RemoteException e) {

			StatusBroker.getManagerViaSessionID(getSessionId()).newStatus(EventType.ACQUIRE_MEC_FAILED);

			throw new RuntimeException("RemoteException.", e);
		}
	}

	@Override
	public void releaseMEController() {
		try {

			StatusBroker.getManagerViaSessionID(getSessionId()).newStatus(EventType.RELEASE_MEC);

			meController.releaseMEController(getSessionId());

			StatusBroker.getManagerViaSessionID(getSessionId()).newStatus(EventType.MEASUREMENT_FINISHED);
		} catch (RemoteException e) {
			throw new RuntimeException("RemoteException.", e);
		}
	}

	private void checkRunToBeAborted() {
		boolean abort = Configuration.getSessionSingleton(getSessionId()).getPropertyAsBoolean(
				IConfiguration.EXPERIMENT_RUN_ABORT, false);
		if (abort) {
			Configuration.getSessionSingleton(getSessionId()).setProperty(IConfiguration.EXPERIMENT_RUN_ABORT, false);
			throw new ExperimentAbortException("Experiment has been aborted by the user!");
		}
	}

}

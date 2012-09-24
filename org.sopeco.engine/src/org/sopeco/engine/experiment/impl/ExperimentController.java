package org.sopeco.engine.experiment.impl;

import java.rmi.RemoteException;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.config.Configuration;
import org.sopeco.config.IConfiguration;
import org.sopeco.engine.experiment.IExperimentController;
import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.PersistenceProviderFactory;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.DataSetRowBuilder;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.ParameterValueList;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.definition.ExperimentTerminationCondition;
import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
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
	private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentController.class);

	private static final String USE_CACHE_PROPERTY = "sopeco.config.engine.useExperimentResultCache";

	/**
	 * Constructor.
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
	public void prepareExperimentSeries(ExperimentSeriesRun experimentSeriesRun,
			ParameterCollection<ParameterValue<?>> preparationPVs) {
		if (experimentSeriesRun == null) {
			throw new IllegalArgumentException("ExperimentSeriesRun must not be null.");
		}

		if (experimentSeriesRun.getExperimentSeries().getName()
				.equals("ElementPropertyCostTest_uipage.table.NUMBER_OF_TEXT_VIEW_COLUMNS_1")) {
			boolean x = true;
		}

		try {

			this.currentExperimentSeriesRun = experimentSeriesRun;
			this.preparationPVs = preparationPVs;

			meController.prepareExperimentSeries(preparationPVs);

			ParameterCollection<ParameterDefinition> observationParams = ParameterCollectionFactory
					.createParameterDefinitionCollection(meDefinition.getRoot().getObservationParameters());
			meController.setObservationParameters(observationParams);

		} catch (RemoteException e) {
			throw new RuntimeException("RemoteException.", e);
		}
	}

	@Override
	public void runExperiment(ParameterCollection<ParameterValue<?>> inputPVs,
			ExperimentTerminationCondition terminationCondition) {
		if (terminationCondition == null) {
			throw new IllegalArgumentException("TerminationCondition must be set (not null).");
		}

		boolean experimentWasSuccessful = false;

		try {
			// update experiment series run instance with latest database
			// content
			currentExperimentSeriesRun = persistenceProvider.loadExperimentSeriesRun(currentExperimentSeriesRun
					.getPrimaryKey());

			experimentWasSuccessful = runExperimentOnME(meController, inputPVs, terminationCondition);

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
	private boolean runExperimentOnME(IMeasurementEnvironmentController meController,
			ParameterCollection<ParameterValue<?>> inputPVs, ExperimentTerminationCondition terminationCondition) {

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
					observations = meController.runExperiment(inputParamCollection, terminationCondition);
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
			for (ParameterValue<?> parameterValue : inputParamCollection)
				builder.addInputParameterValue(parameterValue.getParameter(), parameterValue.getValue());

			// 2.4. add observation values, if the experiment was successful
			if (experimentSuccessful) {
				for (ParameterValueList<?> pvl : observations)
					builder.addObservationParameterValues(pvl);
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
			throw new RuntimeException("RemoteException.", e);
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
}

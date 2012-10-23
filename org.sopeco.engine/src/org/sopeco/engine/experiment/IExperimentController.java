package org.sopeco.engine.experiment;

import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.entities.exceptions.ExperimentFailedException;
import org.sopeco.persistence.util.ParameterCollection;
import org.sopeco.util.session.ISessionAwareObject;

/**
 * An ExperimentController controls execution of any single experiment.
 * 
 * @author D053711
 * 
 */
public interface IExperimentController extends ISessionAwareObject {

	/**
	 * Acquires the used MEController for measurement execution.
	 */
	void acquireMEController();

	/**
	 * Releases used MEController.
	 */
	void releaseMEController();

	/**
	 * Initializes the experiment controller before the one set of experiments.
	 * 
	 * @param initializationPVs
	 *            initialization arguments
	 * @param meDefinition
	 *            mesearuement environment definition
	 */
	void initialize(ParameterCollection<ParameterValue<?>> initializationPVs,
			MeasurementEnvironmentDefinition meDefinition);

	/**
	 * Prepares the experiment controller for one single experiment series. This
	 * is called before subsequent calls to
	 * {@link #runExperiment(ParameterCollection)}
	 * .
	 * 
	 * @param experimentSeriesRun
	 *            an instance of experiment series run
	 * @param preparationPVs
	 *            preparation arguments
	 */
	public void prepareExperimentSeries(ExperimentSeriesRun experimentSeriesRun, 
			ParameterCollection<ParameterValue<?>> preparationPVs);

	/**
	 * Runs a single experiment with the given arguments. It also appends the
	 * data to the already-persisted data using the persistence provider.
	 * 
	 * @param inputPVs
	 *            a collection of parameter values
	 */
	void runExperiment(ParameterCollection<ParameterValue<?>> inputPVs);

	/**
	 * Finalizes the experiment; a proxy method to the measurement environment
	 * controller.
	 * 
	 * 
	 */
	void finalizeExperimentSeries();

	/**
	 * Returns the single data row that is generated as the result of the last
	 * successful experiment.
	 * 
	 * @return successful result set, if the last experiment was successful, or
	 *         <code>null</code> otherwise.
	 */
	DataSetAggregated getLastSuccessfulExperimentResults();

	/**
	 * Returns the experiment failed exception that is generated as the result
	 * of the last failed experiment.
	 * 
	 * @return an experiment failed exception, if the last experiment failed, or
	 *         <code>null</code> otherwise.
	 */
	ExperimentFailedException getLastFailedExperimentException();
}

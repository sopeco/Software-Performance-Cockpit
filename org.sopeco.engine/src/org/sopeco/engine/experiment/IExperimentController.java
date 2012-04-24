package org.sopeco.engine.experiment;

import java.util.Collection;
import java.util.List;

import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.model.configuration.measurements.ExperimentTerminationCondition;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.entities.ExperimentSeriesRun;


/**
 * An ExperimentController controls execution of any single experiment.
 * 
 * @author D053711
 *
 */
public interface IExperimentController {

	/**
	 * Initializes the experiment controller before the one set of experiments. 
	 *  
	 * @param initializationPVList initialization arguments
	 */
	public void initialize(List<ParameterValue<?>> initializationPVList);
	
	/**
	 * Prepares the experiment controller for one single experiment series. This is called
	 * before subsequent calls to {@link #runExperiment(List, ExperimentTerminationCondition)}. 
	 * 
	 * @param experimentSeriesRun an instance of experiment series run 
	 * @param preparationPVList preparation arguments
	 */
	public void prepareExperimentSeries(ExperimentSeriesRun experimentSeriesRun, Collection<ParameterValue<?>> preparationPVList);
	
	/**
	 * Runs a single experiment with the given arguments.
	 * It also appends the data to the already-persisted data using the persistence provider.
	 * 
	 * @return the single data row that is generated as the result of the experiment
	 */
	public DataSetAggregated runExperiment(List<ParameterValue<?>> inputPVList, ExperimentTerminationCondition terminationCondition);

	/**
	 * Finalizes the experiment; a proxy method to the measurement environment controller.
	 * 
	 * @see IMeasurementEnvironmentController
	 */
	public void finalizeExperimentSeries();
}

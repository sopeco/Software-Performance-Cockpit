package org.sopeco.engine.experiment;

import java.util.List;

import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.engine.util.ParameterCollection;
import org.sopeco.model.configuration.environment.MeasurementEnvironmentDefinition;
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
	 * @param initializationPVs initialization arguments
	 * @param meDefinition
	 */
	void initialize(ParameterCollection<ParameterValue<?>> initializationPVs,
			MeasurementEnvironmentDefinition meDefinition);
	
	/**
	 * Prepares the experiment controller for one single experiment series. This is called
	 * before subsequent calls to {@link #runExperiment(List, ExperimentTerminationCondition)}. 
	 * 
	 * @param experimentSeriesRun an instance of experiment series run 
	 * @param preparationPVs preparation arguments
	 */
	public void prepareExperimentSeries(ExperimentSeriesRun experimentSeriesRun, ParameterCollection<ParameterValue<?>> preparationPVs);
	
	/**
	 * Runs a single experiment with the given arguments.
	 * It also appends the data to the already-persisted data using the persistence provider.
	 * 
	 * @return the single data row that is generated as the result of the experiment
	 */
	public DataSetAggregated runExperiment(ParameterCollection<ParameterValue<?>> inputPVs, ExperimentTerminationCondition terminationCondition);

	/**
	 * Finalizes the experiment; a proxy method to the measurement environment controller.
	 * 
	 * @see IMeasurementEnvironmentController
	 */
	public void finalizeExperimentSeries();

	
}

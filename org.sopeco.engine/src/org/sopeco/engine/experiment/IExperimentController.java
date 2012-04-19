package org.sopeco.engine.experiment;

import java.util.List;

import org.sopeco.model.configuration.measurements.ExperimentTerminationCondition;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.ParameterValue;


/**
 * An ExperimentController controls execution of any single experiment.
 * 
 * @author D053711
 *
 */
public interface IExperimentController {
	
	public void initialize(List<ParameterValue<?>> initializationPVList);
	
	public void prepareExperimentSeries(List<ParameterValue<?>> preparationPVList);
	
	/**
	 * To be executed you have to set the entry point of the measurement env. first by calling initialize(...) 
	 */
	public DataSetAggregated runExperiment(List<ParameterValue<?>> inputPVList, ExperimentTerminationCondition terminationCondition);

	
	public void finalizeExperimentSeries();
}

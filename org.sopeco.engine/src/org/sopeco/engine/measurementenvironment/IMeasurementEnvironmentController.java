package org.sopeco.engine.measurementenvironment;

import java.util.Collection;
import java.util.List;

import org.sopeco.model.configuration.environment.ParameterDefinition;
import org.sopeco.model.configuration.measurements.ExperimentTerminationCondition;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.ParameterValue;

/**
 * Interface that has to be implemented by the provider of the measurement environment. 
 * It is the connection between the SoPeCo engine and the target system.
 * 
 * @author Dennis Westermann
 *
 */
public interface IMeasurementEnvironmentController {
	
	public void initialize(List<ParameterValue<?>> initializationPVList);

	public void prepareExperimentSeries(Collection<ParameterValue<?>> preparationPVList);
	
	/**
	 * @return DataSet containing only output parameter values.
	 */
	public DataSetAggregated runExperiment(List<ParameterValue<?>> inputPVList, ExperimentTerminationCondition  terminationCondition);
	
	/**
	 * 
	 */
	public void finalizeExperimentSeries();

	void setObservationParameters(List<ParameterDefinition> observationParameters);
	
}

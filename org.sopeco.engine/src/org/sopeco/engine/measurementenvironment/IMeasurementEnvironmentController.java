package org.sopeco.engine.measurementenvironment;

import java.util.Collection;
import java.util.List;

import org.sopeco.model.configuration.environment.MeasurementEnvironmentDefinition;
import org.sopeco.model.configuration.measurements.ExperimentTerminationCondition;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.ParameterValue;

public interface IMeasurementEnvironmentController {
	
	public void initialize(List<ParameterValue<?>> initializationPVList);

	public void prepareExperimentSeries(Collection<ParameterValue<?>> preparationPVList);
	
	/**
	 * @return DataSet containing only output parameter values.
	 */
	public DataSetAggregated runExperiment(List<ParameterValue<?>> inputPVList, ExperimentTerminationCondition  terminationCondition);
	
	public void setMeasurementEnvironment(MeasurementEnvironmentDefinition meDefinition);
	
}

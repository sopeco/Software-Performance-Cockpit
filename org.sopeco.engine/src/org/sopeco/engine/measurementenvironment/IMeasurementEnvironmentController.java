package org.sopeco.engine.measurementenvironment;

import java.util.List;

import org.sopeco.engine.experimentseries.ParameterValue;
import org.sopeco.model.configuration.environment.ParameterDefinition;
import org.sopeco.model.configuration.measurements.ExperimentTerminationCondition;
import org.sopeco.persistence.dataset.DataSetAggregated;

@SuppressWarnings("rawtypes")
public interface IMeasurementEnvironmentController {
	
	public void initialize(List<ParameterValue> initializationAssignments);

	public void prepareExperimentSeries(List<ParameterValue> preparationAssignments);
	
	public DataSetAggregated runExperiment(List<ParameterValue> inputPVList, ExperimentTerminationCondition  terminationCondition);
	
	/**
	 * Injects all parameters that are to be known to the MEController, i.e.,
	 * the ones that have to be included in the DataSet result.
	 * 
	 * @param parameters
	 *            Parameter definitions used by MEController.
	 */
	public void setParameters(List<ParameterDefinition> parameterList);

	
}

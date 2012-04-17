package org.sopeco.core.measurementenvironment;

import java.util.List;

import org.sopeco.core.experimentseries.ParameterValue;
import org.sopeco.core.model.configuration.environment.ParameterDefinition;
import org.sopeco.core.model.configuration.measurements.ExperimentTerminationCondition;

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

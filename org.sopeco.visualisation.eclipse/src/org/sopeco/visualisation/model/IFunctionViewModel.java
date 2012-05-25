package org.sopeco.visualisation.model;

import java.util.List;
import java.util.Map;

import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

public interface IFunctionViewModel extends IViewModel {
	public FunctionViewConfiguration getConfigurationAlternatives(ExperimentSeriesRun experimentSeriesRun, ErrorStatus errorStatus);

	public void addDataItem(ExperimentSeriesRun experimentSeriesRun, ParameterDefinition xPar, ParameterDefinition yPar, ErrorStatus errorStatus);

	public void addDataItem(ExperimentSeriesRun experimentSeriesRun, ParameterDefinition xPar, ParameterDefinition yPar,
			Map<ParameterDefinition, Object> valueAssignments, ErrorStatus errorStatus);

	public void addDataItem(ExperimentSeriesRun experimentSeriesRun, ParameterDefinition yPar, ErrorStatus errorStatus);

	public List<FunctionData> getFunctionsToVisualize();

}

package org.sopeco.visualisation.model;

import java.util.Map;

import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

public interface IViewModel {
	public void addDataItem(ExperimentSeriesRun experimentSeriesRun, ParameterDefinition xPar, ParameterDefinition yPar, ErrorStatus errorStatus);

	public void addDataItem(ExperimentSeriesRun experimentSeriesRun, ParameterDefinition xPar, ParameterDefinition yPar,
			Map<ParameterDefinition, Object> valueAssignments, ErrorStatus errorStatus);

	public void addDataItem(ExperimentSeriesRun experimentSeriesRun, ParameterDefinition yPar, ErrorStatus errorStatus);

	public void addDataItem(ExperimentSeriesRun experimentSeriesRun, ParameterDefinition yPar,
			Map<ParameterDefinition, Object> valueAssignments, ErrorStatus errorStatus);
}

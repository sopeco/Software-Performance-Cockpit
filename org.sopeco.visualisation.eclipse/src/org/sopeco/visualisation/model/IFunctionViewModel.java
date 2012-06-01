package org.sopeco.visualisation.model;

import java.util.List;
import java.util.Map;

import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

public interface IFunctionViewModel extends IViewModel {
	public ViewConfiguration getConfigurationAlternatives(ExperimentSeriesRun experimentSeriesRun, ErrorStatus errorStatus);

	

	
	public List<FunctionData> getFunctionsToVisualize();

}

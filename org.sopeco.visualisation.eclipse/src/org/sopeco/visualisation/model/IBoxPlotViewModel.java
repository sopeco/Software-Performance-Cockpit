package org.sopeco.visualisation.model;

import java.util.List;

import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

public interface IBoxPlotViewModel extends IViewModel {
	public ViewConfiguration getConfigurationAlternatives(ExperimentSeriesRun experimentSeriesRun, ErrorStatus errorStatus);

	

	public List<BoxPlotData> getBoxesToVisualize();
	

}

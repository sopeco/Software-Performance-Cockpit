package org.sopeco.visualisation.model;

import java.util.List;

import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

public interface IBoxPlotViewModel extends IViewModel {
	public BoxPlotViewConfiguration getConfigurationAlternatives(ExperimentSeriesRun experimentSeriesRun, ErrorStatus errorStatus);

	public void addDataItem(ExperimentSeriesRun experimentSeriesRun, ParameterDefinition yPar, ErrorStatus errorStatus);
	
	public void addDataItem(ExperimentSeriesRun experimentSeriesRun, ParameterDefinition xPar, ParameterDefinition yPar, ErrorStatus errorStatus);

	public List<BoxPlotData> getBoxesToVisualize();
	

}

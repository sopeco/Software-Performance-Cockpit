package org.sopeco.visualisation.model;

import java.util.Map;

import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

public interface IViewModel {
	public void addDataItem(ViewItemConfiguration configuration, ErrorStatus errorStatus);
}

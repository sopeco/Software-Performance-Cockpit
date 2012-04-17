package org.sopeco.core.persistence;

import java.util.List;

import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.sopeco.core.model.configuration.measurements.ExperimentSeriesDefinition;

public class ExperimentSeries {
	
	private String experimentSeriesDefinitionId;

	@ManyToOne
	private ScenarioInstance scenarioInstance;
	
	@Lob
	private ExperimentSeriesDefinition experimentSeriesDefinition;
	
	
	List<ExperimentSeriesRun> getExperimentRuns(){
		return null;
	}

}

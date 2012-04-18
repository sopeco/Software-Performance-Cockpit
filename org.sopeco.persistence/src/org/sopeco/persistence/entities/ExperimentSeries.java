package org.sopeco.persistence.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition;

public class ExperimentSeries {
	
	private String name;

	@ManyToOne
	private ScenarioInstance scenarioInstance;
	
	@Lob
	private ExperimentSeriesDefinition experimentSeriesDefinition;
	
	@OneToMany(mappedBy = "ExperimentSeries")
	private List<ExperimentSeriesRun> experimentSeriesRuns = new ArrayList<ExperimentSeriesRun>();
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public ScenarioInstance getScenarioInstance() {
		return scenarioInstance;
	}


	public void setScenarioInstance(ScenarioInstance scenarioInstance) {
		this.scenarioInstance = scenarioInstance;
	}


	public ExperimentSeriesDefinition getExperimentSeriesDefinition() {
		return experimentSeriesDefinition;
	}


	public void setExperimentSeriesDefinition(
			ExperimentSeriesDefinition experimentSeriesDefinition) {
		this.experimentSeriesDefinition = experimentSeriesDefinition;
	}
	
	public List<ExperimentSeriesRun> getExperimentSeriesRuns(){
		return this.experimentSeriesRuns;
	}

}

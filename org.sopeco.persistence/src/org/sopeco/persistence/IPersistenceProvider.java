package org.sopeco.persistence;

import java.util.List;

import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.ScenarioInstance;

/**
 * TODO: Extend as needed. Implement a method only if needed. 
 * @author D053711
 *
 */
public interface IPersistenceProvider {
	
	
	void store(ExperimentSeriesRun experimentSeriesRun);
	
	void store(ExperimentSeries experimentSeries);

	void store(ScenarioInstance scenarioInstance);

	ExperimentSeries loadExperimentSeries(String experimentSeriesId);
	
	List<ExperimentSeriesRun> loadExperimentSeriesRuns(String experimentSeriesId);
	
	List<ScenarioInstance> loadScenarioInstances(String scenarioName);
	
	ScenarioInstance loadScenarioInstance(String scenarioName, String measurementEnvironmentUrl);
	

}

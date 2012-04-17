package org.sopeco.core.persistence;

import java.util.List;

/**
 * TODO: Extend as needed. Implement a method only if needed. 
 * @author D053711
 *
 */
public interface IPersistenceProvider {
	
	
	void store(ExperimentSeriesRun experimentSeriesRun);
	
	void store(ExperimentSeries experimentSeries);

	void store(ScenarioInstance scenarioInstance);

	ExperimentSeries load(String experimentSeriesId);
	
	List<ExperimentSeriesRun> load(String experimentSeriesId);
	
	List<ScenarioInstance> load(String scenarioName);
	
	ScenarioInstance load(String scenarioName, String measurementEnvironmentUrl);
	

}

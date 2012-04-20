package org.sopeco.persistence;

import java.util.List;

import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.ScenarioInstance;
import org.sopeco.persistence.exceptions.DataNotFoundException;

/**
 * TODO: Extend as needed. Implement a method only if needed. 
 * @author D053711
 *
 */
public interface IPersistenceProvider {
	
	
	void store(ExperimentSeriesRun experimentSeriesRun);
	
	void store(ExperimentSeries experimentSeries);

	void store(ScenarioInstance scenarioInstance);
	
	List<ExperimentSeriesRun> loadExperimentSeriesRuns(String experimentSeriesId) throws DataNotFoundException;
	
	List<ScenarioInstance> loadScenarioInstances(String scenarioName) throws DataNotFoundException;
	
	ScenarioInstance loadScenarioInstance(String scenarioName, String measurementEnvironmentUrl) throws DataNotFoundException;

	ExperimentSeries loadExperimentSeries(String experimentSeriesName,
			String scenarioInstanceName, String measurementEnvironmentUrl)
			throws DataNotFoundException;

	
	

}

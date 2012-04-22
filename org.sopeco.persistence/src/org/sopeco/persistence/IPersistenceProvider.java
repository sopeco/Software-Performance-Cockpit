package org.sopeco.persistence;

import java.util.List;

import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.ScenarioInstance;
import org.sopeco.persistence.entities.keys.ExperimentSeriesPK;
import org.sopeco.persistence.entities.keys.ScenarioInstancePK;
import org.sopeco.persistence.exceptions.DataNotFoundException;

/**
 * TODO: Extend as needed. Implement a method only if needed. 
 * @author Dennis Westermann
 *
 */
public interface IPersistenceProvider {
	
	/* create, update */
	void store(ExperimentSeriesRun experimentSeriesRun);
	
	void store(ExperimentSeries experimentSeries);

	void store(ScenarioInstance scenarioInstance);
		
	/* read */
	List<ScenarioInstance> loadScenarioInstances(String scenarioName) throws DataNotFoundException;
	
	ScenarioInstance loadScenarioInstance(String scenarioName, String measurementEnvironmentUrl) throws DataNotFoundException;

	ScenarioInstance loadScenarioInstance(ScenarioInstancePK primaryKey)
			throws DataNotFoundException;
	
	ExperimentSeries loadExperimentSeries(String experimentSeriesName,
			String scenarioInstanceName, String measurementEnvironmentUrl)
			throws DataNotFoundException;
	
	ExperimentSeries loadExperimentSeries(ExperimentSeriesPK primaryKey)
			throws DataNotFoundException;

	ExperimentSeriesRun loadExperimentSeriesRun(Long timestamp) throws DataNotFoundException;

	/* delete */
	void remove(ExperimentSeriesRun experimentSeriesRun)
			throws DataNotFoundException;

	void remove(ExperimentSeries experimentSeries) throws DataNotFoundException;

	void remove(ScenarioInstance scenarioInstance) throws DataNotFoundException;


}

package org.sopeco.persistence;

import java.util.List;

import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.ProcessedDataSet;
import org.sopeco.persistence.entities.ScenarioInstance;
import org.sopeco.persistence.entities.analysis.IStorableAnalysisResult;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;
import org.sopeco.persistence.entities.keys.ExperimentSeriesPK;
import org.sopeco.persistence.entities.keys.ScenarioInstancePK;
import org.sopeco.persistence.exceptions.DataNotFoundException;

/**
 * TODO: Extend as needed. Implement a method only if needed.
 * 
 * @author Dennis Westermann
 * 
 */
public interface IPersistenceProvider {

	/* create, update */
	void store(ExperimentSeriesRun experimentSeriesRun);

	void store(ProcessedDataSet processedDataSet);

	void store(ExperimentSeries experimentSeries);

	void store(ScenarioInstance scenarioInstance);

	void store(ScenarioDefinition scenarioDefinition);

	void store(String resultId, IStorableAnalysisResult analysisResult);

	void store(DataSetAggregated dataSet);

	/* read */
	List<ScenarioInstance> loadScenarioInstances(String scenarioName) throws DataNotFoundException;

	List<ScenarioInstance> loadAllScenarioInstances() throws DataNotFoundException;

	List<ScenarioDefinition> loadAllScenarioDefinitions() throws DataNotFoundException;

	ScenarioInstance loadScenarioInstance(String scenarioName, String measurementEnvironmentUrl)
			throws DataNotFoundException;

	ScenarioDefinition loadScenarioDefinition(String scenarioName) throws DataNotFoundException;

	ScenarioInstance loadScenarioInstance(ScenarioInstancePK primaryKey) throws DataNotFoundException;

	ExperimentSeries loadExperimentSeries(String experimentSeriesName, String scenarioInstanceName,
			String measurementEnvironmentUrl) throws DataNotFoundException;

	ExperimentSeries loadExperimentSeries(String experimentSeriesName, Long version, String scenarioInstanceName,
			String measurementEnvironmentUrl) throws DataNotFoundException;

	List<ExperimentSeries> loadAllExperimentSeries(String experimentSeriesName, String scenarioInstanceName,
			String measurementEnvironmentUrl) throws DataNotFoundException;

	ExperimentSeries loadExperimentSeries(ExperimentSeriesPK primaryKey) throws DataNotFoundException;

	ExperimentSeriesRun loadExperimentSeriesRun(Long timestamp) throws DataNotFoundException;

	IStorableAnalysisResult loadAnalysisResult(String resultId) throws DataNotFoundException;

	DataSetAggregated loadDataSet(String dataSetId) throws DataNotFoundException;

	/* delete */
	void remove(ExperimentSeriesRun experimentSeriesRun) throws DataNotFoundException;

	void remove(ProcessedDataSet processedDataSet) throws DataNotFoundException;

	void remove(ExperimentSeries experimentSeries) throws DataNotFoundException;

	void remove(ScenarioInstance scenarioInstance) throws DataNotFoundException;

	void remove(ScenarioDefinition scenarioDefinition) throws DataNotFoundException;
	
	void remove(String analysisResultId) throws DataNotFoundException;

	void remove(DataSetAggregated dataSet) throws DataNotFoundException;

}

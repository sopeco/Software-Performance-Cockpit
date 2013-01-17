/**
 * Copyright (c) 2013 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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

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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sopeco.config.Configuration;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.util.ParameterType;
import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.ProcessedDataSet;
import org.sopeco.persistence.entities.ScenarioInstance;
import org.sopeco.persistence.entities.definition.ParameterRole;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;
import org.sopeco.persistence.exceptions.DataNotFoundException;
import org.sopeco.persistence.util.ScenarioDefinitionBuilder;

public class ProcessedDatasetTest {
	private static final String EXPERIMENT_SERIES = "EmpSeries";
	private static final String SCENARIO_NAME = "ScenarioName";
	private static final String ME_URL = "meURL";
	private static ProcessedDataSet dummyPDS;
	private static ExperimentSeriesRun dummyRun;
	private static ExperimentSeries dummyES;
	private static ScenarioInstance dummySI;
	private static IPersistenceProvider provider = null;
	private static DataSetAggregated dataSetAggregated = null;
	private static String sessionId = "testId";

	@Before
	public void setUp() throws Exception {
		Configuration.getSessionSingleton(sessionId).setProperty("sopeco.config.persistence.dbtype", "InMemory");
		provider = PersistenceProviderFactory.getInstance().getPersistenceProvider(sessionId);
		dummySI = createScenarioInstance();
		dummyES = createExpSeries(dummySI);
		dummyPDS = new ProcessedDataSet();
		dummyPDS.setId(String.valueOf(System.nanoTime()));
		dataSetAggregated = DummyFactory.createDummyResultDataSet();
		dummyPDS.setDataSet(dataSetAggregated);
		dummyES.addProcessedDataSet(dummyPDS);
		dummyRun = new ExperimentSeriesRun();
		dummyRun.setExperimentSeries(dummyES);
		dummyRun.setSuccessfulResultDataSet(dataSetAggregated);
		dummyRun.setTimestamp(System.nanoTime());
		dummyES.getExperimentSeriesRuns().add(dummyRun);
	}

	@After
	public void cleanUp() throws DataNotFoundException {
		provider.remove(dummySI);
		dummySI = null;
		dummyES = null;
		dummyPDS = null;
		dummyRun = null;

	}

	@Test
	public void testProcessedDatasetStoreAndLoad() {
		try {
			provider.store(dummyPDS);
			ScenarioInstance loadedSi = provider.loadScenarioInstance(SCENARIO_NAME, ME_URL);
			assertTrue(loadedSi.getExperimentSeriesList().get(0).getProcessedDataSets().size() == 1);
			assertEquals(dataSetAggregated.getID(), loadedSi.getExperimentSeriesList().get(0).getProcessedDataSets()
					.get(0).getDataSet().getID());
		} catch (DataNotFoundException e) {
			fail(e.getMessage());
		}

	}

	@Test
	public void testProcessedDatasetRemove() {
		try {
			provider.store(dummyPDS);
			provider.remove(dummyPDS);
			ScenarioInstance loadedSi = provider.loadScenarioInstance(SCENARIO_NAME, ME_URL);
			assertTrue(loadedSi.getExperimentSeriesList().get(0).getProcessedDataSets().size() == 0);
		} catch (DataNotFoundException e) {
			fail(e.getMessage());
		}
	}

	private ScenarioInstance createScenarioInstance() {
		ScenarioInstance si = new ScenarioInstance();
		si.setScenarioDefinition(getScenariodefinition());
		si.setMeasurementEnvironmentUrl(ME_URL);
		return si;
	}

	private ExperimentSeries createExpSeries(ScenarioInstance si) {
		ExperimentSeries es = new ExperimentSeries();
		es.setScenarioInstance(si);
		es.setVersion(0L);
		es.setName(EXPERIMENT_SERIES);
		es.setExperimentSeriesDefinition(si.getScenarioDefinition().getExperimentSeriesDefinition(EXPERIMENT_SERIES));
		return es;
	}

	private ScenarioDefinition getScenariodefinition() {
		ScenarioDefinitionBuilder builder = new ScenarioDefinitionBuilder(SCENARIO_NAME);
		createMeasurementEnvironment(builder);

		builder.createMeasurementSpecification("measSpec");
		createESD(builder);

		return builder.getScenarioDefinition();
	}

	private void createMeasurementEnvironment(ScenarioDefinitionBuilder builder) {
		builder.createNewNamespace("default");
		builder.createParameter("param 1", ParameterType.INTEGER, ParameterRole.INPUT);
		builder.createParameter("param 2", ParameterType.INTEGER, ParameterRole.INPUT);
		builder.createParameter("param 3", ParameterType.INTEGER, ParameterRole.OBSERVATION);
	}

	private void createESD(ScenarioDefinitionBuilder builder) {
		builder.createExperimentSeriesDefinition(EXPERIMENT_SERIES);

		builder.createExplorationStrategy("Full Exploration Strategy", Collections.EMPTY_MAP);
	}

}

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
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.ScenarioInstance;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;

/**
 * Test class for the {@link EntityFactory}.
 * 
 * @author Dennis Westermann
 * 
 */
public class EntityFactoryTest {

	private static ScenarioDefinition dummyScenarioDefinition;

	@Before
	public void setUp() throws Exception {

		dummyScenarioDefinition = DummyFactory.loadScenarioDefinition();

	}

	@Test
	public void testCreateScenarioInstance() {

		try {

			ScenarioInstance scenarioInstance = EntityFactory.createScenarioInstance(dummyScenarioDefinition, "Dummy");

			assertNotNull(scenarioInstance);
			assertEquals("Dummy", scenarioInstance.getName());
			assertEquals("Dummy", scenarioInstance.getMeasurementEnvironmentUrl());
			assertEquals(dummyScenarioDefinition, scenarioInstance.getScenarioDefinition());

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

	}

	@Test
	public void testCreateExperimentSeries() {

		try {
			ScenarioInstance scenarioInstance = EntityFactory.createScenarioInstance(dummyScenarioDefinition, "Dummy");
			ExperimentSeriesDefinition esd = dummyScenarioDefinition.getExperimentSeriesDefinition("Dummy0");
			ExperimentSeries series = EntityFactory.createExperimentSeries(esd);
			scenarioInstance.getExperimentSeriesList().add(series);
			series.setScenarioInstance(scenarioInstance);
		
			assertNotNull(series);
			assertEquals("Dummy0", series.getName());
			assertEquals(scenarioInstance, series.getScenarioInstance());
			assertEquals(esd.getName(), series.getExperimentSeriesDefinition().getName());
			assertEquals(0, series.getExperimentSeriesRuns().size());
			assertEquals(1, scenarioInstance.getExperimentSeriesList().size());
			assertEquals(series, scenarioInstance.getExperimentSeriesList().get(0));
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

	}
	
	
	@Test
	public void testCreateExperimentSeriesRun() {

		try {
			ScenarioInstance scenarioInstance = EntityFactory.createScenarioInstance(dummyScenarioDefinition, "Dummy");
			
			ExperimentSeriesDefinition esd = dummyScenarioDefinition.getExperimentSeriesDefinition("Dummy0");
			ExperimentSeries series = EntityFactory.createExperimentSeries(esd);
			scenarioInstance.getExperimentSeriesList().add(series);
			series.setScenarioInstance(scenarioInstance);
			
			ExperimentSeriesRun seriesRun = EntityFactory.createExperimentSeriesRun();
			series.getExperimentSeriesRuns().add(seriesRun);
			seriesRun.setExperimentSeries(series);
			
			assertNotNull(seriesRun);
			assertEquals(series, seriesRun.getExperimentSeries());
			assertEquals(scenarioInstance, seriesRun.getExperimentSeries().getScenarioInstance());
			assertNotNull(seriesRun.getTimestamp());
			assertEquals(null, seriesRun.getSuccessfulResultDataSet());
			assertEquals(1, series.getExperimentSeriesRuns().size());
			assertEquals(seriesRun, series.getExperimentSeriesRuns().get(0));
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

	}
}

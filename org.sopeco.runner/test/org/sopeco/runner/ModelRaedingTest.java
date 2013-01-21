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
package org.sopeco.runner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.net.URL;
import java.rmi.RemoteException;

import org.junit.Test;
import org.sopeco.config.Configuration;
import org.sopeco.config.IConfiguration;
import org.sopeco.engine.model.ScenarioDefinitionReader;
import org.sopeco.engine.model.ScenarioDefinitionWriter;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.persistence.entities.definition.ExperimentTerminationCondition;
import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.entities.definition.MeasurementSpecification;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;
import org.sopeco.util.Tools;

public class ModelRaedingTest {

	private static final String SCENARIO_NAME = "unitTestScenario";
	private static final String MS_NAME = "unitTestSpec";
	private static final String EXP_SERIES_NAME = "unitTestSeries";
	private static final String INIT_PARAMETER_NAME = "init.initParameter";
	private static final String PREPARE_PARAMETER_NAME = "prepare.prepareParameter";
	private static final String TERMINATION_CONDITION = "Number of Repetitions";
	private static final String TERMINATION_CONDITION_KEY = "repetitions";
	private static final String TERMINATION_CONDITION_VALUE = "2";
	private static final String EXPLORATION_STRATEGY = "Full Exploration Strategy";

	@Test
	public void testReadModel() {

		DummyMEController meController = new DummyMEController();
		MeasurementEnvironmentDefinition meDefinition = null;
		try {
			meDefinition = meController.getMEDefinition();
		} catch (RemoteException e) {
			fail(e.getMessage());
		}

		IConfiguration config = Configuration.getSessionSingleton(Tools.getUniqueTimeStamp()); 
		
		ScenarioDefinitionReader reader = new ScenarioDefinitionReader(meDefinition, config.getSessionId());

		URL url = this.getClass().getResource("/testScenario.xml");
		ScenarioDefinition scenarioDefinition = reader.readFromFile(url.getFile());

		assertEquals(scenarioDefinition.getScenarioName(), SCENARIO_NAME);
		assertEquals(scenarioDefinition.getMeasurementSpecifications().size(), 1);
		MeasurementSpecification ms = scenarioDefinition.getMeasurementSpecification(MS_NAME);
		assertNotNull(ms);
		assertEquals(ms.getInitializationAssignemts().size(), 1);
		assertEquals(ms.getInitializationAssignemts().get(0).getParameter().getFullName(), INIT_PARAMETER_NAME);
		assertEquals(ms.getInitializationAssignemts().get(0).getValue(), "1");

		ExperimentSeriesDefinition esd = scenarioDefinition.getExperimentSeriesDefinition(EXP_SERIES_NAME);
		assertNotNull(esd);
		assertEquals(esd.getTerminationConditions().size(), 1);
		ExperimentTerminationCondition etc = esd.getTerminationConditions().iterator().next();
		assertEquals(etc.getName(), TERMINATION_CONDITION);
		assertEquals(etc.getParamValue(TERMINATION_CONDITION_KEY), TERMINATION_CONDITION_VALUE);

		assertEquals(esd.getPreperationAssignments().size(), 1);
		assertEquals(esd.getPreperationAssignments().get(0).getParameter().getFullName(), PREPARE_PARAMETER_NAME);
		assertEquals(esd.getPreperationAssignments().get(0).getValue(), "2");

		assertEquals(esd.getExplorationStrategy().getName(), EXPLORATION_STRATEGY);

		assertEquals(esd.getExperimentAssignments().size(), 2);

	}

	@Test
	public void testWriteAndRead() {
		IConfiguration config = Configuration.getSessionSingleton(Tools.getUniqueTimeStamp()); 
		
		String tempDir = System.getProperty("java.io.tmpdir");
		tempDir = tempDir.replace("\\", "/");
		assertNotNull(tempDir);
		assertFalse(tempDir.equals(""));

		ScenarioDefinition scenario = DummyModelBuilder.getReferenceScenariodefinition();
		MeasurementEnvironmentDefinition meDefinition = scenario.getMeasurementEnvironmentDefinition();

		ScenarioDefinitionWriter writer = new ScenarioDefinitionWriter(config.getSessionId());

		String filePath = tempDir + (tempDir.endsWith("/") ? "" : "/") + "tempScenarioDefinition.xml";
		writer.writeToFile(scenario, filePath);

		ScenarioDefinitionReader reader = new ScenarioDefinitionReader(meDefinition, config.getSessionId());
		ScenarioDefinition loadedScenario = reader.readFromFile(filePath);
		assertEquals(scenario, loadedScenario);

	}
}

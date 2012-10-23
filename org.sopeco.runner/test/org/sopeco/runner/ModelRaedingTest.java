package org.sopeco.runner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.net.URL;
import java.rmi.RemoteException;

import org.junit.Test;
import org.sopeco.engine.model.ScenarioDefinitionFileReader;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.entities.definition.MeasurementSpecification;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;

public class ModelRaedingTest {

	private static final String SCENARIO_NAME = "unitTestScenario";
	private static final String MS_NAME = "unitTestSpec";
	private static final String EXP_SERIES_NAME = "unitTestSeries";
	private static final String INIT_PARAMETER_NAME = "init.initParameter";
	private static final String PREPARE_PARAMETER_NAME = "prepare.prepareParameter";
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

		ScenarioDefinitionFileReader reader = new ScenarioDefinitionFileReader(meDefinition);

		URL url = this.getClass().getResource("/testScenario.xml");
		ScenarioDefinition scenarioDefinition = reader.read(url.getFile());

		assertEquals(scenarioDefinition.getScenarioName(), SCENARIO_NAME);
		assertEquals(scenarioDefinition.getMeasurementSpecifications().size(), 1);
		MeasurementSpecification ms = scenarioDefinition.getMeasurementSpecification(MS_NAME);
		assertNotNull(ms);
		assertEquals(ms.getInitializationAssignemts().size(), 1);
		assertEquals(ms.getInitializationAssignemts().get(0).getParameter().getFullName(), INIT_PARAMETER_NAME);
		assertEquals(ms.getInitializationAssignemts().get(0).getValue(), "1");

		ExperimentSeriesDefinition esd = scenarioDefinition.getExperimentSeriesDefinition(EXP_SERIES_NAME);
		assertNotNull(esd);

		assertEquals(esd.getPreperationAssignments().size(), 2);
		assertEquals(esd.getPreperationAssignments().get(0).getParameter().getFullName(), PREPARE_PARAMETER_NAME);
		assertEquals(esd.getPreperationAssignments().get(0).getValue(), "2");

		assertEquals(esd.getExplorationStrategy().getName(), EXPLORATION_STRATEGY);

		assertEquals(esd.getExperimentAssignments().size(), 2);

	}
}

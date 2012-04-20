/**
 * 
 */
package org.sopeco.core.persistence;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.sopeco.model.configuration.SoPeCoModelFactoryHandler;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.PersistenceProviderFactory;
import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ScenarioInstance;
import org.sopeco.persistence.exceptions.DataNotFoundException;

/**
 * @author D053105
 *
 */
public class PersistenceProviderTest extends TestCase{

	IPersistenceProvider provider;
	ScenarioInstance dummyScenarioInstance;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		SoPeCoModelFactoryHandler.initFactories();
		provider = PersistenceProviderFactory.getPersistenceProvider();

	}
	

	@Test
	public void testStoreAndLoadScenarioInstance() {
		dummyScenarioInstance = DummyFactory.createDummyScenarioInstance();
		try{
			provider.store(dummyScenarioInstance);
		}catch(Exception e){
			e.printStackTrace();
		}
		ScenarioInstance loadedInstance;
		try {
			loadedInstance = provider.loadScenarioInstance(dummyScenarioInstance.getName(), dummyScenarioInstance.getMeasurementEnvironmentUrl());
		} catch (DataNotFoundException e) {
			fail(e.getMessage());return;
		}
		assertNotNull(loadedInstance);
		assertTrue(dummyScenarioInstance.equals(loadedInstance));
		checkAvailableExperimentSeries(loadedInstance);
	}

	@Test
	public void testStoreAndLoadExperimentSeries() {
		dummyScenarioInstance = DummyFactory.createDummyScenarioInstance();
		ExperimentSeries dummySeries = dummyScenarioInstance.getExperimentSeries().get(0);
		provider.store(dummySeries);
		ExperimentSeries loadedSeries;
		try {
			loadedSeries = provider.loadExperimentSeries(dummySeries.getName(), dummySeries.getScenarioInstance().getName(), dummySeries.getScenarioInstance().getMeasurementEnvironmentUrl());
		} catch (DataNotFoundException e) {
			fail(e.getMessage());return;
		}
		assertNotNull(loadedSeries);
		assertTrue(dummySeries.equals(loadedSeries));
		assertNotNull(loadedSeries.getScenarioInstance());
		checkAvailableExperimentSeries(loadedSeries.getScenarioInstance());
	}

	
	private void checkAvailableExperimentSeries(ScenarioInstance scenarioInstance) {

		// We should have 2 ExperimentSeries in the database
		assertTrue(scenarioInstance.getExperimentSeries().size() == 2);

		// ExperimentSeries should reference the ScenarioInstance
		assertNotNull(scenarioInstance.getExperimentSeries().get(0).getScenarioInstance());
		assertTrue(scenarioInstance.getExperimentSeries().get(0).getScenarioInstance().getName().equalsIgnoreCase("Dummy"));
		assertTrue(scenarioInstance.equals(scenarioInstance.getExperimentSeries().get(0).getScenarioInstance()));

		// ExperimentSeries should reference the ExperimentSeriesDefintion
		assertNotNull(scenarioInstance.getExperimentSeries().get(0).getExperimentSeriesDefinition());
		assertTrue(scenarioInstance.getExperimentSeries().get(0).getExperimentSeriesDefinition().getName().equalsIgnoreCase("Dummy0"));

		// Name of ExperimentSeries should be equal to the name of its definition
		assertTrue(scenarioInstance.getExperimentSeries().get(0).getName().equals(scenarioInstance.getExperimentSeries().get(0).getExperimentSeriesDefinition().getName()));

		// ExperimentSeries should reference ExperimentSeriesRuns
		checkAvailableExperimentSeriesRuns(scenarioInstance.getExperimentSeries().get(0));
	}
	
	private void checkAvailableExperimentSeriesRuns(ExperimentSeries expSeries) {

		// We should have 10 ExperimentSeriesRuns in the database
		assertTrue(expSeries.getExperimentSeriesRuns().size() == 10);

		// ExperimentSeriesRun should reference the ExperimentSeries
		assertNotNull(expSeries.getExperimentSeriesRuns().get(0).getExperimentSeries().getName());
		assertTrue(expSeries.getExperimentSeriesRuns().get(0).getExperimentSeries().getName().equalsIgnoreCase("Dummy0"));
		assertTrue(expSeries.equals(expSeries.getExperimentSeriesRuns().get(0).getExperimentSeries()));

		// ExperimentSeriesRun should contain a result data set
		assertNotNull(expSeries.getExperimentSeriesRuns().get(0).getResultDataSet());
		assertTrue(expSeries.getExperimentSeriesRuns().get(0).getResultDataSet().getObservationColumns().size() == 1);
		assertTrue(expSeries.getExperimentSeriesRuns().get(0).getResultDataSet().getInputColumns().size() == 1);
	
	}

}

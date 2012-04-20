/**
 * 
 */
package org.sopeco.persistence;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sopeco.model.configuration.SoPeCoModelFactoryHandler;
import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.ScenarioInstance;
import org.sopeco.persistence.exceptions.DataNotFoundException;
/**
 * @author Dennis Westermann
 *
 */
public class PersistenceProviderTest {

	static JPAPersistenceProvider provider;
	static ScenarioInstance dummyScenarioInstance;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setItUp() throws Exception {

		SoPeCoModelFactoryHandler.initFactories();
		provider = (JPAPersistenceProvider) PersistenceProviderFactory.getPersistenceProvider();
		dummyScenarioInstance = DummyFactory.createDummyScenarioInstance();
	}
	
	@Before
	public void setUp() {
		assertEquals(0, provider.getSize(ExperimentSeriesRun.class));
		assertEquals(0, provider.getSize(ExperimentSeries.class));
		assertEquals(0, provider.getSize(ScenarioInstance.class));
	}
	
	@After
	public void tearDown() {
		provider.disposeAll();
	}
	

	@Test
	public void testStoreAndLoadScenarioInstance() {
		
		assertEquals(0, provider.getSize(ExperimentSeriesRun.class));
		try{
			provider.store(dummyScenarioInstance);
		}catch(Exception e){
			e.printStackTrace();
		}
		assertEquals(20, provider.getSize(ExperimentSeriesRun.class));
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
	public void testStoreLoadRemove() {
		assertEquals(0, provider.getSize(ExperimentSeriesRun.class));
		
		try{
			provider.store(dummyScenarioInstance);
			assertEquals(20, provider.getSize(ExperimentSeriesRun.class));
			final ScenarioInstance loaded1 = provider.loadScenarioInstance(dummyScenarioInstance.getName(), dummyScenarioInstance.getMeasurementEnvironmentUrl());
			assertEquals(10, loaded1.getExperimentSeries().get(0).getExperimentSeriesRuns().size());
			loaded1.getExperimentSeries().get(0).getExperimentSeriesRuns().remove(0);
			provider.store(loaded1);
			assertEquals(19, provider.getSize(ExperimentSeriesRun.class));
			final ScenarioInstance loaded2 = provider.loadScenarioInstance(dummyScenarioInstance.getName(), dummyScenarioInstance.getMeasurementEnvironmentUrl());
			assertEquals(9, loaded2.getExperimentSeries().get(0).getExperimentSeriesRuns().size());
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
	}
	
	@Test
	public void testLoadScenarioInstancesByName() {

		try {
			provider.store(dummyScenarioInstance);
			List<ScenarioInstance> loadedInstances;
			loadedInstances = provider.loadScenarioInstances(dummyScenarioInstance.getName());
			System.out.println(loadedInstances.get(0).getExperimentSeries().get(0).getExperimentSeriesRuns().size());
			System.out.println(loadedInstances);
		} catch (DataNotFoundException e) {
			fail(e.getMessage());return;
		}
		try{
			provider.store(dummyScenarioInstance);
		}catch(Exception e){
			e.printStackTrace();
		}
		List<ScenarioInstance> loadedInstances;
		try {
			loadedInstances = provider.loadScenarioInstances(dummyScenarioInstance.getName());
		} catch (DataNotFoundException e) {
			fail(e.getMessage());return;
		}
		loadedInstances.get(0).getExperimentSeries().get(0).getExperimentSeriesRuns().size();
		assertNotNull(loadedInstances);
		assertTrue(loadedInstances.size() == 1);
		assertTrue(dummyScenarioInstance.equals(loadedInstances.get(0)));
	}
	
	@Test
	public void testRemoveScenarioInstance() {
		
		
		try {
			provider.store(dummyScenarioInstance);
			provider.remove(dummyScenarioInstance);
		} catch (DataNotFoundException e) {
			fail(e.getMessage());return;
		}
		
		assertTrue(isRemoved(dummyScenarioInstance));
		
		// experiment series and runs should be removed as well
		assertTrue(isRemoved(dummyScenarioInstance.getExperimentSeries().get(0)));
		assertTrue(isRemoved(dummyScenarioInstance.getExperimentSeries().get(0).getExperimentSeriesRuns().get(0)));
		
	}

	@Test
	public void testStoreAndLoadExperimentSeries() {
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
	
	@Test
	public void testRemoveExperimentSeries() {
		
		ExperimentSeries dummySeries = dummyScenarioInstance.getExperimentSeries().get(0);
		
		
		try {
			provider.store(dummySeries);
			provider.remove(dummySeries);
		} catch (DataNotFoundException e) {
			fail(e.getMessage());return;
		}
		
		assertTrue(isRemoved(dummySeries));
		
		// experiment series runs should be removed as well
		assertTrue(isRemoved(dummySeries.getExperimentSeriesRuns().get(0)));
				
		// scenario instance should not be removed
		assertFalse(isRemoved(dummySeries.getScenarioInstance()));
	}
	
	@Test
	public void testStoreAndLoadExperimentSeriesRun() {
		ExperimentSeriesRun dummySeriesRun = dummyScenarioInstance.getExperimentSeries().get(0).getExperimentSeriesRuns().get(0);
		provider.store(dummySeriesRun);
		ExperimentSeriesRun loadedSeriesRun;
		try {
			loadedSeriesRun = provider.loadExperimentSeriesRun(dummySeriesRun.getTimestamp());
		} catch (DataNotFoundException e) {
			fail(e.getMessage());return;
		}
		assertNotNull(loadedSeriesRun);
		assertTrue(dummySeriesRun.equals(loadedSeriesRun));
		assertNotNull(loadedSeriesRun.getExperimentSeries());
		assertTrue(dummySeriesRun.getExperimentSeries().equals(loadedSeriesRun.getExperimentSeries()));
		assertNotNull(loadedSeriesRun.getExperimentSeries().getScenarioInstance());
		checkAvailableExperimentSeriesRuns(loadedSeriesRun.getExperimentSeries());
	}

	@Test
	public void testRemoveExperimentSeriesRun() {
		
		ExperimentSeriesRun dummySeriesRun = dummyScenarioInstance.getExperimentSeries().get(0).getExperimentSeriesRuns().get(0);
		
		try {
			provider.store(dummySeriesRun);
			provider.remove(dummySeriesRun);
		} catch (DataNotFoundException e) {
			fail(e.getMessage());return;
		}
		
		assertTrue(isRemoved(dummySeriesRun));
		
		// experiment series should not be removed
		assertFalse(isRemoved(dummySeriesRun.getExperimentSeries()));
				
	}
	
	private boolean isRemoved(ScenarioInstance instance){
		ScenarioInstance loadedInstance;
		try {
			loadedInstance = provider.loadScenarioInstance(instance.getName(), instance.getMeasurementEnvironmentUrl());
		} catch (DataNotFoundException e) {
			loadedInstance = null;
		}
		if (loadedInstance == null){
			return true;
		}
		return false;
	}
	
	private boolean isRemoved(ExperimentSeries series){
		ExperimentSeries loadedSeries;
		try {
			loadedSeries = provider.loadExperimentSeries(series.getName(), series.getScenarioInstance().getName(), series.getScenarioInstance().getMeasurementEnvironmentUrl());
		} catch (DataNotFoundException e) {
			loadedSeries=null;
		}
		if (loadedSeries == null){
			return true;
		}
		return false;
	}
	
	private boolean isRemoved(ExperimentSeriesRun run){
		ExperimentSeriesRun loadedRun;
		try {
			loadedRun = provider.loadExperimentSeriesRun(run.getTimestamp());
		} catch (DataNotFoundException e) {
			loadedRun=null;
		}
		if (loadedRun == null){
			return true;
		}
		return false;
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

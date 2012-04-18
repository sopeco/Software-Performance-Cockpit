package org.sopeco.core.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;


import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.sopeco.configuration.parameter.ParameterDefinition;
import org.sopeco.configuration.parameter.ParameterFactory;
import org.sopeco.configuration.parameter.ParameterRole;
import org.sopeco.configuration.parameter.ParameterType;
import org.sopeco.configuration.parameter.ParameterUsage;
import org.sopeco.core.model.configuration.measurements.ExperimentSeriesDefinition;
import org.sopeco.core.model.configuration.measurements.MeasurementsFactory;
import org.sopeco.persistence.advanced.DataSetAggregated;
import org.sopeco.persistence.advanced.DataSetColumnBuilder;
import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.ScenarioInstance;

public class JpaTest {

	private static final String PERSISTENCE_UNIT_NAME = "sopeco";
	private EntityManagerFactory factory;

	@Before
	public void setUp() throws Exception {
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		EntityManager em = factory.createEntityManager();

		// Begin a new local transaction so that we can persist a new entity
		em.getTransaction().begin();

		// Read the existing entries
		Query q = em.createQuery("select s from ScenarioInstance s");
		// ScenarioInstance should be empty

		// Do we have entries?
		boolean createNewEntries = (q.getResultList().size() == 0);

		// No, so lets create new entries
		if (createNewEntries) {
			TestCase.assertTrue(q.getResultList().size() == 0);
			ScenarioInstance scenario = new ScenarioInstance();
			scenario.setName("scenarioName");
			scenario.setDescription("scenarioDescr");
			scenario.setMeasurementEnvironmentUrl("measurementEnvUrl");
			em.persist(scenario);
			for (int i = 0; i < 40; i++) {
				ExperimentSeries expSeries = new ExperimentSeries();
				
				expSeries.ssetExperimentSeriesConfigurationId("configId_" + i);
				expSeries.setExperimentSeriesConfiguration(
						new ExperimentSeriesConfiguration("configId_" + i, 
								new ArrayList<String>()));
				expSeries.setScenarioInstance(scenario);
				
				expSeries.getExperimentSeriesRuns().add(createDummyDataSet());
				expSeries.getExperimentSeriesRuns().add(createDummyDataSet());
				
				em.persist(expSeries);
				// Now persists the scenario expSeries relationship
				scenario.getExperimentSeries().add(expSeries);
				em.persist(expSeries);
				em.persist(scenario);
			}
		}

		// Commit the transaction, which will cause the entity to
		// be stored in the database
		em.getTransaction().commit();

		// It is always good practice to close the EntityManager so that
		// resources are conserved.
		em.close();

	}
	
	private ExperimentSeriesDefinition createExperimentSeriesDefinition(){
		ExperimentSeriesDefinition expSeriesDef = MeasurementsFactory.eINSTANCE.createExperimentSeriesDefinition();
		expSeriesDef.
	}
	
	
	private DataSetAggregated createDummyDataSet(){
		DataSetColumnBuilder builder = new DataSetColumnBuilder();
		ParameterUsage pu1 = ParameterFactory.eINSTANCE.createParameterUsage();
		ParameterDefinition pd1 = ParameterFactory.eINSTANCE.createParameterDefinition();
		pd1.setName("param1");
		pd1.setRole(ParameterRole.INPUT);
		pd1.setType(ParameterType.INTEGER);
		pu1.setParameterDefinition(pd1);
		builder.startInputColumn(pu1);
		builder.addInputValue(1);
		builder.finishColumn();
		
		ParameterUsage pu2 = ParameterFactory.eINSTANCE.createParameterUsage();
		ParameterDefinition pd2 = ParameterFactory.eINSTANCE.createParameterDefinition();
		pd2.setName("param2");
		pd2.setRole(ParameterRole.OBSERVATION);
		pd2.setType(ParameterType.DOUBLE);
		pu2.setParameterDefinition(pd2);
		
		builder.startObservationColumn(pu2);
		List<Object> obsValues = new ArrayList<Object>();
		obsValues.add(2.0);
		builder.addObservationValues(obsValues);
		builder.finishColumn();
		
		return builder.createDataSet();
	}

	@Test
	public void checkAvailableExperimentSeries() {

		// Now lets check the database and see if the created entries are there
		// Create a fresh, new EntityManager
		EntityManager em = factory.createEntityManager();

		// Perform a simple query for all the entities
		Query q = em.createQuery("select e from ExperimentSeries e");

		// We should have 40 ExperimentSeries in the database
		TestCase.assertTrue(q.getResultList().size() == 40);

		// ExperimentSeries should reference the ScenarioInstance
		TestCase.assertTrue(((ExperimentSeriesRun) q.getResultList().get(0)).getScenarioInstance().getName().equalsIgnoreCase("scenarioName"));

		// ExperimentSeries should reference the ExperimentSeriesConfiguration
		TestCase.assertTrue(((ExperimentSeriesRun) q.getResultList().get(0)).getExperimentSeriesConfiguration().getConfigId().startsWith("configId"));

		// ExperimentSeries should reference ExperimentSeriesRuns
		List<DataSetAggregated> runs = ((ExperimentSeriesRun) q.getResultList().get(0)).getExperimentSeriesRuns();
		TestCase.assertTrue(runs.size() == 2);
		TestCase.assertTrue(runs.get(0).getObservationColumns().size() == 1);
		em.close();
	}

	@Test
	public void checkScenarioInstance() {
		EntityManager em = factory.createEntityManager();
		// Go through each of the entities and print out each of their
		// messages, as well as the date on which it was created
		Query q = em.createQuery("select s from ScenarioInstance s");

		// We should have one ScenarioInstance with 40 ExperimentSeries
		TestCase.assertTrue(q.getResultList().size() == 1);
		TestCase.assertTrue(((ScenarioInstance) q.getSingleResult()).getExperimentSeries().size() == 40);
		em.close();
	}

	@Test(expected = javax.persistence.NoResultException.class)
	public void deleteExpSeries() {
		EntityManager em = factory.createEntityManager();
		// Begin a new local transaction so that we can persist a new entity
		em.getTransaction().begin();
		Query q = em
				.createQuery("SELECT e FROM ExperimentSeries e WHERE e.experimentSeriesConfigurationId = :configId");
		q.setParameter("configId", "configId_1");
		ExperimentSeriesRun series = (ExperimentSeriesRun) q.getSingleResult();
		em.remove(series);
		em.getTransaction().commit();
		ExperimentSeriesRun expSeries = (ExperimentSeriesRun) q.getSingleResult();
		// Begin a new local transaction so that we can persist a new entity

		em.close();
	}

}

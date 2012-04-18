package org.sopeco.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.sopeco.persistence.entities.ScenarioInstance;

public class SampleStarter {

	private static final String PERSISTENCE_UNIT_NAME = "sopeco";
	private static EntityManagerFactory factory;

	public static void main(String[] args) {
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		EntityManager em = factory.createEntityManager();
		// Read the existing entries and write to console
		Query q = em.createQuery("Select t from ScenarioInstance t");
		List<ScenarioInstance> scenarioInstances = q.getResultList();
		for (ScenarioInstance scenario : scenarioInstances) {
			System.out.println(scenario.getId());
		}
		System.out.println("Size: " + scenarioInstances.size());

		// Create new ScenarioInstance
		em.getTransaction().begin();
		ScenarioInstance scenario = new ScenarioInstance();
		scenario.setName("testname");
		scenario.setDescription("This is a test description.");
		em.persist(scenario);
		em.getTransaction().commit();

		em.close();
	}

}

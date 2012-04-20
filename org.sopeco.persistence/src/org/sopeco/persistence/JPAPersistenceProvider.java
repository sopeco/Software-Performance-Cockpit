package org.sopeco.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.ScenarioInstance;
import org.sopeco.persistence.entities.keys.ExperimentSeriesPK;
import org.sopeco.persistence.entities.keys.ScenarioInstancePK;
import org.sopeco.persistence.exceptions.DataNotFoundException;
/**
 * This class implements the SoPeCo persistence interface based on
 * the Java Peristence API. 
 * 
 * @author Dennis Westermann
 *
 */
public class JPAPersistenceProvider implements IPersistenceProvider{

	
	
	private static EntityManagerFactory factory;
	
	private static Logger logger = LoggerFactory.getLogger(JPAPersistenceProvider.class);
	
	/**
	 * This constructor creates an instance of {@link JPAPersistenceProvider} and
	 * creates an {@link EntityManagerFactory} for the given persistence unit.
	 * 
	 * @param peristenceUnit - the name of the peristence unit that should be used by the provider
	 */
	protected JPAPersistenceProvider(String peristenceUnit){
		 
		logger.debug("Create EntityManagerFactory for persistence unit {}.", peristenceUnit);
		try{
//			Map properties = new HashMap();
//			// Ensure RESOURCE_LOCAL transactions is used.
//			properties.put(TRANSACTION_TYPE,
//			  PersistenceUnitTransactionType.RESOURCE_LOCAL.name());
//
//			// Configure the internal EclipseLink connection pool
//			properties.put(JDBC_DRIVER, "oracle.jdbc.OracleDriver");
//			properties.put(JDBC_URL, "jdbc:oracle:thin:@localhost:1521:ORCL");
//			properties.put(JDBC_USER, "user-name");
//			properties.put(JDBC_PASSWORD, "password");
//			properties.put(JDBC_READ_CONNECTIONS_MIN, "1");
//			properties.put(JDBC_WRITE_CONNECTIONS_MIN, "1");
//
//			// Configure logging. FINE ensures all SQL is shown
//			properties.put(LOGGING_LEVEL, "FINE");
//
//			// Ensure that no server-platform is configured
//			properties.put(TARGET_SERVER, TargetServer.None);
//			
		factory = Persistence.createEntityManagerFactory(peristenceUnit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	@Override
	public void store(ExperimentSeriesRun experimentSeriesRun) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void store(ExperimentSeries experimentSeries) {
		EntityManager em = factory.createEntityManager();
		try {
			
			em.getTransaction().begin();
			em.merge(experimentSeries);
			em.getTransaction().commit();
			
		
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			em.close();
		}
		
	}

	@Override
	public void store(ScenarioInstance scenarioInstance) {
		EntityManager em = factory.createEntityManager();
		try {
			
			em.getTransaction().begin();
			em.merge(scenarioInstance);
			em.getTransaction().commit();
			
		
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			em.close();
		}
		
	}

	@Override
	public ExperimentSeries loadExperimentSeries(String experimentSeriesName, String scenarioInstanceName, String measurementEnvironmentUrl) throws DataNotFoundException {
		ExperimentSeries experimentSeries;
		
		EntityManager em = factory.createEntityManager();
		try {
			experimentSeries = em.find(ExperimentSeries.class, new ExperimentSeriesPK(experimentSeriesName, scenarioInstanceName, measurementEnvironmentUrl));
		} catch (Exception e) {
			String errorMsg = "Could not find experiment series for scenario instance { " 
					+ scenarioInstanceName + ", " + measurementEnvironmentUrl + " and series name " 
					+ experimentSeriesName + " .";
			logger.error(errorMsg);
			throw new DataNotFoundException(errorMsg);
		} finally {
		  em.close();
		}
		
		return experimentSeries;
	
	}

	@Override
	public List<ExperimentSeriesRun> loadExperimentSeriesRuns(
			String experimentSeriesId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ScenarioInstance> loadScenarioInstances(String scenarioName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScenarioInstance loadScenarioInstance(String scenarioName,
			String measurementEnvironmentUrl) throws DataNotFoundException {
		
		ScenarioInstance scenarioInstance;
		String errorMsg = "Could not find scenario instance for scenario " 
				+ scenarioName + " and measurement environment URL" 
				+ measurementEnvironmentUrl + " .";
		
		EntityManager em = factory.createEntityManager();
		try {
			
			scenarioInstance = em.find(ScenarioInstance.class, new ScenarioInstancePK(scenarioName, measurementEnvironmentUrl));

		} catch (Exception e) {
			
			logger.error(errorMsg);
			throw new DataNotFoundException(errorMsg, e);
		}  finally {
		  em.close();
		}
		
		// check if query was successful
		if(scenarioInstance != null){
			return scenarioInstance;
		} else {
			logger.debug(errorMsg);
			throw new DataNotFoundException(errorMsg);
		}

	}
	

}

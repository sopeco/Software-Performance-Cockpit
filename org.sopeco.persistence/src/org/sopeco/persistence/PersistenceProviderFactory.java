package org.sopeco.persistence;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.model.configuration.ScenarioDefinition;
import org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition;
import org.sopeco.persistence.config.DBType;
import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.ScenarioInstance;

/**
 * Factory class that allows access to a persistence provider ({@link IPersistenceProvider}). 
 * The persistence provider is a singleton.
 * Currently, we have only one type of persistence provider (JPA persistence). 
 * Moreover, the factory is used to create new instance of the SoPeCo persistence entities.
 * 
 * @author Dennis Westermann
 *
 */
public class PersistenceProviderFactory {

	// JPA Provider constants
	private final static String PERSISTENCE_UNIT_VALUE = "sopeco";
	
	private final static String DB_DRIVER_CLASS = "javax.persistence.jdbc.driver";
	private final static String DB_URL = "javax.persistence.jdbc.url"; 
	
	private final static String SERVER_DB_DRIVER_CLASS_VALUE = "org.apache.derby.jdbc.ClientDriver";
	private final static String SERVER_DB_URL_VALUE = "jdbc:derby://localhost/sopeco-jpa;create=true";
	
	private final static String MEM_DB_DRIVER_CLASS_VALUE = "org.apache.derby.jdbc.EmbeddedDriver";
	private final static String MEM_DB_URL_VALUE = "jdbc:derby:memory:sopeco-jpa;create=true";
	
	
	// General persistence provider constants TODO: retrieve values from config registry
	private final static String DB_TYPE = "DB_TYPE";
	protected static DBType DB_TYPE_VALUE = DBType.MEM;
	
	private static Logger logger = LoggerFactory.getLogger(PersistenceProviderFactory.class);
	
	private static IPersistenceProvider persistenceProviderInstance = null;

    
	public static IPersistenceProvider getPersistenceProvider(){
		
		if(persistenceProviderInstance == null){
			logger.debug("Creating new peristence provider instance.");
			persistenceProviderInstance = createJPAPersistenceProvider();
		}   
		
		return persistenceProviderInstance;
	}
	
	
	private static IPersistenceProvider createJPAPersistenceProvider(){
		try{
			
			logger.debug("Create EntityManagerFactory for persistence unit {}.", PERSISTENCE_UNIT_VALUE);	
			
			Map<String, Object> configOverrides = getConfigOverrides(DB_TYPE_VALUE);
			
			EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_VALUE, configOverrides);
			
			return new JPAPersistenceProvider(factory);
		} catch (Exception e) {
			throw new IllegalArgumentException("Could not create peristence provider!", e);
		}
	}


	private static Map<String, Object> getConfigOverrides(DBType dbType) {
		Map<String, Object> configOverrides = new HashMap<String, Object>();
		
		switch (dbType) {
		case MEM:
			configOverrides.put(DB_DRIVER_CLASS, MEM_DB_DRIVER_CLASS_VALUE);
			configOverrides.put(DB_URL, MEM_DB_URL_VALUE);
			break;
			
		case SERVER:
			configOverrides.put(DB_DRIVER_CLASS, SERVER_DB_DRIVER_CLASS_VALUE);
			configOverrides.put(DB_URL, SERVER_DB_URL_VALUE);
			break;
		
		default:
			break;
		}
		return configOverrides;
	}
	
	
	/**
	 * Creates a new instance of the {@link ScenarioInstance} entity. Sets the name and measurementEnvironmentUrl according to the given parameters.
	 * 
	 * @param scenarioDefinition
	 * @param measurementEnvironmentUrl
	 * 
	 * @return a new instance of {@link ScenarioInstance} based on the given parameters
	 */
	public static ScenarioInstance createScenarioInstance(ScenarioDefinition scenarioDefinition, String measurementEnvironmentUrl){
		ScenarioInstance si = new ScenarioInstance();
		si.setScenarioDefinition(scenarioDefinition);
		si.setName(scenarioDefinition.getName());
		si.setMeasurementEnvironmentUrl(measurementEnvironmentUrl);
		return si;
	}
	
	/**
	 * Creates a new instance of the {@link ExperimentSeries} entity. 
	 * Sets the name, experimentDefintion and scenarioInstance according to the given parameters.
	 * Adds the newly created instance to the given scenarioInstance (resolving the relationship).
	 * 
	 * @param scenarioInstance
	 * @param expSeriesDefinition
	 * @return a new instance of {@link ExperimentSeries} based on the given parameters
	 */
	public static ExperimentSeries createExperimentSeries(ScenarioInstance scenarioInstance, ExperimentSeriesDefinition expSeriesDefinition){
		ExperimentSeries expSeries = new ExperimentSeries();
		expSeries.setName(expSeriesDefinition.getName());
		expSeries.setScenarioInstance(scenarioInstance);
		scenarioInstance.getExperimentSeries().add(expSeries);
		
		return expSeries;
	}
	
	
	/**
	 * Creates a new instance of the {@link ExperimentSeriesRun} entity. 
	 * Sets the given experimentSeries as well as the timestamp (to the current {@link System.nanoTime()}).
	 * Adds the newly created instance to the given experiment series (resolving the relationship).
	 * 
	 * @param scenarioInstance
	 * @param expSeriesDefinition
	 * @return a new instance of {@link ExperimentSeriesRun} based on the given parameters
	 */
	public static ExperimentSeriesRun createExperimentSeriesRun(ExperimentSeries experimentSeries){
		ExperimentSeriesRun expSeriesRun = new ExperimentSeriesRun();
		
		expSeriesRun.setTimestamp(System.nanoTime());
		expSeriesRun.setExperimentSeries(experimentSeries);
		experimentSeries.getExperimentSeriesRuns().add(expSeriesRun);
		return expSeriesRun;
	}
	
	
}

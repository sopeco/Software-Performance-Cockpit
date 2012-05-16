package org.sopeco.persistence;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.persistence.config.PersistenceConfiguration;

/**
 * Factory class that allows access to a persistence provider ({@link IPersistenceProvider}). 
 * The persistence provider is a singleton.
 * Currently, we have only one type of persistence provider (JPA persistence). 
 * 
 * @author Dennis Westermann
 *
 */
public class PersistenceProviderFactory {

	// JPA Provider constants
	private final static String PERSISTENCE_UNIT_VALUE = "sopeco";
	
	private final static String DB_DRIVER_CLASS = "javax.persistence.jdbc.driver";
	private final static String SERVER_DB_DRIVER_CLASS_VALUE = "org.apache.derby.jdbc.ClientDriver";
	private final static String MEM_DB_DRIVER_CLASS_VALUE = "org.apache.derby.jdbc.EmbeddedDriver";
	
	private final static String DB_URL = "javax.persistence.jdbc.url";
	private final static String MEM_DB_URL_VALUE = "jdbc:derby:memory:sopeco-jpa;create=true";
	
	private final static String DDL_GENERATION = "eclipselink.ddl-generation";
	
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
			
			PersistenceConfiguration persistenceConfig = new PersistenceConfiguration();
			
			logger.debug("Create EntityManagerFactory for persistence unit {}.", PERSISTENCE_UNIT_VALUE);	
			
			Map<String, Object> configOverrides = getConfigOverrides(persistenceConfig);
			
			EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_VALUE, configOverrides);
			
			return new JPAPersistenceProvider(factory);
		} catch (Exception e) {
			throw new IllegalArgumentException("Could not create peristence provider!", e);
		}
	}


	private static Map<String, Object> getConfigOverrides(PersistenceConfiguration persistenceConfig) {
		Map<String, Object> configOverrides = new HashMap<String, Object>();
		
		switch (persistenceConfig.getDBType()) {
		case InMemory:
			configOverrides.put(DB_DRIVER_CLASS, MEM_DB_DRIVER_CLASS_VALUE);
			configOverrides.put(DB_URL, MEM_DB_URL_VALUE);
			break;
			
		case Server:
			configOverrides.put(DB_DRIVER_CLASS, SERVER_DB_DRIVER_CLASS_VALUE);
			configOverrides.put(DB_URL, persistenceConfig.getServerUrl());
			break;
		
		default:
			break;
		}
		
		
		configOverrides.put(DDL_GENERATION, persistenceConfig.getDDLGenerationStrategy());
		
		return configOverrides;
	}
	
	
	
	
	
}

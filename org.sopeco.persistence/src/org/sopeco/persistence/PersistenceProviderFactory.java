package org.sopeco.persistence;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.persistence.config.PersistenceConfiguration;
import org.sopeco.persistence.config.PersistenceConfiguration.DBType;
import org.sopeco.persistence.exceptions.DataNotFoundException;
import org.sopeco.persistence.metadata.entities.DatabaseInstance;

/**
 * Factory class that allows access to a persistence provider (
 * {@link IPersistenceProvider}). The persistence provider is a singleton.
 * Currently, we have only one type of persistence provider (JPA persistence).
 * 
 * @author Dennis Westermann
 * 
 */
public class PersistenceProviderFactory {

	// JPA Provider constants
	private final static String PERSISTENCE_UNIT_VALUE = "sopeco";
	private final static String META_DATA_PERSISTENCE_UNIT_VALUE = "sopeco_metadata";
	private final static String DB_DRIVER_CLASS = "javax.persistence.jdbc.driver";
	private final static String SERVER_DB_DRIVER_CLASS_VALUE = "org.apache.derby.jdbc.ClientDriver";
	private final static String MEM_DB_DRIVER_CLASS_VALUE = "org.apache.derby.jdbc.EmbeddedDriver";

	private final static String DB_URL = "javax.persistence.jdbc.url";
	private final static String MEM_DB_URL_VALUE = "jdbc:derby:memory:sopeco-jpa;create=true";

	private final static String DDL_GENERATION = "eclipselink.ddl-generation";

	private static Logger logger = LoggerFactory.getLogger(PersistenceProviderFactory.class);

	private static IPersistenceProvider persistenceProviderInstance = null;
	private static IMetaDataPersistenceProvider metaDataPersistenceProviderInstance = null;

	public static IPersistenceProvider getPersistenceProvider() {

		if (persistenceProviderInstance == null) {
			logger.debug("Creating new peristence provider instance.");
			persistenceProviderInstance = createJPAPersistenceProvider();
		}

		return persistenceProviderInstance;
	}

	public static IMetaDataPersistenceProvider getMetaDataPersistenceProvider() {
		PersistenceConfiguration persistenceConfig = new PersistenceConfiguration();
		if (!persistenceConfig.getDBType().equals(DBType.Server)) {
			throw new RuntimeException("Meta data cannot be retrieved for DB type 'InMemory' !");
		}
		if (metaDataPersistenceProviderInstance == null) {
			logger.debug("Creating new peristence provider instance for meta data.");
			metaDataPersistenceProviderInstance = createMetaDataPersistenceProvider();
		}

		return metaDataPersistenceProviderInstance;
	}

	private static IMetaDataPersistenceProvider createMetaDataPersistenceProvider() {
		try {

			PersistenceConfiguration persistenceConfig = new PersistenceConfiguration();

			logger.debug("Create EntityManagerFactory for persistence unit {}.", META_DATA_PERSISTENCE_UNIT_VALUE);

			Map<String, Object> configOverrides = getConfigOverridesForMetaData(persistenceConfig);

			EntityManagerFactory factory = Persistence.createEntityManagerFactory(META_DATA_PERSISTENCE_UNIT_VALUE,
					configOverrides);

			return new MetaDataPersistenceProvider(factory);
		} catch (Exception e) {
			throw new IllegalArgumentException("Could not create peristence provider for meta data!", e);
		}
	}

	private static IPersistenceProvider createJPAPersistenceProvider() {

		try {

			PersistenceConfiguration persistenceConfig = new PersistenceConfiguration();

			logger.debug("Create EntityManagerFactory for persistence unit {}.", PERSISTENCE_UNIT_VALUE);
			
			Map<String, Object> configOverrides = getConfigOverrides(persistenceConfig);

			EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_VALUE,
					configOverrides);

			if (persistenceConfig.getDBType().equals(DBType.Server)) {
				try {
					// check whether meta data entry is available
					getMetaDataPersistenceProvider().loadDatabaseInstance(persistenceConfig.getServerUrl());
				} catch (DataNotFoundException dnfe) {
					logger.debug("Creating a new meta data entry for DB Instance {}", persistenceConfig.getServerUrl());
					getMetaDataPersistenceProvider().store(
							new DatabaseInstance(persistenceConfig.getDBName(), persistenceConfig.getServerUrl()));
				}

			}

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

	private static Map<String, Object> getConfigOverridesForMetaData(PersistenceConfiguration persistenceConfig) {
		Map<String, Object> configOverrides = new HashMap<String, Object>();
		configOverrides.put(DB_DRIVER_CLASS, SERVER_DB_DRIVER_CLASS_VALUE);
		configOverrides.put(DB_URL, persistenceConfig.getMetaDataConnectionUrl());
		configOverrides.put(DDL_GENERATION, persistenceConfig.getDDLGenerationStrategy());

		return configOverrides;
	}

}

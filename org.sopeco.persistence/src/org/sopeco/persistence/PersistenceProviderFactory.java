package org.sopeco.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.config.Configuration;
import org.sopeco.persistence.config.PersistenceConfiguration;
import org.sopeco.persistence.config.PersistenceConfiguration.DBType;
import org.sopeco.persistence.exceptions.DataNotFoundException;
import org.sopeco.persistence.exceptions.WrongCredentialsException;
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
	private static Logger logger = LoggerFactory.getLogger(PersistenceProviderFactory.class);

	private static PersistenceProviderFactory instance;

	// JPA Provider constants
	private static final String PERSISTENCE_UNIT_VALUE = "sopeco";
	private static final String META_DATA_PERSISTENCE_UNIT_VALUE = "sopeco_metadata";
	private static final String DB_DRIVER_CLASS = "javax.persistence.jdbc.driver";
	private static final String SERVER_DB_DRIVER_CLASS_VALUE = "org.apache.derby.jdbc.ClientDriver";
	private static final String MEM_DB_DRIVER_CLASS_VALUE = "org.apache.derby.jdbc.EmbeddedDriver";

	private static final String DB_URL = "javax.persistence.jdbc.url";
	private static final String MEM_DB_URL_VALUE = "jdbc:derby:memory:sopeco-jpa;create=true";

	private static final String DDL_GENERATION = "eclipselink.ddl-generation";
	private static final String JAVAX_PERSISTENCE_USER = "javax.persistence.jdbc.user";
	private static final String JAVAX_PERSISTENCE_PASSWORD = "javax.persistence.jdbc.password";
	private static final String JAVAX_PERSISTENCE_USER_DEFAULT = "app";
	private static final String JAVAX_PERSISTENCE_PASSWORD_DEFAULT = "app";

	/**
	 * Returns the singleton instance of the persistence provider factory.
	 * 
	 * @return Returns the singleton instance of the persistence provider
	 *         factory.
	 */
	public static PersistenceProviderFactory getInstance() {
		if (instance == null) {
			instance = new PersistenceProviderFactory();
		}
		return instance;
	}

	private Map<String, IPersistenceProvider> persistenceProviderInstances;
	private IMetaDataPersistenceProvider metaDataPersistenceProvider;
	/**
	 * Private Constructor for singleton instance.
	 */
	protected PersistenceProviderFactory() {

	}

	/**
	 * Returns the session-singleton instance of the persistence provider for
	 * the session specified by the passed sessionId.
	 * 
	 * @param sessionId
	 *            id specifying the session for which the persistence provider
	 *            should be returned.
	 * @return Returns the session-singleton instance of the persistence
	 *         provider.
	 */
	public IPersistenceProvider getPersistenceProvider(String sessionId) {
		if (persistenceProviderInstances == null) {
			persistenceProviderInstances = new HashMap<String, IPersistenceProvider>();
		}
		if (persistenceProviderInstances.get(sessionId) == null) {
			logger.debug("Creating new peristence provider instance for session {}.", sessionId);
			persistenceProviderInstances.put(sessionId, createJPAPersistenceProvider(sessionId));
		}

		return persistenceProviderInstances.get(sessionId);
	}

	/**
	 * Returns the session-singleton instance of the meta-data persistence
	 * provider for the session specified by the passed sessionId.
	 * 
	 * @param sessionId
	 *            id specifying the session for which the persistence provider
	 *            should be returned.
	 * @return Returns the session-singleton instance of the meta-data
	 *         persistence provider.
	 */
	public IMetaDataPersistenceProvider getMetaDataPersistenceProvider() {
		PersistenceConfiguration persistenceConfig = PersistenceConfiguration.getSessionUnrelatedSingleton();
		if (!persistenceConfig.getDBType().equals(DBType.Server)) {
			throw new RuntimeException("Meta data cannot be retrieved for DB type 'InMemory' !");
		}


		if (metaDataPersistenceProvider == null) {
			logger.debug("Creating new meta-data peristence provider.");
			metaDataPersistenceProvider = createMetaDataPersistenceProvider();
		}

		return metaDataPersistenceProvider;
	}

	private IMetaDataPersistenceProvider createMetaDataPersistenceProvider() {
		try {

			PersistenceConfiguration persistenceConfig = PersistenceConfiguration.getSessionUnrelatedSingleton();

			logger.debug("Create EntityManagerFactory for persistence unit {}.", META_DATA_PERSISTENCE_UNIT_VALUE);

			Map<String, Object> configOverrides = getConfigOverridesForMetaData(persistenceConfig);

			EntityManagerFactory factory = Persistence.createEntityManagerFactory(META_DATA_PERSISTENCE_UNIT_VALUE,
					configOverrides);

			return new MetaDataPersistenceProvider(Configuration.getGlobalSessionId(), factory);
		} catch (Exception e) {
			throw new IllegalArgumentException("Could not create peristence provider for meta data!", e);
		}
	}

	protected IPersistenceProvider createJPAPersistenceProvider(String sessionId) {

		try {

			PersistenceConfiguration persistenceConfig = PersistenceConfiguration.getSessionSingleton(sessionId);

			if (persistenceConfig.isPasswordUsed()) {
				enableAuthentication(persistenceConfig);
			}

			logger.debug("Create EntityManagerFactory for persistence unit {}.", PERSISTENCE_UNIT_VALUE);

			Map<String, Object> configOverrides = getConfigOverrides(persistenceConfig);

			EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_VALUE,
					configOverrides);

			if (persistenceConfig.getDBType().equals(DBType.Server)) {
				DatabaseInstance dbInstance = new DatabaseInstance(persistenceConfig.getDBName(),
						persistenceConfig.getDBHost(), persistenceConfig.getDBPort(),
						persistenceConfig.isPasswordUsed());
				try {
					// check whether meta data entry is available
					getMetaDataPersistenceProvider().loadDatabaseInstance(dbInstance.getId());
				} catch (DataNotFoundException dnfe) {
					// entry is not available create a new one
					logger.debug("Creating a new meta data entry for DB Instance {}", dbInstance.getId());
					getMetaDataPersistenceProvider().store(dbInstance);
				} catch (PersistenceException persistenceException) {
					// if meta database is not available show a warning,
					// otherwise propagate the thrown exception
					boolean connectionException = false;
					Throwable exception = persistenceException;
					while (exception.getCause() != null) {
						if (exception.getCause() instanceof SQLNonTransientConnectionException) {
							connectionException = true;
							break;
						}
						exception = exception.getCause();
					}
					if (connectionException) {
						logger.warn("Couldn't store meta information about used database. Meta database is not available! "
								+ "Check the settings for the meta database connection and the availability of the meta database server!");
					} else {
						throw new RuntimeException("PersistenceException!", persistenceException);
					}

				}

			}

			return new JPAPersistenceProvider(sessionId, factory);
		} catch (SQLNonTransientConnectionException sqlException) {
			throw new WrongCredentialsException("Could not connect to database. User name or password are wrong!");
		} catch (Exception e) {
			throw new IllegalArgumentException("Could not create peristence provider!", e);
		}
	}

	private Map<String, Object> getConfigOverrides(PersistenceConfiguration persistenceConfig) {
		Map<String, Object> configOverrides = new HashMap<String, Object>();

		switch (persistenceConfig.getDBType()) {
		case InMemory:
			configOverrides.put(DB_DRIVER_CLASS, MEM_DB_DRIVER_CLASS_VALUE);
			configOverrides.put(DB_URL, MEM_DB_URL_VALUE);
			break;

		case Server:
			configOverrides.put(DB_DRIVER_CLASS, SERVER_DB_DRIVER_CLASS_VALUE);
			configOverrides.put(DB_URL, persistenceConfig.getServerUrl());
			if (persistenceConfig.isPasswordUsed()) {
				configOverrides.put(JAVAX_PERSISTENCE_USER, persistenceConfig.getDBName());
				configOverrides.put(JAVAX_PERSISTENCE_PASSWORD, persistenceConfig.getPassword());
			} else {
				configOverrides.put(JAVAX_PERSISTENCE_USER, JAVAX_PERSISTENCE_USER_DEFAULT);
				configOverrides.put(JAVAX_PERSISTENCE_PASSWORD, JAVAX_PERSISTENCE_PASSWORD_DEFAULT);
			}
			break;

		default:
			break;
		}

		configOverrides.put(DDL_GENERATION, persistenceConfig.getDDLGenerationStrategy());

		return configOverrides;
	}

	private Map<String, Object> getConfigOverridesForMetaData(PersistenceConfiguration persistenceConfig) {
		Map<String, Object> configOverrides = new HashMap<String, Object>();
		configOverrides.put(DB_DRIVER_CLASS, SERVER_DB_DRIVER_CLASS_VALUE);
		configOverrides.put(DB_URL, persistenceConfig.getMetaDataConnectionUrl());
		configOverrides.put(DDL_GENERATION, persistenceConfig.getDDLGenerationStrategyForMetaData());

		return configOverrides;
	}

	private void enableAuthentication(PersistenceConfiguration persistenceConfig) throws SQLException {

		try {
			Class.forName(SERVER_DB_DRIVER_CLASS_VALUE);
		} catch (ClassNotFoundException e1) {
			throw new RuntimeException(e1);
		}

		Connection conn = DriverManager.getConnection(persistenceConfig.getServerUrl(), persistenceConfig.getDBName(),
				persistenceConfig.getPassword());

		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery("VALUES SYSCS_UTIL.SYSCS_GET_DATABASE_PROPERTY("
				+ "'derby.database.fullAccessUsers')");
		rs.next();
		String fullAccessUsers = rs.getString(1);

		s.close();

		if (fullAccessUsers == null || !fullAccessUsers.contains(persistenceConfig.getDBName())) {
			s = conn.createStatement();

			s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY("
					+ "'derby.connection.requireAuthentication', 'true')");

			s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY("
					+ "'derby.authentication.provider', 'BUILTIN')");

			s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(" + "'derby.user."
					+ persistenceConfig.getDBName() + "', '" + persistenceConfig.getPassword() + "')");

			s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY("
					+ "'derby.database.defaultConnectionMode', 'noAccess')");

			s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(" + "'derby.database.fullAccessUsers', '"
					+ persistenceConfig.getDBName() + "')");

			s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY("
					+ "'derby.database.propertiesOnly', 'false')");
			s.close();
			conn.close();
			try {
				// database shutdown throws always an exception (see derby
				// description...)
				DriverManager.getConnection(persistenceConfig.getServerUrlWithShutdown(),
						persistenceConfig.getDBName(), persistenceConfig.getPassword());
			} catch (SQLNonTransientConnectionException e) {
				logger.debug("Created user for database!");
			}

		} else {
			conn.close();
		}

	}

}

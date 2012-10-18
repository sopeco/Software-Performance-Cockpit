package org.sopeco.persistence.config;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.config.Configuration;
import org.sopeco.config.IConfiguration;
import org.sopeco.config.exception.ConfigurationException;
import org.sopeco.util.session.SessionAwareObject;

/**
 * This class holds the configuration keys and default values. Moreover, it
 * provides access to the configuration given to SoPeCo.
 * 
 * @author Dennis Westermann
 * 
 */
public final class PersistenceConfiguration extends SessionAwareObject {

	/**
	 * Enumeration for different database types.
	 * 
	 * @author Alexander Wert
	 * 
	 */
	public enum DBType {
		/**
		 * An in memory database is used.
		 */
		InMemory,
		/**
		 * An database server is used.
		 */
		Server
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(PersistenceConfiguration.class);
	private static final String DEFAULT_PERSISTENCE_CONFIG_FILE_NAME = "sopeco-persistence-defaults.conf";
	private static final String DDL_GENERATION = "sopeco.config.persistence.ddlgeneration";

	private static final String DDL_GENERATION_META_DATA = "sopeco.config.persistence.meta.ddlgeneration";

	private static final String META_DATA_DB = "sopeco.config.persistence.metaServer.dbName";
	private static final String META_DATA_HOST = "sopeco.config.persistence.metaServer.host";
	private static final String META_DATA_PORT = "sopeco.config.persistence.metaServer.port";
	private static final String SERVER_HOST = "sopeco.config.persistence.server.host";
	private static final String SERVER_PORT = "sopeco.config.persistence.server.port";
	private static final String DATABASE_NAME = "sopeco.config.persistence.server.dbname";
	private static final String DB_PASSWORD_USED = "sopeco.config.persistence.server.protectedByPassword";
	private static final String DB_PASSWORD = "sopeco.config.persistence.server.password";

	private static final String SERVER_URL_PREFIX = "jdbc:derby://";
	private static final String SERVER_URL_SUFFIX = ";create=true";
	private static final String SERVER_URL_SHUTDOWN_SUFFIX = ";shutdown=true";

	private static final String DB_TYPE = "sopeco.config.persistence.dbtype";

	private static Map<String, PersistenceConfiguration> sessionSingletonInstances;

	/**
	 * Returns the session-singleton instance of the persistence configuration
	 * for the session specified by the given sessionId.
	 * 
	 * @param sessionId
	 *            Id specifying the session for which the persistence
	 *            configuration should be returned.
	 * @return Returns the session-singleton instance of the persistence
	 *         configuration.
	 */
	public static PersistenceConfiguration getSessionSingleton(String sessionId) {
		if (sessionSingletonInstances == null) {
			sessionSingletonInstances = new HashMap<String, PersistenceConfiguration>();
		}

		if (sessionSingletonInstances.get(sessionId) == null) {
			sessionSingletonInstances.put(sessionId, new PersistenceConfiguration(sessionId));
		}

		return sessionSingletonInstances.get(sessionId);
	}

	/**
	 * Returns the session-unrelated-singleton instance of the persistence
	 * configuration.
	 * 
	 * 
	 * @return Returns the session-unrelated-singleton instance of the
	 *         persistence configuration.
	 */
	public static PersistenceConfiguration getSessionUnrelatedSingleton() {
		return getSessionSingleton(Configuration.getGlobalSessionId());
	}

	private IConfiguration sopecoConfig;

	private PersistenceConfiguration(String sessionId) {
		super(sessionId);
		sopecoConfig = Configuration.getSessionSingleton(sessionId);

		try {
			sopecoConfig.loadDefaultConfiguration(this.getClass().getClassLoader(),
					DEFAULT_PERSISTENCE_CONFIG_FILE_NAME);
		} catch (ConfigurationException e) {
			LOGGER.error("Unable to read default config.");
			throw new RuntimeException(e);
		}

	}

	/**
	 * Returns the database name.
	 * 
	 * @return Returns the database name.
	 */
	public String getDBName() {
		return sopecoConfig.getPropertyAsStr(DATABASE_NAME);
	}

	/**
	 * Updates the database name.
	 * 
	 * @param dbName
	 *            the new database name
	 */
	public void updateDBName(String dbName) {
		sopecoConfig.setProperty(DATABASE_NAME, dbName);
	}

	/**
	 * Returns the host name.
	 * 
	 * @return Returns the hsot name.
	 */
	public String getDBHost() {
		return sopecoConfig.getPropertyAsStr(SERVER_HOST);
	}

	/**
	 * Updates the host name.
	 * 
	 * @param dbHost
	 *            the new host name
	 */
	public void updateDBHost(String dbHost) {
		sopecoConfig.setProperty(SERVER_HOST, dbHost);
	}

	/**
	 * Returns the database port.
	 * 
	 * @return Returns the database port.
	 */
	public String getDBPort() {
		return sopecoConfig.getPropertyAsStr(SERVER_PORT);
	}

	/**
	 * Updates the database port.
	 * 
	 * @param dbPort
	 *            the new port
	 */
	public void updateDBPort(String dbPort) {
		sopecoConfig.setProperty(SERVER_PORT, dbPort);
	}

	/**
	 * Updates the database password.
	 * 
	 * @param password
	 *            the new password
	 */
	public void updateDBPassword(String password) {
		sopecoConfig.setProperty(DB_PASSWORD, password);
	}

	/**
	 * Returns the password for the database.
	 * 
	 * @return Returns the password for the database.
	 */
	public String getPassword() {
		return sopecoConfig.getPropertyAsStr(DB_PASSWORD);
	}

	/**
	 * sets whether a password should be used or not.
	 * 
	 * @param use
	 *            use password or not.
	 */
	public void setUsePassword(boolean use) {
		sopecoConfig.setProperty(DB_PASSWORD_USED, Boolean.toString(use));
	}

	/**
	 * Indicates whether a password should be used.
	 * 
	 * @return Returns true, if password is used.
	 */
	public boolean isPasswordUsed() {
		return sopecoConfig.getPropertyAsBoolean(DB_PASSWORD_USED, false);
	}

	/**
	 * @return the type of database to be used
	 */
	public DBType getDBType() {
		String value = (String) sopecoConfig.getProperty(DB_TYPE);
		if (value.equalsIgnoreCase(DBType.InMemory.name())) {
			return DBType.InMemory;
		} else if (value.equalsIgnoreCase(DBType.Server.name())) {
			return DBType.Server;
		}

		throw new IllegalArgumentException("Illegal value for property sopeco.config.persistence.dbtype: " + value);
	}

	/**
	 * @return the strategy for the ddl generation
	 */
	public String getDDLGenerationStrategy() {
		String value = (String) sopecoConfig.getProperty(DDL_GENERATION);
		if (value.equals("create-tables") || value.equals("drop-and-create-tables")) {
			return value;
		}

		throw new IllegalArgumentException("Illegal value for property sopeco.config.persistence.ddlgeneration: "
				+ value);
	}

	/**
	 * @return the url of the database server
	 */
	public String getServerUrl() {
		return SERVER_URL_PREFIX + sopecoConfig.getPropertyAsStr(SERVER_HOST) + ":"
				+ sopecoConfig.getPropertyAsStr(SERVER_PORT) + "/" + sopecoConfig.getPropertyAsStr(DATABASE_NAME)
				+ SERVER_URL_SUFFIX;
	}

	/**
	 * Returns the connection url to the database which shuts down the database.
	 * 
	 * @return Returns the connection url to the database which shuts down the
	 *         database.
	 */
	public String getServerUrlWithShutdown() {

		return SERVER_URL_PREFIX + sopecoConfig.getPropertyAsStr(SERVER_HOST) + ":"
				+ sopecoConfig.getPropertyAsStr(SERVER_PORT) + "/" + sopecoConfig.getPropertyAsStr(DATABASE_NAME)
				+ SERVER_URL_SHUTDOWN_SUFFIX;
	}

	/**
	 * @return the url of the meta data database
	 */
	public String getMetaDataConnectionUrl() {
		return SERVER_URL_PREFIX + sopecoConfig.getPropertyAsStr(META_DATA_HOST) + ":"
				+ sopecoConfig.getPropertyAsStr(META_DATA_PORT) + "/" + sopecoConfig.getPropertyAsStr(META_DATA_DB)
				+ SERVER_URL_SUFFIX;
	}

	/**
	 * Returns the host name of the meta-data database.
	 * 
	 * @return Returns the host name of the meta-data database.
	 */
	public String getMetaDataHost() {
		return sopecoConfig.getPropertyAsStr(META_DATA_HOST);
	}

	/**
	 * Returns the port of the meta-data database.
	 * 
	 * @return Returns the port of the meta-data database.
	 */
	public String getMetaDataPort() {
		return sopecoConfig.getPropertyAsStr(META_DATA_PORT);
	}

	/**
	 * Returns the name of the meta-data database.
	 * 
	 * @return Returns the name of the meta-data database.
	 */
	public String getMetaDataDBName() {
		return sopecoConfig.getPropertyAsStr(META_DATA_DB);
	}

	/**
	 * 
	 * @return Returns the standard ddl generation strategy for meta data
	 */
	public String getDDLGenerationStrategyForMetaData() {
		return sopecoConfig.getPropertyAsStr(DDL_GENERATION_META_DATA);
	}

}

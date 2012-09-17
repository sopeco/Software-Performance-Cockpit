package org.sopeco.persistence.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.config.Configuration;
import org.sopeco.config.IConfiguration;
import org.sopeco.config.exception.ConfigurationException;

/**
 * This class holds the configuration keys and default values. Moreover, it
 * provides access to the configuration given to SoPeCo.
 * 
 * @author Dennis Westermann
 * 
 */
public class PersistenceConfiguration {
	private Logger logger = LoggerFactory.getLogger(PersistenceConfiguration.class);
	public enum DBType {
		InMemory, Server
	}

	private final static String DB_TYPE = "sopeco.config.persistence.dbtype";

	private static PersistenceConfiguration singletonInstance;
	
	
	public static PersistenceConfiguration getSingleton(){
		if(singletonInstance == null){
			singletonInstance = new PersistenceConfiguration();
		}
		
		return singletonInstance;
	}
	
	private IConfiguration sopecoConfig;
	

	public final static String DEFAULT_PERSISTENCE_CONFIG_FILE_NAME = "sopeco-persistence-defaults.conf";
	public final static String DDL_GENERATION = "sopeco.config.persistence.ddlgeneration";

	public final static String META_DATA_DB = "sopeco.config.persistence.metaServer.dbName";
	public final static String META_DATA_HOST = "sopeco.config.persistence.metaServer.host";
	public final static String META_DATA_PORT = "sopeco.config.persistence.metaServer.port";
	public final static String SERVER_HOST = "sopeco.config.persistence.server.host";
	public final static String SERVER_PORT = "sopeco.config.persistence.server.port";
	public final static String DATABASE_NAME = "sopeco.config.persistence.server.dbname";
	public final static String DB_PASSWORD_USED = "sopeco.config.persistence.server.protectedByPassword";
	public final static String DB_PASSWORD = "sopeco.config.persistence.server.password";

	public final static String SERVER_URL_PREFIX = "jdbc:derby://";
	public final static String SERVER_URL_SUFFIX = ";create=true";
	public final static String SERVER_URL_SHUTDOWN_SUFFIX = ";shutdown=true";
	private PersistenceConfiguration() {
		sopecoConfig = Configuration.getSingleton();

		try {
			sopecoConfig.loadDefaultConfiguration(this.getClass().getClassLoader(),
					DEFAULT_PERSISTENCE_CONFIG_FILE_NAME);
		} catch (ConfigurationException e) {
			logger.error("Unable to read default config.");
			throw new RuntimeException(e);
		}

	}

	public String getDBName() {
		return sopecoConfig.getPropertyAsStr(DATABASE_NAME);
	}
	
	public void updateDBName(String dbName) {
		 sopecoConfig.setProperty(DATABASE_NAME, dbName);
	}

	public String getDBHost() {
		return sopecoConfig.getPropertyAsStr(SERVER_HOST);
	}
	
	public void updateDBHost(String dbName) {
		 sopecoConfig.setProperty(SERVER_HOST, dbName);
	}

	public String getDBPort() {
		return sopecoConfig.getPropertyAsStr(SERVER_PORT);
	}
	
	public void updateDBPort(String dbPort) {
		 sopecoConfig.setProperty(SERVER_PORT, dbPort);
	}
	
	public void updateDBPassword(String password) {
		 sopecoConfig.setProperty(DB_PASSWORD, password);
	}
	
	public String getPassword(){
		return sopecoConfig.getPropertyAsStr(DB_PASSWORD);
	}
	
	public void setUsePassword(boolean use){
		 sopecoConfig.setProperty(DB_PASSWORD_USED, Boolean.toString(use));
	}
	
	public boolean isPasswordUsed(){
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
//		String passwordSuffix = "";
//		if (sopecoConfig.getPropertyAsBoolean(DB_PASSWORD_USED, false)) {
//			passwordSuffix = ";user=" + sopecoConfig.getPropertyAsStr(DATABASE_NAME) + ";password="
//					+ sopecoConfig.getPropertyAsStr(DB_PASSWORD);
//		}

		return SERVER_URL_PREFIX + sopecoConfig.getPropertyAsStr(SERVER_HOST) + ":"
				+ sopecoConfig.getPropertyAsStr(SERVER_PORT) + "/" + sopecoConfig.getPropertyAsStr(DATABASE_NAME)
				+ SERVER_URL_SUFFIX;
	}

	public String getServerUrlWithShutdown() {
//		String passwordSuffix = "";
//		if (sopecoConfig.getPropertyAsBoolean(DB_PASSWORD_USED, false)) {
//			passwordSuffix = ";user=" + sopecoConfig.getPropertyAsStr(DATABASE_NAME) + ";password="
//					+ sopecoConfig.getPropertyAsStr(DB_PASSWORD);
//		}

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

	public String getMetaDataHost() {
		return sopecoConfig.getPropertyAsStr(META_DATA_HOST);
	}

	public String getMetaDataPort() {
		return sopecoConfig.getPropertyAsStr(META_DATA_PORT);
	}

	public String getMetaDataDBName() {
		return sopecoConfig.getPropertyAsStr(META_DATA_DB);
	}

}

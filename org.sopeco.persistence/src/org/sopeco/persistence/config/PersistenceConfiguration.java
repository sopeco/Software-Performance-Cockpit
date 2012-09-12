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
	private IConfiguration sopecoConfig;
	
	private final static String DB_TYPE = "sopeco.config.persistence.dbtype";
	public enum DBType {InMemory, Server}

	private final static String DEFAULT_PERSISTENCE_CONFIG_FILE_NAME ="sopeco-persistence-defaults.conf";
	private final static String DDL_GENERATION = "sopeco.config.persistence.ddlgeneration";
	
	public final static String META_DATA_DB = "sopeco.config.persistence.metaServer.dbName";
	public final static String META_DATA_HOST = "sopeco.config.persistence.metaServer.host";
	public final static String META_DATA_PORT = "sopeco.config.persistence.metaServer.port";
	private final static String SERVER_HOST = "sopeco.config.persistence.server.host";
	private final static String SERVER_PORT = "sopeco.config.persistence.server.port";
	private final static String DATABASE_NAME = "sopeco.config.persistence.server.dbname";
	private final static String SERVER_URL_PREFIX = "jdbc:derby://";
	private final static String SERVER_URL_SUFFIX = ";create=true";
	
	public PersistenceConfiguration(){	
		sopecoConfig = Configuration.getSingleton();
		
		try {
			sopecoConfig.loadDefaultConfiguration(this.getClass().getClassLoader(), DEFAULT_PERSISTENCE_CONFIG_FILE_NAME);
		} catch (ConfigurationException e) {
			logger.error("Unable to read default config.");
			throw new RuntimeException(e);
		}
		
		
		
	}
	
	public String getDBName(){
		return sopecoConfig.getPropertyAsStr(DATABASE_NAME);
	}
	
	public String getDBHost(){
		return sopecoConfig.getPropertyAsStr(SERVER_HOST);
	}
	
	public String getDBPort(){
		return sopecoConfig.getPropertyAsStr(SERVER_PORT);
	}
	
	/**
	 * @return the type of database to be used
	 */
	public DBType getDBType(){
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
	public String getDDLGenerationStrategy(){
		String value = (String) sopecoConfig.getProperty(DDL_GENERATION);	
		if (value.equals("create-tables") || value.equals("drop-and-create-tables")) {
			return value;
		} 
		
		throw new IllegalArgumentException("Illegal value for property sopeco.config.persistence.ddlgeneration: " + value);
	}
	
	
	/**
	 * @return the url of the database server
	 */
	public String getServerUrl() {
		return SERVER_URL_PREFIX + sopecoConfig.getPropertyAsStr(SERVER_HOST) 
				+ ":" + sopecoConfig.getPropertyAsStr(SERVER_PORT) + "/" + sopecoConfig.getPropertyAsStr(DATABASE_NAME) + SERVER_URL_SUFFIX;
	}
	
	/**
	 * @return the url of the meta data database
	 */
	public String getMetaDataConnectionUrl(){
		return SERVER_URL_PREFIX + sopecoConfig.getPropertyAsStr(META_DATA_HOST) 
				+ ":" + sopecoConfig.getPropertyAsStr(META_DATA_PORT) + "/" + sopecoConfig.getPropertyAsStr(META_DATA_DB) + SERVER_URL_SUFFIX;
	}
	
	public String getMetaDataHost(){
		return sopecoConfig.getPropertyAsStr(META_DATA_HOST);
	}
	
	public String getMetaDataPort(){
		return sopecoConfig.getPropertyAsStr(META_DATA_PORT);
	}
	
	public String getMetaDataDBName(){
		return sopecoConfig.getPropertyAsStr(META_DATA_DB);
	}
	
	
}

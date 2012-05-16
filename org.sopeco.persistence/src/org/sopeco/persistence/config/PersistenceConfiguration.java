package org.sopeco.persistence.config;

import java.io.File;

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
	
	private IConfiguration sopecoConfig;
	
	private final static String DB_TYPE = "sopeco.config.persistence.dbtype";
	public enum DBType {InMemory, Server}

	private final static String DDL_GENERATION = "sopeco.config.persistence.ddlgeneration";
	
	private final static String SERVER_HOST = "sopeco.config.persistence.server.host";
	private final static String SERVER_PORT = "sopeco.config.persistence.server.port";
	private final static String SERVER_URL_PREFIX = "jdbc:derby://";
	private final static String SERVER_URL_SUFFIX = "/sopeco-jpa;create=true";
	
	public PersistenceConfiguration(){	
		sopecoConfig = Configuration.getSingleton();
		try {
			sopecoConfig.loadDefaultConfiguration(this.getClass().getClassLoader(), "config" + File.separator + "sopeco-persistence-defaults.conf");
		} catch (ConfigurationException e) {
			throw new IllegalStateException("Cannot load default configuration");
		}
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
				+ ":" + sopecoConfig.getPropertyAsStr(SERVER_PORT) + SERVER_URL_SUFFIX;
	}
}

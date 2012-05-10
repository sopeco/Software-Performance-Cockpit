/**
 * 
 */
package org.sopeco.config;

import java.net.URI;

import org.sopeco.config.exception.ConfigurationException;

/**
 * Interface to the SoPeCo global configuration component.
 * 
 * @author Roozbeh Farahbod
 *
 */
public interface IConfiguration {

	public static final String CONF_LOGGER_CONFIG_FILE_NAME = "sopeco.config.loggerConfigFileName";
	public static final String CONF_SCENARIO_DESCRIPTION_FILE_NAME = "sopeco.config.measurementSpecFileName";
	public static final String CONF_SCENARIO_DESCRIPTION = "sopeco.config.measurementSpecification";
	public static final String CONF_MEASUREMENT_CONTROLLER_URI = "sopeco.config.measurementControllerURI";
	public static final String CONF_MEASUREMENT_CONTROLLER_CLASS_NAME = "sopeco.config.measurementControllerClassName";
	public static final String CONF_APP_NAME = "sopeco.config.applicationName";
	
	/** Holds the path to the root folder of SoPeCo. */ 
	public static final String CONF_APP_ROOT_FOLDER = "sopeco.config.rootFolder";
	
	/** Holds the path to the plugins folder, relative to the root folder of SoPeCo. */ 
	public static final String CONF_PLUGINS_FOLDER = "sopeco.config.pluginsFolder";

	public static final String CLA_EXTENSION_ID = "org.sopeco.config.commandlinearguments";

	
	/** Folder for configuration files relative to the application root folder */
	public static final String CONFIGURATION_FOLDER = "conf";

	public static final String DEFAULT_CONFIG_FILE_NAME = "sopeco-defaults.conf";
	
	/**
	 * Returns the configured value of the given property in SoPeCo. 
	 * 
	 * It first looks up the current SoPeCo configuration, if there 
	 * is no value defined there, looks up the system properties, 
	 * if no value is defined there, then loads it from the default values; 
	 * in case of no default value, returns null.
	 * 
	 * @param key property key
	 */
	public Object getProperty(String key);

	/**
	 * Returns the configured value of the given property as a String.
	 * 
	 * This method calls the {@link Object#toString()} of the property value
	 * and is for convenience only. If the given property is not set, 
	 * it returns <code>null</code>. 
	 * 
	 * @param key property key
	 * 
	 * @see #getProperty(String)
	 */
	public String getPropertyAsStr(String key);

	/**
	 * Sets the value of a property for the current run.
	 * 
	 * @param key property key
	 * @param value property value
	 */
	public void setProperty(String key, Object value);
	
	/**
	 * Returns the default value (ignoring the current runtime configuration)
	 * for a given property. 
	 * 
	 * @param key porperty key
	 */
	public Object getDefaultValue(String key);

	/**
	 * Processes the given command line arguments, the effects of which will
	 * reflect in the global property values.
	 * 
	 * @param args command line arguments
	 * @throws ConfigurationException if there is any problem with command line arguments
	 */
	public void processCommandLineArguments(String[] args) throws ConfigurationException;

	/**
	 * Sets the value of scenario description file name.
	 *  
	 * @param fileName file name
	 * @see #CONF_SCENARIO_DESCRIPTION_FILE_NAME
	 */
	public void setScenarioDescriptionFileName(String fileName);
	
	/**
	 * Sets the sceanrio description as the given object. This 
	 * property in effect overrides the value of 
	 * scenario description file name ({@link IConfiguration#CONF_SCENARIO_DESCRIPTION_FILE_NAME}).
	 * 
	 * @param sceanrioDescription an instance of a scenario description
	 * @see #CONF_SCENARIO_DESCRIPTION
	 */
	public void setScenarioDescription(Object sceanrioDescription);
	
	/**
	 * Sets the measurement controller URI. 
	 * 
	 * @param uri a URI as an String
	 * @throws ConfigurationException 
	 * @see #CONF_MEASUREMENT_CONTROLLER_URI
	 */
	public void setMeasurementControllerURI(String uriStr) throws ConfigurationException;
	
	/**
	 * Sets the measurement controller class name. This also 
	 * sets the measurement controller URI to be '<code>class://[CLASS_NAME]</code>'.
	 * 
	 * @param className the full name of the class
	 * @see #CONF_MEASUREMENT_CONTROLLER_CLASS_NAME
	 */
	public void setMeasurementControllerClassName(String className);
	
	/**
	 * Sets the application name for this executable instance.
	 * 
	 * @param appName an application name
	 */
	public void setApplicationName(String appName);

	/**
	 * Returns the application root directory.
	 */
	public String getAppRootDirectory();
	
	/**
	 * Returns the application's configuration directory.
	 */
	public String getAppConfDirectory();

	/**
	 * Gets the value of scenario description file name.
	 *  
	 * @see #CONF_SCENARIO_DESCRIPTION_FILE_NAME
	 */
	public String getScenarioDescriptionFileName();
	
	/**
	 * Gets the sceanrio description as the given object. 
	 * 
	 * @see #CONF_SCENARIO_DESCRIPTION
	 */
	public Object getScenarioDescription();
	
	/**
	 * Gets the measurement controller URI. 
	 * 
	 * @see #CONF_MEASUREMENT_CONTROLLER_URI
	 */
	public URI getMeasurementControllerURI();
	
	/**
	 * Gets the measurement controller URI as a String. 
	 * 
	 * @see #CONF_MEASUREMENT_CONTROLLER_URI
	 */
	public String getMeasurementControllerURIAsStr();
	
	/**
	 * Gets the measurement controller class name. 
	 * 
	 * @see #CONF_MEASUREMENT_CONTROLLER_CLASS_NAME
	 */
	public String getMeasurementControllerClassName();
	
	/**
	 * Gets the application name for this executable instance.
	 */
	public String getApplicationName();

}

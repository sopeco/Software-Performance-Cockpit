/**
 * 
 */
package org.sopeco.config;

/**
 * Interface to the SoPeCo global configuration component.
 * 
 * @author Roozbeh Farahbod
 *
 */
public interface IConfiguration {

	public static final String CONF_LOGGER_CONFIG_FILE_NAME = "sopeco.config.loggerConfigFileName";
	public static final String CONF_SCENARIO_DESCRIPTION_FILE_NAME = "sopeco.config.measurementSpecFileName";
	public static final String CONF_MEASUREMENT_CONTROLLER_URI = "sopeco.config.measurementControllerURI";
	public static final String CONF_MEASUREMENT_CONTROLLER_CLASS_NAME = "sopeco.config.measurementControllerClassName";
	public static final String CONF_APP_NAME = "sopeco.config.applicationName";
	
	public static final String CONF_PLUGINS_FOLDER = "sopeco.config.pluginsFolder";

	public static final String CLA_EXTENSION_ID = "org.sopeco.config.commandlinearguments";

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
	 */
	public void processCommandLineArguments(String[] args);
	
}

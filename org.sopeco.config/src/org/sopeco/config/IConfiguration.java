/**
 * 
 */
package org.sopeco.config;

import java.io.IOException;
import java.net.URI;

import org.sopeco.config.exception.ConfigurationException;
import org.sopeco.util.session.ISessionAwareObject;

/**
 * Interface to the SoPeCo global configuration component.
 * 
 * @author Roozbeh Farahbod
 * 
 */
public interface IConfiguration extends ISessionAwareObject {

	String ENV_SOPECO_HOME = "SOPECO_HOME";

	String CONF_LOGGER_CONFIG_FILE_NAME = "sopeco.config.loggerConfigFileName";

	String CONF_SCENARIO_DESCRIPTION_FILE_NAME = "sopeco.config.measurementSpecFileName";

	String CONF_SCENARIO_DESCRIPTION = "sopeco.config.measurementSpecification";

	String CONF_MEASUREMENT_CONTROLLER_URI = "sopeco.config.measurementControllerURI";

	String CONF_MEASUREMENT_CONTROLLER_CLASS_NAME = "sopeco.config.measurementControllerClassName";

	String CONF_APP_NAME = "sopeco.config.applicationName";

	String CONF_MAIN_CLASS = "sopeco.config.mainClass";

	String CONF_MEC_ACQUISITION_TIMEOUT = "sopeco.config.MECAcquisitionTimeout";

	String CONF_MODEL_CHANGE_HANDLING_MODE = "sopeco.config.modelChangeHandlingMode";
	String MCH_MODE_OVERWRITE = "overwrite";
	String MCH_MODE_OVERWRITE_KEEP_RESULTS = "overwriteKeepResults";
	String MCH_MODE_FAIL = "fail";
	String MCH_MODE_NEW_VERSION = "newVersion";

	String CONF_SCENARIO_DEFINITION_PACKAGE = "sopeco.config.xml.scenarioDefinitionPackage";
	/** Holds the path to the root folder of SoPeCo. */
	String CONF_APP_ROOT_FOLDER = "sopeco.config.rootFolder";

	/**
	 * Holds the path to the plugins folder, relative to the root folder of
	 * SoPeCo.
	 */
	String CONF_PLUGINS_DIRECTORIES = "sopeco.config.pluginsDirs";

	String CLA_EXTENSION_ID = "org.sopeco.config.commandlinearguments";

	/** Folder for configuration files relative to the application root folder */
	String DEFAULT_CONFIG_FOLDER_NAME = "config";

	String DEFAULT_CONFIG_FILE_NAME = "sopeco-defaults.conf";

	String DIR_SEPARATOR = ":";

	/**
	 * Returns the configured value of the given property in SoPeCo.
	 * 
	 * It first looks up the current SoPeCo configuration, if there is no value
	 * defined there, looks up the system properties, if no value is defined
	 * there, then loads it from the default values; in case of no default
	 * value, returns null.
	 * 
	 * @param key
	 *            property key
	 * @return Returns the configured value of the given property in SoPeCo.
	 */
	Object getProperty(String key);

	/**
	 * Returns the configured value of the given property as a String.
	 * 
	 * This method calls the {@link Object#toString()} of the property value and
	 * is for convenience only. If the given property is not set, it returns
	 * <code>null</code>.
	 * 
	 * @param key
	 *            property key
	 * 
	 * @see #getProperty(String)
	 * @return Returns the configured value of the given property as a String.
	 */
	String getPropertyAsStr(String key);

	/**
	 * Returns the configured value of the given property as a Boolean value.
	 * 
	 * This method uses the {@link #getPropertyAsStr(String)} and interprets
	 * values 'yes' and 'true' (case insensitive) as a Boolean <code>true</code>
	 * value and all other values as <code>false</code>. If the value of the
	 * given property is <code>null</code> it returns the passed default value.
	 * 
	 * @param key
	 *            property key
	 * @param defaultValue
	 *            the default value returned in case of a null property value
	 * 
	 * @return the value of the given property as a boolean
	 * 
	 * @see #getProperty(String)
	 */
	boolean getPropertyAsBoolean(String key, boolean defaultValue);

	/**
	 * Returns the configured value of the given property as a Long value.
	 * 
	 * This method uses the {@link Long.#parseLong(String)} to interpret the
	 * values. If the value of the given property is <code>null</code> it
	 * returns the passed default value.
	 * 
	 * @param key
	 *            property key
	 * @param defaultValue
	 *            the default value returned in case of a null property value
	 * 
	 * @return the value of the given property as a long
	 * 
	 * @see #getProperty(String)
	 */
	long getPropertyAsLong(String key, long defaultValue);

	/**
	 * Returns the configured value of the given property as an Integer value.
	 * 
	 * This method uses the {@link Integer.#parseInt(String)} to interpret the
	 * values. If the value of the given property is <code>null</code> it
	 * returns the passed default value.
	 * 
	 * @param key
	 *            property key
	 * @param defaultValue
	 *            the default value returned in case of a null property value
	 * 
	 * @return the value of the given property as an int
	 * 
	 * @see #getProperty(String)
	 */
	int getPropertyAsInteger(String key, int defaultValue);

	/**
	 * Sets the value of a property for the current run.
	 * 
	 * @param key
	 *            property key
	 * @param value
	 *            property value
	 */
	void setProperty(String key, Object value);

	/**
	 * Returns the default value (ignoring the current runtime configuration)
	 * for a given property.
	 * 
	 * @param key
	 *            porperty key
	 * 
	 * @return Returns the default value for a given property.
	 */
	Object getDefaultValue(String key);

	/**
	 * Processes the given command line arguments, the effects of which will
	 * reflect in the global property values.
	 * 
	 * @param args
	 *            command line arguments
	 * @throws ConfigurationException
	 *             if there is any problem with command line arguments
	 */
	void processCommandLineArguments(String[] args) throws ConfigurationException;

	/**
	 * Loads default configurations from a file name. If the file name is not an
	 * absolute path, the file is searched in the following places:
	 * <ol>
	 * <li>the {@value #DEFAULT_CONFIG_FOLDER_NAME} directory,</li>
	 * <li>current folder,</li>
	 * <li>the {@value #DEFAULT_CONFIG_FOLDER_NAME} directory in classpath,</li>
	 * <li>and finaly the classpath.</li>
	 * </ol>
	 * where classpath is determined by the system class loader. See
	 * {@link #loadDefaultConfiguration(ClassLoader, String)} for loading
	 * default configuration providing a class loader.
	 * 
	 * The configuration is loaded in an incremental fashion; i.e., the loaded
	 * configuration will be added to (and overriding) the existing default
	 * configuration.
	 * <p>
	 * See {@link #getAppRootDirectory()} and {@link #getDefaultValue(String)}.
	 * 
	 * @param fileName
	 *            the name of a properties file
	 * @throws ConfigurationException
	 *             if initializing the configuration fails
	 * 
	 */
	void loadDefaultConfiguration(String fileName) throws ConfigurationException;

	/**
	 * Loads default configurations from a file name. If the file name is not an
	 * absolute path, the file is searched in the following places:
	 * <ol>
	 * <li>the {@value #DEFAULT_CONFIG_FOLDER_NAME} directory,</li>
	 * <li>current folder,</li>
	 * <li>the {@value #DEFAULT_CONFIG_FOLDER_NAME} directory in classpath,</li>
	 * <li>and finaly the classpath.</li>
	 * </ol>
	 * where classpath is determined by the given class loader.
	 * 
	 * The configuration is loaded in an incremental fashion; i.e., the loaded
	 * configuration will be added to (and overriding) the existing default
	 * configuration.
	 * <p>
	 * See {@link #getAppRootDirectory()} and {@link #getDefaultValue(String)}.
	 * 
	 * @param classLoader
	 *            an instance of a class loader
	 * @param fileName
	 *            the name of a properties file
	 * @throws ConfigurationException
	 *             if initializing the configuration fails
	 */
	void loadDefaultConfiguration(ClassLoader classLoader, String fileName) throws ConfigurationException;

	/**
	 * Loads user-level configurations from a file name. If the file name is not
	 * an absolute path, the file is searched in the following places:
	 * <ol>
	 * <li>the {@value #DEFAULT_CONFIG_FOLDER_NAME} directory,</li>
	 * <li>current folder,</li>
	 * <li>the {@value #DEFAULT_CONFIG_FOLDER_NAME} directory in classpath,</li>
	 * <li>and finaly the classpath.</li>
	 * </ol>
	 * where classpath is determined by the system class loader. See
	 * {@link #loadConfiguration(ClassLoader, String)} for loading default
	 * configuration providing a class loader.
	 * 
	 * The configuration is loaded in an incremental fashion; i.e., the loaded
	 * configuration will be added to (and overriding) the existing default
	 * configuration.
	 * <p>
	 * See {@link #getAppRootDirectory()} and {@link #getDefaultValue(String)}.
	 * 
	 * @param fileName
	 *            the name of a properties file
	 * @throws ConfigurationException
	 *             if initializing the configuration fails
	 */
	void loadConfiguration(String fileName) throws ConfigurationException;

	/**
	 * Loads user-level configurations from a file name. If the file name is not
	 * an absolute path, the file is searched in the following places:
	 * <ol>
	 * <li>the {@value #DEFAULT_CONFIG_FOLDER_NAME} directory,</li>
	 * <li>current folder,</li>
	 * <li>the {@value #DEFAULT_CONFIG_FOLDER_NAME} directory in classpath,</li>
	 * <li>and finally the classpath.</li>
	 * </ol>
	 * where classpath is determined by the given class loader.
	 * 
	 * The configuration is loaded in an incremental fashion; i.e., the loaded
	 * configuration will be added to (and overriding) the existing default
	 * configuration.
	 * <p>
	 * See {@link #getAppRootDirectory()} and {@link #getDefaultValue(String)}.
	 * 
	 * @param classLoader
	 *            an instance of a class loader
	 * @param fileName
	 *            the name of a properties file
	 * @throws ConfigurationException
	 *             if initializing the configuration fails
	 */
	void loadConfiguration(ClassLoader classLoader, String fileName) throws ConfigurationException;

	/**
	 * Performs any post processing of configuration settings that may be
	 * required.
	 * 
	 * This method can be called after manually making changes to the
	 * configuration values. It should be called automatically after a call to
	 * {@link IConfiguration#loadConfiguration(String)}.
	 */
	void applyConfiguration();

	/**
	 * Sets the value of scenario description file name.
	 * 
	 * @param fileName
	 *            file name
	 * @see #CONF_SCENARIO_DESCRIPTION_FILE_NAME
	 */
	void setScenarioDescriptionFileName(String fileName);

	/**
	 * Sets the sceanrio description as the given object. This property in
	 * effect overrides the value of scenario description file name (
	 * {@link IConfiguration#CONF_SCENARIO_DESCRIPTION_FILE_NAME}).
	 * 
	 * @param sceanrioDescription
	 *            an instance of a scenario description
	 * @see #CONF_SCENARIO_DESCRIPTION
	 */
	void setScenarioDescription(Object sceanrioDescription);

	/**
	 * Sets the measurement controller URI.
	 * 
	 * @param uriStr
	 *            a URI as an String
	 * @throws ConfigurationException
	 *             if initializing the configuration fails
	 * @see #CONF_MEASUREMENT_CONTROLLER_URI
	 */
	void setMeasurementControllerURI(String uriStr) throws ConfigurationException;

	/**
	 * Sets the measurement controller class name. This also sets the
	 * measurement controller URI to be '<code>class://[CLASS_NAME]</code>'.
	 * 
	 * @param className
	 *            the full name of the class
	 * @see #CONF_MEASUREMENT_CONTROLLER_CLASS_NAME
	 */
	void setMeasurementControllerClassName(String className);

	/**
	 * Sets the application name for this executable instance.
	 * 
	 * @param appName
	 *            an application name
	 */
	void setApplicationName(String appName);

	/**
	 * Sets the main class that runs this thread. This will also be used in
	 * finding the root folder
	 * 
	 * @param mainClass
	 *            class to be set as main class
	 */
	void setMainClass(Class<?> mainClass);

	/**
	 * Sets the logger configuration file name and triggers logger
	 * configuration.
	 * 
	 * @param fileName
	 *            a file name
	 */
	void setLoggerConfigFileName(String fileName);

	/**
	 * @return Returns the application root directory.
	 */
	String getAppRootDirectory();

	/**
	 * Sets the application root directory to the given folder.
	 * 
	 * @param rootDir path to a folder
	 */
	void setAppRootDirectory(String rootDir);
	
	/**
	 * @return Returns the application's configuration directory.
	 */
	String getAppConfDirectory();

	/**
	 * @return Returns the value of scenario description file name.
	 * 
	 * @see #CONF_SCENARIO_DESCRIPTION_FILE_NAME
	 */
	String getScenarioDescriptionFileName();

	/**
	 * @return returns the sceanrio description as the given object.
	 * 
	 * @see #CONF_SCENARIO_DESCRIPTION
	 */
	Object getScenarioDescription();

	/**
	 * @return Returns the measurement controller URI.
	 * 
	 * @see #CONF_MEASUREMENT_CONTROLLER_URI
	 */
	URI getMeasurementControllerURI();

	/**
	 * @return Returns the measurement controller URI as a String.
	 * 
	 * @see #CONF_MEASUREMENT_CONTROLLER_URI
	 */
	String getMeasurementControllerURIAsStr();

	/**
	 * @return Returns the measurement controller class name.
	 * 
	 * @see #CONF_MEASUREMENT_CONTROLLER_CLASS_NAME
	 */
	String getMeasurementControllerClassName();

	/**
	 * @return Returns the application name for this executable instance.
	 */
	String getApplicationName();

	/**
	 * @return Returns the main class that runs this thread. This value must
	 *         have been set by a call to
	 *         {@link IConfiguration#setMainClass(Class)}.
	 */
	Class<?> getMainClass();

	/**
	 * Writes the current configuration values into a file.
	 * 
	 * @param fileName
	 *            the name of the file
	 * @throws IOException
	 *             if exporting the configuration fails
	 */
	void writeConfiguration(String fileName) throws IOException;

}

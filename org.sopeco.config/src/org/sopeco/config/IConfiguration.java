/**
 * Copyright (c) 2013 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/**
 * 
 */
package org.sopeco.config;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

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


	String CONF_MEC_SOCKET_RECONNECT_DELAY = "sopeco.config.mec.reconnectDelay";

	String CONF_HTTP_PROXY_HOST = "sopeco.config.httpProxyHost";
	
	String CONF_HTTP_PROXY_PORT = "sopeco.config.httpProxyPort";

	
	String CONF_DEFINITION_CHANGE_HANDLING_MODE = "sopeco.config.definitionChangeHandlingMode";
	String DCHM_ARCHIVE = "archive";
	String DCHM_DISCARD = "discard";

	String CONF_SCENARIO_DEFINITION_PACKAGE = "sopeco.config.xml.scenarioDefinitionPackage";
	/** Holds the path to the root folder of SoPeCo. */
	String CONF_APP_ROOT_FOLDER = "sopeco.config.rootFolder";
	String CONF_EXPERIMENT_EXECUTION_SELECTION = "sopeco.engine.experimentExecutionSelection";
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
	
	String EXPERIMENT_RUN_ABORT = "org.sopeco.experiment.run.abort";

	/**
	 * Export the configuration as a key-value map. Both, the default ones and the ones in the
	 * system environment are included.
	 * 
	 * @return a key-value representation of the configuration
	 * 
	 * @deprecated Use {@code exportConfiguration()} and {@code exportDefaultConfiguration}.
	 */
	@Deprecated
	Map<String, Object> getProperties();
	
	/**
	 * Exports the configuration as a key-value map. The default configuration and the ones
	 * defined in the system environment are not included. 
	 * 
	 * @return a key-value representation of the configuration
	 */
	Map<String, Object> exportConfiguration();
	
	/**
	 * Exports the default configuration as a key-value map. The actual configuration and the ones
	 * defined in the system environment are not included. 
	 * 
	 * @return a key-value representation of the configuration
	 */
	Map<String, Object> exportDefaultConfiguration();
	
	/**
	 * Imports the configuration as a key-value map.  
	 * 
	 * @param config a key-value map representation of the configuration
	 */
	void importConfiguration(Map<String, Object> config);
	
	/**
	 * Imports the default configuration as a key-value map.  
	 * 
	 * @param config a key-value map representation of the configuration
	 */
	void importDefaultConfiguration(Map<String, Object> config);
	
	/**
	 * Overwrites the configuration with the given configuration.  
	 * 
	 * @param config a key-value map representation of the configuration
	 */
	void overwriteConfiguration(Map<String, Object> config);
	
	/**
	 * Overwrites the default configuration with the given configuration.  
	 * 
	 * @param config a key-value map representation of the default configuration
	 */
	void overwriteDefaultConfiguration(Map<String, Object> config);
	
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
	 * Returns the configured value of the given property as a Double value.
	 * 
	 * This method uses the {@link Double.#parseLong(String)} to interpret the
	 * values. If the value of the given property is <code>null</code> it
	 * returns the passed default value.
	 * 
	 * @param key
	 *            property key
	 * @param defaultValue
	 *            the default value returned in case of a null property value
	 * 
	 * @return the value of the given property as a double
	 * 
	 * @see #getProperty(String)
	 */
	double getPropertyAsDouble(String key, double defaultValue);

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
	 * Clears the value of the given property in all layers of configuration,
	 * including the system property environment.
	 * 
	 * @param key the property
	 */
	void clearProperty(String key);

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
	 * @param rootDir
	 *            path to a folder
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

	/**
	 * Overrides the values of this configuration with those of the given
	 * configuration.
	 * 
	 * @param configuration
	 *            with the new values
	 */
	 void overwrite(IConfiguration configuration);

	 /**
	  * Adds a new command-line extension to the configuration component. 
	  * 
	  * The same extension will not be added twice. 
	  * 
	  * @param extension a command-line extension
	  */
	 void addCommandLineExtension(ICommandLineArgumentsExtension extension);
	 
	 /**
	  * Removes a new command-line extension from the configuration component. 
	  * 
	  * @param extension a command-line extension
	  */
	 void removeCommandLineExtension(ICommandLineArgumentsExtension extension);
}

package org.sopeco.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.config.exception.ConfigurationException;
import org.sopeco.util.Tools;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;


/**
 * This class is the access point to all SoPeCo global configuration. 
 * 
 * @author Roozbeh Farahbod
 *
 */
public class Configuration implements IConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(Configuration.class);

	/** Holds the singleton reference to this class. */
	private static IConfiguration SINGLETON = null;

	private List<ICommandLineArgumentsExtension> extensions = null;
	
	/** Holds the default property values. */
	private Map<String, Object> defaultValues = new HashMap<String, Object>();
	
	/** Holds the configured property values. */
	private Map<String, Object> properties = new HashMap<String, Object>();

	/*
	 * Preventing the instantiation of the class by other classes.
	 */
	private Configuration(Class<?> mainClass) {
		try {
			setDefaultValues(mainClass);
		} catch (ConfigurationException e) {
			throw new RuntimeException(e);
		}
	};
	
	/**
	 * Returns a singleton instance of the kernel.
	 * @return
	 */
	public static IConfiguration getSingleton() {
		if (SINGLETON == null)
			SINGLETON = new Configuration(null);
		
		return SINGLETON;
	}

	/**
	 * Returns a singleton instance of the kernel with 
	 * the given main class.
	 * 
	 * @see #setMainClass(Class)
	 */
	public static IConfiguration getSingleton(Class<?> mainClass) {
		if (SINGLETON == null)
			SINGLETON = new Configuration(mainClass);
		
		return SINGLETON;
	}

	@Override
	public Object getProperty(String key) {
		Object value = properties.get(key);
		if (value == null)
			value = System.getProperty(key);
		if (value == null)
			value = defaultValues.get(key);
		
		return value;
	}

	@Override
	public String getPropertyAsStr(String key) {
		final Object value = getProperty(key);
		if (value != null) {
			return value.toString();
		} else
			return null;
	}

	@Override
	public void setProperty(String key, Object value) {
		properties.put(key, value);
	}

	@Override
	public Object getDefaultValue(String key) {
		return defaultValues.get(key);
	}

	@Override
	public void processCommandLineArguments(String[] args) throws ConfigurationException {
		Options options = new Options();
		
		// option: help
		Option help = new Option( "help", "print this message" );
		
		// option: sopeco config file
		Option config = OptionBuilder.withArgName("file")
										.hasArg()
										.withDescription("sopeco configuration file")
										.create("config");

		// option: logger config file
		Option logconfig = OptionBuilder.withArgName("file")
										.hasArg()
										.withDescription("the logback configuration file")
										.create("logconfig");

		// option: ME URI
		Option meURI = OptionBuilder.withArgName("URI")
										.hasArg()
										.withDescription("URI of the measurement environment controller service")
										.create("uri");
		
		// option: ME Class
		Option meClass = OptionBuilder.withArgName("class")
										.hasArg()
										.withDescription("classname of the measurement environment controller")
										.create("meClass");
		
		// option: scenario definition
		Option scenarioDef = OptionBuilder.withArgName("file")
										.hasArg()
										.withDescription("scenario definition file")
										.create("sd");

		// option 
		Option logVerbosity = OptionBuilder.withArgName("level")
				.hasArg()
				.withDescription("logging verbosity level (overrides log config)")
				.create("lv");
		
//		Option selectedSeries = OptionBuilder.withArgName("series")
//		.withValueSeparator(',')
//		.hasArg()
//		.withDescription("the names of selected experiment series separated by comma")
//		.create("series");

		OptionGroup meGroup = new OptionGroup();
		meGroup.addOption(meURI);
		meGroup.addOption(meClass);
		
		options.addOption(scenarioDef);
		options.addOption(help);
		options.addOption(config);
		options.addOptionGroup(meGroup);
		options.addOption(logconfig);
//		options.addOption(logVerbosity);

		/* Adding all extension options */
		// the following does not check for option conflicts 
		for (ICommandLineArgumentsExtension iclae: getCLAExtensions()) 
			for (Option opt: iclae.getCommandLineOptions())
				options.addOption(opt);
		
	    CommandLineParser parser = new GnuParser();
	    CommandLine line = null;
	    try {
	        line = parser.parse( options, args );
	    }
	    catch( ParseException exp ) {
	    	if (Tools.exists("-help", args) < 0) {
		    	final String err = "Invalid options. " + exp.getMessage();
		        System.err.println(err);
		        printHelpMessage(options);
		        throw new ConfigurationException(err, exp);
	    	} else {
	    		printHelpMessage(options);
	    		System.exit(0);
	    	}
	    }

	    // -help
	    if (line.hasOption(help.getOpt())) {
	        printHelpMessage(options);
	        System.exit(0);
	    }

	    // -sd 
	    setScenarioDescriptionFileName(line.getOptionValue(scenarioDef.getOpt()));
	    
	    // -config
	    if (line.hasOption(config.getOpt())) {
	    	final String configFilePath = line.getOptionValue(config.getOpt());
	    	loadConfiguration(configFilePath);
	    }
	    
	    // -uri
	    if (line.hasOption(meURI.getOpt())) {
	    	final String uriStr = line.getOptionValue(meURI.getOpt());
	    	setMeasurementControllerURI(uriStr);
	    }
	    
	    // -meClass
	    if (line.hasOption(meClass.getOpt())) {
	    	final String className = line.getOptionValue(meClass.getOpt());
	    	setMeasurementControllerClassName(className);
	    }

	    // -logconfig
	    if (line.hasOption(logconfig.getOpt())) {
	    	setLoggerConfigFileName(line.getOptionValue(logconfig.getOpt()));
	    }
	    
	    // -lv
	    if (line.hasOption(logVerbosity.getOpt())) {
	    	final String verbosity = line.getOptionValue(logVerbosity.getOpt());
	    	// TODO it may not be possible
	    }

	    /* processing all extension options */
	    for (ICommandLineArgumentsExtension iclae: getCLAExtensions())
	    	iclae.processOptions(line);
	    
	} 

	/**
	 * Sets the default property values. 
	 * @throws ConfigurationException 
	 */
	private void setDefaultValues(Class<?> mainClass) throws ConfigurationException {
		if (mainClass != null)
			defaultValues.put(CONF_MAIN_CLASS, mainClass);
		
		if (getAppRootDirectory() == null)
		defaultValues.put(CONF_APP_ROOT_FOLDER, Tools.getRootFolder());

		loadDefaultConfiguration(this.getClass().getClassLoader(), DEFAULT_CONFIG_FILE_NAME);
	}

	/**
	 * Prints a usage help message.
	 * 
	 * @param options command-line options 
	 */
	private void printHelpMessage(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.setWidth(120);
		formatter.setArgName("Test");
		formatter.printHelp(getApplicationName(), options, true );
	}

	@Override
	public void loadDefaultConfiguration(String fileName) throws ConfigurationException {
		loadConfiguration(defaultValues, fileName);
	}

	@Override
	public void loadDefaultConfiguration(ClassLoader classLoader, String fileName) throws ConfigurationException {
		try {
			loadConfiguration(defaultValues, classLoader, fileName);
		} catch (ConfigurationException ce) {
			logger.warn("Could not find the default configuration file from classpath. Trying the root folder...");
			loadDefaultConfiguration(getAppRootDirectory() + File.separator + fileName);
		}
	}

	@Override
	public void loadConfiguration(String fileName) throws ConfigurationException {
		loadConfiguration(properties, fileName);
		applyConfiguration();
	}

	@Override
	public void applyConfiguration() {
		configureLogger();
	}

	/**
	 * Loads configurations from a file into
	 * the destination properties holder. 
	 */
	private void loadConfiguration(Map<String, Object> dest, String fileName) throws ConfigurationException {
		logger.debug("Loading configuration from '{}'...", fileName);
		
		try {
			loadConfigFromStream(dest, new FileInputStream(fileName));
		} catch (FileNotFoundException e) {
			final String msg = "Could not load configuration from file. File '" + fileName + "' was not found.";
			throw new ConfigurationException(msg);
			// logger.error("Could not load configuration from file. File not found.");
		}
		
		logger.debug("Configuration file loaded.");
	}

	/**
	 * Loads configurations from a file available through the given class loader into
	 * the destination properties holder. 
	 */
	private void loadConfiguration(Map<String, Object> dest, ClassLoader classLoader, String fileName) throws ConfigurationException {
		logger.debug("Loading configuration from '{}' in classpath...", fileName);
		
		InputStream in = classLoader.getResourceAsStream(fileName);
			
		if (in == null) {
			final String msg = "Could not load configuration from file. File '" + fileName + "' was not found in the classpath.";
			throw new ConfigurationException(msg);
		}
		
		loadConfigFromStream(dest, in);

		logger.debug("Configuration file loaded.");
	}

	/**
	 * Loads configuration from a config stream into the destination
	 * configuration holder.
	 * 
	 * @param stream the input stream
	 */
	private void loadConfigFromStream(Map<String, Object> dest, InputStream stream) throws ConfigurationException {
		Properties prop = new Properties();
		try {
			prop.load(stream);
		} catch (IOException e) {
			throw new ConfigurationException("Could not load configuration. (Reason: " + e.getMessage() + ")", e);
		}
		for (Entry<Object, Object> entry : prop.entrySet()) {
			if (dest == properties) {
				setProperty((String)entry.getKey(), entry.getValue());
			} else
				dest.put((String)entry.getKey(), entry.getValue());
		}
	}
	
	/**
	 * Loads all the command line argument provider extensions. 
	 */
	private List<ICommandLineArgumentsExtension> getCLAExtensions() {
		if (extensions == null) {
			extensions = new ArrayList<ICommandLineArgumentsExtension>();
			
			final IExtensionRegistry registry = Platform.getExtensionRegistry();
			
			// TODO need to use the Engine registry
			if (registry == null) {
				logger.warn("Cannot load command line argument extensions. Cannot access platform extensions registry.");
				return Collections.emptyList();
			}
			
			IConfigurationElement[] configs = registry.getConfigurationElementsFor(CLA_EXTENSION_ID);
			for (IConfigurationElement ext : configs) {
				Object o = null;
				try {
					o = ext.createExecutableExtension("class");
					if (o instanceof ICommandLineArgumentsExtension) {
						final ICommandLineArgumentsExtension es = (ICommandLineArgumentsExtension)o;
						extensions.add(es);
					}
				} catch (CoreException e) {
					logger.warn("Could not load the {} extension. Error: {}", ext.getName(), e.getMessage());
				}
			}
		}
		return extensions;
	}

	@Override
	public void setScenarioDescriptionFileName(String fileName) {
		setProperty(CONF_SCENARIO_DESCRIPTION_FILE_NAME, fileName);
	}

	@Override
	public void setScenarioDescription(Object sceanrioDescription) {
		setProperty(CONF_SCENARIO_DESCRIPTION, sceanrioDescription);
	}

	@Override
	public void setMeasurementControllerURI(String uri) throws ConfigurationException {
		try {
			setProperty(CONF_MEASUREMENT_CONTROLLER_URI, new URI(uri));
		} catch (URISyntaxException e) {
			logger.error("Could not parse the URI {}. Error: {}", uri, e.getMessage());
			throw new ConfigurationException(e);
		}
	}

	@Override
	public void setMeasurementControllerClassName(String className) {
		setProperty(CONF_MEASUREMENT_CONTROLLER_CLASS_NAME, className);
		setProperty(CONF_MEASUREMENT_CONTROLLER_URI, "class://" + className);
	}

	@Override
	public void setApplicationName(String appName) {
		setProperty(CONF_APP_NAME, appName);
	}

	@Override
	public String getScenarioDescriptionFileName() {
		return getPropertyAsStr(CONF_SCENARIO_DESCRIPTION_FILE_NAME);
	}

	@Override
	public Object getScenarioDescription() {
		return getProperty(CONF_SCENARIO_DESCRIPTION);
	}

	@Override
	public String getMeasurementControllerURIAsStr() {
		return getPropertyAsStr(CONF_MEASUREMENT_CONTROLLER_URI);
	}

	@Override
	public URI getMeasurementControllerURI() {
		return (URI)getProperty(CONF_MEASUREMENT_CONTROLLER_URI);
	}
	
	@Override
	public String getMeasurementControllerClassName() {
		return getPropertyAsStr(CONF_MEASUREMENT_CONTROLLER_CLASS_NAME);
	}

	@Override
	public String getApplicationName() {
		return getPropertyAsStr(CONF_APP_NAME);
	}
	
	@Override
	public String getAppRootDirectory() {
		String result = getPropertyAsStr(CONF_APP_ROOT_FOLDER);
		if (result == null) {
			result = System.getenv(ENV_SOPECO_HOME);
			if (result == null) {
				result = Tools.getRootFolder(getMainClass());
			}
			setProperty(CONF_APP_ROOT_FOLDER, result);
		}
		return result;
	}

	@Override
	public String getAppConfDirectory() {
		return getAppRootDirectory() + File.separator + CONFIGURATION_FOLDER;
	}
	
	public void setMainClass(Class<?> mainClass) {
		setProperty(CONF_MAIN_CLASS, mainClass);
	}

	@Override
	public Class<?> getMainClass() {
		return (Class<?>) getProperty(CONF_MAIN_CLASS);
	}

	@Override
	public void setLoggerConfigFileName(String fileName) {
        setProperty(CONF_LOGGER_CONFIG_FILE_NAME, fileName);
        configureLogger();
	}

	/**
	 * Configures the logging system with the provided logger configuration file.
	 */
	private void configureLogger() {
    	// The following code loads the logback config file using JoranConfigurator.
    	// Alternatively, you can specify the location of the config file using
    	// the system property 'logback.configurationFile'
    	// e.g., 
    	// $ java -Dlogback.configurationFile=/path/to/config.xml ...
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

        final String fileName = getPropertyAsStr(CONF_LOGGER_CONFIG_FILE_NAME);
        if (fileName != null) {
	        try {
	          JoranConfigurator configurator = new JoranConfigurator();
	          configurator.setContext(lc);
	          // the context was probably already configured by default configuration 
	          // rules
	          lc.reset(); 
	          configurator.doConfigure(fileName);
	        } catch (JoranException je) {
	           logger.warn("Failed loading the logback configuration file. Using default configuration. Error message: {}", je.getMessage());
	        }
	        
	    	logger.debug("Configured logback using '{}'.", fileName);
        }
	}
	
	
	@Override
	public void writeConfiguration(String fileName) throws IOException {
		Properties props = new Properties();
		
		logger.debug("Writing configuration file to {}...", fileName);
		
		for (Entry<String, Object> e: defaultValues.entrySet()) 
			copyConfigItem(e, props);
		
		for (Entry<String, Object> e: properties.entrySet()) 
			copyConfigItem(e, props);
		
		props.store(new FileOutputStream(fileName), "SoPeCo Configuration");
	
		logger.debug("Configuration file written to {}.", fileName);
	}

	private void copyConfigItem(Entry<String, Object> e, Properties destination) {
		if (e.getValue() instanceof Number
				|| e.getValue() instanceof Boolean
				|| e.getValue() instanceof String) {
			destination.setProperty(e.getKey(), e.getValue().toString());
		} else
			logger.debug("Skipping configuration item '{}'.", e.getKey());
	}

}




//// -series
//if (line.hasOption(selectedSeries.getOpt())) {
//	final String[] selectedNames = line.getOptionValues(selectedSeries.getOpt());
//	this.selectedSeries = new HashSet<String>();
//	for (String sname: selectedNames)
//		this.selectedSeries.add(sname);
//}


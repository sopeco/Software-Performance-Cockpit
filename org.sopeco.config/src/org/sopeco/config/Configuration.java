package org.sopeco.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
		setDefaultValues(mainClass);
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
		meGroup.setRequired(true);
		
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
	    	loadConfigFromFile(configFilePath);
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
	    	String logbackConfigFilePath = line.getOptionValue(logconfig.getOpt());
	    	
	    	// The following code loads the logback config file using JoranConfigurator.
	    	// Alternatively, you can specify the location of the config file using
	    	// the system property 'logback.configurationFile'
	    	// e.g., 
	    	// $ java -Dlogback.configurationFile=/path/to/config.xml ...
	    	
	        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
	        
	        try {
	          JoranConfigurator configurator = new JoranConfigurator();
	          configurator.setContext(lc);
	          // the context was probably already configured by default configuration 
	          // rules
	          lc.reset(); 
	          configurator.doConfigure(logbackConfigFilePath);
	        } catch (JoranException je) {
	           logger.warn("Failed loading the logback configuration file. Using default configuration. Error message: {}", je.getMessage());
	        }

	        setProperty(CONF_LOGGER_CONFIG_FILE_NAME, logbackConfigFilePath);
	        
	    	logger.debug("Configured logback using '{}'.", logbackConfigFilePath);
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
	 */
	private void setDefaultValues(Class<?> mainClass) {
		if (mainClass != null)
			setMainClass(mainClass);
		
		setApplicationName("sopeco");
		setProperty(CONF_PLUGINS_FOLDER, "plugins");
		
		if (getAppRootDirectory() == null)
		setProperty(CONF_APP_ROOT_FOLDER, Tools.getRootFolder());
		
		String configFileName = getAppConfDirectory() + File.separator + DEFAULT_CONFIG_FILE_NAME;
		InputStream in = this.getClass().getResourceAsStream(configFileName);
		
		if (in != null) {
			loadConfigFromStream(in);
			logger.debug("Loaded default configuration file.");
		} else {
			logger.warn("Could not find default configuration file ('{}').", configFileName);
		}
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

	/**
	 * Loads SoPeCo configuration from a config file.
	 * 
	 * @param fileName file name
	 */
	private void loadConfigFromFile(String fileName) {
		logger.debug("Loading configuration from {}...", fileName);
		
		try {
			loadConfigFromStream(new FileInputStream(fileName));
		} catch (FileNotFoundException e) {
			logger.error("Could not load configuration from file. File not found.");
			return;
		}
		
		logger.debug("Configuration file loaded.");
	}
	
	/**
	 * Loads SoPeCo configuration from a config stream.
	 * 
	 * @param stream the input stream
	 */
	private void loadConfigFromStream(InputStream stream) {
		Properties prop = new Properties();
		try {
			prop.load(stream);
		} catch (IOException e) {
			logger.error("Could not load configuration. (Reason: {})", e.getMessage());
			return;
		}
		for (Entry<Object, Object> entry : prop.entrySet())
			properties.put((String)entry.getKey(), entry.getValue());
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

}




//// -series
//if (line.hasOption(selectedSeries.getOpt())) {
//	final String[] selectedNames = line.getOptionValues(selectedSeries.getOpt());
//	this.selectedSeries = new HashSet<String>();
//	for (String sname: selectedNames)
//		this.selectedSeries.add(sname);
//}


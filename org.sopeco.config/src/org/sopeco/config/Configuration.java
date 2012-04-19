package org.sopeco.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private Configuration() {
		setDefaultValues();
	};
	
	/**
	 * Returns a singleton instance of the kernel.
	 * @return
	 */
	public static IConfiguration getSingleton() {
		if (SINGLETON == null)
			SINGLETON = new Configuration();
		
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
	public void setProperty(String key, Object value) {
		properties.put(key, value);
	}

	@Override
	public Object getDefaultValue(String key) {
		return defaultValues.get(key);
	}

	@Override
	public void processCommandLineArguments(String[] args) {
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
		
//		Option selectedSeries = OptionBuilder.withArgName("series")
//										.withValueSeparator(',')
//										.hasArg()
//										.withDescription("the names of selected experiment series separated by comma")
//										.create("series");
		
		options.addOption(help);
		options.addOption(config);
		options.addOption(logconfig);

		/* Adding all extension options */
		// the following does not check for option conflicts 
		for (ICommandLineArgumentsExtension iclae: getCLAExtensions()) 
			for (Option opt: iclae.getCommandLineOptions())
				options.addOption(opt);
		
	    CommandLineParser parser = new GnuParser();
	    CommandLine line = null;
	    try {
	        line = parser.parse( options, args );
	        
	        // there must be exactly one argument, which is the config file name 
	        if (line.getArgs().length != 1) {
	        	throw new ParseException("This program requires at least one argument"
	        			+ " (the path to the measurement specification file).");
	        } else
	        	setProperty(CONF_MEASUREMENT_SPEC_FILE_NAME, line.getArgs()[0]);
	    }
	    catch( ParseException exp ) {
	        System.err.println("Invalid options. " + exp.getMessage() );
	        printHelpMessage(options);
	        return;
	    }
	    
	    // -help
	    if (line.hasOption(help.getOpt())) {
	        printHelpMessage(options);
	        System.exit(0);
	    }
	    
	    // -config
	    if (line.hasOption(config.getOpt())) {
	    	final String configFilePath = line.getOptionValue(config.getOpt());
	    	loadConfigFromFile(configFilePath);
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

	    /* processing all extension options */
	    for (ICommandLineArgumentsExtension iclae: getCLAExtensions())
	    	iclae.processOptions(line);
	    
	} 

	/**
	 * Sets the default property values. 
	 */
	private void setDefaultValues() {
		setProperty(CONF_APP_NAME, "SoPeCo");
	}

	/**
	 * Prints a usage help message.
	 * 
	 * @param options command-line options 
	 */
	private void printHelpMessage(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(getProperty(CONF_APP_NAME) + " [OPTIONS] MEASUREMENT_SPEC_FILE", options );
	}

	/**
	 * Loads SoPeCo configuration from a config file.
	 * 
	 * @param fileName file name
	 */
	private void loadConfigFromFile(String fileName) {
		logger.debug("Loading configuration from {}...", fileName);
		
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(fileName));
		} catch (FileNotFoundException e) {
			logger.error("Could not load configuration from file. File not found.");
		} catch (IOException e) {
			logger.error("Could not load configuration from file. (Reason: {})", e.getMessage());
		}
		for (Entry<Object, Object> entry : prop.entrySet())
			properties.put((String)entry.getKey(), entry.getValue());
		
		logger.debug("Configuration file loaded.");
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
}



//// -series
//if (line.hasOption(selectedSeries.getOpt())) {
//	final String[] selectedNames = line.getOptionValues(selectedSeries.getOpt());
//	this.selectedSeries = new HashSet<String>();
//	for (String sname: selectedNames)
//		this.selectedSeries.add(sname);
//}


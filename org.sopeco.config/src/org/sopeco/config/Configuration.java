package org.sopeco.config;

import java.util.HashMap;
import java.util.Map;


/**
 * This class is the access point to all SoPeCo global configuration. 
 * 
 * @author Roozbeh Farahbod
 *
 */
public class Configuration implements IConfiguration {

	private static IConfiguration SINGLETON = null;
	
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
		// TODO perhaps following a plugin-based approach to have command-line options
	}

	/**
	 * Sets the default property values. 
	 */
	private void setDefaultValues() {
		// does nothing yet
	}
}

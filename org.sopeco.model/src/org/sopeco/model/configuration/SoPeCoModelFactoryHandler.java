package org.sopeco.model.configuration;

import org.sopeco.model.configuration.environment.EnvironmentFactory;
import org.sopeco.model.configuration.environment.ext.EnvironmentFactoryExt;
import org.sopeco.model.configuration.ext.ConfigurationFactoryExt;


/**
 * This class is responsible for the initialization of the factories provided by the generated code.
 * The initialization needs to be called once before the generated factories are used in the code.
 * Furthermore, it acts as a proxy to get the factories.
 * 
 * @author Dennis Westermann
 *
 */
public class SoPeCoModelFactoryHandler {
	
	/**
	 * Initializes the factories so that the extended implementations of it are used.
	 */
	public static void initFactories(){
	 
		ConfigurationFactoryExt.getFactory();
		EnvironmentFactoryExt.getFactory();
	}

	/**
	 * Provides access to the {@link ConfigurationFactory}.
	 * 
	 * @return the extended version of the {@link ConfigurationFactory}
	 */
	public static ConfigurationFactory getConfigurationFactory(){
		
		return ConfigurationFactoryExt.getFactory();
		
	}
	
	/**
	 * Provides access to the {@link EnvironmentFactory}.
	 * 
	 * @return the extended version of the {@link EnvironmentFactory}
	 */
	public static EnvironmentFactory getEnvironmentFactory(){
		
		return EnvironmentFactoryExt.getFactory();
		
	}

}

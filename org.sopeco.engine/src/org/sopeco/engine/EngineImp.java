/**
 * 
 */
package org.sopeco.engine;

import org.sopeco.config.Configuration;
import org.sopeco.config.IConfiguration;
import org.sopeco.engine.registry.ExtensionRegistry;
import org.sopeco.engine.registry.IExtensionRegistry;

/**
 * The default implementation of SoPeCo engine.
 * 
 * @author Roozbeh Farahbod
 *
 */
public class EngineImp implements IEngine {

	private IConfiguration configuration = null; 
	
	private IExtensionRegistry registry = null; 
	
	@Override
	public IConfiguration getConfiguration() {
		if (configuration == null)
			configuration = Configuration.getSingleton();
		return configuration;
	}

	@Override
	public IExtensionRegistry getExtensionRegistry() {
		if (registry == null)
			registry = ExtensionRegistry.getSingleton();
		return registry;
	}

}

/**
 * 
 */
package org.sopeco.engine;

import org.sopeco.config.IConfiguration;
import org.sopeco.engine.registry.IExtensionRegistry;

/**
 * Interface of the SoPeCo Engine.
 * 
 * @author Roozbeh Farahbod
 *
 */
public interface IEngine {

	/**
	 * Returns the global configuration element.
	 */
	public IConfiguration getConfiguration();
	
	/**
	 * Returns the SoPeCo extension registry.
	 */
	public IExtensionRegistry getExtensionRegistry();
	
}

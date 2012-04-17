/**
 * 
 */
package org.sopeco.example.plugins.kernel;

import org.sopeco.core.registry.PluginRegistry;

/**
 * A sample kernel for SoPeCo plugin-based architecture.
 * 
 * @author d057134
 *
 */
public class Kernel {

	private PluginRegistry pluginRegistry;
	
	/**
	 * Initializes the Kernel.
	 */
	public void initialize() {
		this.pluginRegistry = new PluginRegistry();
		this.pluginRegistry.initialize();
	}
	
	public PluginRegistry getPluginRegistry()  {
		return pluginRegistry;
	}
}

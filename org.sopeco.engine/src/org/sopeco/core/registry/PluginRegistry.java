/**
 * 
 */
package org.sopeco.core.registry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.sopeco.example.plugins.exploration.ExplorationStrategy;

/**
 * A sample plugin registry.
 * 
 * TODO: To be implemented as Generic
 * 
 * @author d057134
 *
 */
public class PluginRegistry {

	public static final String EP_EXPLORATION_STRATEGY_ID = "org.sopeco.example.plugins.eps.explorationstrategy";
	
	Map<String, ExplorationStrategy> explorationStrategies = new HashMap<String, ExplorationStrategy>();

	/** 
	 * Initializes the plugin registry.
	 */
	public void initialize() {
		updatePluginRegistry();
	}
	
	/**
	 * Returns a collection of registered exploration strategies.
	 */
	public Collection<ExplorationStrategy> getExplorationStrategies() {
		return explorationStrategies.values();
	}
	
	/**
	 * Returns a single exploration strategy registerd with the given name.
	 * 
	 */
	public ExplorationStrategy getExplorationStrategy(String name) {
		return explorationStrategies.get(name);
	}
	
	private void updatePluginRegistry() {
		updateExplorationStrategyPlugins();
	}
	
	private void updateExplorationStrategyPlugins() {
		
		IConfigurationElement[] config = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(EP_EXPLORATION_STRATEGY_ID);
		try {
			for (IConfigurationElement e : config) {
				final Object o = e.createExecutableExtension("class");
				if (o instanceof ExplorationStrategy) {
					final ExplorationStrategy es = (ExplorationStrategy)o;
					explorationStrategies.put(es.getName(), es);
				}
			}
		} catch (CoreException ex) {
			System.err.println(ex.getMessage());
		}
	}

}

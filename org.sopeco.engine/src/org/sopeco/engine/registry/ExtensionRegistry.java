/**
 * 
 */
package org.sopeco.engine.registry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The extension registry of SoPeCo. This class acts as a wrapper around Eclipse OSGi extensions 
 * repository.
 * 
 * @author Roozbeh Farahbod
 *
 */
public class ExtensionRegistry {

	private static Logger logger = LoggerFactory.getLogger(ExtensionRegistry.class);
	
	/**
	 * Holds the list of extension point identifiers supported by the engine.
	 */
	public static final String[] EXTENSION_POINTS = {
		"org.sopeco.engine.experimentseries.explorationstrategy",
		"org.sopeco.engine.experimentseries.parametervariation",
		"org.sopeco.engine.analysis.analysisstrategy"
	};
	
	private static ExtensionRegistry SINGLETON = null;
	
	/** Holds a mapping of extension names to extensions. */
	private Map<String, ISoPeCoExtension> extensions = new HashMap<String, ISoPeCoExtension>();
	
	private boolean initialized = false;

	/*
 	 * Preventing public instantiation of the registry. 
	 */
	private ExtensionRegistry() {
		initialize();
	}
	
	/**
	 * Returns a singleton instance of the extension registry.
	 */
	public static ExtensionRegistry getRegistry() {
		if (SINGLETON == null)
			SINGLETON = new ExtensionRegistry();
		return SINGLETON;
	}
	
	/** 
	 * Initializes the plugin registry.
	 */
	private void initialize() {
		if (!initialized)
			loadEngineExtensions();
		else 
			logger.warn("Plugin registry cannot be re-initialized.");
		
		initialized = true;
	}
	
	/**
	 * Returns a collection of all registered SoPeCo extensions.
	 */
	public Collection<ISoPeCoExtension> getExtensions() {
		return extensions.values();
	}
	
	/**
	 * Loads all the extensions that are supported by the engine.
	 */
	private void loadEngineExtensions() {
		final IExtensionRegistry registry = Platform.getExtensionRegistry();

		// loads all extensions
		for (String id: EXTENSION_POINTS) {
			loadExtensions(registry, id);
		}
	}

	/**
	 * Loads the extensions based on the given extension point id.
	 *  
	 * @param registry the Eclipse platform extension registry
	 * @param eid extension point id
	 */
	private void loadExtensions(IExtensionRegistry registry, String eid) {
		IConfigurationElement[] configs = registry.getConfigurationElementsFor(eid);
		for (IConfigurationElement ext : configs) {
			Object o = null;
			try {
				o = ext.createExecutableExtension("class");
				if (o instanceof ISoPeCoExtension) {
					final ISoPeCoExtension es = (ISoPeCoExtension) o;
					extensions.put(es.getName(), es);
				}
			} catch (CoreException e) {
				logger.warn("Could not load the {} extension. Error: {}", ext.getName(), e.getMessage());
			}
		}
	}
}

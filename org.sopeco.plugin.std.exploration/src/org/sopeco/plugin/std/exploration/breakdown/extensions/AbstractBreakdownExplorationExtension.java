/**
 * 
 */
package org.sopeco.plugin.std.exploration.breakdown.extensions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.sopeco.engine.experimentseries.IExplorationStrategy;
import org.sopeco.engine.experimentseries.IExplorationStrategyExtension;
import org.sopeco.plugin.std.exploration.breakdown.BreakdownExplorationController;

/**
 * Abstract extension that provides the methods common to all breakdown
 * strategy extensions.
 * 
 * @author Dennis Westermann, Roozbeh Farahbod
 * 
 */
public abstract class AbstractBreakdownExplorationExtension implements IExplorationStrategyExtension {

	/** Holds a mapping of configuration parameters to their optional default values. */
	protected final Map<String, String> configParams = new HashMap<String, String>();



	protected AbstractBreakdownExplorationExtension() {
		prepareConfigurationParameterMap();
	}

//	@Override
//	public String getName();

	@Override
	public IExplorationStrategy createExtensionArtifact() {
		return new BreakdownExplorationController(this);
	}

	/**
	 * Called by the constructor of this class to prepare
	 * the set of configuration parameters ({@link #configParams}).
	 */
	protected abstract void prepareConfigurationParameterMap();

	@Override
	public Map<String, String> getConfigParameters() {
		return Collections.unmodifiableMap(configParams);
	}


}

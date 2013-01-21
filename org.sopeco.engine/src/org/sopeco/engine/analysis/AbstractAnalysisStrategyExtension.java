/**
 * 
 */
package org.sopeco.engine.analysis;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * An abstract class for Prediction Function Extensions that provides the default 
 * handling of configuarion parameters.
 * 
 * @author Roozbeh Farahbod
 *
 */
public abstract class AbstractAnalysisStrategyExtension {
	
	/** Holds a mapping of configuration parameters to their optional default values. */
	private final Map<String, String> configParams = new HashMap<String, String>();

	protected AbstractAnalysisStrategyExtension() {
		prepareConfigurationParameterMap();
	}
	
	/**
	 * Called by the constructor of this class to prepare
	 * the set of configuration parameters ({@link #configParams}).
	 */
	protected abstract void prepareConfigurationParameterMap();

	/**
	 * Returns the supported configurations parameters. The returned map is not modifiable.
	 * @return the supported configurations parameters.
	 */
	public Map<String, String> getConfigParameters() {
		return Collections.unmodifiableMap(getModifiableConfigParams());
	}

	/**
	 * Returns the supported configurations parameters. Use this method to modify the parameters.
	 * @return the supported configurations parameters.
	 */
	protected Map<String, String> getModifiableConfigParams() {
		return configParams;
	}

}

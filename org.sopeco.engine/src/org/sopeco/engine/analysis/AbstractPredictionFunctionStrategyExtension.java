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
public abstract class AbstractPredictionFunctionStrategyExtension implements IPredictionFunctionStrategyExtension {
	
	/** Holds a mapping of configuration parameters to their optional default values. */
	protected final Map<String, String> configParams = new HashMap<String, String>();

	protected AbstractPredictionFunctionStrategyExtension() {
		prepareConfigurationParameterMap();
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

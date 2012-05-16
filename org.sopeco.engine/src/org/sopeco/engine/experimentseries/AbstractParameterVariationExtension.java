/**
 * 
 */
package org.sopeco.engine.experimentseries;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * An abstract class for Parameter Variation Extensions that provides the default 
 * handling of configuarion parameters.
 * 
 * @author Roozbeh Farahbod
 *
 */
public abstract class AbstractParameterVariationExtension implements IParameterVariationExtension {
	
	/** Holds a mapping of configuration parameters to their optional default values. */
	protected final Map<String, String> configParams = new HashMap<String, String>();

	protected AbstractParameterVariationExtension() {
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

package org.sopeco.plugin.std.exploration.screening.extensions;

import org.sopeco.plugin.std.exploration.screening.config.ScreeningConfiguration;

/**
 * The extension that provides the plackett burman exploration strategy.
 * 
 * @author Dennis Westermann
 *
 */
public class PlackettBurmanExtension extends AbstractScreeningExplorationExtension {

	
	/**
	 * The name of the provided extension artifact.
	 */
	public static final String NAME = ScreeningConfiguration.PLACKETT_BURMAN;

	
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	protected void prepareConfigurationParameterMap() {
		
		
		/* Common Properties */
		// Replications not yet supported by screening analysis 
//		configParams.put(ScreeningConfiguration.USE_REPLICATION, "false");
//		configParams.put(ScreeningConfiguration.NUM_REPLICATIONS, "");
		configParams.put(ScreeningConfiguration.RANDOMIZE_RUNS, "false");
		
	}
}
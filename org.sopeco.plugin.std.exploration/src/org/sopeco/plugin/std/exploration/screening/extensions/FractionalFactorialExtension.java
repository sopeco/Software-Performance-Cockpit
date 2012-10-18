package org.sopeco.plugin.std.exploration.screening.extensions;

import org.sopeco.plugin.std.exploration.screening.config.ScreeningConfiguration;

/**
 * The extension that provides the fractional factorial exploration strategy.
 * 
 * @author Dennis Westermann
 *
 */
public class FractionalFactorialExtension extends AbstractScreeningExplorationExtension {

	/**
	 * The name of the provided extension artifact.
	 */
	public static final String NAME = ScreeningConfiguration.FRACTIONAL_FACTORIAL;

	
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
		
		/* Only for Fractional Factorial */
		configParams.put(ScreeningConfiguration.RESOLUTION, "3");
		configParams.put(ScreeningConfiguration.USE_NUM_RUNS_INSTEAD_OF_RESOLUTION, "false");
		configParams.put(ScreeningConfiguration.NUMBER_OF_RUNS, "");
		
	}
}
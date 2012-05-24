package org.sopeco.plugin.std.exploration.screening.extensions;

import org.sopeco.plugin.std.exploration.screening.config.ScreeningConfiguration;

/**
 * The extension that provides the plackett burman exploration strategy.
 * 
 * @author Dennis Westermann
 *
 */
public class PlackettBurmanExtension extends AbstractScreeningExplorationExtension {

	public PlackettBurmanExtension() {
		this.NAME = ScreeningConfiguration.PLACKETT_BURMAN;
	}
	
	@Override
	protected void prepareConfigurationParameterMap() {
		
		
		/* Common Properties */
		configParams.put(ScreeningConfiguration.USE_REPLICATION, "false");
		configParams.put(ScreeningConfiguration.NUM_REPLICATIONS, "");
		configParams.put(ScreeningConfiguration.RANDOMIZE_RUNS, "false");
		
	}
}
package org.sopeco.plugin.std.exploration.breakdown.extensions;

import org.sopeco.plugin.std.exploration.breakdown.util.BreakdownConfiguration;

/**
 * The extension that provides the adaptive random breakdown exploration strategy.
 * 
 * @author Dennis Westermann
 *
 */
public class AdaptiveRandomBreakdownExtension extends AbstractBreakdownExplorationExtension {
	public AdaptiveRandomBreakdownExtension() {
		this.NAME = BreakdownConfiguration.ADAPTIVE_RANDOM_BREAKDOWN;
	}

	@Override
	protected void prepareConfigurationParameterMap() {
		// TODO Dennis
		
	}
}

package org.sopeco.plugin.std.exploration.breakdown.extensions;

import org.sopeco.plugin.std.exploration.breakdown.util.BreakdownConfiguration;

/**
 * The extension that provides the equidistant breakdown exploration strategy.
 * 
 * @author Dennis Westermann
 *
 */
public class EquidistantBreakdownExtension extends AbstractBreakdownExplorationExtension {
	public EquidistantBreakdownExtension() {
		this.NAME = BreakdownConfiguration.EQUIDISTANT_BREAKDOWN;
	}

	@Override
	protected void prepareConfigurationParameterMap() {
		// TODO Dennis
		
	}
}
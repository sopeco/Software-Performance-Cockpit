package org.sopeco.plugin.std.exploration.breakdown.extensions;

import org.sopeco.plugin.std.exploration.breakdown.util.BreakdownConfiguration;

/**
 * The extension that provides the adaptive equidistant breakdown exploration strategy.
 * 
 * @author Dennis Westermann
 *
 */
public class AdaptiveEquidistantBreakdownExtension extends AbstractBreakdownExplorationExtension {
	public AdaptiveEquidistantBreakdownExtension() {
		this.NAME = BreakdownConfiguration.EQUIDISTANT_BREAKDOWN;
	}
}
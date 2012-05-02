package org.sopeco.plugin.std.exploration.breakdown.extensions;

import org.sopeco.plugin.std.exploration.breakdown.util.BreakdownConfiguration;

/**
 * The extension that provides the random breakdown exploration strategy.
 * 
 * @author Dennis Westermann
 *
 */
public class RandomBreakdownExtension extends AbstractBreakdownExplorationExtension {

	public RandomBreakdownExtension() {
		this.NAME = BreakdownConfiguration.RANDOM_BREAKDOWN;
	}
}

/**
 * 
 */
package org.sopeco.plugin.std.exploration.breakdown;

import org.sopeco.engine.experimentseries.IExplorationStrategy;
import org.sopeco.engine.experimentseries.IExplorationStrategyExtension;
import org.sopeco.plugin.std.exploration.breakdown.util.BreakdownConfiguration;

/**
 * The extension that provides the random breakdown exploration strategy.
 * 
 * @author Dennis Westermann
 * 
 */
public abstract class BreakdownExplorationExtensions {

	/**
	 * Abstract extension that provides the methods common to all breakdown
	 * strategy extensions.
	 * 
	 * @author Dennis Westermann
	 * 
	 */
	public abstract class AbstractBreakdownExtension implements IExplorationStrategyExtension {

		/**
		 * The name of the provided extension artifact.
		 */
		public String NAME;

		@Override
		public String getName() {
			return NAME;
		}

		@Override
		public IExplorationStrategy createExtensionArtifact() {
			return new BreakdownExplorationController(this);
		}

	}

	/**
	 * The extension that provides the random breakdown exploration strategy.
	 * 
	 * @author Dennis Westermann
	 *
	 */
	public class RandomBreakdownExtension extends AbstractBreakdownExtension {
		public RandomBreakdownExtension() {
			this.NAME = BreakdownConfiguration.RANDOM_BREAKDOWN;
		}
	}

	/**
	 * The extension that provides the adaptive random breakdown exploration strategy.
	 * 
	 * @author Dennis Westermann
	 *
	 */
	public class AdaptiveRandomBreakdownExtension extends AbstractBreakdownExtension {
		public AdaptiveRandomBreakdownExtension() {
			this.NAME = BreakdownConfiguration.ADAPTIVE_RANDOM_BREAKDOWN;
		}
	}

	/**
	 * The extension that provides the equidistant breakdown exploration strategy.
	 * 
	 * @author Dennis Westermann
	 *
	 */
	public class EquidistantBreakdownExtension extends AbstractBreakdownExtension {
		public EquidistantBreakdownExtension() {
			this.NAME = BreakdownConfiguration.EQUIDISTANT_BREAKDOWN;
		}
	}
	
	/**
	 * The extension that provides the adaptive equidistant breakdown exploration strategy.
	 * 
	 * @author Dennis Westermann
	 *
	 */
	public class AdaptiveEquidistantBreakdownExtension extends AbstractBreakdownExtension {
		public AdaptiveEquidistantBreakdownExtension() {
			this.NAME = BreakdownConfiguration.EQUIDISTANT_BREAKDOWN;
		}
	}

}

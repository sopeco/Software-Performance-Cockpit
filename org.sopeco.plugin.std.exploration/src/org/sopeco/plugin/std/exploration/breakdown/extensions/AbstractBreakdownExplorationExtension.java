/**
 * 
 */
package org.sopeco.plugin.std.exploration.breakdown.extensions;

import org.sopeco.engine.experimentseries.IExplorationStrategy;
import org.sopeco.engine.experimentseries.IExplorationStrategyExtension;
import org.sopeco.plugin.std.exploration.breakdown.BreakdownExplorationController;

/**
 * Abstract extension that provides the methods common to all breakdown
 * strategy extensions.
 * 
 * @author Dennis Westermann
 * 
 */
public abstract class AbstractBreakdownExplorationExtension implements IExplorationStrategyExtension {

	
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

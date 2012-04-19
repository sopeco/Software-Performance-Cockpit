/**
 * 
 */
package org.sopeco.engine.experimentseries;

import org.sopeco.engine.registry.ISoPeCoExtension;

/**
 * The interface of extensions providing new exploration strategies.
 * 
 * @author Roozbeh Farahbod
 *
 */
public interface IExplorationStrategyExtension extends ISoPeCoExtension<IExplorationStrategy> {

	/**
	 * Creates a new exploration strategy provided by the extension.
	 */
	public IExplorationStrategy createExtensionArtifact();
}

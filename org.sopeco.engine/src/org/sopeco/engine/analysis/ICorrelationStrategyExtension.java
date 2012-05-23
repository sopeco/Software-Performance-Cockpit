/**
 * 
 */
package org.sopeco.engine.analysis;

import org.sopeco.engine.registry.ISoPeCoExtension;


/**
 * The interface of extensions providing new correlation analysis strategies.
 * 
 * @author Dennis Westermann
 *
 */
public interface ICorrelationStrategyExtension extends ISoPeCoExtension<ICorrelationStrategy> {

	/**
	 * Creates a correlation analysis strategy provided by the extension.
	 */
	public ICorrelationStrategy createExtensionArtifact();

}

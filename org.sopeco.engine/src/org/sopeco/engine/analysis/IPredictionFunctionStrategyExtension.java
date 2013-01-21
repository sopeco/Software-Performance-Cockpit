/**
 * 
 */
package org.sopeco.engine.analysis;

import org.sopeco.engine.registry.ISoPeCoExtension;

/**
 * The interface of extensions providing new prediction function analysis
 * strategies.
 * 
 * @author Dennis Westermann
 * 
 */
public interface IPredictionFunctionStrategyExtension extends ISoPeCoExtension<IPredictionFunctionStrategy> {

	/**
	 * Creates a prediction function analysis strategy provided by the
	 * extension.
	 * 
	 * @return a prediction function analysis strategy
	 */
	IPredictionFunctionStrategy createExtensionArtifact();

}

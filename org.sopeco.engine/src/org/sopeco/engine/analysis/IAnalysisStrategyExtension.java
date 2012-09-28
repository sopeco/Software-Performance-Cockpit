/**
 * 
 */
package org.sopeco.engine.analysis;

import org.sopeco.engine.registry.ISoPeCoExtension;

/**
 * The interface of extensions providing new analysis strategies.
 * 
 * @author Roozbeh Farahbod
 * 
 */
public interface IAnalysisStrategyExtension extends ISoPeCoExtension<IAnalysisStrategy> {

	/**
	 * Creates a new analysis strategy provided by the extension.
	 * 
	 * @return Returns an analysis strategy
	 */
	IAnalysisStrategy createExtensionArtifact();

}

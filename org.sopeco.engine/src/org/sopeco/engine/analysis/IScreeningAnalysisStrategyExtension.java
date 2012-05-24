/**
 * 
 */
package org.sopeco.engine.analysis;

import org.sopeco.engine.registry.ISoPeCoExtension;


/**
 * The interface of extensions providing new screening design analysis strategies.
 * 
 * @author Dennis Westermann
 *
 */
public interface IScreeningAnalysisStrategyExtension extends ISoPeCoExtension<IScreeningAnalysisStrategy> {

	/**
	 * Creates a screening design analysis strategy provided by the extension.
	 */
	public IScreeningAnalysisStrategy createExtensionArtifact();

}

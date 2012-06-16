/**
 * 
 */
package org.sopeco.engine.analysis;

import org.sopeco.engine.registry.ISoPeCoExtension;


/**
 * The interface of extensions providing new ANOVA analysis strategies.
 * 
 * @author Dennis Westermann
 *
 */
public interface IAnovaStrategyExtension extends ISoPeCoExtension<IAnovaStrategy> {

	/**
	 * Creates an ANOVA analysis strategy provided by the extension.
	 */
	public IAnovaStrategy createExtensionArtifact();

}

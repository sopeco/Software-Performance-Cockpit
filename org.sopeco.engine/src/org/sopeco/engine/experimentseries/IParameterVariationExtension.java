/**
 * 
 */
package org.sopeco.engine.experimentseries;

import org.sopeco.engine.registry.ISoPeCoExtension;

/**
 * The interface of extensions providing new parameter variations.
 * 
 * @author Roozbeh Farahbod
 *
 */
public interface IParameterVariationExtension extends ISoPeCoExtension<IParameterVariation> {

	/**
	 * Creates a new parameter variation strategy provided by the extension.
	 */
	public IParameterVariation createExtensionArtifact();
}

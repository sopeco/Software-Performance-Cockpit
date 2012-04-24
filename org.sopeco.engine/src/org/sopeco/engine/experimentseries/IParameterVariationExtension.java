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

	/** The name of the special constant variation extension. */
	public static final String CONSTANT_VARIATION_EXTENSION_NAME = "Constant Variation";
	
	/**
	 * Creates a new parameter variation strategy provided by the extension.
	 */
	public IParameterVariation createExtensionArtifact();
}

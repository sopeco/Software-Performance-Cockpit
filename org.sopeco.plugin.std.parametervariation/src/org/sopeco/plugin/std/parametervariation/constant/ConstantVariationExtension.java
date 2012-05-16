/**
 * 
 */
package org.sopeco.plugin.std.parametervariation.constant;

import org.sopeco.engine.experimentseries.AbstractParameterVariationExtension;
import org.sopeco.engine.experimentseries.IParameterVariation;
import org.sopeco.engine.experimentseries.IParameterVariationExtension;

/**
 * Provides the constant value variation (read "no-variation") extension. 
 * This is provided to have a generic approach to parameter value variations, regardless of 
 * the values being dynamic or constant. 
 * 
 * @author Roozbeh Farahbod
 *
 */
public class ConstantVariationExtension extends AbstractParameterVariationExtension {

	/** Holds the name of this extension. */
	public static final String NAME = IParameterVariationExtension.CONSTANT_VARIATION_EXTENSION_NAME;
	
	public ConstantVariationExtension() { }

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IParameterVariation createExtensionArtifact() {
		return new ConstantVariation(this);
	}

	@Override
	protected void prepareConfigurationParameterMap() {
		// nothing.
	}


}

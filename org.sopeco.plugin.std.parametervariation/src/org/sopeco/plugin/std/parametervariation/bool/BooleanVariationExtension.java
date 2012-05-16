/**
 * 
 */
package org.sopeco.plugin.std.parametervariation.bool;

import org.sopeco.engine.experimentseries.AbstractParameterVariationExtension;
import org.sopeco.engine.experimentseries.IParameterVariation;

/**
 * Provides a Boolean value variation extension.
 * 
 * @see BooleanVariation
 * 
 * @author Roozbeh Farahbod
 *
 */
public class BooleanVariationExtension extends AbstractParameterVariationExtension {

	/** Holds the name of this extension. */
	public static final String NAME = "Boolean Variation";
	
	public BooleanVariationExtension() { }

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IParameterVariation createExtensionArtifact() {
		return new BooleanVariation(this);
	}

	@Override
	protected void prepareConfigurationParameterMap() {
		// nothing.
	}


}

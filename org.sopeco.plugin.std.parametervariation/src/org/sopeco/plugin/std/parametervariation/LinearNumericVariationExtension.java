package org.sopeco.plugin.std.parametervariation;

import org.sopeco.engine.experimentseries.IParameterVariation;
import org.sopeco.engine.experimentseries.IParameterVariationExtension;

/**
 * Provides linear numeric parameter variation for Double and Integer.
 * 
 * @author Roozbeh Farahbod
 *
 */
public class LinearNumericVariationExtension implements IParameterVariationExtension {

	/** Holds the name of this extension. */
	public static final String NAME = "Linear Numeric Variation";
	
	public LinearNumericVariationExtension() { }

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IParameterVariation createExtensionArtifact() {
		return new LinearNumericVariation(this);
	}

}

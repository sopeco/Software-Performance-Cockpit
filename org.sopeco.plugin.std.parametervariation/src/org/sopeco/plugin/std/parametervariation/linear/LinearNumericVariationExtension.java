package org.sopeco.plugin.std.parametervariation.linear;

import org.sopeco.engine.experimentseries.AbstractParameterVariationExtension;
import org.sopeco.engine.experimentseries.IParameterVariation;

/**
 * Provides linear numeric parameter variation for Double and Integer.
 * 
 * @author Roozbeh Farahbod
 *
 */
public class LinearNumericVariationExtension extends AbstractParameterVariationExtension {

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

	@Override
	protected void prepareConfigurationParameterMap() {
		configParams.put("min", "");
		configParams.put("max", "");
		configParams.put("step", "1");
	}

}

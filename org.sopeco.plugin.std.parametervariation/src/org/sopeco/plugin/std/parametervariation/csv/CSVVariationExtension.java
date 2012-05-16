/**
 * 
 */
package org.sopeco.plugin.std.parametervariation.csv;

import org.sopeco.engine.experimentseries.AbstractParameterVariationExtension;
import org.sopeco.engine.experimentseries.IParameterVariation;

/**
 * Provides comma-separated value variation extension.
 * 
 * @author Roozbeh Farahbod
 *
 */
public class CSVVariationExtension extends AbstractParameterVariationExtension {

	/** Holds the name of this extension. */
	public static final String NAME = "CSV";

	public CSVVariationExtension() { }

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IParameterVariation createExtensionArtifact() {
		return new CSVVariation(this);
	}

	@Override
	protected void prepareConfigurationParameterMap() {
		configParams.put("values", "");
	}

}

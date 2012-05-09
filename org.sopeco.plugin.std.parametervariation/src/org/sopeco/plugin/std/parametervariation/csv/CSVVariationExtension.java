/**
 * 
 */
package org.sopeco.plugin.std.parametervariation.csv;

import org.sopeco.engine.experimentseries.IParameterVariation;
import org.sopeco.engine.experimentseries.IParameterVariationExtension;

/**
 * Provides comma-separated value variation extension.
 * 
 * @author Roozbeh Farahbod
 *
 */
public class CSVVariationExtension implements IParameterVariationExtension {

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

}

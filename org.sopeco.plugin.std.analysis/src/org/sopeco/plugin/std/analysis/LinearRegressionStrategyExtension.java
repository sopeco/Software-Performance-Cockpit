/**
 * 
 */
package org.sopeco.plugin.std.analysis;

import org.sopeco.engine.analysis.AbstractPredictionFunctionStrategyExtension;
import org.sopeco.engine.analysis.IPredictionFunctionStrategy;

/**
 * The extension that provides the linear regression analysis strategy.
 * 
 * @author Dennis Westermann, Roozbeh Farahbod
 *
 */
public class LinearRegressionStrategyExtension extends AbstractPredictionFunctionStrategyExtension {

	public static final String NAME = "Linear Regression";
	
	public LinearRegressionStrategyExtension() {}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IPredictionFunctionStrategy createExtensionArtifact() {
		
		return new LinearRegressionStrategy(this);
	}

	@Override
	protected void prepareConfigurationParameterMap() {
		// TODO Dennis
		
	}

}

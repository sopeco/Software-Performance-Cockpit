/**
 * 
 */
package org.sopeco.plugin.std.analysis;

import org.sopeco.engine.analysis.IPredictionFunctionStrategy;
import org.sopeco.engine.analysis.IPredictionFunctionStrategyExtension;

/**
 * The extension that provides the linear regression analysis strategy.
 * 
 * @author Dennis Westermann
 *
 */
public class LinearRegressionStrategyExtension implements IPredictionFunctionStrategyExtension {

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

}

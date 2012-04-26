/**
 * 
 */
package org.sopeco.plugin.std.analysis;

import org.sopeco.engine.analysis.IAnalysisStrategy;
import org.sopeco.engine.analysis.IAnalysisStrategyExtension;

/**
 * The extension that provides the linear regression analysis strategy.
 * 
 * @author Dennis Westermann
 *
 */
public class LinearRegressionStrategyExtension implements IAnalysisStrategyExtension {

	public static final String NAME = "Linear Regression";
	
	protected LinearRegressionStrategyExtension() {}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IAnalysisStrategy createExtensionArtifact() {
		
		return new LinearRegressionStrategy(this);
	}

}

package org.sopeco.plugin.std.analysis;

import org.sopeco.engine.analysis.AbstractAnalysisStrategyExtension;
import org.sopeco.engine.analysis.ICorrelationStrategy;
import org.sopeco.engine.analysis.ICorrelationStrategyExtension;

/**
 * The extension that provides the linear regression analysis strategy.
 * 
 * @author Dennis Westermann, Roozbeh Farahbod
 *
 */
public class CorrelationAnalysisStrategyExtension extends AbstractAnalysisStrategyExtension implements ICorrelationStrategyExtension {

	public static final String NAME = "Correlation Analysis";
	
	public CorrelationAnalysisStrategyExtension() {}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public ICorrelationStrategy createExtensionArtifact() {
		
		return new CorrelationAnalysisStrategy(this);
	}

	@Override
	protected void prepareConfigurationParameterMap() {
		
	}

}

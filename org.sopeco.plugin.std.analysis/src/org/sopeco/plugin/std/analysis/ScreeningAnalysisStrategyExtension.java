package org.sopeco.plugin.std.analysis;

import org.sopeco.engine.analysis.AbstractAnalysisStrategyExtension;
import org.sopeco.engine.analysis.IScreeningAnalysisStrategy;
import org.sopeco.engine.analysis.IScreeningAnalysisStrategyExtension;

/**
 * The extension that provides the screening analysis strategy.
 * 
 * @author Dennis Westermann
 *
 */
public class ScreeningAnalysisStrategyExtension extends AbstractAnalysisStrategyExtension implements IScreeningAnalysisStrategyExtension {

	public static final String NAME = "Screening Analysis";
	
	public ScreeningAnalysisStrategyExtension() {}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IScreeningAnalysisStrategy createExtensionArtifact() {
		
		return new ScreeningAnalysisStrategy(this);
	}

	@Override
	protected void prepareConfigurationParameterMap() {
		configParams.put(ScreeningAnalysisStrategyConfiguration.INTERACTION_DEPTH, 
				Integer.toString(ScreeningAnalysisStrategyConfiguration.INTERACTION_DEPTH_DEFAULT_VALUE));
		configParams.put(ScreeningAnalysisStrategyConfiguration.RANDOMIZE_RUNS, 
				Boolean.toString(ScreeningAnalysisStrategyConfiguration.RANDOMIZE_RUNS_DEFAULT_VALUE));
	}
	
	
	

}

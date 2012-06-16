package org.sopeco.plugin.std.analysis;

import org.sopeco.engine.analysis.AbstractAnalysisStrategyExtension;
import org.sopeco.engine.analysis.IAnovaStrategy;
import org.sopeco.engine.analysis.IAnovaStrategyExtension;

/**
 * The extension that provides the ANOVA analysis strategy.
 * 
 * @author Dennis Westermann
 *
 */
public class AnovaStrategyExtension extends AbstractAnalysisStrategyExtension implements IAnovaStrategyExtension {

	public static final String NAME = "ANOVA";
	
	public AnovaStrategyExtension() {}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IAnovaStrategy createExtensionArtifact() {
		
		return new AnovaStrategy(this);
	}

	@Override
	protected void prepareConfigurationParameterMap() {
		
	}

}

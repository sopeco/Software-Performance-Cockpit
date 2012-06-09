package org.sopeco.plugin.std.exploration.breakdown.extensions;

import org.sopeco.plugin.std.exploration.breakdown.util.BreakdownConfiguration;
import org.sopeco.plugin.std.exploration.breakdown.util.BreakdownConfiguration.AccuracyDetermination;
import org.sopeco.plugin.std.exploration.breakdown.util.BreakdownConfiguration.DynamicSectorAccuracyScope;

/**
 * The extension that provides the adaptive random breakdown exploration strategy.
 * 
 * @author Dennis Westermann
 *
 */
public class AdaptiveRandomBreakdownExtension extends AbstractBreakdownExplorationExtension {
	public AdaptiveRandomBreakdownExtension() {
		super();
	}
	
	/**
	 * The name of the provided extension artifact.
	 */
	public static final String NAME = BreakdownConfiguration.ADAPTIVE_RANDOM_BREAKDOWN;

	@Override
	protected void prepareConfigurationParameterMap() {
		/* Stop Criteria */
		configParams.put(BreakdownConfiguration.DESIRED_MODEL_ACCURACY, "0.3");
		configParams.put(BreakdownConfiguration.MAX_EXPLORATION_TIME_IN_MIN, "");
		configParams.put(BreakdownConfiguration.MAX_NUMBER_OF_EXPERIMENTS, "");
		
		/* Algorithm tuning */
		configParams.put(BreakdownConfiguration.NUMBER_OF_INITIAL_EXPERIMENTS, "0");
		configParams.put(BreakdownConfiguration.NUMBER_OF_EXPERIMENTS_PER_SECTION_SPLIT, "5");
		configParams.put(BreakdownConfiguration.DIMINUTION_OF_VALIDATIONS, "1.04");
		
		
		/* Accuracy Determination */
		configParams.put(BreakdownConfiguration.ACCURACY_DETERMINATION_METHOD, AccuracyDetermination.DynamicSector.name());
		
		// only for DynamicSector validation
		configParams.put(BreakdownConfiguration.DYNAMIC_SECTOR_ACCURACY_SCOPE, DynamicSectorAccuracyScope.Local.name());
		
		// only for RandomValidationSet validation
		configParams.put(BreakdownConfiguration.SIZE_OF_VALIDATION_SET, "");
		
	}

	@Override
	public String getName() {
		
		return NAME;
	}
}

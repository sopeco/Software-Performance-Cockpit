package org.sopeco.plugin.std.exploration.breakdown.extensions;

import java.util.HashMap;
import java.util.Map;

import org.sopeco.plugin.std.exploration.breakdown.util.BreakdownConfiguration;
import org.sopeco.plugin.std.exploration.breakdown.util.BreakdownConfiguration.AccuracyDetermination;
import org.sopeco.plugin.std.exploration.breakdown.util.BreakdownConfiguration.DynamicSectorAccuracyScope;

/**
 * The extension that provides the adaptive equidistant breakdown exploration strategy.
 * 
 * @author Dennis Westermann
 *
 */
public class AdaptiveEquidistantBreakdownExtension extends AbstractBreakdownExplorationExtension {

	public AdaptiveEquidistantBreakdownExtension() {
		this.NAME = BreakdownConfiguration.EQUIDISTANT_BREAKDOWN;
	}
	
	@Override
	protected void prepareConfigurationParameterMap() {
		
		
		/* Stop Criteria */
		configParams.put(BreakdownConfiguration.DESIRED_MODEL_ACCURACY, "0.3");
		configParams.put(BreakdownConfiguration.MAX_EXPLORATION_TIME_IN_MIN, "");
		configParams.put(BreakdownConfiguration.MAX_NUMBER_OF_EXPERIMENTS, "");
		
		/* Algorithm tuning */
		configParams.put(BreakdownConfiguration.BORDER_MEASUREMENT_DEPTH, "1");
		configParams.put(BreakdownConfiguration.DIMINUTION_OF_VALIDATIONS, "1.04");
		
		
		/* Accuracy Determination */
		configParams.put(BreakdownConfiguration.ACCURACY_DETERMINATION_METHOD, AccuracyDetermination.DynamicSector.name());
		
		// only for DynamicSector validation
		configParams.put(BreakdownConfiguration.DYNAMIC_SECTOR_ACCURACY_SCOPE, DynamicSectorAccuracyScope.Local.name());
		
		// only for RandomValidationSet validation
		configParams.put(BreakdownConfiguration.SIZE_OF_VALIDATION_SET, "");
		
	}
}
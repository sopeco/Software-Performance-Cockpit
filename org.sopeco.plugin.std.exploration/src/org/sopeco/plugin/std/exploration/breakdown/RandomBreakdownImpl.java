package org.sopeco.plugin.std.exploration.breakdown;

import java.util.List;

import org.sopeco.engine.registry.ISoPeCoExtension;
import org.sopeco.persistence.entities.definition.ExplorationStrategy;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.plugin.std.exploration.breakdown.environment.EnvironmentCachedAccess;
import org.sopeco.plugin.std.exploration.breakdown.environment.EnvironmentCachedAccess.MeasurementCacheResult;
import org.sopeco.plugin.std.exploration.breakdown.space.RelativePosition;
import org.sopeco.plugin.std.exploration.breakdown.stop.AbstractStopController;
import org.sopeco.plugin.std.exploration.breakdown.util.BreakdownConfiguration;

/**
 * This class implements the RandomBrakdown strategy. This strategy randomly
 * selects additional experiments from the parameter space until a stop criteria
 * has been reached.
 * 
 * @author Dennis Westermann
 * 
 */
public class RandomBreakdownImpl extends AbstractBreakdownExploration implements IBreakdownExploration {

	

	public RandomBreakdownImpl(ExplorationStrategy strategyConfig, 
			EnvironmentCachedAccess cachedEnvironmentAccess, 
			List<ParameterDefinition> allParameters,
			AbstractStopController stopController, 
			ISoPeCoExtension<?> provider) {
		super(allParameters, cachedEnvironmentAccess, strategyConfig, stopController, provider);
	}

	@Override
	public void doIteration() {
		long numPointsInModelBeforeIteration = cachedEnvironmentAccess.getNumberOfPointsInModel();

		for (int j = 0; j < BreakdownConfiguration.getNumberOfExperimentsPerIteration(strategyConfig); j++) {
			RelativePosition point = createRandomPoint();
			MeasurementCacheResult measuredValue = cachedEnvironmentAccess.measure(point);
			cachedEnvironmentAccess.addToModel(point, measuredValue.value);
		}

		// check if new points have been added to the model
		hasNewModelPoints = numPointsInModelBeforeIteration < cachedEnvironmentAccess.getNumberOfPointsInModel();

	}

	@Override
	public void finishWork() {
		// not used

	}

	@Override
	public void initialise() {
		for (int j = 0; j < BreakdownConfiguration.getNumberOfInitialExperiments(strategyConfig); j++) {
			RelativePosition point = createRandomPoint();
			MeasurementCacheResult measuredValue = cachedEnvironmentAccess.measure(point);
			cachedEnvironmentAccess.addToModel(point, measuredValue.value);
		}

	}

}

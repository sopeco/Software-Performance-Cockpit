package org.sopeco.plugin.std.exploration.breakdown.stop;

import org.sopeco.persistence.entities.definition.ExplorationStrategy;
import org.sopeco.plugin.std.exploration.breakdown.environment.AlgorithmsEnvironment;
import org.sopeco.plugin.std.exploration.breakdown.util.BreakdownConfiguration;
import org.sopeco.plugin.std.exploration.breakdown.util.IMeasurementSeriesAwareable;

/**
 * This class provides the opportunity to create a Stop Controller which can be
 * used to stop an exploration before it reaches it self defined termination.
 * 
 * @author Rouven Krebs, Dennis Westermann
 */
public abstract class StopControllerFactory {
	/**
	 * Create a stop controller deciding if the exploration should be stopped.
	 * The creation is based on the information from the configuration model.
	 * 
	 * @param expSeriesConfig
	 *            the configuration defining the stop conditions
	 * @param info
	 *            where information about the current exploration can get from.
	 * @return the controller deciding if the exploration should be stopped.
	 */
	public static AbstractStopController createStopController(ExplorationStrategy strategyConfig, IMeasurementSeriesAwareable info,
			AlgorithmsEnvironment environment) {
		OneStopController ofsc = new OneStopController();

		
		if (BreakdownConfiguration.getMaxExplorationTimeInMin(strategyConfig) != null) {
			TimeStopCondition tsc = new TimeStopCondition(BreakdownConfiguration.getMaxExplorationTimeInMin(strategyConfig) * 60 * 1000); 
			ofsc.addCondition(tsc);
		}																																

		if(BreakdownConfiguration.getMaxNumberOfExperiments(strategyConfig) != null) {
			NumberOfMeasurementsStopCondition nsc = new NumberOfMeasurementsStopCondition(BreakdownConfiguration.getMaxNumberOfExperiments(strategyConfig), info);
			ofsc.addCondition(nsc);
		}

		return ofsc;
	}

}

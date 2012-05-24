package org.sopeco.plugin.std.analysis;

import org.sopeco.persistence.entities.definition.AnalysisConfiguration;

/**
 * This class holds all the configuration parameter names and default values of
 * the screening analysis strategy. Its methods either return the value configured in
 * the scenario definition of SoPeCo or the default value of a parameter.
 * 
 * @author Dennis Westermann
 * 
 */
public abstract class ScreeningAnalysisStrategyConfiguration {

	
	protected static final String RANDOMIZE_RUNS = "RandomizeRuns";
	protected static final boolean RANDOMIZE_RUNS_DEFAULT_VALUE = false;
	
	protected final static String INTERACTION_DEPTH = "InteractionDepth";
	protected final static int INTERACTION_DEPTH_DEFAULT_VALUE = 1;

	/**
	 * @param strategyConfig
	 *            - the configuration of the analysis strategy provided by
	 *            the users of SoPeCo
	 * @return the value of the configuration parameter
	 */
	public static boolean randomizeRuns(AnalysisConfiguration strategyConfig) {
		String value = strategyConfig.getConfiguration().get(RANDOMIZE_RUNS);
		if (value != null) {
			return Boolean.parseBoolean(value);
		} else {
			return RANDOMIZE_RUNS_DEFAULT_VALUE;
		}
	}
	
	/**
	 * @param strategy
	 *            - the configuration of the analysis strategy provided by
	 *            the users of SoPeCo
	 * @return the value of the configuration parameter
	 */
	public static int getInteractionDepth(AnalysisConfiguration strategy) {
		String value = strategy.getConfiguration().get(INTERACTION_DEPTH);
		if (value != null) {
			return Integer.parseInt(value);
		} else {
			return INTERACTION_DEPTH_DEFAULT_VALUE;
		}
	}


}

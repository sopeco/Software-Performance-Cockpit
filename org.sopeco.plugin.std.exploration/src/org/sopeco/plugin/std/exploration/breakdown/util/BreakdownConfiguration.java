package org.sopeco.plugin.std.exploration.breakdown.util;

import org.sopeco.model.configuration.environment.ParameterDefinition;
import org.sopeco.model.configuration.measurements.ExplorationStrategy;
import org.sopeco.model.util.ScenarioDefinitionUtil;
import org.sopeco.persistence.entities.ExperimentSeriesRun;

/**
 * This class holds all the configuration parameter names and default values of
 * the breakdown explorations. Its methods either return the value configured in
 * the scenario definition of SoPeCo or the default value of a parameter.
 * 
 * @author Dennis Westermann
 * 
 */
public abstract class BreakdownConfiguration {

	
	public static final String RANDOM_BREAKDOWN = "Random Breakdown";
	public static final String EQUIDISTANT_BREAKDOWN = "Equidistant Breakdown";
	public static final String ADAPTIVE_RANDOM_BREAKDOWN = "Adaptive Random Breakdown";
	public static final String ADAPTIVE_EQUIDISTANT_BREAKDOWN = "Adaptive Equidistant Breakdown";

	private static final String MAIN_PERFORMANCE_INDICATOR = "MainPerformanceIndicator";
	
	private static final String DIMINUTION_OF_VALIDATIONS = "DiminutionOfValidations";
	private static final Double DIMINUTION_OF_VALIDATIONS_DEFAULT_VALUE = 1.2;

	private static final String BORDER_MEASUREMENT_DEPTH = "BorderMeasurementDepth";
	private static final Integer BORDER_MEASUREMENT_DEPTH_DEFAULT_VALUE = 1;
	
	private static final String ACCURACY_DETERMINATION_METHOD = "AccuracyDeterminationMethod";
	public enum AccuracyDetermination { RandomValidationSet, DynamicSector }
	private static final AccuracyDetermination ACCURACY_DETERMINATION_METHOD_DEFAULT_VALUE = AccuracyDetermination.RandomValidationSet;
	
	private static final String DYNAMIC_SECTOR_ACCURACY_SCOPE = "DynamicSectorAccuracyScope";
    public enum DynamicSectorAccuracyScope{Local, Global};
	private static final DynamicSectorAccuracyScope DYNAMIC_SECTOR_ACCURACY_SCOPE_DEFAULT_VALUE = DynamicSectorAccuracyScope.Local;
	
	private static final String SIZE_OF_VALIDATION_SET = "SizeOfValidationSet";
	private static final Integer SIZE_OF_VALIDATION_SET_DEFAULT_VALUE = 10;
	
	private static final String DESIRED_MODEL_ACCURACY = "DesiredModelAccuracy";
	private static final Double DESIRED_MODEL_ACCURACY_DEFAULT_VALUE = 0.3;
	
	private static final String MAX_EXPLORATION_TIME_IN_MIN = "MaxExplorationTimeInMin";
	private static final Long MAX_EXPLORATION_TIME_IN_MIN_DEFAULT_VALUE = null;
	
	private static final String MAX_NUMBER_OF_EXPERIMENTS = "MaxNumberOfExperiments";
	private static final Integer MAX_NUMBER_OF_EXPERIMENTS_DEFAULT_VALUE = null;
	
	private static final String NUMBER_OF_EXPERIMENTS_PER_ITERATION = "NumberOfExperimentsPerIteration";
	private static final Integer NUMBER_OF_EXPERIMENTS_PER_ITERATION_DEFAULT_VALUE = null;
	
	private static final String NUMBER_OF_EXPERIMENTS_PER_SECTION_SPLIT = "NumberOfExperimentsPerSectionSplit";
	private static final Integer NUMBER_OF_EXPERIMENTS_PER_SECTION_SPLIT_DEFAULT_VALUE = 5;
	
	private static final String IRRELEVANT_PARAMETER_THRESHOLD = "IrrelevantParameterThreshold";
	private static final Double IRRELEVANT_PARAMETER_THRESHOLD_DEFAULT_VALUE = 0.0;


	/**
	 * @param strategyConfig - the configuration of the exploration strategy provided by the users of SoPeCo
	 * @return the value of the configuration parameter
	 */
	public static Double getDiminutionOfValidation(ExplorationStrategy strategyConfig) {
		Object value = strategyConfig.getConfiguration().get(DIMINUTION_OF_VALIDATIONS);
		if (value != null) {
			return (Double) value;
		} else {
			return DIMINUTION_OF_VALIDATIONS_DEFAULT_VALUE;	
		}
	}
	
	/**
	 * @param strategyConfig - the configuration of the exploration strategy provided by the users of SoPeCo
	 * @return the value of the configuration parameter
	 */
	public static Integer getBorderMeasurementDepth(ExplorationStrategy strategyConfig) {
		Object value = strategyConfig.getConfiguration().get(BORDER_MEASUREMENT_DEPTH);
		if (value != null) {
			return (Integer) value;
		} else {
			return BORDER_MEASUREMENT_DEPTH_DEFAULT_VALUE;
			
		}
	}
	
	/**
	 * @param strategyConfig - the configuration of the exploration strategy provided by the users of SoPeCo
	 * @return the value of the configuration parameter
	 */
	public static AccuracyDetermination getAccuracyDeterminationMethod(ExplorationStrategy strategyConfig) {
		String value = strategyConfig.getConfiguration().get(ACCURACY_DETERMINATION_METHOD);
		if (value != null) {
			if (value.equalsIgnoreCase(AccuracyDetermination.RandomValidationSet.name())) {
				return AccuracyDetermination.RandomValidationSet;
			} else if (value.equalsIgnoreCase(AccuracyDetermination.DynamicSector.name())) {
				return AccuracyDetermination.DynamicSector;
			}
		} else {
			return ACCURACY_DETERMINATION_METHOD_DEFAULT_VALUE;
		}
		
		throw new IllegalArgumentException("Unkown accuracy determination method: " + value);
	}
	
	/**
	 * @param strategyConfig - the configuration of the exploration strategy provided by the users of SoPeCo
	 * @return the value of the configuration parameter
	 */
	public static DynamicSectorAccuracyScope getDynamicSectorAccuracyScope(ExplorationStrategy strategyConfig) {
		String value = strategyConfig.getConfiguration().get(DYNAMIC_SECTOR_ACCURACY_SCOPE);
		if (value != null) {
			if (value.equalsIgnoreCase(DynamicSectorAccuracyScope.Local.name())) {
				return DynamicSectorAccuracyScope.Local;
			} else if (value.equalsIgnoreCase(DynamicSectorAccuracyScope.Global.name())) {
				return DynamicSectorAccuracyScope.Global;
			}
		} else {
			return DYNAMIC_SECTOR_ACCURACY_SCOPE_DEFAULT_VALUE;
		}
		
		throw new IllegalArgumentException("Unkown dynamic sector accuracy scope: " + value);
	}
	
	/**
	 * @param strategyConfig - the configuration of the exploration strategy provided by the users of SoPeCo
	 * @return the value of the configuration parameter
	 */
	public static Integer getSizeOfValidationSet(ExplorationStrategy strategyConfig) {
		Object value = strategyConfig.getConfiguration().get(SIZE_OF_VALIDATION_SET);
		if (value != null) {
			return (Integer) value;
		} else {
			return SIZE_OF_VALIDATION_SET_DEFAULT_VALUE;
			
		}
	}
	
	/**
	 * @param strategyConfig - the configuration of the exploration strategy provided by the users of SoPeCo
	 * @return the value of the configuration parameter
	 */
	public static Double getDesiredModelAccuracy(ExplorationStrategy strategyConfig) {
		Object value = strategyConfig.getConfiguration().get(DESIRED_MODEL_ACCURACY);
		if (value != null) {
			return (Double) value;
		} else {
			return DESIRED_MODEL_ACCURACY_DEFAULT_VALUE;	
		}
	}
	
	/**
	 * @param strategyConfig - the configuration of the exploration strategy provided by the users of SoPeCo
	 * @return the value of the configuration parameter, <code>null</code> if the parameter is not set
	 */
	public static Long getMaxExplorationTimeInMin(ExplorationStrategy strategyConfig) {
		Object value = strategyConfig.getConfiguration().get(MAX_EXPLORATION_TIME_IN_MIN);
		if (value != null) {
			return (Long) value;
		} else {
			return MAX_EXPLORATION_TIME_IN_MIN_DEFAULT_VALUE;
			
		}
	}
	
	/**
	 * @param strategyConfig - the configuration of the exploration strategy provided by the users of SoPeCo
	 * @return the value of the configuration parameter, <code>null</code> if the parameter is not set
	 */
	public static Integer getMaxNumberOfExperiments(ExplorationStrategy strategyConfig) {
		Object value = strategyConfig.getConfiguration().get(MAX_NUMBER_OF_EXPERIMENTS);
		if (value != null) {
			return (Integer) value;
		} else {
			return MAX_NUMBER_OF_EXPERIMENTS_DEFAULT_VALUE;
			
		}
	}
	
	/**
	 * @param strategyConfig - the configuration of the exploration strategy provided by the users of SoPeCo
	 * @return the value of the configuration parameter
	 */
	public static Integer getNumberOfExperimentsPerIteration(ExplorationStrategy strategyConfig) {
		Object value = strategyConfig.getConfiguration().get(NUMBER_OF_EXPERIMENTS_PER_ITERATION);
		if (value != null) {
			return (Integer) value;
		} else {
			return NUMBER_OF_EXPERIMENTS_PER_ITERATION_DEFAULT_VALUE;
			
		}
	}
	
	/**
	 * @param strategyConfig - the configuration of the exploration strategy provided by the users of SoPeCo
	 * @return the value of the configuration parameter
	 */
	public static Integer getNumberOfExperimentsPerSectionSplit(ExplorationStrategy strategyConfig) {
		Object value = strategyConfig.getConfiguration().get(NUMBER_OF_EXPERIMENTS_PER_SECTION_SPLIT);
		if (value != null) {
			return (Integer) value;
		} else {
			return NUMBER_OF_EXPERIMENTS_PER_SECTION_SPLIT_DEFAULT_VALUE;
			
		}
	}
	
	/**
	 * @param strategyConfig - the configuration of the exploration strategy provided by the users of SoPeCo
	 * @return the value of the configuration parameter
	 */
	public static ParameterDefinition getMainPerformanceIndicator(ExplorationStrategy strategyConfig, ExperimentSeriesRun expSeriesRun) {
		String value = strategyConfig.getConfiguration().get(MAIN_PERFORMANCE_INDICATOR);
		if (value != null) {
			return ScenarioDefinitionUtil.getParameterDefinition(value, expSeriesRun.getExperimentSeries().getScenarioInstance().getScenarioDefinition());
		} else {
			throw new IllegalArgumentException("The property " + MAIN_PERFORMANCE_INDICATOR + " must be specified");	
		}
	}
	
	/**
	 * @param strategyConfig - the configuration of the exploration strategy provided by the users of SoPeCo
	 * @return the value of the configuration parameter
	 */
	public static Double getIrrelevantParameterThreshold(ExplorationStrategy strategyConfig) {
		Object value = strategyConfig.getConfiguration().get(IRRELEVANT_PARAMETER_THRESHOLD);
		if (value != null) {
			return (Double) value;
		} else {
			return IRRELEVANT_PARAMETER_THRESHOLD_DEFAULT_VALUE;	
		}
	}

}
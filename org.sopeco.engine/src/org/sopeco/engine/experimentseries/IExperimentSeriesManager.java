package org.sopeco.engine.experimentseries;

import org.sopeco.core.model.configuration.measurements.ExperimentSeriesDefinition;

/**
 * The ExperimentSeriesManager manages the execution of any single experiment
 * series.
 * 
 * For that purpose it creates VariationStrategies for InputParameters, creates
 * and selects ExplorationStrategy plugins, can create Analysis- and
 * ValidationStrategies and runs the ExplorationStrategy (which gets
 * TerminationCondition).
 * 
 * @author D053711
 * 
 */
public interface IExperimentSeriesManager {

	public boolean canRun(ExperimentSeriesDefinition expSeries);

	public void runExperimentSeries(ExperimentSeriesDefinition expSeries);

}

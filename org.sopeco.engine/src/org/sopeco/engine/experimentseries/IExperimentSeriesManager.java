package org.sopeco.engine.experimentseries;

import org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition;
import org.sopeco.persistence.entities.ExperimentSeries;

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

	public void runExperimentSeries(ExperimentSeries expSeries);

}
